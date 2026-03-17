package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Déclaration
        float montantBase;
        int age;
        int rep;
        int repType;
        int nbMembre = 0;
        boolean modeSimulation = false;
        Reservation reservation = new Reservation();
        boolean estHandicape;
        // I/O
        Scanner sc = new Scanner(System.in);
        System.out.println("Saisir le montant de base:");
        montantBase = sc.nextFloat();

        System.out.println("Type de réservation:\n 1.Individuelle\n 2.Duo\n 3.Familiale");
        repType = sc.nextInt();

        if (repType == 1) {
            reservation.setTypereservation(TypeReservation.Individuelle);
            nbMembre = 1;
        } else if (repType == 2) {
            reservation.setTypereservation(TypeReservation.Duo);
            nbMembre = 2;
        }
        if (repType == 3) {
            reservation.setTypereservation(TypeReservation.Familiale);
            System.out.println("Saisir le nombre des membres:");
            nbMembre = sc.nextInt();
        }

        for (int i = 0; i < nbMembre; i++) {
            Membre m = new Membre();
            System.out.println("Entrer l'âge " + (i + 1));
            m.setAge(sc.nextInt());

            if (repType != 2) {
                System.out.println("Handicapé?\n 1.Oui\n 2.Non");
                estHandicape = sc.nextInt() == 1;
                m.setEstHandicape(estHandicape);

                if (!estHandicape & !m.estMineur() & !m.estSenior()) {
                    System.out.println(
                            "Métier:\n 1.Cadre médicale\n 2.Enseignants\n 3.Agents de police\n 4.Protection civile\n 5.Personnel de l'organisation de transport elle-même\n 6.Autre");
                    rep = sc.nextInt();
                    switch (rep) {
                        case 1:
                            m.setMetier(TypeMetier.Medical);
                            break;
                        case 2:
                            m.setMetier(TypeMetier.Enseignant);
                            break;
                        case 3:
                            m.setMetier(TypeMetier.Police);
                            break;
                        case 4:
                            m.setMetier(TypeMetier.Civil);
                            break;
                        case 5:
                            m.setMetier(TypeMetier.Personnel);
                            break;
                        case 6:
                            m.setMetier(TypeMetier.Autre);
                            break;
                        default:
                            m.setMetier(TypeMetier.Autre);
                            System.out.println("Métier invalide, 'Autre' appliqué.");
                            break;
                    }
                }
            }

            reservation.ajouterMembre(m);
        }

        System.out.println("\nMode:\n 1.Calcul réel\n 2.Simulation");
        rep = sc.nextInt();
        sc.close();

        CalculFrais calc = new CalculFrais("src/main/java/com/example/cotisations.cfg", montantBase);

        if (rep == 1)
            calc.afficherCalcul(reservation);
        else
            calc.simuler(reservation);

    }
}