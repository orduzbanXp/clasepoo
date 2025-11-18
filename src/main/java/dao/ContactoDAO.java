package dao;

import modelo.Contacto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ContactoDAO {
    
    public void crearContacto(Contacto contacto) {
        String sql = "INSERT INTO contactos (nombre, apellidos, telefono, direccion, correo) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, contacto.getNombre());
            pstmt.setString(2, contacto.getApellidos());
            pstmt.setString(3, contacto.getTelefono());
            pstmt.setString(4, contacto.getDireccion());
            pstmt.setString(5, contacto.getCorreo());
            
            pstmt.executeUpdate();
            
            // Obtener el ID generado
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contacto.setId(generatedKeys.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al crear contacto: " + e.getMessage());
        }
    }
    
    public List<Contacto> obtenerContactos() {
        List<Contacto> contactos = new ArrayList<>();
        String sql = "SELECT * FROM contactos ORDER BY nombre, apellidos";
        
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Contacto contacto = new Contacto();
                contacto.setId(rs.getInt("id"));
                contacto.setNombre(rs.getString("nombre"));
                contacto.setApellidos(rs.getString("apellidos"));
                contacto.setTelefono(rs.getString("telefono"));
                contacto.setDireccion(rs.getString("direccion"));
                contacto.setCorreo(rs.getString("correo"));
                
                contactos.add(contacto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar contactos: " + e.getMessage());
        }
        return contactos;
    }
    
    public void actualizarContacto(Contacto contacto) {
        String sql = "UPDATE contactos SET nombre=?, apellidos=?, telefono=?, direccion=?, correo=? WHERE id=?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, contacto.getNombre());
            pstmt.setString(2, contacto.getApellidos());
            pstmt.setString(3, contacto.getTelefono());
            pstmt.setString(4, contacto.getDireccion());
            pstmt.setString(5, contacto.getCorreo());
            pstmt.setInt(6, contacto.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar contacto: " + e.getMessage());
        }
    }
    
    public void eliminarContacto(int id) {
        String sql = "DELETE FROM contactos WHERE id=?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar contacto: " + e.getMessage());
        }
    }
}