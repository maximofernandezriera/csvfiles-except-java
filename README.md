# Lectura y Escritura de Ficheros CSV en Java con Manejo de Excepciones

## Introducción

Los archivos CSV (Comma-Separated Values) son ampliamente utilizados para el intercambio de datos tabulares entre aplicaciones. En esta práctica, aprenderemos a leer y escribir ficheros CSV en Java, implementando un manejo adecuado de excepciones para operaciones de E/S, habilidad fundamental en el desarrollo de aplicaciones robustas.

## Ejemplo 1: Lectura básica de un CSV de datos de alumnosPuntos clave

### Ficheros:

* [ClaseCSV.java](ClaseCSV.java)
* [datos_alumnos.csv](datos_alumnos.csv)

Este ejemplo muestra cómo leer un archivo CSV que contiene información de estudiantes, utilizando la clase `BufferedReader` y gestionando las excepciones más comunes en operaciones de lectura.

## Ejemplo 2: Escritura de datos en formato CSV

### Ficheros:

* [ManejoErroresCSV.java](ManejoErroresCSV.java)
* [resultados.csv](resultados.csv)

En este ejemplo se implementa la escritura de datos en un archivo CSV utilizando FileWriter y BufferedWriter, con un manejo detallado de excepciones, incluyendo el uso de try-with-resources.

## Ejemplo 3: Operaciones mixtas de lectura/escritura con validación

### Ficheros:

* [EjemploMixto.java](EjemploMixto.java)
* [inventario.csv](inventario.csv)

Este ejemplo más avanzado combina lectura y escritura de archivos CSV, implementando validación de datos y manejo personalizado de excepciones para situaciones específicas.

## Conclusiones y puntos clave

- **Try-with-resources**:  
  Utilizar siempre esta estructura para garantizar el cierre adecuado de recursos de E/S, incluso en caso de excepción.

- **Manejo específico**:  
  Capturar excepciones específicas (`FileNotFoundException`, `IOException`) antes que genéricas para un tratamiento más adecuado.

- **Validación previa**:  
  Verificar la existencia y permisos de los archivos antes de intentar operaciones de lectura/escritura.

- **Logs detallados**:  
  Implementar registros de error que proporcionen información clara sobre la naturaleza y ubicación del problema.

- **Liberación de recursos**:  
  Asegurar que todos los recursos (streams, readers, writers) sean cerrados adecuadamente en el bloque `finally` o mediante `try-with-resources`.  
