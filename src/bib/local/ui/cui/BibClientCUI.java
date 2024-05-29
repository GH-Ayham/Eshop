package bib.local.ui.cui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import bib.local.domain.exceptions.*;
import bib.local.domain.EShop;
import bib.local.entities.*;


/**
 * Klasse für sehr einfache Benutzungsschnittstelle für die Bibliothek.
 * Die Benutzungsschnittstelle basiert auf Ein- und Ausgaben auf der Kommandozeile,
 * daher der Name CUI (Command line User Interface).
 * 
 * @author teschke
 * @version 2 (Verwaltung in ArrayList)
 */
public class BibClientCUI {

	private EShop shop;
	private BufferedReader in;

	public BibClientCUI(String datei) throws IOException {
		shop = new EShop(datei);
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	private String liesEingabe() throws IOException {
		return in.readLine().trim();
	}

	private int liesIntEingabe() throws IOException, FalscheEingabeTypException {
        try{
			return Integer.parseInt(liesEingabe());
		}catch (NumberFormatException  e){
			throw new FalscheEingabeTypException("Ungültige Eingabe. Bitte eine Zahl eingeben.");
		}
    }

	private double liesDoubleEingabe() throws IOException, FalscheEingabeTypException {
		try{
			return Double.parseDouble(liesEingabe());
		}catch (NumberFormatException  e){
			throw new FalscheEingabeTypException("Ungültige Eingabe. Bitte eine Zahl eingeben.");
		}
	}

	/* (non-Javadoc)
	 *
	 * Interne (private) Methode zum Ausgeben von Artikellisten.
	 *
	 */
	private void gibArtikellisteAus(List<Artikel> ArtikelListe) {
		if (ArtikelListe.isEmpty()) {
			System.out.println("Artikelliste ist leer.");
		} else {
			for (Artikel artikel : ArtikelListe) {
				System.out.println(artikel);
			}
		}
	}

	private void zeigeErstesMenu() {
		System.out.println("\nHauptmenü:");
		System.out.println("1. Einloggen");
		System.out.println("2. Registrieren");
		System.out.println("q. Beenden");
		System.out.print("Ihre Wahl: ");
	}

	private void verarbeiteErstesMenuEingabe(String auswahl) throws IOException, BenutzerkennungOderPasswortFalschException, NutzerExistiertBereitsException {
		switch (auswahl) {
			case "1":
				zeigeLoginMenu();
				break;
			case "2":
				registrierenKundeMenu();
				break;
			case "q":
				System.out.println("Programm beendet. Auf Wiedersehen!");
				break;
			default:
				System.out.println("Ungültige Eingabe. Bitte erneut versuchen.");
				break;
		}
	}

	private void zeigeLoginMenu() throws IOException, BenutzerkennungOderPasswortFalschException {
		System.out.println("\nLogin-Optionen:");
		System.out.println("1. Einloggen als Kunde");
		System.out.println("2. Einloggen als Mitarbeiter");
		System.out.println("3. Zurück zum registrieren");
		System.out.print("Ihre Wahl: ");
		String auswahl = liesEingabe();
		verarbeiteLoginMenuEingabe(auswahl);
	}

	private void verarbeiteLoginMenuEingabe(String auswahl) throws IOException, BenutzerkennungOderPasswortFalschException {
		if (auswahl.equals("1") || auswahl.equals("2")) {
			System.out.print("Benutzerkennung: ");
			String benutzerkennung = liesEingabe();
			System.out.print("Passwort: ");
			String passwort = liesEingabe();
			switch (auswahl) {
				case "1":
					einloggenAlsKunde(benutzerkennung, passwort);
					break;
				case "2":
					einloggenAlsMitarbeiter(benutzerkennung, passwort);
					break;
			}
		}else if (auswahl.equals("3")) {
			run();
		} else {
			System.out.println("Ungültige Eingabe. Bitte erneut versuchen.");
			zeigeLoginMenu();
		}
	}

	private void einloggenAlsKunde(String benutzerkennung, String passwort) throws BenutzerkennungOderPasswortFalschException {
		Kunde kunde = shop.getKundenVW().sucheKundeBeiBenutzerkennung(benutzerkennung);
		if (kunde != null && kunde.getPassword().equals(passwort)) {
			shop.setBenutzer(kunde);
			System.out.println("Erfolgreich als Kunde eingeloggt: " + kunde.getName());
		} else {
			throw new BenutzerkennungOderPasswortFalschException("Benutzerkennung oder Passwort falsch.");
		}
	}

	private void einloggenAlsMitarbeiter(String benutzerkennung, String passwort) throws BenutzerkennungOderPasswortFalschException {
		Mitarbeiter mitarbeiter = shop.getMitarbeiterVW().sucheMitarbeiterBeiBenutzerkennung(benutzerkennung);
		if (mitarbeiter != null && mitarbeiter.getPassword().equals(passwort)) {
			shop.setBenutzer(mitarbeiter);
			System.out.println("Erfolgreich als Mitarbeiter eingeloggt: " + mitarbeiter.getName());
		} else {
			throw new BenutzerkennungOderPasswortFalschException("Benutzerkennung oder Passwort falsch.");
		}
	}

	private void zeigeKundeMenu() {
		System.out.println("\nKundenmenü:");
		System.out.println("1. Artikelliste anzeigen");
		System.out.println("2. Artikel zum Warenkorb hinzufügen");
		System.out.println("3. Warenkorb anzeigen");
		System.out.println("4. Artikelbestand ändern");
		System.out.println("5. Artikel aus Warenkorb entfernen");
		System.out.println("6. Warenkorb leeren");
		System.out.println("7. Einkauf abschließen");
		System.out.println("8. Ausloggen");
		System.out.print("Ihre Wahl: ");
	}

	private void verarbeiteKundeMenuEingabe(String auswahl) throws IOException, ArtikelNichtGefundenException, WarenkorbLeerException, BestandPasstNichtMitPackungsGroesseException {
		switch (auswahl) {
			case "1":
				gibArtikellisteAus(shop.gibAlleArtikel());
				break;
			case "2":
				fuegeArtikelZuWarenkorbHinzu();
				break;
			case "3":
				shop.zeigeWarenkorbAn();
				break;
			case "4":
				aendereArtikelbestand();
				break;
			case "5":
				entferneArtikelAusWarenkorb();
				break;
			case "6":
				shop.getWarenkorbVW().WarenkorbLeeren();
				System.out.println("Warenkorb wurde geleert.");
				break;
			case "7":
				shop.RechnungAusgeben();
				break;
			case "8":
				shop.setBenutzer(null);
				System.out.println("Sie sind ausgeloggt.");
				break;
			default:
				System.out.println("Ungültige Eingabe. Bitte erneut versuchen.");
				break;
		}
	}

	private void fuegeArtikelZuWarenkorbHinzu() throws IOException, ArtikelNichtGefundenException {
		while (true) {
			try {
				System.out.print("Artikelnummer: ");
				int nr = liesIntEingabe();

				Artikel artikel = shop.getArtikelVW().sucheArtikel(nr);

				if (artikel != null && !artikel.getIstPackung()) {
					System.out.print("Menge: ");
					int menge = liesIntEingabe();
					shop.fuegeArtikelZuWarenkorbHinzu(artikel, menge);
					System.out.println("Artikel wurde zum Warenkorb hinzugefügt.");

				} else if (artikel != null && artikel.getIstPackung()){
					System.out.println("Sie können diesen Artikel nur als Massengut kaufen.");
					Massengutartikel artikel_1 = (Massengutartikel) artikel;
					System.out.println("Menge gleich oder nur in Vielfachen dieser Packungsgröße: " +  artikel_1.getPackungsGroesse());
					System.out.print("Menge: ");
					int menge = liesIntEingabe();
					shop.fuegeMassengutartikelzuWarenKorbHinzu((Massengutartikel) artikel, menge);
					System.out.println("Artikel wurde zum Warenkorb hinzugefügt.");

				} else {
					System.out.println("Artikel mit der Nummer " + nr + " wurde nicht gefunden.");
				}
				break; // Schleife verlassen nach erfolgreicher Eingabe
			} catch (FalscheEingabeTypException e) {
				System.out.println(e.getMessage());
			} catch (BestandPasstNichtMitPackungsGroesseException e) {
				System.out.println("Fehler: " + e.getMessage());
			} catch (NichtGenuegendBestandException e) {
                throw new RuntimeException(e);
            }
        }
	}

	private void aendereArtikelbestand() throws IOException, ArtikelNichtGefundenException, WarenkorbLeerException {
		shop.zeigeWarenkorbAn();

		try {
			System.out.print("Artikelnummer: ");
			int nr = liesIntEingabe();
			Artikel artikel = shop.getArtikelVW().sucheArtikel(nr);
			if (artikel != null) {
				System.out.print("Neue Menge: ");
				int neueMenge = liesIntEingabe();
				shop.artikelBestandAendern(artikel, neueMenge);
				System.out.println("Artikelbestand wurde geändert.");
			} else {
				System.out.println("Artikel mit der Nummer " + nr + " wurde nicht gefunden.");
			}

		} catch (FalscheEingabeTypException | ArtikelNichtImWarenkorbGefunden e) {
			System.out.println(e.getMessage());
		}

	}

	private void entferneArtikelAusWarenkorb() throws IOException, WarenkorbLeerException {
		shop.zeigeWarenkorbAn();
		try {
			System.out.print("Artikelnummer: ");
			int nr = liesIntEingabe();
			shop.entferneArtikelAusWarenkorb(nr);
			System.out.println("Artikel wurde aus dem Warenkorb entfernt.");

		} catch (FalscheEingabeTypException | BestandPasstNichtMitPackungsGroesseException |
				 NichtGenuegendBestandException e) {
			System.out.println(e.getMessage());
		} catch (ArtikelNichtGefundenException e) {
			System.out.println("Fehler: " + e.getMessage());
		}

    }

	private void zeigeMitarbeiterMenu() {
		System.out.println("\nMitarbeitermenü:");
		System.out.println("1. Artikelliste anzeigen");
		System.out.println("2. Neuen Artikel hinzufügen");
		System.out.println("3. Artikel bearbeiten");
		System.out.println("4. Artikel löschen");
		System.out.println("5. Neuen Mitarbeiter registrieren");
		System.out.println("6. Ausloggen");
		System.out.print("Ihre Wahl: ");
	}

	private void verarbeiteMitarbeiterMenuEingabe(String auswahl) throws IOException, NutzerExistiertBereitsException, ArtikelNichtGefundenException, BestandPasstNichtMitPackungsGroesseException {
		switch (auswahl) {
			case "1":
				gibArtikellisteAus(shop.gibAlleArtikel());
				break;
			case "2":
				fuegeNeuenArtikelHinzu();
				break;
			case "3":
				bearbeiteArtikel();
				break;
			case "4":
				loescheArtikel();
				break;
			case "5":
				registriereMitarbeiter();
				break;
			case "6":
				shop.setBenutzer(null);
				System.out.println("Sie sind ausgeloggt.");
				break;
			default:
				System.out.println("Ungültige Eingabe. Bitte erneut versuchen.");
				break;
		}
	}



	private void fuegeNeuenArtikelHinzu() throws IOException, ArtikelNichtGefundenException, BestandPasstNichtMitPackungsGroesseException {
		try{
			System.out.print("Artikelnummer: ");
			int nr = liesIntEingabe();

			System.out.print("Artikelbezeichnung: ");
			String bezeichnung = liesEingabe();

			System.out.print("Artikelbestand: ");
			int bestand = liesIntEingabe();

			System.out.print("Artikelpreis: ");
			double preis = liesDoubleEingabe();

			System.out.print("Ist es ein Massengutartikel? (ja/nein): ");
			String istPackungEingabe = liesEingabe();
			boolean istPackung = istPackungEingabe.equalsIgnoreCase("ja");
			int packungsgroesse = 0;
			if (istPackung) {
				System.out.print("Packungsgröße: ");
				packungsgroesse = liesIntEingabe();
			}

			shop.fuegeArtikelEin(nr, bezeichnung, bestand, preis, istPackung, packungsgroesse);
			shop.schreibeArtikel();
			System.out.println("Artikel wurde erfolgreich hinzugefügt.");

		} catch (FalscheEingabeTypException e) {
			System.out.println(e.getMessage());
			// Erneut versuchen, wenn falsche Eingabe
			fuegeArtikelZuWarenkorbHinzu();
		} catch (ArtikelExistiertBereitsException e) {
			System.out.println("Fehler: Artikel existiert bereits.");
		}
	}

	private void bearbeiteArtikel() throws IOException {
		try {
		System.out.print("Artikelnummer: ");
		int nr = liesIntEingabe();

			Artikel artikel = shop.getArtikelVW().sucheArtikel(nr);
			if (artikel != null) {
				System.out.print("Neue Bezeichnung: ");
				String bezeichnung = liesEingabe();
				artikel.setBezeichnung(bezeichnung);

				System.out.print("Neuer Bestand: ");
				int bestand = liesIntEingabe();
				artikel.setBestand(bestand);

				System.out.print("Neuer Preis: ");
				double preis = liesDoubleEingabe();
				artikel.setPreis(preis);

				if (artikel instanceof Massengutartikel) {
					System.out.print("Neue Packungsgröße: ");
					int packungsgroesse = liesIntEingabe();
					((Massengutartikel) artikel).setPackungsGroesse(packungsgroesse);
				}

				System.out.println("Artikel wurde erfolgreich aktualisiert.");
			} else {
				System.out.println("Artikel mit der Nummer " + nr + " wurde nicht gefunden.");
			}

		} catch (FalscheEingabeTypException e) {
			System.out.println(e.getMessage());
			// Erneut versuchen, wenn falsche Eingabe
			bearbeiteArtikel();
		} catch (ArtikelNichtGefundenException e) {
			System.out.println("Fehler: " + e.getMessage());
		}
	}

	private void loescheArtikel() throws IOException {
		try {
		System.out.print("Artikelnummer: ");
		int nr = liesIntEingabe();

		shop.loescheArtikel(nr);
		shop.schreibeArtikel();
		System.out.println("Artikel wurde erfolgreich gelöscht.");

		} catch (FalscheEingabeTypException e) {
			System.out.println(e.getMessage());
			// Erneut versuchen, wenn falsche Eingabe
			loescheArtikel();
		} catch (ArtikelNichtGefundenException e) {
			System.out.println("Fehler: " + e.getMessage());
		}
	}

	private void registriereMitarbeiter() throws IOException, NutzerExistiertBereitsException {
		try {
		System.out.print("Mitarbeiternummer: ");
		int nr = liesIntEingabe();
		System.out.print("Name: ");
		String name = liesEingabe();
		System.out.print("Benutzerkennung: ");
		String benutzerkennung = liesEingabe();
		System.out.print("Passwort: ");
		String passwort = liesEingabe();

		Mitarbeiter mitarbeiter = shop.registriereMitarbeiter(nr, name, benutzerkennung, passwort);
		shop.schreibeMitarbeiter();
		System.out.println("Mitarbeiter wurde erfolgreich registriert: " + mitarbeiter.getName());

		} catch (FalscheEingabeTypException e) {
			System.out.println(e.getMessage());
			// Erneut versuchen, wenn falsche Eingabe
			registriereMitarbeiter();
		} catch (NutzerExistiertBereitsException e) {
			System.out.println("Fehler: " + e.getMessage());
		}
	}

	private void registrierenKundeMenu() throws IOException {
		System.out.print("Name: ");
		String name = liesEingabe();
		System.out.print("Benutzerkennung: ");
		String benutzerkennung = liesEingabe();
		System.out.print("Passwort: ");
		String passwort = liesEingabe();
		System.out.print("Straße: ");
		String strasse = liesEingabe();
		System.out.print("PLZ: ");
		String plz = liesEingabe();
		System.out.print("Wohnort: ");
		String wohnort = liesEingabe();

		try {
			Kunde kunde = shop.registriereKunde(name, benutzerkennung, passwort, strasse, plz, wohnort);
			shop.schreibeKunde();
			System.out.println("Registrierung erfolgreich: " + kunde.getName());
		} catch (NutzerExistiertBereitsException e) {
			System.out.println("Fehler: " + e.getMessage());
		}
	}

	public void run() {
		String input = "";

		do {
			try {
				if (shop.getBenutzer() == null) {
					zeigeErstesMenu();
					input = liesEingabe();
					verarbeiteErstesMenuEingabe(input);
				} else if (shop.istMitarbeiter()) {
					zeigeMitarbeiterMenu();
					input = liesEingabe();
					verarbeiteMitarbeiterMenuEingabe(input);
				} else if (shop.istKunde()) {
					zeigeKundeMenu();
					input = liesEingabe();
					verarbeiteKundeMenuEingabe(input);
				}
			} catch (IOException e) {
				System.out.println("Ein Fehler ist aufgetreten: " + e.getMessage());
			} catch (NutzerExistiertBereitsException e) {
				System.out.println("Ein Nutzer existiert bereits: " + e.getMessage());
			} catch (ArtikelNichtGefundenException e) {
				System.out.println("Artikel nicht gefunden: " + e.getMessage());
			} catch (WarenkorbLeerException | BestandPasstNichtMitPackungsGroesseException e) {
				System.out.println(e.getMessage());
			} catch (BenutzerkennungOderPasswortFalschException e) {
				System.out.println("Benutzerkennung oder Passwort falsch: " + e.getMessage());
			}
        }while (!input.equals("q"));
	}

	
	/**
	 * Die main-Methode...
	 */
	public static void main(String[] args) {
		BibClientCUI cui;
		try {
			cui = new BibClientCUI("BIB");
			cui.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
