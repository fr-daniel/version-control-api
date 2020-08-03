package br.com.atlantico.versioncontrolapi.service.impl;

import br.com.atlantico.versioncontrolapi.exception.VersionControlException;
import br.com.atlantico.versioncontrolapi.model.Firmware;
import br.com.atlantico.versioncontrolapi.model.Placa;
import br.com.atlantico.versioncontrolapi.model.Projeto;
import br.com.atlantico.versioncontrolapi.model.TipoVersao;
import br.com.atlantico.versioncontrolapi.repository.FirmwareRepository;
import br.com.atlantico.versioncontrolapi.service.FirmwareService;

import br.com.atlantico.versioncontrolapi.util.CryptoUtils;
import br.com.atlantico.versioncontrolapi.util.Messages;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class FirmwareServiceImpl implements FirmwareService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    @Qualifier("bucket")
    private String bucket;

    @Autowired
    private FirmwareRepository firmwareRepository;

    @Autowired
    CryptoUtils cryptoUtils;

    @Override
    public Firmware salvarFirmware(MultipartFile file, TipoVersao tipoVersao, Projeto projeto, Placa placa) throws VersionControlException, IOException {

        String contentType = file.getContentType();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!contentType.equals("application/zip") && !contentType.equals("application/octet-stream"))
            throw new VersionControlException(Messages.ERRO_FIRMWARE_INVALIDA);

        Firmware firmwareBeforeSave = new Firmware(extension, contentType, projeto, placa);

        Optional<Firmware> lastFirmware = firmwareRepository.buscarUltimaFirmwarePorProjeto(projeto.getId());

        if(lastFirmware.isPresent()) {
            firmwareBeforeSave.setTipoVersao(tipoVersao);
            BeanUtils.copyProperties(lastFirmware.get(), firmwareBeforeSave, "id", "pathName", "tipoVersao", "createdOn", "placa");
        }

        Firmware firmware = firmwareRepository.save(firmwareBeforeSave);

        uploadFirmware(file, contentType, firmware);

        return firmware;
    }

    private void uploadFirmware(MultipartFile file, String contentType, Firmware firmware) throws VersionControlException, IOException {
//        byte[] bytesa = cryptoUtils.encrypt("Mary has one cat", file.getBytes());
        InputStream targetStream = new ByteArrayInputStream(file.getBytes());

        try {
            minioClient.putObject(bucket, firmware.getPathNameWithextension(), targetStream, file.getSize(), null, null, contentType);
        } catch (Exception e) {
            throw new VersionControlException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean deletarFirmware(Long id) throws VersionControlException {
        Firmware firmware = firmwareRepository.getOne(id);

        try {
            minioClient.removeObject(bucket, firmware.getId().toString());
        } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException | InvalidKeyException | NoResponseException | XmlPullParserException | ErrorResponseException | InternalException | InvalidArgumentException e) {
            throw new VersionControlException(Messages.ERRO_EXCLUIR_FIRMWARE, HttpStatus.BAD_REQUEST);
        }

        this.firmwareRepository.delete(firmware);
        return true;
    }


    @Override
    public Firmware buscarFirmware(Long id) {
        return firmwareRepository.getOne(id);
    }

    @Override
    public List<Firmware> buscarFirmwaresPorProjeto(Long id) {
        return firmwareRepository.exibirTodasFirmwarePorProjeto(id);
    }

    @Override
    public List<Firmware> buscarFirmwaresPorPlaca(Long id) {
        return firmwareRepository.exibirTodasFirmwarePorPlaca(id);
    }

    @Override
    public List<Firmware> listarTodas() {
        return firmwareRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public InputStream buscarFirmwareDownload(Long id) throws VersionControlException, IOException {

        Firmware firmware = firmwareRepository.getOne(id);
        String idArquivo = firmware.getPathNameWithextension();
        InputStream is = downloadArquivo(idArquivo);

//        byte[] bytes = IOUtils.toByteArray(is);
//
//        byte[] bytesa = cryptoUtils.decrypt("Mary has one cat", bytes);
//        InputStream targetStream = new ByteArrayInputStream(bytes);
        return is;
    }


    //Minio
    private InputStream downloadArquivo(String objectName) throws VersionControlException {
        try {
            return minioClient.getObject(bucket, objectName);
        } catch (Exception e) {
            throw new VersionControlException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


}
