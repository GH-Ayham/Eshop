package bib.local.ui.gui.panels;

import bib.local.domain.EShop;
import bib.local.domain.exceptions.ArtikelNichtGefundenException;
import bib.local.domain.exceptions.FalscheEingabeTypException;
import bib.local.entities.Artikel;
import bib.local.entities.Mitarbeiter;
import bib.local.entities.WarenkorbEintrag;
import bib.local.ui.gui.models.ArtikelTableModel;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Panel zum Verwalten von Artikeln und Mitarbeitern im Shop.
 */
public class MitarbeiterMenuPanel extends JPanel implements AddArticlePanel.AddArticleListener, RegisterMitarbeiterPanel.RegisterMitarbeiterListener {
    private EShop shop;
    private ArtikelTablePanel artikelTablePanel;
    private ArtikelTableModel tableModel;
    private Mitarbeiter eingeloggterMitarbeiter;
    private JButton editArticleButton;
    private JButton deleteArticleButton;
    private JButton logoutButton;

    /**
     * Konstruktor für MitarbeiterMenuPanel.
     *
     * @param cardLayout Das CardLayout des übergeordneten Containers.
     * @param mainPanel  Das Hauptpanel, das die verschiedenen Ansichten enthält.
     * @param shop       Die EShop-Instanz.
     */
    public MitarbeiterMenuPanel(CardLayout cardLayout, JPanel mainPanel, EShop shop, Mitarbeiter eingeloggterMitarbeiter) {
        this.shop = shop;
        this.eingeloggterMitarbeiter = eingeloggterMitarbeiter;
        setupUI();
        setupEvents(cardLayout, mainPanel);
    }



    /**
     * Initialisiert die Benutzeroberfläche.
     */
    private void setupUI() {
        setLayout(new BorderLayout());

        // Oberer Bereich für Begrüßung und Buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        String mitarbeiterName = (shop.getBenutzer() != null) ? shop.getBenutzer().getName() : "Unbekannter Benutzer";
        JLabel welcomeLabel = new JLabel("Willkommen Mitarbeiter/in: " + mitarbeiterName);
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        editArticleButton = new JButton("Artikel bearbeiten");
        buttonPanel.add(editArticleButton);

        deleteArticleButton = new JButton("Artikel löschen");
        buttonPanel.add(deleteArticleButton);

        logoutButton = new JButton("Ausloggen");
        buttonPanel.add(logoutButton);

        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Linkes Panel für RegisterMitarbeiterPanel
        RegisterMitarbeiterPanel registerPanel = new RegisterMitarbeiterPanel(shop, this);
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(registerPanel, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // Mittleres Panel für AddArticlePanel
        AddArticlePanel addArticlePanel = new AddArticlePanel(shop, this);
        add(addArticlePanel, BorderLayout.CENTER);

        // Rechtes Panel für ArtikelTablePanel
        artikelTablePanel = new ArtikelTablePanel(shop);
        tableModel = artikelTablePanel.getModel();
        add(new JScrollPane(artikelTablePanel), BorderLayout.EAST);
    }

    /**
     * Legt die Ereignishandler für die Komponenten fest.
     *
     * @param cardLayout der CardLayout-Manager
     * @param mainPanel  das Hauptpanel, das alle Kartenpanels enthält
     */
    private void setupEvents(CardLayout cardLayout, JPanel mainPanel) {
        editArticleButton.addActionListener(e -> {
            try {
                bearbeiteArtikel();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteArticleButton.addActionListener(e -> {
            try {
                loescheArtikel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        logoutButton.addActionListener(e -> {
            shop.setBenutzer(null);
            JOptionPane.showMessageDialog(null, "Sie sind ausgeloggt.");
            cardLayout.show(mainPanel, "login");
        });
    }

    @Override
    public void onArticleAdded(Artikel artikel) {
        artikelTablePanel.updateArtikelliste(shop.gibAlleArtikel());
    }

    @Override
    public void onEmployeeRegistered(Mitarbeiter mitarbeiter) {
        // Aktionen nach der Registrierung des Mitarbeiters
        JOptionPane.showMessageDialog(this, "Mitarbeiter erfolgreich registriert: " + mitarbeiter.getName());
    }

    private void loescheArtikel() throws IOException{
        int selectedRow = artikelTablePanel.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie einen Artikel aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Artikel artikel = artikelTablePanel.getModel().getArtikelAt(selectedRow);
        int artikelNr = artikel.getNummer();

        try{
            shop.loescheArtikel(artikelNr);
            //shop.schreibeArtikel();
            JOptionPane.showMessageDialog(this, "Artikel wurde erfolgreich gelöscht.");

            // Aktualisieren der Tabelle nach der Entfernung
            List<Artikel> aktualisierteArtikelListe = shop.gibAlleArtikel();
            artikelTablePanel.updateArtikelliste(aktualisierteArtikelListe);

        } catch (ArtikelNichtGefundenException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Bearbeitet den ausgewählten Artikel.
     *
     * @throws IOException Falls ein Fehler beim Schreiben der Artikeldaten auftritt.
     */
    private void bearbeiteArtikel() throws IOException {
        int selectedRow = artikelTablePanel.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie einen Artikel aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Artikel artikel = tableModel.getArtikelAt(selectedRow);
        BearbeiteArtikelPanel bearbeitePanel = new BearbeiteArtikelPanel(shop, artikel);
        boolean updated = bearbeitePanel.showDialog();
        if (updated) {
            // Aktualisieren der Tabelle nach der Entfernung
            List<Artikel> aktualisierteArtikelListe = shop.gibAlleArtikel();
            artikelTablePanel.updateArtikelliste(aktualisierteArtikelListe);
        }
    }

    private String getMitarbeiterName() {
        // Ersetzen Sie dies durch die tatsächliche Logik zum Abrufen des Mitarbeiternamens
        return "MitarbeiterName";
    }
}
