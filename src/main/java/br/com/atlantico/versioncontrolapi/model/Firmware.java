package br.com.atlantico.versioncontrolapi.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "firmware")
public class Firmware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "path_name")
    private String pathName;

    @NotNull
    private String extension;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "tipo_versao")
    @Enumerated(EnumType.STRING)
    private TipoVersao tipoVersao;

    private Integer major;
    private Integer minor;
    private Integer patch;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "id_projeto")
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "id_placa")
    private Placa placa;

    public Firmware() {
    }

    public Firmware(String extension, String contentType, Projeto projeto, Placa placa) {
        this.tipoVersao = TipoVersao.INICIAL;
        this.extension = extension;
        this.contentType = contentType;
        this.projeto = projeto;
        this.placa = placa;
    }

    @PrePersist
    public void onPrePersist() {
        this.tipoVersao.updateVersion(this);
        makePatchName();
        this.createdOn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public TipoVersao getTipoVersao() {
        return tipoVersao;
    }

    public void setTipoVersao(TipoVersao tipoVersao) {
        this.tipoVersao = tipoVersao;
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public Integer getPatch() {
        return patch;
    }

    public void setPatch(Integer patch) {
        this.patch = patch;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getContentType() {
        return contentType;
    }

    public Placa getPlaca() {
        return placa;
    }

    public void setPlaca(Placa placa) {
        this.placa = placa;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPathNameWithextension() {
        return this.pathName + "." + extension;
    }


    public void initVersion() {
        this.major = 0;
        this.minor = 0;
        this.patch = 0;
    }

    public void nextMajor() {
        this.major += 1;
    }

    public void nextMinor() {
        this.minor += 1;
    }

    public void nextPatch() {
        this.patch += 1;
    }

    private void makePatchName() {
        this.pathName = projeto.getNomeNormalize() + "_v" + this.major + "_" + this.minor +"_" + this.patch;
    }
}
