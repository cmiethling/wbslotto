// Version 0.25 vom 12.3.2019, 11:50
// Status: alles gecodet, XML eingebaut, Logik geprüft; nächster Schritt: Fehlerquellen suchen und abfangen, Durchlauf mit Testdaten
// Todos: Prüfung, ob nicht schon DS zu Datum/..., Quoten flexibel oder fix, Jackpot letzte Ziehung: namedQuery über Ziehungsid/Ziehungsdatum (Übung: Bau namedQuery), LS durchgehen: nur die in der Ziehung
// Todo: LottoUtil.getGkl arbeitet mit Berechnung der Gkl (n-Anztreffer-IsSuperzahl) -> paßt bei einer Änderung der Schlüssel nicht mehr -> besser neu schreiben mittels Tabelle berechnen

package de.wbstraining.lotto.business.lottogesellschaft;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.wbstraining.lotto.cache.GebuehrenCacheLocal;
import de.wbstraining.lotto.persistence.dao.GewinnklasseFacadeLocal;
import de.wbstraining.lotto.persistence.dao.GewinnklasseziehungquoteFacadeLocal;
import de.wbstraining.lotto.persistence.dao.JackpotFacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinFacadeLocal;
import de.wbstraining.lotto.persistence.dao.Lottoscheinziehung6aus49FacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinziehungFacadeLocal;
import de.wbstraining.lotto.persistence.dao.SpielFacadeLocal;
import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Gebuehr;
import de.wbstraining.lotto.persistence.model.Gewinnklasse;
import de.wbstraining.lotto.persistence.model.Gewinnklasseziehungquote;
import de.wbstraining.lotto.persistence.model.Jackpot;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung6aus49;
import de.wbstraining.lotto.persistence.model.Spiel;
import de.wbstraining.lotto.persistence.model.Ziehung;
import de.wbstraining.lotto.util.ByteLongConverter;
import de.wbstraining.lotto.util.LottoUtil;

/**
 * Session Bean implementation class ZiehungAuswerten
 */

// Wir skizzieren den Prozeß zum Auswerten einer Ziehung:
// Aufwand: 2 - 5 Tage ( z.B schwierigere jpql-Queries dabei, u.U. 1 Tag Einarbeitung); Risiko: mittel bis hoch (Kenntnisse und Selbständigkeitsgrad!)
// Dozent:
// - Für jeden Tipp jedes LSs, der in 6Aus49-Gewinnklasse liegt: neuer Rec in LSZiehung6Aus49   //new 337 edit 610
// - für jeden LS mit GKl 77/s6: Update von LSZiehung (GKL77/s6, Gewinn77/S6 (redundant)       //edit  360 , 631
// - für jedes Spiel und jede GKL: Rec in GKLZiehungQuote anlegen          //new 643,653,663
// - in Ziehung aktualisieren: EinsatzLotto/77/s6, Status             //edit 372,670   
// - in Jackpot aktualisieren: ZiehungID, AnzahlZiehungen, GewinnklasseID, Betrag (in dieser Ziehung dazu),
//BetragKumuliert (Gesamtbetrag, kumuliert über mehrere Ziehungen)
//   Methoden: ZiehungAuswerten, init, lottoScheineVerarbeiten, jackpotsVerwalten, quotenBerechnen, abschluss

// gegeben: die gezogenen Zahlen per Servlet, wurden bereits in Tab eingetragen -> Daten holen
// benötigt: alle Scheine der Ziehung, zu Beginn in List -> Tab. Ziehung einspritzen -> macht es bei dieser Menge Sinn, für 2 Zugriffe in Cache zu holen?
// 			 nötige Tabellen per Inject holen
// Frage: Wann ist cache sinnvoll? Nicht bei nur 2 Zugriffen (Ziehung, Jackpot -> dauert nicht lang), 
//		  							nicht bei Mio von DS (Kunde, Lottoschein, LottoscheinZiehung,... -< riesige Datenmenge) ???

// Beteiligte Tabellen:
// - Ziehung: Ergebnis der Ziehnung am x.x.xxxx: gezogene Zahlen, Erlös pro Spielart -> interessiert nur der Rec mit Datum = "heute"
// - Gebuehr: Einsätze je Spiel, GültigVon/bis
// - Gewinnklasse: für jede Spielart Infos über Gewinnberechnung: Beschreibung, Betrag, isAbsolute, GültigVon/bis
// - Jackpot: je Ziehung und GKl: Betrag, BetragKumuliert, Ziehungsnr (Regel: ab 12. Z. in andere GKl) -> letzten DS lesen, neuen schreiben:
// - Lottoschein: abgegebener LS: Tipps..., WTag, Spielarten, Laufzeit, Kosten, Kunde, isAbgeschlossen
// - LSZiehung: ausgewerteter Schein: Schein, Ziehung, GKls, Gewinne pro Schein, isAbgeschlossen
// - LSZiehung6Aus49: ausgewerteter Schein je Tipp: LSZiehung, GKl, Gewinn
// - GKlZiehungQuote: je Ziehung und GKL: Anzahl Gewinner, Quote (= Einzelgewinn in GKL)
// -> Benachrichtigung des Kunden eigener UseCase? (Tab. Kunde)
//
// Vorgehen: allg: Erlös berechnen, die Gewinnverteilung berechnen (Jackpot alt berücksichtigen und neu schreiben), Einzelgewinne ermitteln, 
//			 LS durchgehen I: Erlös und GKl ermitteln: 
//				a) Kosten berechnen für Erlös (Erlös = Summe(Kosten pro Schein)), dazu 
//				b) Ziehungsergebnis mit Lottoschein abgleichen und in GKl eintragen => Anzahl Gewinner für Berechnung Einzelgewinn pro Spiel
//				c) Lottoscheine durchgehen und Gewinne eintragen
//
// Schritt 1: Berechnen des Erlöses: alle Scheine durch: Kosten ermitteln und summieren
//				hier bereits die Anzahlen in den GKl zählen, spart weiteren Durchlauf
//				-> Tab Ziehung(gezogene Zahlung, Erlös eintragen) , Lottoschein(getippte Zahlen...), Gebuehr(Kosten pro Soiel), 
//					   LottoscheinZiehung (pro Schein und Ziehung: Gewinnklasse eintragen), LSZiehung6Aus49 (ebenso)
//			  konkret: je LS: a) Schein auswerten: Kosten -> für Erlös, dazu auch Tab. Gebuehr (Datum!) -> Erlös+=
//							  b) Schein auswerten: Gewinnklasse je Spiel, GKl eintragen in LSZiehung und LSZiehung6Aus49 -> dazu 
//                                                                                  Gewinnklassen-Definition in Gewinnklasse beachten (berechnen!!!)
//												   Zählen der Gewinner je GKl
//							  c) am Ende: Eintrag Erlös in Ziehung (je Spielart), Eintrag GKlZiehungQuote: Anzahl der Gewinner je GKl
// Schritt 2: Ausschüttung berechnen: je Jackpot dazu, prüfen, ob GKl belegt oder in Jackpot -> Sonderregeln beachten; Jackput aktualisieren
//				hier auch die Gewinn pro Spielschein (bei 5 Richtigen gibts 123,45 €) berechnen
//				-> Tab Jackpot (letzter Eintrag holen), Gewinnklasse, GewinnklasseZiehung (wird sich da was ändern???)
//			  konkret: a) Erlös auf die verschiedenen GKLs verteilen
//					   b) Jackpot dazurechnen (Sonderregeln!)
//					   c) ggf. Jackpot neu berechnen und als neuer DS in Tab
//					   d) Berechnung der Einzelgewinne pro GKl
//					   e) Eintragen in DB: GKlZiehungQuote: Quote (?) = Einzelgewinn (Update, Rec existiert schon)
// Schritt 3: Gewinner ermitteln und benachrichtigen: für jeden Schein je nach GKl und dann den Gewinn berechnen
//				Benachrichtigungsemail erstellen und per Email senden (eigener Usecase?)
//				-> Tab Kunde, Bankverbindung
//					   LottoscheinZiehung (pro Schein und Ziehung: über Gewinnklasse Gewinn eintragen), LSZiehung6Aus49 (ebenso)
//				konkret: a) Scheine auswerten (nochmals): wo GKl eingetragen, dort den Gewinn pro Spielschein eintragen: LSZiehung / LSZiehung6Aus49
//						 b) Schein auswerten: gleichzeitig: Gesamtgewinn pro Schein aufs Konto des Kunden buchen
//						 c) Benachrichtigungs-Email an Kunden
//
// Methoden: ZiehungAuswerten: 			Hauptmethode
//			 ErlösBerechnen:  	 		Erlös ermitteln, GKl pro Schein eintragen
//			 AusschuettungBerechnen:	Erlös auf die GKls verteilen, Jackpot berücksichtigen, Einzelgewinne berechnen
//			 GewinnerErmitteln:			für die Scheine den jeweiligen Gewinn eintragen
//
// Was kann man per SQL-Update bzw -Abfrage erzeugen (ohne durchzählen zu müssen) ???
//			Erlös berechnen: Lottoschein.Kosten ist für alle Ziehungen des Scheins, nicht für die Einzelziehung. Die muß man berechenen.
//							 Da kein Feld dafür existiert, muß in Java der Gesamterlös berechnet werden
//			Gewinnklasse ermitteln muß man je Schein machen...
//			Anzahl Gewinner je GKL kann als count per SQL gewonnen werden
//			Ausschüttung berechnen ist eher DB-unabhängig, muß man mauell machen
//			Gewinn pro Schein eintragen: ist als Update-Anweisung möglich (where GKL = ...)
//			

@Stateless
@LocalBean
public class ZiehungAuswerten implements ZiehungAuswertenLocal {

// Felder:
    @PersistenceContext(unitName = "corejsfPU")
    private EntityManager em;

    // Tabellen-Injectionen
	@EJB private LottoscheinFacadeLocal lottoscheinFacadeLocal;
	@EJB private GewinnklasseFacadeLocal gewinnklasseFacadeLocal;
	@EJB private SpielFacadeLocal spielFacadeLocal;
	@EJB private LottoscheinziehungFacadeLocal lottoscheinziehungFacadeLocal;
	@EJB private Lottoscheinziehung6aus49FacadeLocal lottoscheinziehung6aus49FacadeLocal;
	@EJB private ZiehungFacadeLocal ziehungFacadeLocal;
	@EJB private GewinnklasseziehungquoteFacadeLocal gewinnklasseziehungquoteFacadeLocal;
	@EJB private JackpotFacadeLocal jackpotFacadeLocal;
	@EJB private GebuehrenCacheLocal gebuehrenCacheLocal;           //MM2

	// Spielnummern
	private Spiel spiel6aus49;
	private Spiel spielSpiel77;
	private Spiel spielSuper6;
	
	//absolute anzahlen
	private int anzahlScheine = 0;	// Anzahl aller Scheine
	private int anzahl6Aus49 =0;	// = Anzahl Tipps 6Aus49 ???
	private int anzahlSpiel77 = 0;
	private int anzahlSuper6 = 0;
	
	// absolute Einnahmen   CENT
	private long einnahmen6Aus49 = 0;
	private long einnahmenSpiel77 = 0;
	private long einnahmenSuper6 = 0;
	
	//absolute Häufigkeiten in den Gewinnklassen der Spiele
	// Index ist die Gewinnklasse (0 = Pseudogewinnklasse ohne Ausschüttung)
	private int[] gkl6aus49 = new int[11];
	private int[] gklSpiel77 = new int[8];
	private int[] gklSuper6 = new int[7];
	
	// aktuelle GewinnklassenID
	private Gewinnklasse[] aktGkl6aus49 = new Gewinnklasse[11];
	private Gewinnklasse[] aktGklSpiel77 = new Gewinnklasse[8];
	private Gewinnklasse[] aktGklSuper6 = new Gewinnklasse[7];
	
	private Jackpot[] jackpotAlt = new Jackpot[11];
	
	private String iniDatei = "../standalone/deployments/wbslotto.war/resources/LottoINI.xml";	// unterverz. von windfly
	
	private int ausschuettungsquote;	// zZ 50% -> nicht fix, sondern aus XML-Datei (wildfly\bin\resources.LottoIni.Xml !!!)
	private int ausschuettungsquoteSpiel77;  //MM2   aus XML datei
	//private double ausschuettungGkl1Spiel6Aus49;	// zZ. 12.8 
	private BigInteger[] verteilungsSchluessel6Aus49 = new BigInteger[11];
	private long ausschuettungGkl9Spiel6Aus49;	// zZ. fix 5,00 €
	
	private long ausschuettung6Aus49Gesamt;					// Gesamteinnahme 6 aus 49, verrechnet mit der Ausschüttungsquote
	private long ausschuettung6Aus49OhneGkl1;				// Ausschüttung6Aus49Gesamt, vermindert um Anteil für GKl1
	private long ausschuettung6Aus49OhneGkl1UndOhneGkl9;	// ausschuettung6Aus49OhneGkl1 vermindert um den Anteil für GKl9, = 100% für GKl 2-8
	
	// Gesamteinnahmen in den GKls in 6Aus49 -> Einnahme hier im Sinn von Ausschüttung über Einnahmern ( ohne Jackpot)?
	//private long[] gesamtEinnahmeGkl6Aus49;		// ??? ist Doppelung zu einnahmen6Aus49

	// gesamteinnahmen in den gewinnklassen im spiel 6 aus 49, mit kumuliertem jackpot
	//private long[] quoten6Aus49;	// wohl die Quote in Prozent bzw, fixer Wert
	private long[] quotenSpiel77 = new long[8];
	private long[] quotenSuper6 = new long[7];

	private long[] ausschuettungSumme6aus49 = new long[11]; // ohne Gkl 1+9	// Summe, die ausbezahlt wird in Cent (Gesamtsumme für alle)
	private long[] ausschuettungSummeSpiel77 = new long[8];
	private long[] ausschuettungSummeSuper6 = new long[7];

	private long[] ausschuettung6aus49 = new long[11]; 	// ohne Gkl 1+9		// Summe, die ausbezahlt wird in Cent (Einzelsumme pro Tip)
	private long[] ausschuettungSpiel77 = new long[8];	// = quotenSpiel77
	private long[] ausschuettungSuper6 = new long[7];		// = quotenSuper6


// Methoden
	// Hauptmethode
	public void ziehungAuswerten(Ziehung ziehung) {
		init(ziehung);
		lottoScheineVerarbeiten(ziehung);
		jackpotsVerwalten(ziehung);
		quotenBerechnen(ziehung);
		abschluss(ziehung);
		//ausgabe(ziehung);
	}
	
	protected void init(Ziehung ziehung) {
		// folgende parameter einlesen: -> aus Tabelle oder Datei; hier erst mal fix
		// ausschuettungsQuote = 0.50 = 50 %
		// ausschuettungGkl1Spiel6Aus49 = 0.128 = 12,8 %
		// verteilungsSchluesselAus49[]	-> in 10000stel!!!
		// AusschüttungWert GKl9 später, aber besser hier als fixer Wert, als Quote

		// Spielnummern: Welche ID ist welches Spiel, für Gewinnklasse
		List<Spiel> lSpiel = spielFacadeLocal.findAll();
		for (Spiel sp : lSpiel) {
			if (sp.getName().equals("6 aus 49")) spiel6aus49 = sp;
			if (sp.getName().equals("Spiel 77")) spielSpiel77 = sp;
			if (sp.getName().equals("Super 6") )spielSuper6 = sp;
		}
		if (spiel6aus49 == null) throw new RuntimeException("Spiel 6 aus 49 nicht vorhanden");
		if (spielSpiel77 == null) throw new RuntimeException("Spiel 77 nicht vorhanden");
		if (spielSuper6 == null) throw new RuntimeException("Spiel Super 6 nicht vorhanden");
		
		// die für diesen Tag gültigen Quoten (fix oder flexibel) einlesen -> SpielID, Datum(GueltigVon/Bis) => Gewinnklasse -> Wert
		// Quoten sind als BigInteger hinterlegt, was Probleme beim Umrechnen bereitet ( da wird in 10Tausenstel gerechnet...)
		// TODO: Auswerten von IsAbsolut, Prüfen ob alle Werte besetzt, Prüfen ob eine gkl doppelt in Tab
		List<Gewinnklasse> gkList = gewinnklasseFacadeLocal.findAll();
		// 6aus49 -> was ist der Unterschied zwischen verteilungsSchlüssel6Aus49 und quoten6Aus49 ???
		for (int i = 1; i < 9; i++) {	// für GKl 2-8, 1 und 9 sind fix -> aber: Liste nicht nach Gkl sortiert -> hier: sortiert durch Zuweisung von[...]
			for (Gewinnklasse gk : gkList) {
				if (gk.getSpielid().equals(spiel6aus49) && gk.getGueltigab().compareTo(ziehung.getZiehungsdatum()) <= 0 
						&& gk.getGueltigbis().compareTo(ziehung.getZiehungsdatum()) >= 0) {
					verteilungsSchluessel6Aus49[gk.getGewinnklassenr()] = gk.getBetrag();
					aktGkl6aus49[gk.getGewinnklassenr()] = gk;
				}
			}
		
		}
		// Ist NICHT in einer Tabelle hinterlegt !!!, vom Kollegen in XML gepackt (Ordner von Wildfly!!!) : wildfly\bin\resources\lottoini.xml
		ausschuettungsquote  = propertiesLesen(iniDatei)[0];  //MM2
		ausschuettungsquoteSpiel77=propertiesLesen(iniDatei)[1]; //MM2
		
		//ausschuettungGkl1Spiel6Aus49 = 1280;	// in Tabelle, aber als BigInteger -> double... 
		ausschuettungGkl9Spiel6Aus49 = aktGkl6aus49[9].getBetrag().longValue();	 // Quote ist Fixbetrag
		
		// Spiel77: Gkl 1-7 -> sortiert
		for (int i = 1; i < 8; i++) {	// für GKl 1-7
			for (Gewinnklasse gk : gkList) {
				if (gk.getSpielid().equals(spielSpiel77) && gk.getGueltigab().compareTo(ziehung.getZiehungsdatum()) <= 0
						&& gk.getGueltigbis().compareTo(ziehung.getZiehungsdatum()) >= 0) {
					quotenSpiel77[gk.getGewinnklassenr()] = gk.getBetrag().longValue();	 // Quote ist Fixbetrag
					aktGklSpiel77[gk.getGewinnklassenr()] = gk;
				}
			}
		}

		// Super6: Gkl 1-6 -> sortiert
		for (int i = 1; i < 7; i++) {	// für GKl 1-6
			for (Gewinnklasse gk : gkList) {
				if (gk.getSpielid().equals(spielSuper6) && gk.getGueltigab().compareTo(ziehung.getZiehungsdatum()) <= 0 
						&& gk.getGueltigbis().compareTo(ziehung.getZiehungsdatum()) >= 0) {
					quotenSuper6[gk.getGewinnklassenr()] = gk.getBetrag().longValue();	 // Quote ist Fixbetrag
					aktGklSuper6[gk.getGewinnklassenr()] = gk;
				}
			}
		}
		
	} // end init

	public void lottoScheineVerarbeiten(Ziehung ziehung) {
		// Code mit Ls per Query und Aufteilung in Häppchen; 
		// Dozent empfahl Methode per Holen der Listen per Methode (von Ziehung zu Lottoscheinziehung zu Lottosschein)
		// 1. Jeden LS auswerten nach Erlös pro Spielart und nach Gewinnklasse
		//    Das muß wegen der Menge in Teilen zu je 100.000 DS passieren
//		int schrittweitse = 100_000;	// Anzahl Lottoscheine pro Auswertungsdurchgang 
//		Query qAlleLottoscheine;
//	    List<Lottoschein> lottoscheinliste;
//	    long anzGes;
//	    
//	    qAlleLottoscheine = em.createNamedQuery("Lottoschein.findAll");
//		anzGes = lottoscheinFacadeLocal.count();
//		
//		 for (int i = 0; i<= anzGes; i+=2) {
//			 qAlleLottoscheine.setFirstResult(i);
//			 qAlleLottoscheine.setMaxResults(2);
//			 lottoscheinliste = qAlleLottoscheine.getResultList();
//			 for (Lottoschein ls :lottoscheinliste) {
//				 // Hier jeden einzelnen Lottoschein auswerten: Berechne Erlös, Erhöhe Anzahl Spieler, Berechne Gewinnklasse
//				 // a) Erlös PRO SPIELART berechnen
//				 
//				 
//			 }
//		 }

// Code nach Empfehlung des Dozenten: Holen der Listen per Methode (von Ziehung zu Lottoscheinziehung zu Lottosschein)
// damit aber keine Häppchen-Verarbeitung!
// Aufgaben: alle Lottoscheine duchgehen (Anzahlen Spiele, Einnahmen, GKl je Schein, GKl speichern in LSZiehung/4aus49, Ausschüttungen berechnen
	// TODO: nur die in aktueller Ziehung (verringert Anzahl beträchtlich!)
		
		Lottoschein lottoschein;
		List<Lottoscheinziehung> lZiehung = ziehung.getLottoscheinziehungList();	// die Liste aller (ausgewerteten) Lottoscheine
		Date heute = new Date();
		
		// für alle Lottoscheine der Ziehung: Anzahl von gespielt, Einnahmen, Feststellung der Gewinnklassen der Spiele
		for (Lottoscheinziehung lz : lZiehung) {
			// Lottoschein holen und auswerten
			lottoschein = lz.getLottoscheinid();
			//absolute Anzahlen; Voraussetzung: mindestens 1 6Aus49 pro Schein, kein S77/S6 ohne 6Aus49 (sonst: mit if length == 0 prüfen)
			long[] tippsAsLong = ByteLongConverter.byteToLong(lottoschein.getTipps());
			anzahl6Aus49 +=  tippsAsLong.length;
			if (lottoschein.getIsspiel77() == true) anzahlSpiel77++;	// unnötig, da keine von Spielanzahl abhängige Quote
			if (lottoschein.getIssuper6() == true) anzahlSuper6++;		// unnötig, da keine von Spielanzahl abhängige Quote
			anzahlScheine++;
			// absolute Einnahmen in Cent !!!
			Gebuehr gebuehr = gebuehrenCacheLocal.gebuehrByDatum(LocalDate.of(2019, 9, 29));           //MM2
			einnahmen6Aus49  += gebuehr.getEinsatzprotipp() * tippsAsLong.length;         //MM2 
			if (lottoschein.getIsspiel77() == true) einnahmenSpiel77 += gebuehr.getEinsatzspiel77();    //MM1
			if (lottoschein.getIssuper6() == true) einnahmenSuper6  += gebuehr.getEinsatzsuper6();   //MM2
			
			// Wenn wir den Lottoschein schon dahaben, dann auch gleich die Gewinnklassen feststellen -> erst später wenn Einzelgewinne
			//bfeststeheh wegen Gewinn eintragen
			// Dabei die GKls in LSZiehung/..6Aus49 persistieren. (bei Pack erst in Speicher/List, dann vor nächstem Pack persistieren
			// Werte dabei eintragen in Entity (im Ram), am Ende, nach allen DS, die Daten per update/create persistieren in die DB per
			//modified = heute oder sofort nach Erstelleng
			// Rück der LottoUtil-Methoden: GKL oder 0 wenn kein Gewinn -> bei uns [0] = die ohne Gewinn, paßt...
			// bei 6aus49 alle bis zu 12 Einzeltipps auf dem Schein durchlaufen!
			// anzahl6Aus49 ist falsche Variable (Anzahl spiel49 aus ALLEN Scheinen), 
			//    nötig ist hier Anzahl pro diesem Schein, nicht als Var. gespeichert!, über Lottoschein.Tipps berechnen (= byte[] aus 
			//long[] mit den Tipps als long)
			int gkl;
			int AnzTippsAufSchein = tippsAsLong.length;
			for (int tipnr = 0; tipnr < AnzTippsAufSchein; tipnr++) {
				gkl = LottoUtil.gkl6Aus49(ziehung.getZahlenalsbits().longValue(), tippsAsLong[tipnr], 
						ziehung.getSuperzahl() == (lottoschein.getLosnummer() % 10));
				gkl6aus49[gkl] = gkl6aus49[gkl] + 1; 
				for (int i = 1; i < 10; i++) {		// kein for [gkl gkl : aktGkl6aus49, da in aktGkl6aus49 auch[0] und [10] drin, die null
						//Gewinnklasse gk : aktGkl6aus49) {
					if (aktGkl6aus49[i].getGewinnklassenr() == gkl) {
						// hier neuen DS in Lottoscheinziehung6aus49 erstellen und dann gleich persistieren (besser: am Ende gesamt per modified = heute)
						Lottoscheinziehung6aus49 neueLz = new Lottoscheinziehung6aus49();
						neueLz.setLottoscheinziehungid(lz);
						neueLz.setTippnr(tipnr);
						neueLz.setGewinnklasseid(aktGkl6aus49[i]);
						neueLz.setCreated(heute);
						neueLz.setLastmodified(heute);	// Version und Gewinn per default = 0
					
						lottoscheinziehung6aus49FacadeLocal.create(neueLz);         //MM9
					}
				}
			}
			
			gkl = LottoUtil.gklSpiel77(ziehung.getSpiel77(), lottoschein.getLosnummer());	// GKl des Scheins feststellen
			gklSpiel77[gkl]	= gklSpiel77[gkl] + 1;
			// GKl eintragen: von int (von LottoUtil geliefert, Gkl VIII) zu Gewinnklasse (mit gkl.gewinnklassenr = int) -> die ganze
			//Tab einlesen und durchlaufen
			// -> es reicht, die aktGklSpiel77 zu durchlaufen, nicht die ganze Tabelle, dann spart man sich die Abfrage nach Datum und Spielart
			for (int i = 1; i < 8; i++) {		// kein for [gkl gkl : aktGkl6aus49, da in aktGkl6aus49 auch[0] und [1] drin, die null
				if (aktGklSpiel77[i].getGewinnklassenr() == gkl ) {
					lz.setGewinnklasseidspiel77(aktGklSpiel77[i]);
				}
			}
			
			gkl = LottoUtil.gklSuper6(ziehung.getSuper6(), lottoschein.getLosnummer());
			gklSuper6[gkl] = gklSuper6[gkl] + 1;
			for (int i = 1; i < 7; i++) {		// kein for [gkl gkl : aktGkl6aus49, da in aktGkl6aus49 auch[0] und [1] drin, die null
				if (aktGklSuper6[i].getGewinnklassenr() == gkl) {
					lz.setGewinnklasseidsuper6(aktGklSuper6[i]);					
				}
			}
			lottoscheinziehungFacadeLocal.edit(lz); 

		} // end Lottoscheinziehung lz : lZiehung

		// Jetzt sind alle Lottoscheine bearbeitet; es stehen die Gesamtstückzahlen fest: persistieren (NUR Einnahmen je Art, 
		//NICHT Anzahl Scheine gesamt, NICHT Anzahl Spieler je Spielart!!!)
		// Tab. Ziehung: einsatzxxx in Cent, GKls in Gewinnklasseziehungquote später! (je 1 Eintrag pro GKl/Spielart aus Gewinnklasse),
		//Scheinanzahl wird nicht persistiert
		ziehung.setEinsatzlotto(BigInteger.valueOf(einnahmen6Aus49));
		ziehung.setEinsatzspiel77(BigInteger.valueOf(einnahmenSpiel77));
		ziehung.setEinsatzsuper6(BigInteger.valueOf(einnahmenSuper6));
		ziehung.setLastmodified(heute);
		ziehungFacadeLocal.edit(ziehung);
		
		// Ausschüttung ermitteln (da kommen danach noch die Jackpots dazu, vorerst nur das, was sich aus den Einnahmen ergibt)
		// Gesamterlös * 50%	Werte als Long / Cent, was passiert bei Änderung der Quote, erzeugend Cent-Bruchteile mit den Bruckteilen? Kaufmänn. Rundung?
		ausschuettung6Aus49Gesamt = einnahmen6Aus49 * ausschuettungsquote / 100 ;	//MM1 10000
		long ausschuettungGkl1 = (long)(ausschuettung6Aus49Gesamt * verteilungsSchluessel6Aus49[1].longValue()/10000); 		//MM2	// Ausschüttung6Aus49Gesamt, vermindert um Anteil für GKl1
		// ausschuettung6Aus49OhneGkl1 vermindert um den Anteil für GKl9, = 100% für GKl 2-8
		ausschuettung6Aus49OhneGkl1 = ausschuettung6Aus49Gesamt - ausschuettungGkl1 ;	
		// [9] ist GKl9 mit 5 € je Gewinn TODO: 5€ aus Tabelle statt fix
		ausschuettung6Aus49OhneGkl1UndOhneGkl9 = ausschuettung6Aus49OhneGkl1 - verteilungsSchluessel6Aus49[9].longValue() * gkl6aus49[9];  //MM2	
		// 6Aus49: Ausschüttung der GKl 2-8 gemäß Quote aus (dort in 10000stel !?!), ist Gesamtuasschüttung in der Gkl,
		//wird dann (um Jackpot ergänzt) aufgeteilt unter die Spieler
		for (int i=2; i<9; i++) {
			ausschuettungSumme6aus49[i] = verteilungsSchluessel6Aus49[i].longValue() *ausschuettung6Aus49OhneGkl1UndOhneGkl9 /10000;
		}
		ausschuettungSumme6aus49[1] = ausschuettungGkl1;
		ausschuettungSumme6aus49[9] = ausschuettungGkl9Spiel6Aus49 * gkl6aus49[9];
		// Spiel 77: fixe Quoten -> Todo: Abfrage ob Fixwert oder Quote
		// Gkl1: 50% der Einnahmen -Ausschüttung Gkl2-7, den Wert / AnzGewinnerin Gkl1
		long ausschGkl1 = einnahmenSpiel77 * ausschuettungsquoteSpiel77/100;	// 50 %   //MM2
		for (int i=2; i<8; i++) {
			ausschuettungSummeSpiel77[i] = quotenSpiel77[i] * gklSpiel77[i];
			ausschuettungSpiel77[i] = quotenSpiel77[i];
			ausschGkl1 = ausschGkl1 - ausschuettungSummeSpiel77[i];
		}
		ausschuettungSummeSpiel77[1] = ausschGkl1;
		if (gklSpiel77[1] != 0) ausschuettungSpiel77[1] = ausschGkl1 / gklSpiel77[1];
		else ausschuettungSpiel77[1] = ausschGkl1;
		quotenSpiel77[1] = ausschuettungSpiel77[1];
		// Super6: fixe Quoten -> Todo: Abfrage ob Fixwert oder Quote
		for (int i=1; i<7; i++) {
			ausschuettungSummeSuper6[i] = quotenSuper6[i] * gklSuper6[i];
			ausschuettungSuper6[i] = quotenSuper6[i];
		}  
		
	}	// end lottoScheineVerarbeiten

	protected void jackpotsVerwalten(Ziehung ziehung) {
		// 1. Jackpots holen
		// 2. Sonderregel Gkl2: wenn vorhanden, in Gkl1
		// 3. Sonderregel Gkl1: wenn 12 mal unbesetzt in nächste besetzte Gkl
		// 4. Gkl 3-9: Prüfen ob GKl vorhanden: ja: Jackpot dazu, nein: Summe zu Jackpot
		// 5. Ebenso Spiel 77: 1 Jackpot, wenn Gkl unbesetzt -> in Jackpot, der Gkl 1 zurechnen (in selber Ziehung?)
		// 6. Ebenso Super6: kein Jackpot, wenn unbesetzt, verfällt Gewinn (an Lottogesellschaft)
		// 7. Persistieren der neuen Jackpots
		
		// gesamtEinnahmeGkl6Aus49JackpotKumuliert initialisieren (mit gesamtEinnahmeGkl6Aus49)
		
		// gewinnklasse I besetzt:
		// => gesamtEinnahmeGkl6Aus49JackpotKumuliert[1] erhöhen (mit jackpot gkl1)
			// gewinnklasse II nicht besetzt
			// => gesamtEinnahmeGkl6Aus49JackpotKumuliert erhöhen (mit betrag gkl II)
		
		// evtl. vorhandene jackpots zu aktuell besetzten gewinnklassen addieren
		
		// zu aktuell nicht besetzten gewinnklassen jackpot kumulieren, oder neuen jackpot bilden
		
		// spezialregel gkl1 / 12 ziehungen unbesetzt berücksichtigen 
		
		// => create / update jackpot
		
		// Jackpots holen:
		// gegeben: Jackpot zu Ziehung (Ziehung DAVOR und Gkl) und Gkl, 
		//			davon Betrag (in dieser Ziehung dazu) und BetragKumuliert (akt. Summe des Jackpots), weiters AnzahlZiehungen (Sonderregel Gkl1)
		
		// letzte Ziehung (Mi ODER Sa): Tab. Ziehung hat nur Ziehungsdatum; keine Infos hinterlegt über letzte Ziehung (bei Jackpot wird nicht
		//nach Wochentag unterschieden!); 
		// gesucht: ID der Ziehung davor: höchstes Ziehungsdatum, das kleiner als akteulles Ziehungsdatum (per Query) -> Jackpot zu der Ziehung
		// Weg: alle Ziehungen in List, durchsuchen ( bei 100 DS/Jahr von Anzahl her von Aufwand gering)
		long[] neuerJackpot = new long[11];	// speichert die neuen Jackpot-Werte		  -> Spalte BetragKumuliert
		long[] jackpotDazu = new long[11];	// was in dieser Ziehung beim Jackpot dazukam -> Spalte Betrag
		Date heute = new Date();
		Ziehung ziehungDavor = null;
		Date aktDatum = ziehung.getZiehungsdatum();
		
		List<Ziehung> zlist = ziehungFacadeLocal.findAll();
		for (Ziehung z : zlist) {
			if (z.getZiehungsdatum().before(aktDatum)) {
				if (ziehungDavor == null || z.getZiehungsdatum().after(ziehungDavor.getZiehungsdatum())) {
					ziehungDavor = z;
				}
			}
		}
		
		// Jackpots der letzten Ziehung (für jede Gkl ein eigener Eintrag!):
		List<Jackpot> lJackpot = jackpotFacadeLocal.findAll(); // -> keine namedQuery über Ziehungsid/Ziehungsdatum = alle einzeln durchgehen
		for (Jackpot jp : lJackpot) {
			if (ziehungDavor.equals(jp.getZiehungid())) {
				jackpotAlt[jp.getGewinnklasseid().getGewinnklasseid().intValue()] = jp;  //MM1
			}
		}
		
		// Sonderregel Gkl2: wenn unbesetzt, zu Gkl1
		// Ausschüttung ist hier Gesamtausschüttung pro Gkl, nicht Einzelgewinn)  
		if (gkl6aus49[2] == 0) {
			if ( gkl6aus49[1] == 0) {
				// weder 1 noch 2 besetzt -> in Jackpot
				if (jackpotAlt[2] != null)                 //MM2
				neuerJackpot[2] = jackpotAlt[2].getBetragkumuliert() + ausschuettungSumme6aus49[2];
				else neuerJackpot[2] =  ausschuettungSumme6aus49[2];
				jackpotDazu[2] = jackpotDazu[2] + ausschuettungSumme6aus49[2];
				ausschuettungSumme6aus49[2] = 0;	// bereits ausgeschüttet in Jackpot2
			} else {
				// 2 unbesetzt, 1 besetzt -> zu 1 addieren
			//MM2	jackpotAlt[1].setBetragkumuliert(jackpotAlt[1].getBetragkumuliert() + ausschuettungSumme6aus49[2]);
				ausschuettungSumme6aus49[1]+= ausschuettungSumme6aus49[2];   //MM2
				ausschuettungSumme6aus49[2] = 0;	// bereits ausgeschüttet in Gkl1
			}
		}
	
		// Sonderregel Gkl1: wenn 12. mal unbesetzt, dann in nächste besetzte Gkl
		if (gkl6aus49[1] == 0) {
			int anzahlziehungen = jackpotAlt[1]==null ? 0 : jackpotAlt[1].getAnzahlziehungen();   //MM1
			//jackpotAlt[1].setAnzahlziehungen(anzahlziehungen + 1);	// unbesetzte Ziehungen hochzählen //MM1
			  if ( anzahlziehungen +1>= 12) {	// > falls beim 2. Mal keine GKl besetzt  //MM1 
				// in nächste freie Gkl
				for (int i = 2; i < 9; i++) {
					if (gkl6aus49[i] != 0) {
						// belegt: dazu, nicht weiter
						//jackpotAlt[i].setBetragkumuliert(jackpotAlt[i].getBetragkumuliert() + 
							//	ausschuettungSumme6aus49[1]+jackpotAlt[1].getBetragkumuliert());                //MM2
						
						ausschuettungSumme6aus49[i] += ausschuettungSumme6aus49[1]+jackpotAlt[1].getBetragkumuliert();  //MM3
						ausschuettungSumme6aus49[1] = 0;	// bereits ausgeschüttet in Gkl1
						//jackpotAlt[1].setBetragkumuliert(0);               //MM2
						jackpotDazu[1]=0;
						break;
					}	// else: diese Gkl ist nicht belegt, dann den Jackpot in die nächste Gkl stopfen
				}
				// kommt man bis hierher, heißt das, daß keine Gewinnklasse besetzt ist (außer vielleicht Gkl9, die hier außer Konkurrenz läuft)
				// dann würde ich sagen, daß wir die Summe doch wieder in den Jackpot setzen
//				jackpotAlt[1].setBetragkumuliert(jackpotAlt[1].getBetragkumuliert() + ausschuettungSumme6aus49[1]);
//				jackpotDazu[1] = jackpotDazu[1] + ausschuettungSumme6aus49[1];
//				ausschuettungSumme6aus49[1] = 0;	// bereits ausgeschüttet in Jackpot1
			}
			// AnzZiehungen < 12: normal in Pool
			  //MM1
			  if (jackpotAlt[1] != null && anzahlziehungen<11 )               //MM1
			neuerJackpot[1]= jackpotAlt[1].getBetragkumuliert() + ausschuettungSumme6aus49[1];
			  else 			neuerJackpot[1]= ausschuettungSumme6aus49[1];

			jackpotDazu[1] = jackpotDazu[1] + ausschuettungSumme6aus49[1];
			ausschuettungSumme6aus49[1] = 0;	// bereits ausgeschüttet in Jackpot1
		}
		
		// Jetzt alle Gkls von 3 bis 9 prüfen, ob besetzt oder Jackpot (kein Jackpot in Gkl 9, da dort fixe Ausspielung)
		
		for (int i = 3; i < 9; i++) {
			if (gkl6aus49[i] == 0) {
				// unbelegt -> in den Pool
				

				if (jackpotAlt[i] != null)
				neuerJackpot[i] = jackpotAlt[i].getBetragkumuliert() + ausschuettungSumme6aus49[i];
				else neuerJackpot[i] =  ausschuettungSumme6aus49[i];
				jackpotDazu[i] = jackpotDazu[i] + ausschuettungSumme6aus49[i];
				ausschuettungSumme6aus49[i] = 0;	// bereits ausgeschüttet in Jackpoti
			}
		}
	
		// Spiel77: wenn eine Gkl nicht besetzt -> in Gkl1 (Wenn Gkl1??? kein Datenbankfeld für diesen pool -> Gkl10 verwenden)
		long pool77 = 0;   //MM1
	/*	for (int i = 2; i < 8; i++) {         //MM1  
			if (gklSpiel77[i] == 0) {
				// unbelegt -> in den Pool
				// TODO: ist das die Gesamt- oder Einzelsumme???
				pool77 = pool77 + ausschuettungSummeSpiel77[i];
				ausschuettungSummeSpiel77[i] = 0;	// bereits ausgeschüttet in Jackpot
			}
		} */
		if (gklSpiel77[1] == 0) {
			// Gkl1 leer, somit in den Pool (als Gkl10 gespeichert])
			if (jackpotAlt[10] != null)                 //MM1
			neuerJackpot[10] = ausschuettungSummeSpiel77[1]+jackpotAlt[10].getBetragkumuliert();            //MM1
			else neuerJackpot[10] = ausschuettungSummeSpiel77[1];    //MM1			
			pool77 = ausschuettungSummeSpiel77[1];
		} else {
			// Gkl 1 vorhanden, dort rein
			ausschuettungSummeSpiel77[1] = ausschuettungSummeSpiel77[1] + pool77;   
		//	pool77 = 0;
		}
		
		// kein Jackpot bei Spiel Super6
		
		// Persistierung von Jackpot
		// Gkls 1-8
		for (int i = 1; i < 9; i++) {
			Jackpot jackpot = new Jackpot();
			jackpot.setZiehungid(ziehung);
			jackpot.setGewinnklasseid(aktGkl6aus49[i]);
			//jackpot.setAnzahlziehungen(jackpotAlt[1].getAnzahlziehungen());		//?????????????????????? Das geht nicht! warum???
			//MM1
			if (jackpotAlt[i] != null && jackpotAlt[i].getBetrag()>0 && jackpotDazu[i]>0) jackpot.setAnzahlziehungen(jackpotAlt[i].getAnzahlziehungen()+1);   //MM1
			else if (jackpotDazu[i]>0) jackpot.setAnzahlziehungen(1);
			else jackpot.setAnzahlziehungen(0);
			jackpot.setBetrag(jackpotDazu[i]);
			jackpot.setBetragkumuliert(neuerJackpot[i]);
			jackpot.setCreated(heute);
			jackpot.setLastmodified(heute);
			
		
			jackpotFacadeLocal.create(jackpot);  //MM9
		}
		// jackpot77 -> strukturelles Problem der Speicherung !!! -> erst klären bzgl. Speicherung von Spiel77-Jackot
		Jackpot jackpot = new Jackpot();
		jackpot.setZiehungid(ziehung);
		jackpot.setGewinnklasseid(aktGklSpiel77[1]);		// !!! die wurde nicht erzeugt -> strukturelles Problem der Speicherung
		//MM1
		if (jackpotAlt[10] != null && jackpotAlt[10].getBetrag()>0 && pool77>0) jackpot.setAnzahlziehungen(jackpotAlt[10].getAnzahlziehungen()+1);   //MM1
		else if (pool77>0) jackpot.setAnzahlziehungen(1);
		else jackpot.setAnzahlziehungen(0);
		jackpot.setBetrag(pool77);   //MM1
		jackpot.setBetragkumuliert(neuerJackpot[10]);
		jackpot.setCreated(heute);
		jackpot.setLastmodified(heute);
		 jackpotFacadeLocal.create(jackpot);   //MM9
	}	// end jackpotsVerwalten

	protected void quotenBerechnen(Ziehung ziehung) {
		// Ermitteln der Quoten, d.h. des Gewinns pro Tip, = Ausspielung geteilt durch Gewinner (je Gkl)
		//		Prüfung, ob Gkl besetzt ist (sonst Division durch 0)
		// Update von GewinnklasseZiehungQuote (Quoten)
		
		// aus gesamtEinnahmeGkl6Aus49JackpotKumuliert[] und den gkl6Au49[] kann quoten6Aus49[] (bei mir: ausschuettung6aus49) berechnet werden
		// quoten in spiel77 und spiel super 6 sind fix. -> keine Quotenberechnung, aber Übernahme des fixen Werte
		// => create GewinnklasseZiehungQuote (bei mir: Update, da Gkl bereits zuvor persistiert

		// danach Gewinn je Schein ermitteln = Klasse GewinnErmitteln
		
		Date heute = new Date();
		// 6aus49
		for (int i = 1; i < 9; i++) {
			if (gkl6aus49[i] != 0) {	// wenn belegt, Vermeidung Teilung durch 0
				if (jackpotAlt[i] != null)        //MM2
				ausschuettung6aus49[i] = (ausschuettungSumme6aus49[i] + jackpotAlt[i].getBetragkumuliert()) / gkl6aus49[i];
				else ausschuettung6aus49[i] = (ausschuettungSumme6aus49[i]) / gkl6aus49[i];
				//ausschuettung6aus49[i] = ausschuettungSumme6aus49[i] / gkl6aus49[i];
			}
		}
		if (gkl6aus49[9] != 0) {
			ausschuettung6aus49[9] = ausschuettungGkl9Spiel6Aus49;	// Fixwert, ohne Jackpot
		}
		
		// Spiel 77: bei Gkl1 ev. Jackpot dabei! -> erst Speicherort Jackpot Spiel77 klären!
		//quotenSpiel77[1] = quotenSpiel77[1] + jackpotAlt[10].getBetragKumuliert() / gklSpiel77[1];
		if (gklSpiel77[1] != 0) quotenSpiel77[1] /=  gklSpiel77[1];
		 
		// jetzt für jeden LS den Gewinn berechnen und eintragen
		Lottoschein lottoschein;
		List<Lottoscheinziehung> lZiehung = ziehung.getLottoscheinziehungList();	// die Liste aller (ausgewerteten) Lottoscheine
		
		// für alle Lottoscheine der Ziehung: Anzahl von gespielt, Einnahmen, Feststellung der Gewinnklassen der Spiele
		for (Lottoscheinziehung lz : lZiehung) {
			// Lottoschein holen und auswerten
			lottoschein = lz.getLottoscheinid();
			long[] tippsAsLong = ByteLongConverter.byteToLong(lottoschein.getTipps());
			int gkl;
			int AnzTippsAufSchein = tippsAsLong.length;
			for (int tipnr = 0; tipnr < AnzTippsAufSchein; tipnr++) {
				gkl = LottoUtil.gkl6Aus49(ziehung.getZahlenalsbits().longValue(), tippsAsLong[tipnr],
						ziehung.getSuperzahl() == (lottoschein.getLosnummer() % 10));
				for (int i = 1; i < 10; i++) {		// kein for [gkl gkl : aktGkl6aus49, da in aktGkl6aus49 auch[0] und [10] drin, die null
					if (aktGkl6aus49[i].getGewinnklassenr() == gkl) {
						List<Lottoscheinziehung6aus49> Listlsz49 = lottoscheinziehung6aus49FacadeLocal.findAll();
						for (Lottoscheinziehung6aus49 lsz49 : Listlsz49) {
							if (lsz49.getLottoscheinziehungid().equals(lz)) {
								Lottoscheinziehung6aus49 neueLz = lsz49;
								neueLz.setGewinn(BigInteger.valueOf(ausschuettung6aus49[i]));
								neueLz.setCreated(heute);
								neueLz.setLastmodified(heute);	// Version per default = 0
								lottoscheinziehung6aus49FacadeLocal.edit(neueLz);
							}
						}
					}
				}
			}

			gkl = LottoUtil.gklSpiel77(ziehung.getSpiel77(), lottoschein.getLosnummer());	// GKl des Scheins feststellen
			for (int i = 1; i < 8; i++) {		// kein for [gkl gkl : aktGkl6aus49, da in aktGkl6aus49 auch[0] und [1] drin, die null
				if (aktGklSpiel77[i].getGewinnklassenr() == gkl ) {
					lz.setGewinnspiel77(BigInteger.valueOf(ausschuettungSpiel77[i]));
				}
			}
			
			gkl = LottoUtil.gklSuper6(ziehung.getSuper6(), lottoschein.getLosnummer());
			for (int i = 1; i < 7; i++) {		// kein for [gkl gkl : aktGkl6aus49, da in aktGkl6aus49 auch[0] und [1] drin, die null
				if (aktGklSuper6[i].getGewinnklassenr() == gkl) {
					lz.setGewinnsuper6(BigInteger.valueOf(ausschuettungSuper6[i]));
				}
			}
			lz.setLastmodified(heute);
			lottoscheinziehungFacadeLocal.edit(lz);
		}	// end for (Lottoscheinziehung lz : lZiehung), d.h. nächster Lottoschein

		// Persistieren der GklZiehungQuote
		for (int i = 1; i < 10; i++) {	// für Gkl 1-9
			Gewinnklasseziehungquote gklq = new Gewinnklasseziehungquote();
			gklq.setZiehungid(ziehung);
			gklq.setGewinnklasseid(aktGkl6aus49[i]);
			gklq.setAnzahlgewinner(gkl6aus49[i]);
			gklq.setQuote(ausschuettung6aus49[i]);
			gklq.setCreated(heute);
			gklq.setLastmodified(heute);
			gewinnklasseziehungquoteFacadeLocal.create(gklq);
		}
		for (int i = 1; i < gklSpiel77.length; i++) {
			Gewinnklasseziehungquote gklq = new Gewinnklasseziehungquote();
			gklq.setZiehungid(ziehung);
			gklq.setGewinnklasseid(aktGklSpiel77[i]);
			gklq.setAnzahlgewinner(gklSpiel77[i]);
			gklq.setQuote(quotenSpiel77[i]);
			gklq.setCreated(heute);
			gklq.setLastmodified(heute);
			gewinnklasseziehungquoteFacadeLocal.create(gklq);
		}
		for (int i = 1; i < gklSuper6.length; i++) {
			Gewinnklasseziehungquote gklq = new Gewinnklasseziehungquote();
			gklq.setZiehungid(ziehung);
			gklq.setGewinnklasseid(aktGklSuper6[i]);
			gklq.setAnzahlgewinner(gklSuper6[i]);
			gklq.setQuote(quotenSuper6[i]);
			gklq.setCreated(heute);
			gklq.setLastmodified(heute);
			gewinnklasseziehungquoteFacadeLocal.create(gklq);
		}
	}	// end quotenBerechnen
	
	protected void abschluss(Ziehung ziehung) {
		// update ziehung (Status)
		ziehung.setStatus(2);
		ziehungFacadeLocal.edit(ziehung);
		
		//ReportGeneration(ziehung);
		
		 spiel6aus49=null;
		 spielSpiel77=null;
		 spielSuper6=null;
		 anzahlScheine = 0;	
		 anzahl6Aus49 =0;	
		 anzahlSpiel77 = 0;
		 anzahlSuper6 = 0;
		 einnahmen6Aus49 = 0;
		 einnahmenSpiel77 = 0;
		 einnahmenSuper6 = 0;
		 gkl6aus49 = new int[11];
		 gklSpiel77 = new int[8];
		 gklSuper6 = new int[7];
		 aktGkl6aus49 = new Gewinnklasse[11];
		 aktGklSpiel77 = new Gewinnklasse[8];
		 aktGklSuper6 = new Gewinnklasse[7];
		 jackpotAlt = new Jackpot[11];
		 verteilungsSchluessel6Aus49 = new BigInteger[11];
		 ausschuettung6Aus49Gesamt=0;				
		 ausschuettung6Aus49OhneGkl1=0;				
		 ausschuettung6Aus49OhneGkl1UndOhneGkl9=0;	
		 quotenSpiel77 = new long[8];
		 quotenSuper6 = new long[7];
		 ausschuettungSumme6aus49 = new long[11]; 
		 ausschuettungSummeSpiel77 = new long[8];
		 ausschuettungSummeSuper6 = new long[7];
		 ausschuettung6aus49 = new long[11]; 	
		 ausschuettungSpiel77 = new long[8];	
		 ausschuettungSuper6 = new long[7];		

		//throw new RuntimeException("Abbruch während Testphase");	// falls nicht persistiert werden soll, sondern nur Werte überprüft werden sollen		
		//System.out.println("Programm ist durchgelaufen !!!");
		// logging...
		// report generation...
		// Maps mit Quoten und Ziehungen updaten
		
		//MapQuotenUndZiehungen.bauMaps();
		
		// -> ad temporem apta: die Gewinne auswerten, dem Kunden gutschreiben und den Kunden benachrichtigen ( = Klasse "GewinnErmitteln")
	}
	
	// Hilfsklasse zum Einlesen der Ausschuettungsquote aus XML-Datei 
	private Integer[] propertiesLesen(String dateiname)  //MM2
	{
	
		Integer[] retvalue=new Integer[2] ;
		File file = null;
		FileInputStream fis = null;		
		try
		{
			file = new File(dateiname);
			fis = new FileInputStream(file);
			Properties prop = new Properties();
			prop.loadFromXML(fis);
			retvalue[0] = Integer.valueOf(prop.getProperty("Ausschuettungsquote"));              //MM2
			retvalue[1] = Integer.valueOf(prop.getProperty("ausschuettungsquoteSpiel77"));       //MM2
			
		}
		catch (Exception e)
		{
			e.printStackTrace();			
		}
		try
		{
			fis.close();
		}
		catch (IOException e)
		{
				e.printStackTrace();
		}
		return retvalue;		
	}
		
	public void ReportGeneration(Ziehung ziehung) {
		
		 List<Gewinnklasseziehungquote> gewinnklasseziehungquoteList = new ArrayList<Gewinnklasseziehungquote>();
		 List<Jackpot> jackpotList = new ArrayList<Jackpot>();
		 
		 
		 
		 jackpotFacadeLocal.findAll().stream().filter(j -> j.getBetrag() > 0  && ziehung.getZiehungid() == j.getZiehungid().getZiehungid()).
		 forEach(j-> jackpotList.add(j));
		 gewinnklasseziehungquoteFacadeLocal.findAll().stream().filter(g -> g.getZiehungid().getZiehungid() == ziehung.getZiehungid())
		 .forEach(g -> gewinnklasseziehungquoteList.add(g));
	
		PdfReportGenerator.createRepotPDF(jackpotList,gewinnklasseziehungquoteList);
	}
	
	public void sendEmail_Gewinner(Ziehung ziehung) {

		List<Lottoscheinziehung> list = em.createNamedQuery("Lottoscheinziehung.findByZiehungid").setParameter("ziehungid", ziehung).
				getResultList();
		
		List<Lottoscheinziehung> listGewinner=new ArrayList<>();
		listGewinner=list.stream().filter(a->a.getGewinnspiel77()!=null || a.getGewinnsuper6()!=null)
		.collect(Collectors.toList());
		
		
		for(Lottoscheinziehung lottoscheinziehung:listGewinner) {
			Lottoschein lottoschein=(Lottoschein)em.createNamedQuery("Lottoschein.findByLottoscheinid").
					setParameter("lottoscheinid",lottoscheinziehung.getLottoscheinid().getLottoscheinid() ).getSingleResult();
			Kunde kunde =(Kunde)em.createNamedQuery("Kunde.findByKundeid").
					setParameter("kundeid",lottoschein.getKundeid().getKundeid() ).getSingleResult();
			
			
			System.out.println(lottoscheinziehung.getLottoscheinziehungid());
			
			List<Lottoscheinziehung6aus49> ListLottoscheinziehung6aus49 =   em.createNamedQuery("Lottoscheinziehung6aus49.findByLottoscheinziehungid")
					.setParameter("lottoscheinziehungid", lottoscheinziehung).getResultList();
			
//			Lottoscheinziehung6aus49 lottoscheinziehung6aus49  =  (Lottoscheinziehung6aus49) em.createNamedQuery("findByLottoscheinziehungid")
//					.setParameter("lottoscheinziehungid", lottoscheinziehung).getSingleResult();
			Lottoscheinziehung6aus49 lottoscheinziehung6aus49  =  null;
			
			if(ListLottoscheinziehung6aus49.size() > 0)
				lottoscheinziehung6aus49 = ListLottoscheinziehung6aus49.get(0);
	 
			
			AuftragGewinner auftrag = new AuftragGewinner(lottoschein, kunde, ziehung,lottoscheinziehung,lottoscheinziehung6aus49);
		
			PDFGewinnerGenerator.createPDFAsByteArray(auftrag);
		}
		
	}
	
	
	public void createJackpots(Ziehung ziehung) {   
		// erzeugt leere Jackpots für jede Gkl von 6aus49 (da anfangs noch kein Jackpot vorhanden) -< SpielID fix hinterlegt => u.U ändern
		List<Gewinnklasse> lgkl = gewinnklasseFacadeLocal.findAll();
		for (int i = 1; i < 9; i++) {	// für Gkl 1-8
			Jackpot j = new Jackpot();
			j.setZiehungid(ziehung);
			j.setAnzahlziehungen(11);
			j.setBetrag(10000);
			for (Gewinnklasse g : lgkl) {
				if (g.getSpielid().getSpielid() == 4 && g.getGewinnklassenr() == i) {
					j.setGewinnklasseid(g);
				}
			}
			j.setBetragkumuliert(10000);
			j.setCreated(new Date());
			j.setLastmodified(new Date());
			jackpotFacadeLocal.create(j);
			System.out.println("Jackpots erzeugt");
		}
	} 
}	// end class
