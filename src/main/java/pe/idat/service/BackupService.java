package pe.idat.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BackupService {

    // DATOS DE CONEXIÓN (Deben coincidir con tu application.properties)
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456"; 
    private static final String DB_NAME = "db_kids";

    // RUTAS DE LOS EJECUTABLES DE MYSQL (Verifica que existan en tu PC)
    private static final String MYSQL_DUMP_PATH = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe";
    private static final String MYSQL_PATH      = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql.exe";

    // 1. GENERAR BACKUP (Exportar)
    public void exportarBackup(HttpServletResponse response) {
        String fecha = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
        String fileName = "Backup_KidsMadeHere_" + fecha + ".sql";

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        try {
            ProcessBuilder pb = new ProcessBuilder(
                MYSQL_DUMP_PATH,
                "-u" + DB_USER,
                "-p" + DB_PASSWORD,
                "--add-drop-table",
                DB_NAME
            );
            
            pb.redirectErrorStream(true);
            Process process = pb.start();

            InputStream is = process.getInputStream();
            ServletOutputStream os = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            is.close();
            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2. RESTAURAR BACKUP (Importar)
    public boolean restaurarBackup(MultipartFile archivoSql) {
        if (archivoSql.isEmpty()) return false;

        try {
            ProcessBuilder pb = new ProcessBuilder(
                MYSQL_PATH,
                "-u" + DB_USER,
                "-p" + DB_PASSWORD,
                DB_NAME
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            OutputStream os = process.getOutputStream();
            os.write(archivoSql.getBytes());
            os.flush();
            os.close();

            int exitCode = process.waitFor();
            return exitCode == 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}