<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>KIDS MADE HERE</title>

    <!-- CSS del mockup -->
    <!--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">-->

<style>
body {
    margin: 0;
    font-family: 'Helvetica Neue', sans-serif;
    background-color: #fff;
    color: #111;
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 40px;
    background-color: white;
    border-bottom: 1px solid #ddd;
}

.logo img {
    height: 60px;
    width: auto;
}

.nav a {
    text-decoration: none;
    color: #333;
    margin-left: 20px;
    font-weight: 500;
    font-size: 16px;
}

.nav a:hover {
    color: #ec0909;
}

.hero {
    background: url('${pageContext.request.contextPath}/images/fondo_tienda.jpg') 
               center/cover no-repeat;
    height: 80vh;
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
}

.hero-text {
    background-color: rgba(255,255,255,0.8);
    padding: 40px;
    border-radius: 8px;
}

.hero-text h1 {
    font-size: 2.5em;
    margin-bottom: 10px;
}

.hero-text p {
    font-size: 1.2em;
    margin-bottom: 20px;
}

.btn-nav {
    padding: 10px 18px;
    border-radius: 5px;
    background-color: black;
    color: white;
}

.btn-nav:hover {
    background-color: #e11212;
}

.categories {
    padding: 50px 80px;
    display: flex;
    justify-content: center;
    gap: 40px;
    flex-wrap: wrap;
}

.category-item {
    width: 220px;
    text-align: center;
}

.category-item img {
    width: 100%;
    border-radius: 8px;
}

.category-item h3 {
    margin-top: 10px;
}

.footer {
    background: #f5f5f5;
    text-align: center;
    padding: 20px;
    margin-top: 40px;
}
</style>

</head>

<body>

    <header class="header">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/images/logoKIDS.png" alt="Kids Made Here">
        </div>

        <nav class="nav">
            <a href="${pageContext.request.contextPath}/login/logincliente" class="btn-nav">Clientes</a>
            <a href="${pageContext.request.contextPath}/login/loginusuario" class="btn-nav">Empleados</a>
        </nav>
    </header>

    <section class="hero">
        <div class="hero-text">
            <h1>Kids Made Here</h1>
            <p>Lo mejor en ropa juvenil</p>
        </div>
    </section>

    <section class="categories">
        <div class="category-item">
            <a href="/ropa">
                <img src="img/ropa.jpg" alt="Ropa">
                <h3>Ropa</h3>
            </a>
        </div>

        <div class="category-item">
            <a href="/jeans">
                <img src="img/jeans.jpg" alt="Jeans">
                <h3>Jeans</h3>
            </a>
        </div>

        <div class="category-item">
            <a href="/outwear">
                <img src="img/outwear.jpg" alt="Outwear">
                <h3>Outwear</h3>
            </a>
        </div>

        <div class="category-item">
            <a href="/accesorios">
                <img src="img/accesorios.jpg" alt="Accesorios">
                <h3>Accesorios</h3>
            </a>
        </div>
    </section>

    <footer class="footer">
        <p>© 2025 Kids Made Here - Todos los derechos reservados</p>
    </footer>

</body>
</html>
