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

        zurueckButton = new JButton("Zurück");
        add(zurueckButton, BorderLayout.SOUTH);
    }

    /**
     * Setzt die Ereignishandler für die Komponenten.
     *
     * @param cardLayout der CardLayout-Manager
     * @param mainPanel  das Hauptpanel, das alle Kartenpanels enthält
     */
    private void setupEvents(CardLayout cardLayout, JPanel mainPanel) {
        zurueckButton.addActionListener(e -> cardLayout.show(mainPanel, "kundeMenu"));
    }
}