package bib.local.ui.gui.models;

import bib.local.entities.Artikel;
import bib.local.entities.Massengutartikel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ArtikelTableModel extends AbstractTableModel {
    private List<Artikel> artikelListe;
    private List<Boolean> selectedList;
    private String[] columnNames = {"Auswählen", "Nummer", "Bezeichnung", "Bestand", "Preis", "Massengutartikel", "Packungsgröße"};

    public ArtikelTableModel(List<Artikel> artikelListe) {
        this.artikelListe = artikelListe;
        this.selectedList = new ArrayList<>(artikelListe.size());
        for (int i = 0; i < artikelListe.size(); i++) {
            selectedList.add(Boolean.FALSE); // Initial checkbox values
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
        Artikel artikel = artikelListe.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return selectedList.get(rowIndex);
            case 1:
                return artikel.getNummer();
            case 2:
                return artikel.getBezeichnung();
            case 3:
                return artikel.getBestand();
            case 4:
                return artikel.getPreis();
            case 5:
                return artikel.getIstPackung();
            case 6:
                if (artikel instanceof Massengutartikel) {
                    return ((Massengutartikel) artikel).getPackungsGroesse();
                } else {
                    return null;
                }
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
        return columnIndex == 0; // Only checkbox is editable
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            selectedList.set(rowIndex, (Boolean) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void setArtikels(List<Artikel> artikelListe) {
        this.artikelListe = artikelListe;
        this.selectedList = new ArrayList<>(artikelListe.size());
        for (int i = 0; i < artikelListe.size(); i++) {
            selectedList.add(Boolean.FALSE); // Reset checkbox values
        }
        fireTableDataChanged();
    }

    public Artikel getArtikelAt(int rowIndex) {
        return artikelListe.get(rowIndex);
    }

    public void removeArtikel(int rowIndex) {
        artikelListe.remove(rowIndex);
        selectedList.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public List<Boolean> getSelectedList() {
        return selectedList;
    }
}
