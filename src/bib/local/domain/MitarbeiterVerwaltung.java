package bib.local.domain;

import bib.local.domain.exceptions.NutzerExistiertBereitsException;
import bib.local.entities.Benutzer;
import bib.local.entities.Mitarbeiter;
import bib.local.persistence.FilePersistenceManager;
import bib.local.persistence.PersistenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MitarbeiterVerwaltung {

    // Liste, um Benutzerobjekte zu speichern
    private List<Benutzer> benutzerList = new ArrayList<>();

    // Liste, um Mitarbeiterobjekte zu speichern
    private List<Mitarbeiter> mitarbeiterList = new ArrayList<>();



    private PersistenceManager pm = new FilePersistenceManager();
    private Random rand = new Random();

    private int randomMitarbeiterID(){
        int nr;
        do {
            nr = rand.nextInt(100);
        }while (mitarbeiterList.contains(mitarbeiterList.get(nr)));
        return nr;
    }

    public Mitarbeiter sucheMitarbeiterBeiBenutzerkennung(String benutzerkennung){
        for (Mitarbeiter mitarbeiter : mitarbeiterList){
        if (mitarbeiter.getBenutzerkennung().equalsIgnoreCase(benutzerkennung)){
                return mitarbeiter;
            }
        }
        return null;
    }

    public void liesDaten(String datei) throws IOException {
        // PersistenzManager für Lesevorgänge öffnen
        pm.openForReading(datei);

        Mitarbeiter einMitarbeiter;
        do {
            // Kunde einlesen
            einMitarbeiter = pm.ladeMitarbeiter();
            if (einMitarbeiter != null) {

                einfuegen(einMitarbeiter);
            }
        } while (einMitarbeiter != null);
        // Persistenz-Schnittstelle wieder schließen
        pm.close();

    }

    public void einfuegen(Mitarbeiter einMitarbeiter) {
        mitarbeiterList.add(einMitarbeiter);
    }

    public void schreibeDaten(String datei) throws IOException {
        // PersistenzManager für Schreibvorgänge öffnen
        pm.openForWriting(datei);
        {
            for (Object mitarbeiter : mitarbeiterList) {
                pm.speichereMitarbeiter((Mitarbeiter) mitarbeiter);
            }
        }

        // Persistenz-Schnittstelle wieder schließen
        pm.close();
    }



    // Methode, um einen Mitarbeiter zu registrieren
    public Mitarbeiter registriereMitarbeiter(int nr, String name, String benutzerkennung, String passwort) throws NutzerExistiertBereitsException {
        if (sucheMitarbeiterBeiBenutzerkennung(benutzerkennung) !=null){
            throw new NutzerExistiertBereitsException("Benutzerkennung " + benutzerkennung + " existiert bereits.");
        }

        // Erstellt ein neues Mitarbeiterobjekt
        Mitarbeiter mitarbeiter = new Mitarbeiter(nr, name, benutzerkennung, passwort);

        // Fügt das Mitarbeiterobjekt zur Liste der Mitarbeiter hinzu
        mitarbeiterList.add(mitarbeiter);

        return mitarbeiter;
    }

}

