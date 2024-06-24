package bib.local.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bib.local.domain.exceptions.*;
import bib.local.entities.*;

/**
 * Klasse zur Verwaltung einer (sehr einfachen) Bibliothek.
 * Bietet Methoden zum Zurückgeben aller Bücher im Bestand, 
 * zur Suche nach Büchern, zum Einfügen neuer Bücher 
 * und zum Speichern des Bestands.
 * 
 * @author teschke
 * @version 2 (Verwaltung in ArrayList)
 */
public class EShop {
	// Präfix für Namen der Dateien, in der die Bibliotheksdaten gespeichert sind
	private String datei = "";
	
	private ArtikelVerwaltung ArtikelVW;
	private KundeVerwaltung kundenVW;
	private MitarbeiterVerwaltung mitarbeiterVW;
	private Benutzer benutzer;
	private WarenkorbVerwaltung warenkorbVerwaltung;
	private Rechnung rechnung;

	// hier weitere Verwaltungsklassen, z.B. für Autoren oder Angestellte
	
	/**
	 * Konstruktor, der die Basisdaten (Bücher, Kunden, Autoren) aus Dateien einliest
	 * (Initialisierung der Bibliothek).
	 * 
	 * Namensmuster für Dateien:
	 *   datei+"_B.txt" ist die Datei der Bücher
	 *   datei+"_K.txt" ist die Datei der Kunden
	 * 
	 * @param datei Präfix für Dateien mit Basisdaten (Bücher, Kunden, Autoren)
	 * @throws IOException z.B. wenn eine der Dateien nicht existiert.
	 */
	public EShop(String datei) throws IOException {
		this.datei = datei;
		
		// Buchbestand aus Datei einlesen
		ArtikelVW = new ArtikelVerwaltung();
		ArtikelVW.liesDaten(datei+"_B.txt");

//		// Kundenkartei aus Datei einlesen
		kundenVW = new KundeVerwaltung();
		kundenVW.liesDaten(datei+"_K.txt");
//		meineKunden.schreibeDaten(datei+"_K.txt");

		// Mitarbeiter aus Datei einlesen
		mitarbeiterVW = new MitarbeiterVerwaltung();
		mitarbeiterVW.liesDaten(datei+"_M.txt");

		// Warenkorb
		warenkorbVerwaltung = new WarenkorbVerwaltung();

	}


	/**
	 * Methode, die eine Liste aller im Bestand befindlichen Bücher zurückgibt.
	 * 
	 * @return Liste aller Bücher im Bestand der Bibliothek
	*/
	public ArrayList<Artikel> gibAlleArtikel() {
		// einfach delegieren an meineBuecher
		return ArtikelVW.getArtikelBestand();
	}

	/**
	 * Methode zum Suchen von Büchern anhand des Titels. Es wird eine Liste von Büchern
	 * zurückgegeben, die alle Bücher mit exakt übereinstimmendem Titel enthält.
	 * 
	 * @param titel Titel des gesuchten Buchs
	 * @return Liste der gefundenen Bücher (evtl. leer)

	public List<Artikel> sucheNachTitel(String titel) {
		// einfach delegieren an meineBuecher
		return ArtikelVW.sucheBuecher(titel);
	}
	*/
	public Artikel sucheArtikel(int artikelNummer) throws ArtikelNichtGefundenException {
		try {
			return ArtikelVW.sucheArtikel(artikelNummer);
		}catch (ArtikelNichtGefundenException e) {
			throw new ArtikelNichtGefundenException(artikelNummer, " - in 'sucheArtikel()'");
		}
	}

	/**
	 * Methode zum Einfügen eines neuen Buchs in den Bestand. 
	 * Wenn das Buch bereits im Bestand ist, wird der Bestand nicht geändert.
	 * 
	 * @param bezeichnung Titel des Buchs
	 * @param nummer Nummer des Buchs
	 * @return Buch-Objekt, das im Erfolgsfall eingefügt wurde
	 * @throws ArtikelExistiertBereitsException wenn das Buch bereits existiert
	 */
	public Artikel fuegeArtikelEin(int nummer, String bezeichnung, int bestand, double preis, boolean istPackung, int packungsgroesse) throws ArtikelExistiertBereitsException {

		//Artikel artikel = new Artikel(nummer, bezeichnung, bestand, preis);
		//ArtikelVW.einfuegen(artikel);
		//return artikel;

		Artikel artikel;
		if (istPackung) {
			artikel = new Massengutartikel(nummer, bezeichnung, bestand, preis, packungsgroesse);
		}else {
			artikel = new Artikel(nummer, bezeichnung, bestand, preis, istPackung);
		}
		ArtikelVW.einfuegen(artikel);
		return artikel;
	}

	/**
	 * Methode zum Löschen eines Buchs aus dem Bestand. 
	 * Es wird nur das erste Vorkommen des Buchs gelöscht.
	 * 
	 * @param bezeichnung Titel des Buchs
	 * @param nummer Nummer des Buchs

	public void loescheBuch(String bezeichnung,int bestand, int nummer) {
		Artikel b = new Artikel(bezeichnung, bestand, nummer);
		ArtikelVW.loeschen(b);
	}
	*/
	public void loescheArtikel(int artikelNummer) throws ArtikelNichtGefundenException {
		try {
			ArtikelVW.loeschen(artikelNummer);
		} catch (ArtikelNichtGefundenException e) {
			throw new ArtikelNichtGefundenException(artikelNummer, " - in 'loescheArtikel()'");
		}
	}
	/**
	 * Methode zum Speichern des Artikelbestands in einer Datei.
	 * 
	 * @throws IOException z.B. wenn Datei nicht existiert
	 */
	public void schreibeArtikel() throws IOException {
		ArtikelVW.schreibeDaten(datei+"_B.txt");
	}

	public void schreibeKunde() throws IOException {
		kundenVW.schreibeDaten(datei+"_K.txt");
	}

	public void schreibeMitarbeiter() throws IOException {
		mitarbeiterVW.schreibeDaten(datei+"_M.txt");
	}

	// TODO: Weitere Funktionen der Bibliotheksverwaltung, z.B. ausleihen, zurückgeben etc.
	// ...

	//Mitarbeiter registieren
	public Mitarbeiter registriereMitarbeiter(int nr,String name, String benutzerkennung, String passwort) throws NutzerExistiertBereitsException {
		Mitarbeiter mitarbeiter = mitarbeiterVW.registriereMitarbeiter(nr, name, benutzerkennung, passwort);
		return mitarbeiter;
	}

	//Kunde registieren
	public Kunde registriereKunde(String name, String benutzerkennung, String passwort, String strasse, String plz, String wohnort) throws NutzerExistiertBereitsException {
		Kunde kunde = kundenVW.registriereKunde(name, benutzerkennung, passwort, strasse, plz, wohnort);
		return kunde;

	}

	public ArtikelVerwaltung getArtikelVW(){
		return ArtikelVW;
	}

	public KundeVerwaltung getKundenVW() {
		return kundenVW;
	}

	public MitarbeiterVerwaltung getMitarbeiterVW() {
		return mitarbeiterVW;
	}

	public Benutzer getBenutzer() {
		return benutzer;
	}

	public boolean istMitarbeiter() {
		return benutzer instanceof Mitarbeiter;
	}

	public boolean istKunde() {
		return benutzer instanceof Kunde;
	}

	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}

	//Warenkorb methoden

	public WarenkorbVerwaltung getWarenkorbVW() {return warenkorbVerwaltung;}

	// Änderungen an den Warenkorb-Methoden, um den aktuellen Kunden zu berücksichtigen
	public void fuegeArtikelZuWarenkorbHinzu(Kunde kunde, Artikel artikel, int menge) throws  BestandPasstNichtMitPackungsGroesseException, NichtGenuegendBestandException {
		warenkorbVerwaltung.addToWarenkorb(kunde, artikel, menge);
	}



	public void entferneArtikelAusWarenkorb(Kunde kunde, int artikelNummer) throws ArtikelNichtGefundenException, BestandPasstNichtMitPackungsGroesseException, NichtGenuegendBestandException, WarenkorbLeerException {
		warenkorbVerwaltung.ArtikelInWarenkorbLoeschen(kunde, artikelNummer);
	}

	public void leereWarenkorb(Kunde kunde) throws WarenkorbLeerException {
		warenkorbVerwaltung.WarenkorbLeeren(kunde);
	}

	public void zeigeWarenkorbAn(Kunde kunde) throws WarenkorbLeerException {
		warenkorbVerwaltung.WarenkorbAnzeigen(kunde);
	}

	public void artikelBestandAendern(Kunde kunde, Artikel artikel, int neueMenge) throws ArtikelNichtImWarenkorbGefunden {
		warenkorbVerwaltung.artikelBestandAendern(kunde, artikel, neueMenge);
	}

	public void RechnungAusgeben() {
		if (benutzer instanceof Kunde) {
			Kunde kunde = (Kunde) benutzer;
			//Kunde Daten aufrufen
			String KundeName = kunde.getName();
			String KundeStrasse = kunde.getStrasse();
			String KundePlz = kunde.getPlz();
			String KundeWohnort = kunde.getWohnort();
			System.out.println("Rechnung für Kunde: " + KundeName);
			System.out.println("Adresse: " + KundeStrasse + ", " + KundePlz + " " + KundeWohnort);

            Warenkorb warenkorb = kunde.getWarenkorb();  // ÄNDERUNG: Kunden-spezifischen Warenkorb verwenden
			List<WarenkorbEintrag> eintraege = warenkorb.getEintraege();  // ÄNDERUNG: Warenkorb-Einträge des Kunden durchlaufen
			System.out.println("Sie Haben gekauft: ");
			for (WarenkorbEintrag w : eintraege) {
				System.out.println(w.getArtikel().getBezeichnung() + "Menge: " + w.getMenge() + "Preis: " + w.getTotalPreis());
			}
            double totalPrice = warenkorbVerwaltung.getTotalPreis(kunde);
			Rechnung rechnung = new Rechnung(kunde, totalPrice);
			rechnung.printRechnung();
            try {
                schreibeArtikel();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
			warenkorbVerwaltung.warenkorbAbschliessen(kunde);
        }
	}

	public Benutzer login(String benutzername, String passwort) {
		Kunde kunde = kundenVW.findeKunde(benutzername, passwort);
		if (kunde != null) {
			this.benutzer = kunde;
			return kunde;
		}
		Mitarbeiter mitarbeiter = mitarbeiterVW.findeMitarbeiter(benutzername, passwort);
		if (mitarbeiter != null) {
			this.benutzer = mitarbeiter;
			return mitarbeiter;
		}

		return null;
	}


}
