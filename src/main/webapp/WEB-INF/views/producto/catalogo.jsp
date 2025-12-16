<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>KIDS MADE HERE | ${tituloCategoria}</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

    <style>
        /* --- ESTILOS GENERALES --- */
        :root { --kids-red: #E30613; --kids-black: #000000; }
        body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; background-color: #ffffff; color: #000; }
        
        /* NAVBAR */
        .navbar-kids { background-color: #ffffff; padding: 1rem 0; border-bottom: 1px solid #eee; }
        .navbar-brand img { height: 50px; object-fit: contain; }
        .nav-link { color: #555 !important; text-transform: uppercase; font-size: 0.75rem; letter-spacing: 1px; font-weight: 600; margin: 0 5px; transition: color 0.3s; }
        .nav-link:hover { color: var(--kids-red) !important; text-decoration: underline; }
        .text-fire { color: var(--kids-red) !important; font-weight: bold; }
        .btn-icon-action { color: #333; font-size: 1.2rem; margin-left: 15px; text-decoration: none; }
        
        /* ESTILOS CATÁLOGO */
        .category-header { background-color: #f8f9fa; padding: 3rem 0; margin-bottom: 2rem; border-bottom: 2px solid #000; }
        .category-title { font-weight: 900; font-size: 3rem; text-transform: uppercase; letter-spacing: -1px; margin: 0; }

        /* PRODUCT CARD */
        .product-card { border: none; background: transparent; height: 100%; display: flex; flex-direction: column; }
        .img-container { position: relative; width: 100%; padding-top: 125%; overflow: hidden; background-color: #f0f0f0; margin-bottom: 15px; }
        .img-container img { position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover; transition: transform 0.5s ease; }
        .product-card:hover .img-container img { transform: scale(1.05); }

        .product-info { text-align: left; margin-top: auto; }
        .product-name { font-size: 0.9rem; font-weight: 700; text-transform: uppercase; color: #000; text-decoration: none; display: block; margin-bottom: 5px; }
        .product-name:hover { color: var(--kids-red); }
        .product-price { font-size: 1rem; color: #555; font-weight: 400; }

        /* CONTADOR Y BOTÓN */
        .quantity-control { display: flex; align-items: center; border: 1px solid #000; margin-top: 10px; }
        .btn-qty { background: transparent; border: none; padding: 5px 15px; font-size: 1.2rem; cursor: pointer; color: #555; }
        .btn-qty:hover { color: var(--kids-red); }
        .input-qty { width: 40px; text-align: center; border: none; font-weight: bold; -moz-appearance: textfield; }
        .input-qty::-webkit-outer-spin-button, .input-qty::-webkit-inner-spin-button { -webkit-appearance: none; margin: 0; }
        
        .btn-shop { flex-grow: 1; background-color: #000; color: #fff; border: 1px solid #000; padding: 10px; text-transform: uppercase; font-weight: bold; font-size: 0.75rem; transition: all 0.3s; border-radius: 0; }
        .btn-shop:hover { background-color: var(--kids-red); border-color: var(--kids-red); }
        
        .empty-alert { border: 1px dashed #000; padding: 3rem; background-color: #f9f9f9; }
        
        /* FOOTER */
        footer { background-color: #f2f2f2; padding-top: 50px; padding-bottom: 20px; color: #333; font-size: 0.85rem; margin-top: 5rem; }
        .footer-title { font-family: 'Times New Roman', serif; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 20px; font-size: 1rem; color: #000; }
        .footer-link { display: block; color: #666; text-decoration: none; margin-bottom: 10px; text-transform: uppercase; font-size: 0.75rem; }
        .footer-link:hover { color: var(--kids-red); text-decoration: underline; }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <nav class="navbar navbar-expand-lg navbar-kids sticky-top">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/images/logoKIDS.png" alt="KIDS MADE HERE">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav mx-auto align-items-center">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/new-in">NEW IN</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/nite-out">NITE OUT</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/outerwear">OUTERWEAR</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/ropa">ROPA</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/jeans">JEANS</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/polos">POLOS</a></li>
                    <li class="nav-item"><a class="nav-link text-fire" href="${pageContext.request.contextPath}/productos/categoria/black-sunday">BLACK SUNDAY</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/accesorios">ACCESORIOS</a></li>
                </ul>
                <div class="d-flex align-items-center">
                    <c:choose>
                        <c:when test="${pageContext.request.userPrincipal != null}">
                            <a href="${pageContext.request.contextPath}/mi-cuenta/pedidos" class="btn-icon-action" title="Mi Cuenta"><i class="bi bi-person-check-fill"></i></a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login/logincliente" class="btn-icon-action" title="Iniciar Sesión"><i class="bi bi-person"></i></a>
                        </c:otherwise>
                    </c:choose>
                    
                    <a href="${pageContext.request.contextPath}/carrito" class="btn-icon-action position-relative"> 
                        <i class="bi bi-bag"></i> 
                        <c:if test="${not empty sessionScope.carrito and sessionScope.carrito.size() > 0}">
                            <span class="position-absolute top-0 start-100 translate-middle badge rounded-circle bg-danger border border-light" 
                                  style="font-size: 0.6rem; padding: 0.35em 0.5em;">
                                ${sessionScope.carrito.size()} </span>
                        </c:if>
                    </a>
                </div>
            </div>
        </div>
    </nav>

    <div class="category-header text-center">
        <div class="container">
            <h1 class="category-title">${tituloCategoria}</h1>
            <p class="text-muted text-uppercase mt-2 letter-spacing-2">Colección 2025</p>
        </div>
    </div>

    <div class="container">
        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${success} <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error} <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
    </div>

    <div class="container mb-5 flex-grow-1">
        <div class="row g-4">
            
            <c:forEach items="${listaProductos}" var="p" varStatus="status">
                <div class="col-6 col-md-4 col-lg-3">
                    <div class="product-card">
                        
                        <a href="${pageContext.request.contextPath}/productos/detalle/${p.productoId}" style="text-decoration: none; color: inherit;">
                            <div class="img-container">
                                <c:choose>
                                    <c:when test="${not empty p.imagenUrl}">
                                        <img src="${pageContext.request.contextPath}/images/productos/${p.imagenUrl}" 
                                             alt="${p.nombre}"
                                             onerror="this.onerror=null; this.src='https://via.placeholder.com/300x400?text=KIDS';">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="https://via.placeholder.com/300x400?text=SIN+FOTO" alt="Demo">
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${p.stockActual <= 0}">
                                    <div class="position-absolute top-0 end-0 bg-dark text-white px-2 py-1 m-2 small fw-bold">AGOTADO</div>
                                </c:if>
                            </div>

                            <div class="product-info">
                                <span class="product-name">${p.nombre}</span>
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <span class="product-price">S/ ${p.precioVenta}</span>
                                </div>
                            </div>
                        </a>
                        
                        <c:choose>
                            <c:when test="${p.stockActual > 0}">
                                <form action="${pageContext.request.contextPath}/carrito/agregar/${p.productoId}" method="get">
                                    <div class="d-flex">
                                        <div class="quantity-control me-2">
                                            <button type="button" class="btn-qty" onclick="updateQty(this, -1)">-</button>
                                            <input type="number" name="cantidad" value="1" min="1" max="${p.stockActual}" class="input-qty" readonly>
                                            <button type="button" class="btn-qty" onclick="updateQty(this, 1)">+</button>
                                        </div>
                                        <button type="submit" class="btn-shop">AÑADIR</button>
                                    </div>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <button class="btn-shop" style="background-color: #eee; color:#aaa; border:none;" disabled>SIN STOCK</button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>

            <c:if test="${empty listaProductos}">
                <div class="col-12">
                    <div class="empty-alert text-center">
                        <h4 class="fw-bold text-uppercase">Ups, está vacío.</h4>
                        <p class="mb-4">No encontramos productos en <strong>${tituloCategoria}</strong>.</p>
                        <a href="${pageContext.request.contextPath}/" class="btn btn-dark rounded-0 px-4">VER OTRAS CATEGORÍAS</a>
                    </div>
                </div>
            </c:if>

        </div>
    </div>

    <footer>
        <div class="container">
            <div class="row">
                <div class="col-md-3 mb-4">
                    <h5 class="footer-title">ATENCIÓN AL CLIENTE</h5>
                    <a href="#" class="footer-link">TÉRMINOS Y CONDICIONES</a>
                    <a href="#" class="footer-link">PRIVACIDAD</a>
                </div>
                <div class="col-md-3 mb-4">
                    <h5 class="footer-title">COMUNIDAD</h5>
                    <a href="#" class="footer-link">TIENDAS</a>
                </div>
                <div class="col-md-3 mb-4">
                    <h5 class="footer-title">GUÍA DE COMPRAS</h5>
                    <a href="#" class="footer-link">ENVÍOS</a>
                </div>
                <div class="col-md-3 mb-4">
                    <h5 class="footer-title">SOCIAL</h5>
                    <div class="mb-3">
                        <i class="bi bi-instagram"></i> INSTAGRAM
                    </div>
                </div>
            </div>
            <div class="text-center mt-5 pt-3 border-top border-secondary">
                <p class="mb-0">2025 KIDS MADE HERE.</p>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        function updateQty(btn, change) {
            const input = btn.parentElement.querySelector('input');
            let value = parseInt(input.value);
            const min = parseInt(input.min);
            const max = parseInt(input.max);
            
            value += change;
            if (value >= min && value <= max) {
                input.value = value;
            }
        }
    </script>
</body>
</html>