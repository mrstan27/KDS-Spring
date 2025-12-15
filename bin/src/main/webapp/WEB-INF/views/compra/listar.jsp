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

    <div class="registro-card wide" style="width: 90%; max-width: 1300px; margin: 30px auto;">
        
        <div style="margin-bottom: 15px;">
            <a href="${pageContext.request.contextPath}/login/menu" style="color: #c0392b; text-decoration: none; font-weight: bold;">
                <i class="fa-solid fa-arrow-left"></i> Volver al Menú
            </a>
        </div>

        <div class="registro-header">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h2>Seguimiento de Órdenes de Compra</h2>
                    <p class="text-muted">Gestión de flujo: Cotización -> Aprobación -> Recepción</p>
                </div>
                <div class="input-group" style="width: 300px;">
                    <span class="input-group-text"><i class="fa-solid fa-search"></i></span>
                    <input type="text" id="filtroTabla" class="form-control" placeholder="Buscar orden, proveedor...">
                </div>
            </div>
            <hr class="header-separator">
        </div>

        <c:if test="${not empty success}">
            <div class="alert alert-success"><i class="fa-solid fa-check-circle"></i> ${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger"><i class="fa-solid fa-triangle-exclamation"></i> ${error}</div>
        </c:if>
        
        <div class="tabla-container">
            <table class="tabla-estilo table-hover" id="tablaCompras">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Fecha</th>
                        <th>Proveedor</th>
                        <th>Documento</th>
                        <th>Monto</th>
                        <th>Estado Aprobación</th>
                        <th>Estado Logístico</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listaCompras}" var="c">
                        <%-- Ocultamos las Cotizaciones puras, solo mostramos Ordenes y Facturas --%>
                        <c:if test="${c.tipoDocumento != 'COTIZACION'}">
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
                                <c:choose>
                                    <c:when test="${c.estado == 'PENDIENTE'}">
                                        <span class="badge bg-warning text-dark"><i class="fa-solid fa-clock"></i> Por Aprobar</span>
                                    </c:when>
                                    <c:when test="${c.estado == 'APROBADA'}">
                                        <span class="badge bg-success"><i class="fa-solid fa-check"></i> Aprobada</span>
                                    </c:when>
                                    <c:when test="${c.estado == 'RECHAZADA'}">
                                        <span class="badge bg-danger"><i class="fa-solid fa-xmark"></i> Rechazada</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-light text-dark">${c.estado}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            
                            <td>
                                <c:if test="${c.estado == 'APROBADA'}">
                                    <c:if test="${c.estadoLogistico == 'PENDIENTE'}">
                                        <span class="badge bg-info text-dark">Esperando Mercadería</span>
                                    </c:if>
                                    <c:if test="${c.estadoLogistico == 'RECIBIDO'}">
                                        <span class="badge bg-success">En Almacén</span>
                                    </c:if>
                                </c:if>
                                <c:if test="${c.estado != 'APROBADA'}">
                                    <span class="text-muted" style="font-size:0.8em;">Bloqueado</span>
                                </c:if>
                            </td>

                            <td>
                                <div class="d-flex gap-1">
                                    <a href="${pageContext.request.contextPath}/compras/pdf/${c.compraId}" 
                                       class="btn btn-sm btn-danger" title="Descargar PDF">
                                        <i class="fa-solid fa-file-pdf"></i>
                                    </a>

                                    <c:if test="${usuario.rol.nombreRol == 'Administrador' && c.estado == 'PENDIENTE'}">
                                        <a href="${pageContext.request.contextPath}/compras/aprobar/${c.compraId}" 
                                           class="btn btn-sm btn-success" title="Aprobar Orden"
                                           onclick="return confirm('¿Aprobar esta orden de compra?')">
                                            <i class="fa-solid fa-thumbs-up"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/compras/rechazar/${c.compraId}" 
                                           class="btn btn-sm btn-dark" title="Rechazar Orden"
                                           onclick="return confirm('¿Rechazar esta orden? No se podrá recuperar.')">
                                            <i class="fa-solid fa-thumbs-down"></i>
                                        </a>
                                    </c:if>

                                    <c:if test="${c.estado == 'APROBADA'}">
                                        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Almacenero'}">
                                            
                                            <c:if test="${c.tipoDocumento == 'ORDEN_COMPRA'}">
                                                <button type="button" class="btn btn-sm btn-outline-primary" 
                                                        onclick="abrirModalFactura(${c.compraId})" title="Registrar Factura">
                                                    <i class="fa-solid fa-file-invoice-dollar"></i>
                                                </button>
                                            </c:if>

                                            <c:if test="${c.estadoLogistico == 'PENDIENTE'}">
                                                <a href="${pageContext.request.contextPath}/compras/recepcionar/${c.compraId}" 
                                                   class="btn btn-sm btn-success" 
                                                   onclick="return confirm('¿Confirmas el ingreso al almacén? Se actualizará el stock.')" title="Recibir Mercadería">
                                                    <i class="fa-solid fa-box-open"></i>
                                                </a>
                                            </c:if>
                                            
                                        </c:if>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                        </c:if>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <div class="modal fade" id="modalFactura" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header bg-primary text-white">
            <h5 class="modal-title">Registrar Factura</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <form action="${pageContext.request.contextPath}/compras/facturar" method="post">
              <div class="modal-body">
                <input type="hidden" name="idCompra" id="modalIdCompra">
                <div class="mb-3">
                    <label>Número de Factura Física:</label>
                    <input type="text" name="numFactura" class="form-control" 
                           required placeholder="Ej: F001-004532">
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="submit" class="btn btn-primary">Guardar</button>
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

        // --- SCRIPT DE FILTRADO RÁPIDO ---
        document.getElementById('filtroTabla').addEventListener('keyup', function() {
            let texto = this.value.toLowerCase();
            let filas = document.querySelectorAll('#tablaCompras tbody tr');

            filas.forEach(fila => {
                let contenido = fila.innerText.toLowerCase();
                if (contenido.includes(texto)) {
                    fila.style.display = '';
                } else {
                    fila.style.display = 'none';
                }
            });
        });
    </script>
</body>
</html>