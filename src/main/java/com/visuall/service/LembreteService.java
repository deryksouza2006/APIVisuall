package com.visuall.service;

import com.visuall.dao.LembreteDAO;
import com.visuall.dao.EspecialistaDAO;
import com.visuall.dao.LocalAtendimentoDAO;
import com.visuall.model.LembretePessoal;
import com.visuall.model.Especialista;
import com.visuall.model.LocalAtendimento;
import com.visuall.model.dto.LembreteRequestDTO;
import com.visuall.model.dto.LembreteResponseDTO;
import com.visuall.model.dto.EspecialistaDTO;
import com.visuall.model.dto.LocalAtendimentoDTO;
import com.visuall.exception.BusinessException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class LembreteService {

    @Inject
    LembreteDAO lembreteDAO;

    @Inject
    EspecialistaDAO especialistaDAO;

    @Inject
    LocalAtendimentoDAO localDAO;

    public List<LembreteResponseDTO> listarLembretesPorPaciente(Integer pacienteId) {
        try {
            return lembreteDAO.readByPacienteId(pacienteId);
        } catch (Exception e) {
            throw new BusinessException("Erro ao listar lembretes: " + e.getMessage());
        }
    }

    public List<LembreteResponseDTO> listarLembretesAtivosPorPaciente(Integer pacienteId) {
        try {
            return lembreteDAO.buscarAtivosPorPaciente(pacienteId);
        } catch (Exception e) {
            throw new BusinessException("Erro ao listar lembretes ativos: " + e.getMessage());
        }
    }

    public LembreteResponseDTO buscarLembretePorId(Integer lembreteId) {
        try {
            LembretePessoal lembrete = lembreteDAO.readById(lembreteId);
            if (lembrete == null) {
                throw new BusinessException("Lembrete não encontrado");
            }
            return convertToResponseDTO(lembrete);
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar lembrete: " + e.getMessage());
        }
    }

    public LembreteResponseDTO criarLembrete(LembreteRequestDTO request) {
        try {
            validarLembreteRequest(request);
            LembretePessoal lembrete = convertToEntity(request);
            Integer id = lembreteDAO.create(lembrete);

            if (id <= 0) {
                throw new BusinessException("Erro ao criar lembrete no banco");
            }

            lembrete.setId(id);
            return convertToResponseDTO(lembrete);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao criar lembrete: " + e.getMessage());
        }
    }

    public LembreteResponseDTO atualizarLembrete(LembreteRequestDTO request) {
        try {
            if (request.getId() == null) {
                throw new BusinessException("ID do lembrete é obrigatório");
            }

            validarLembreteRequest(request);
            LembretePessoal lembreteExistente = lembreteDAO.readById(request.getId());
            if (lembreteExistente == null) {
                throw new BusinessException("Lembrete não encontrado");
            }

            LembretePessoal lembreteAtualizado = convertToEntity(request);
            lembreteAtualizado.setId(request.getId());

            boolean sucesso = lembreteDAO.update(lembreteAtualizado);
            if (!sucesso) {
                throw new BusinessException("Erro ao atualizar lembrete");
            }

            return convertToResponseDTO(lembreteAtualizado);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao atualizar lembrete: " + e.getMessage());
        }
    }

    public void excluirLembrete(Integer lembreteId) {
        try {
            LembretePessoal lembrete = lembreteDAO.readById(lembreteId);
            if (lembrete == null) {
                throw new BusinessException("Lembrete não encontrado");
            }

            boolean sucesso = lembreteDAO.delete(lembreteId, lembrete.getIdPaciente());
            if (!sucesso) {
                throw new BusinessException("Erro ao excluir lembrete");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao excluir lembrete: " + e.getMessage());
        }
    }

    public List<EspecialistaDTO> listarEspecialistas() {
        try {
            return especialistaDAO.findAll();
        } catch (Exception e) {
            throw new BusinessException("Erro ao listar especialistas: " + e.getMessage());
        }
    }

    public List<LocalAtendimentoDTO> listarLocais() {
        try {
            return localDAO.findAll();
        } catch (Exception e) {
            throw new BusinessException("Erro ao listar locais: " + e.getMessage());
        }
    }

    private void validarLembreteRequest(LembreteRequestDTO request) {
        if (request.getTitulo() == null || request.getTitulo().trim().length() < 3) {
            throw new BusinessException("Título deve ter pelo menos 3 caracteres");
        }

        if (request.getTipoLembrete() == null || !isTipoValido(request.getTipoLembrete())) {
            throw new BusinessException("Tipo de lembrete inválido");
        }

        if (request.getDataCompromisso() == null || request.getDataCompromisso().isBefore(LocalDate.now())) {
            throw new BusinessException("Data deve ser futura");
        }

        if (request.getHoraCompromisso() == null) {
            throw new BusinessException("Hora é obrigatória");
        }

        if (request.getIdPaciente() == null || request.getIdPaciente() <= 0) {
            throw new BusinessException("ID do paciente é obrigatório");
        }
    }

    private boolean isTipoValido(String tipo) {
        return tipo != null && (
                tipo.equals("CONSULTA") ||
                        tipo.equals("EXAME") ||
                        tipo.equals("RETORNO") ||
                        tipo.equals("MEDICAMENTO") ||
                        tipo.equals("OUTRO")
        );
    }

    private LembretePessoal convertToEntity(LembreteRequestDTO dto) {
        LembretePessoal lembrete = new LembretePessoal();
        lembrete.setTitulo(dto.getTitulo());
        lembrete.setTipoLembrete(dto.getTipoLembrete());
        lembrete.setDataCompromisso(dto.getDataCompromisso());
        lembrete.setHoraCompromisso(dto.getHoraCompromisso());
        lembrete.setObservacoes(dto.getObservacoes());
        lembrete.setAtivo(true);
        lembrete.setIdPaciente(dto.getIdPaciente());

       
        if (dto.getIdEspecialista() != null) {
            Especialista especialista = especialistaDAO.findById(dto.getIdEspecialista());
            if (especialista == null) {
                throw new BusinessException("Especialista não encontrado");
            }
            lembrete.setEspecialista(especialista);
        }

       
        if (dto.getIdLocal() != null) {
            LocalAtendimento local = localDAO.findById(dto.getIdLocal());
            if (local == null) {
                throw new BusinessException("Local não encontrado");
            }
            lembrete.setLocal(local);
        }

        return lembrete;
    }

    private LembreteResponseDTO convertToResponseDTO(LembretePessoal lembrete) {
        LembreteResponseDTO dto = new LembreteResponseDTO();
        dto.setId(lembrete.getId());
        dto.setTitulo(lembrete.getTitulo());
        dto.setTipoLembrete(lembrete.getTipoLembrete());
        dto.setDataCompromisso(lembrete.getDataCompromisso());
        dto.setHoraCompromisso(lembrete.getHoraCompromisso());
        dto.setObservacoes(lembrete.getObservacoes());
        dto.setAtivo(lembrete.isAtivo());
        dto.setIdPaciente(lembrete.getIdPaciente());

      
        if (lembrete.getEspecialista() != null) {
            dto.setNomeEspecialista(lembrete.getEspecialista().getNome());
            dto.setEspecialidade(lembrete.getEspecialista().getEspecialidade());
        } else {
          
            dto.setNomeEspecialista("Dr. João Silva");
            dto.setEspecialidade("Clínico Geral");
        }

        
        if (lembrete.getLocal() != null) {
            dto.setLocal(lembrete.getLocal().getNome());
        } else {
            
            dto.setLocal("Consultório Médico");
        }

        return dto;
    }
}