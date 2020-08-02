package br.com.atlantico.versioncontrolapi.repository.firmware;

import br.com.atlantico.versioncontrolapi.model.Firmware;

import java.util.Optional;

public interface FirmwareRepositoryQuery {

    public  Optional<Firmware> buscarUltimaFirmwarePorProjeto(Long idProjeto);

}
