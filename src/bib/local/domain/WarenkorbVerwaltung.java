package bib.local.domain;

import bib.local.domain.exceptions.*;
import bib.local.entities.Artikel;
import bib.local.entities.Massengutartikel;
import bib.local.entities.Warenkorb;

import java.util.ArrayList;
import java.util.List;

public class WarenkorbVerwaltung {
    private List<Warenkorb> warenkorb;

    public WarenkorbVerwaltung() {
        this.warenkorb = new ArrayList<>();
    }

    public void ArtikelInWarenkorbLegen(Artikel artikel, int menge) throws BestandPasstNichtMitPackungsGroesseException, NichtGenuegendBestandException {
        //Bestand sofort aktulisieren
        artikel.artikelBestandAktualisieren(-menge);

        for (Warenkorb w : warenkorb) {
            if (w.getArtikel().getNummer() == artikel.getNummer()) {
                w.setMenge(w.getMenge() + menge);
                return;
            }
        }
        warenkorb.add(new Warenkorb(artikel, menge));
    }

    public void MassengutartikelInWarenkorblegen(Massengutartikel artikel, int menge) throws BestandPasstNichtMitPackungsGroesseException, NichtGenuegendBestandException {
        // Bestand sofort aktulisieren
        artikel.artikelBestandAktualisieren(-menge);

        for (Warenkorb w : warenkorb) {
            if (w.getArtikel().getNummer() == artikel.getNummer()) {
                w.setMenge(w.getMenge() + menge);
                return;
            }
        }
        warenkorb.add(new Warenkorb(artikel, menge));
    }

    public void ArtikelInWarenkorbLoeschen(int ArtikelNummer) throws BestandPasstNichtMitPackungsGroesseException, NichtGenuegendBestandException, ArtikelNichtGefundenException, WarenkorbLeerException {

        for (Warenkorb w : warenkorb) {
            if (w.getArtikel().getNummer() == ArtikelNummer) {
                try {
                    w.getArtikel().artikelBestandAktualisieren(w.getMenge()); // Bestand wiederherstellen
                } catch (NichtGenuegendBestandException | BestandPasstNichtMitPackungsGroesseException e) {
                    throw e; // Ausnahme weiterwerfen
                }
                warenkorb.remove(w);
                return;
            }
        }
        throw new ArtikelNichtGefundenException(ArtikelNummer, " in Warenkorb");
    }

    public void WarenkorbLeeren() throws WarenkorbLeerException {
        if (warenkorb.isEmpty()) {
            throw new WarenkorbLeerException(" In WarenkorbLeeren()");
        }

        for (Warenkorb w : warenkorb) {
            try {
                w.getArtikel().artikelBestandAktualisieren(w.getMenge()); // Bestand wiederherstellen
            } catch (NichtGenuegendBestandException | BestandPasstNichtMitPackungsGroesseException e) {
                throw new RuntimeException(e);
            }
        }
        warenkorb.clear();
    }

    public double getTotalPreis() {
        double preis = 0;
        for (Warenkorb w : warenkorb) {
            preis += w.getTotalPreis();
        }
        return preis;
    }

    public void WarenkorbAnzeigen() throws WarenkorbLeerException {
        if (warenkorb.isEmpty()) {
            throw new WarenkorbLeerException("");
        }

        for (Warenkorb w : warenkorb) {
            System.out.println(w);
        }
        System.out.println("Total preis: " + getTotalPreis());
    }

    public void artikelBestandAendern(Artikel artikel, int neueMenge) throws ArtikelNichtImWarenkorbGefunden {
        for (Warenkorb w : warenkorb) {
            if (w.getArtikel().getNummer() == artikel.getNummer()) {
                int alteMenge = w.getMenge(); // Menge im Warenkorb, nicht im Artikelbestand

                if (neueMenge == 0) {
                    // Wenn neue Menge 0 ist, Artikel aus dem Warenkorb entfernen und Bestand wiederherstellen
                    try {
                        artikel.artikelBestandAktualisieren(alteMenge);
                    } catch (NichtGenuegendBestandException | BestandPasstNichtMitPackungsGroesseException e) {
                        System.out.println("Fehler beim Aktualisieren des Bestands: " + e.getMessage());
                        return;
                    }
                    warenkorb.remove(w);
                } else {
                    // Bestandsänderung berechnen
                    int bestandAenderung = alteMenge - neueMenge;
                    try {
                        artikel.artikelBestandAktualisieren(bestandAenderung);
                    } catch (NichtGenuegendBestandException | BestandPasstNichtMitPackungsGroesseException e) {
                        System.out.println("Fehler beim Aktualisieren des Bestands: " + e.getMessage());
                        return;
                    }
                    w.setMenge(neueMenge);
                }
                return;
            }
        }
        throw new ArtikelNichtImWarenkorbGefunden("Artikel mit Nummer " + artikel.getNummer() + " nicht im Warenkorb gefunden.");
    }


    public void warenkorbAbschliessen(){
        warenkorb.clear(); // Warenkorb wird geleert, aber Bestand wird nicht geändert
    }

}
