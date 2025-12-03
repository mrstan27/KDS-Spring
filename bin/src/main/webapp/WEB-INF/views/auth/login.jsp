<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Iniciar Sesión - Kids Made Here</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registro-estilo.css">
    <style>
        body {
            background-image: url('${pageContext.request.contextPath}/images/fondo_tienda.jpg');
            background-size: cover; background-position: center;
            background-repeat: no-repeat; background-attachment: fixed;
            display: flex; justify-content: center; align-items: center;
            min-height: 100vh; margin: 0; font-family: 'Segoe UI', sans-serif;
        }
        .login-card {
            background-color: rgba(255, 255, 255, 0.95);
            width: 350px; padding: 40px; border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.2); text-align: center;
        }
        .registro-header h2 { color: #c0392b; margin-bottom: 10px; }
        .header-separator { margin-bottom: 30px; }
    </style>
</head>
<body>

<div class="login-card">
    <div class="registro-header">
        <h2>Acceso Staff</h2>
        <hr class="header-separator">
    </div>

    <c:if test="${param.error != null}">
        <div style="color: #e74c3c; margin-bottom: 20px; font-weight: bold; background-color: #fadbd8; padding: 10px; border-radius: 5px;">
            Credenciales incorrectas.
        </div>
    </c:if>

    <c:if test="${param.logout != null}">
        <div style="color: #27ae60; margin-bottom: 20px; font-weight: bold; background-color: #d4efdf; padding: 10px; border-radius: 5px;">
            Has cerrado sesión.
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        
        <input type="hidden" name="tipoAcceso" value="admin"/> 

        <div class="form-group">
            <label class="form-label" style="text-align: left;">Correo Electrónico</label>
            <input type="email" name="email" class="form-input" required placeholder="admin@empresa.com"/>
        </div>

        <div class="form-group">
            <label class="form-label" style="text-align: left;">Contraseña</label>
            <input type="password" name="password" class="form-input" required placeholder="******"/>
        </div>

        <button type="submit" class="btn-registrar">Ingresar</button>
    </form>
    
    <div style="margin-top: 25px; font-size: 14px; color: #666;">
        ¿Problemas de acceso? <br> <span style="color: #c0392b; font-weight: bold;">Contacta a TI</span>
    </div>
</div>

</body>
</html>