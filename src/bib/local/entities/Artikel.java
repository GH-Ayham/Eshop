package bib.local.entities;


import bib.local.domain.exceptions.BestandPasstNichtMitPackungsGroesseException;
import bib.local.domain.exceptions.NichtGenuegendBestandException;

/**
 * Klasse zur Repräsentation einzelner Bücher.
 * 
 * @author teschke
 */
public class Artikel {

	// Attribute zur Beschreibung ein Artikel:
	private String bezeichnung;
	private int nummer;
	private int bestand;
	private double preis;
	private boolean istPackung;

	/**
	public Artikel(int nr, String bezeichnung, int bestand, double preis) {
		this(nr, bezeichnung, bestand, preis);
	}
	*/

	public Artikel(int nr, String bezeichnung, int bestand, double preis, boolean istPackung) {
		nummer = nr;
		this.bezeichnung = bezeichnung;
		this.bestand = bestand;
		this.preis = preis;
		this.istPackung = istPackung;
	}
	
	// --- Dienste der Buch-Objekte ---

	/**
	 * Standard-Methode von Object überschrieben.
	 * Methode wird immer automatisch aufgerufen, wenn ein Buch-Objekt als String
	 * benutzt wird (z.B. in println(buch);)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ("Nr: " + nummer + " / Bezeichnung: " + bezeichnung + " / Bestand:  " + bestand + " / Preis: " + preis );
	}

	/**
	 * Standard-Methode von Object überschrieben.
	 * Methode dient Vergleich von zwei Buch-Objekten anhand ihrer Werte,
	 * d.h. Titel und Nummer.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public boolean equals(Object anderesArtikel) {
		if (anderesArtikel instanceof Artikel)
			return ((this.nummer == ((Artikel) anderesArtikel).nummer)
					&& (this.bezeichnung.equals(((Artikel) anderesArtikel).bezeichnung)));
		else
			return false;
	}

	public void artikelBestandAktualisieren(int menge) throws NichtGenuegendBestandException, BestandPasstNichtMitPackungsGroesseException {

		if (this.bestand + menge >= 0) {
			this.bestand += menge;
		}else {
			throw new NichtGenuegendBestandException("in artikelBestandAktualisieren()");
		}
	}



	/*
	 * Ab hier Accessor-Methoden
	 */
	
	public int getNummer() {
		return nummer;
	}

	public int getBestand(){ return bestand; }

	public String getBezeichnung() {
		return bezeichnung;
	}

	public double getPreis() {return preis;}

	public void setNummer(int nummer) {this.nummer = nummer;}

	public void setBestand(int bestand) {this.bestand = bestand;}

	public void setBezeichnung(String bezeichnung) {this.bezeichnung = bezeichnung;}

	public void setPreis(double preis) {this.preis = preis;}

	public boolean getIstPackung() {return istPackung;}

	public void setIstPackung(boolean istPackung) {this.istPackung = istPackung;}

}
