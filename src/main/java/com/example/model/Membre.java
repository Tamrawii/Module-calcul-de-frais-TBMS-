package com.example.model;

public class Membre {
    private int age;
    private boolean estHandicape;
    private TypeMetier metier;
    private float montant;

    public Membre() {
        this.metier = TypeMetier.Autre;
    }

    public boolean estMineur() {
        return this.age < 18;
    }

    public boolean estSenior() {
        return this.age > 70;
    }

    public boolean estHandicape() {
        return this.estHandicape;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEstHandicape(boolean estHandicape) {
        this.estHandicape = estHandicape;
    }

    public TypeMetier getMetier() {
        return this.metier;
    }

    public void setMetier(TypeMetier metier) {
        this.metier = metier;
    }

    public float getMontant() {
        return this.montant;
    }

    public void setMontant(float m) {
        this.montant = m;
    }
}
