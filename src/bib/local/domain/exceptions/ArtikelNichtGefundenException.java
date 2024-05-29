package bib.local.domain.exceptions;

import bib.local.entities.Artikel;

public class ArtikelNichtGefundenException extends Exception {
    private int artikelnummer;

    public ArtikelNichtGefundenException(int artikelnummer, String zusatzMsg) {
        super("Artikel mit Nr. " + artikelnummer + " nicht gefunden" + zusatzMsg);
        this.artikelnummer = artikelnummer;
    }

    public int getArtikelnummer() {return artikelnummer;}
}
