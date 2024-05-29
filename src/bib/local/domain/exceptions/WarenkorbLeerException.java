package bib.local.domain.exceptions;

public class WarenkorbLeerException extends Exception{

    public WarenkorbLeerException(String zusatzMsg){
        super("Der Warenkorb ist bereits leer." + zusatzMsg);
    }
}
