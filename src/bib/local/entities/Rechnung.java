package bib.local.entities;

import bib.local.domain.WarenkorbVerwaltung;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Rechnung {
    private Kunde kunde;
    private double totalPreis;
    private LocalDate date;

    public Rechnung(Kunde kunde, double totalPreis) {
        this.kunde = kunde;
        this.totalPreis = totalPreis;
        this.date = LocalDate.now();
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    public double getTotalPreis() {
        return totalPreis;
    }

    public void setTotalPreis(double totalPreis) {
        this.totalPreis = totalPreis;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void printRechnung() {
        System.out.println("Datum: " + date);
        System.out.println("Gesamtpreis: " + totalPreis);
    }

}
