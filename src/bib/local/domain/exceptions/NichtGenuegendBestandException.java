package bib.local.domain.exceptions;

public class NichtGenuegendBestandException extends Exception{

    public NichtGenuegendBestandException(String zusatzMsg) {
        super("Nicht genügend Bestand verfügbar" +  zusatzMsg);
    }
}
