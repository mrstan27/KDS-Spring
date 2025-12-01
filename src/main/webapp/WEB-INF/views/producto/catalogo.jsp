<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        .btn-icon-action:hover { color: var(--kids-red); }

        /* ESTILOS CATÁLOGO */
        .category-header { background-color: #f8f9fa; padding: 3rem 0; margin-bottom: 2rem; border-bottom: 2px solid #000; }
        .category-title { font-weight: 900; font-size: 3rem; text-transform: uppercase; letter-spacing: -1px; margin: 0; }

        /* PRODUCT CARD */
        .product-card { border: none; background: transparent; transition: transform 0.3s ease; }
        .img-container { position: relative; width: 100%; padding-top: 125%; overflow: hidden; background-color: #f0f0f0; margin-bottom: 15px; }
        
        /* IMAGEN DEL PRODUCTO */
        .img-container img { 
            position: absolute; top: 0; left: 0; width: 100%; height: 100%; 
            object-fit: cover; /* Ajusta la foto al marco */
            transition: transform 0.5s ease; 
        }
        .product-card:hover .img-container img { transform: scale(1.05); }

        .product-info { text-align: left; }
        .product-name { font-size: 0.9rem; font-weight: 700; text-transform: uppercase; color: #000; text-decoration: none; display: block; margin-bottom: 5px; }
        .product-price { font-size: 1rem; color: #555; font-weight: 400; }

        .btn-shop { display: block; width: 100%; background-color: transparent; color: #000; border: 1px solid #000; padding: 10px; text-transform: uppercase; font-weight: bold; font-size: 0.75rem; border-radius: 0; margin-top: 10px; transition: all 0.3s; text-decoration: none; text-align: center; }
        .btn-shop:hover { background-color: var(--kids-red); color: #fff; border-color: var(--kids-red); }

        .empty-alert { border: 1px dashed #000; padding: 3rem; background-color: #f9f9f9; }
        
        /* FOOTER */
        footer { background-color: #f2f2f2; padding-top: 50px; padding-bottom: 20px; color: #333; font-size: 0.85rem; margin-top: 5rem; }
        .footer-title { font-family: 'Times New Roman', serif; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 20px; font-size: 1rem; color: #000; }
        .footer-link { display: block; color: #666; text-decoration: none; margin-bottom: 10px; text-transform: uppercase; font-size: 0.75rem; transition: color 0.3s; }
        .footer-link:hover { color: var(--kids-red); text-decoration: underline; }
        .newsletter-box { border-bottom: 1px solid #000; display: inline-block; color: #000; font-weight: bold; text-decoration: none; }
    </style>
</head>
<body>

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
                    <a href="${pageContext.request.contextPath}/login/logincliente" class="btn-icon-action" title="Cliente"><i class="bi bi-person"></i></a>
                    <a href="${pageContext.request.contextPath}/login/loginusuario" class="btn-icon-action" title="Staff"><i class="bi bi-person-lock"></i></a>
                    <a href="#" class="btn-icon-action"><i class="bi bi-bag"></i></a>
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

    <div class="container mb-5">
        <div class="row g-4">
            
            <c:forEach items="${listaProductos}" var="p" varStatus="status">
                <div class="col-6 col-md-4 col-lg-3">
                    <div class="product-card">
                        
                        <div class="img-container">
                            <c:choose>
                                <%-- CASO 1: Si tiene imagen en BD (ej: 'jeans/jean1.jpg') --%>
                                <c:when test="${not empty p.imagenUrl}">
                                    <img src="${pageContext.request.contextPath}/images/productos/${p.imagenUrl}" 
                                         alt="${p.nombre}"
                                         onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/productos/pantalon1.jpg';">
                                </c:when>

                                <%-- CASO 2: Si NO tiene imagen, usamos el demo par/impar --%>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${status.count % 2 == 0}">
                                            <img src="${pageContext.request.contextPath}/images/productos/pantalon1.jpg" alt="Demo">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/images/productos/pantalonbeige.jpg" alt="Demo">
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="product-info">
                            <a href="#" class="product-name">${p.nombre}</a>
                            <div class="d-flex justify-content-between align-items-center">
                                <span class="product-price">S/ ${p.precioVenta}</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/compra/carrito/agregar/${p.productoId}" class="btn-shop">
                                AÑADIR AL CARRITO
                            </a>
                        </div>
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
                    <a href="#" class="footer-link">CAMBIOS Y DEVOLUCIONES</a>
                    <a href="#" class="footer-link">PRIVACIDAD</a>
                    <a href="#" class="footer-link">PROMOCIONES</a>
                    <div class="mt-3">
                        <img src="${pageContext.request.contextPath}/images/libro_reclamaciones.png" alt="Libro de Reclamaciones" style="width: 120px; border: 1px solid #ccc;">
                    </div>
                </div>
                <div class="col-md-3 mb-4">
                    <h5 class="footer-title">COMUNIDAD</h5>
                    <a href="#" class="footer-link">REGALA UNA GIFT CARD!</a>
                    <a href="#" class="footer-link">TIENDAS</a>
                </div>
                <div class="col-md-3 mb-4">
                    <h5 class="footer-title">GUÍA DE COMPRAS</h5>
                    <a href="#" class="footer-link">¿CÓMO COMPRAR?</a>
                    <a href="#" class="footer-link">¿CÓMO PAGAR CON YAPE?</a>
                    <a href="#" class="footer-link">RETIRO EN TIENDA</a>
                    <a href="#" class="footer-link">ENVÍOS</a>
                </div>
                <div class="col-md-3 mb-4">
                    <h5 class="footer-title">SOCIAL</h5>
                    <div class="mb-3" style="font-size: 0.8rem;">
                        <a href="#" class="text-decoration-none text-dark d-block mb-1"><i class="bi bi-facebook"></i> FACEBOOK</a>
                        <a href="#" class="text-decoration-none text-dark d-block mb-1"><i class="bi bi-instagram"></i> INSTAGRAM</a>
                        <a href="#" class="text-decoration-none text-dark d-block mb-1"><i class="bi bi-tiktok"></i> TIKTOK</a>
                    </div>
                    <h5 class="footer-title mt-4">SUSCRÍBETE AL NEWSLETTER</h5>
                    <a href="#" class="newsletter-box">¡REGÍSTRATE AHORA!</a>
                </div>
            </div>
            <div class="text-center mt-5 pt-3 border-top border-secondary">
                <p class="mb-0">2025 KIDS MADE HERE. Todos los derechos reservados.</p>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>