package bib.local.ui.gui.panels;

import bib.local.domain.EShop;
import bib.local.entities.Artikel;
import bib.local.ui.gui.models.ArtikelTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArtikelTablePanel extends JPanel {
    private EShop shop;
    private ArtikelTableModel tableModel;
    private JTable table;

    public ArtikelTablePanel(EShop shop) {
        this.shop = shop;
        setLayout(new BorderLayout());

        // Create the table model and table
        tableModel = new ArtikelTableModel(new ArrayList<>());
        table = new JTable(tableModel);

        // Set column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50); // Checkbox column
        columnModel.getColumn(1).setPreferredWidth(100); // Number column
        columnModel.getColumn(2).setPreferredWidth(200); // Description column

        // Set custom renderer and editor for the checkbox column
        columnModel.getColumn(0).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected((Boolean) value);
                checkBox.setHorizontalAlignment(SwingConstants.CENTER);
                return checkBox;
            }
        });

        columnModel.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add sort buttons
        JPanel sortPanel = new JPanel(new FlowLayout());
        JButton sortNumberButton = new JButton("Sortieren nach Nummer");
        JButton sortDescriptionButton = new JButton("Sortieren nach Bezeichnung");
        sortPanel.add(sortNumberButton);
        sortPanel.add(sortDescriptionButton);

        add(sortPanel, BorderLayout.NORTH);

        // Add action listeners
        sortNumberButton.addActionListener(e -> sortArtikelsByNumber());
        sortDescriptionButton.addActionListener(e -> sortArtikelsByDescription());

        // Load initial data
        updateArtikelliste(shop.gibAlleArtikel());
    }

    public void updateArtikelliste(List<Artikel> artikelListe) {
        tableModel.setArtikels(artikelListe);
    }

    private void sortArtikelsByNumber() {
        List<Artikel> artikelListe = shop.gibAlleArtikel();
        Collections.sort(artikelListe, (a1, a2) -> Integer.compare(a1.getNummer(), a2.getNummer()));
        updateArtikelliste(artikelListe);
    }

    private void sortArtikelsByDescription() {
        List<Artikel> artikelListe = shop.gibAlleArtikel();
        Collections.sort(artikelListe, (a1, a2) -> a1.getBezeichnung().compareTo(a2.getBezeichnung()));
        updateArtikelliste(artikelListe);
    }

    public ArtikelTableModel getModel() {
        return tableModel;
    }

    /**
     * Gibt die ausgewählte Zeile in der Tabelle zurück.
     *
     * @return die ausgewählte Zeile.
     */
    public int getSelectedRow() {
        return table.getSelectedRow();
    }

}
