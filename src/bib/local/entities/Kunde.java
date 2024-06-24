package bib.local.entities;


import bib.local.domain.WarenkorbVerwaltung;

/**
 * Klasse zur Repräsentation einzelner Kunden.
 * 
 * Die Klasse wird derzeit noch nicht verwendet, weil die Bibliotheks-
 * Anwendung bisland nur Bücher verwaltet.
 * 
 * @author teschke
 */
public class Kunde extends Benutzer{

	private int KundenNr;
	//private float umsatz = 0.0f;
	private String strasse = "";
	private String plz = "";
	private String wohnort = "";
	// private WarenkorbVerwaltung warenkorbVerwaltung;
	private Warenkorb warenkorb;


    public Kunde(int Nr, String name, String benutzerkennung, String passwort, String strasse, String plz, String wohnort) {

		super(name, benutzerkennung, passwort, false, true);
		this.KundenNr = Nr;
		//this.umsatz = umsatz;
		this.strasse = strasse;
		this.plz = plz;
		this.wohnort = wohnort;
//		this.warenkorbVerwaltung = new WarenkorbVerwaltung();
		this.warenkorb = new Warenkorb();
	}
    
	// Methoden zum Setzen und Lesen der Kunden-Eigenschaften,
	// z.B. getStrasse() und setStrasse()

	//public float getUmsatz() {return umsatz;}

	public String getStrasse() {return strasse;}

	public String getPlz() {return plz;}

	public String getWohnort() {return wohnort;}

	public int getKundenNr() {return KundenNr;}

	//public void setUmsatz(float umsatz) {umsatz = umsatz;}

	public void setStrasse(String strasse) {strasse = strasse;}

	public void setPlz(String plz) {plz = plz;}

	public void setWohnort(String wohnort) {wohnort = wohnort;}

	public Warenkorb getWarenkorb() {
		return warenkorb;
	}



}
