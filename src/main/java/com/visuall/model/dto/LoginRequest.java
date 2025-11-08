package com.visuall.model.dto;

public class RegisterRequest {
    public String nome;
    public String email;
    public String senha;

    // Construtor padrão
    public RegisterRequest() {}

    // Construtor com parâmetros
    public RegisterRequest(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // Getters (se quiser usar)
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}