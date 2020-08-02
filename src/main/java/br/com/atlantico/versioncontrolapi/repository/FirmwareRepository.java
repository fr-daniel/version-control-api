package br.com.atlantico.versioncontrolapi.repository;

import br.com.atlantico.versioncontrolapi.model.Firmware;
import br.com.atlantico.versioncontrolapi.repository.firmware.FirmwareRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FirmwareRepository extends JpaRepository<Firmware, Long>, FirmwareRepositoryQuery {

    @Query("FROM Firmware f WHERE f.projeto.id = :id")
    List<Firmware> exibirTodasFirmwarePorProjeto(Long id);

    @Query("FROM Firmware f WHERE f.placa.id = :id")
    List<Firmware> exibirTodasFirmwarePorPlaca(Long id);

}
