package com.example.GUI;

import com.example.model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

public class MainGUI extends JFrame {

    // ── Composants globaux ──
    private JTextField txtMontantBase;
    private JComboBox<String> cmbTypeReservation;
    private JSpinner spnNbMembres;
    private JPanel panelMembres;
    private JRadioButton rdoCalcul, rdoSimulation;
    private JTextArea txtResultat;

    // ── Listes dynamiques des champs membres ──
    private ArrayList<JSpinner> listeAges = new ArrayList<>();
    private ArrayList<JCheckBox> listeHandicapes = new ArrayList<>();
    private ArrayList<JComboBox<String>> listeMetiers = new ArrayList<>();

    public MainGUI() {
        setTitle("Calcul des Frais de Réservation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(creerSectionGenerale());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(creerSectionMembres());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(creerSectionMode());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(creerBoutonCalculer());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(creerSectionResultat());

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll);

        setVisible(true);
    }

    // ────────────────────────────────────────────
    // Section 1 — Informations générales
    // ────────────────────────────────────────────
    private JPanel creerSectionGenerale() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 8));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Informations générales"));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("Montant de base (TND) :"));
        txtMontantBase = new JTextField("50.0");
        panel.add(txtMontantBase);

        panel.add(new JLabel("Type de réservation :"));
        cmbTypeReservation = new JComboBox<>(new String[] {
                "Individuelle", "Duo", "Familiale"
        });
        cmbTypeReservation.addActionListener(e -> mettreAJourMembres());
        panel.add(cmbTypeReservation);

        panel.add(new JLabel("Nombre de membres (Familiale) :"));
        spnNbMembres = new JSpinner(new SpinnerNumberModel(2, 2, 20, 1));
        spnNbMembres.setEnabled(false);
        spnNbMembres.addChangeListener(e -> mettreAJourMembres());
        panel.add(spnNbMembres);

        return panel;
    }

    // ────────────────────────────────────────────
    // Section 2 — Membres dynamiques
    // ────────────────────────────────────────────
    private JPanel creerSectionMembres() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Membres"));

        panelMembres = new JPanel();
        panelMembres.setLayout(new BoxLayout(panelMembres, BoxLayout.Y_AXIS));
        panelMembres.setBackground(Color.WHITE);

        wrapper.add(panelMembres, BorderLayout.CENTER);
        mettreAJourMembres();

        return wrapper;
    }

    private void mettreAJourMembres() {
        String type = (String) cmbTypeReservation.getSelectedItem();
        int nb;

        if ("Individuelle".equals(type)) {
            nb = 1;
            spnNbMembres.setEnabled(false);
        } else if ("Duo".equals(type)) {
            nb = 2;
            spnNbMembres.setEnabled(false);
        } else {
            nb = (int) spnNbMembres.getValue();
            spnNbMembres.setEnabled(true);
        }

        panelMembres.removeAll();
        listeAges.clear();
        listeHandicapes.clear();
        listeMetiers.clear();

        String[] metiers = {
                "Medical", "Enseignant", "Police",
                "Civil", "Personnel", "Autre"
        };

        for (int i = 0; i < nb; i++) {
            JPanel panelMembre = new JPanel(new GridLayout(3, 2, 8, 5));
            panelMembre.setBackground(new Color(248, 248, 248));
            panelMembre.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    "Membre " + (i + 1)));

            // Âge
            panelMembre.add(new JLabel("  Âge :"));
            JSpinner spnAge = new JSpinner(new SpinnerNumberModel(25, 0, 120, 1));
            listeAges.add(spnAge);
            panelMembre.add(spnAge);

            // Handicapé
            panelMembre.add(new JLabel("  Handicapé :"));
            JCheckBox chkHandicape = new JCheckBox();
            listeHandicapes.add(chkHandicape);
            panelMembre.add(chkHandicape);

            // Métier
            panelMembre.add(new JLabel("  Métier :"));
            JComboBox<String> cmbMetier = new JComboBox<>(metiers);
            cmbMetier.setSelectedItem("Autre");
            listeMetiers.add(cmbMetier);
            panelMembre.add(cmbMetier);

            // désactiver métier si handicapé
            chkHandicape.addActionListener(e -> {
                cmbMetier.setEnabled(!chkHandicape.isSelected());
                if (chkHandicape.isSelected())
                    cmbMetier.setSelectedItem("Autre");
            });

            panelMembres.add(panelMembre);
            panelMembres.add(Box.createVerticalStrut(5));
        }

        panelMembres.revalidate();
        panelMembres.repaint();
    }

    // ────────────────────────────────────────────
    // Section 3 — Mode
    // ────────────────────────────────────────────
    private JPanel creerSectionMode() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Mode"));
        panel.setBackground(Color.WHITE);

        rdoCalcul = new JRadioButton("Calcul réel", true);
        rdoSimulation = new JRadioButton("Simulation");
        rdoCalcul.setBackground(Color.WHITE);
        rdoSimulation.setBackground(Color.WHITE);

        ButtonGroup group = new ButtonGroup();
        group.add(rdoCalcul);
        group.add(rdoSimulation);

        panel.add(rdoCalcul);
        panel.add(rdoSimulation);

        return panel;
    }

    // ────────────────────────────────────────────
    // Bouton Calculer
    // ────────────────────────────────────────────
    private JPanel creerBoutonCalculer() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);

        JButton btnCalculer = new JButton("Calculer");
        btnCalculer.setPreferredSize(new Dimension(200, 35));
        btnCalculer.setFocusPainted(false);
        btnCalculer.addActionListener(e -> lancerCalcul());
        panel.add(btnCalculer);

        return panel;
    }

    // ────────────────────────────────────────────
    // Section 4 — Résultat
    // ────────────────────────────────────────────
    private JPanel creerSectionResultat() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Résultat"));
        panel.setBackground(Color.WHITE);

        txtResultat = new JTextArea(10, 50);
        txtResultat.setEditable(false);
        txtResultat.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        txtResultat.setBackground(new Color(248, 248, 248));
        txtResultat.setBorder(new EmptyBorder(8, 8, 8, 8));

        panel.add(new JScrollPane(txtResultat), BorderLayout.CENTER);
        return panel;
    }

    // ────────────────────────────────────────────
    // Lancer le calcul
    // ────────────────────────────────────────────
    private void lancerCalcul() {
        txtResultat.setText("");

        // Lire montant de base
        float montantBase;
        try {
            montantBase = Float.parseFloat(txtMontantBase.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Montant de base invalide !",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Construire la réservation
        Reservation r = new Reservation();
        String type = (String) cmbTypeReservation.getSelectedItem();

        if ("Individuelle".equals(type))
            r.setTypereservation(TypeReservation.Individuelle);
        else if ("Duo".equals(type))
            r.setTypereservation(TypeReservation.Duo);
        else
            r.setTypereservation(TypeReservation.Familiale);

        // Construire les membres
        for (int i = 0; i < listeAges.size(); i++) {
            Membre m = new Membre();
            m.setAge((int) listeAges.get(i).getValue());

            boolean handicape = listeHandicapes.get(i).isSelected();
            m.setEstHandicape(handicape);

            if (!handicape) {
                String metierChoisi = (String) listeMetiers.get(i).getSelectedItem();
                switch (metierChoisi) {
                    case "Medical":
                        m.setMetier(TypeMetier.Medical);
                        break;
                    case "Enseignant":
                        m.setMetier(TypeMetier.Enseignant);
                        break;
                    case "Police":
                        m.setMetier(TypeMetier.Police);
                        break;
                    case "Civil":
                        m.setMetier(TypeMetier.Civil);
                        break;
                    case "Personnel":
                        m.setMetier(TypeMetier.Personnel);
                        break;
                    default:
                        m.setMetier(TypeMetier.Autre);
                        break;
                }
            } else {
                m.setMetier(TypeMetier.Autre);
            }

            r.ajouterMembre(m);
        }

        // Rediriger System.out vers txtResultat
        CalculFrais calc = new CalculFrais("cotisations.cfg", montantBase);
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream old = System.out;
        System.setOut(ps);

        if (rdoCalcul.isSelected())
            calc.afficherCalcul(r);
        else
            calc.simuler(r);

        System.out.flush();
        System.setOut(old);
        txtResultat.setText(baos.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI());
    }
}