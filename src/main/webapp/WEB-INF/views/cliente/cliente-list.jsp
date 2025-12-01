<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Clientes</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<div class="registro-card wide">

    <div class="registro-header" style="display: flex; justify-content: space-between; align-items: center;">
        <h2 style="margin: 0;">Lista de Clientes</h2>
        <a href="${pageContext.request.contextPath}/login/menu" class="btn-nav" style="background-color: #555; font-size: 0.9rem; text-decoration: none; color: white; padding: 8px 15px; border-radius: 5px;">
            <i class="fa-solid fa-arrow-left"></i> Volver al Menú
        </a>
    </div>
    <hr class="header-separator">

    <c:if test="${not empty param.success}">
        <div style="background-color: #d4efdf; color: #27ae60; padding: 10px; margin-bottom: 15px; border-radius: 5px; text-align: center; font-weight: bold;">
            Operación realizada con éxito.
        </div>
    </c:if>

    <div class="tabla-container">
        <table class="tabla-estilo">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre Completo</th>
                    <th>Tipo Doc</th>
                    <th>N° Doc</th>
                    <th>Teléfono</th>
                    <th>Correo</th>
                    <th>Estado</th>
                    <th style="width:160px; text-align: center;">Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="c" items="${listaClientes}">
                    <tr>
                        <td>${c.clienteId}</td>
                        <td>${c.nombre} ${c.apellido}</td>
                        <td>${c.tipoDocumento}</td>
                        <td>${c.numeroDocumento}</td>
                        <td>${c.telefono}</td>
                        <td>${c.correo}</td>

                        <td>
                            <span style="padding: 5px 10px; border-radius: 15px; font-size: 0.8rem; 
                                         background-color: ${c.estado == 'Activo' ? '#d4efdf' : '#fadbd8'};
                                         color: ${c.estado == 'Activo' ? '#27ae60' : '#c0392b'}; font-weight: bold;">
                                ${c.estado}
                            </span>
                        </td>

                        <td style="text-align: center;">
                            <div style="display: flex; gap: 5px; justify-content: center;">
                                <a href="${pageContext.request.contextPath}/cliente/editar/${c.clienteId}"
                                   class="btn-accion" 
                                   style="text-decoration: none; padding: 5px 10px; background: #f39c12; color: white; border-radius: 4px;">
                                   Editar
                                </a>
                                
                                <a href="${pageContext.request.contextPath}/cliente/eliminar/${c.clienteId}"
                                   class="btn-accion" 
                                   style="text-decoration: none; padding: 5px 10px; background: #e74c3c; color: white; border-radius: 4px;"
                                   onclick="return confirm('¿Seguro que deseas eliminar al cliente ${c.nombre}?');">
                                   Eliminar
                                </a>
                            </div>
                        </td>

                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</div>

</body>
</html>
