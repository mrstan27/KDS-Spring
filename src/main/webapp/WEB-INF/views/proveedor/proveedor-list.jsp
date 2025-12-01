<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Proveedores</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
</head>
<body>

<div class="registro-card wide">
	
	<div style="margin-bottom: 15px;">
            <a href="${pageContext.request.contextPath}/login/menu" style="color: #c0392b; text-decoration: none; font-weight: bold;">
                <i class="fa-solid fa-arrow-left"></i> Volver al Menú
            </a>
        </div>
	
    <div class="registro-header">
        <h2>Lista de Proveedores</h2>
        <hr class="header-separator">
    </div>

    <a href="${pageContext.request.contextPath}/proveedor/nuevo" class="btn-nuevo">Nuevo Proveedor</a>

    <div class="tabla-container">
        <table class="tabla-estilo">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Razón Social</th>
                    <th>RUC</th>
                    <th>Teléfono</th>
                    <th>Correo</th>
                    <th>Rubro</th>
                    <th>Estado</th>
                    <th style="width: 200px;">Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${listaProveedores}">
                    <tr>
                        <td>${p.proveedorId}</td>
                        <td>${p.razonSocial}</td>
                        <td>${p.ruc}</td>
                        <td>${p.telefono}</td>
                        <td>${p.correo}</td>
                        <td>${p.rubro}</td>

                        <td>
                            <span class="${p.estado == 'Activo' ? 'badge-activo' : 'badge-inactivo'}">
                                ${p.estado}
                            </span>
                        </td>

                        <td>
                            <a href="${pageContext.request.contextPath}/proveedor/editar/${p.proveedorId}"
                               class="btn-accion btn-editar">Editar</a>

                            <a href="${pageContext.request.contextPath}/proveedor/eliminar/${p.proveedorId}"
                               class="btn-accion btn-eliminar"
                               onclick="return confirm('¿Eliminar proveedor?');">
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
