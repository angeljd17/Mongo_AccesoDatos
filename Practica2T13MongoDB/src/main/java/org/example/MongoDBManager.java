import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Clase que gestiona las operaciones para la base de datos de MongoDB.
public class MongoDBManager {
    // Conexión a la base de datos.
    private MongoClient mongoClient;
    private MongoDatabase database;

    // Constructor que inicializa la conexión a la base de datos MongoDB.
    public MongoDBManager(String connectionString, String databaseName) {
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(databaseName);
    }

    // Método para insertar un documento en una colección específica.
    public void insertDocument(String collectionName, Document document) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.insertOne(document);
    }

    // Método para actualizar documentos en una colección basado en una consulta.
    public void updateDocument(String collectionName, Document query, Document update) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.updateMany(query, update);
    }

    // Método para reemplazar documentos en una colección basado en una consulta.
    public void replaceDocument(String collectionName, Document query, Document replacement) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.replaceOne(query, replacement);
    }

    // Método para eliminar documentos en una colección basado en una consulta.
    public void deleteDocument(String collectionName, Document query) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.deleteOne(query);
    }

    // Método para obtener todos los documentos de una colección.
    public List<Document> getAllDocuments(String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> cursor = findIterable.iterator();

        List<Document> documents = new ArrayList<>();
        while (cursor.hasNext()) {
            documents.add(cursor.next());
        }
        return documents;
    }

    // Método para encontrar documentos en una colección basado en una consulta.
    public List<Document> findDocuments(String collectionName, Document query) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        FindIterable<Document> findIterable = collection.find(query);
        MongoCursor<Document> cursor = findIterable.iterator();

        List<Document> documents = new ArrayList<>();
        while (cursor.hasNext()) {
            documents.add(cursor.next());
        }
        return documents;
    }

    // Método para eliminar todos los documentos de una colección.
    public void deleteAllDocuments(String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.deleteMany(new Document());
    }

    // Utilidad 1: Obtener documentos con un campo específico igual a un valor dado.
    public List<Document> getDocumentsByFieldValue(String collectionName, String fieldName, Object value) {
        Document query = new Document(fieldName, value);
        return findDocuments(collectionName, query);
    }

    // Utilidad 2: Obtener documentos con un campo específico que contenga un valor dado.
    public List<Document> getDocumentsByFieldContains(String collectionName, String fieldName, String value) {
        Document query = new Document(fieldName, new Document("$regex", value).append("$options", "i"));
        return findDocuments(collectionName, query);
    }

    // Método para ejecutar agregaciones pipeline.
    public List<Document> runAggregate(String collectionName, List<Document> pipeline) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // Método para exportar una colección a un archivo CSV.
    public void exportCollectionToCSV(String collectionName, String fileName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        try (PrintWriter writer = new PrintWriter(new File(fileName + ".csv"))) {
            MongoCursor<Docume
