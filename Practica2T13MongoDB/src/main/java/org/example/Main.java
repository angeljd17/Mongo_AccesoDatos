package org.example;

import org.bson.Document;

import java.awt.*;
import java.util.List;

// Clase principal del programa.
public class Main {
    // Método principal que se ejecuta al iniciar la aplicación.
    public static void main(String[] args) {
        // Crea una instancia de la clase Presentacion, que parece ser una clase personalizada.
        // Se le proporciona la URL de la base de datos ("mongodb://localhost:27017") y el nombre de la base de datos ("myDatabase").
        Presentacion presentacion = new Presentacion("mongodb://localhost:27017", "myDatabase");

        // Llama al método mostrarMenu() de la instancia de Presentacion.
        // Este método contiene la lógica para mostrar un menú interactivo de la base de datos MongoDB.
        presentacion.mostrarMenu();
    }
}