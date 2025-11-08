package com.visuall.service;

import com.visuall.model.dto.AuthResponse;
import com.visuall.model.dto.UserDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class AuthService {

    public AuthResponse login(String email, String senha) {
        try {
            // TODO: Verificar no banco de dados
            // Por enquanto, aceita qualquer login para teste
            if (email != null && senha != null && senha.length() >= 6) {
                AuthResponse response = new AuthResponse();
                response.setToken(UUID.randomUUID().toString());

                UserDTO user = new UserDTO();
                user.setId(1);
                user.setNome("Usuário Teste");
                user.setEmail(email);

                response.setUser(user);
                return response;
            } else {
                throw new Exception("Credenciais inválidas");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro no login: " + e.getMessage());
        }
    }

    public AuthResponse register(String nome, String email, String senha) {
        try {
            // TODO: Salvar no banco de dados
            // Por enquanto, simula registro bem-sucedido
            if (nome != null && email != null && senha != null && senha.length() >= 6) {
                AuthResponse response = new AuthResponse();
                response.setToken(UUID.randomUUID().toString());

                UserDTO user = new UserDTO();
                user.setId(1); // ID fictício
                user.setNome(nome);
                user.setEmail(email);

                response.setUser(user);
                return response;
            } else {
                throw new Exception("Dados inválidos");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro no registro: " + e.getMessage());
        }
    }
}