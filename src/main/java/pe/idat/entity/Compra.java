package pe.idat.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compra_id")
    private Integer compraId;

    // Fecha de creación del registro (La fecha del pedido)
    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra = LocalDateTime.now();

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;

    // Estado administrativo del registro (ej: Registrada, Anulada)
    @Column(name = "estado", length = 20)
    private String estado = "Registrada";

    // === NUEVOS CAMPOS PARA SOPORTAR EL FLUJO (Pedido -> Factura -> Guía) ===
    
    // Distingue si es solo una Orden o si ya es una Factura oficial
    @Column(name = "tipo_documento", length = 20, nullable = false)
    private String tipoDocumento = "ORDEN_COMPRA"; // Valores: 'ORDEN_COMPRA', 'FACTURA'
    
    // Guarda el número real del papel (ej: F001-000458)
    @Column(name = "numero_documento_fisico", length = 50)
    private String numeroDocumentoFisico; 
    
    // Controla si la mercadería ya entró al almacén
    @Column(name = "estado_logistico", length = 20)
    private String estadoLogistico = "PENDIENTE"; // Valores: 'PENDIENTE', 'RECIBIDO'

    // ========================================================================

    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // CascadeType.ALL: Al guardar la Compra, se guardan sus Detalles automáticamente
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleCompra> detalle = new ArrayList<>();

    public Compra() {
    }

    // Método auxiliar para mantener la coherencia de la relación bidireccional
    public void agregarDetalle(DetalleCompra item) {
        this.detalle.add(item);
        item.setCompra(this);
    }

    // === GETTERS Y SETTERS ===

    public Integer getCompraId() { return compraId; }
    public void setCompraId(Integer compraId) { this.compraId = compraId; }

    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }

    public Double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(Double montoTotal) { this.montoTotal = montoTotal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumentoFisico() { return numeroDocumentoFisico; }
    public void setNumeroDocumentoFisico(String numeroDocumentoFisico) { this.numeroDocumentoFisico = numeroDocumentoFisico; }

    public String getEstadoLogistico() { return estadoLogistico; }
    public void setEstadoLogistico(String estadoLogistico) { this.estadoLogistico = estadoLogistico; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<DetalleCompra> getDetalle() { return detalle; }
    public void setDetalle(List<DetalleCompra> detalle) { this.detalle = detalle; }
}