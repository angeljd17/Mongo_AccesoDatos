package org.example;

import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// Clase que maneja la presentación y la interacción con el usuario.
public class Presentacion {
    // Instancia de MongoDBManager para gestionar las operaciones en la base de datos.
    private MongoDBManager dbManager;

    // Constructor que inicializa la instancia de MongoDBManager.
    public Presentacion(String connectionString, String databaseName) {
        dbManager = new MongoDBManager(connectionString, databaseName);
    }

    // Método principal que muestra un menú interactivo y realiza operaciones en la base de datos según la elección del usuario.
    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        // Ciclo principal que muestra el menú hasta que el usuario decida salir.
        while (continuar) {
            // Muestra el menú de opciones.
            System.out.println("Menú:");
            System.out.println("1. Insertar documento");
            System.out.println("2. Actualizar documento");
            System.out.println("3. Reemplazar documento");
            System.out.println("4. Eliminar documento");
            System.out.println("5. Mostrar todos los documentos");
            System.out.println("6. Buscar documentos");
            System.out.println("7. Eliminar todos los documentos de una colección");
            System.out.println("8. Salir");
            // Pide al usuario seleccionar una opción.
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            // Switch para manejar diferentes opciones del menú.
            switch (opcion) {
                case 1:
                    // Insertar documento
                    System.out.println("Insertar documento:");
                    System.out.print("Ingrese el nombre: ");
                    String nombre = scanner.next();
                    System.out.print("Ingrese la edad: ");
                    int edad = scanner.nextInt();
                    System.out.print("Ingrese el correo electrónico: ");
                    String correo = scanner.next();
                    Document documentToInsert = new Document("name", nombre)
                            .append("age", edad)
                            .append("email", correo);
                    dbManager.insertDocument("users", documentToInsert);
                    System.out.println("Documento insertado con éxito.");
                    break;

                case 2:
                    // Actualizar documento
                    System.out.println("Actualizar documento:");
                    System.out.print("Ingrese el nombre del campo a actualizar: ");
                    String campoActualizar = scanner.next();
                    System.out.print("Ingrese el valor actual del campo: ");
                    String valorActual = scanner.next();
                    System.out.print("Ingrese el nuevo valor para el campo: ");
                    String nuevoValor = scanner.next();
                    Document queryActualizar = new Document(campoActualizar, valorActual);
                    Document update = new Document("$set", new Document(campoActualizar, nuevoValor));
                    dbManager.updateDocument("users", queryActualizar, update);
                    System.out.println("Documento actualizado con éxito.");
                    break;
                case 3:
                    // Reemplazar documento
                    System.out.println("Reemplazar documento:");
                    System.out.print("Ingrese el nombre del campo que identifica al documento: ");
                    String campoReemplazar = scanner.next();
                    System.out.print("Ingrese el valor del campo que identifica al documento: ");
                    String valorReemplazar = scanner.next();

                    // Crear el nuevo documento con los valores actualizados
                    System.out.print("Ingrese el nuevo valor para el campo: ");
                    String nuevoValorReemplazo = scanner.next();
                    Document nuevoDocumento = new Document(campoReemplazar, nuevoValorReemplazo);

                    // Crear la consulta para identificar el documento que se va a reemplazar
                    Document queryReemplazar = new Document(campoReemplazar, valorReemplazar);

                    // Realizar el reemplazo del documento
                    dbManager.replaceDocument("users", queryReemplazar, nuevoDocumento);
                    System.out.println("Documento reemplazado con éxito.");
                    break;


                case 4:
                    // Eliminar documento
                    System.out.println("Eliminar documento:");
                    System.out.print("Ingrese el nombre del documento a eliminar: ");
                    String nombreDocumento = scanner.next();
                    Document queryEliminar = new Document("name", nombreDocumento);
                    dbManager.deleteDocument("users", queryEliminar);
                    System.out.println("Documento eliminado con éxito.");
                    break;
                case 5:
                    // Mostrar todos los documentos
                    System.out.println("Mostrar todos los documentos:");
                    List<Document> allDocuments = dbManager.getAllDocuments("users");
                    for (Document doc : allDocuments) {
                        System.out.println(doc.toJson());
                        System.out.println("Operación completada con éxito.");
                    }
                    break;
                case 6:
                    // Buscar documentos
                    System.out.println("Buscar documentos:");
                    System.out.print("Ingrese el nombre del campo a buscar: ");
                    String campo = scanner.next();
                    System.out.print("Ingrese el valor a buscar: ");
                    String valor = scanner.next();
                    List<Document> foundDocuments = dbManager.getDocumentsByFieldValue("users", campo, valor);
                    if (foundDocuments.isEmpty()) {
                        System.out.println("No se encontraron documentos que coincidan con la búsqueda.");
                    } else {
                        System.out.println("Documentos encontrados:");
                        for (Document doc : foundDocuments) {
                            System.out.println(doc.toJson());
                        }
                    }
                    System.out.println("Búsqueda completada con éxito.");
                    break;

                case 7:
                    // Eliminar todos los documentos de una colección
                    System.out.println("Eliminar todos los documentos de una colección:");
                    dbManager.deleteAllDocuments("users");
                    System.out.println("Documentos eliminados con éxito.");
                    break;
                case 8:
                    continuar = false;
                    break;
                case 9:
                    // Búsqueda utilizando agregaciones pipeline
                    System.out.println("Búsqueda utilizando agregaciones pipeline:");
                    System.out.println("Ejemplo de búsqueda por edad:");

                    // Aquí puedes definir el pipeline de agregación
                    List<Document> pipeline = Arrays.asList(
                            new Document("$match", new Document("age", new Document("$gt", 30))),
                            new Document("$group", new Document("_id", null).append("count", new Document("$sum", 1)))
                    );

                    // Ejecutar la agregación
                    List<Document> results = dbManager.runAggregate("users", pipeline);
                    if (!results.isEmpty()) {
                        Document result = results.get(0);
                        System.out.println("Número de usuarios mayores de 30 años: " + result.getInteger("count"));
                    } else {
                        System.out.println("No se encontraron resultados.");
                    }
                    System.out.println("Búsqueda completada con éxito.");
                    break;

                case 10:
                    // Exportar datos de colecciones como ficheros CSV y JSON
                    System.out.println("Exportar datos de colecciones:");
                    System.out.println("Seleccione el formato de exportación:");
                    System.out.println("1. CSV");
                    System.out.println("2. JSON");
                    int formatoExportacion = scanner.nextInt();
                    System.out.println("Ingrese el nombre del archivo de destino:");
                    String nombreArchivo = scanner.next();
                    switch (formatoExportacion) {
                        case 1:
                            dbManager.exportCollectionToCSV("users", nombreArchivo);
                            break;
                        case 2:
                            dbManager.exportCollectionToJSON("users", nombreArchivo);
                            break;
                        default:
                            System.out.println("Formato de exportación no válido.");
                            break;
                    }
                    System.out.println("Exportación completada con éxito.");
                    break;

                case 11:
                    // Importar datos de colecciones desde un fichero CSV y JSON
                    System.out.println("Importar datos de colecciones:");
                    System.out.println("Seleccione el formato de importación:");
                    System.out.println("1. CSV");
                    System.out.println("2. JSON");
                    int formatoImportacion = scanner.nextInt();
                    System.out.println("Ingrese la ruta del archivo de origen:");
                    String rutaArchivo = scanner.next();
                    switch (formatoImportacion) {
                        case 1:
                            dbManager.importCSVToCollection("users", rutaArchivo);
                            break;
                        case 2:
                            dbManager.importJSONToCollection("users", rutaArchivo);
                            break;
                        default:
                            System.out.println("Formato de importación no válido.");
                            break;
                    }
                    System.out.println("Importación completada con éxito.");
                    break;

                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        }
        scanner.close(); // Cierra el scanner al final del método.
    }
}
