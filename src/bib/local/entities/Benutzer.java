package bib.local.entities;

public class Benutzer {

    private String name;
    private String benutzerkennung;
    private String password;
    boolean istMitarbeiter;
    boolean istKunde;

    // Constructor for Benutzer
    public Benutzer(String name, String benutzerkennung, String password, boolean istMitarbeiter, boolean istKunde) {
        this.name = name;
        this.benutzerkennung = benutzerkennung;
        this.password = password;
        this.istMitarbeiter = istMitarbeiter;
        this.istKunde = istKunde;
    }

    public String getName(){ return name;}

    public String getPassword(){ return password;}

    public String getBenutzerkennung() { return benutzerkennung;}

    public boolean getistMitarbeiter() { return istMitarbeiter;}

    public boolean getistKunde() { return istKunde;}
}
