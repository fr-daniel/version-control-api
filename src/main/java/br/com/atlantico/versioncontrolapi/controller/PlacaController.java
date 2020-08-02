package br.com.atlantico.versioncontrolapi.controller;

import br.com.atlantico.versioncontrolapi.exception.VersionControlException;
import br.com.atlantico.versioncontrolapi.model.Placa;
import br.com.atlantico.versioncontrolapi.repository.PlacaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/placas")
public class PlacaController {

    @Autowired
    private PlacaRepository placaRepository;

    @GetMapping
    private List<Placa> listar() {
        return placaRepository.findAll();
    }


    @DeleteMapping("/{placaId}")
    public ResponseEntity deletarPlaca(@PathVariable("placaId") Placa placa) throws VersionControlException {
        placaRepository.delete(placa);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Placa> cadastrarExame(@RequestBody Placa placa) throws VersionControlException {
        Placa placaSalva = this.placaRepository.save(placa);
        return new ResponseEntity<>(placaSalva, HttpStatus.CREATED);
    }

}
