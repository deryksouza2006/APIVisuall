package com.visuall.service;

import com.visuall.dao.LembreteDAO;
import com.visuall.dao.UsuarioDAO;
import com.visuall.model.LembretePessoal;
import com.visuall.model.Usuario;
import com.visuall.model.dto.LembreteRequestDTO;
import com.visuall.model.dto.LembreteResponseDTO;
import com.visuall.model.dto.EspecialistaDTO;
import com.visuall.model.dto.LocalAtendimentoDTO;
import com.visuall.exception.BusinessException;
import java.time.LocalTime;
import java.util.List;

public class LembreteService {

    LembreteDAO lembreteDAO = new LembreteDAO();
    UsuarioDAO usuarioDAO = new UsuarioDAO();

    public List<LembreteResponseDTO> listarPorUsuario(Integer usuarioId) {
        Usuario usuario = usuarioDAO.findById(usuarioId);
        if (usuario == null) {
            throw new BusinessException("Usuário não encontrado");
        }
        return lembreteDAO.readByPacienteId(usuarioId);
    }

    public List<LembreteResponseDTO> listarAtivosPorUsuario(Integer usuarioId) {
        Usuario usuario = usuarioDAO.findById(usuarioId);
        if (usuario == null) {
            throw new BusinessException("Usuário não encontrado");
        }
        return lembreteDAO.buscarAtivosPorPaciente(usuarioId);
    }

    public LembreteResponseDTO buscarLembretePorId(Integer id) {
        LembreteResponseDTO lembrete = lembreteDAO.readByIdDTO(id);
        if (lembrete == null) {
            throw new BusinessException("Lembrete não encontrado");
        }
        return lembrete;
    }

    public LembreteResponseDTO criarLembrete(LembreteRequestDTO request) {
        if (request.getNomeMedico() == null || request.getNomeMedico().trim().isEmpty()) {
            throw new BusinessException("Nome do médico é obrigatório");
        }
        if (request.getDataConsulta() == null) {
            throw new BusinessException("Data da consulta é obrigatória");
        }

        LembretePessoal lembrete = new LembretePessoal();
        if (request.getTitulo() != null && !request.getTitulo().trim().isEmpty()) {
            lembrete.setTitulo(request.getTitulo());
        } else {
            lembrete.setTitulo("Consulta com " + request.getNomeMedico());
        }
        lembrete.setDataCompromisso(request.getDataConsulta());
        lembrete.setHoraCompromisso(LocalTime.parse(request.getHoraConsulta() + ":00"));
        lembrete.setObservacoes(request.getObservacoes());
        lembrete.setIdPaciente(request.getUsuarioId());
        lembrete.setAtivo(true);

        Integer id = lembreteDAO.create(lembrete);
        if (id == -1) {
            throw new BusinessException("Erro ao criar lembrete");
        }

        return lembreteDAO.readByIdDTO(id);
    }

    public LembreteResponseDTO atualizarLembrete(Integer id, LembreteRequestDTO request) {
        LembretePessoal lembreteExistente = lembreteDAO.readById(id);
        if (lembreteExistente == null) {
            throw new BusinessException("Lembrete não encontrado");
        }

        lembreteExistente.setTitulo("Consulta com " + request.getNomeMedico());
        lembreteExistente.setDataCompromisso(request.getDataConsulta());
        lembreteExistente.setHoraCompromisso(LocalTime.parse(request.getHoraConsulta() + ":00"));
        lembreteExistente.setObservacoes(request.getObservacoes());

        boolean atualizado = lembreteDAO.update(lembreteExistente);
        if (!atualizado) {
            throw new BusinessException("Erro ao atualizar lembrete");
        }

        return lembreteDAO.readByIdDTO(id);
    }

    public void excluirLembrete(Integer id) {
        LembretePessoal lembrete = lembreteDAO.readById(id);
        if (lembrete == null) {
            throw new BusinessException("Lembrete não encontrado");
        }

        boolean excluido = lembreteDAO.delete(id, lembrete.getIdPaciente());
        if (!excluido) {
            throw new BusinessException("Erro ao excluir lembrete");
        }
    }

    public void marcarComoConcluido(Integer id) {
        LembretePessoal lembrete = lembreteDAO.readById(id);
        if (lembrete == null) {
            throw new BusinessException("Lembrete não encontrado");
        }

        lembrete.setAtivo(false);
        boolean atualizado = lembreteDAO.update(lembrete);
        if (!atualizado) {
            throw new BusinessException("Erro ao marcar lembrete como concluído");
        }
    }

    public List<EspecialistaDTO> listarEspecialistas() {
        return List.of(
                criarEspecialista(1, "Dr. João Silva", "Cardiologia"),
                criarEspecialista(2, "Dra. Maria Santos", "Dermatologia"),
                criarEspecialista(3, "Dr. Pedro Costa", "Ortopedia"),
                criarEspecialista(4, "Dra. Ana Oliveira", "Pediatria")
        );
    }

    public List<LocalAtendimentoDTO> listarLocais() {
        return List.of(
                criarLocal(1, "Hospital Central", "Av. Principal, 1000"),
                criarLocal(2, "Clínica Saúde Total", "Rua Saúde, 500"),
                criarLocal(3, "Laboratório Análises", "Praça Exames, 200")
        );
    }

    private EspecialistaDTO criarEspecialista(Integer id, String nome, String especialidade) {
        EspecialistaDTO especialista = new EspecialistaDTO();
        especialista.setId(id);
        especialista.setNome(nome);
        especialista.setEspecialidade(especialidade);
        return especialista;
    }

    private LocalAtendimentoDTO criarLocal(Integer id, String nome, String endereco) {
        LocalAtendimentoDTO local = new LocalAtendimentoDTO();
        local.setId(id);
        local.setNome(nome);
        local.setEndereco(endereco);
        return local;
    }
}