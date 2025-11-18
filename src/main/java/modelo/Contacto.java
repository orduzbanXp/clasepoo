package modelo;

public class Contacto {
    private int id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String direccion;
    private String correo;
    
    // Constructores
    public Contacto() {}
    
    public Contacto(String nombre, String apellidos, String telefono, String direccion, String correo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    
    @Override
    public String toString() {
        return nombre + " " + apellidos;
    }
}