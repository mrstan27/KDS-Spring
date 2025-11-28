<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Roles</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
</head>
<body>

<div class="registro-card">
    <div class="registro-header">
        <h2>${rol.rolId != null ? 'Editar' : 'Nuevo'} Rol</h2>
        <hr class="header-separator">
    </div>

    <form:form action="${pageContext.request.contextPath}/roles/guardar" method="post" modelAttribute="rol">
        <form:hidden path="rolId"/>
        
        <div class="form-group">
            <label class="form-label">Nombre del Rol</label>
            <form:input path="nombreRol" cssClass="form-input" required="true" placeholder="Ej: Administrador, Cajero..."/>
        </div>

        <button type="submit" class="btn-registrar">Guardar Rol</button>
        
        <div style="text-align: center; margin-top: 15px;">
            <a href="${pageContext.request.contextPath}/roles" style="color: #888; text-decoration: none;">Volver a la lista</a>
        </div>
    </form:form>
</div>

</body>
</html>