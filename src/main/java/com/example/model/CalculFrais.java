package com.example.model;

import java.util.ArrayList;

public class CalculFrais {
    private CotisationLoader loader;
    private float mb;

    public CalculFrais(String path, float mb) {
        this.loader = new CotisationLoader(path);
        this.mb = mb;
    }

    public void calculerPourMembre(Membre m) {
        if (m.getMetier() == TypeMetier.Autre)
            m.setMontant(mb * choisirMeilleureOption(m));
        else
            m.setMontant(appliquerCotisation(m));
    }

    public void calculerReservation(Reservation r) {
        ArrayList<Membre> listeMembres = r.getListeMembres();
        if (r.getTypereservation() == TypeReservation.Individuelle)
            calculerPourMembre(listeMembres.get(0));
        else if (r.getTypereservation() == TypeReservation.Duo) {
            Membre m1 = listeMembres.get(0);
            Membre m2 = listeMembres.get(1);

            m1.setMontant(mb * 0.96f);
            m2.setMontant(mb * 0.96f);
        }

        else if (r.nbMembres() == 3) {
            for (Membre m : listeMembres) {
                calculerPourMembre(m);
            }
        } else if (r.nbMembres() >= 4) {
            for (int i = 0; i < r.nbMembres(); i++) {
                Membre m = listeMembres.get(i);

                if ((i + 1) % 4 == 0) {
                    calculerPourMembre(m);
                    m.setMontant(m.getMontant() * 0.95f);
                } else
                    calculerPourMembre(m);
            }
        }
    }

    public float choisirMeilleureOption(Membre m) {
        if (m.estSenior())
            return 0.1f;
        else if (m.estHandicape())
            return 0.15f;
        else if (m.estMineur())
            return 0.5f;
        else
            return 1.0f;
    }

    public float appliquerCotisation(Membre m) {
        return mb - loader.getCotisation(m.getMetier().toString());
    }

    public void afficherCalcul(Reservation r) {
        calculerReservation(r);

        ArrayList<Membre> membres = r.getListeMembres();
        float total = 0;

        for (Membre m : membres) {
            total += m.getMontant();
        }

        r.setMontantTotal(total);

        System.out.println("====================");
        System.out.println("| CALCUL DES FRAIS |");
        System.out.println("====================");
        System.out.println("Montant de base     : " + mb + " TND");
        System.out.println("Type de réservation : " + r.getTypereservation());
        System.out.println("Nombre de membres   : " + r.nbMembres());
        System.out.println("--------------------------------");
        System.out.println("TOTAL À PAYER : " + total + " TND");
        System.out.println("--------------------------------");
    }

    public void simuler(Reservation r) {
        System.out.println("===================");
        System.out.println("| MODE SIMULATION |");
        System.out.println("===================");
        System.out.println("Montant de base     : " + mb + " TND");
        System.out.println("Type de réservation : " + r.getTypereservation());
        System.out.println("--------------------------------");

        ArrayList<Membre> membres = r.getListeMembres();
        float total = 0;

        for (int i = 0; i < membres.size(); i++) {
            Membre m = membres.get(i);

            // calculer localement sans modifier le membre
            float montant;

            if (r.getTypereservation() == TypeReservation.Duo) {
                montant = mb * 0.96f;
            } else if (m.getMetier() != TypeMetier.Autre) {
                montant = appliquerCotisation(m);
            } else {
                montant = mb * choisirMeilleureOption(m);
            }

            // réduction 4e membre familial
            if (r.getTypereservation() == TypeReservation.Familiale
                    && (i + 1) % 4 == 0) {
                montant = montant * 0.95f;
            }

            System.out.println("-Membre " + (i + 1) + "-");
            System.out.println("Age : " + m.getAge() + " ans");

            if (r.getTypereservation() != TypeReservation.Duo) {
                if (m.estHandicape()) {
                    System.out.println("Statut : Handicapé -> 15%");
                } else if (m.estMineur()) {
                    System.out.println("Statut : Mineur -> 50%");
                } else if (m.estSenior()) {
                    System.out.println("Statut : Senior -> 10%");
                } else {
                    System.out.println("Statut : Adulte -> 0%");
                }
            }

            if (!m.estHandicape() && m.getMetier() != TypeMetier.Autre) {
                System.out.println("Métier : " + m.getMetier());
                System.out.println("Cotisation : -" + loader.getCotisation(m.getMetier().toString()) + " TND");
            }

            if (r.getTypereservation() == TypeReservation.Duo)
                System.out.println("Réduction duo : -4%");

            if (r.getTypereservation() == TypeReservation.Familiale && (i + 1) % 4 == 0)
                System.out.println("Réduction familial : -5% (4e membre)");

            System.out.println("Montant estimé : " + montant + " TND");
            System.out.println("--------------------------------");

            total += montant;
        }

        System.out.println("TOTAL ESTIMÉ : " + total + " TND");
        System.out.println("--------------------------------");
    }
}
