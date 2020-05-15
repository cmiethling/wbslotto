package de.wbstraining.lotto.business.lottogesellschaft;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
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
import de.wbstraining.lotto.persistence.dao.JackpotFacadeLocal;
import de.wbstraining.lotto.persistence.dao.Lottoscheinziehung6aus49FacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinziehungFacadeLocal;
import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Gebuehr;
import de.wbstraining.lotto.persistence.model.Gewinnklasse;
import de.wbstraining.lotto.persistence.model.Gewinnklasseziehungquote;
import de.wbstraining.lotto.persistence.model.Jackpot;
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
	 * Namen allgemein:
	 * -6 aus 49 >> Lotto
	 * -Gewinnklasse = gkl
	 * -GewinnklasseZiehungQuote = gklZieQuo
	 * 
	 * -Lottoschein = schein, l
	 * List<Lottoschein> = scheine, ls
	 * 
	 * -LottoscheinZiehung = scheinZiehung, lz
	 * List<LottoscheinZiehung> = scheinZiehungen,lzs
	 * 
	 * -LottoscheinZiehung6aus49 = scheinZieLotto, lzLotto
	 * List<LottoscheinZiehung6aus49> = lzLottos
	 * 
	 */
	private final static Logger log = Logger
//		.getLogger(ZiehungAuswerten.class.getName());
			.getLogger("wbs.business.ZiehungAuswerten");
	{
//		log.setLevel(Level.OFF);
		log.setLevel(Level.INFO);
	}

//############ Konstanten ################
	private static final String SPIEL77 = "Spiel 77";
	private static final String SUPER6 = "Super 6";
	private static final String LOTTO = "6 aus 49";
	private static final int ABZUG_EINSATZ_LOTTO = 5000; // 50%
	private static final int ABZUG_EINSATZ_SPIELL77 = ABZUG_EINSATZ_LOTTO;
	private static final int SPIEL77_GKL1_ANTEIL_IN_PROZENT = 20; // 20%
//AnzZiehungen bei welcher angehaeufter Jackpotgewinn auf untere Gewinnklasse geht
	private static final int MAX_TIMES_JACKPOT = 12;

//	################ Felder ###############
	private Ziehung zie;
	private Gebuehr geb;
	private List<Lottoscheinziehung> scheinZiehungen;
	private List<Gewinnklasse> gewinnklassen;

	private long einsatzSpiel77;
	private long einsatzLotto;
	private LocalDateTime jetzt;
// Value: anzGewinner dieser Gewinnklasse
	private ConcurrentMap<Gewinnklasse, Integer> anzGewinnerProKlasse;

	private AtomicInteger anzSpiel77;
	private AtomicInteger anzSuper6;
	private AtomicInteger summeAnzahlTipps;
	private List<Lottoscheinziehung6aus49> lzLottosOhneGkl9;

//	V1.1
	private List<Lottoscheinziehung> lzSpiel77Gkl1;

	private Map<Jackpot, boolean[]> jackpotParameters;

//	############### Konstruktor ###############
	public ZiehungAuswerten() {
		jetzt = LocalDateTime.now();
		anzSpiel77 = new AtomicInteger();
		anzSuper6 = new AtomicInteger();
		summeAnzahlTipps = new AtomicInteger();
		lzLottosOhneGkl9 = new LinkedList<>();
//	V1.1
		lzSpiel77Gkl1 = new ArrayList<>();
	}

// ############# EJBs ##############
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

	@EJB
	private JackpotFacadeLocal jackpotFacade;

//	################ 
	@EJB
	private KostenErmittelnLocal kostenErmitteln;

	@EJB
	private JPAQueriesLocal jpaQueries;

//	########################### Methoden ##########################
	/**
	 * Hauptmethode:
	 * Hier wird die ZiehungsAuswertung durchgefuehrt mit Beruecksichtigung des
	 * Jackpots fuer Gewinnklasse 1 vom Spiel 6aus49
	 * 
	 * Tabellen:
	 * -ziehung: updatet einsaetze fuer alle 3 Spiele
	 * -lottoscheinziehung: updatet gewinn und gewinnklasseie fuer Spiel77 und
	 * Super6
	 * -folgende Tabellen werden befuellt:
	 * ..... lottoscheinziehung6aus49
	 * ..... gewinnklasseziehungquote
	 * ..... jackpot
	 */
	@Override
	public void ziehungAuswerten(Ziehung zie) {
//	(zie), gewinnklassen, gebuehr, scheine, lzs, Map: anzGewinnerProKlasse
		loadData(ziehungFacade.find(zie.getZiehungid()));

		log.info(
				"#### updateLottoscheinziehung + createLottoscheinziehung6aus49 ####");
		scheinZiehungen.stream()
				.forEach(lz -> {
					updateValuesForZiehung(lz);
// fuer jeweils Super6 und Spiel77: gewinnklasseId + gewinn
					updateLottoscheinziehung(lz);
					createLottoscheinziehung6aus49(lz);
				});
//	einsatzLotto, einsatzSuper6, einsatzSpiel77
		updateZiehung();
//	fuer jede Gkl record erstellen (in Lotto quote erstmal mit falschen Wert initialisieren)
		createGewinnklasseZiehungQuote();

//	mit Jackpot
		jackpotParameters = getJackpotParameters();
//	Update GklZieQuo fuer Lotto: quote  und create Jackpot (nicht immer)
		berechneLottoQuote(jackpotParameters);
//	Update GklZieQuo falls Ausschuettung Jackpot: quote mit Jackpot addieren
		includeJackpotInQuote(jackpotParameters);

//	gewinn
		updateLottoscheinZiehung6aus49();

//		############## Log #############
		final String anzGew = "AnzahlGewinner ";
		log.info("");
		log.info(anzGew + SUPER6 + ": "
				+ getMapKeyAsGewinnklassenr(SUPER6, anzGewinnerProKlasse));
		log.info(anzGew + SPIEL77 + ": "
				+ getMapKeyAsGewinnklassenr(SPIEL77, anzGewinnerProKlasse));
		log.info(anzGew + LOTTO + ": "
				+ getMapKeyAsGewinnklassenr(LOTTO, anzGewinnerProKlasse));
	}

	/*
	 * 
	 */
	private void loadData(Ziehung ziehung) {
		this.zie = ziehung;
		LocalDate ziehungsdatum = zie.getZiehungsdatum();

		geb = kostenErmitteln.findGebuehrForSpielTag(gebuehrFacade.findAll(),
				ziehungsdatum);

		this.scheinZiehungen = zie.getLottoscheinziehungList();

////	Gewinnklassen genau fuer diese eine Ziehung heraussuchen
		gewinnklassen = jpaQueries.findGewinnklassenForZiehung(zie);
//		gewinnklassen = gewinnklasseFacade.findAll();

//	alle Felder ruecksetzen (fuer Auswertung mehrerer Ziehungen)
		resetFields();

		log.info("############# loadData #####################");
		log.info("ZiehungsId: " + zie.getZiehungid() + "   " + SPIEL77 + ": "
				+ zie.getSpiel77() + "   " + SUPER6 + ": " + zie.getSuper6()
				+ "   LottoZahlen: "
				+ LottoUtil.tippAsString((zie.getZahlenalsbits())));
		log.info("AnzGewinnklassen = 22?: "
				+ (gewinnklassen.size() == 22 ? true : false));
//		log.info("GewinnklassenIds: " + gewinnklassen.stream()
//			.collect(Collectors.mapping(gkl -> gkl.getGewinnklasseid(),
//				Collectors.toList())));
		log.info("Gebuehr: " + geb);
//		log.info("lottoscheinziehungIds: " + scheinZiehungen.stream()
//			.collect(Collectors.mapping(lz -> lz.getLottoscheinziehungid(),
//				Collectors.toList())));
//		log.info("lottoscheinIds: " + scheine.stream()
//			.collect(
//				Collectors.mapping(s -> s.getLottoscheinid(), Collectors.toList())));
	}

	/* 
	 * 
	 */
	private void updateValuesForZiehung(Lottoscheinziehung lz) {
		if (lz.getLottoschein()
				.getIsspiel77())
			anzSpiel77.incrementAndGet();
		if (lz.getLottoschein()
				.getIssuper6())
			anzSuper6.incrementAndGet();
		summeAnzahlTipps.addAndGet(ByteLongConverter.byteToLong(lz.getLottoschein()
				.getTipps()).length);
	}

	/*
	 * 
	 */
	private void updateLottoscheinziehung(Lottoscheinziehung lz) {

// was sagt isAbgeschlossen? >> ev. ist schon bezahlt? 1 = ja

//	Super 6
		int gklNr = LottoUtil.gklSuper6(this.zie.getSuper6(), lz.getLottoschein()
				.getLosnummer());
		Optional<Gewinnklasse> gklSuper6 = findGklForScheinZie(SUPER6, gklNr,
				this.gewinnklassen);

		gklSuper6.ifPresent(gkl -> {
			// fuer gewinnklasseziehungquote
			anzGewinnerProKlasse.computeIfPresent(gkl,
					(Gewinnklasse g, Integer anzGew) -> ++anzGew);

			lz.setVersion(getUpdatedVersion(lz.getVersion()));
			lz.setLastmodified(jetzt);

			lz.setGewinnklassesuper6(gkl);
			// Bei Super6 alle Gewinne absolut
			lz.setGewinnsuper6(gkl.getBetrag());

			log.fine("\t" + SUPER6 + ": zieId: " + zie.getZiehungid()
					+ "   ScheinId: " + lz.getLottoschein()
							.getLottoscheinid()
					+ "  >> Zie: " + zie.getSuper6() + " <-> " + lz.getLottoschein()
							.getLosnummer()
					+ ": Losnr, " + gkl.getBeschreibung());
		});

// Spiel 77
		gklNr = LottoUtil.gklSpiel77(this.zie.getSpiel77(), lz.getLottoschein()
				.getLosnummer());
		Optional<Gewinnklasse> gklSpiel77 = findGklForScheinZie(SPIEL77, gklNr,
				this.gewinnklassen);

		gklSpiel77.ifPresent(gkl -> {
			// fuer gewinnklasseziehungquote
			anzGewinnerProKlasse.computeIfPresent(gkl,
					(Gewinnklasse g, Integer anzGew) -> ++anzGew);

			lz.setVersion(getUpdatedVersion(lz.getVersion()));
			lz.setLastmodified(jetzt);

			lz.setGewinnklassespiel77(gkl);

			if (gkl.getGewinnklassenr() != 1) {
				lz.setGewinnspiel77(gkl.getBetrag());
			} else {
// V1.1: Sp77gkl1 Zwischenspeichern, da einsatzSpiel77 noch nicht befuellt (in updateValuesForZiehung())
				lzSpiel77Gkl1.add(lz);
			}

			log.fine(SPIEL77 + ": zieId: " + zie.getZiehungid() + "   ScheinId: "
					+ lz.getLottoschein()
							.getLottoscheinid()
					+ "  >> Zie: " + zie.getSpiel77() + " <-> " + lz.getLottoschein()
							.getLosnummer()
					+ ": Losnr, " + gkl.getBeschreibung());
		}); // endSpiel77

		lottoscheinziehungFacade.edit(lz);
	}

	/* 
	 *
	 */
	private void createLottoscheinziehung6aus49(Lottoscheinziehung lz) {
		Lottoscheinziehung6aus49 lzLotto = new Lottoscheinziehung6aus49();
		Lottoschein schein = lz.getLottoschein();

		int gkl6Aus49;
		long ziehungsZahlen;
		long tipp;
		long[] tippsAsLongArray;

		int superzahl;
		int losnummer;

		boolean hasMatchingSuperzahl;

		// ######################################
		ziehungsZahlen = zie.getZahlenalsbits();
		superzahl = zie.getSuperzahl();
		losnummer = schein.getLosnummer();
		// schein.getTipps() >> byte[]
		tippsAsLongArray = ByteLongConverter.byteToLong(schein.getTipps());

		for (int i = 0; i < tippsAsLongArray.length; i++) {
			tipp = tippsAsLongArray[i];
			hasMatchingSuperzahl = losnummer % 10 == superzahl;
			gkl6Aus49 = LottoUtil.gkl6Aus49(ziehungsZahlen, tipp,
					hasMatchingSuperzahl);
			if (gkl6Aus49 > 0) {
				lzLotto = new Lottoscheinziehung6aus49();

				Optional<Gewinnklasse> gklLottoOpt = findGklForScheinZie(LOTTO,
						gkl6Aus49, this.gewinnklassen);

				Gewinnklasse gkl = gklLottoOpt
						.orElseThrow(() -> new NoSuchElementException(
								"no valid record in gewinnklasse..."));

				lzLotto.setCreated(jetzt);
				lzLotto.setLastmodified(jetzt);
				lzLotto.setTippnr(i + 1);
				lzLotto.setLottoscheinziehung(lz);

				lzLotto.setGewinnklasse(gkl);
				anzGewinnerProKlasse.computeIfPresent(gkl,
						(Gewinnklasse g, Integer anzGew) -> ++anzGew);

				if (gkl.getGewinnklassenr() == 9) {
					lzLotto.setGewinn(gkl.getBetrag());
				} else {
					lzLottosOhneGkl9.add(lzLotto);
				}

				lottoscheinziehung6aus49Facade.create(lzLotto);

//				// ########## Log ##################
				log.fine(
						"scheinZiehungId: " + lz.getLottoscheinziehungid() + "   tippnr: "
								+ (i + 1) + "   tipp: " + LottoUtil.tippAsString(tipp) + " >> "
								+ LottoUtil.tippAsString(ziehungsZahlen) + ": ziehung  >> "
								+ gkl.getBeschreibung() + "   GewKla: " + gkl6Aus49
								+ "       Losnr: " + lz.getLottoschein()
										.getLosnummer()
								+ " >> " + zie.getSuperzahl() + ": Superzahl  (scheinId: "
								+ lz.getLottoschein()
										.getLottoscheinid()
								+ ")");
			}
		}
	}

	private void updateZiehung() {
		zie.setVersion(getUpdatedVersion(zie.getVersion()));
		zie.setLastmodified(jetzt);

		final long einsatzSuper6 = anzSuper6.get() * geb.getEinsatzsuper6();
		einsatzSpiel77 = anzSpiel77.get() * geb.getEinsatzspiel77();
		einsatzLotto = summeAnzahlTipps.get() * geb.getEinsatzprotipp();

		zie.setEinsatzspiel77(einsatzSpiel77);
		zie.setEinsatzsuper6(einsatzSuper6);
		zie.setEinsatzlotto(einsatzLotto);

		ziehungFacade.edit(zie);

		log.info("############# updateZiehung ##############");
		log.info("AnzSuper6 * gebuehr = Einsatz: " + anzSuper6 + " * "
				+ geb.getEinsatzsuper6() + " = " + einsatzSuper6);
		log.info("AnzSpiel77 * gebuehr = Einsatz: " + anzSpiel77 + " * "
				+ geb.getEinsatzspiel77() + " = " + einsatzSpiel77);
		log.info("GesamtAnzTipps * gebuehr = Einsatz: " + summeAnzahlTipps + " * "
				+ geb.getEinsatzprotipp() + " = " + einsatzLotto);
	}

	/*
	 * 
	 */
	private void createGewinnklasseZiehungQuote() {

		gewinnklassen.forEach(gkl -> {
			Gewinnklasseziehungquote gklZieQuo = new Gewinnklasseziehungquote();
			gklZieQuo.setCreated(jetzt);
			gklZieQuo.setLastmodified(jetzt);
			gklZieQuo.setZiehung(zie);
			gklZieQuo.setAnzahlgewinner(anzGewinnerProKlasse.get(gkl));

			gklZieQuo.setGewinnklasse(gkl);

//	setBetrag: falls Gkl1 aus Spiel 77 dann Ausnahme!
			Optional<Gewinnklasse> gkl1AusSpiel77 = Optional.of(gkl)
					.filter(g -> g.getSpiel()
							.getName()
							.equals(SPIEL77))
					.filter(g -> g.getGewinnklassenr() == 1);

			if (gkl1AusSpiel77.isPresent()) {
				gklZieQuo.setQuote(
						setBetragForGkl1ForSpiel77(SPIEL77_GKL1_ANTEIL_IN_PROZENT));
//	V1.1: zusaetzlich Betrag fuer alle Lottoscheinziehungen updaten (jetzt ist einsatzSpiel77 aktuell!)
				lzSpiel77Gkl1.stream()
						.forEach(lz -> {
							lz.setGewinnspiel77(
									setBetragForGkl1ForSpiel77(SPIEL77_GKL1_ANTEIL_IN_PROZENT));
							lz.setVersion(getUpdatedVersion(lz.getVersion()));
							lottoscheinziehungFacade.edit(lz); // irgendwie nicht notwendig???
						});

			} else {
				long betrag = anzGewinnerProKlasse.get(gkl) != 0 ? gkl.getBetrag() : 0L;
				gklZieQuo.setQuote(betrag);
			}

			gewinnklasseziehungquoteFacade.create(gklZieQuo);
		});

	}

	/*
	 * Meth nur einmal aufrufen!
	 * >> Map <Jackpot,boolean[2]> getJackpotParameter(Map<gkl, gklZieQuo>
	 * gklZieQuoProGkl, ziehung, int prozenteMal100FuerGlk1=1280,
	 * int maxTimesJackpot=12, SPIEL77, SUPER6)
	 * 
	 * Map<Jackpot jkDimi,<bool[0]=12wo, bool[1]=anzGew>>
	 * -boolean(0) is boolean isGkl1WinnerJackpot >> anzGewGkl1>0 =true (bekommt
	 * demzufolge Jackpot)
	 * -boolean(1) is boolean is12TimesSep zb Woche 2 = false
	 * -is12TimesSep=false: Gkl = 1
	 * -is12TimesSep=true: Gkl =1-9
	 */
	private Map<Jackpot, boolean[]> getJackpotParameters() {
		Map<Gewinnklasse, Gewinnklasseziehungquote> gklZieQuoProGkl = //
				zie.getGewinnklasseziehungquoteList()
						.stream()
						.collect(Collectors.toMap(gklZieQuo -> gklZieQuo.getGewinnklasse(),
								gklZieQuo -> gklZieQuo));

		int prozenteMal100FuerGlk1 = gewinnklassen.get(0)
				.getBetrag()
				.intValue();

		return getJackpotParam(zie, gklZieQuoProGkl, prozenteMal100FuerGlk1,
				ABZUG_EINSATZ_LOTTO, MAX_TIMES_JACKPOT, LOTTO);
	}

	/*
	 * Hier werden die Quoten fuer das Spiel 6 aus 49 berechnet. Der
	 * Jackpotanteil, falls vorhanden, wird noch NICHT der Quote hinzugefuegt.
	 * Aber es wird ein Jackpot record persistiert, falls es keine Gewinner in
	 * Gkl1 gibt.
	 */
	private void berechneLottoQuote(Map<Jackpot, boolean[]> jackpotParameters) {

		Gewinnklasse[] gklsLotto = getGewinnklassenLotto(gewinnklassen)
				.toArray(new Gewinnklasse[9]);

		final int[] prozenteMal100 = new int[9];
		for (int i = 0; i < prozenteMal100.length; i++) {
			prozenteMal100[i] = gewinnklassen.get(i)
					.getBetrag()
					.intValue();
		}

		long uebrigeVerteilung = einsatzLotto * ABZUG_EINSATZ_LOTTO / 100_00;

//	################ fuer Log ####################
		long gkl1abgezogen = uebrigeVerteilung * prozenteMal100[0] / 100_00;
		log.info("############ berechneLottoQuote #############");
		log.info(
				"(Prozentrechnung: uebrigeVerteilung * Prozent / 100 = AnteilGkl)");
		log.info((ABZUG_EINSATZ_LOTTO / 100) + "% von EinsatzLotto: " + einsatzLotto
				+ " * " + (ABZUG_EINSATZ_LOTTO / 100) + " / " + 100 + " = "
				+ (einsatzLotto * ABZUG_EINSATZ_LOTTO / 100_00));
//	###################################

//	Gkl1 berechnen + abziehen   und  Update GklZieQuo: quote
		uebrigeVerteilung = setBetragForGkl1(gklsLotto[0], prozenteMal100[0],
				uebrigeVerteilung, anzGewinnerProKlasse, jackpotParameters);

//	Gkl9 berechnen + abziehen
		Gewinnklasse gkl9 = gklsLotto[8];
		int anzGkl9 = anzGewinnerProKlasse.get(gkl9);
		uebrigeVerteilung = uebrigeVerteilung - (anzGkl9 * gkl9.getBetrag());
//	quote
		updateGewinnklasseziehungQuote(gkl9, gkl9.getBetrag());

//		########### ZwischenLog ##############
		log.info("anzGkl9 * gebuehr = gkl9abgezogen: " + anzGkl9 + " * "
				+ gkl9.getBetrag()
						.intValue()
				+ " = " + (anzGkl9 * gkl9.getBetrag()
						.intValue()));
		log.info(">> uebrigeVerteilungFuerGkl2-8: "
				+ (einsatzLotto * ABZUG_EINSATZ_LOTTO / 100_00) + " - " + gkl1abgezogen
				+ " - " + (anzGkl9 * gkl9.getBetrag()
						.intValue())
				+ " = " + uebrigeVerteilung);
		log.info("");
//		###################################

//		Gkl2-8 berechnen  und  Update GklZieQuo: anzahlgewinner + quote
		for (int i = 1; i < gklsLotto.length - 1; i++) {
			setBetragForGkl2_8(gklsLotto[i], prozenteMal100[i], uebrigeVerteilung,
					anzGewinnerProKlasse);
		}
	}

	/**
	 * @param jackpotsPlusParams: im Jackpot Object ist schon richtige Gkl
	 *                            enthalten!
	 */
	private void includeJackpotInQuote(
			Map<Jackpot, boolean[]> jackpotsPlusParams) {

		jackpotsPlusParams.forEach((jp, bools) -> {
			zie.getGewinnklasseziehungquoteList()
					.stream()
					.filter(g -> g.getGewinnklasse()
							.getGewinnklasseid()
							.equals(jp.getGewinnklasse()
									.getGewinnklasseid()))
					.filter(g -> anzGewinnerProKlasse.get(jp.getGewinnklasse()) > 0)
					.findAny()
					.ifPresent((Gewinnklasseziehungquote gklZieQuoForJackpot) -> {
						boolean is12times = bools[1];
						/*
						 * -is12times=0: Betragkumuliert hat schon Gesamtgewinn der SELBEN
						 * Gkl
						 * mit drin.
						 * -is12times=1: Falls von Gkl1 runter auf Gkl2-9 gesetzt wird
						 * muss erst der kumulierte Betrag vom LETZTEN Jackpot genommen
						 * werden und der aktuelle Gesamtgewinn der niederen Klasse addiert
						 * werden!
						 */
						long neuerGesamtGewinn = is12times
								? jp.getBetragkumuliert() - jp.getBetrag()
										+ (gklZieQuoForJackpot.getQuote() * anzGewinnerProKlasse
												.get(gklZieQuoForJackpot.getGewinnklasse()))
								: jp.getBetragkumuliert();
						long neueQuote = neuerGesamtGewinn / anzGewinnerProKlasse
								.get(gklZieQuoForJackpot.getGewinnklasse());

						log.fine("JackpotKumuliert: " + jp.getBetragkumuliert());
						log.info("");
						log.info("!!!!!!!!!! JACKPOT !!!!!!! fuer Gkl "
								+ gklZieQuoForJackpot.getGewinnklasse()
										.getGewinnklasseid()
								+ " >> GesamtBetrag mit Jackpot: " + neuerGesamtGewinn
								+ "   >> Quote pro Gewinner: " + neueQuote + " !!!!!");
						gklZieQuoForJackpot.setQuote(neueQuote);
						gklZieQuoForJackpot.setVersion(
								getUpdatedVersion(gklZieQuoForJackpot.getVersion()));
						gklZieQuoForJackpot.setLastmodified(jetzt);
						gewinnklasseziehungquoteFacade.edit(gklZieQuoForJackpot);
					});
		});
	}

	/*
	 * 
	 */
	private void updateLottoscheinZiehung6aus49() {

		List<Gewinnklasseziehungquote> gklZieQuos = zie
				.getGewinnklasseziehungquoteList();

		lzLottosOhneGkl9.forEach(lzLotto -> {
			Gewinnklasseziehungquote gklZieQuo = gklZieQuos.stream()
					.filter(g -> g.getGewinnklasse()
							.getGewinnklasseid()
							.equals(lzLotto.getGewinnklasse()
									.getGewinnklasseid())) // mit long vergleichen
					.findAny()
					.orElseThrow(() -> new NoSuchElementException(
							"invalid record in gewinnklasseziehungsquote or "
									+ "lottoscheinziehung6aus49...  lzLottoGklId: "
									+ lzLotto.getGewinnklasse()
											.getGewinnklasseid()
									+ "   gklZieQuosGklIds: " + gklZieQuos.stream()
											.map(g -> g.getGewinnklasse()
													.getGewinnklasseid())
											.collect(Collectors.toList())));

			lzLotto.setGewinn(gklZieQuo.getQuote());
			lzLotto.setVersion(getUpdatedVersion(lzLotto.getVersion()));
			lzLotto.setLastmodified(jetzt);
			lottoscheinziehung6aus49Facade.edit(lzLotto);
		});
	}

//	@formatter:off
//####################################################
// ################# Dimitri ######################
	/**
	 * @author Dimitri
	 * 
	 */
	private Map<Jackpot, boolean[]> getJackpotParam(Ziehung ziehung,
				Map<Gewinnklasse, Gewinnklasseziehungquote> gwklZquoteByGkl_Map, 
				long partToJackpot, long partToSpiel, // 50_00                                                                                                                                                                                                               // =50%
				int maxTimesJpot, String spielName) {
		
		log.info("######################## getJackpotParam ####################");
    Set<Gewinnklasse> gklSet = gwklZquoteByGkl_Map.keySet();

    // ============== Jackpot ENTWURF STEP 0 =================== parametrs
    // (ziehung,gwklZquoteByGkl_Map)

    // wightig: gwklZquoteByGkl_Map key gkl(22 mal), gwklZquote has: anzahl
    // gewinner, quote
    

    Gewinnklasse gwnkls1 = gwklZquoteByGkl_Map.keySet().stream().filter(gkl -> gkl.getGewinnklassenr() == 1)
                    .filter(gkl -> gkl.getSpiel().getName().equals(spielName))
                    .findAny().orElseThrow(()-> new IllegalArgumentException("invalid Gewinnklasse..."));

    List<Jackpot> jpList = jackpotFacade.findAll();
    // Last jackpot in Spiel 6 aus 49:
    Optional<Jackpot> lastJkpotBeforeZieh_opt = Optional.empty();
    if (!jpList.isEmpty()) {
            lastJkpotBeforeZieh_opt = jpList.stream()
              .filter(jp -> jp.getGewinnklasse().getSpiel().getName().equals(spielName))
              .filter(jp -> jp.getZiehung().getStatus()==1)   //TODO For Christian --- it is Important
              // would be good, if indexing and sorting in DB
              .filter(jp -> (jp.getZiehung().getZiehungsdatum().isBefore(ziehung.getZiehungsdatum())))                                                                                                                                                                                                                                 
              .max((jp1, jp2) -> jp1.getZiehung().getZiehungsdatum()
                              .compareTo(jp2.getZiehung().getZiehungsdatum()));
    }

    ////// find day of current "Ziehung" .START:
     log.info("Jackpot calculate for ZiehungsDate " + ziehung.getZiehungsdatum());

    ////// find day of last "Ziehung" .START:   
    LocalDate aktZieDate = ziehung.getZiehungsdatum();
    DayOfWeek dow = aktZieDate.getDayOfWeek();
    LocalDate lastZieDate;
    if(dow == DayOfWeek.WEDNESDAY) {
    	lastZieDate = aktZieDate.minusDays(4);
    }else if(dow == DayOfWeek.SATURDAY) {
    	lastZieDate = aktZieDate.minusDays(3) ;
    }else {
    	throw new IllegalArgumentException("invalid ziehungsDatum...");
    }
//    log.info("aktZiehDatum: " + aktZieDate + "    lastZiehDatum: " + lastZieDate);
////// find day of last "Ziehung" <<<< END

//    System.out.println(" ============== Jackpot ENTWURF STEP 1 ===================");
    long jackPotGewinn = 0L; // TODO 1) JPQL Metode, SELECT last Jackpot649 of Jackpots before Ziehung
    long commulJPotGewinn = jackPotGewinn; // <--will be actualize with Methode
    // if jackPotGewinn.date is not firstZiehung day before ziehung =then:
    // ==>Jackpot.anzahlziehungen=1 and commulJPotGewinn=einzatz649*12,8%,
    // jackPotGewinn==einzatz649*12,8%

    // else if jackPotGwn6_49.date is firstZiehung day before ziehung and
    // Jackpot.anzahlziehungen<11? =>then:
    // ==> jackPotGewinn= from JPQL "1)" TODO new Jackpot.anzahlziehungen+=1, if no
    // gkl1:
    // ==> jackPotGewinn=einzatz649*12,8% +jackPotGwn6_49
    // else if jackPotGwn6_49.date is firstZiehung day before ziehung and
    // Jackpot.anzahlziehungen==11 (without Gkl-1)? =>then:
    // ==> boolean is12Wochen6_49=>true;
    // ==> No record in Jackpot Table
    // ==> commulJPotGwn6_49=einzatz649*12,8% +jackPotGwn6_49



    boolean is12TimeSep = false; // <-- is Time to separate (in 12 Time)

    // Preparation Jackpot. "Templ" is "Tamplate"
    // ==================
    LocalDateTime jpTemplDate = LocalDateTime.now();
    Jackpot jackpotToPersist = new Jackpot();
    jackpotToPersist.setCreated(jpTemplDate);
    jackpotToPersist.setZiehung(ziehung);
    jackpotToPersist.setVersion(1);
    jackpotToPersist.setLastmodified(jpTemplDate);

    if (spielName.equals(LOTTO)) {
        if (gwnkls1.getIsabsolut()) {
            jackPotGewinn = gwnkls1.getBetrag();
            commulJPotGewinn = jackPotGewinn;
        } else {
            log.info("ziehung.getEinsatzlotto()= " + ziehung.getEinsatzlotto()+", part to spiel partToSpiel/1 "+partToSpiel);
            
            jackPotGewinn = (((ziehung.getEinsatzlotto() * partToSpiel * partToJackpot) / 10000))
                            / 10000;
            commulJPotGewinn = jackPotGewinn;
        }
    }

    if (spielName.equals(SPIEL77)) {
        if (gwnkls1.getIsabsolut()) {
            jackPotGewinn = gwnkls1.getBetrag();
            commulJPotGewinn = jackPotGewinn;
        } else {
            jackPotGewinn = (ziehung.getEinsatzspiel77() * partToSpiel / 10000 * partToJackpot) / 10000;
            commulJPotGewinn = jackPotGewinn;
        }
    }
    // Step1, choose Type of Jackpot
    // -------------------------
    // was no records
    final String commulateGewString = " === !lastJkpotBeforeZieh_opt.isPresent())==>jackPotGewinn, commulJPotGewinn ";
    if (!lastJkpotBeforeZieh_opt.isPresent()) {
        // first record in Table

        
        commulJPotGewinn = jackPotGewinn;
        jackpotToPersist.setAnzahlziehungen(1);
        jackpotToPersist.setBetrag(jackPotGewinn);
        jackpotToPersist.setBetragkumuliert(commulJPotGewinn);
        is12TimeSep = false;
        
        log.info(commulateGewString + jackPotGewinn+", "+commulJPotGewinn);
    }

    // If date is old (Jackpot is won before) -> we must have new Jackpot wit Nr 1
    // from "anzahlziehungen"
    else if (lastJkpotBeforeZieh_opt.isPresent()
                && (lastJkpotBeforeZieh_opt.get().getZiehung().getZiehungsdatum().isBefore(lastZieDate))) {
        // Someone had won before
        
        commulJPotGewinn = jackPotGewinn;
        jackpotToPersist.setAnzahlziehungen(1);
        jackpotToPersist.setBetrag(jackPotGewinn);
        jackpotToPersist.setBetragkumuliert(commulJPotGewinn);
        is12TimeSep = false;
        log.info(commulateGewString + jackPotGewinn+", "+commulJPotGewinn);
    }
    // If Standard
    else if (lastJkpotBeforeZieh_opt.isPresent()
        && lastJkpotBeforeZieh_opt.get().getAnzahlziehungen() < (maxTimesJpot - 1)
        && lastJkpotBeforeZieh_opt.get().getZiehung().getZiehungsdatum().isBefore(aktZieDate)
        && lastJkpotBeforeZieh_opt.get().getZiehung().getZiehungsdatum().compareTo(lastZieDate) >= 0) {
        
        if (gwnkls1.getIsabsolut()) {
            commulJPotGewinn = lastJkpotBeforeZieh_opt.get().getBetragkumuliert() + jackPotGewinn;
        }else {
		        commulJPotGewinn = lastJkpotBeforeZieh_opt.get().getBetragkumuliert() + jackPotGewinn;}
		        jackpotToPersist.setBetrag(jackPotGewinn);
		        jackpotToPersist.setBetragkumuliert(commulJPotGewinn);
		        jackpotToPersist.setAnzahlziehungen(lastJkpotBeforeZieh_opt.get().getAnzahlziehungen() + 1);
		        is12TimeSep = false;
		        log.info(commulateGewString + jackPotGewinn+", "+commulJPotGewinn);
    }
    // If it will be the 12th Time; We must divide(separate) Jackpot between lower
    // players
    else if (lastJkpotBeforeZieh_opt.isPresent()
            && lastJkpotBeforeZieh_opt.get().getAnzahlziehungen() == (maxTimesJpot - 1)) {

        if (gwnkls1.getIsabsolut()) {
                commulJPotGewinn = jackPotGewinn;
        }else {
        commulJPotGewinn = lastJkpotBeforeZieh_opt.get().getBetragkumuliert() + jackPotGewinn;}
        jackpotToPersist.setBetrag(jackPotGewinn);
        jackpotToPersist.setBetragkumuliert(commulJPotGewinn);
        jackpotToPersist.setAnzahlziehungen(lastJkpotBeforeZieh_opt.get().getAnzahlziehungen() + 1);
        is12TimeSep = true;
        log.info(commulateGewString + jackPotGewinn+", "+commulJPotGewinn);
    };
    // ============== ===================
    //                 ============== Jackpot ENTWURF STEP 2 Find JackpotGkl===================";

    // Set<Gewinnklasse> gklSet = gwklZquoteByGkl_Map.keySet() =>>
    gklSet = gwklZquoteByGkl_Map.keySet();
    
    ///////////////////////////
    Optional<Gewinnklasse> gkl1Winners_any = gklSet.stream()
        .filter(gkl -> gkl.getSpiel().getName().equals(spielName)).filter(gkl -> gkl.getGewinnklassenr() == 1)
        .filter(gkl -> gwklZquoteByGkl_Map.get(gkl).getAnzahlgewinner() > 0).findAny();

    // if (isGkl1WinnerInJkpot)==true ->NOT Persistence Jackpot6_49
    boolean isGkl1WinnerInJkpot = gkl1Winners_any.isPresent();

    
    Optional<Gewinnklasse> gklJackPotLow = gklSet.stream()
        .filter(gkl -> gkl.getSpiel().getName().equals(spielName))
        .filter(gkl -> gwklZquoteByGkl_Map.get(gkl).getAnzahlgewinner() > 0)
        .filter(gkl -> gkl.getGewinnklassenr() > 1)
        .min((gkl1, gkl2) -> Integer.valueOf(gkl1.getGewinnklassenr())
        .compareTo(Integer.valueOf(gkl2.getGewinnklassenr())));

    /// if we must separate jackPot (is12WeekSep6_49==true) or (is12WeekSep77==true)
    // if true ->NOT Persistence Jackpot6_49.anzahlziehungen===>0

    Gewinnklasse gklJackPotLowKlasse = (gklJackPotLow.isPresent()) ? gklJackPotLow.get() : null;

    ///////// Who is Jkpot Winner?
    jackpotToPersist.setGewinnklasse(isGkl1WinnerInJkpot ? gkl1Winners_any.get()
                    : (gklJackPotLowKlasse != null ? gklJackPotLowKlasse : gwnkls1));

    // ----------------------------------
    // ---------------------
    // RESULT return Map <Jackpot,array[boolean,boolean]>
    // array(0) is boolean isGkl1WinnerInJkpot, arrayt(1) is boolean is12TimeSep
    boolean[] boolOptArr = { isGkl1WinnerInJkpot, is12TimeSep };

    Map<Jackpot, boolean[]> resMap = new HashMap<>();
    resMap.put(jackpotToPersist, boolOptArr);

    return resMap;
  	};
//    @formatter:on

//#############################################################
//############## Helper Methods ################################

	private long setBetragForGkl1(Gewinnklasse gkl, int prozenteMal100,
			long uebrigeVerteilung, Map<Gewinnklasse, Integer> anzGewinnerProKla,
			Map<Jackpot, boolean[]> jackpotParameters) {

		// /10_000 zum Schluss, sonst wird uebrigeVerteilung "beschnitten"
		long gklAnteil = uebrigeVerteilung * prozenteMal100 / 10_000;
		int anzGew = anzGewinnerProKla.get(gkl);
		long quoteGkl = 0;

		if (anzGew != 0) {
			quoteGkl = gklAnteil / anzGew;
		} else { // keine Gewinner in Gkl1
			Jackpot jackpot = jackpotParameters.keySet()
					.stream()
//				.filter(jp -> jp.getGewinnklasseid() // besser? : Map<Gkl, Jackpot>
//					.getGewinnklasseid() == 1)
					.findFirst()
					.orElseThrow(
							() -> new NoSuchElementException("no Jackpot Object found..."));
			boolean is12TimesSep = jackpotParameters.get(jackpot)[1];

			if (is12TimesSep) {
//			keinen Jackpot record erstellen
			} else {
// im Jackpot steht falsche Gkl! (gklJackPotLowKlasse)
				jackpot.setGewinnklasse(gkl);
				jackpotFacade.create(jackpot);
			}
		}
		// quote
		updateGewinnklasseziehungQuote(gkl, quoteGkl);

		log.info("Gkl " + gkl.getGewinnklassenr() + ": " + (prozenteMal100 / 100.0)
				+ "% von " + uebrigeVerteilung + ": " + gklAnteil
				+ "   >> quote =  gklAnteil / anzGew(bei 0>>Jackpot):  " + gklAnteil
				+ " / " + anzGew + " = " + quoteGkl);

		// muss hinter Log sein, sonst zeigt Log falsche uebrigeVerteilung
		if (gkl.getGewinnklassenr() == 1)

		{
			uebrigeVerteilung -= gklAnteil;
		}

		return uebrigeVerteilung; // fuer Gkl1
	}

	private void setBetragForGkl2_8(Gewinnklasse gkl, int prozenteMal100,
			long uebrigeVerteilung, Map<Gewinnklasse, Integer> anzGewinnerProKla) {

// /10_000 zum Schluss, sonst wird uebrigeVerteilung "beschnitten"
		long gklAnteil = uebrigeVerteilung * prozenteMal100 / 10_000;

		int anzGew = anzGewinnerProKla.get(gkl);
		long quoteGkl = 0;
		if (anzGew != 0) {
			quoteGkl = gklAnteil / anzGew;
		}
		updateGewinnklasseziehungQuote(gkl, quoteGkl);

		log.info("Gkl " + gkl.getGewinnklassenr() + ": " + (prozenteMal100 / 100.0)
				+ "% von " + uebrigeVerteilung + ": " + gklAnteil
				+ "   >> quote =  gklAnteil / anzGew(bei 0>>Jackpot):  " + gklAnteil
				+ " / " + anzGew + " = " + quoteGkl);
	}

	private Optional<Gewinnklasse> findGklForScheinZie(String spielName,
			int computedKlssNumber, List<Gewinnklasse> gwnClassesForZhng) {
		return gwnClassesForZhng.stream()
				.filter(g -> g.getSpiel()
						.getName()
						.equals(spielName))
				.filter(g -> g.getGewinnklassenr() == computedKlssNumber)
				.findAny();
	}

	private int getUpdatedVersion(Integer version) {
		Optional<Integer> versionOpt = Optional.ofNullable(version);
		return versionOpt.isPresent() ? versionOpt.get() + 1 : 1;
	}

	private Map<Integer, Integer> getMapKeyAsGewinnklassenr(String spielX,
			Map<Gewinnklasse, Integer> anzGewinnerProKlaSpielX) {
		Map<Integer, Integer> anzGewProKlaSpielX = anzGewinnerProKlaSpielX
				.entrySet()
				.stream()
				.filter(e -> e.getKey()
						.getSpiel()
						.getName()
						.equals(spielX))
				.collect(Collectors.toMap(e -> e.getKey()
						.getGewinnklassenr(), e -> e.getValue()));
		return anzGewProKlaSpielX;
	}

	private List<Gewinnklasse> getGewinnklassenLotto(
			List<Gewinnklasse> gewinnklassen) {
		return gewinnklassen.stream()
				.filter(g -> g.getSpiel()
						.getName()
						.equals(LOTTO))
//				.sorted((g1, g2) -> Long.compare(g1.getGewinnklasseid(),
//					g2.getGewinnklasseid()))
				.collect(Collectors.toList());
	}

// quote
	private void updateGewinnklasseziehungQuote(Gewinnklasse gkl, long quoteGkl) {
		Gewinnklasseziehungquote gklZieQuo = gewinnklasseziehungquoteFacade
				.findAll()
				.stream()
				.filter(g -> g.getGewinnklasse()
						.getGewinnklasseid()
						.equals(gkl.getGewinnklasseid()))
//	Fehlersuche erfolgreich!
				.filter(g -> g.getZiehung() // gklZieQuo zur richtigen Ziehung!
						.getZiehungid()
						.equals(zie.getZiehungid()))
				.findAny()
				.orElseThrow(() -> new NoSuchElementException(
						"no valid record in gewinnklasseziehungquote..."));

		gklZieQuo.setVersion(getUpdatedVersion(gklZieQuo.getVersion()));
		gklZieQuo.setLastmodified(jetzt);

		gklZieQuo.setQuote(quoteGkl);
		log.fine("GklZieQuo " + gklZieQuo.getGewinnklasseziehungquoteid()
				+ " UpdatedQuote: " + quoteGkl);

		gewinnklasseziehungquoteFacade.edit(gklZieQuo);
	}

	private long setBetragForGkl1ForSpiel77(int spiel77Gkl1AnteilInProzent) {
		return (einsatzSpiel77 * ABZUG_EINSATZ_SPIELL77 / 100_00)
				* spiel77Gkl1AnteilInProzent / 100;
	}

	private void resetFields() {
		anzSpiel77.set(0);
		anzSuper6.set(0);
		summeAnzahlTipps.set(0);
		lzLottosOhneGkl9.removeAll(lzLottosOhneGkl9);
		// V1.1
		lzSpiel77Gkl1.removeAll(lzSpiel77Gkl1);
		// mit anzGewinner=0 initialisieren
		anzGewinnerProKlasse = gewinnklassen.stream()
				.collect(Collectors.toConcurrentMap(gkl -> gkl, gkl -> 0));
		jetzt = LocalDateTime.now();
	}
}
