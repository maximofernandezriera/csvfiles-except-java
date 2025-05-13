import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Ejemplo de lectura de archivos CSV con datos de alumnos
 * Este programa muestra cómo leer un archivo CSV y manejar las excepciones relacionadas
 */
public class ClaseCSV {

    public static void main(String[] args) {
        // Definimos la ruta al archivo CSV
        String rutaArchivo = "datos_alumnos.csv";
        
        // Utilizamos try-with-resources para garantizar que los recursos se cierren correctamente
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean primeraLinea = true;
            
            System.out.println("Leyendo datos de alumnos desde: " + rutaArchivo);
            System.out.println("----------------------------------------");
            
            // Leemos el archivo línea por línea
            while ((linea = br.readLine()) != null) {
                // Verificamos si es la primera línea (encabezados)
                if (primeraLinea) {
                    System.out.println("ENCABEZADOS: " + linea);
                    primeraLinea = false;
                    System.out.println("----------------------------------------");
                    continue;
                }
                
                // Dividimos la línea por comas
                String[] datos = linea.split(",");
                
                // Verificamos que tengamos al menos 4 campos (ID, Nombre, Apellido, Calificación)
                if (datos.length >= 4) {
                    // Extraemos los datos del alumno
                    String id = datos[0].trim();
                    String nombre = datos[1].trim();
                    String apellido = datos[2].trim();
                    
                    // Intentamos convertir la calificación a un valor numérico
                    try {
                        double calificacion = Double.parseDouble(datos[3].trim());
                        
                        // Mostramos los datos procesados
                        System.out.printf("Alumno: %s %s (ID: %s) - Calificación: %.2f%n", 
                                        nombre, apellido, id, calificacion);
                    } catch (NumberFormatException e) {
                        // Manejamos el caso en que la calificación no sea un número válido
                        System.out.printf("ERROR: Alumno: %s %s (ID: %s) - Calificación inválida: %s%n", 
                                        nombre, apellido, id, datos[3].trim());
                    }
                } else {
                    // La línea no tiene el formato esperado
                    System.out.println("AVISO: Línea con formato incorrecto: " + linea);
                }
            }
            System.out.println("----------------------------------------");
            System.out.println("Lectura del archivo CSV completada.");
            
        } catch (FileNotFoundException e) {
            // Capturamos específicamente la excepción de archivo no encontrado
            System.err.println("ERROR: No se encontró el archivo: " + rutaArchivo);
            System.err.println("Detalles del error: " + e.getMessage());
        } catch (IOException e) {
            // Capturamos otras excepciones de E/S que puedan ocurrir
            System.err.println("ERROR: Problema al leer el archivo: " + rutaArchivo);
            System.err.println("Detalles del error: " + e.getMessage());
        }
    }
}
