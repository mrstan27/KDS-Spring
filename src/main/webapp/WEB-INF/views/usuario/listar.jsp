<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Usuarios - Kids Made Here</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="registro-card wide">
        
        <div style="margin-bottom: 15px;">
            <a href="${pageContext.request.contextPath}/menu" style="color: #c0392b; text-decoration: none; font-weight: bold;">
                <i class="fa-solid fa-arrow-left"></i> Volver al Menú
            </a>
        </div>

        <div class="registro-header">
            <h2>Gestión de Usuarios</h2>
            <hr class="header-separator">
        </div>
        
        <a href="${pageContext.request.contextPath}/usuarios/nuevo" class="btn-nuevo">
            <i class="fa-solid fa-plus"></i> Nuevo Usuario
        </a>

        <div class="tabla-container">
            <table class="tabla-estilo">
                <thead>
                    <tr>
                        <th style="width: 50px;">#</th> <th>Nombre</th>
                        <th>Apellido</th>
                        <th>Email</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listaUsuarios}" var="u" varStatus="status">
                        <tr>
                            <td style="font-weight: bold; color: #c0392b;">${status.count}</td>
                            
                            <td>${u.nombre}</td>
                            <td>${u.apellido}</td>
                            <td>${u.email}</td>
                            <td><i class="fa-solid fa-user-tag" style="color: #999; margin-right: 5px;"></i> ${u.rol.nombreRol}</td>
                            
                            <td>
                                <c:if test="${u.activo}">
                                    <span class="badge-activo">Activo</span>
                                </c:if>
                                <c:if test="${!u.activo}">
                                    <span class="badge-inactivo">Inactivo</span>
                                </c:if>
                            </td>
                            
                            <td>
                                <a href="${pageContext.request.contextPath}/usuarios/editar/${u.usuarioId}" class="btn-accion btn-editar" title="Editar">
                                    <i class="fa-solid fa-pen"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/usuarios/eliminar/${u.usuarioId}" class="btn-accion btn-eliminar" title="Eliminar" onclick="return confirm('¿Estás seguro de eliminar a ${u.nombre}?')">
                                    <i class="fa-solid fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <c:if test="${empty listaUsuarios}">
                <div style="text-align: center; padding: 20px; color: #777;">
                    No se encontraron usuarios registrados.
                </div>
            </c:if>
        </div>
    </div>

</body>
</html>