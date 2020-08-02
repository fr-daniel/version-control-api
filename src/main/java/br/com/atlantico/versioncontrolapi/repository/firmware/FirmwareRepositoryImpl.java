package br.com.atlantico.versioncontrolapi.repository.firmware;

import br.com.atlantico.versioncontrolapi.model.Firmware;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class FirmwareRepositoryImpl implements FirmwareRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Optional<Firmware> buscarUltimaFirmwarePorProjeto(Long idProjeto) {
        List<Firmware> firmwares = manager.createQuery("SELECT f FROM Firmware f WHERE f.projeto.id = :idProjeto ORDER BY f.id DESC",
                Firmware.class).setParameter("idProjeto", idProjeto).setMaxResults(1).getResultList();

        if(firmwares.isEmpty())
            return Optional.empty();

        return Optional.of(firmwares.get(0));
    }
}
