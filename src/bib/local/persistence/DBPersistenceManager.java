package bib.local.persistence;

import java.io.IOException;

import bib.local.entities.Artikel;
import bib.local.entities.Kunde;
import bib.local.entities.Mitarbeiter;

public class DBPersistenceManager implements PersistenceManager {

	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Artikel ladeArtikel() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openForReading(String datenquelle) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void openForWriting(String datenquelle) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean speichereArtikel(Artikel artikel) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 *  Wenn später mal eine Kundenverwaltung ergänzt wird:
	*/

	@Override
	public Kunde ladeKunde() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean speichereKunde(Kunde k) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Mitarbeiter ladeMitarbeiter() throws IOException{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean speichereMitarbeiter(Mitarbeiter m) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
}
