package br.com.atlantico.versioncontrolapi.repository;

import br.com.atlantico.versioncontrolapi.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {


}
