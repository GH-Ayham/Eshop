package bib.local.domain.exceptions;

public class FalscheEingabeTypException extends Exception {
    public FalscheEingabeTypException(String zusatzMsg) {
        super(zusatzMsg);
    }
}
