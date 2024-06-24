package bib.local.ui.gui.panels;

import bib.local.domain.EShop;
import bib.local.domain.exceptions.ArtikelNichtGefundenException;
import bib.local.domain.exceptions.BestandPasstNichtMitPackungsGroesseException;
import bib.local.domain.exceptions.NichtGenuegendBestandException;
import bib.local.entities.Artikel;
import bib.local.entities.Kunde;
import bib.local.entities.Massengutartikel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class KundeMenuPanel extends JPanel implements AddArticlePanel.AddArticleListener {

    private EShop shop;
    private ArtikelTablePanel artikelTablePanel;
    private JButton addToCartButton;
    private Kunde eingeloggterKunde;
    private JButton warenkorbButton;


    /**
     * Konstruktor für KundeMenuPanel.
     *
     * @param cardLayout       der CardLayout-Manager
     * @param mainPanel        das Hauptpanel, das alle Kartenpanels enthält
     * @param shop             die EShop-Instanz
     * @param eingeloggterKunde der eingeloggte Kunde
     */
    public KundeMenuPanel(CardLayout cardLayout, JPanel mainPanel, EShop shop, Kunde eingeloggterKunde) {
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

        // Oberer Bereich für Begrüßung
        JPanel topPanel = new JPanel(new BorderLayout());
        String kundeName = (shop.getBenutzer() != null) ? shop.getBenutzer().getName() : "Unbekannter Benutzer";
        JLabel welcomeLabel = new JLabel("Willkommen Kunde: " + kundeName);
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        artikelTablePanel = new ArtikelTablePanel(shop);
        add(new JScrollPane(artikelTablePanel), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        addToCartButton = new JButton("Zum Warenkorb einfügen");
        warenkorbButton = new JButton("Zum Warenkorb");

        buttonPanel.add(addToCartButton);
        buttonPanel.add(warenkorbButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Legt die Ereignishandler für die Komponenten fest.
     *
     * @param cardLayout der CardLayout-Manager
     * @param mainPanel  das Hauptpanel, das alle Kartenpanels enthält
     */
    private void setupEvents(CardLayout cardLayout, JPanel mainPanel) {
        addToCartButton.addActionListener(e -> {
            try {
                fuegeArtikelZuWarenkorbHinzu();
            } catch (IOException | ArtikelNichtGefundenException ex) {
                JOptionPane.showMessageDialog(KundeMenuPanel.this, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        warenkorbButton.addActionListener(e -> {
            JPanel warenkorbMenuPanel = new WarenkorbMenuPanel(cardLayout, mainPanel, shop, eingeloggterKunde);
            mainPanel.add(warenkorbMenuPanel, "warenkorbMenu");
            cardLayout.show(mainPanel, "warenkorbMenu");
        });
    }

    /**
     * Fügt den ausgewählten Artikel zum Warenkorb hinzu.
     *
     * @throws IOException
     * @throws ArtikelNichtGefundenException
     */
    private void fuegeArtikelZuWarenkorbHinzu() throws IOException, ArtikelNichtGefundenException {
        int selectedRow = artikelTablePanel.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie einen Artikel aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Artikel artikel = artikelTablePanel.getModel().getArtikelAt(selectedRow);
        try {
            if (artikel instanceof Massengutartikel) {
                int packungsGrosse = ((Massengutartikel) artikel).getPackungsGroesse();
                String mengeStr = JOptionPane.showInputDialog(this, "Menge (Vielfaches von " + packungsGrosse + "):");
                int menge = Integer.parseInt(mengeStr);
                shop.fuegeArtikelZuWarenkorbHinzu(eingeloggterKunde, artikel, menge);
            } else {
                String mengeStr = JOptionPane.showInputDialog(this, "Menge:");
                int menge = Integer.parseInt(mengeStr);
                shop.fuegeArtikelZuWarenkorbHinzu(eingeloggterKunde, artikel, menge);
            }
            JOptionPane.showMessageDialog(this, "Artikel wurde zum Warenkorb hinzugefügt.");
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Ungültige Menge.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (BestandPasstNichtMitPackungsGroesseException | NichtGenuegendBestandException e) {
            JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onArticleAdded(Artikel artikel) {

    }
}
