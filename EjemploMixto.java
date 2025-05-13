import java.io.*;
import java.util.*;

/**
 * Ejemplo de operaciones mixtas (lectura/escritura) con archivos CSV
 * y manejo avanzado de excepciones.
 * 
 * Este programa gestiona un inventario, permitiendo:
 * - Leer productos del inventario desde un CSV
 * - Actualizar cantidades de productos
 * - Guardar el inventario actualizado a un nuevo archivo CSV
 */
public class EjemploMixto {
    
    // Clase interna para representar productos en el inventario
    private static class Producto {
        private String id;
        private String nombre;
        private int cantidad;
        private double precio;
        
        public Producto(String id, String nombre, int cantidad, double precio) {
            this.id = id;
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.precio = precio;
        }
        
        @Override
        public String toString() {
            return String.format("%s,%s,%d,%.2f", id, nombre, cantidad, precio);
        }
    }
    
    // Excepción personalizada para validación de datos
    private static class DatosInvalidosException extends Exception {
        public DatosInvalidosException(String mensaje) {
            super(mensaje);
        }
    }
    
    public static void main(String[] args) {
        // Definimos rutas de archivos
        final String ARCHIVO_ENTRADA = "inventario.csv";
        final String ARCHIVO_SALIDA = "inventario_actualizado.csv";
        
        List<Producto> inventario = new ArrayList<>();
        
        // Lectura del archivo CSV
        try {
            cargarInventario(ARCHIVO_ENTRADA, inventario);
            System.out.println("Inventario cargado correctamente: " + inventario.size() + " productos");
            
            // Actualizar algunos productos (simulación)
            actualizarInventario(inventario);
            
            // Guardar inventario actualizado
            guardarInventario(ARCHIVO_SALIDA, inventario);
            System.out.println("Inventario actualizado guardado en: " + ARCHIVO_SALIDA);
            
        } catch (FileNotFoundException e) {
            // Manejo específico cuando no se encuentra el archivo
            System.err.println("Error: No se pudo encontrar el archivo de inventario.");
            System.err.println("Ruta: " + e.getMessage());
            System.err.println("Verifica que el archivo exista y la ruta sea correcta.");
        } catch (IOException e) {
            // Manejo para otros errores de E/S
            System.err.println("Error de E/S durante el procesamiento: " + e.getMessage());
        } catch (DatosInvalidosException e) {
            // Manejo para errores de validación de datos
            System.err.println("Error de validación de datos: " + e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otra excepción no anticipada
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Carga el inventario desde un archivo CSV.
     * 
     * @param nombreArchivo Ruta del archivo CSV a leer
     * @param inventario Lista donde se cargarán los productos
     * @throws IOException Si ocurre un error de E/S
     * @throws DatosInvalidosException Si hay datos mal formateados
     */
    private static void cargarInventario(String nombreArchivo, List<Producto> inventario) 
            throws IOException, DatosInvalidosException {
        
        // Verificación previa del archivo
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            throw new FileNotFoundException(nombreArchivo);
        }
        
        if (!archivo.canRead()) {
            throw new IOException("No hay permisos de lectura para: " + nombreArchivo);
        }
        
        // Usamos try-with-resources para garantizar el cierre de recursos
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int numeroLinea = 0;
            
            // Leemos la cabecera (si existe)
            linea = reader.readLine();
            numeroLinea++;
            
            // Procesamos cada línea del archivo
            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                
                // Ignoramos líneas vacías
                if (linea.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    // Separamos los campos y validamos
                    String[] campos = linea.split(",");
                    if (campos.length != 4) {
                        throw new DatosInvalidosException("La línea " + numeroLinea + 
                                " no tiene el formato esperado (id,nombre,cantidad,precio)");
                    }
                    
                    String id = campos[0].trim();
                    String nombre = campos[1].trim();
                    int cantidad;
                    double precio;
                    
                    try {
                        cantidad = Integer.parseInt(campos[2].trim());
                        if (cantidad < 0) {
                            throw new DatosInvalidosException("La cantidad no puede ser negativa");
                        }
                    } catch (NumberFormatException e) {
                        throw new DatosInvalidosException("La cantidad debe ser un número entero");
                    }
                    
                    try {
                        precio = Double.parseDouble(campos[3].trim());
                        if (precio < 0) {
                            throw new DatosInvalidosException("El precio no puede ser negativo");
                        }
                    } catch (NumberFormatException e) {
                        throw new DatosInvalidosException("El precio debe ser un número válido");
                    }
                    
                    // Creamos el producto y lo añadimos al inventario
                    Producto producto = new Producto(id, nombre, cantidad, precio);
                    inventario.add(producto);
                    
                } catch (DatosInvalidosException e) {
                    // Añadimos contexto adicional a la excepción
                    throw new DatosInvalidosException("Error en línea " + numeroLinea + ": " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Actualiza algunas cantidades en el inventario (operación simulada)
     * @param inventario Lista de productos a actualizar
     */
    private static void actualizarInventario(List<Producto> inventario) {
        // Esta es una operación simulada para el ejemplo
        if (inventario.size() > 0) {
            // Aumentamos en 10 la cantidad del primer producto
            Producto primero = inventario.get(0);
            primero.cantidad += 10;
            System.out.println("Actualizado: " + primero.nombre + " - Nueva cantidad: " + primero.cantidad);
        }
        
        if (inventario.size() > 1) {
            // Reducimos en 5 la cantidad del segundo producto
            Producto segundo = inventario.get(1);
            segundo.cantidad = Math.max(0, segundo.cantidad - 5); // Evitamos negativos
            System.out.println("Actualizado: " + segundo.nombre + " - Nueva cantidad: " + segundo.cantidad);
        }
    }
    
    /**
     * Guarda el inventario actualizado en un nuevo archivo CSV
     * 
     * @param nombreArchivo Ruta del archivo donde se guardará el inventario
     * @param inventario Lista de productos a guardar
     * @throws IOException Si ocurre un error al escribir
     */
    private static void guardarInventario(String nombreArchivo, List<Producto> inventario) 
            throws IOException {
        
        // Usamos try-with-resources para garantizar el cierre del escritor
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            // Escribimos la cabecera
            writer.write("ID,Nombre,Cantidad,Precio");
            writer.newLine();
            
            // Escribimos cada producto
            for (Producto producto : inventario) {
                writer.write(producto.toString());
                writer.newLine();
            }
            
            // Forzamos la escritura de cualquier dato en búfer
            writer.flush();
        } catch (IOException e) {
            // Agregamos contexto adicional antes de relanzar
            throw new IOException("Error al guardar el inventario: " + e.getMessage(), e);
        }
    }
}
