package bib.local.entities;

public class Mitarbeiter extends Benutzer {
    private int mitarbeiterNr;


    public Mitarbeiter(int nr, String name, String benutzerkennung, String password) {
        super(name, benutzerkennung, password, true, false);
        this.mitarbeiterNr = nr;
    }

    public int getMitarbeiterNr() {return mitarbeiterNr;}

}
