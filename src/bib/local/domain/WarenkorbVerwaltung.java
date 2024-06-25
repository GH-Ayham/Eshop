package bib.local.domain;

import bib.local.domain.exceptions.*;
import bib.local.entities.*;

import java.util.List;

public class WarenkorbVerwaltung {

    /**
     * Fügt einen Artikel zum Warenkorb eines Kunden hinzu.
     *
     * @param kunde  Der Kunde, dessen Warenkorb aktualisiert werden soll.
     * @param artikel Der Artikel, der hinzugefügt werden soll.
     * @param menge   Die Menge des Artikels.
     */
    public void addToWarenkorb(Kunde kunde, Artikel artikel, int menge) throws BestandPasstNichtMitPackungsGroesseException, NichtGenuegendBestandException {
        Warenkorb warenkorb = kunde.getWarenkorb();  // Kunden-spezifischen Warenkorb verwenden
        artikel.artikelBestandAktualisieren(-menge);
        for (WarenkorbEintrag w : warenkorb.getEintraege()) {  // Warenkorb-Einträge des Kunden durchlaufen
            if (w.getArtikel().getNummer() == artikel.getNummer()) {
                w.setMenge(w.getMenge() + menge);
                return;
            }
        }
        warenkorb.addEintrag(new WarenkorbEintrag(artikel, menge));  // Eintrag in den Kunden-Warenkorb hinzufügen
    }

    public void ArtikelInWarenkorbLoeschen(Kunde kunde, int artikelNummer) throws BestandPasstNichtMitPackungsGroesseException, NichtGenuegendBestandException, ArtikelNichtGefundenException, WarenkorbLeerException {
        Warenkorb warenkorb = kunde.getWarenkorb();  // Kunden-spezifischen Warenkorb verwenden
        List<WarenkorbEintrag> eintraege = warenkorb.getEintraege();  // Warenkorb-Einträge des Kunden durchlaufen
        for (WarenkorbEintrag w : eintraege) {
            if (w.getArtikel().getNummer() == artikelNummer) {
                w.getArtikel().artikelBestandAktualisieren(w.getMenge()); // Bestand wiederherstellen
                eintraege.remove(w);
                return;
            }
        }
        throw new ArtikelNichtGefundenException(artikelNummer, " im Warenkorb");
    }

    public void WarenkorbLeeren(Kunde kunde) throws WarenkorbLeerException {
        Warenkorb warenkorb = kunde.getWarenkorb();  // Kunden-spezifischen Warenkorb verwenden
        List<WarenkorbEintrag> eintraege = warenkorb.getEintraege();  // Warenkorb-Einträge des Kunden durchlaufen
        if (eintraege.isEmpty()) {
            throw new WarenkorbLeerException(" Im WarenkorbLeeren()");
        }

        for (WarenkorbEintrag w : eintraege) {
            try {
                w.getArtikel().artikelBestandAktualisieren(w.getMenge()); // Bestand wiederherstellen
            } catch (NichtGenuegendBestandException | BestandPasstNichtMitPackungsGroesseException e) {
                throw new RuntimeException(e);
            }
        }
        eintraege.clear();  // Einträge des Kunden-Warenkorbs löschen
    }

    public double getTotalPreis(Kunde kunde) {
        Warenkorb warenkorb = kunde.getWarenkorb();  // Kunden-spezifischen Warenkorb verwenden
        double preis = 0;
        for (WarenkorbEintrag w : warenkorb.getEintraege()) {  // Warenkorb-Einträge des Kunden durchlaufen
            preis += w.getTotalPreis();
        }
        return preis;
    }

    public List<WarenkorbEintrag> WarenkorbAnzeigen(Kunde kunde) throws WarenkorbLeerException {
        Warenkorb warenkorb = kunde.getWarenkorb();  // Kunden-spezifischen Warenkorb verwenden
        List<WarenkorbEintrag> eintraege = warenkorb.getEintraege();  // Warenkorb-Einträge des Kunden durchlaufen
        if (eintraege.isEmpty()) {
            throw new WarenkorbLeerException("");
        }

        for (WarenkorbEintrag w : eintraege) {
            System.out.println(w);
        }
        System.out.println("Total preis: " + getTotalPreis(kunde));  // Gesamtpreis des Kunden-Warenkorbs anzeigen
        return eintraege;
    }

    public void artikelBestandAendern(Kunde kunde, Artikel artikel, int neueMenge) throws ArtikelNichtImWarenkorbGefunden, NichtGenuegendBestandException, BestandPasstNichtMitPackungsGroesseException {
        Warenkorb warenkorb = kunde.getWarenkorb(); //Kunden-spezifischen Warenkorb verwenden
        List<WarenkorbEintrag> eintraege = warenkorb.getEintraege(); // Warenkorb-Einträge des Kunden durchlaufen
        for (WarenkorbEintrag w : eintraege) {
            if (w.getArtikel().getNummer() == artikel.getNummer()) {
                int alteMenge = w.getMenge(); // Menge im Warenkorb, nicht im Artikelbestand

                if (neueMenge == 0) {
                    // Wenn neue Menge 0 ist, Artikel aus dem Warenkorb entfernen und Bestand wiederherstellen
                    artikel.artikelBestandAktualisieren(alteMenge);
                    eintraege.remove(w);
                } else {
                    // Bestandsänderung berechnen
                    int bestandAenderung = alteMenge - neueMenge;
                    artikel.artikelBestandAktualisieren(bestandAenderung);
                    w.setMenge(neueMenge);
                }
                return;
            }
        }
        throw new ArtikelNichtImWarenkorbGefunden("Artikel mit Nummer " + artikel.getNummer() + " nicht im Warenkorb gefunden.");
    }

    public void warenkorbAbschliessen(Kunde kunde) {
        Warenkorb warenkorb = kunde.getWarenkorb();  // Kunden-spezifischen Warenkorb verwenden
        warenkorb.getEintraege().clear(); // Warenkorb wird geleert, aber Bestand wird nicht geändert
    }
}
