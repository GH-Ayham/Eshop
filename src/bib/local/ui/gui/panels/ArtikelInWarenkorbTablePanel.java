package bib.local.ui.gui.panels;

import bib.local.entities.WarenkorbEintrag;
import bib.local.ui.gui.models.ArtikelInWarenkorbTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class ArtikelInWarenkorbTablePanel extends JPanel {
    private ArtikelInWarenkorbTableModel tableModel;
    private JTable table;

    /**
     * Konstruktor für ArtikelInWarenkorbTablePanel mit der gegebenen Artikelliste.
     *
     * @param artikelListe die Liste der Artikel im Warenkorb
     */
    public ArtikelInWarenkorbTablePanel(List<WarenkorbEintrag> artikelListe) {
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
    }

    /**
     * Gibt das Tabellenmodell für die Artikel im Warenkorb zurück.
     *
     * @return das Tabellenmodell
     */
    public ArtikelInWarenkorbTableModel getModel() {
        return tableModel;
    }
}


