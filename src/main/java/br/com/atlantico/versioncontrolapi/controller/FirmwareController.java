package br.com.atlantico.versioncontrolapi.controller;

import br.com.atlantico.versioncontrolapi.exception.VersionControlException;
import br.com.atlantico.versioncontrolapi.model.Firmware;
import br.com.atlantico.versioncontrolapi.model.Placa;
import br.com.atlantico.versioncontrolapi.model.Projeto;
import br.com.atlantico.versioncontrolapi.model.TipoVersao;
import br.com.atlantico.versioncontrolapi.service.FirmwareService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/firmware")
public class FirmwareController {

    @Autowired
    private FirmwareService firmwareService;

    @GetMapping
    public ResponseEntity<List<Firmware>> listar() {
        List<Firmware> firmwares = this.firmwareService.listarTodas();
        return new ResponseEntity<>(firmwares, HttpStatus.OK);
    }

    @GetMapping("projeto/{id}")
    public ResponseEntity<List<Firmware>> listarFirmwaresPorProjeto(@PathVariable Long id) {
        List<Firmware> firmwares = this.firmwareService.buscarFirmwaresPorProjeto(id);
        return new ResponseEntity<>(firmwares, HttpStatus.OK);
    }

    @GetMapping("placa/{id}")
    public ResponseEntity<List<Firmware>> listarFirmwaresPorPlaca(@PathVariable Long id) {
        List<Firmware> firmwares = this.firmwareService.buscarFirmwaresPorPlaca(id);
        return new ResponseEntity<>(firmwares, HttpStatus.OK);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Firmware> adicionarFirmware(
            @RequestParam TipoVersao tipoVersao,
            @RequestParam Projeto projeto,
            @RequestParam Placa placa,
            @RequestParam MultipartFile file) throws IOException, VersionControlException {

        Firmware firmware = this.firmwareService.salvarFirmware(file, tipoVersao, projeto, placa);

        return new ResponseEntity<>(firmware, HttpStatus.CREATED);
    }

    @GetMapping("download/{id}")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ResponseEntity<Resource> downloadFirmware(@PathVariable("id") Long id) throws VersionControlException, IOException {
        Firmware firmware = firmwareService.buscarFirmware(id);

        InputStream objectStream = firmwareService.buscarFirmwareDownload(id);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(firmware.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + firmware.getPathName())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "nomeFirmware")
                .header("nomeFirmware", firmware.getPathName())
                .body(new InputStreamResource(objectStream));
    }

    @DeleteMapping("/{idFirmware}")
    public ResponseEntity deletarFirmware(
            @PathVariable Long idFirmware) throws VersionControlException {
            firmwareService.deletarFirmware(idFirmware);
        return new ResponseEntity(HttpStatus.OK);

    }

}
