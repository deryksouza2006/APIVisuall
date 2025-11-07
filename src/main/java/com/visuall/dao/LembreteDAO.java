package com.visuall.dao;

import com.visuall.config.DatabaseConfig;
import com.visuall.model.LembretePessoal;
import com.visuall.model.dto.LembreteResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class LembreteDAO {

    private static final Logger logger = Logger.getLogger(LembreteDAO.class.getName());

    public List<LembreteResponseDTO> readByPacienteId(Integer pacienteId) {
        List<LembreteResponseDTO> lembretes = new ArrayList<>();

        String sql = "SELECT id_lembrete, mensagem as titulo, data_envio, enviado, " +
                "id_paciente, id_consulta " +
                "FROM LEMBRETES " +
                "WHERE id_paciente = ? ORDER BY data_envio DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pacienteId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                try {
                    LembreteResponseDTO dto = new LembreteResponseDTO();
                    dto.setId(rs.getInt("id_lembrete"));
                    dto.setTitulo(rs.getString("titulo"));
                    dto.setNomeMedico("Dr. João Silva"); // Valor padrão
                    dto.setEspecialidade("Clínico Geral"); // Valor padrão

                    // Data
                    java.sql.Date dataSql = rs.getDate("data_envio");
                    if (dataSql != null) {
                        dto.setDataConsulta(dataSql.toLocalDate());
                    } else {
                        dto.setDataConsulta(LocalDate.now());
                    }

                    // Hora
                    dto.setHoraConsulta(LocalTime.of(12, 0)); // Valor padrão

                    dto.setLocalConsulta("Consultório"); // Valor padrão
                    dto.setObservacoes("Lembrete de consulta");
                    dto.setConcluido(!"S".equals(rs.getString("enviado")));
                    dto.setUsuarioId(rs.getInt("id_paciente"));
                    dto.setDataCriacao(rs.getTimestamp("data_envio") != null ?
                            rs.getTimestamp("data_envio").toString() : LocalDate.now().toString());

                    lembretes.add(dto);

                } catch (Exception e) {
                    logger.warning("Erro ao processar linha do resultado: " + e.getMessage());
                }
            }

            logger.info("Encontrados " + lembretes.size() + " lembretes para paciente " + pacienteId);
            return lembretes;

        } catch (SQLException e) {
            logger.severe("Erro SQL ao buscar lembretes: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar lembretes: " + e.getMessage(), e);
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public LembretePessoal readById(Integer lembreteId) {
        String sql = "SELECT * FROM LEMBRETES WHERE id_lembrete = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, lembreteId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                LembretePessoal lembrete = new LembretePessoal();
                lembrete.setId(rs.getInt("id_lembrete"));
                lembrete.setTitulo(rs.getString("mensagem"));

                // Data
                java.sql.Date dataSql = rs.getDate("data_envio");
                if (dataSql != null) {
                    lembrete.setDataCompromisso(dataSql.toLocalDate());
                } else {
                    lembrete.setDataCompromisso(LocalDate.now());
                }

                // Hora padrão
                lembrete.setHoraCompromisso(LocalTime.of(12, 0));

                lembrete.setObservacoes("Lembrete de consulta");
                lembrete.setIdPaciente(rs.getInt("id_paciente"));
                lembrete.setAtivo("S".equals(rs.getString("enviado")));

                return lembrete;
            }
            return null;

        } catch (SQLException e) {
            logger.severe("Erro ao buscar lembrete por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar lembrete: " + e.getMessage(), e);
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public Integer create(LembretePessoal lembrete) {
        String sql = "INSERT INTO LEMBRETES (data_envio, enviado, mensagem, id_paciente, id_consulta) " +
                "VALUES (SYSDATE, 'N', ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql, new String[]{"id_lembrete"});

            stmt.setString(1, lembrete.getTitulo());
            stmt.setInt(2, lembrete.getIdPaciente());

            // ID da consulta (pode ser null)
            if (lembrete.getId() != null) {
                stmt.setInt(3, lembrete.getId());
            } else {
                stmt.setInt(3, 1);
            }

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys != null && generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            return -1;

        } catch (SQLException e) {
            logger.severe("Erro ao criar lembrete: " + e.getMessage());
            throw new RuntimeException("Erro ao criar lembrete: " + e.getMessage(), e);
        } finally {
            closeResources(generatedKeys, stmt, conn);
        }
    }

    public boolean update(LembretePessoal lembrete) {
        String sql = "UPDATE LEMBRETES SET mensagem = ? WHERE id_lembrete = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, lembrete.getTitulo());
            stmt.setInt(2, lembrete.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.severe("Erro ao atualizar lembrete: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar lembrete: " + e.getMessage(), e);
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    public boolean delete(Integer lembreteId, Integer pacienteId) {
        String sql = "DELETE FROM LEMBRETES WHERE id_lembrete = ? AND id_paciente = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, lembreteId);
            stmt.setInt(2, pacienteId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.severe("Erro ao deletar lembrete: " + e.getMessage());
            throw new RuntimeException("Erro ao deletar lembrete: " + e.getMessage(), e);
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    public List<LembreteResponseDTO> buscarAtivosPorPaciente(Integer pacienteId) {
        String sql = "SELECT id_lembrete, mensagem as titulo, data_envio, enviado, " +
                "id_paciente, id_consulta " +
                "FROM LEMBRETES " +
                "WHERE id_paciente = ? AND enviado = 'N' ORDER BY data_envio DESC";

        List<LembreteResponseDTO> lembretes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pacienteId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                LembreteResponseDTO dto = new LembreteResponseDTO();
                dto.setId(rs.getInt("id_lembrete"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setNomeMedico("Dr. João Silva");
                dto.setEspecialidade("Clínico Geral");

                // Data
                java.sql.Date dataSql = rs.getDate("data_envio");
                if (dataSql != null) {
                    dto.setDataConsulta(dataSql.toLocalDate());
                } else {
                    dto.setDataConsulta(LocalDate.now());
                }

                // Hora
                dto.setHoraConsulta(LocalTime.of(12, 0));
                dto.setLocalConsulta("Consultório");
                dto.setObservacoes("Lembrete de consulta");
                dto.setConcluido(false); // Ativos não estão concluídos
                dto.setUsuarioId(rs.getInt("id_paciente"));
                dto.setDataCriacao(rs.getTimestamp("data_envio") != null ?
                        rs.getTimestamp("data_envio").toString() : LocalDate.now().toString());

                lembretes.add(dto);
            }

            return lembretes;

        } catch (SQLException e) {
            logger.severe("Erro ao buscar lembretes ativos: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar lembretes ativos: " + e.getMessage(), e);
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            logger.warning("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}