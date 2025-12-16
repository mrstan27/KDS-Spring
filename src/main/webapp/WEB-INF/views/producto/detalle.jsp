<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>${p.nombre} | KIDS MADE HERE</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    
    <style>
        body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; color: #000; }
        .navbar-kids { background-color: #fff; border-bottom: 1px solid #eee; padding: 1rem 0; }
        .navbar-brand img { height: 50px; }
        .btn-icon-action { color: #333; font-size: 1.2rem; margin-left: 15px; text-decoration: none; }
        
        /* Detalle Producto */
        .product-title { font-weight: 900; text-transform: uppercase; font-size: 2rem; letter-spacing: -1px; }
        .product-price { font-size: 1.5rem; color: #555; margin-bottom: 20px; }
        .product-meta { font-size: 0.85rem; color: #777; text-transform: uppercase; letter-spacing: 1px; }
        
        .quantity-control { display: flex; align-items: center; border: 1px solid #000; width: fit-content; }
        .btn-qty { background: transparent; border: none; padding: 10px 15px; font-size: 1.2rem; cursor: pointer; }
        .input-qty { width: 50px; text-align: center; border: none; font-weight: bold; background: transparent; }
        
        .btn-add-main { background-color: #000; color: #fff; width: 100%; padding: 15px; 
                        text-transform: uppercase; font-weight: bold; border: none; margin-top: 20px; transition: 0.3s; }
        .btn-add-main:hover { background-color: #E30613; }
        
        /* Sugeridos */
        .card-suggested { border: none; }
        .img-suggested { position: relative; padding-top: 125%; overflow: hidden; background: #f0f0f0; margin-bottom: 10px; }
        .img-suggested img { position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover; transition: transform 0.5s; }
        .card-suggested:hover .img-suggested img { transform: scale(1.05); }
        .suggested-title { font-size: 0.85rem; font-weight: bold; text-transform: uppercase; text-decoration: none; color: #000; }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-kids sticky-top">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/images/logoKIDS.png" alt="KIDS">
            </a>
            <div class="ms-auto d-flex align-items-center">
                <a href="${pageContext.request.contextPath}/" class="btn btn-link text-dark text-decoration-none fw-bold text-uppercase" style="font-size: 0.8rem;">
                    <i class="bi bi-arrow-left"></i> Seguir Comprando
                </a>
                <a href="${pageContext.request.contextPath}/carrito" class="btn-icon-action position-relative"> 
                    <i class="bi bi-bag"></i>
                    <c:if test="${not empty sessionScope.carrito and sessionScope.carrito.size() > 0}">
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-circle bg-danger border border-light" style="font-size: 0.6rem;"> 
                            ${sessionScope.carrito.size()}
                        </span>
                    </c:if>
                </a>
            </div>
        </div>
    </nav>

    <div class="container py-5">
        <div class="row gx-5">
            <div class="col-md-7 mb-4">
                <div style="background-color: #f8f9fa; padding: 20px; text-align: center;">
                    <img src="${pageContext.request.contextPath}/images/productos/${p.imagenUrl}" 
                         class="img-fluid" 
                         style="max-height: 600px; object-fit: contain;" 
                         alt="${p.nombre}"
                         onerror="this.src='https://via.placeholder.com/600x800?text=KIDS'">
                </div>
            </div>

            <div class="col-md-5">
                <div class="product-meta mb-2">${p.categoria.nombreCategoria}</div>
                <h1 class="product-title">${p.nombre}</h1>
                <div class="product-price">S/ ${p.precioVenta}</div>
                
                <hr>
                
                <p class="text-muted">
                    Prenda exclusiva de la colección 2025. Diseñada con materiales de alta calidad para brindarte comodidad y estilo urbano único.
                </p>

                <div class="mb-4">
                    <span class="fw-bold"><i class="bi bi-box-seam"></i> Stock Disponible:</span> 
                    <c:choose>
                        <c:when test="${p.stockActual > 5}">
                            <span class="text-success fw-bold">${p.stockActual} unidades</span>
                        </c:when>
                        <c:when test="${p.stockActual > 0}">
                            <span class="text-warning fw-bold">¡Solo quedan ${p.stockActual}!</span>
                        </c:when>
                        <c:otherwise>
                            <span class="text-danger fw-bold">Agotado</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <c:if test="${p.stockActual > 0}">
                    <form action="${pageContext.request.contextPath}/carrito/agregar/${p.productoId}" method="get">
                        <div class="mb-3">
                            <label class="form-label fw-bold small text-uppercase">Cantidad:</label>
                            <div class="quantity-control">
                                <button type="button" class="btn-qty" onclick="updateQty(-1)">-</button>
                                <input type="number" name="cantidad" id="qtyInput" value="1" min="1" max="${p.stockActual}" class="input-qty" readonly>
                                <button type="button" class="btn-qty" onclick="updateQty(1)">+</button>
                            </div>
                        </div>
                        <button type="submit" class="btn-add-main">AÑADIR AL CARRITO</button>
                    </form>
                </c:if>
                
                <div class="mt-4 pt-4 border-top">
                    <div class="d-flex align-items-center mb-2">
                        <i class="bi bi-truck me-2 fs-4"></i>
                        <span class="small">Envío a todo el Perú</span>
                    </div>
                    <div class="d-flex align-items-center">
                        <i class="bi bi-shield-check me-2 fs-4"></i>
                        <span class="small">Compra 100% Segura</span>
                    </div>
                </div>
            </div>
        </div>

        <c:if test="${not empty sugeridos}">
            <div class="mt-5 pt-5 border-top">
                <h3 class="text-center fw-bold text-uppercase mb-4">También te podría gustar</h3>
                <div class="row g-4">
                    <c:forEach items="${sugeridos}" var="s">
                        <div class="col-6 col-md-3">
                            <div class="card-suggested h-100">
                                <a href="${pageContext.request.contextPath}/productos/detalle/${s.productoId}" class="text-decoration-none">
                                    <div class="img-suggested">
                                        <img src="${pageContext.request.contextPath}/images/productos/${s.imagenUrl}" 
                                             alt="${s.nombre}"
                                             onerror="this.src='https://via.placeholder.com/300x400?text=KIDS'">
                                    </div>
                                    <div class="text-center">
                                        <div class="suggested-title">${s.nombre}</div>
                                        <small class="text-muted">S/ ${s.precioVenta}</small>
                                    </div>
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>
    </div>

    <script>
        function updateQty(change) {
            const input = document.getElementById('qtyInput');
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