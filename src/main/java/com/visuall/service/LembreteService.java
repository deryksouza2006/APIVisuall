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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class LembreteService {

    @Inject
    LembreteDAO lembreteDAO;

    @Inject
    UsuarioDAO usuarioDAO;

    public List<LembreteResponseDTO> listarPorUsuario(Integer usuarioId) {
        // Verificar se usuário existe
        Usuario usuario = usuarioDAO.findById(usuarioId);
        if (usuario == null) {
            throw new BusinessException("Usuário não encontrado");
        }

        return lembreteDAO.readByPacienteId(usuarioId);
    }

    public List<LembreteResponseDTO> listarAtivosPorUsuario(Integer usuarioId) {
        // Verificar se usuário existe
        Usuario usuario = usuarioDAO.findById(usuarioId);
        if (usuario == null) {
            throw new BusinessException("Usuário não encontrado");
        }

        return lembreteDAO.buscarAtivosPorPaciente(usuarioId);
    }

    public LembreteResponseDTO buscarLembretePorId(Integer id) {
        LembretePessoal lembrete = lembreteDAO.readById(id);
        if (lembrete == null) {
            throw new BusinessException("Lembrete não encontrado");
        }

        // Buscar na lista e retornar o primeiro que corresponde ao ID
        return lembreteDAO.readByPacienteId(lembrete.getIdPaciente())
                .stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Lembrete não encontrado"));
    }

    public LembreteResponseDTO criarLembrete(LembreteRequestDTO request) {
        // Validar dados
        if (request.getNomeMedico() == null || request.getNomeMedico().trim().isEmpty()) {
            throw new BusinessException("Nome do médico é obrigatório");
        }
        if (request.getDataConsulta() == null) {
            throw new BusinessException("Data da consulta é obrigatória");
        }

        // Converter DTO para entidade
        LembretePessoal lembrete = new LembretePessoal();
        lembrete.setTitulo("Consulta com " + request.getNomeMedico());
        lembrete.setDataCompromisso(request.getDataConsulta());
        lembrete.setHoraCompromisso(LocalTime.parse(request.getHoraConsulta() + ":00")); // Formatar hora
        lembrete.setObservacoes(request.getObservacoes());
        lembrete.setIdPaciente(request.getUsuarioId());
        lembrete.setAtivo(true);

        // Salvar no banco
        Integer id = lembreteDAO.create(lembrete);
        if (id == -1) {
            throw new BusinessException("Erro ao criar lembrete");
        }

        // Buscar o lembrete criado e converter para ResponseDTO
        return lembreteDAO.readByPacienteId(request.getUsuarioId())
                .stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Erro ao recuperar lembrete criado"));
    }

    public LembreteResponseDTO atualizarLembrete(Integer id, LembreteRequestDTO request) {
        // Verificar se lembrete existe
        LembretePessoal lembreteExistente = lembreteDAO.readById(id);
        if (lembreteExistente == null) {
            throw new BusinessException("Lembrete não encontrado");
        }

        // Atualizar dados
        lembreteExistente.setTitulo("Consulta com " + request.getNomeMedico());
        lembreteExistente.setDataCompromisso(request.getDataConsulta());
        lembreteExistente.setHoraCompromisso(LocalTime.parse(request.getHoraConsulta() + ":00")); // Formatar hora
        lembreteExistente.setObservacoes(request.getObservacoes());

        // Salvar atualização
        boolean atualizado = lembreteDAO.update(lembreteExistente);
        if (!atualizado) {
            throw new BusinessException("Erro ao atualizar lembrete");
        }

        // Buscar o lembrete atualizado
        return lembreteDAO.readByPacienteId(request.getUsuarioId())
                .stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Erro ao recuperar lembrete atualizado"));
    }

    public void excluirLembrete(Integer id) {
        // Verificar se lembrete existe
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
        // Verificar se lembrete existe
        LembretePessoal lembrete = lembreteDAO.readById(id);
        if (lembrete == null) {
            throw new BusinessException("Lembrete não encontrado");
        }

        // Marcar como concluído (inativo)
        lembrete.setAtivo(false);
        boolean atualizado = lembreteDAO.update(lembrete);
        if (!atualizado) {
            throw new BusinessException("Erro ao marcar lembrete como concluído");
        }
    }

    public List<EspecialistaDTO> listarEspecialistas() {
        // Retornar lista fixa de especialistas
        return List.of(
                criarEspecialista(1, "Dr. João Silva", "Cardiologia"),
                criarEspecialista(2, "Dra. Maria Santos", "Dermatologia"),
                criarEspecialista(3, "Dr. Pedro Costa", "Ortopedia"),
                criarEspecialista(4, "Dra. Ana Oliveira", "Pediatria")
        );
    }

    public List<LocalAtendimentoDTO> listarLocais() {
        // Retornar lista fixa de locais
        return List.of(
                criarLocal(1, "Hospital Central", "Av. Principal, 1000"),
                criarLocal(2, "Clínica Saúde Total", "Rua Saúde, 500"),
                criarLocal(3, "Laboratório Análises", "Praça Exames, 200")
        );
    }

    // Métodos auxiliares
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