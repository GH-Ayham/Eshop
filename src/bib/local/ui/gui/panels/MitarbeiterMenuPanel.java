package bib.local.ui.gui.panels;

import bib.local.domain.EShop;
import bib.local.entities.Artikel;
import bib.local.entities.Benutzer;
import bib.local.entities.Mitarbeiter;
import bib.local.ui.gui.panels.AddArticlePanel;
import bib.local.ui.gui.panels.ArtikelTablePanel;
import bib.local.ui.gui.panels.BearbeiteArtikelPanel;
import bib.local.ui.gui.panels.RegisterMitarbeiterPanel;
import bib.local.ui.gui.models.ArtikelTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Panel zum Verwalten von Artikeln und Mitarbeitern im Shop.
 */
public class MitarbeiterMenuPanel extends JPanel implements AddArticlePanel.AddArticleListener {
    private EShop shop;
    private ArtikelTablePanel artikelTablePanel;
    private ArtikelTableModel tableModel;
    private Mitarbeiter eingeloggterMitarbeiter;

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
        setLayout(new BorderLayout());

        // Oberer Bereich für Begrüßung und Buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        String mitarbeiterName = (shop.getBenutzer() != null) ? shop.getBenutzer().getName() : "Unbekannter Benutzer";
        JLabel welcomeLabel = new JLabel("Willkommen Mitarbeiter/in: " + mitarbeiterName);
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        JButton editArticleButton = new JButton("Artikel bearbeiten");
        editArticleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    bearbeiteArtikel();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(editArticleButton);

        JButton deleteArticleButton = new JButton("Artikel löschen");
        deleteArticleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedArtikels();
            }
        });
        buttonPanel.add(deleteArticleButton);

        JButton logoutButton = new JButton("Ausloggen");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shop.setBenutzer(null);
                JOptionPane.showMessageDialog(null, "Sie sind ausgeloggt.");
                cardLayout.show(mainPanel, "login");
            }
        });
        buttonPanel.add(logoutButton);

        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Linkes Panel für RegisterMitarbeiterPanel
        RegisterMitarbeiterPanel registerPanel = new RegisterMitarbeiterPanel(shop, new RegisterMitarbeiterPanel.RegisterMitarbeiterListener() {
            @Override
            public void onEmployeeRegistered(Mitarbeiter mitarbeiter) {
                // Aktionen nach der Registrierung des Mitarbeiters
            }
        });
        JPanel leftPanel = new JPanel(new BorderLayout());
        //leftPanel.add(new JLabel("Neue Mitarbeiter registrieren", SwingConstants.CENTER), BorderLayout.NORTH);
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


    @Override
    public void onArticleAdded(Artikel artikel) {
        artikelTablePanel.updateArtikelliste(shop.gibAlleArtikel());
    }

    private void deleteSelectedArtikels() {
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected) {
                Artikel artikel = tableModel.getArtikelAt(i);
                try {
                    shop.loescheArtikel(artikel.getNummer());
                    tableModel.removeArtikel(i);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Fehler beim Löschen des Artikels: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Bearbeitet den ausgewählten Artikel.
     *
     * @throws IOException Falls ein Fehler beim Schreiben der Artikeldaten auftritt.
     */
    private void bearbeiteArtikel() throws IOException {
        int selectedRow = -1;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((Boolean) tableModel.getValueAt(i, 0)) {
                selectedRow = i;
                break;
            }
        }

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie einen Artikel zum Bearbeiten aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Artikel artikel = tableModel.getArtikelAt(selectedRow);
        BearbeiteArtikelPanel bearbeitePanel = new BearbeiteArtikelPanel(shop, artikel);
        boolean updated = bearbeitePanel.showDialog();
        if (updated) {
            tableModel.fireTableRowsUpdated(selectedRow, selectedRow);
        }
    }

    private String getMitarbeiterName() {
        // Ersetzen Sie dies durch die tatsächliche Logik zum Abrufen des Mitarbeiternamens
        return "MitarbeiterName";
    }
}
