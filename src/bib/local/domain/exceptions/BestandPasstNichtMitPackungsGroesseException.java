package bib.local.domain.exceptions;

public class BestandPasstNichtMitPackungsGroesseException extends Exception{

    public BestandPasstNichtMitPackungsGroesseException(String zusatzMsg) {
        super("Bestand passt nicht mit Packungsgrösse: " +  zusatzMsg);
    }
}
