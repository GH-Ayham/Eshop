package bib.local.ui.gui.models;

import bib.local.entities.Artikel;
import bib.local.entities.Kunde;
import bib.local.entities.WarenkorbEintrag;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ArtikelInWarenkorbTableModel extends AbstractTableModel {
    private List<WarenkorbEintrag> artikelListe;
    private List<Boolean> selectedList;
    private String[] columnNames = {"Auswählen", "Nummer", "Bezeichnung", "Menge", "Preis"};


    /**
     * Konstruktor für ArtikelInWarenkorbTableModel mit der gegebenen Artikelliste.
     *
     * @param artikelListe die Liste der Artikel im Warenkorb
     */
    public ArtikelInWarenkorbTableModel(List<WarenkorbEintrag> artikelListe) {
        this.artikelListe = artikelListe;
        this.selectedList = new ArrayList<>(artikelListe.size());
        for (int i = 0; i < artikelListe.size(); i++) {
            selectedList.add(false); // Initiale Checkbox-Werte
        }
    }

    @Override
    public int getRowCount() {
        return artikelListe.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        WarenkorbEintrag eintrag = artikelListe.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return selectedList.get(rowIndex);
            case 1:
                return eintrag.getArtikel().getNummer();
            case 2:
                return eintrag.getArtikel().getBezeichnung();
            case 3:
                return eintrag.getMenge();
            case 4:
                return eintrag.getArtikel().getPreis();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0; //Nur die Checkbox ist editierbar
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            selectedList.set(rowIndex, (Boolean) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * Gibt den Artikel an der angegebenen Zeilenindex zurück.
     *
     * @param rowIndex der Zeilenindex
     * @return der Artikel an der angegebenen Zeilenindex
     */
    public WarenkorbEintrag getArtikelAt(int rowIndex) {
        return artikelListe.get(rowIndex);
    }

    /**
     * Entfernt den Artikel an der angegebenen Zeilenindex.
     *
     * @param rowIndex der Zeilenindex
     */
    public void removeArtikel(int rowIndex) {
        artikelListe.remove(rowIndex);
        selectedList.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void setArtikels(List<WarenkorbEintrag> artikelListe) {
        this.artikelListe = artikelListe;
        this.selectedList = new ArrayList<>(artikelListe.size());
        for (int i = 0; i < artikelListe.size(); i++) {
            selectedList.add(Boolean.FALSE); // Reset checkbox values
        }
        fireTableDataChanged();
    }

    /**
     * Gibt die Liste der ausgewählten Checkboxen zurück.
     *
     * @return die Liste der ausgewählten Checkboxen
     */
    public List<Boolean> getSelectedList() {
        return selectedList;
    }


}
