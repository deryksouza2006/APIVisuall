package com.visuall.service;

import com.visuall.dao.PacienteDAO;
import com.visuall.model.Paciente;
import com.visuall.model.dto.AuthResponse;
import com.visuall.model.dto.UserDTO;
import com.visuall.exception.BusinessException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class AuthService {

    @Inject
    PacienteDAO pacienteDAO;

    public AuthResponse login(String cpf, String senha) { // CORRIGIDO: "semha" → "senha"
        // 1. Validar campos
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new BusinessException("CPF é obrigatório");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new BusinessException("Senha é obrigatória");
        }

        // 2. Buscar paciente no banco
        Paciente paciente = pacienteDAO.findByCpf(cpf);
        if (paciente == null) {
            throw new BusinessException("Paciente não encontrado");
        }

        // 3. Validar senha
        if (!senha.equals(paciente.getSenha())) {
            throw new BusinessException("Senha incorreta");
        }

        // 4. Gerar token
        String token = "visuall-token-" + UUID.randomUUID().toString(); // CORRIGIDO: "visual1" → "visuall"

        // 5. Retornar resposta // CORRIGIDO: "Retomar" → "Retornar"
        AuthResponse response = new AuthResponse();
        response.setToken(token);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(paciente.getId());
        userDTO.setCpf(paciente.getCpf());
        userDTO.setIdPaciente(paciente.getIdPaciente());

        response.setUser(userDTO);
        return response;
    }

    public AuthResponse register(String cpf, String senha, Integer idPaciente) {
        // 1. Validar campos
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new BusinessException("CPF é obrigatório");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new BusinessException("Senha é obrigatória");
        }
        // REMOVER validação do idPaciente aqui

        // 2. Verificar se CPF já existe
        Paciente pacienteExistente = pacienteDAO.findByCpf(cpf);
        if (pacienteExistente != null) {
            throw new BusinessException("CPF já cadastrado");
        }

        // 3. Criar novo paciente
        Paciente novoPaciente = new Paciente();
        novoPaciente.setCpf(cpf);
        novoPaciente.setSenha(senha);
        novoPaciente.setIdPaciente(idPaciente); // Pode ser null ou um valor padrão

        // 4. Salvar no banco
        Integer id = pacienteDAO.create(novoPaciente);
        novoPaciente.setId(id);

        // 5. Gerar token
        String token = "visuall-token-" + UUID.randomUUID().toString();

        // 6. Retornar response
        AuthResponse response = new AuthResponse();
        response.setToken(token);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(novoPaciente.getId());
        userDTO.setCpf(novoPaciente.getCpf());
        userDTO.setIdPaciente(novoPaciente.getIdPaciente());

        response.setUser(userDTO);
        return response;
    }
}