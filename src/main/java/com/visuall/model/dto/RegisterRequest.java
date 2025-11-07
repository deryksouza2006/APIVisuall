package com.visuall.model.dto;

public class RegisterRequest {
    private String cpf;
    private String senha;
    private Integer idPaciente; // ID do paciente na tabela principal

    // getters e setters
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }
}