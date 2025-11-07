package com.visuall.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class LembreteRequestDTO {
    private Integer id;
    private String titulo;
    private String tipoLembrete;
    private Integer idEspecialista;
    private Integer idLocal;
    private LocalDate dataCompromisso;
    private LocalTime horaCompromisso;
    private String observacoes;
    private Integer idPaciente;

   
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTipoLembrete() { return tipoLembrete; }
    public void setTipoLembrete(String tipoLembrete) { this.tipoLembrete = tipoLembrete; }

    public Integer getIdEspecialista() { return idEspecialista; }
    public void setIdEspecialista(Integer idEspecialista) { this.idEspecialista = idEspecialista; }

    public Integer getIdLocal() { return idLocal; }
    public void setIdLocal(Integer idLocal) { this.idLocal = idLocal; }

    public LocalDate getDataCompromisso() { return dataCompromisso; }
    public void setDataCompromisso(LocalDate dataCompromisso) { this.dataCompromisso = dataCompromisso; }

    public LocalTime getHoraCompromisso() { return horaCompromisso; }
    public void setHoraCompromisso(LocalTime horaCompromisso) { this.horaCompromisso = horaCompromisso; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }
}