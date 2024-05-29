package bib.local.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import bib.local.entities.Artikel;
import bib.local.entities.Kunde;
import bib.local.entities.Massengutartikel;
import bib.local.entities.Mitarbeiter;

/**
 * @author teschke
 *
 * Realisierung einer Schnittstelle zur persistenten Speicherung von
 * Daten in Dateien.
 * @see bib.local.persistence.PersistenceManager
 */
public class FilePersistenceManager implements PersistenceManager {

	private BufferedReader reader = null;
	private PrintWriter writer = null;
	
	public void openForReading(String datei) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(datei));
	}

	public void openForWriting(String datei) throws IOException {
		writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));
	}

	public boolean close() {
		if (writer != null)
			writer.close();
		
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return false;
			}
		}

		return true;
	}

	/**
	 * Methode zum Einlesen der Buchdaten aus einer externen Datenquelle.
	 * Das Verfügbarkeitsattribut ist in der Datenquelle (Datei) als "t" oder "f"
	 * codiert abgelegt.
	 * 
	 * @return Buch-Objekt, wenn Einlesen erfolgreich, false null

	public Artikel ladeArtikel() throws IOException {
		// Titel einlesen
		String bezeichnung = liesZeile();
		if (bezeichnung == null) {
			// keine Daten mehr vorhanden
			return null;
		}
		// Nummer einlesen ...
		String nummerString = liesZeile();
		// ... und von String in int konvertieren
		int nummer = Integer.parseInt(nummerString);

		// bestand einlesen ...
		String bestandString = liesZeile();
		// ... und von String in int konvertieren
		int bestand = Integer.parseInt(bestandString);

		// preis einlesen
		String preisString = liesZeile();
		double preis = Double.parseDouble(preisString);
		
		// neues Buch-Objekt anlegen und zurückgeben
		return new Artikel(nummer, bezeichnung, bestand, preis);
	}
	*/
	public Artikel ladeArtikel() throws IOException {
		Artikel artikel;

		// Nummer einlesen ...
		String nummerString = liesZeile();
		if (nummerString == null) {
			// keine Daten mehr vorhanden
			return null;
		}
		int nummer;
		try {
			// ... und von String in int konvertieren
			nummer = Integer.parseInt(nummerString);
		} catch (NumberFormatException e) {
			System.err.println("Fehler beim Parsen der Artikelnummer: " + nummerString);
			return null; // Return null or throw an exception, depending on your error handling strategy
		}
		String bezeichnung = liesZeile();
		if (bezeichnung == null) {
			// keine Daten mehr vorhanden
			return null;
		}

		// bestand einlesen ...
		String bestandString = liesZeile();
		int bestand;
		try {
			// ... und von String in int konvertieren
			bestand = Integer.parseInt(bestandString);
		} catch (NumberFormatException e) {
			System.err.println("Fehler beim Parsen des Bestands: " + bestandString);
			return null; // Return null or throw an exception
		}

		// preis einlesen
		String preisString = liesZeile();
		double preis;
		try {
			// ... und von String in double konvertieren
			preis = Double.parseDouble(preisString);
		} catch (NumberFormatException e) {
			System.err.println("Fehler beim Parsen des Preises: " + preisString);
			return null; // Return null or throw an exception
		}

		String istPackungCode = liesZeile();
		boolean istPackung = istPackungCode.equals("t") ? true : false;
		if (istPackung) {
			int packungsGroesse = Integer.parseInt(liesZeile());
			artikel = new Massengutartikel(nummer, bezeichnung, bestand, preis, packungsGroesse);
		} else {
			artikel = new Artikel(nummer, bezeichnung, bestand, preis, istPackung);
		}
		return artikel;

		// neues Artikel-Objekt anlegen und zurückgeben
		//return new Artikel(nummer, bezeichnung, bestand, preis);
	}


	/**
	 * Methode zum Schreiben der Buchdaten in eine externe Datenquelle.
	 * Das Verfügbarkeitsattribut wird in der Datenquelle (Datei) als "t" oder "f"
	 * codiert abgelegt.
	 * 
	 * @param artikel Buch-Objekt, das gespeichert werden soll
	 * @return true, wenn Schreibvorgang erfolgreich, false sonst
	 */
	public boolean speichereArtikel(Artikel artikel) throws IOException {
		// Titel, Nummer, Bestand und Preis schreiben
		schreibeZeile(artikel.getNummer() + "");
		schreibeZeile(artikel.getBezeichnung() + "");
		schreibeZeile(artikel.getBestand() + "");
		schreibeZeile(artikel.getPreis() + "");
		schreibeZeile((artikel.getIstPackung() ? "t" : "f") + "");
		if (artikel.getIstPackung()) {
			Massengutartikel artikel_1 = (Massengutartikel) artikel;
			schreibeZeile(artikel_1.getPackungsGroesse() + "");
		}
		return true;
	}

	/*
	 *  Wenn später mal eine Kundenverwaltung ergänzt wird:
	 */

	/**
	 * Methode zum Einlesen der Kundedaten aus einer externen Datenquelle.
	 * @return
	 * @throws IOException
	 */
	public Kunde ladeKunde() throws IOException {
		// KundeNummer einlesen
		String nummerString = liesZeile();
		if (nummerString == null) {
			return null;
		}
		int nummer;
		try {
			nummer = Integer.parseInt(nummerString);
		}catch (NumberFormatException e) {
			System.err.println("Fehler beim Parsen der KundeNummer: " + nummerString);
			return null;
		}
		//KundeName einlesen
		String name = liesZeile();
		String benutzerkennung = liesZeile();
		String passwort = liesZeile();
		String strasse = liesZeile();
		String plz = liesZeile();
		String wohnort = liesZeile();
		return new Kunde(nummer,name, benutzerkennung, passwort, strasse, plz, wohnort);
	}

	/**
	 * Methode zum Schreiben der Kundedaten in eine externe Datenquelle.
	 * @param k
	 * @return
	 * @throws IOException
	 */
	public boolean speichereKunde(Kunde k) throws IOException {
		// nr, name, benutzerkennung, passwort, strasse, plz, wohnort
		schreibeZeile(k.getKundenNr() + "");
		schreibeZeile(k.getName() + "");
		schreibeZeile(k.getBenutzerkennung() + "");
		schreibeZeile(k.getPassword() + "");
		schreibeZeile(k.getStrasse() + "");
		schreibeZeile(k.getPlz() + "");
		schreibeZeile(k.getWohnort() + "");

		return true;
	}

	/**
	 * Methode zum Einlesen der Mitarbeiterdaten aus einer externen Datenquelle.
	 * @return
	 * @throws IOException
	 */
	public Mitarbeiter ladeMitarbeiter() throws IOException {
		//Mitarbeiter einlesen
		/**
		String Mitarbeiter = liesZeile();
		if (Mitarbeiter == null){
			//wenn keine Daten mehr vorhanden
			return null;
		}
		 */
		// MitarbeiterNummer einlesen
		String nummerString = liesZeile();
		if (nummerString == null) {
			return null;
		}
		int nummer;
		try {
			nummer = Integer.parseInt(nummerString);
		}catch (NumberFormatException e) {
			System.err.println("Fehler beim Parsen der KundeNummer: " + nummerString);
			return null;
		}
		String name = liesZeile();
		String benutzerkennung = liesZeile();
		String passwort = liesZeile();

		return new Mitarbeiter(nummer,name, benutzerkennung, passwort);
	}

	public boolean speichereMitarbeiter(Mitarbeiter m) throws IOException {
		//nr, name, benutzerkennung, passwort
		schreibeZeile(m.getMitarbeiterNr() + "");
		schreibeZeile(m.getName() + "");
		schreibeZeile(m.getBenutzerkennung() + "");
		schreibeZeile(m.getPassword() + "");

		return true;
	}
	
	/*
	 * Private Hilfsmethoden
	 */
	
	private String liesZeile() throws IOException {
		if (reader != null)
			return reader.readLine();
		else
			return "";
	}

	private void schreibeZeile(String daten) {
		if (writer != null)
			writer.println(daten);
	}
}
