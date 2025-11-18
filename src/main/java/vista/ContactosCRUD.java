package vista;

import modelo.Contacto;
import dao.ContactoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ContactosCRUD extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private ContactoDAO dao;
    private JTextField txtBuscar;
    
    public ContactosCRUD() {
        dao = new ContactoDAO();
        initComponents();
        cargarDatos();
    }
    
    private void initComponents() {
        setTitle("CRUD Contactos - Clever Cloud");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Panel superior con búsqueda
        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(20);
        panelSuperior.add(txtBuscar);
        JButton btnBuscar = new JButton("Buscar");
        panelSuperior.add(btnBuscar);
        
        // Modelo de tabla
        String[] columnas = {"ID", "Nombre", "Apellidos", "Teléfono", "Dirección", "Correo"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        
        // Botones
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar Lista");
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        
        // Layout principal
        setLayout(new BorderLayout());
        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Listeners
        btnAgregar.addActionListener(e -> mostrarFormulario(false));
        btnEditar.addActionListener(e -> mostrarFormulario(true));
        btnEliminar.addActionListener(e -> eliminarContacto());
        btnActualizar.addActionListener(e -> cargarDatos());
        btnBuscar.addActionListener(e -> buscarContactos());
        
        // Doble click para editar
        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mostrarFormulario(true);
                }
            }
        });
    }
    
    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Contacto> contactos = dao.obtenerContactos();
        for (Contacto c : contactos) {
            modelo.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getApellidos(),
                c.getTelefono(),
                c.getDireccion(),
                c.getCorreo()
            });
        }
    }
    
    private void buscarContactos() {
        // Implementación básica de búsqueda
        String texto = txtBuscar.getText().toLowerCase();
        if (texto.isEmpty()) {
            cargarDatos();
            return;
        }
        
        modelo.setRowCount(0);
        List<Contacto> contactos = dao.obtenerContactos();
        for (Contacto c : contactos) {
            if (c.getNombre().toLowerCase().contains(texto) ||
                c.getApellidos().toLowerCase().contains(texto) ||
                c.getCorreo().toLowerCase().contains(texto)) {
                
                modelo.addRow(new Object[]{
                    c.getId(),
                    c.getNombre(),
                    c.getApellidos(),
                    c.getTelefono(),
                    c.getDireccion(),
                    c.getCorreo()
                });
            }
        }
    }
    
    private void mostrarFormulario(boolean editar) {
        if (editar && tabla.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto para editar");
            return;
        }
        
        JDialog dialogo = new JDialog(this, editar ? "Editar Contacto" : "Nuevo Contacto", true);
        dialogo.setSize(400, 300);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new GridLayout(6, 2, 10, 10));
        
        // Campos del formulario
        JTextField txtNombre = new JTextField();
        JTextField txtApellidos = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtDireccion = new JTextField();
        JTextField txtCorreo = new JTextField();
        
        dialogo.add(new JLabel("Nombre:"));
        dialogo.add(txtNombre);
        dialogo.add(new JLabel("Apellidos:"));
        dialogo.add(txtApellidos);
        dialogo.add(new JLabel("Teléfono:"));
        dialogo.add(txtTelefono);
        dialogo.add(new JLabel("Dirección:"));
        dialogo.add(txtDireccion);
        dialogo.add(new JLabel("Correo:"));
        dialogo.add(txtCorreo);
        
        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        dialogo.add(new JLabel()); // Espacio vacío
        dialogo.add(panelBotones);
        
        // Cargar datos si es edición
        if (editar) {
            int fila = tabla.getSelectedRow();
            txtNombre.setText(modelo.getValueAt(fila, 1).toString());
            txtApellidos.setText(modelo.getValueAt(fila, 2).toString());
            txtTelefono.setText(modelo.getValueAt(fila, 3).toString());
            txtDireccion.setText(modelo.getValueAt(fila, 4).toString());
            txtCorreo.setText(modelo.getValueAt(fila, 5).toString());
        }
        
        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().trim().isEmpty() || txtApellidos.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Nombre y apellidos son obligatorios");
                return;
            }
            
            Contacto contacto = new Contacto(
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                txtTelefono.getText().trim(),
                txtDireccion.getText().trim(),
                txtCorreo.getText().trim()
            );
            
            try {
                if (editar) {
                    contacto.setId((int) modelo.getValueAt(tabla.getSelectedRow(), 0));
                    dao.actualizarContacto(contacto);
                    JOptionPane.showMessageDialog(dialogo, "Contacto actualizado correctamente");
                } else {
                    dao.crearContacto(contacto);
                    JOptionPane.showMessageDialog(dialogo, "Contacto creado correctamente");
                }
                
                cargarDatos();
                dialogo.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error: " + ex.getMessage());
            }
        });
        
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        dialogo.setVisible(true);
    }
    
    private void eliminarContacto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto para eliminar");
            return;
        }
        
        int id = (int) modelo.getValueAt(fila, 0);
        String nombre = modelo.getValueAt(fila, 1) + " " + modelo.getValueAt(fila, 2);
        
        int confirmar = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar el contacto: " + nombre + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                dao.eliminarContacto(id);
                JOptionPane.showMessageDialog(this, "Contacto eliminado correctamente");
                cargarDatos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            // CORRECCIÓN: Usar getSystemLookAndFeelClassName() en lugar de getSystemLookAndFeel()
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            // Si falla, usar el look and feel por defecto
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            new ContactosCRUD().setVisible(true);
        });
    }
}