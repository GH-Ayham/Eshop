package bib.local.ui.gui.panels;

import bib.local.domain.EShop;
import bib.local.domain.exceptions.ArtikelExistiertBereitsException;
import bib.local.entities.Artikel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AddArticlePanel extends JPanel {

    // Interface to notify when a new article is added
    public interface AddArticleListener {
        void onArticleAdded(Artikel artikel);
    }

    private EShop shop = null;
    private AddArticleListener addListener = null;

    private JButton addButton;
    private JTextField numberTextField = null;
    private JTextField titleTextField = null;
    private JTextField stockTextField = null;
    private JTextField priceTextField = null;
    private JCheckBox bulkCheckBox = null;
    private JTextField packSizeTextField = null;

    public AddArticlePanel(EShop shop, AddArticleListener listener) {
        this.shop = shop;
        this.addListener = listener;

        setupUI();
        setupEvents();
    }

    private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        Dimension borderMinSize = new Dimension(5, 10);
        Dimension borderPrefSize = new Dimension(5, 10);
        Dimension borderMaxSize = new Dimension(5, 10);
        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));

        add(new JLabel("Artikelnummer: "));
        numberTextField = new JTextField();
        add(numberTextField);

        add(new JLabel("Artikelbezeichnung: "));
        titleTextField = new JTextField();
        add(titleTextField);

        add(new JLabel("Artikelbestand: "));
        stockTextField = new JTextField();
        add(stockTextField);

        add(new JLabel("Artikelpreis: "));
        priceTextField = new JTextField();
        add(priceTextField);

        bulkCheckBox = new JCheckBox("Ist es ein Massengutartikel?");
        add(bulkCheckBox);

        add(new JLabel("Packungsgröße: "));
        packSizeTextField = new JTextField();
        packSizeTextField.setEnabled(false);
        add(packSizeTextField);

        bulkCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                packSizeTextField.setEnabled(bulkCheckBox.isSelected());
            }
        });

        Dimension minSize = new Dimension(1, 20);
        Dimension prefSize = new Dimension(1, Short.MAX_VALUE);
        Dimension maxSize = new Dimension(1, Short.MAX_VALUE);
        Box.Filler filler = new Box.Filler(minSize, prefSize, maxSize);
        add(filler);

        addButton = new JButton("Hinzufügen");
        add(addButton);

        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));

        setBorder(BorderFactory.createTitledBorder("Artikel Einfügen"));
    }

    private void setupEvents() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("Event: " + ae.getActionCommand());
                onAddButtonClick();
            }
        });
    }

    private void onAddButtonClick() {
        String nummer = numberTextField.getText();
        String bezeichnung = titleTextField.getText();
        String bestand = stockTextField.getText();
        String preis = priceTextField.getText();
        boolean istPackung = bulkCheckBox.isSelected();
        String packungsgroesse = packSizeTextField.getText();

        if (!nummer.isEmpty() && !bezeichnung.isEmpty() && !bestand.isEmpty() && !preis.isEmpty()) {
            try {
                int nummerAlsInt = Integer.parseInt(nummer);
                int bestandAlsInt = Integer.parseInt(bestand);
                double preisAlsDouble = Double.parseDouble(preis);
                int packungsgroesseAlsInt = istPackung ? Integer.parseInt(packungsgroesse) : 0;

                Artikel artikel = shop.fuegeArtikelEin(nummerAlsInt, bezeichnung, bestandAlsInt, preisAlsDouble, istPackung, packungsgroesseAlsInt);
                shop.schreibeArtikel();

                numberTextField.setText("");
                titleTextField.setText("");
                stockTextField.setText("");
                priceTextField.setText("");
                bulkCheckBox.setSelected(false);
                packSizeTextField.setText("");
                packSizeTextField.setEnabled(false);

                // Am Ende Listener, d.h. unseren Frame benachrichtigen:
                addListener.onArticleAdded(artikel);
                JOptionPane.showMessageDialog(this, "Artikel wurde erfolgreich hinzugefügt.");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Bitte eine gültige Zahl eingeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
            } catch (ArtikelExistiertBereitsException | IOException e) {
                JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
