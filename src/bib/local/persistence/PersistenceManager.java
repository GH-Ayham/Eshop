package bib.local.persistence;

import java.io.IOException;

import bib.local.entities.Artikel;
import bib.local.entities.Kunde;
import bib.local.entities.Mitarbeiter;

/**
 * @author teschke
 *
 * Allgemeine Schnittstelle für den Zugriff auf ein Speichermedium
 * (z.B. Datei oder Datenbank) zum Ablegen von beispielsweise
 * Bücher- oder Kundendaten.
 * 
 * Das Interface muss von Klassen implementiert werden, die eine
 * Persistenz-Schnittstelle realisieren wollen.
 */
public interface PersistenceManager {

	public void openForReading(String datenquelle) throws IOException;
	
	public void openForWriting(String datenquelle) throws IOException;
	
	public boolean close();

	/**
	 * Methode zum Einlesen der Buchdaten aus einer externen Datenquelle.
	 * 
	 * @return Buch-Objekt, wenn Einlesen erfolgreich, false null
	 */
	public Artikel ladeArtikel() throws IOException;

	/**
	 * Methode zum Schreiben der Buchdaten in eine externe Datenquelle.
	 * 
	 * @param artikel Buch-Objekt, das gespeichert werden soll
	 * @return true, wenn Schreibvorgang erfolgreich, false sonst
	 */
	public boolean speichereArtikel(Artikel artikel) throws IOException;

	/*
	 *  Wenn später mal eine Kundenverwaltung ergänzt wird:
	 */
	public Kunde ladeKunde() throws IOException;

	public boolean speichereKunde(Kunde k) throws IOException;

	public Mitarbeiter ladeMitarbeiter() throws IOException;

	public boolean speichereMitarbeiter(Mitarbeiter mit) throws IOException;

}
