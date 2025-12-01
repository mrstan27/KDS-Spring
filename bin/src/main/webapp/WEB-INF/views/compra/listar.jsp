<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %> <!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Compras</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="registro-card wide" style="width: 90%; max-width: 1200px;">
        
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
            <div style="background: #d4efdf; color: #27ae60; padding: 10px; border-radius: 5px; margin-bottom: 15px;">
                <i class="fa-solid fa-check-circle"></i> ${success}
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div style="background: #fadbd8; color: #c0392b; padding: 10px; border-radius: 5px; margin-bottom: 15px;">
                <i class="fa-solid fa-triangle-exclamation"></i> ${error}
            </div>
        </c:if>
        
        <a href="${pageContext.request.contextPath}/compras/nueva" class="btn-nuevo">
            <i class="fa-solid fa-cart-plus"></i> Nueva Orden de Compra
        </a>

        <div class="tabla-container">
            <table class="tabla-estilo">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Fecha</th>
                        <th>Proveedor</th>
                        <th>Responsable</th>
                        <th>Monto Total</th>
                        <th>Estado Logístico</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listaCompras}" var="c">
                        <tr>
                            <td><b>#${c.compraId}</b></td>
                            
                            <td>${c.fechaCompra.toLocalDate()} ${c.fechaCompra.toLocalTime().toString().substring(0,5)}</td>
                            
                            <td>${c.proveedor.razonSocial}</td>
                            <td>${c.usuario.nombre}</td>
                            <td style="font-weight: bold;">S/ ${c.montoTotal}</td>
                            
                            <td>
                                <c:if test="${c.estadoLogistico == 'PENDIENTE'}">
                                    <span style="background: #f1c40f; color: #fff; padding: 4px 8px; border-radius: 4px; font-size: 12px;">
                                        <i class="fa-solid fa-clock"></i> Pendiente
                                    </span>
                                </c:if>
                                <c:if test="${c.estadoLogistico == 'RECIBIDO'}">
                                    <span style="background: #27ae60; color: #fff; padding: 4px 8px; border-radius: 4px; font-size: 12px;">
                                        <i class="fa-solid fa-check-double"></i> En Almacén
                                    </span>
                                </c:if>
                            </td>

                            <td>
                                <c:if test="${c.estadoLogistico == 'PENDIENTE'}">
                                    <a href="${pageContext.request.contextPath}/compras/recepcionar/${c.compraId}" 
                                       class="btn-accion" 
                                       style="background-color: #3498db; color: white; padding: 5px 10px; border-radius: 5px; text-decoration: none;"
                                       onclick="return confirm('¿Confirmas que la mercadería llegó físicamente al almacén? Esto aumentará el stock.')">
                                        <i class="fa-solid fa-box-open"></i> Recibir
                                    </a>
                                </c:if>
                                
                                <c:if test="${c.estadoLogistico == 'RECIBIDO'}">
                                    <span style="color: #aaa; font-size: 13px;">Completado</span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <c:if test="${empty listaCompras}">
                <div style="text-align: center; padding: 20px; color: #777;">
                    No hay compras registradas.
                </div>
            </c:if>
        </div>
    </div>

</body>
</html>