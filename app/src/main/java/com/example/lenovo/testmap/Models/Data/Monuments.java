package com.example.lenovo.testmap.Models.Data;

import java.util.List;

public class Monuments
{
    private int id;
    private String nom;
    private String description;
    private String type;
    private List<byte[]> images;

    public Monuments(int id, String nom, String description, String type, List<byte[]> images) {
        this.id=id;
        this.nom = nom;
        this.description = description;
        this.images = images;
        this.type=type;
    }

    public Monuments(int id, String nom, String description, String type) {
        this.id=id;
        this.nom = nom;
        this.description = description;
        this.type=type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

     public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString()
    {
        return ("Nom monument: " + this.nom + "Description: " + this.description + "type: " + this.type);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
