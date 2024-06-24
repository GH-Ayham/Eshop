package bib.local.ui.gui.panels;

import bib.local.domain.EShop;
import bib.local.domain.exceptions.WarenkorbLeerException;
import bib.local.entities.Kunde;
import bib.local.entities.WarenkorbEintrag;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel zum Anzeigen des Warenkorbs eines Kunden.
 */
public class WarenkorbMenuPanel extends JPanel {

    private EShop shop;
    private Kunde eingeloggterKunde;
    private ArtikelInWarenkorbTablePanel artikelInWarenkorbTablePanel;
    private JButton zurueckButton;
    private JButton rechnungButton;

    /**
     * Konstruktor für WarenkorbMenuPanel.
     *
     * @param cardLayout       der CardLayout-Manager
     * @param mainPanel        das Hauptpanel, das alle Kartenpanels enthält
     * @param shop             die EShop-Instanz
     * @param eingeloggterKunde der eingeloggte Kunde
     */
    public WarenkorbMenuPanel(CardLayout cardLayout, JPanel mainPanel, EShop shop, Kunde eingeloggterKunde) {
        this.shop = shop;
        this.eingeloggterKunde = eingeloggterKunde;
        setupUI();
        setupEvents(cardLayout, mainPanel);
    }

    /**
     * Initialisiert die Benutzeroberfläche.
     */
    private void setupUI() {
        setLayout(new BorderLayout());

        try {
            List<WarenkorbEintrag> artikelListe = shop.getWarenkorbVW().WarenkorbAnzeigen(eingeloggterKunde);
            artikelInWarenkorbTablePanel = new ArtikelInWarenkorbTablePanel(artikelListe);
            add(new JScrollPane(artikelInWarenkorbTablePanel), BorderLayout.CENTER);
        } catch (WarenkorbLeerException e) {
            JOptionPane.showMessageDialog(this, "Der Warenkorb ist leer.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        zurueckButton = new JButton("Zurück");
        rechnungButton = new JButton("Rechnung anzeigen");

        buttonPanel.add(zurueckButton);
        buttonPanel.add(rechnungButton);

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
    }
}