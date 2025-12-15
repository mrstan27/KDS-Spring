<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reportes | Kids Made Here</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { background-color: #f4f6f9; font-family: sans-serif; }
        .report-card { transition: transform 0.3s; cursor: pointer; border: none; }
        .report-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.1); }
        .icon-box { font-size: 3rem; margin-bottom: 1rem; }
        .btn-back { margin-bottom: 20px; }
    </style>
</head>
<body>
    <div class="container py-5">
        <div style="margin-bottom: 15px;">
            <a href="${pageContext.request.contextPath}/login/menu" style="color: #c0392b; text-decoration: none; font-weight: bold;">
                <i class="fa-solid fa-arrow-left"></i> Volver al Menú
            </a>
        </div>
        
        <h2 class="text-center mb-5 text-uppercase fw-bold">Centro de Reportes</h2>
        
        <div class="row justify-content-center g-4">
            
            <div class="col-md-5">
                <div class="card report-card h-100 text-center p-4">
                    <div class="card-body">
                        <div class="icon-box text-primary">
                            <i class="fa-solid fa-boxes-stacked"></i>
                        </div>
                        <h4 class="card-title fw-bold">Reporte de Stock</h4>
                        <p class="card-text text-muted">Genera un PDF con el inventario actual de productos, precios y categorías.</p>
                        <a href="${pageContext.request.contextPath}/reportes/stock-pdf" class="btn btn-primary w-100 mt-3">
                            <i class="fa-solid fa-download"></i> Descargar Reporte Stock
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-5">
                <div class="card report-card h-100 text-center p-4">
                    <div class="card-body">
                        <div class="icon-box text-success">
                            <i class="fa-solid fa-chart-line"></i>
                        </div>
                        <h4 class="card-title fw-bold">Reporte de Ventas</h4>
                        <p class="card-text text-muted">Genera un PDF con el historial completo de ventas, clientes y totales recaudados.</p>
                        <a href="${pageContext.request.contextPath}/reportes/ventas-pdf" class="btn btn-success w-100 mt-3">
                            <i class="fa-solid fa-download"></i> Descargar Reporte Ventas
                        </a>
                    </div>
                </div>
            </div>
            
        </div>
    </div>
</body>
</html>