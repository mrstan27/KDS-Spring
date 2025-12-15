package pe.idat.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.idat.entity.Producto;
import pe.idat.entity.Venta;
import pe.idat.service.PdfService;
import pe.idat.service.ProductoService;
import pe.idat.service.VentaService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    // Vista del menú de reportes
    @GetMapping
    public String verMenuReportes() {
        return "reporte/reportes"; 
    }

    // Descargar PDF de Stock
    @GetMapping("/stock-pdf")
    public void descargarReporteStock(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Stock_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Producto> listaProductos = productoService.listarProductos();
        pdfService.exportarReporteStock(response, listaProductos);
    }

    // Descargar PDF de Ventas
    @GetMapping("/ventas-pdf")
    public void descargarReporteVentas(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Ventas_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Venta> listaVentas = ventaService.listarVentas();
        pdfService.exportarReporteVentas(response, listaVentas);
    }
}