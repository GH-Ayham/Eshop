package bib.local.entities;

public class Warenkorb {
    private Artikel artikel;
    private int menge;

    public Warenkorb(Artikel artikel, int menge) {
        this.artikel = artikel;
        this.menge = menge;
    }

    public Artikel getArtikel() {return artikel;}
    public int getMenge() {return menge;}
    public void setMenge(int menge) {this.menge = menge;}

    public double getTotalPreis(){
        return artikel.getPreis() * menge;
    }

    @Override
    public String toString() {
        return ("ArtikelNummer: " + artikel.getNummer() + "Artikel: " + artikel.getBezeichnung() + ", Menge: " + menge + ", TotalPreis: " + getTotalPreis());
    }
}
