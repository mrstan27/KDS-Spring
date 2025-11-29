package pe.idat.dto;

import java.math.BigDecimal;

public class DetalleDTO {
    private Integer productoId;
    private Integer cantidad;
    private BigDecimal precio; // Costo unitario

    // Getters y Setters
    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
}