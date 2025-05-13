import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que demuestra la escritura de datos en formato CSV
 * con manejo adecuado de excepciones.
 */
public class ManejoErroresCSV {

    public static void main(String[] args) {
        // Lista de ejemplo con resultados de exámenes
        List<String[]> datosResultados = new ArrayList<>();
        
        // Cabecera del CSV
        datosResultados.add(new String[]{"DNI", "Nombre", "Asignatura", "Calificación"});
        
        // Datos de ejemplo
        datosResultados.add(new String[]{"12345678A", "Ana García", "Programación", "8.5"});
        datosResultados.add(new String[]{"23456789B", "Carlos López", "Programación", "7.2"});
        datosResultados.add(new String[]{"34567890C", "Laura Martínez", "Programación", "9.0"});
        
        // Llamamos al método para escribir el CSV
        escribirCSV("resultados.csv", datosResultados);
        
        // Ejemplo adicional con try-catch interno
        try {
            escribirCSVConVerificacion("resultados_verificado.csv", datosResultados);
            System.out.println("Archivo resultados_verificado.csv creado correctamente.");
        } catch (IOException e) {
            // Manejo centralizado de excepciones
            System.err.println("Error en la operación de escritura verificada: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Escribe datos en un archivo CSV usando try-with-resources
     * @param nombreArchivo Nombre del archivo a crear
     * @param datos Lista de arrays con los datos a escribir
     */
    public static void escribirCSV(String nombreArchivo, List<String[]> datos) {
        // Try-with-resources garantiza que los recursos se cierren automáticamente
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            
            // Iteramos sobre cada fila de datos
            for (String[] fila : datos) {
                // Construimos la línea CSV uniendo los campos con comas
                StringBuilder linea = new StringBuilder();
                
                for (int i = 0; i < fila.length; i++) {
                    // Agregamos comillas dobles para campos que podrían contener comas
                    linea.append("\"").append(fila[i].replace("\"", "\"\"")).append("\"");
                    
                    // Agregamos coma después de cada campo excepto el último
                    if (i < fila.length - 1) {
                        linea.append(",");
                    }
                }
                
                // Escribir la línea al archivo y agregar un salto de línea
                writer.write(linea.toString());
                writer.newLine();
            }
            
            System.out.println("Archivo " + nombreArchivo + " creado correctamente.");
            
        } catch (IOException e) {
            // Capturamos específicamente IOException para manejar errores de E/S
            System.err.println("Error al escribir en el archivo CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Versión avanzada que incluye verificaciones previas y lanza excepciones
     * @param nombreArchivo Nombre del archivo a crear
     * @param datos Lista de arrays con los datos a escribir
     * @throws IOException Si ocurre un error durante la escritura
     */
    public static void escribirCSVConVerificacion(String nombreArchivo, List<String[]> datos) 
            throws IOException {
        
        // Verificación previa de los datos de entrada
        if (datos == null || datos.isEmpty()) {
            throw new IllegalArgumentException("No hay datos para escribir en el CSV");
        }
        
        // Verificar si el directorio existe y tiene permisos de escritura
        File archivo = new File(nombreArchivo);
        File directorio = archivo.getParentFile();
        
        if (directorio != null && !directorio.exists()) {
            // Intentar crear el directorio si no existe
            if (!directorio.mkdirs()) {
                throw new IOException("No se pudo crear el directorio para el archivo CSV");
            }
        }
        
        // Verificar si podemos escribir en el archivo (si ya existe)
        if (archivo.exists() && !archivo.canWrite()) {
            throw new IOException("No se tienen permisos de escritura en el archivo: " + nombreArchivo);
        }
        
        // Una vez hechas todas las verificaciones, procedemos con la escritura
        // usando try-with-resources
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            for (String[] fila : datos) {
                // Validación adicional para cada fila
                if (fila == null) {
                    // Omitir filas nulas (podríamos optar por lanzar excepción también)
                    continue;
                }
                
                StringBuilder linea = new StringBuilder();
                
                for (int i = 0; i < fila.length; i++) {
                    // Manejo de valores nulos
                    String valor = fila[i] != null ? fila[i] : "";
                    
                    // Escapado de valores que contienen comillas o comas
                    boolean requiereComillas = valor.contains("\"") || valor.contains(",") || valor.contains("\n");
                    
                    if (requiereComillas) {
                        // Duplicar comillas internas y encerrar en comillas
                        linea.append("\"").append(valor.replace("\"", "\"\"")).append("\"");
                    } else {
                        linea.append(valor);
                    }
                    
                    if (i < fila.length - 1) {
                        linea.append(",");
                    }
                }
                
                writer.write(linea.toString());
                writer.newLine();
            }
        }
        // No hace falta catch pues la excepción será manejada por el llamador
    }
}
