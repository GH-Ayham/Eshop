package bib.local.ui.gui.panels;

import bib.local.domain.EShop;
import bib.local.domain.exceptions.*;
import bib.local.entities.Artikel;
import bib.local.entities.Kunde;
import bib.local.entities.Massengutartikel;
import bib.local.entities.WarenkorbEintrag;
import bib.local.ui.gui.models.ArtikelInWarenkorbTableModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Panel zum Anzeigen des Warenkorbs eines Kunden.
 */
public class WarenkorbMenuPanel extends JPanel {

    private EShop shop;
    private Kunde eingeloggterKunde;
    private ArtikelInWarenkorbTablePanel artikelInWarenkorbTablePanel;
    private ArtikelInWarenkorbTableModel artikelInWarenkorbTableModel;
    private JButton zurueckButton;
    private JButton rechnungButton;
    private JButton entfehrnenButton;
    private JButton aendereArtikelbestand;

    /**
     * Konstruktor für WarenkorbMenuPanel.
     *
     * @param cardLayout       der CardLayout-Manager
     * @param mainPanel        das Hauptpanel, das alle Kartenpanels enthält
     * @param shop             die EShop-Instanz
     * @param eingeloggterKunde der eingeloggte Kunde
     */
    public WarenkorbMenuPanel(CardLayout cardLayout, JPanel mainPanel, EShop shop, Kunde eingeloggterKunde) throws WarenkorbLeerException{
        this.shop = shop;
        this.eingeloggterKunde = eingeloggterKunde;
        setupUI();
        setupEvents(cardLayout, mainPanel);
    }

    /**
     * Initialisiert die Benutzeroberfläche.
     */
    private void setupUI() throws WarenkorbLeerException{
        setLayout(new BorderLayout());

        List<WarenkorbEintrag> artikelListe = shop.getWarenkorbVW().WarenkorbAnzeigen(eingeloggterKunde);
        if (artikelListe.isEmpty()) {
            throw new WarenkorbLeerException("Der Warenkorb ist leer.");
        }

        if (artikelInWarenkorbTablePanel == null) {
            artikelInWarenkorbTablePanel = new ArtikelInWarenkorbTablePanel(artikelListe, shop, eingeloggterKunde);
            add(new JScrollPane(artikelInWarenkorbTablePanel), BorderLayout.CENTER);
        } else {
            artikelInWarenkorbTablePanel.updateArtikelliste(artikelListe);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        zurueckButton = new JButton("Zurück");
        rechnungButton = new JButton("Rechnung anzeigen");
        entfehrnenButton = new JButton("Artikel entfehrnen");
        aendereArtikelbestand = new JButton("Artikel Bestand änderen");

        buttonPanel.add(zurueckButton);
        buttonPanel.add(rechnungButton);
        buttonPanel.add(entfehrnenButton);
        buttonPanel.add(aendereArtikelbestand);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Setzt die Ereignishandler für die Komponenten.
     *
     * @param cardLayout der CardLayout-Manager
     * @param mainPanel  das Hauptpanel, das alle Kartenpanels enthält
     */
    private void setupEvents(CardLayout cardLayout, JPanel mainPanel) {
        zurueckButton.addActionListener(e -> cardLayout.show(mainPanel, "kundeMenu"));

        rechnungButton.addActionListener(e -> {
            JPanel rechnungPanel = new RechnungPanel(shop, eingeloggterKunde, cardLayout, mainPanel);
            mainPanel.add(rechnungPanel, "rechnung");
            cardLayout.show(mainPanel, "rechnung");
        });

        entfehrnenButton.addActionListener(e -> {
            try {
                entferneArtikelAusWarenkorb(cardLayout, mainPanel);
            } catch (IOException | WarenkorbLeerException ex) {
                JOptionPane.showMessageDialog(WarenkorbMenuPanel.this, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        aendereArtikelbestand.addActionListener(e -> {
            try {
                aendereArtikelbestand(cardLayout, mainPanel);
            } catch (IOException | ArtikelNichtGefundenException | WarenkorbLeerException ex) {
                JOptionPane.showMessageDialog(WarenkorbMenuPanel.this, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

   /* @Override
    public void updateListe(Kunde eingeloggterKunde) {
        try {
            artikelInWarenkorbTablePanel.updateArtikelliste(shop.getWarenkorbVW().WarenkorbAnzeigen(eingeloggterKunde));
        } catch (WarenkorbLeerException e) {
            throw new RuntimeException(e);
        }
    }*/

    private void entferneArtikelAusWarenkorb(CardLayout cardLayout, JPanel mainPanel) throws IOException, WarenkorbLeerException {
        int selectedRow = artikelInWarenkorbTablePanel.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie einen Artikel aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        WarenkorbEintrag eintrag = artikelInWarenkorbTablePanel.getModel().getArtikelAt(selectedRow);
        int eintragNr = eintrag.getArtikel().getNummer();

        try {
            shop.entferneArtikelAusWarenkorb(eingeloggterKunde, eintragNr);
            JOptionPane.showMessageDialog(this, "Artikel wurde aus dem Warenkorb entfernt.");

            // Aktualisieren der Tabelle nach der Entfernung
            try {
                List<WarenkorbEintrag> aktualisierteArtikelListe = shop.getWarenkorbVW().WarenkorbAnzeigen(eingeloggterKunde);
                artikelInWarenkorbTablePanel.updateArtikelliste(aktualisierteArtikelListe);

            } catch (WarenkorbLeerException e) {
                JOptionPane.showMessageDialog(this, "Der Warenkorb ist jetzt leer. Sie werden zum Hauptmenü weitergeleitet.");
                cardLayout.show(mainPanel, "kundeMenu");
            }

        } catch (BestandPasstNichtMitPackungsGroesseException | NichtGenuegendBestandException |
                 ArtikelNichtGefundenException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aendereArtikelbestand(CardLayout cardLayout, Container mainPanel) throws IOException, ArtikelNichtGefundenException, WarenkorbLeerException {
        int selectedRow = artikelInWarenkorbTablePanel.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie einen Artikel aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        WarenkorbEintrag eintrag = artikelInWarenkorbTablePanel.getModel().getArtikelAt(selectedRow);
        int artikelNr = eintrag.getArtikel().getNummer();
        Artikel artikel = shop.getArtikelVW().sucheArtikel(artikelNr);

        try {
            if (artikel instanceof Massengutartikel) {
                Massengutartikel massengutArtikel = (Massengutartikel) artikel;
                String packungsGroesseInfo = "Die neue Menge nur in Vielfachen dieser Packungsgröße: " + massengutArtikel.getPackungsGroesse();
                String neueMengeStr = JOptionPane.showInputDialog(this, packungsGroesseInfo + "\nNeue Menge:");
                int neueMenge = Integer.parseInt(neueMengeStr);
                shop.artikelBestandAendern(eingeloggterKunde, massengutArtikel, neueMenge);
            } else {
                String neueMengeStr = JOptionPane.showInputDialog(this, "Neue Menge:");
                int neueMenge = Integer.parseInt(neueMengeStr);
                shop.artikelBestandAendern(eingeloggterKunde, artikel, neueMenge);
            }
            JOptionPane.showMessageDialog(this, "Artikelbestand wurde geändert.");

            // Aktualisieren der Tabelle nach der Entfernung
            try {
                List<WarenkorbEintrag> aktualisierteArtikelListe = shop.getWarenkorbVW().WarenkorbAnzeigen(eingeloggterKunde);
                artikelInWarenkorbTablePanel.updateArtikelliste(aktualisierteArtikelListe);

            } catch (WarenkorbLeerException e) {
                JOptionPane.showMessageDialog(this, "Der Warenkorb ist jetzt leer. Sie werden zum Hauptmenü weitergeleitet.");
                cardLayout.show(mainPanel, "kundeMenu");
            }
        } catch (ArtikelNichtImWarenkorbGefunden e) {
            JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (NichtGenuegendBestandException | BestandPasstNichtMitPackungsGroesseException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Aktualisieren des Bestands: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }



}