package com.visuall.service;

import com.visuall.model.dto.AuthResponse;
import com.visuall.model.dto.UserDTO;
import com.visuall.model.Usuario;
import com.visuall.dao.UsuarioDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class AuthService {

    @Inject
    UsuarioDAO usuarioDAO;

    public AuthResponse login(String email, String senha) {
        // Buscar usuário por email (ativo)
        Usuario usuario = usuarioDAO.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        // Verificar senha (TODO: implementar hash)
        if (!usuario.getSenha().equals(senha)) {
            throw new RuntimeException("Credenciais inválidas");
        }

        AuthResponse response = new AuthResponse();
        response.setToken(UUID.randomUUID().toString());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(usuario.getId());
        userDTO.setNome(usuario.getNome());
        userDTO.setEmail(usuario.getEmail());

        response.setUser(userDTO);
        return response;
    }

    public AuthResponse register(String nome, String email, String senha) {
        // VERIFICAR se email já existe
        if (usuarioDAO.findByEmail(email) != null) {
            throw new RuntimeException("Email já cadastrado");
        }

        // SALVAR no banco
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senha); // TODO: Hash da senha
        novoUsuario.setAtivo("S");
        novoUsuario.setDataCriacao(java.time.LocalDateTime.now());

        Integer id = usuarioDAO.create(novoUsuario);
        if (id == -1) {
            throw new RuntimeException("Erro ao criar usuário");
        }

        // Buscar usuário criado
        Usuario usuarioSalvo = usuarioDAO.findById(id);

        AuthResponse response = new AuthResponse();
        response.setToken(UUID.randomUUID().toString());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(usuarioSalvo.getId());
        userDTO.setNome(usuarioSalvo.getNome());
        userDTO.setEmail(usuarioSalvo.getEmail());

        response.setUser(userDTO);
        return response;
    }
}