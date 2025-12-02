package pe.idat.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_almacen")
public class MovimientoAlmacen {

    // ... (Ids y fechas igual que antes) ...
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Integer movimientoId;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento = LocalDateTime.now();

    @Column(name = "tipo_movimiento", length = 20, nullable = false)
    private String tipoMovimiento;

    @Column(name = "motivo", length = 100)
    private String motivo; 

    // === CAMBIO: NUEVO CAMPO PARA LA GUÍA DE REMISIÓN ===
    @Column(name = "numero_guia", length = 50)
    private String numeroGuia; // Aquí guardaremos el N° de Guía física

    // ... (Relaciones y Getters/Setters igual que antes) ...
    @OneToOne
    @JoinColumn(name = "compra_id_referencia", nullable = true)
    private Compra compraReferencia;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public MovimientoAlmacen() {}

    // ... (Resto de getters y setters) ...
    public String getNumeroGuia() { return numeroGuia; }
    public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }
    
    // ... (Getters y Setters faltantes de ID, fecha, tipo, motivo, etc.)
    public Integer getMovimientoId() { return movimientoId; }
    public void setMovimientoId(Integer movimientoId) { this.movimientoId = movimientoId; }
    public LocalDateTime getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Compra getCompraReferencia() { return compraReferencia; }
    public void setCompraReferencia(Compra compraReferencia) { this.compraReferencia = compraReferencia; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}