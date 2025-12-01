<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>KIDS MADE HERE | Official Store</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    
    <style>
        /* --- ESTILOS KIDS MADE HERE (ROJO Y BLANCO) --- */
        :root {
            --kids-red: #E30613; /* Color rojo exacto de la marca */
            --kids-black: #000000;
        }

        body { 
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; 
            background-color: #ffffff; 
            color: #000;
        }
        
        /* NAVBAR */
        .navbar-kids { 
            background-color: #ffffff; /* Fondo blanco como la original */
            padding: 1rem 0; 
            border-bottom: 1px solid #eee;
        }

        .navbar-brand img {
            height: 50px; /* Ajusta el tamaño de tu logo aquí */
            object-fit: contain;
        }

        /* Enlaces del menú (Categorías) */
        .nav-link { 
            color: #555 !important; 
            text-transform: uppercase; 
            font-size: 0.75rem; 
            letter-spacing: 1px; 
            font-weight: 600; 
            margin: 0 5px;
            transition: color 0.3s;
        }
        .nav-link:hover { 
            color: var(--kids-red) !important; 
            text-decoration: underline; 
        }

        /* Elementos especiales */
        .text-fire { color: var(--kids-red) !important; font-weight: bold; }
        .text-sun { color: #f39c12 !important; }

        /* Botones de Login (Iconos) */
        .btn-icon-action { 
            color: #333; 
            font-size: 1.2rem; 
            margin-left: 15px; 
            text-decoration: none;
            transition: color 0.3s;
        }
        .btn-icon-action:hover { color: var(--kids-red); }

        /* HERO SECTION (Tu imagen de fondo) */
        .hero-section {
            /* AQUÍ ESTÁ LA RUTA A TU IMAGEN DE FONDO */
            background-image: url('${pageContext.request.contextPath}/images/fondo_tienda.jpg');
            background-size: cover;
            background-position: center;
            height: 85vh; /* Altura de la pantalla */
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
        }

        /* Capa oscura suave para que se lea el texto si la imagen es clara */
        .hero-overlay {
            position: absolute;
            top: 0; left: 0; width: 100%; height: 100%;
            background: rgba(0,0,0,0.2); 
        }

        .hero-content {
            position: relative;
            z-index: 2;
            text-align: center;
            color: #fff;
        }
        
        .hero-title { 
            font-weight: 900; 
            font-size: 4rem; 
            text-transform: uppercase; 
            letter-spacing: -1px;
            text-shadow: 2px 2px 8px rgba(0,0,0,0.6);
        }
        
        .btn-hero {
            background-color: var(--kids-red);
            color: #fff;
            padding: 12px 40px;
            font-weight: 700;
            text-transform: uppercase;
            border: none;
            border-radius: 0;
            margin-top: 20px;
        }
        .btn-hero:hover { background-color: #c00510; color: #fff; }

        /* Footer */
        /* --- ESTILOS DEL FOOTER --- */
        footer { 
            background-color: #f2f2f2; /* Gris claro de la foto */
            padding-top: 50px;
            padding-bottom: 20px;
            color: #333;
            font-size: 0.85rem;
            margin-top: auto;
        }

        .footer-title {
            font-family: 'Times New Roman', serif; /* Tipografía Serifa de la foto */
            text-transform: uppercase;
            letter-spacing: 1px;
            margin-bottom: 20px;
            font-size: 1rem;
            color: #000;
        }

        .footer-link {
            display: block;
            color: #666;
            text-decoration: none;
            margin-bottom: 10px;
            text-transform: uppercase;
            font-size: 0.75rem;
            letter-spacing: 1px;
            transition: color 0.3s;
        }
        .footer-link:hover { color: var(--kids-red); text-decoration: underline; }

        .libro-reclamaciones {
            border: 1px solid #ccc;
            border-radius: 5px;
            padding: 5px;
            background: white;
            width: 150px;
            display: block;
            margin-top: 10px;
        }

        .social-icons a {
            font-size: 1.2rem;
            margin-right: 15px;
            color: #000;
        }
        
        .newsletter-box {
            border-bottom: 1px solid #000;
            display: inline-block;
            color: #000;
            font-weight: bold;
            text-decoration: none;
        }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <div class="bg-light text-center py-2 text-muted small" style="font-size: 0.7rem;">
        Envío gratis por compras mayores a S/250* 💖
    </div>

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
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/nite-out">NITE OUT 🌙</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/outerwear">OUTERWEAR</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/ropa">ROPA</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/jeans">JEANS</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/polos">POLOS</a></li>
                    <li class="nav-item"><a class="nav-link text-fire" href="${pageContext.request.contextPath}/productos/categoria/black-sunday">BLACK SUNDAY 💥</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/productos/categoria/accesorios">ACCESORIOS</a></li>
                </ul>

                <div class="d-flex align-items-center">
                    <a href="#" class="btn-icon-action"><i class="bi bi-search"></i></a>
                    
                    <a href="${pageContext.request.contextPath}/login/logincliente" class="btn-icon-action" title="Cliente">
                        <i class="bi bi-person"></i>
                    </a>

                    <a href="${pageContext.request.contextPath}/login/loginusuario" class="btn-icon-action" title="Staff">
                        <i class="bi bi-person-lock"></i>
                    </a>

                    <a href="#" class="btn-icon-action"><i class="bi bi-bag"></i></a>
                </div>
            </div>
        </div>
    </nav>

    <section class="hero-section">
        <div class="hero-overlay"></div>
        <div class="hero-content">
            <h1 class="hero-title">BLACK<br> SUNDAY 💥</h1>
            <p class="fs-4 mt-3 text-uppercase letter-spacing-2">Nueva Colección Urbana 2025</p>
            <a href="${pageContext.request.contextPath}/productos/categoria/new-in" class="btn btn-hero">COMPRAR AHORA</a>
        </div>
    </section>

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
                        <img src="${pageContext.request.contextPath}/images/libro_reclamaciones.png" 
                             alt="Libro de Reclamaciones" class="libro-reclamaciones">
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
                    <a href="#" class="footer-link">PREGUNTAS FRECUENTES</a>
                </div>

                <div class="col-md-3 mb-4">
                    <h5 class="footer-title">SOCIAL</h5>
                    <div class="social-icons mb-3">
                        <a href="#"><i class="bi bi-facebook"></i> FACEBOOK</a><br>
                        <a href="#"><i class="bi bi-instagram"></i> INSTAGRAM</a><br>
                        <a href="#"><i class="bi bi-tiktok"></i> TIKTOK</a><br>
                        <a href="#"><i class="bi bi-spotify"></i> SPOTIFY</a>
                    </div>

                    <h5 class="footer-title mt-4">SUSCRÍBETE AL NEWSLETTER</h5>
                    <a href="#" class="newsletter-box">¡REGÍSTRATE AHORA!</a>
                </div>

            </div>

            <div class="text-center mt-5 pt-3 border-top border-secondary">
                <p class="mb-0">2025 KIDS MADE HERE. Todos los derechos reservados.</p>
                <p class="small text-muted mt-2">Cuando visita o interactúa con nuestra web, nosotros podemos usar cookies...</p>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

