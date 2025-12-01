package pe.idat.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Integer clienteId;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 100, nullable = false)
    private String apellido;

    @Column(name = "tipo_documento", length = 20, nullable = false)
    private String tipoDocumento;

    @Column(name = "numero_documento", length = 20, nullable = false, unique = true)
    private String numeroDocumento;

    @Column(name = "correo", length = 100, nullable = false, unique = true)
    private String correo;
    
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "estado", length = 20)
    private String estado = "Activo";

    // Relación con ventas
    //@OneToMany(mappedBy = "cliente")
    //private List<VentaCabecera> ventas;

    public Cliente() {}

    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

	public String getPassword() {return password;}

	public void setPassword(String password) {this.password = password;}

    //public List<VentaCabecera> getVentas() { return ventas; }
    //public void setVentas(List<VentaCabecera> ventas) { this.ventas = ventas; }
}
