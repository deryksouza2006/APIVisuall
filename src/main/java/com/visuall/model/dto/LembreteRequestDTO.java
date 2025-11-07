package com.visuall.model.dto;

import java.time.LocalDate;

public class LembreteRequestDTO {
    private Integer usuarioId;
    private String nomeMedico;
    private String especialidade;
    private LocalDate dataConsulta;
    private String horaConsulta;
    private String localConsulta;
    private String observacoes;

    // Getters e Setters
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getNomeMedico() { return nomeMedico; }
    public void setNomeMedico(String nomeMedico) { this.nomeMedico = nomeMedico; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public LocalDate getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDate dataConsulta) { this.dataConsulta = dataConsulta; }

    public String getHoraConsulta() { return horaConsulta; }
    public void setHoraConsulta(String horaConsulta) { this.horaConsulta = horaConsulta; }

    public String getLocalConsulta() { return localConsulta; }
    public void setLocalConsulta(String localConsulta) { this.localConsulta = localConsulta; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}