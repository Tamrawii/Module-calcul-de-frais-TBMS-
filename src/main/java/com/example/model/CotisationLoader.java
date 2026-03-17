package com.example.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CotisationLoader {
    private String path;
    private Map<String, String> cotisation;

    public CotisationLoader(String p) {
        this.cotisation = new HashMap<>();
        this.path = p;
    }

    public void charger() {
        if (fichierExiste()) {
            try (BufferedReader reader = new BufferedReader(
                    new FileReader(this.path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        String m = parts[0].trim();
                        String c = parts[1].trim();
                        this.cotisation.put(m, c);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Fichier inéxistant");
        }
    }

    public boolean fichierExiste() {
        File file = new File(path);
        return file.exists()
                && file.isFile()
                && file.canRead()
                && file.length() > 0;
    }

    public Float getCotisation(String metier) {
        this.charger();
        return Float.parseFloat(this.cotisation.get(metier).split(" ")[0]);
    }

    public String getDevis(String metier) {
        this.charger();
        return this.cotisation.get(metier).split(" ")[1];
    }
}
