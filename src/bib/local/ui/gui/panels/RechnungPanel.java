package bib.local.ui.gui.panels;

import bib.local.domain.EShop;
import bib.local.entities.Kunde;
import bib.local.entities.WarenkorbEintrag;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Panel zum Anzeigen der Rechnung eines Kunden.
 */
public class RechnungPanel extends JPanel {
    private EShop shop;
    private Kunde eingeloggterKunde;
    private JButton zurueckButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    /**
     * Konstruktor für RechnungPanel.
     *
     * @param shop             die EShop-Instanz
     * @param eingeloggterKunde der eingeloggte Kunde
     * @param cardLayout       der CardLayout-Manager
     * @param mainPanel        das Hauptpanel, das alle Kartenpanels enthält
     */
    public RechnungPanel(EShop shop, Kunde eingeloggterKunde, CardLayout cardLayout, JPanel mainPanel) {
        this.shop = shop;
        this.eingeloggterKunde = eingeloggterKunde;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        setupUI();
        setupEvents();
    }

    /**
     * Initialisiert die Benutzeroberfläche.
     */
    private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Rechnung");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel);

        // Kunde-Daten
        JLabel nameLabel = new JLabel("Name: " + eingeloggterKunde.getName());
        add(nameLabel);

        JLabel adresseLabel = new JLabel("Adresse: " + eingeloggterKunde.getStrasse() + ", " + eingeloggterKunde.getPlz() + " " + eingeloggterKunde.getWohnort());
        add(adresseLabel);

        // Warenkorb-Einträge
        JLabel warenkorbLabel = new JLabel("Sie haben gekauft:");
        add(warenkorbLabel);

        List<WarenkorbEintrag> eintraege = eingeloggterKunde.getWarenkorb().getEintraege();
        for (WarenkorbEintrag w : eintraege) {
            JLabel artikelLabel = new JLabel(w.getArtikel().getBezeichnung() + " - Menge: " + w.getMenge() + " - Preis: " + w.getTotalPreis());
            add(artikelLabel);
        }

        double totalPrice = shop.getWarenkorbVW().getTotalPreis(eingeloggterKunde);
        JLabel totalLabel = new JLabel("Gesamtpreis: " + totalPrice);
        add(totalLabel);

       /* try {
            shop.schreibeArtikel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
        zurueckButton = new JButton("Schließen und zum EShop zurückgehen");
        add(zurueckButton);

        // Warenkorb abschließen
        shop.getWarenkorbVW().warenkorbAbschliessen(eingeloggterKunde);
    }

    /**
     * Setzt die Ereignishandler für die Komponenten.
     */
    private void setupEvents() {
        zurueckButton.addActionListener(e -> cardLayout.show(mainPanel, "kundeMenu"));
    }
}
