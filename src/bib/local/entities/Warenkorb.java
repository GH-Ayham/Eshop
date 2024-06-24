package bib.local.entities;

import java.util.ArrayList;
import java.util.List;

public class Warenkorb {
    private List<WarenkorbEintrag> eintraege;

    public Warenkorb() {
        this.eintraege = new ArrayList<>();
    }

    public void addEintrag(WarenkorbEintrag eintrag) {
        this.eintraege.add(eintrag);
    }

    public List<WarenkorbEintrag> getEintraege() {return eintraege;}
}
