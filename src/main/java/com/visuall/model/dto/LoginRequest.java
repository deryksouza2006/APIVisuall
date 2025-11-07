package com.visuall.model.dto;

public class LoginRequest {
    private String cpf;
    private String senha;

    // getters e setters
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}