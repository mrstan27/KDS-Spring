<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Roles</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
</head>
<body>

<div class="registro-card wide">
    <div class="registro-header">
        <h2>Lista de Roles</h2>
        <hr class="header-separator">
    </div>

    <a href="${pageContext.request.contextPath}/roles/nuevo" class="btn-registrar" style="text-decoration: none; display: inline-block; width: auto; margin-bottom: 15px;">+ Nuevo Rol</a>

    <table class="tabla-estilo">
        <thead>
            <tr>
                <th>ID</th>
                <th>Nombre del Rol</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${listaRoles}" var="r">
                <tr>
                    <td>${r.rolId}</td>
                    <td>${r.nombreRol}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/roles/editar/${r.rolId}" style="color: #e67e22; font-weight: bold; margin-right: 10px;">Editar</a>
                        <a href="${pageContext.request.contextPath}/roles/eliminar/${r.rolId}" style="color: #c0392b; font-weight: bold;" onclick="return confirm('¿Eliminar este rol?')">Eliminar</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <div style="text-align: center; margin-top: 20px;">
        <a href="${pageContext.request.contextPath}/usuarios" style="color: #888;">Ir a Usuarios</a>
    </div>
</div>

</body>
</html>