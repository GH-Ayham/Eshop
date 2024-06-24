package bib.local.ui.gui.panels;

import bib.local.domain.EShop;
import bib.local.entities.Artikel;
import bib.local.entities.Massengutartikel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Panel zum Bearbeiten eines Artikels.
 */
public class BearbeiteArtikelPanel extends JPanel {
    private JTextField bezeichnungField;
    private JTextField bestandField;
    private JTextField preisField;
    private JTextField packungsgroesseField;
    private JButton saveButton;
    private EShop shop;
    private Artikel artikel;

    /**
     * Konstruktor für BearbeiteArtikelPanel.
     *
     * @param shop    Die EShop-Instanz.
     * @param artikel Der zu bearbeitende Artikel.
     */
    public BearbeiteArtikelPanel(EShop shop, Artikel artikel) {
        this.shop = shop;
        this.artikel = artikel;
        setupUI();
        setupEvents();
    }

    /**
     * Initialisiert die UI-Komponenten für das Panel.
     */
    private void setupUI() {
        setLayout(new GridLayout(0, 2));

        bezeichnungField = new JTextField(artikel.getBezeichnung());
        bestandField = new JTextField(String.valueOf(artikel.getBestand()));
        preisField = new JTextField(String.valueOf(artikel.getPreis()));
        packungsgroesseField = new JTextField();

        add(new JLabel("Bezeichnung:"));
        add(bezeichnungField);
        add(new JLabel("Bestand:"));
        add(bestandField);
        add(new JLabel("Preis:"));
        add(preisField);
        if (artikel instanceof Massengutartikel) {
            packungsgroesseField.setText(String.valueOf(((Massengutartikel) artikel).getPackungsGroesse()));
            add(new JLabel("Packungsgröße:"));
            add(packungsgroesseField);
        }

        saveButton = new JButton("Speichern");
        add(saveButton);
    }

    /**
     * Initialisiert die Ereignis-Listener für das Panel.
     */
    private void setupEvents() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                onSaveButtonClick();
            }
        });
    }

    /**
     * Aktion, die ausgeführt wird, wenn der Speichern-Button geklickt wird.
     */
    private void onSaveButtonClick() {
        try {
            artikel.setBezeichnung(bezeichnungField.getText());
            artikel.setBestand(Integer.parseInt(bestandField.getText()));
            artikel.setPreis(Double.parseDouble(preisField.getText()));
            if (artikel instanceof Massengutartikel) {
                ((Massengutartikel) artikel).setPackungsGroesse(Integer.parseInt(packungsgroesseField.getText()));
            }
            shop.schreibeArtikel();
            JOptionPane.showMessageDialog(this, "Artikel wurde erfolgreich aktualisiert.");
        } catch (NumberFormatException | IOException e) {
            JOptionPane.showMessageDialog(this, "Ungültige Eingabe. Bitte stellen Sie sicher, dass die Felder korrekt ausgefüllt sind.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Zeigt den Dialog zum Bearbeiten des Artikels an.
     *
     * @return True, wenn der Artikel erfolgreich bearbeitet wurde, false sonst.
     */
    public boolean showDialog() {
        int result = JOptionPane.showConfirmDialog(this, this, "Artikel bearbeiten", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        return result == JOptionPane.OK_OPTION;
    }
}
