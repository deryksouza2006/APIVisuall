package com.visuall.model;

public class Paciente {
    private Integer id;
    private String cpf;        // CORRETO: cpf
    private String senha;      // CORRETO: senha
    private Integer idPaciente; // CORRETO: idPaciente

    // GETTERS E SETTERS CORRETOS:
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpf() {     // CORRIGIDO: getCpf (não getOpf)
        return cpf;
    }

    public void setCpf(String cpf) { // CORRIGIDO: setCpf (não setOpf)
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getIdPaciente() { // CORRIGIDO: getIdPaciente
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) { // CORRIGIDO: setIdPaciente
        this.idPaciente = idPaciente;
    }
}