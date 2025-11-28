<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Clientes</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
</head>
<body>

<div class="registro-card wide">

    <div class="registro-header">
        <h2>Lista de Clientes</h2>
        <hr class="header-separator">
    </div>

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
                    <th style="width:140px;">Acciones</th>
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
                            <span class="${c.estado == 'Activo' ? 'badge-activo' : 'badge-inactivo'}">
                                ${c.estado}
                            </span>
                        </td>

                        <td>
                            <a href="${pageContext.request.contextPath}/cliente/editar/${c.clienteId}"
                               class="btn-accion btn-editar">Editar</a>
							<p>
                            <a href="${pageContext.request.contextPath}/cliente/eliminar/${c.clienteId}"
                               class="btn-accion btn-eliminar"
                               onclick="return confirm('¿Eliminar cliente?');">
                                Eliminar
                            </a>
                        </td>

                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</div>

</body>
</html>

