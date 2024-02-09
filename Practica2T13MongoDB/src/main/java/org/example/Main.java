package org.example;

import org.bson.Document;

import java.awt.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Presentacion presentacion = new Presentacion("mongodb://localhost:27017", "myDatabase");
        presentacion.mostrarMenu();
    }
}
