package bib.local.domain.exceptions;

public class NutzerExistiertBereitsException extends Exception{

    public NutzerExistiertBereitsException(String zusatzMsg){
        super(zusatzMsg);
    }
}
