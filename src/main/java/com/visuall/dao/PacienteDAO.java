package com.visuall.dao;

import com.visuall.model.Paciente;
import com.visuall.config.DatabaseConfig;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;

@ApplicationScoped
public class PacienteDAO {

    public Paciente findByCpf(String cpf) {
        System.out.println("=== DEBUG: Buscando paciente com CPF: " + cpf + " ===");
        String sql = "SELECT * FROM TB_VA_LOGIN_PACIENTE WHERE cpf = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("=== DEBUG: Conexão obtida, executando query ===");
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("=== DEBUG: Paciente encontrado no banco ===");
                Paciente paciente = new Paciente();
                paciente.setId(rs.getInt("id_login_paciente"));
                paciente.setCpf(rs.getString("cpf"));
                paciente.setSenha(rs.getString("senha"));
                paciente.setIdPaciente(rs.getInt("id_paciente"));
                System.out.println("=== DEBUG: CPF no banco: " + paciente.getCpf() + " ===");
                return paciente;
            } else {
                System.out.println("=== DEBUG: Nenhum paciente encontrado com CPF: " + cpf + " ===");
            }

        } catch (SQLException e) {
            System.out.println("=== DEBUG: Erro SQL: " + e.getMessage() + " ===");
            e.printStackTrace();
        }

        return null;
    }

    public Integer create(Paciente paciente) {
        String sql = "INSERT INTO TB_VA_LOGIN_PACIENTE (cpf, senha, id_paciente) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, paciente.getCpf());
            stmt.setString(2, paciente.getSenha());
            stmt.setInt(3, paciente.getIdPaciente());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}