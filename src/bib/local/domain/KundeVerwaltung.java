package bib.local.domain;

import bib.local.domain.exceptions.NutzerExistiertBereitsException;
import bib.local.entities.Benutzer;
import bib.local.entities.Kunde;
import bib.local.entities.Mitarbeiter;
import bib.local.persistence.FilePersistenceManager;
import bib.local.persistence.PersistenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KundeVerwaltung {
    private List<Kunde> kundeList = new ArrayList<Kunde>();
    private List<Benutzer> benutzerList = new ArrayList<>();
    private PersistenceManager pm = new FilePersistenceManager();
    private Random rand = new Random();


    private int randomKundeID(){
        int nr;
        do {
            nr = rand.nextInt(100);
        }while (kundeList.contains(nr));
        return nr;
    }

    public void liesDaten(String datei) throws IOException{
        // PersistenzManager für Lesevorgänge öffnen
        pm.openForReading(datei);

        Kunde einKunde;
        do {
            // Kunde einlesen
            einKunde = pm.ladeKunde();
            if (einKunde != null) {

                einfuegen(einKunde);
            }
        } while (einKunde != null);
        // Persistenz-Schnittstelle wieder schließen
        pm.close();

    }

    public Kunde sucheKundeBeiBenutzerkennung(String benutzerkennung){
        for (Kunde kunde: kundeList){
            System.out.println("Überprüfe Benutzerkennung: " + kunde.getBenutzerkennung()); // Debugging-Ausgabe
            if (kunde.getBenutzerkennung().equals(benutzerkennung)){
                return kunde;
            }
        }
        return null;
    }

    public void einfuegen(Kunde einKunde) {kundeList.add(einKunde);}


    /**
     * Registriert einen neuen Kunden.
     *
     * @param nr              Die Kundennummer.
     * @param name            Der Name des Kunden.
     * @param benutzerkennung Der Benutzername für die Anmeldung.
     * @param passwort        Das Anmeldepasswort.
     * @param umsatz          Der Gesamtumsatz des Kunden.
     * @param strasse         Die Straßenadresse des Kunden.
     * @param plz             Die Postleitzahl des Kunden.
     * @param wohnort         Der Wohnort des Kunden.
     * @return
     */

    public Kunde registriereKunde(String name, String benutzerkennung, String passwort, String strasse, String plz, String wohnort) throws NutzerExistiertBereitsException {

        if (sucheKundeBeiBenutzerkennung(benutzerkennung) !=null){
            throw new NutzerExistiertBereitsException("Benutzerkennung " + benutzerkennung + " existiert bereits.");
        }

        int nr = randomKundeID();

        Kunde kunde = new Kunde(nr, name, benutzerkennung, passwort, strasse, plz, wohnort);
        kundeList.add(kunde);
        return kunde;
    }

    public void schreibeDaten(String datei) throws IOException {
        pm.openForWriting(datei);

        for (Object kunde: kundeList){
            pm.speichereKunde((Kunde) kunde);
        }
        pm.close();
    }


    public ArrayList<Kunde> getKundeList(){return new ArrayList<>(kundeList);}

    public Kunde findeKunde(String benutzername, String passwort) {
        for (Kunde kunde : kundeList) {
            if (kunde.getBenutzerkennung().equals(benutzername) && kunde.getPassword().equals(passwort)) {
                return kunde;
            }
        }
        return null;
    }

}
