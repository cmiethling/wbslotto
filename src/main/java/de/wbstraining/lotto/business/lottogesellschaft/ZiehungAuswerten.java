package de.wbstraining.lotto.business.lottogesellschaft;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.wbstraining.lotto.business.lottospieler.KostenErmittelnLocal;
import de.wbstraining.lotto.persistence.dao.GebuehrFacadeLocal;
import de.wbstraining.lotto.persistence.dao.GewinnklasseFacadeLocal;
import de.wbstraining.lotto.persistence.dao.GewinnklasseziehungquoteFacadeLocal;
import de.wbstraining.lotto.persistence.dao.Lottoscheinziehung6aus49FacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinziehungFacadeLocal;
import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Gebuehr;
import de.wbstraining.lotto.persistence.model.Gewinnklasse;
import de.wbstraining.lotto.persistence.model.Gewinnklasseziehungquote;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung6aus49;
import de.wbstraining.lotto.persistence.model.Ziehung;
import de.wbstraining.lotto.persistence.util.JPAQueriesLocal;
import de.wbstraining.lotto.util.ByteLongConverter;
import de.wbstraining.lotto.util.LottoUtil;

/**
 * Session Bean implementation class ZiehungAuswerten
 */
@Stateless
public class ZiehungAuswerten implements ZiehungAuswertenLocal {
	/*
	 * Dimitri, Christian
	 * 
	 * Namen allgemein: -6 aus 49 >> Lotto -Gewinnklasse = gkl
	 * 
	 * -Lottoschein = schein, l List<Lottoschein> = scheine, ls
	 * 
	 * -LottoscheinZiehung = scheinZiehung, lz List<LottoscheinZiehung> =
	 * scheinZiehungen,lzs
	 * 
	 * -LottoscheinZiehung6aus49 = scheinZieLotto, lzLotto
	 * List<LottoscheinZiehung6aus49> = lzLottos
	 * 
	 * Aenderungen im Code: -in KostenErmittelnLocal einfuegen: Gebuehr
	 * findGebuehrForSpielTag(List<Gebuehr> gebuehren, Date spielTag); + diese Meth
	 * in KostenErmitteln public machen (schon als private vorhanden)!
	 * 
	 * -in JPAQueries + JPAQueriesLocal einfuegen:
	 * findGewinnklassenForZiehung(Ziehung ziehung)
	 * 
	 * -PopulateDatabase.populateDatabase: diese Meth auskommentieren //
	 * populateGewinnklasseZiehungQuote.populateGewinnklasseZiehungQuote(); //
	 * populateJackpot.populateJackpot(); //
	 * populateLottoscheinZiehung6Aus49.populateLottoscheinZiehung6Aus49(); extra
	 * fuer Testdatengenerator: //
	 * populateZiehungUndLottoschein.populateZiehungUndLottoschein();
	 * 
	 * 
	 * TODO -bei hoher Datenmenge (>=100_000 lottoscheine) schafft es
	 * GarbargeCollector nicht mehr >> wegen zB ziehung.getLottoscheinziehungList()
	 * >> bei 100_000 lottoscheinziehungen kann RAM (oder Heap?) overloaded werden
	 * (java.lang.OutOfMemoryError: GC overhead limit exceeded) Lsg: Haeppchenweise
	 * Daten uebermitteln mittels facade.findRange()!!!
	 * 
	 */
	private final static Logger log = Logger
//                .getLogger(ZiehungAuswerten.class.getName());
			.getLogger("wbs.business.ZiehungAuswerten");
	{
//                log.setLevel(Level.OFF);
		log.setLevel(Level.INFO);
// !!!NICHT ConsoleHandler BENUTZEN!!!
	}

//############ Konstanten ################
	private final String SPIEL77 = "Spiel 77";
	private final String SUPER6 = "Super 6";
	private final String SECHS_AUS_49 = "6 aus 49";
	private final int ABZUG_EINSATZ_LOTTO = 100 / 50; // 50%
	private final int ABZUG_EINSATZ_SPIELL77 = ABZUG_EINSATZ_LOTTO;
	private final int SPIEL77_GKL1_ANTEIL_IN_PROZENT = 20; // 20%

//        ################ Felder ###############
	private Ziehung zie;
	private Gebuehr geb;
	private List<Lottoscheinziehung> scheinZiehungen;
	private List<Gewinnklasse> gewinnklassen;

	private long einsatzSpiel77;
	private long einsatzLotto;
	private Date jetzt;
// Value: anzGewinner dieser Gewinnklasse
	private ConcurrentMap<Gewinnklasse, Integer> anzGewinnerProKlasse;

	private AtomicInteger anzSpiel77;
	private AtomicInteger anzSuper6;
	private AtomicInteger summeAnzahlTipps;
	private List<Lottoscheinziehung6aus49> lzLottosOhneGkl9;

//        V1.1
	private List<Lottoscheinziehung> lzSpiel77Gkl1;

//        ############### Konstruktor ###############
	public ZiehungAuswerten() {
		jetzt = new Date();

		anzSpiel77 = new AtomicInteger();
		anzSuper6 = new AtomicInteger();
		summeAnzahlTipps = new AtomicInteger();

		lzLottosOhneGkl9 = new LinkedList<>();

//        V1.1
		lzSpiel77Gkl1 = new ArrayList<>();
	}

	@EJB
	private GebuehrFacadeLocal gebuehrFacade;

	@EJB
	private ZiehungFacadeLocal ziehungFacade;

	@EJB
	private LottoscheinziehungFacadeLocal lottoscheinziehungFacade;

	@EJB
	private GewinnklasseFacadeLocal gewinnklasseFacade;

	@EJB
	private Lottoscheinziehung6aus49FacadeLocal lottoscheinziehung6aus49Facade;

	@EJB
	private GewinnklasseziehungquoteFacadeLocal gewinnklasseziehungquoteFacade;

//        ################ 
	@EJB
	private KostenErmittelnLocal kostenErmitteln;

	@EJB
	private JPAQueriesLocal jpaQueries;

//        ########################### Methoden ##########################
	/**
	 * Hauptmethode
	 */
	@Override
	public void ziehungAuswerten(Ziehung zie) {
//                (zie), gewinnklassen, gebuehr, scheine, lzs, Map: anzGewinnerProKlasse
		loadData(ziehungFacade.find(zie.getZiehungid()));

		log.log(Level.INFO, "######## updateLottoscheinziehung + createLottoscheinziehung6aus49 ########");
		scheinZiehungen.stream()
//        hier kann es zu OutOfMemoryError
				.forEach(lz -> {
					updateValuesForZiehung(lz);
					updateLottoscheinziehung(lz);
					createLottoscheinziehung6aus49(lz);
				});
		updateZiehung();

		createGewinnklasseZiehungQuote();
		berechneLottoQuote();
		// TODO: Jackpot
		updateLottoscheinZiehung6aus49();

//                ############## Log #############
		log.log(Level.INFO, "");
		log.log(Level.INFO,
				"AnzahlGewinner " + SUPER6 + ": " + getMapKeyAsGewinnklassenr(SUPER6, anzGewinnerProKlasse));
		log.log(Level.INFO,
				"AnzahlGewinner " + SPIEL77 + ": " + getMapKeyAsGewinnklassenr(SPIEL77, anzGewinnerProKlasse));
		log.log(Level.INFO, "AnzahlGewinner " + SECHS_AUS_49 + ": "
				+ getMapKeyAsGewinnklassenr(SECHS_AUS_49, anzGewinnerProKlasse));
	}

	/**
	 * @param ziehung
	 * 
	 */
	private void loadData(Ziehung ziehung) {
		this.zie = ziehung;

		Date ziehungsdatum = zie.getZiehungsdatum();

		geb = kostenErmitteln.findGebuehrForSpielTag(gebuehrFacade.findAll(), ziehungsdatum);

//                hier kann es zu OutOfMemoryError kommen >> siehe todo ganz oben
		this.scheinZiehungen = zie.getLottoscheinziehungList();

////        Gewinnklassen genau fuer diese eine Ziehung heraussuchen, 
////        TODO: fuer Testdatengenerator UNTERE Zeile nehmen
		gewinnklassen = jpaQueries.findGewinnklassenForZiehung(zie);
//                gewinnklassen = gewinnklasseFacade.findAll();

		// mit anzGewinner=0 initialisieren
		anzGewinnerProKlasse = gewinnklassen.stream().collect(Collectors.toConcurrentMap(gkl -> gkl, gkl -> 0));

		log.log(Level.INFO, "############# loadData #####################");
		log.log(Level.INFO,
				"ZiehungsId: " + zie.getZiehungid() + "   " + SPIEL77 + ": " + zie.getSpiel77() + "   " + SUPER6 + ": "
						+ zie.getSuper6() + "   LottoZahlen: "
						+ LottoUtil.tippAsString((zie.getZahlenalsbits().longValue())));
		log.log(Level.INFO, "AnzGewinnklassen = 22?: " + (gewinnklassen.size() == 22 ? true : false));
//                log.log(Level.INFO, "GewinnklassenIds: " + gewinnklassen.stream()
//                        .collect(Collectors.mapping(gkl -> gkl.getGewinnklasseid(),
//                                Collectors.toList())));
		log.log(Level.INFO, "Gebuehr: " + geb);
//                log.log(Level.INFO, "lottoscheinziehungIds: " + scheinZiehungen.stream()
//                        .collect(Collectors.mapping(lz -> lz.getLottoscheinziehungid(),
//                                Collectors.toList())));
//                log.log(Level.INFO, "lottoscheinIds: " + scheine.stream()
//                        .collect(
//                                Collectors.mapping(s -> s.getLottoscheinid(), Collectors.toList())));
	}

	/**
	 * 
	 * @param lz
	 */
	private void updateValuesForZiehung(Lottoscheinziehung lz) {
		if (lz.getLottoscheinid().getIsspiel77())
			anzSpiel77.incrementAndGet();
		if (lz.getLottoscheinid().getIssuper6())
			anzSuper6.incrementAndGet();
		summeAnzahlTipps.addAndGet(ByteLongConverter.byteToLong(lz.getLottoscheinid().getTipps()).length);
	}

	/**
	 * 
	 * @param lz
	 */
	private void updateLottoscheinziehung(Lottoscheinziehung lz) {

// was sagt isAbgeschlossen? >> ev. ist schon bezahlt? 1 = ja

		int gklNr = LottoUtil.gklSuper6(this.zie.getSuper6(), lz.getLottoscheinid().getLosnummer());
		Optional<Gewinnklasse> gklSuper6 = findGklForScheinZie(SUPER6, gklNr, this.gewinnklassen);

		gklSuper6.ifPresent(gkl -> {
			// fuer gewinnklasseziehungquote
			anzGewinnerProKlasse.computeIfPresent(gkl, (Gewinnklasse g, Integer anzGew) -> ++anzGew);

			lz.setVersion(getUpdatedVersion(lz.getVersion()));
			lz.setLastmodified(jetzt);

			lz.setGewinnklasseidsuper6(gkl);
			// Bei Super6 alle Gewinne absolut
			lz.setGewinnsuper6(gkl.getBetrag());

//                        log.log(Level.INFO,
//                                "\t"+ SUPER6+ ": zieId: " + zie.getZiehungid() + "   ScheinId: "
//                                        + lz.getLottoscheinid()
//                                                .getLottoscheinid()
//                                        + "  >> Zie: " + zie.getSuper6() + " <-> " + lz.getLottoscheinid()
//                                                .getLosnummer()
//                                        + ": Losnr, " + gkl.getBeschreibung());
		});

		// Spiel 77
		gklNr = LottoUtil.gklSpiel77(this.zie.getSpiel77(), lz.getLottoscheinid().getLosnummer());
		Optional<Gewinnklasse> gklSpiel77 = findGklForScheinZie(SPIEL77, gklNr, this.gewinnklassen);

		gklSpiel77.ifPresent(gkl -> {
			// fuer gewinnklasseziehungquote
			anzGewinnerProKlasse.computeIfPresent(gkl, (Gewinnklasse g, Integer anzGew) -> ++anzGew);

			lz.setVersion(getUpdatedVersion(lz.getVersion()));
			lz.setLastmodified(jetzt);

			lz.setGewinnklasseidspiel77(gkl);

			if (gkl.getGewinnklassenr() != 1) {
				lz.setGewinnspiel77(gkl.getBetrag());
			} else {
// V1.1: Sp77gkl1 Zwischenspeichern, da einsatzSpiel77 noch nicht befuellt (in updateValuesForZiehung())
				lzSpiel77Gkl1.add(lz);
			}

//                        log.log(Level.INFO,
//                                SPIEL77 + ": zieId: " + zie.getZiehungid() + "   ScheinId: "
//                                        + lz.getLottoscheinid()
//                                                .getLottoscheinid()
//                                        + "  >> Zie: " + zie.getSpiel77() + " <-> " + lz.getLottoscheinid()
//                                                .getLosnummer()
//                                        + ": Losnr, " + gkl.getBeschreibung());
		}); // endSpiel77

		lottoscheinziehungFacade.edit(lz);
	}

	/**
	 * 
	 * @param lz
	 */
	private void createLottoscheinziehung6aus49(Lottoscheinziehung lz) {
		Lottoscheinziehung6aus49 lzLotto = new Lottoscheinziehung6aus49();
		Lottoschein schein = lz.getLottoscheinid();

		int gkl6Aus49;
		long ziehungsZahlen;
		long tipp;
		long[] tippsAsLongArray;

		int superzahl;
		int losnummer;

		boolean hasMatchingSuperzahl;

		// ######################################
		ziehungsZahlen = zie.getZahlenalsbits().longValue();
		superzahl = zie.getSuperzahl();
		losnummer = schein.getLosnummer();
		// schein.getTipps() >> byte[]
		tippsAsLongArray = ByteLongConverter.byteToLong(schein.getTipps());

		for (int i = 0; i < tippsAsLongArray.length; i++) {

			tipp = tippsAsLongArray[i];
			hasMatchingSuperzahl = losnummer % 10 == superzahl;
			gkl6Aus49 = LottoUtil.gkl6Aus49(ziehungsZahlen, tipp, hasMatchingSuperzahl);
			if (gkl6Aus49 > 0) {
				lzLotto = new Lottoscheinziehung6aus49();

				Optional<Gewinnklasse> gklLottoOpt = findGklForScheinZie(SECHS_AUS_49, gkl6Aus49, this.gewinnklassen);

				Gewinnklasse gkl = gklLottoOpt
						.orElseThrow(() -> new NoSuchElementException("no valid record in gewinnklasse..."));

				lzLotto.setCreated(jetzt);
				lzLotto.setLastmodified(jetzt);
				lzLotto.setTippnr(i + 1);
				lzLotto.setLottoscheinziehungid(lz);

				lzLotto.setGewinnklasseid(gkl);
				anzGewinnerProKlasse.computeIfPresent(gkl, (Gewinnklasse g, Integer anzGew) -> ++anzGew);

				if (gkl.getGewinnklassenr() == 9) {
					lzLotto.setGewinn(gkl.getBetrag());
				} else {
					lzLottosOhneGkl9.add(lzLotto);
				}

				lottoscheinziehung6aus49Facade.create(lzLotto);

//                                // ########## Log ##################
//                                log.log(Level.INFO,
//                                        "scheinZiehungId: " + lz.getLottoscheinziehungid() + "   tippnr: "
//                                                + (i + 1) + "   tipp: " + LottoUtil.tippAsString(tipp) + " >> "
//                                                + LottoUtil.tippAsString(ziehungsZahlen) + ": ziehung  >> "
//                                                + gkl.getBeschreibung() + "   GewKla: " + gkl6Aus49
//                                                + "       Losnr: " + lz.getLottoscheinid()
//                                                        .getLosnummer()
//                                                + " >> " + zie.getSuperzahl() + ": Superzahl  (scheinId: "
//                                                + lz.getLottoscheinid()
//                                                        .getLottoscheinid()
//                                                + ")");
			}
		}
	}

	private void updateZiehung() {
		zie.setVersion(getUpdatedVersion(zie.getVersion()));

		final long einsatzSuper6 = anzSuper6.get() * geb.getEinsatzsuper6();
		einsatzSpiel77 = anzSpiel77.get() * geb.getEinsatzspiel77();
		einsatzLotto = summeAnzahlTipps.get() * geb.getEinsatzprotipp();

		zie.setEinsatzspiel77(BigInteger.valueOf(einsatzSpiel77));
		zie.setEinsatzsuper6(BigInteger.valueOf(einsatzSuper6));
		zie.setEinsatzlotto(BigInteger.valueOf(einsatzLotto));

		ziehungFacade.edit(zie);

		log.log(Level.INFO, "############# updateZiehung(ziehung) ##############");
		log.log(Level.INFO,
				"AnzSuper6 * gebuehr = Einsatz: " + anzSuper6 + " * " + geb.getEinsatzsuper6() + " = " + einsatzSuper6);
		log.log(Level.INFO, "AnzSpiel77 * gebuehr = Einsatz: " + anzSpiel77 + " * " + geb.getEinsatzspiel77() + " = "
				+ einsatzSpiel77);
		log.log(Level.INFO, "GesamtAnzTipps * gebuehr = Einsatz: " + summeAnzahlTipps + " * " + geb.getEinsatzprotipp()
				+ " = " + einsatzLotto);
	}

	/**
	 * 
	 */
	private void createGewinnklasseZiehungQuote() {

		gewinnklassen.forEach(gkl -> {
			Gewinnklasseziehungquote gklZieQuo = new Gewinnklasseziehungquote();
			gklZieQuo.setCreated(jetzt);
			gklZieQuo.setLastmodified(jetzt);
			gklZieQuo.setZiehungid(zie);
			gklZieQuo.setAnzahlgewinner(anzGewinnerProKlasse.get(gkl));

			gklZieQuo.setGewinnklasseid(gkl);

//        setBetrag: falls Gkl1 aus Spiel 77 dann Ausnahme!
			Optional<Gewinnklasse> gkl1AusSpiel77 = Optional.of(gkl)
					.filter(g -> g.getSpielid().getName().equals(SPIEL77)).filter(g -> g.getGewinnklassenr() == 1);

			if (gkl1AusSpiel77.isPresent()) {
				gklZieQuo.setQuote(setBetragForGkl1ForSpiel77(SPIEL77_GKL1_ANTEIL_IN_PROZENT));
//        V1.1: zusaetzlich Betrag fuer alle Lottoscheinziehungen updaten (jetzt ist einsatzSpiel77 aktuell!)
				lzSpiel77Gkl1.stream().forEach(lz -> {
					lz.setGewinnspiel77(BigInteger.valueOf(setBetragForGkl1ForSpiel77(SPIEL77_GKL1_ANTEIL_IN_PROZENT)));
					lz.setVersion(getUpdatedVersion(lz.getVersion()));
					lottoscheinziehungFacade.edit(lz); // irgendwie nicht notwendig???
				});

			} else {
				long betrag = anzGewinnerProKlasse.get(gkl) != 0 ? gkl.getBetrag().longValue() : 0L;
				gklZieQuo.setQuote(betrag);
			}

			gewinnklasseziehungquoteFacade.create(gklZieQuo);
		});

	}

	/**
	 * 
	 */
	private void berechneLottoQuote() {

		Gewinnklasse[] gklsLotto = getGewinnklassenLotto(gewinnklassen).toArray(new Gewinnklasse[9]);

		final int[] prozenteMal100 = new int[9];
		for (int i = 0; i < prozenteMal100.length; i++) {
			prozenteMal100[i] = gewinnklassen.get(i).getBetrag().intValue();
		}
		long uebrigeVerteilung = einsatzLotto / ABZUG_EINSATZ_LOTTO;

//        ################ fuer Log ####################
		long gkl1abgezogen = uebrigeVerteilung * prozenteMal100[0] / 10000;
		log.log(Level.INFO, "############ berechneLottoQuote #############");
		log.log(Level.INFO, "(Prozentrechnung: uebrigeVerteilung * Prozent / 100 = AnteilGkl)");
//        ###################################

//        Gkl1 berechnen + abziehen
		uebrigeVerteilung = setBetragForGkl(gklsLotto[0], prozenteMal100[0], uebrigeVerteilung, anzGewinnerProKlasse);

//        Gkl9 berechnen + abziehen
		Gewinnklasse gkl9 = gklsLotto[8];
		int anzGkl9 = anzGewinnerProKlasse.get(gkl9);
		uebrigeVerteilung = uebrigeVerteilung - (anzGkl9 * gkl9.getBetrag().longValue());
		updateGewinnklasseziehungQuote(gkl9, gkl9.getBetrag().longValue());

//                ########### ZwischenLog ##############
		log.log(Level.INFO, "EinsatzLotto/" + ABZUG_EINSATZ_LOTTO + ": " + (einsatzLotto / ABZUG_EINSATZ_LOTTO)
				+ "    gkl1abgezogen: " + gkl1abgezogen +

				"     anzGkl9 * gebuehr = gkl9abgezogen: " + anzGkl9 + " * " + gkl9.getBetrag().intValue() + " = "
				+ (anzGkl9 * gkl9.getBetrag().intValue()));
		log.log(Level.INFO, "uebrigeVerteilungFuerGkl2-8: " + (einsatzLotto / ABZUG_EINSATZ_LOTTO) + " - "
				+ gkl1abgezogen + " - " + (anzGkl9 * gkl9.getBetrag().intValue()) + " = " + uebrigeVerteilung);
//                ###################################

//                Gkl2-8 berechnen
		for (int i = 1; i < gklsLotto.length - 1; i++) {
			setBetragForGkl(gklsLotto[i], prozenteMal100[i], uebrigeVerteilung, anzGewinnerProKlasse);
		}
	}

	/**
	 * 
	 */
	private void updateLottoscheinZiehung6aus49() {

		List<Gewinnklasseziehungquote> gklZieQuos = zie.getGewinnklasseziehungquoteList();
////        nehmen wenn  NoSuchElementException fliegt!!!
//                scheinZiehungen.stream()
//                        .map(lz -> lz.getLottoscheinziehung6aus49List())
//                        .flatMap(lzLottoList -> lzLottoList.stream()) // Stream<lzLotto>
//// alle relativen Eintraege (alle Gkls ausser Gkl9)
//                        .filter(lzLotto -> !lzLotto.getGewinnklasseid()
//                                .getIsabsolut())
		lzLottosOhneGkl9.forEach(lzLotto -> {
			Gewinnklasseziehungquote gklZie = gklZieQuos.stream().filter(
					g -> g.getGewinnklasseid().getGewinnklasseid() == lzLotto.getGewinnklasseid().getGewinnklasseid()) // mit
																														// long
																														// vergleichen
					.findAny()
					.orElseThrow(() -> new NoSuchElementException("invalid record in gewinnklasseziehungsquote or "
							+ "lottoscheinziehung6aus49...  lzLottoGklId: "
							+ lzLotto.getGewinnklasseid().getGewinnklasseid() + "   gklZieQuosGklIds: "
							+ gklZieQuos.stream().map(g -> g.getGewinnklasseid().getGewinnklasseid())
									.collect(Collectors.toList())));

			lzLotto.setGewinn(BigInteger.valueOf(gklZie.getQuote()));
			lzLotto.setVersion(getUpdatedVersion(lzLotto.getVersion()));
			lottoscheinziehung6aus49Facade.edit(lzLotto);
		});
	}

// #############################################################
//############## Helper Methods ################################

	private Optional<Gewinnklasse> findGklForScheinZie(String spielName, int computedKlssNumber,
			List<Gewinnklasse> gwnClassesForZhng) {
		return gwnClassesForZhng.stream().filter(g -> g.getSpielid().getName().equals(spielName))
				.filter(g -> g.getGewinnklassenr() == computedKlssNumber).findAny();
	}

	private long setBetragForGkl(Gewinnklasse gkl, int prozenteMal100, long uebrigeVerteilung,
			Map<Gewinnklasse, Integer> anzGewinnerProKlasse) {
// /10_000 zum Schluss, sonst wird uebrigeVerteilung "beschnitten"
		long gklAnteil = uebrigeVerteilung * prozenteMal100 / 10_000;

//                
		int anzGew = anzGewinnerProKlasse.get(gkl);
		long quoteGkl = 0;
		if (anzGew != 0) {
			quoteGkl = gklAnteil / anzGew;
		} else {
//                        TODO: createJackpot(gklAnteil, gkl, (zie))
		}
		updateGewinnklasseziehungQuote(gkl, quoteGkl);

		log.log(Level.INFO,
				"Gkl: " + gkl.getGewinnklassenr() + " >> gklAnteil / anzGew(bei 0>>Jackpot) = quoteGlk:  " + gklAnteil
						+ " / " + anzGew + " = " + quoteGkl + "   >>  Gkl " + gkl.getGewinnklassenr() + ": "
						+ (prozenteMal100 / 100.0) + "% von " + uebrigeVerteilung + ": " + gklAnteil);

//                muss hinter Log sein, sonst zeigt Log falsche uebrigeVerteilung
		if (gkl.getGewinnklassenr() == 1) {
			uebrigeVerteilung -= gklAnteil;
		}

		return uebrigeVerteilung; // fuer Gkl1
	}

	private int getUpdatedVersion(Integer version) {
		Optional<Integer> versionOpt = Optional.ofNullable(version);
		return versionOpt.isPresent() ? versionOpt.get() + 1 : 1;
	}

	private Map<Integer, Integer> getMapKeyAsGewinnklassenr(String spielX,
			Map<Gewinnklasse, Integer> anzGewinnerProKlaSpielX) {
		Map<Integer, Integer> anzGewProKlaSpielX = anzGewinnerProKlaSpielX.entrySet().stream()
				.filter(e -> e.getKey().getSpielid().getName().equals(spielX))
				.collect(Collectors.toMap(e -> e.getKey().getGewinnklassenr(), e -> e.getValue()));
		return anzGewProKlaSpielX;
	}

	private List<Gewinnklasse> getGewinnklassenLotto(List<Gewinnklasse> gewinnklassen) {
		return gewinnklassen.stream().filter(g -> g.getSpielid().getName().equals(SECHS_AUS_49))
//                                .sorted((g1, g2) -> Long.compare(g1.getGewinnklasseid(),
//                                        g2.getGewinnklasseid()))
				.collect(Collectors.toList());
	}

	private void updateGewinnklasseziehungQuote(Gewinnklasse gkl, long quoteGkl) {
		Gewinnklasseziehungquote gklZieQuo = gewinnklasseziehungquoteFacade.findAll().stream()
				.filter(g -> g.getGewinnklasseid().getGewinnklasseid() == gkl.getGewinnklasseid()).findAny()
				.orElseThrow(() -> new NoSuchElementException("no valid record in gewinnklasseziehungquote..."));

		gklZieQuo.setVersion(getUpdatedVersion(gklZieQuo.getVersion()));
		gklZieQuo.setLastmodified(jetzt);

		gklZieQuo.setAnzahlgewinner(anzGewinnerProKlasse.get(gkl));
		gklZieQuo.setQuote(quoteGkl);

		gewinnklasseziehungquoteFacade.edit(gklZieQuo);
	}

	private long setBetragForGkl1ForSpiel77(int spiel77Gkl1AnteilInProzent) {
		return (einsatzSpiel77 / ABZUG_EINSATZ_SPIELL77) //
				* spiel77Gkl1AnteilInProzent / 100;
	}
}