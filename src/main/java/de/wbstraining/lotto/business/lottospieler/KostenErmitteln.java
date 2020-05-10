package de.wbstraining.lotto.business.lottospieler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.wbstraining.lotto.dto.KostenDetailedDto;
import de.wbstraining.lotto.dto.KostenDto;
import de.wbstraining.lotto.persistence.dao.GebuehrFacadeLocal;
import de.wbstraining.lotto.persistence.model.Gebuehr;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.util.ByteLongConverter;
import de.wbstraining.lotto.util.LottoDatum8Util;
import de.wbstraining.lotto.util.LottoUtil;

/**
 * Session Bean implementation class KostenErmitteln
 */

// vom christian, martin, dimitri
@Stateless
public class KostenErmitteln implements KostenErmittelnLocal {

	final int ABGABESCHLUSSMITTWOCH = 18;
	final int ABGABESCHLUSSSAMSTAG = 18;

	private static final Logger log = Logger
		.getLogger("wbs.business.KostenErmitteln");
	{
//                log.setLevel(Level.INFO);
		log.setLevel(Level.OFF);
	}

//        @EJB
//        private DBCacheLocal dBCache;

	@EJB
	private GebuehrFacadeLocal gebuehrFacade;

//=============================================
//Bussiness Logic Methods

	@Override
	public int kostenErmitteln(Lottoschein schein) {

		LocalDateTime abgabeZeitpunkt = schein.getAbgabezeitpunkt();
		List<LocalDate> scheinDatums = LottoDatum8Util.ziehungsTage(
			abgabeZeitpunkt.toLocalDate(), abgabeZeitpunkt.toLocalTime(),
			schein.getIsmittwoch(), schein.getIssamstag(), ABGABESCHLUSSMITTWOCH,
			ABGABESCHLUSSSAMSTAG, schein.getLaufzeit());

		List<Gebuehr> gebuehren = gebuehrFacade.findAll();

//  1long = 1tipp
		final int anzTipps = ByteLongConverter.byteToLong(schein.getTipps()).length;
		List<Integer> kostenProZiehungsTag = new ArrayList<Integer>();

		scheinDatums.stream()
			.forEach(spieltag -> {

				Gebuehr g = findGebuehrForSpielTag(gebuehren, spieltag);

				final int einsatzProTipp = g.getEinsatzprotipp();

				final int spiel77 = //
					schein.getIsspiel77() == true ? g.getEinsatzspiel77() : 0;
				final int super6 = //
					schein.getIssuper6() == true ? g.getEinsatzsuper6() : 0;

//   Grundgebuehr einmalig hinzufuegen (Gebuehr fuer Ersten Spieltag)
				if (kostenProZiehungsTag.size() == 0) {
					kostenProZiehungsTag.add(g.getGrundgebuehr());
				}
				kostenProZiehungsTag.add(einsatzProTipp * anzTipps + spiel77 + super6);
			});

		final int mi = schein.getIsmittwoch() == true ? 1 : 0;
		final int sa = schein.getIssamstag() == true ? 1 : 0;
		final int miSa = mi + sa;

		log.log(Level.INFO,
			"KostenProZiehungsTag (inkl Grundgebuehr): " + kostenProZiehungsTag + "\n"
				+ "anzTipps: " + anzTipps + "  Spiel77: " + schein.getIsspiel77()
				+ "  super6: " + schein.getIssuper6() + "  miSa: " + miSa
				+ "  laufzeit: " + schein.getLaufzeit());

		return kostenProZiehungsTag.stream()
			.reduce(0, (n1, n2) -> n1 + n2);

	}

	@Override
	public int kostenErmitteln(KostenDto kosten) {

		Lottoschein schein = new Lottoschein();
		schein.setAbgabezeitpunkt(kosten.getAbgabeZeitpunkt());
		schein.setLaufzeit(kosten.getLaufzeit());

		schein.setTipps(LottoUtil.randomTippsAsByteArray(kosten.getAnzahlTipps()));

		schein.setIsmittwoch(kosten.isMittwoch());
		schein.setIssamstag(kosten.isSamstag());
		schein.setIsspiel77(kosten.isSpiel77());
		schein.setIssuper6(kosten.isSuper6());
		return kostenErmitteln(schein);
	}

	@Override
	public KostenDetailedDto kostenErmittelnDetailed(KostenDto kosten) {
		LocalDateTime abgabeZeitpunkt = kosten.getAbgabeZeitpunkt();
		boolean isMittwoch = kosten.isMittwoch();
		boolean isSamstag = kosten.isSamstag();

		KostenDetailedDto detailedKosten = new KostenDetailedDto();

		detailedKosten.setAbgabeZeitpunkt(abgabeZeitpunkt);
		detailedKosten.setLaufzeit(kosten.getLaufzeit());
		detailedKosten.setAnzahlTipps(kosten.getAnzahlTipps());
		detailedKosten.setMittwoch(isMittwoch);
		detailedKosten.setSamstag(isSamstag);
		detailedKosten.setSpiel77(kosten.isSpiel77());
		detailedKosten.setSuper6(kosten.isSuper6());

		LocalDate erstesZiehungsdatum = LottoDatum8Util.ersterZiehungstag(
			abgabeZeitpunkt.toLocalDate(), abgabeZeitpunkt.toLocalTime(), isMittwoch,
			isSamstag, ABGABESCHLUSSMITTWOCH, ABGABESCHLUSSSAMSTAG);

		detailedKosten.setDatumErsteZiehung(erstesZiehungsdatum);

		List<Gebuehr> gebuehren = gebuehrFacade.findAll();

		List<LocalDate> ziehungsDatums = LottoDatum8Util.ziehungsTage(
			abgabeZeitpunkt.toLocalDate(), abgabeZeitpunkt.toLocalTime(), isMittwoch,
			isSamstag, ABGABESCHLUSSMITTWOCH, ABGABESCHLUSSSAMSTAG,
			kosten.getLaufzeit());

//                einmalig Grundgebuehr 
		Gebuehr gebuehrForFirstSpielTag = findGebuehrForSpielTag(gebuehren,
			erstesZiehungsdatum);
		detailedKosten.setGrundgebuehr(gebuehrForFirstSpielTag.getGrundgebuehr());

//	Dimitri
		Gebuehr gAktuell = null;
		Gebuehr gNeu = null;

		int anzahlZiehungenTmp = 0;
		for (LocalDate date : ziehungsDatums) {
			gNeu = findGebuehrForSpielTag(gebuehren, date);
			if (gAktuell == null) {
				gAktuell = gNeu;
				anzahlZiehungenTmp = 1; // erster Ziehungstag
			} else if (gAktuell == gNeu) {
				anzahlZiehungenTmp++;
			} else {
//                                alte Gebuehr erst speichern
				putEinsaetzeInKstnDtlDtoObj(detailedKosten, gAktuell,
					anzahlZiehungenTmp);
//                                Neue Gebuehr
				gAktuell = gNeu;
				anzahlZiehungenTmp = 1;
			}
		}
//                Letzte Gebuehr speichern
		if (anzahlZiehungenTmp > 0) {
			putEinsaetzeInKstnDtlDtoObj(detailedKosten, gAktuell, anzahlZiehungenTmp);
		}

		LocalDate gueltigAbLD = gebuehrForFirstSpielTag.getGueltigab();

//                ################### LOGGING ############
		log.log(Level.INFO,
			"Grundgebuehr: " + detailedKosten.getGrundgebuehr() + "   anzTipps: "
				+ detailedKosten.getAnzahlTipps() + "   EinsatzSpiel77: "
				+ detailedKosten.getEinsatzSpiel77()
					.get(gueltigAbLD)
					.getGesamtEinsatz()
				+ "   laufzeit: " + detailedKosten.getLaufzeit());

		detailedKosten.setGesamtbetrag(kostenErmitteln(kosten));
		return detailedKosten;
	}

//Help Inner-Methods:
	// ============================================
	@Override
	public Gebuehr findGebuehrForSpielTag(List<Gebuehr> gebuehren,
		LocalDate spielTag) {
		Optional<Gebuehr> optGebuerForSpielTag = gebuehren.stream()
			.filter(g -> g.getGueltigbis()
				.isAfter(spielTag))
			.filter(g -> g.getGueltigab()
				.isBefore(spielTag))
			.max((g1, g2) -> g1.getGueltigab()
				.compareTo(g2.getGueltigab()));

		Gebuehr gebuerForSpielTag = optGebuerForSpielTag.orElseThrow(
			() -> new IllegalArgumentException("no record in gebuehr..."));
		return gebuerForSpielTag;
	}

	private void putEinsaetzeInKstnDtlDtoObj(KostenDetailedDto dtoObj,
		Gebuehr gebuehrForSpielTag, int anzahlZiehungen) {
		if (dtoObj.isSpiel77()) {
			// Map Object in DtoObj, in wich we put type of Einsatz
			dtoObj.putEinsatz(dtoObj.getEinsatzSpiel77(),
				// Key for Map in DtoObj, in wich we put type of Einsatz
				gebuehrForSpielTag.getGueltigab(),
				gebuehrForSpielTag.getEinsatzspiel77(), // Prise
				anzahlZiehungen);
		}
		if (dtoObj.isSuper6()) {
			// Map Object in DtoObj, in wich we put type of Einsatz
			dtoObj.putEinsatz(dtoObj.getEinsatzSuper6(),
				// Key for Map in DtoObj, in wich we put type of Einsatz
				gebuehrForSpielTag.getGueltigab(),
				gebuehrForSpielTag.getEinsatzsuper6(), // Prise
				anzahlZiehungen);
		}

		dtoObj.putEinsatz(dtoObj.getEinsatzTipps(), // Map Object in DtoObj, in wich
																								// we put type of Einsatz
			gebuehrForSpielTag.getGueltigab(), // Key for Map in DtoObj, in wich we
																					// put type of Einsatz
			gebuehrForSpielTag.getEinsatzprotipp(), // Prise
			anzahlZiehungen);
	}
}
