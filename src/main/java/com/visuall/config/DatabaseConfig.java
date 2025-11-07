package com.visuall.config;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println(" Driver Oracle carregado com sucesso!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(" Driver Oracle não encontrado", e);
        }
    }

    public static Connection getConnection() {
        try {
            
            if (URL == null || USER == null || PASSWORD == null) {
                throw new SQLException(" Variáveis de ambiente do banco de dados não configuradas.");
            }
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" Conexão com Oracle estabelecida!");
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(" Erro na conexão com Oracle: " + e.getMessage(), e);
        }
    }
}