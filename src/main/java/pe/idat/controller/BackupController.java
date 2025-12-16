package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import pe.idat.service.BackupService;

@Controller
@RequestMapping("/backup")
public class BackupController {

    @Autowired
    private BackupService backupService;

    // Descargar el archivo .sql
    @GetMapping("/exportar")
    public void descargarBackup(HttpServletResponse response) {
        backupService.exportarBackup(response);
    }

    // Ver la pantalla de restauración
    @GetMapping("/restaurar")
    public String verPaginaRestaurar() {
        return "backup/restaurar"; 
    }

    // Procesar el archivo subido
    @PostMapping("/procesar-restauracion")
    public String procesarRestauracion(@RequestParam("archivo") MultipartFile archivo, RedirectAttributes flash) {
        boolean exito = backupService.restaurarBackup(archivo);
        
        if (exito) {
            // Si funciona, forzamos logout para evitar inconsistencias de datos en sesión
            flash.addFlashAttribute("success", "Base de datos restaurada correctamente. Por favor inicia sesión nuevamente.");
            return "redirect:/login/loginusuario"; 
        } else {
            flash.addFlashAttribute("error", "Error al restaurar. Verifica que el archivo sea un .sql válido y que MySQL esté accesible.");
            return "redirect:/backup/restaurar";
        }
    }
}