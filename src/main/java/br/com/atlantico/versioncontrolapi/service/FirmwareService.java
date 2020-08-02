package br.com.atlantico.versioncontrolapi.service;

import br.com.atlantico.versioncontrolapi.exception.VersionControlException;
import br.com.atlantico.versioncontrolapi.model.Firmware;
import br.com.atlantico.versioncontrolapi.model.Placa;
import br.com.atlantico.versioncontrolapi.model.Projeto;
import br.com.atlantico.versioncontrolapi.model.TipoVersao;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FirmwareService {

    Firmware salvarFirmware(MultipartFile file, TipoVersao tipoVersao, Projeto projeto, Placa placa) throws VersionControlException, IOException;

    boolean deletarFirmware(Long id) throws VersionControlException;

    Firmware buscarFirmware(Long id);

    InputStream buscarFirmwareDownload(Long id) throws VersionControlException, IOException;

    List<Firmware> listarTodas();

    List<Firmware> buscarFirmwaresPorProjeto(Long id);

    List<Firmware> buscarFirmwaresPorPlaca(Long id);

}
