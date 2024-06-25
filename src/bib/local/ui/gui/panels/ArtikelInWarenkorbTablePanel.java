package bib.local.ui.gui.panels;

import bib.local.domain.EShop;
import bib.local.domain.exceptions.WarenkorbLeerException;
import bib.local.entities.Artikel;
import bib.local.entities.Kunde;
import bib.local.entities.WarenkorbEintrag;
import bib.local.ui.gui.models.ArtikelInWarenkorbTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class ArtikelInWarenkorbTablePanel extends JPanel {
    private EShop shop;
    private Kunde eingeloggterKunde;
    private ArtikelInWarenkorbTableModel tableModel;
    private JTable table;

    /**
     * Konstruktor für ArtikelInWarenkorbTablePanel mit der gegebenen Artikelliste.
     *
     * @param artikelListe die Liste der Artikel im Warenkorb
     */
    public ArtikelInWarenkorbTablePanel(List<WarenkorbEintrag> artikelListe, EShop shop, Kunde eingeloggterKunde) throws WarenkorbLeerException {
        this.shop = shop;
        this.eingeloggterKunde = eingeloggterKunde;
        setLayout(new BorderLayout());

        tableModel = new ArtikelInWarenkorbTableModel(artikelListe);
        table = new JTable(tableModel);

        // Spaltenbreiten und benutzerdefinierter Renderer für die Checkbox-Spalte
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected((Boolean) value);
                checkBox.setHorizontalAlignment(SwingConstants.CENTER);
                return checkBox;
            }
        });
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load initial data
        updateArtikelliste(shop.getWarenkorbVW().WarenkorbAnzeigen(eingeloggterKunde));
    }


    /**
     * Gibt das Tabellenmodell für die Artikel im Warenkorb zurück.
     *
     * @return das Tabellenmodell
     */
    public ArtikelInWarenkorbTableModel getModel() {
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

    public void updateArtikelliste(List<WarenkorbEintrag> artikelListe) {
        tableModel.setArtikels(artikelListe);
    }

}


