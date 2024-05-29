package bib.local.domain;

import java.io.IOException;
import java.util.*;

import bib.local.domain.exceptions.ArtikelExistiertBereitsException;
import bib.local.domain.exceptions.ArtikelNichtGefundenException;
import bib.local.persistence.FilePersistenceManager;
import bib.local.persistence.PersistenceManager;
import bib.local.entities.Artikel;


/**
 * Klasse zur Verwaltung von Büchern.
 * 
 * @author teschke
 * @version 2 (Verwaltung in ArrayList)
 */
public class ArtikelVerwaltung {

	private Artikel artikel;

	// Verwaltung des Buchbestands in einer verketteten Liste
	private List<Artikel> ArtikelBestand = new ArrayList<Artikel>();

	// Persistenz-Schnittstelle, die für die Details des Dateizugriffs verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();
	
	/**
	 * Methode zum Einlesen von Buchdaten aus einer Datei.
	 * 
	 * @param datei Datei, die einzulesenden Bücherbestand enthält
	 * @throws IOException
	 */
	public void liesDaten(String datei) throws IOException {
		// PersistenzManager für Lesevorgänge öffnen
		pm.openForReading(datei);

		Artikel einArtikel;
		do {
			// Buch-Objekt einlesen
			einArtikel = pm.ladeArtikel();
			if (einArtikel != null) {
				// Buch in Liste einfügen
				try {
					einfuegen(einArtikel);
				} catch (ArtikelExistiertBereitsException e1) {
					// Kann hier eigentlich nicht auftreten,
					// daher auch keine Fehlerbehandlung...
				}
			}
		} while (einArtikel != null);

		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}

	/**
	 * Methode zum Schreiben der Buchdaten in eine Datei.
	 * 
	 * @param datei Datei, in die der Bücherbestand geschrieben werden soll
	 * @throws IOException
	 */
	public void schreibeDaten(String datei) throws IOException  {
		// PersistenzManager für Schreibvorgänge öffnen
		pm.openForWriting(datei);

		for (Object arikel: ArtikelBestand) {
			pm.speichereArtikel((Artikel) arikel);
		}

		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
		
	/**
	 * Methode, die ein Buch an das Ende der Bücherliste einfügt.
	 * 
	 * @param einArtikel das einzufügende Buch
	 * @throws ArtikelExistiertBereitsException wenn das Buch bereits existiert
	 */
	public void einfuegen(Artikel einArtikel) throws ArtikelExistiertBereitsException {
		if (artikelNummerExistiert(einArtikel.getNummer()) || artikelBezeichnungExistiert(einArtikel.getBezeichnung())) {
			throw new ArtikelExistiertBereitsException(einArtikel, " - in 'einfuegen()'");
		}

		ArtikelBestand.add(einArtikel);
	}

	// Methode, um die Eindeutigkeit der Artikelnummer zu überprüfen
	public boolean artikelNummerExistiert(int nummer) {
		for (Artikel artikel : ArtikelBestand) {
			if (artikel.getNummer() == nummer) {
				return true;
			}
		}
		return false;
	}

	// Methode, um die Eindeutigkeit der Bezeichnung zu überprüfen
	public boolean artikelBezeichnungExistiert(String bezeichnung) {
		for (Artikel artikel : ArtikelBestand) {
			if (artikel.getBezeichnung().equalsIgnoreCase(bezeichnung)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Löscht einen Artikel aus dem Artikelbestand basierend auf der Artikelnummer.
	 *
	 * @param artikelNummer Die Nummer des zu löschenden Artikels
	 * @throws ArtikelNichtGefundenException wenn der Artikel nicht im Bestand gefunden wird
	 */
	public void loeschen(int artikelNummer) throws ArtikelNichtGefundenException {
		Iterator<Artikel> iterator = ArtikelBestand.iterator();
		boolean artikelGefunden = false;

		while (iterator.hasNext()) {
			Artikel artikel = iterator.next();
			if (artikel.getNummer() == artikelNummer) {
				iterator.remove();
				artikelGefunden = true;
				break;
			}
		}

		if (!artikelGefunden) {
			throw new ArtikelNichtGefundenException(artikelNummer, " - in 'loeschen()'");
		}
	}

	/**
	 *
	 * @param artikelNummer
	 * @return
	 * @throws ArtikelNichtGefundenException
	 */
	public Artikel sucheArtikel(int artikelNummer) throws ArtikelNichtGefundenException {
		for (Artikel artikel : ArtikelBestand) {
			if (artikel.getNummer() == artikelNummer) {
				return artikel;
			}
		}
		throw new ArtikelNichtGefundenException(artikelNummer, " - in 'sucheArtikel()'");
	}

	/**
	 * Methode, die anhand eines Titels nach Büchern sucht. Es wird eine Liste von Büchern
	 * zurückgegeben, die alle Bücher mit exakt übereinstimmendem Titel enthält.
	 * 
	 * @param titel Titel des gesuchten Buchs
	 * @return Liste der Bücher mit gesuchtem Titel (evtl. leer)
	 */
	public List<Artikel> sucheBuecher(String titel) {
		List<Artikel> suchErgebnis = new ArrayList<Artikel>();

//		Iterator<Buch> it = buchBestand.iterator();
//		while (it.hasNext()) {
//			Buch buch = it.next();
//			if (buch.getTitel().equals(titel)) {
//				suchErgebnis.add(buch);
//			}
//		}

		for (Artikel buch: ArtikelBestand) {
			if (buch.getBezeichnung().equals(titel)) {
				suchErgebnis.add(buch);
			}
		}

		return suchErgebnis;
	}
	
	/**
	 * Methode, die eine KOPIE des Bücherbestands zurückgibt.
	 * (Eine Kopie ist eine gute Idee, wenn ich dem Empfänger 
	 * der Daten nicht ermöglichen möchte, die Original-Daten 
	 * zu manipulieren.)
	 * ABER ACHTUNG: Die in der kopierten Bücherliste referenzierten
	 * 			sind nicht kopiert worden, d.h. ursprüngliche
	 * 			Bücherliste und ihre Kopie verweisen auf dieselben
	 * 			Buch-Objekte. Eigentlich müssten die einzelnen Buch-Objekte
	 * 			auch kopiert werden.
	 *
	 * @return Liste aller Bücher im Buchbestand (Kopie)
	 */
	public ArrayList<Artikel> getArtikelBestand() {
		return new ArrayList<Artikel>(ArtikelBestand);
	}
	
	// TODO: Weitere Methoden, z.B. zum Auslesen und Entfernen von Büchern
	// ...
}
