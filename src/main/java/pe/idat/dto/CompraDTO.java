package pe.idat.dto;

import java.math.BigDecimal;
import java.util.List;

public class CompraDTO {
    private Integer proveedorId;
    private BigDecimal total;
    private List<DetalleDTO> items; // La lista de productos

    // Getters y Setters
    public Integer getProveedorId() { return proveedorId; }
    public void setProveedorId(Integer proveedorId) { this.proveedorId = proveedorId; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public List<DetalleDTO> getItems() { return items; }
    public void setItems(List<DetalleDTO> items) { this.items = items; }
}