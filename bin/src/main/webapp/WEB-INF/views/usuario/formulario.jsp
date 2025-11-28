<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registro de Usuario</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
</head>
<body>

<div class="registro-card">
    
    <div class="registro-header">
        <h2>${usuario.usuarioId != null ? 'Editar Usuario' : 'Registro de Nuevo Usuario'}</h2>
        <hr class="header-separator">
    </div>

    <form:form action="${pageContext.request.contextPath}/usuarios/guardar" method="post" modelAttribute="usuario">
        
        <form:hidden path="usuarioId"/>
        
        <div class="form-group">
            <label class="form-label">Nombre</label>
            <form:input path="nombre" cssClass="form-input" required="true"/>
        </div>

        <div class="form-group">
            <label class="form-label">Apellido</label>
            <form:input path="apellido" cssClass="form-input" required="true"/>
        </div>

        <div class="form-group">
            <label class="form-label">Correo Electrónico</label>
            <form:input path="email" type="email" cssClass="form-input" required="true"/>
        </div>

        <div class="form-group">
            <label class="form-label">Contraseña</label>
            
            <form:input path="passwordHash" type="password" cssClass="form-input" 
                        required="${usuario.usuarioId == null ? 'true' : 'false'}"/>
            
            <c:if test="${usuario.usuarioId != null}">
                <small style="color: #888; font-size: 12px;">
                    <i class="fa-solid fa-circle-info"></i> Dejar en blanco para mantener la contraseña actual
                </small>
            </c:if>
        </div>
        
        <div class="form-group">
            <label class="form-label">Rol</label>
            <form:select path="rol.rolId" cssClass="form-select" required="true">
                <form:option value="" label="Selecciona un rol"/>
                <form:options items="${listaRoles}" itemValue="rolId" itemLabel="nombreRol"/>
            </form:select>
        </div>

        <div style="display:none;">
            <form:checkbox path="activo" checked="checked"/>
        </div>

        <button type="submit" class="btn-registrar">
            ${usuario.usuarioId != null ? 'Actualizar' : 'Registrarse'}
        </button>
        
    </form:form>
</div>

</body>
</html>