package bib.local.domain.exceptions;

public class ArtikelNichtImWarenkorbGefunden  extends Exception{

    public ArtikelNichtImWarenkorbGefunden(String zusatzMsg) {
        super("Artikel im Warenkorb nicht Gefunden. --->" + zusatzMsg);
    }
}
