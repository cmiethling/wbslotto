/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.populatedb;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import de.wbstraining.lotto.persistence.dao.KundeFacadeLocal;
import de.wbstraining.lotto.persistence.model.Adresse;
import de.wbstraining.lotto.persistence.model.Bankverbindung;
import de.wbstraining.lotto.persistence.model.Kunde;

/**
 *
 * @author gz1
 */
@Stateless
public class PopulateKunde implements PopulateKundeLocal {

	// wir deklarieren eine abhängigkeit von KundeFacadeLocal
	@EJB
	private KundeFacadeLocal kundeFacadeLocal;

	// erzeugt einige kunden mit jeweils 1 adresse und 1 bankverbindung.
	// der aufrufer von populateKunde() ist populateDatabase(),
	// mit der folge, dass populateKunde() in der tx läuft, die von
	// populateDatabase()
	// gestartet wurde.

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void populateKunde() {
		// wir durchlaufen eine schleife 10 mal.
		// bei jedem schleifendurchlauf machen wir folgendes:
		// wir erzeugen jeweils eine instanz von Kunde, Adresse, und Bankverbindung
		// und rufen die passenden setter-methoden auf.
		// dann persistieren wir den kunden unter verwendung der eingespritzten
		// facade mit create().
		// da wir eine passende cascade-option haben, wird beim persistieren eines
		// kunden
		// auch seine adresse und seine bankverbindung persistiert.

		String[] vornamen = { "alfred", "ulla", "marilyn", "arthur", "berenice",
			"michail", "dulcinea", "ursula", "carolin", "krabat" };
		String[] namen = { "jarry", "hahn", "monroe", "miller", "dante", "bulgakow",
			"don quijote", "poznanski", "emcke", "kantorka" };
		Kunde kunde;
		Adresse adresse;
		Bankverbindung bankverbindung;
		LocalDateTime now = LocalDateTime.now();

		for (int i = 0; i < vornamen.length; i++) {

			kunde = new Kunde();
			kunde.setEmail("email_" + i);
			kunde.setName(namen[i]);
			kunde.setVorname(vornamen[i]);
			kunde.setDispo(BigInteger.ZERO);
			kunde.setGuthaben(BigInteger.ZERO);
			kunde.setIsannahmestelle(Boolean.FALSE);
			kunde.setGesperrt(null);
			kunde.setCreated(now);
			kunde.setLastmodified(now);

			kunde.setAdresseList(new ArrayList<Adresse>());
			kunde.setBankverbindungList(new ArrayList<Bankverbindung>());

			adresse = new Adresse();
			adresse.setLand("deutschland");
			adresse.setPlz("7000" + i);
			adresse.setOrt("ort_" + i);
			adresse.setStrasse("strasse_" + i);
			adresse.setHausnummer("12" + i);
			adresse.setAdresszusatz("");
			adresse.setAdressenr(1);
			adresse.setCreated(now);
			adresse.setLastmodified(now);

			adresse.setKundeid(kunde);
			kunde.getAdresseList()
				.add(adresse);

			bankverbindung = new Bankverbindung();
			bankverbindung.setBic("bic_" + i);
			bankverbindung.setIban("iban_" + i);
			bankverbindung.setName("name_" + i);
			bankverbindung.setBankverbindungnr(1);
			bankverbindung.setKontoinhaber("kontoinhaber_" + i);
			bankverbindung.setCreated(now);
			bankverbindung.setLastmodified(now);

			bankverbindung.setKundeid(kunde);
			kunde.getBankverbindungList()
				.add(bankverbindung);

			kundeFacadeLocal.create(kunde);
		}
	}
}
