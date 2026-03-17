package com.example.model;

import java.util.ArrayList;

public class Reservation {
    private ArrayList<Membre> listeMembres;
    private TypeReservation typereservation;
    private float montantTotal;

    public Reservation() {
        this.listeMembres = new ArrayList<>();
    }

    public int nbMembres() {
        return this.listeMembres.size();
    }

    public void ajouterMembre(Membre m) {
        this.listeMembres.add(m);
    }

    public ArrayList<Membre> getListeMembres() {
        return this.listeMembres;
    }

    public void setListMembres(ArrayList<Membre> l) {
        this.listeMembres = l;
    }

    public TypeReservation getTypereservation() {
        return this.typereservation;
    }

    public void setTypereservation(TypeReservation typereservation) {
        this.typereservation = typereservation;
    }

    public float getMontantTotal() {
        return this.montantTotal;
    }

    public void setMontantTotal(float montantTotal) {
        this.montantTotal = montantTotal;
    }

}
