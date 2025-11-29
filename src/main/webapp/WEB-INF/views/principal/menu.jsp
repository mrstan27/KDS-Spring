<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Menú Principal</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/menu-estilo.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="dashboard-header">
        <div class="user-info">
            <h2>Hola, ${usuario.nombre} ${usuario.apellido}</h2>
            <span>Rol: <strong>${usuario.rol.nombreRol}</strong></span>
        </div>
        <a href="${pageContext.request.contextPath}/proveedor/lista" class="btn-logout">
            <i class="fa-solid fa-power-off"></i> Proveedores
        </a>
        <a href="${pageContext.request.contextPath}/roles" class="btn-logout">
            <i class="fa-solid fa-power-off"></i> Roles
        </a>
        <a href="${pageContext.request.contextPath}/usuarios" class="btn-logout">
            <i class="fa-solid fa-power-off"></i> Usuarios
        </a>
        <a href="${pageContext.request.contextPath}/cliente/listar" class="btn-logout">
            <i class="fa-solid fa-power-off"></i> Clientes
        </a>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
            <i class="fa-solid fa-power-off"></i> Cerrar Sesión
        </a>
       
    </div>

    <div class="menu-grid">

        <c:if test="${usuario.rol.nombreRol == 'Administrador'}">
            <a href="${pageContext.request.contextPath}/usuarios" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-users-gear"></i></div>
                <div class="card-title">Usuarios</div>
                <div class="card-desc">Gestionar accesos y cuentas</div>
            </a>
            
            <a href="${pageContext.request.contextPath}/roles" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-id-badge"></i></div>
                <div class="card-title">Roles</div>
                <div class="card-desc">Configurar permisos</div>
            </a>
            
             <a href="${pageContext.request.contextPath}/compras" class="card-menu">
    			<div class="card-icon"><i class="fa-solid fa-cart-shopping"></i></div>
    			<div class="card-title">Compras</div>
    			<div class="card-desc">Pedidos y Recepción</div>
			</a>
        </c:if>

        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Almacenero'}">
            <a href="${pageContext.request.contextPath}/productos" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-shirt"></i></div>
                <div class="card-title">Productos</div>
                <div class="card-desc">Catálogo de prendas</div>
            </a>

            <a href="${pageContext.request.contextPath}/proveedores" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-truck"></i></div>
                <div class="card-title">Proveedores</div>
                <div class="card-desc">Gestión de abastecimiento</div>
            </a>
            
            <a href="${pageContext.request.contextPath}/almacen/movimientos" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-boxes-stacked"></i></div>
                <div class="card-title">Movimientos</div>
                <div class="card-desc">Entradas y salidas</div>
            </a>
        </c:if>

        <c:if test="${usuario.rol.nombreRol == 'Administrador' || usuario.rol.nombreRol == 'Vendedor'}">
            <a href="${pageContext.request.contextPath}/ventas/nueva" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-cash-register"></i></div>
                <div class="card-title">Nueva Venta</div>
                <div class="card-desc">Registrar salida de mercadería</div>
            </a>

            <a href="${pageContext.request.contextPath}/clientes" class="card-menu">
                <div class="card-icon"><i class="fa-solid fa-user-group"></i></div>
                <div class="card-title">Clientes</div>
                <div class="card-desc">Cartera de clientes</div>
            </a>
        </c:if>

    </div>

</body>
</html>