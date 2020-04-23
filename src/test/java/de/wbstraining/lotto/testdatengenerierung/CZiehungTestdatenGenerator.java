/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.testdatengenerierung;

import java.math.BigInteger;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import de.wbstraining.lotto.persistence.dao.KundeFacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinFacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinziehungFacadeLocal;
import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Ziehung;
import de.wbstraining.lotto.util.LottoDatumUtil;

/**
 *
 * @author gz1
 */

@Stateless
@PermitAll
public class CZiehungTestdatenGenerator
	implements CZiehungTestdatenGeneratorLocal {

	@EJB
	private KundeFacadeLocal kundeFacadeLocal;

	@EJB
	private LottoscheinFacadeLocal lottoscheinFacadeLocal;

	@EJB
	private LottoscheinziehungFacadeLocal lottoscheinziehungFacadeLocal;

	@EJB
	private ZiehungFacadeLocal ziehungFacadeLocal;

	private int blockSize;
	private AtomicLong belegNummer;
	private List<Kunde> kunden;
	private static final Logger logger = Logger
		.getLogger("wbs.corejpa.testdatengenerierung");

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private void setInitialValues(int blocksize, long belegnummernStart) {
		this.blockSize = blocksize;
		belegNummer = new AtomicLong(belegnummernStart);
		kunden = kundeFacadeLocal.findAll();

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private long nextBelegnummer() {
		return belegNummer.getAndIncrement();
	}

	private void writeLog(String s) {
		logger.log(Level.INFO, s);
	}

	// @TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void generateSchein(Date abgabeDatum, Kunde kunde, int losnummer,
		byte[] tipps, int kosten, boolean isMittwoch, boolean isSamstag,
		long belegnummer, Ziehung ziehung, boolean isSpiel77, boolean isSuper6) {
		Lottoschein schein = new Lottoschein();
		LocalDateTime date = LocalDateTime.now();

		schein.setCreated(date);
		schein.setLastmodified(date);
		schein.setAbgabedatum(abgabeDatum);
		schein.setKundeid(kunde);
		schein.setIsabgeschlossen(Boolean.FALSE);
		schein.setIsmittwoch(isMittwoch);
		schein.setIssamstag(isSamstag);
		schein.setIsspiel77(isSpiel77);
		schein.setIssuper6(isSuper6);
		schein.setLosnummer(losnummer);
		schein.setTipps(tipps);
		schein.setKosten(kosten);
		schein.setBelegnummer(BigInteger.valueOf(belegnummer));
		schein.setLaufzeit(1);
		// schein.setIsspiel77(isSpiel77);
		// schein.setIssuper6(isSuper6);
		lottoscheinFacadeLocal.create(schein);

		Lottoscheinziehung lottoscheinziehung = new Lottoscheinziehung();
		lottoscheinziehung.setCreated(date);
		lottoscheinziehung.setLastmodified(date);
		lottoscheinziehung.setIsabgeschlossen(false);
		lottoscheinziehung.setIsletzteziehung(true);
		lottoscheinziehung.setZiehungid(ziehung);
		lottoscheinziehung.setZiehungnr(1);
		lottoscheinziehung.setLottoscheinid(schein);
		lottoscheinziehung.setGewinnklasseidspiel77(null);
		lottoscheinziehung.setGewinnklasseidsuper6(null);
		lottoscheinziehungFacadeLocal.create(lottoscheinziehung);
	}

	// @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private void generateScheine6Aus49(CZiehung config, Ziehung ziehung,
		AtomicLong belegNr) {
		int anzahlBloecke;
		int anzahlRest;
		Anzahl6Aus49ProGkl anzahl6Aus49ProGkl = config.getAnzahl6Aus49ProGkl();
		writeLog("generateScheine6Aus49");
		for (int gkl = 0; gkl < anzahl6Aus49ProGkl.getAnzahl()
			.size(); gkl++) {
			writeLog("gkl: " + gkl);
			anzahlBloecke = anzahl6Aus49ProGkl.getAnzahl()
				.get(gkl) / blockSize;
			anzahlRest = anzahl6Aus49ProGkl.getAnzahl()
				.get(gkl) % blockSize;
			for (int i = 0; i < anzahlBloecke; i++) {
				writeLog("block: " + i);
				generateScheine6Aus49Block(gkl, config, ziehung, blockSize, belegNr);
			}
			writeLog("rest: " + anzahlRest);
			generateScheine6Aus49Block(gkl, config, ziehung, anzahlRest, belegNr);
		}
	}

	// @TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void generateScheine6Aus49Block(int gkl, CZiehung config,
		Ziehung ziehung, int anzahl, AtomicLong belegNr) {
		int losnummer = TestdatenGeneratorUtil.generateLosnummer6Aus49(
			config.getSuperzahl(), config.getSpiel77(), config.getSuper6(), gkl);
		byte[] tipps = TestdatenGeneratorUtil.generateTippsFuerEinenSchein(
			config.getZahlenAlsBits(), gkl, config.getAnzahlTippsProSchein());
		int kosten = 0;
		boolean isMittwoch = TestdatenGeneratorUtil
			.isMittwoch(ziehung.getZiehungsdatum());
		boolean isSamstag = TestdatenGeneratorUtil
			.isSamstag(ziehung.getZiehungsdatum());
		int anzahlKunden = kunden.size();
		Date abgabeDatum = ziehung.getZiehungsdatum();
		int kdnr;
		for (int i = 0; i < anzahl; i++) {
			kdnr = (int) (Math.random() * anzahlKunden);
			generateSchein(abgabeDatum, kunden.get(kdnr), losnummer, tipps, kosten,
				isMittwoch, isSamstag, belegNr.getAndIncrement(), ziehung, false,
				false);
		}
	}

	// @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private void generateScheineSpiel77(CZiehung config, Ziehung ziehung,
		AtomicLong belegNr) {
		int anzahlBloecke;
		int anzahlRest;
		AnzahlSpiel77ProGkl anzahlSpiel77ProGkl = config.getAnzahlSpiel77ProGkl();
		int[] losnummernSpiel77 = TestdatenGeneratorUtil
			.generateLosnummernSpiel77(config.getSpiel77());
		writeLog("generateScheineSpiel77");
		for (int gkl = 0; gkl < anzahlSpiel77ProGkl.getAnzahl()
			.size(); gkl++) {
			writeLog("gkl: " + gkl);
			anzahlBloecke = anzahlSpiel77ProGkl.getAnzahl()
				.get(gkl) / blockSize;
			anzahlRest = anzahlSpiel77ProGkl.getAnzahl()
				.get(gkl) % blockSize;
			for (int i = 0; i < anzahlBloecke; i++) {
				writeLog("block: " + i);
				generateScheineSpiel77Block(losnummernSpiel77[gkl], config, ziehung,
					blockSize, belegNr);
			}
			writeLog("rest: " + anzahlRest);
			generateScheineSpiel77Block(losnummernSpiel77[gkl], config, ziehung,
				anzahlRest, belegNr);
		}
	}

	// @TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void generateScheineSpiel77Block(int losnummer, CZiehung config,
		Ziehung ziehung, int anzahl, AtomicLong belegNr) {
		byte[] tipps = TestdatenGeneratorUtil.generateTippsFuerEinenSchein(
			config.getZahlenAlsBits(), 0, config.getAnzahlTippsProSchein());
		int kosten = 0;
		boolean isMittwoch = TestdatenGeneratorUtil
			.isMittwoch(ziehung.getZiehungsdatum());
		boolean isSamstag = TestdatenGeneratorUtil
			.isSamstag(ziehung.getZiehungsdatum());
		int anzahlKunden = kunden.size();
		Date abgabeDatum = ziehung.getZiehungsdatum();
		int kdnr;
		for (int i = 0; i < anzahl; i++) {
			kdnr = (int) (Math.random() * anzahlKunden);
			generateSchein(abgabeDatum, kunden.get(kdnr), losnummer, tipps, kosten,
				isMittwoch, isSamstag, belegNr.getAndIncrement(), ziehung, true, false);
		}

	}

	// @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private void generateScheineSuper6(CZiehung config, Ziehung ziehung,
		AtomicLong belegNr) {
		int anzahlBloecke;
		int anzahlRest;
		AnzahlSuper6ProGkl anzahlSuper6ProGkl = config.getAnzahlSuper6ProGkl();
		int[] losnummernSuper6 = TestdatenGeneratorUtil
			.generateLosnummernSuper6(config.getSuper6());
		writeLog("generateScheineSuper6");
		for (int gkl = 0; gkl < anzahlSuper6ProGkl.getAnzahl()
			.size(); gkl++) {
			writeLog("gkl: " + gkl);
			anzahlBloecke = anzahlSuper6ProGkl.getAnzahl()
				.get(gkl) / blockSize;
			anzahlRest = anzahlSuper6ProGkl.getAnzahl()
				.get(gkl) % blockSize;
			for (int i = 0; i < anzahlBloecke; i++) {
				writeLog("block: " + i);
				generateScheineSuper6Block(losnummernSuper6[gkl], config, ziehung,
					blockSize, belegNr);
			}
			writeLog("rest: " + anzahlRest);
			generateScheineSuper6Block(losnummernSuper6[gkl], config, ziehung,
				anzahlRest, belegNr);
		}
	}

	// @TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void generateScheineSuper6Block(int losnummer, CZiehung config,
		Ziehung ziehung, int anzahl, AtomicLong belegNr) {
		byte[] tipps = TestdatenGeneratorUtil.generateTippsFuerEinenSchein(
			config.getZahlenAlsBits(), 0, config.getAnzahlTippsProSchein());
		int kosten = 0;
		boolean isMittwoch = TestdatenGeneratorUtil
			.isMittwoch(ziehung.getZiehungsdatum());
		boolean isSamstag = TestdatenGeneratorUtil
			.isSamstag(ziehung.getZiehungsdatum());
		int anzahlKunden = kunden.size();
		Date abgabeDatum = ziehung.getZiehungsdatum();
		int kdnr;
		for (int i = 0; i < anzahl; i++) {
			kdnr = (int) (Math.random() * anzahlKunden);
			generateSchein(abgabeDatum, kunden.get(kdnr), losnummer, tipps, kosten,
				isMittwoch, isSamstag, belegNr.getAndIncrement(), ziehung, false, true);
		}

	}

	// @TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Ziehung createZiehung(CZiehung config, Date ziehungsDatum) {
		Ziehung ziehung = new Ziehung();
		LocalDateTime date = LocalDateTime.now();

		ziehung.setCreated(date);
		ziehung.setLastmodified(date);
		ziehung.setZiehungsdatum(ziehungsDatum);
		ziehung.setZahlenalsbits(BigInteger.valueOf(config.getZahlenAlsBits()));
		ziehung.setSpiel77(config.getSpiel77());
		ziehung.setSuper6(config.getSuper6());
		ziehung.setSuperzahl(config.getSuperzahl());
		ziehung.setEinsatzlotto(BigInteger.ZERO);
		ziehung.setEinsatzspiel77(BigInteger.ZERO);
		ziehung.setEinsatzsuper6(BigInteger.ZERO);
		ziehung.setStatus(1);
		ziehungFacadeLocal.create(ziehung);
		return ziehung;
	}

	// generierung von testdaten für eine ziehung
	// @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private void generiereTestDatenFuerEineZiehung(CZiehung config, Date datum,
		AtomicLong belegNr) {
		Ziehung ziehung = createZiehung(config, datum);
		writeLog("ziehungId: " + ziehung.getZiehungid());
		writeLog("datum: " + DateFormat.getInstance()
			.format(ziehung.getZiehungsdatum()));
		generateScheine6Aus49(config, ziehung, belegNr);
		generateScheineSpiel77(config, ziehung, belegNr);
		generateScheineSuper6(config, ziehung, belegNr);
	}

	// @Asynchronous
	// @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public void generiereTestDatenFuerMehrereZiehungen(
		Testdatengenerator generator) {
		long belegNummernStart = generator.belegnummernStart;
		AtomicLong belegNr = new AtomicLong(belegNummernStart);
		int blockSize = generator.getTxBlocksize();
		Date datumErsteZiehung = generator.datumErsteZiehung.toGregorianCalendar()
			.getTime();
		List<CZiehung> configList = generator.getCZiehung();
		setInitialValues(blockSize, belegNummernStart);
		List<Date> ziehungsTage = LottoDatumUtil.ziehungsTage(datumErsteZiehung,
			true, true, 18, 19, generator.getCZiehung()
				.size());

		int i = 0;
		writeLog(
			"start generierung der ziehungen gemaess testdatengenerator.xml...");
		writeLog("anzahl ziehungen: " + configList.size());
		writeLog("txBlocksize: " + generator.getTxBlocksize());
		for (CZiehung config : configList) {
			generiereTestDatenFuerEineZiehung(config, ziehungsTage.get(i), belegNr);
			i++;
		}
		writeLog("end generierung der ziehungen...");
	}
}
