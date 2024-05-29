package bib.local.entities;

import bib.local.domain.exceptions.BestandPasstNichtMitPackungsGroesseException;
import bib.local.domain.exceptions.NichtGenuegendBestandException;

public class Massengutartikel extends Artikel{

    private int packungsGroesse;

    /**
     * public Artikel(int nr, String bezeichnung, int bestand, double preis) {
     * this(nr, bezeichnung, bestand, preis);
     * }
     *
     * @param nr
     * @param bezeichnung
     * @param bestand
     * @param preis
     */
    public Massengutartikel(int nr, String bezeichnung, int bestand, double preis,  int packungsGroesse) {
        super(nr, bezeichnung, bestand, preis, true);
        this.packungsGroesse = packungsGroesse;
    }

    public int getPackungsGroesse() {return packungsGroesse;}

    public void setPackungsGroesse(int packungsGroesse) {this.packungsGroesse = packungsGroesse;}

    @Override
    public void artikelBestandAktualisieren(int menge) throws BestandPasstNichtMitPackungsGroesseException, NichtGenuegendBestandException {
        if (menge % packungsGroesse != 0) {
            throw new BestandPasstNichtMitPackungsGroesseException(" In artikelBestandAktualisieren()");
        }
        super.artikelBestandAktualisieren(menge);
    }

    @Override
    public String toString() {
        return ("Nr: " + getNummer() + " / Bezeichnung: " + getBezeichnung() + " / Bestand:  " + getBestand() + " / Preis: " + getPreis() + " / PackungsGroesse: " + packungsGroesse );
    }
}
