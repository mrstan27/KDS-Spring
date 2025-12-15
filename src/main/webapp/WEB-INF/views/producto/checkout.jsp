<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Checkout | KIDS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-light">

    <div class="container mt-5" style="max-width: 900px;">
        <div class="text-center mb-4">
            <img src="${pageContext.request.contextPath}/images/logoKIDS.png" height="50" alt="Logo">
            <h2 class="mt-3 fw-bold">Finalizar Compra</h2>
        </div>

        <div class="row g-5">
            <div class="col-md-5 order-md-last">
                <h4 class="d-flex justify-content-between align-items-center mb-3">
                    <span class="text-muted">Tu pedido</span>
                    <span class="badge bg-secondary rounded-pill">${carrito.size()} items</span>
                </h4>
                <ul class="list-group mb-3 shadow-sm border-0">
                    <c:forEach items="${carrito}" var="item">
                        <li class="list-group-item d-flex justify-content-between lh-sm">
                            <div>
                                <h6 class="my-0 small fw-bold text-uppercase">${item.nombre}</h6>
                                <small class="text-muted">Cant: ${item.cantidad}</small>
                            </div>
                            <span class="text-muted">S/ ${item.subtotal}</span>
                        </li>
                    </c:forEach>
                    <li class="list-group-item d-flex justify-content-between bg-white fw-bold">
                        <span>Total (PEN)</span>
                        <span>S/ ${total}</span>
                    </li>
                </ul>
            </div>

            <div class="col-md-7">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/carrito/procesar-pago" method="POST">
                    
                    <div class="card p-4 shadow-sm border-0 mb-4">
                        <h5 class="mb-3 fw-bold"><i class="fa-regular fa-credit-card"></i> Pago Seguro</h5>
                        <div class="row g-3">
                            <div class="col-12">
                                <label class="form-label small text-muted">Titular de la tarjeta</label>
                                <input type="text" class="form-control" required placeholder="NOMBRE APELLIDO">
                            </div>
                            <div class="col-12">
                                <label class="form-label small text-muted">Número de tarjeta</label>
                                <input type="text" class="form-control" required placeholder="0000 0000 0000 0000">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small text-muted">Vencimiento</label>
                                <input type="text" class="form-control" required placeholder="MM/YY">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small text-muted">CVV</label>
                                <input type="password" class="form-control" required placeholder="123">
                            </div>
                        </div>
                    </div>

                    <div class="card p-4 shadow-sm border-0 mb-4">
                        <h5 class="mb-3 fw-bold"><i class="fa-solid fa-truck"></i> Envío</h5>
                        <div class="row g-3">
                            <div class="col-12">
                                <label class="form-label small text-muted">Dirección de entrega</label>
                                <input type="text" class="form-control" required placeholder="Av. Principal 123, Dpto 401">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small text-muted">Distrito</label>
                                <select class="form-select">
                                    <option>Lima</option>
                                    <option>Miraflores</option>
                                    <option>San Isidro</option>
                                    <option>Surco</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small text-muted">Teléfono</label>
                                <input type="text" class="form-control" required placeholder="999 000 000">
                            </div>
                        </div>
                    </div>

                    <button class="w-100 btn btn-danger btn-lg py-3 rounded-0 fw-bold" type="submit">
                        PAGAR S/ ${total}
                    </button>
                    <div class="text-center mt-3">
                        <a href="${pageContext.request.contextPath}/carrito" class="text-muted text-decoration-none small">Volver al carrito</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>