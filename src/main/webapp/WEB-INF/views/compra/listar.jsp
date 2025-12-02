<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Compras</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="registro-card wide" style="width: 90%; max-width: 1200px; margin: 30px auto;">
        
        <div style="margin-bottom: 15px;">
            <a href="${pageContext.request.contextPath}/login/menu" style="color: #c0392b; text-decoration: none; font-weight: bold;">
                <i class="fa-solid fa-arrow-left"></i> Volver al Menú
            </a>
        </div>

        <div class="registro-header">
            <h2>Gestión de Compras y Abastecimiento</h2>
            <hr class="header-separator">
        </div>

        <c:if test="${not empty success}">
            <div class="alert alert-success">
                <i class="fa-solid fa-check-circle"></i> ${success}
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i> ${error}
            </div>
        </c:if>
        
        <a href="${pageContext.request.contextPath}/compras/nueva" class="btn-nuevo">
            <i class="fa-solid fa-cart-plus"></i> Nueva Orden de Compra
        </a>

        <div class="tabla-container">
            <table class="tabla-estilo table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Fecha</th>
                        <th>Proveedor</th>
                        <th>Documento</th>
                        <th>Monto</th>
                        <th>Estado Logístico</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listaCompras}" var="c">
                        <tr>
                            <td><b>#${c.compraId}</b></td>
                            
                            <td>${c.fechaCompra.toLocalDate()}</td>
                            
                            <td>${c.proveedor.razonSocial}</td>
                            
                            <td>
                                <c:if test="${c.tipoDocumento == 'ORDEN_COMPRA'}">
                                    <span class="badge bg-secondary">Orden Compra</span>
                                </c:if>
                                <c:if test="${c.tipoDocumento == 'FACTURA'}">
                                    <span class="badge bg-primary">FACTURA</span><br>
                                    <small>${c.numeroDocumentoFisico}</small>
                                </c:if>
                            </td>
                            
                            <td style="font-weight: bold;">S/ ${c.montoTotal}</td>
                            
                            <td>
                                <c:if test="${c.estadoLogistico == 'PENDIENTE'}">
                                    <span class="badge bg-warning text-dark">
                                        <i class="fa-solid fa-clock"></i> Pendiente
                                    </span>
                                </c:if>
                                <c:if test="${c.estadoLogistico == 'RECIBIDO'}">
                                    <span class="badge bg-success">
                                        <i class="fa-solid fa-check-double"></i> En Almacén
                                    </span>
                                </c:if>
                            </td>

                            <td>
                                <div class="d-flex gap-1">
                                    <c:if test="${c.tipoDocumento == 'ORDEN_COMPRA'}">
                                        <button type="button" class="btn btn-sm btn-outline-primary" 
                                                onclick="abrirModalFactura(${c.compraId})" title="Registrar Factura">
                                            <i class="fa-solid fa-file-invoice-dollar"></i> Factura
                                        </button>
                                    </c:if>

                                    <c:if test="${c.estadoLogistico == 'PENDIENTE'}">
                                        <a href="${pageContext.request.contextPath}/compras/recepcionar/${c.compraId}" 
                                           class="btn btn-sm btn-success" 
                                           onclick="return confirm('¿Confirmas el ingreso al almacén?')" title="Recibir Mercadería">
                                            <i class="fa-solid fa-box-open"></i>
                                        </a>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <div class="modal fade" id="modalFactura" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header bg-primary text-white">
            <h5 class="modal-title">Registrar Factura Física</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form action="${pageContext.request.contextPath}/compras/facturar" method="post">
              <div class="modal-body">
                <input type="hidden" name="idCompra" id="modalIdCompra">
                
                <div class="mb-3">
                    <label class="form-label">Número de Factura (Proveedor)</label>
                    <input type="text" name="numFactura" class="form-control" required placeholder="Ej: F001-004532">
                </div>
                <p class="text-muted small">Al registrar la factura, la orden de compra pasará a estado oficial.</p>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="submit" class="btn btn-primary">Guardar Factura</button>
              </div>
          </form>
        </div>
      </div>
    </div>

    <script>
        function abrirModalFactura(id) {
            document.getElementById("modalIdCompra").value = id;
            var myModal = new bootstrap.Modal(document.getElementById('modalFactura'));
            myModal.show();
        }
    </script>

</body>
</html>