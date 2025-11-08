package com.visuall.dao;

import com.visuall.config.DatabaseConfig;
import com.visuall.model.LembretePessoal;
import com.visuall.model.dto.LembreteResponseDTO;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LembreteDAO {

    private static final Logger logger = Logger.getLogger(LembreteDAO.class.getName());

    public LembreteResponseDTO readByIdDTO(Integer lembreteId) {
        String sql = "SELECT id_lembrete, mensagem as titulo, data_envio, enviado, id_paciente, id_consulta FROM LEMBRETES WHERE id_lembrete = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lembreteId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LembreteResponseDTO dto = new LembreteResponseDTO();
                dto.setId(rs.getInt("id_lembrete"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setNomeMedico("Dr. João Silva");
                dto.setEspecialidade("Clínico Geral");

                java.sql.Date dataSql = rs.getDate("data_envio");
                dto.setDataConsulta(dataSql != null ? dataSql.toLocalDate() : LocalDate.now());

                dto.setHoraConsulta(LocalTime.of(12, 0));
                dto.setLocalConsulta("Consultório");
                dto.setObservacoes("Lembrete de consulta");
                dto.setConcluido(!"S".equals(rs.getString("enviado")));
                dto.setUsuarioId(rs.getInt("id_paciente"));

                Timestamp dataCriacao = rs.getTimestamp("data_envio");
                dto.setDataCriacao(dataCriacao != null ? dataCriacao.toString() : LocalDate.now().toString());

                return dto;
            }
            return null;

        } catch (SQLException e) {
            logger.severe("Erro ao buscar lembrete DTO por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar lembrete: " + e.getMessage(), e);
        }
    }

    public List<LembreteResponseDTO> readByPacienteId(Integer pacienteId) {
        List<LembreteResponseDTO> lembretes = new ArrayList<>();
        String sql = "SELECT id_lembrete, mensagem as titulo, data_envio, enviado, id_paciente, id_consulta FROM LEMBRETES WHERE id_paciente = ? ORDER BY data_envio DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LembreteResponseDTO dto = new LembreteResponseDTO();
                dto.setId(rs.getInt("id_lembrete"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setNomeMedico("Dr. João Silva");
                dto.setEspecialidade("Clínico Geral");

                java.sql.Date dataSql = rs.getDate("data_envio");
                dto.setDataConsulta(dataSql != null ? dataSql.toLocalDate() : LocalDate.now());

                dto.setHoraConsulta(LocalTime.of(12, 0));
                dto.setLocalConsulta("Consultório");
                dto.setObservacoes("Lembrete de consulta");
                dto.setConcluido(!"S".equals(rs.getString("enviado")));
                dto.setUsuarioId(rs.getInt("id_paciente"));

                Timestamp dataCriacao = rs.getTimestamp("data_envio");
                dto.setDataCriacao(dataCriacao != null ? dataCriacao.toString() : LocalDate.now().toString());

                lembretes.add(dto);
            }

            logger.info("Encontrados " + lembretes.size() + " lembretes para paciente " + pacienteId);
            return lembretes;

        } catch (SQLException e) {
            logger.severe("Erro SQL ao buscar lembretes: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar lembretes: " + e.getMessage(), e);
        }
    }

    public LembretePessoal readById(Integer lembreteId) {
        String sql = "SELECT * FROM LEMBRETES WHERE id_lembrete = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lembreteId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LembretePessoal lembrete = new LembretePessoal();
                lembrete.setId(rs.getInt("id_lembrete"));
                lembrete.setTitulo(rs.getString("mensagem"));

                java.sql.Date dataSql = rs.getDate("data_envio");
                lembrete.setDataCompromisso(dataSql != null ? dataSql.toLocalDate() : LocalDate.now());

                lembrete.setHoraCompromisso(LocalTime.of(12, 0));
                lembrete.setObservacoes("Lembrete de consulta");
                lembrete.setIdPaciente(rs.getInt("id_paciente"));
                lembrete.setAtivo(!"S".equals(rs.getString("enviado")));

                return lembrete;
            }
            return null;

        } catch (SQLException e) {
            logger.severe("Erro ao buscar lembrete por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar lembrete: " + e.getMessage(), e);
        }
    }

    public Integer create(LembretePessoal lembrete) {
        String sql = "INSERT INTO LEMBRETES (data_envio, enviado, mensagem, id_paciente, id_consulta) VALUES (SYSDATE, 'N', ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_lembrete"})) {

            stmt.setString(1, lembrete.getTitulo());
            stmt.setInt(2, lembrete.getIdPaciente());
            stmt.setInt(3, lembrete.getId() != null ? lembrete.getId() : 1);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys != null && generatedKeys.next()) {
                    int idGerado = generatedKeys.getInt(1);
                    logger.info("Lembrete criado com ID: " + idGerado);
                    return idGerado;
                }
            }

            logger.warning("Nenhuma linha afetada ao criar lembrete");
            return -1;

        } catch (SQLException e) {
            logger.severe("Erro ao criar lembrete: " + e.getMessage());
            throw new RuntimeException("Erro ao criar lembrete: " + e.getMessage(), e);
        }
    }

    public boolean update(LembretePessoal lembrete) {
        String sql = "UPDATE LEMBRETES SET mensagem = ?, enviado = ? WHERE id_lembrete = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lembrete.getTitulo());
            stmt.setString(2, lembrete.getAtivo() ? "N" : "S");
            stmt.setInt(3, lembrete.getId());

            int rowsAffected = stmt.executeUpdate();
            logger.info("Linhas afetadas no update: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.severe("Erro ao atualizar lembrete: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar lembrete: " + e.getMessage(), e);
        }
    }

    public boolean delete(Integer lembreteId, Integer pacienteId) {
        String sql = "DELETE FROM LEMBRETES WHERE id_lembrete = ? AND id_paciente = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lembreteId);
            stmt.setInt(2, pacienteId);

            int rowsAffected = stmt.executeUpdate();
            logger.info("Linhas afetadas no delete: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.severe("Erro ao deletar lembrete: " + e.getMessage());
            throw new RuntimeException("Erro ao deletar lembrete: " + e.getMessage(), e);
        }
    }

    public List<LembreteResponseDTO> buscarAtivosPorPaciente(Integer pacienteId) {
        String sql = "SELECT id_lembrete, mensagem as titulo, data_envio, enviado, id_paciente, id_consulta FROM LEMBRETES WHERE id_paciente = ? AND enviado = 'N' ORDER BY data_envio DESC";

        List<LembreteResponseDTO> lembretes = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LembreteResponseDTO dto = new LembreteResponseDTO();
                dto.setId(rs.getInt("id_lembrete"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setNomeMedico("Dr. João Silva");
                dto.setEspecialidade("Clínico Geral");

                java.sql.Date dataSql = rs.getDate("data_envio");
                dto.setDataConsulta(dataSql != null ? dataSql.toLocalDate() : LocalDate.now());

                dto.setHoraConsulta(LocalTime.of(12, 0));
                dto.setLocalConsulta("Consultório");
                dto.setObservacoes("Lembrete de consulta");
                dto.setConcluido(false);
                dto.setUsuarioId(rs.getInt("id_paciente"));

                Timestamp dataCriacao = rs.getTimestamp("data_envio");
                dto.setDataCriacao(dataCriacao != null ? dataCriacao.toString() : LocalDate.now().toString());

                lembretes.add(dto);
            }

            return lembretes;

        } catch (SQLException e) {
            logger.severe("Erro ao buscar lembretes ativos: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar lembretes ativos: " + e.getMessage(), e);
        }
    }
}