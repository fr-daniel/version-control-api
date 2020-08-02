package br.com.atlantico.versioncontrolapi.controller;

import br.com.atlantico.versioncontrolapi.model.Projeto;
import br.com.atlantico.versioncontrolapi.repository.ProjetoRepository;
import br.com.atlantico.versioncontrolapi.exception.VersionControlException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoRepository projetoRepository;

    @GetMapping
    private List<Projeto> listar() {
        return projetoRepository.findAll();
    }

    @DeleteMapping("/{projetoId}")
    public ResponseEntity deletarProjeto(@PathVariable("projetoId") Projeto projeto) throws VersionControlException {
        projetoRepository.delete(projeto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Projeto> cadastrarExame(@RequestBody Projeto projeto) throws VersionControlException {
        Projeto projetoSalvo = this.projetoRepository.save(projeto);
        return new ResponseEntity<>(projetoSalvo, HttpStatus.CREATED);
    }


}
