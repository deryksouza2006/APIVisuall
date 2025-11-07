package com.visuall.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class LembretePessoal {
    private Integer id;
    private String titulo;
    private Especialista especialista;
    private LocalAtendimento local;
    private LocalDate dataCompromisso;
    private LocalTime horaCompromisso;
    private String observacoes;
    private boolean ativo;
    private String tipoLembrete;
    private Integer idPaciente;

   
    public LembretePessoal() {}

    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Especialista getEspecialista() { return especialista; }
    public void setEspecialista(Especialista especialista) { this.especialista = especialista; }

    public LocalAtendimento getLocal() { return local; }
    public void setLocal(LocalAtendimento local) { this.local = local; }

    public LocalDate getDataCompromisso() { return dataCompromisso; }
    public void setDataCompromisso(LocalDate dataCompromisso) { this.dataCompromisso = dataCompromisso; }

    public LocalTime getHoraCompromisso() { return horaCompromisso; }
    public void setHoraCompromisso(LocalTime horaCompromisso) { this.horaCompromisso = horaCompromisso; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getTipoLembrete() { return tipoLembrete; }
    public void setTipoLembrete(String tipoLembrete) { this.tipoLembrete = tipoLembrete; }

    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }
}