package org.example;

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
import java.util.Arrays;
import java.util.List;

public class MongoDBManager {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBManager(String connectionString, String databaseName) {
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(databaseName);
    }

    public void insertDocument(String collectionName, Document document) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.insertOne(document);
    }

    public void updateDocument(String collectionName, Document query, Document update) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.updateMany(query, update);
    }

    public void replaceDocument(String collectionName, Document query, Document replacement) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.replaceOne(query, replacement);
    }

    public void deleteDocument(String collectionName, Document query) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.deleteOne(query);
    }

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

    public void deleteAllDocuments(String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.deleteMany(new Document());
    }

    // Utilidad 1: Obtener documentos con un campo específico igual a un valor dado
    public List<Document> getDocumentsByFieldValue(String collectionName, String fieldName, Object value) {
        Document query = new Document(fieldName, value);
        return findDocuments(collectionName, query);
    }

    // Utilidad 2: Obtener documentos con un campo específico que contenga un valor dado
    public List<Document> getDocumentsByFieldContains(String collectionName, String fieldName, String value) {
        Document query = new Document(fieldName, new Document("$regex", value).append("$options", "i"));
        return findDocuments(collectionName, query);
    }
    // Método para ejecutar agregaciones pipeline
    public List<Document> runAggregate(String collectionName, List<Document> pipeline) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // Método para exportar una colección a un archivo CSV
    public void exportCollectionToCSV(String collectionName, String fileName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        try (PrintWriter writer = new PrintWriter(new File(fileName + ".csv"))) {
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                writer.println(doc.toJson());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Método para exportar una colección a un archivo JSON
    public void exportCollectionToJSON(String collectionName, String fileName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        try (PrintWriter writer = new PrintWriter(new File(fileName + ".json"))) {
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                writer.println(doc.toJson());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Método para importar datos desde un archivo CSV a una colección
    public void importCSVToCollection(String collectionName, String filePath) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            List<String> headers = Arrays.asList(headerLine.split(","));

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Document doc = new Document();
                for (int i = 0; i < headers.size(); i++) {
                    doc.append(headers.get(i), values[i]);
                }
                collection.insertOne(doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para importar datos desde un archivo JSON a una colección
    public void importJSONToCollection(String collectionName, String filePath) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Document doc = Document.parse(line);
                collection.insertOne(doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



