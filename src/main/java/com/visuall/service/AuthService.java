package com.visuall.service;

import com.visuall.dao.UsuarioDAO;
import com.visuall.model.Usuario;
import com.visuall.model.dto.AuthResponse;
import com.visuall.model.dto.UserDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class AuthService {

    @Inject
    UsuarioDAO usuarioDAO;

    public AuthResponse login(String email, String senha) {
        // Buscar usuário por email
        Usuario usuario = usuarioDAO.findByEmail(email);

        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        // Verificar senha (em produção, usar BCrypt)
        if (!usuario.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta");
        }

        if (!"S".equals(usuario.getAtivo())) {
            throw new RuntimeException("Usuário inativo");
        }

        // Gerar token (simplificado - em produção usar JWT)
        String token = gerarToken();

        // Criar UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(usuario.getId());
        userDTO.setNome(usuario.getNome());
        userDTO.setEmail(usuario.getEmail());

        // Criar resposta
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUser(userDTO);

        return response;
    }

    public AuthResponse register(String nome, String email, String senha) {
        // Verificar se email já existe
        Usuario usuarioExistente = usuarioDAO.findByEmail(email);
        if (usuarioExistente != null) {
            throw new RuntimeException("Email já cadastrado");
        }

        // Criar novo usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senha); // Em produção, hash da senha
        novoUsuario.setAtivo("S");

        // Salvar no banco
        Integer id = usuarioDAO.create(novoUsuario);

        if (id == -1) {
            throw new RuntimeException("Erro ao criar usuário");
        }

        // Buscar usuário criado
        Usuario usuarioCriado = usuarioDAO.findById(id);

        // Gerar token
        String token = gerarToken();

        // Criar UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(usuarioCriado.getId());
        userDTO.setNome(usuarioCriado.getNome());
        userDTO.setEmail(usuarioCriado.getEmail());

        // Criar resposta
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUser(userDTO);

        return response;
    }

    private String gerarToken() {
        // Token simples para desenvolvimento
        // Em produção, usar JWT com expiração
        return "token-" + UUID.randomUUID().toString();
    }
}