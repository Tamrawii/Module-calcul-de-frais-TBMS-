package com.example.applet;

import com.example.model.*;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AppletCalculFrais extends Applet {

    // ── Composants ──
    private TextField txtMontantBase;
    private Choice cmbTypeReservation;
    private TextField txtNbMembres;
    private Button btnValiderNb;
    private Panel panelMembres;
    private CheckboxGroup modeGroup;
    private Checkbox rdoCalcul, rdoSimulation;
    private Button btnCalculer;
    private TextArea txtResultat;

    // ── Listes dynamiques membres ──
    private ArrayList<TextField> listeAges = new ArrayList<>();
    private ArrayList<Checkbox> listeHandicapes = new ArrayList<>();
    private ArrayList<Choice> listeMetiers = new ArrayList<>();

    @Override
    public void init() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // ── Panel : infos générales ──
        Panel nord = new Panel(new GridLayout(4, 2, 8, 6));
        nord.setBackground(Color.WHITE);

        nord.add(new Label("Montant de base (TND) :"));
        txtMontantBase = new TextField("50.0");
        nord.add(txtMontantBase);

        nord.add(new Label("Type de réservation :"));
        cmbTypeReservation = new Choice();
        cmbTypeReservation.add("Individuelle");
        cmbTypeReservation.add("Duo");
        cmbTypeReservation.add("Familiale");
        cmbTypeReservation.addItemListener(e -> mettreAJourMembres());
        nord.add(cmbTypeReservation);

        nord.add(new Label("Nombre de membres (Familiale) :"));
        txtNbMembres = new TextField("2");
        txtNbMembres.setEnabled(false);
        nord.add(txtNbMembres);

        nord.add(new Label(""));
        btnValiderNb = new Button("Valider membres");
        btnValiderNb.setEnabled(false);
        btnValiderNb.addActionListener(e -> mettreAJourMembres());
        nord.add(btnValiderNb);

        add(nord, BorderLayout.NORTH);

        // ── Panel Centre : membres ──
        panelMembres = new Panel();
        panelMembres.setLayout(new GridLayout(0, 1, 5, 5));
        panelMembres.setBackground(Color.WHITE);

        Panel centreWrapper = new Panel(new BorderLayout());
        centreWrapper.setBackground(Color.WHITE);
        centreWrapper.add(new Label("-- Membres --"), BorderLayout.NORTH);
        centreWrapper.add(panelMembres, BorderLayout.CENTER);
        add(centreWrapper, BorderLayout.CENTER);

        // ── Panel : mode + bouton + résultat ──
        Panel sud = new Panel(new BorderLayout(5, 5));
        sud.setBackground(Color.WHITE);

        // Mode
        Panel panelMode = new Panel(new FlowLayout(FlowLayout.LEFT));
        panelMode.setBackground(Color.WHITE);
        panelMode.add(new Label("Mode :"));
        modeGroup = new CheckboxGroup();
        rdoCalcul = new Checkbox("Calcul réel", modeGroup, true);
        rdoSimulation = new Checkbox("Simulation", modeGroup, false);
        rdoCalcul.setBackground(Color.WHITE);
        rdoSimulation.setBackground(Color.WHITE);
        panelMode.add(rdoCalcul);
        panelMode.add(rdoSimulation);
        sud.add(panelMode, BorderLayout.NORTH);

        // Bouton
        Panel panelBtn = new Panel(new FlowLayout(FlowLayout.CENTER));
        panelBtn.setBackground(Color.WHITE);
        btnCalculer = new Button("  Calculer  ");
        btnCalculer.addActionListener(e -> lancerCalcul());
        panelBtn.add(btnCalculer);
        sud.add(panelBtn, BorderLayout.CENTER);

        // Résultat
        txtResultat = new TextArea("", 10, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);
        txtResultat.setEditable(false);
        txtResultat.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        txtResultat.setBackground(new Color(248, 248, 248));
        sud.add(txtResultat, BorderLayout.SOUTH);

        add(sud, BorderLayout.SOUTH);

        // Initialiser avec 1 membre
        mettreAJourMembres();
    }

    // ────────────────────────────────────────────
    // Mise à jour dynamique des membres
    // ────────────────────────────────────────────
    private void mettreAJourMembres() {
        String type = cmbTypeReservation.getSelectedItem();
        int nb;

        if ("Individuelle".equals(type)) {
            nb = 1;
            txtNbMembres.setEnabled(false);
            btnValiderNb.setEnabled(false);
        } else if ("Duo".equals(type)) {
            nb = 2;
            txtNbMembres.setEnabled(false);
            btnValiderNb.setEnabled(false);
        } else {
            try {
                nb = Integer.parseInt(txtNbMembres.getText().trim());
                if (nb < 2)
                    nb = 2;
            } catch (NumberFormatException e) {
                nb = 2;
            }
            txtNbMembres.setEnabled(true);
            btnValiderNb.setEnabled(true);
        }

        // Vider les listes
        panelMembres.removeAll();
        listeAges.clear();
        listeHandicapes.clear();
        listeMetiers.clear();

        String[] metiers = {
                "Medical", "Enseignant", "Police",
                "Civil", "Personnel", "Autre"
        };

        for (int i = 0; i < nb; i++) {
            Panel panelMembre = new Panel(new GridLayout(3, 2, 6, 4));
            panelMembre.setBackground(new Color(245, 245, 245));

            panelMembre.add(new Label("Membre " + (i + 1) + " - Âge :"));
            TextField txtAge = new TextField("25");
            listeAges.add(txtAge);
            panelMembre.add(txtAge);

            panelMembre.add(new Label("Handicapé :"));
            Checkbox chkHandicape = new Checkbox();
            chkHandicape.setBackground(new Color(245, 245, 245));
            listeHandicapes.add(chkHandicape);
            panelMembre.add(chkHandicape);

            panelMembre.add(new Label("Métier :"));
            Choice cmbMetier = new Choice();
            for (String metier : metiers)
                cmbMetier.add(metier);
            cmbMetier.select("Autre");
            listeMetiers.add(cmbMetier);
            panelMembre.add(cmbMetier);

            // désactiver métier si handicapé
            chkHandicape.addItemListener(e -> {
                cmbMetier.setEnabled(!chkHandicape.getState());
                if (chkHandicape.getState())
                    cmbMetier.select("Autre");
            });

            panelMembres.add(panelMembre);
        }

        panelMembres.revalidate();
        panelMembres.repaint();
    }

    // ────────────────────────────────────────────
    // Lancer le calcul
    // ────────────────────────────────────────────
    private void lancerCalcul() {

    }
}