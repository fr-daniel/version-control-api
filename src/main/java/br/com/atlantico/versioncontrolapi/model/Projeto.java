package br.com.atlantico.versioncontrolapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import java.text.Normalizer;

@Entity
@Table(name = "projeto")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeNormalize() {
        return  Normalizer
                .normalize(nome, Normalizer.Form.NFD)
                .replaceAll(" ", "_")
                .replaceAll("[^a-zA-Z0-9_]+", "").toLowerCase();
    }
}
