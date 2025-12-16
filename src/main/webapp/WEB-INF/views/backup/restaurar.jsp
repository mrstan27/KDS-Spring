<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Restaurar Sistema | Kids Made Here</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-light">

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                
                <div style="margin-bottom: 15px;">
                    <a href="${pageContext.request.contextPath}/login/menu" style="color: #c0392b; text-decoration: none; font-weight: bold;">
                        <i class="fa-solid fa-arrow-left"></i> Volver al Menú
                    </a>
                </div>
                
                <div class="card shadow">
                    <div class="card-header bg-danger text-white">
                        <h4 class="mb-0"><i class="fa-solid fa-rotate-left"></i> Restaurar Base de Datos</h4>
                    </div>
                    <div class="card-body">
                        
                        <div class="alert alert-warning">
                            <strong>¡ADVERTENCIA!</strong><br>
                            Al restaurar, se borrarán todos los datos actuales y se reemplazarán por los del archivo que subas. Esta acción es irreversible.
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/backup/procesar-restauracion" method="post" enctype="multipart/form-data">
                            <div class="mb-4">
                                <label for="archivo" class="form-label fw-bold">Selecciona el archivo de respaldo (.sql):</label>
                                <input type="file" class="form-control" id="archivo" name="archivo" accept=".sql" required>
                            </div>
                            
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-danger btn-lg" onclick="return confirm('¿Estás 100% seguro de restaurar? Se perderán los datos actuales.')">
                                    <i class="fa-solid fa-upload"></i> INICIAR RESTAURACIÓN
                                </button>
                            </div>
                        </form>

                    </div>
                </div>

            </div>
        </div>
    </div>

</body>
</html>