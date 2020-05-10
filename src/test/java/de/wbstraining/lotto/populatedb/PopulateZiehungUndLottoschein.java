package de.wbstraining.lotto.populatedb;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.wbstraining.lotto.persistence.dao.KundeFacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinFacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinziehungFacadeLocal;
import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Ziehung;
import de.wbstraining.lotto.util.LottoDatum8Util;
import de.wbstraining.lotto.util.LottoUtil;

@Stateless
public class PopulateZiehungUndLottoschein
	implements PopulateZiehungUndLottoscheinLocal {

	// injections

	@EJB
	private ZiehungFacadeLocal ziehungFacade;

	@EJB
	private LottoscheinziehungFacadeLocal lottoscheinziehungFacade;

	@EJB
	private LottoscheinFacadeLocal lottoscheinFacade;

	@EJB
	private KundeFacadeLocal kundeFacade;

	// cache

	private Map<LocalDate, Ziehung> ziehungenByDate = new TreeMap<>();
	private List<Kunde> kunden;

	// other
	private Random random = new Random();
	int[] laufzeiten = { 1, 2, 3, 4, 5, 8 };

	@PostConstruct
	private void loadKunden() {
		kunden = kundeFacade.findAll();
	}

	@Override
	public void populateZiehungUndLottoschein() {

		// ziehungen erzeugen und cache aufbauen
		List<LocalDate> dates = LottoDatum8Util.ziehungsTage(LocalDate.now(),
			LocalTime.now(), true, true, 18, 19, 20);
		Ziehung ziehung;
		Kunde kunde;
		Lottoschein lottoschein;

		int anzahlZiehungen = 10;
		int anzahlLottoscheineProZiehung = 20;

		for (LocalDate ziehungsDatum : dates) {
			ziehung = createZiehung(ziehungsDatum);
			ziehungenByDate.put(ziehungsDatum, ziehung);
		}

		List<Ziehung> ziehungen = new ArrayList<>();
		ziehungenByDate.forEach((d, z) -> {
			ziehungen.add(z);
		});

		for (int n = 0; n < anzahlZiehungen; n++) {
			for (int m = 0; m < anzahlLottoscheineProZiehung; m++) {
				kunde = randomKunde();
				lottoschein = createLottoschein(kunde, ziehungen.get(n)
					.getZiehungsdatum());
				lottoscheinEinreichen(lottoschein);
			}
		}
	}

	private Kunde randomKunde() {
		return kunden.get(random.nextInt(kunden.size()));
	}

	private Ziehung createZiehung(LocalDate ziehungsDatum) {

		LocalDateTime aktuellesDatum = LocalDateTime.now();
		Ziehung ziehung = new Ziehung();

		ziehung.setEinsatzlotto(BigInteger.ZERO);
		ziehung.setEinsatzspiel77(BigInteger.ZERO);
		ziehung.setEinsatzsuper6(BigInteger.ZERO);
		ziehung.setSpiel77(0);
		ziehung.setSuper6(0);
		ziehung.setSuperzahl(0);
		ziehung.setZahlenalsbits(BigInteger.ZERO);
		ziehung.setStatus(0);
		ziehung.setZiehungsdatum(ziehungsDatum);
		ziehung.setCreated(aktuellesDatum);
		ziehung.setLastmodified(aktuellesDatum);
		ziehungFacade.create(ziehung);
		return ziehung;
	}

	private Lottoschein createLottoschein(Kunde kunde, LocalDate abgabeDatum) {
		Lottoschein schein = new Lottoschein();
		LocalDateTime datum = LocalDateTime.now();
		boolean isMittwoch = random.nextBoolean();
		schein.setKunde(kunde);
		schein.setAbgabedatum(abgabeDatum.atStartOfDay());
		schein.setBelegnummer(
			BigInteger.valueOf((long) (Math.random() * 1_000_000_000)));
		schein.setCreated(datum);
		schein.setLastmodified(datum);
		schein.setIsabgeschlossen(Boolean.FALSE);
		schein.setIsspiel77(random.nextBoolean());
		schein.setIssuper6(random.nextBoolean());
		schein.setLaufzeit(laufzeiten[random.nextInt(laufzeiten.length)]);
		schein.setKosten(0);
		schein.setLosnummer(10_000_000 + ThreadLocalRandom.current()
			.nextInt(90_000_000));
		schein.setIsmittwoch(isMittwoch);
		schein.setIssamstag(!isMittwoch ? true : random.nextBoolean());
		schein.setTipps(LottoUtil.randomTippsAsByteArray(ThreadLocalRandom.current()
			.nextInt(12) + 1));
		return schein;
	}

	private void lottoscheinEinreichen(Lottoschein schein) {

		Ziehung ziehung;
		Lottoscheinziehung lottoscheinziehung;
		LocalDateTime datum = LocalDateTime.now();
		List<LocalDate> dateList;
		lottoscheinFacade.create(schein);
		LocalDateTime abgabeDatum = schein.getAbgabedatum();

		dateList = LottoDatum8Util.ziehungsTage(abgabeDatum.toLocalDate(),
			abgabeDatum.toLocalTime(), schein.getIsmittwoch(), schein.getIssamstag(),
			18, 19, schein.getLaufzeit());
		int nr = 1;
		for (LocalDate date : dateList) {
			ziehung = ziehungenByDate.get(date);
			lottoscheinziehung = new Lottoscheinziehung();
			lottoscheinziehung.setLottoschein(schein);
			lottoscheinziehung.setZiehungnr(nr);
			lottoscheinziehung.setZiehung(ziehung);
			lottoscheinziehung.setGewinnklasseidspiel77(null);
			lottoscheinziehung.setGewinnklasseidsuper6(null);
			lottoscheinziehung.setIsabgeschlossen(false);
			lottoscheinziehung.setIsletzteziehung(nr == (dateList.size()));
			lottoscheinziehung.setCreated(datum);
			lottoscheinziehung.setLastmodified(datum);
			lottoscheinziehungFacade.create(lottoscheinziehung);
			nr++;
		}
	}

}
