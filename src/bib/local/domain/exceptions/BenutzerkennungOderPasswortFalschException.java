package bib.local.domain.exceptions;

public class BenutzerkennungOderPasswortFalschException extends Exception{

    public BenutzerkennungOderPasswortFalschException(String zusatzMsg) {

        super("BenutzerKennung oder Passwort Falsch eingegeben." + zusatzMsg);
    }
}
