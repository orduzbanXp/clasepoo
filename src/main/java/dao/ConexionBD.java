package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://b8tvkndkd1mk84zmewut-mysql.services.clever-cloud.com:3306/b8tvkndkd1mk84zmewut";
    private static final String USER = "ujmtffxhbogmhhn1";  // CAMBIAR POR TU USUARIO REAL
    private static final String PASSWORD = "pI9OmaFGnAsyztwQEVvA";  // CAMBIAR POR TU CONTRASEÑA REAL
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}