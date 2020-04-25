package de.wbstraining.lotto.business.lottogesellschaft;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.wbstraining.lotto.business.lottospieler.KostenErmittelnLocal;
import de.wbstraining.lotto.cache.AdresseCacheLocal;
import de.wbstraining.lotto.cache.DBCacheLocal;
import de.wbstraining.lotto.dto.KostenDetailedDto;
import de.wbstraining.lotto.dto.KostenDto;
import de.wbstraining.lotto.mail.MailQueueSenderLocal;
import de.wbstraining.lotto.persistence.dao.GebuehrFacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinFacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinziehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Adresse;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Ziehung;
import de.wbstraining.lotto.util.ByteLongConverter;
import de.wbstraining.lotto.util.LottoDatum8Util;
import de.wbstraining.lotto.util.LottoDatumUtil;

@Stateless
public class LottoscheinEinreichen implements LottoscheinEinreichenLocal {

	@EJB
	private DBCacheLocal dBCacheLocal;

	@EJB
	private LottoscheinFacadeLocal lottoscheinFacadeLocal;

	@EJB
	private LottoscheinziehungFacadeLocal lottoscheinziehungFacadeLocal;

	// new

	@EJB
	private GebuehrFacadeLocal gebuehrFacadeLocal;

	@EJB
	private KostenErmittelnLocal kostenErmittelnLocal;

	@EJB
	private MailQueueSenderLocal mailQueueSender;

	@EJB
	private AdresseCacheLocal adresseCache;

	@Override
	public void lottoscheinEinreichen(Lottoschein schein) {
		Ziehung ziehung;
		Lottoscheinziehung lottoscheinziehung;

		KostenDto kostenDto = new KostenDto();
		KostenDetailedDto kostenDetailedDto;

		Date datum = new Date();
		List<Date> dateList;
		lottoscheinFacadeLocal.create(schein);
		dateList = LottoDatumUtil.ziehungsTage(schein.getAbgabedatum(),
			schein.getIsmittwoch(), schein.getIssamstag(), 18, 19,
			schein.getLaufzeit());

		kostenDto.setAbgabeDatum(schein.getAbgabedatum());
		kostenDto
			.setAnzahlTipps(ByteLongConverter.byteToLong(schein.getTipps()).length);
		kostenDto.setLaufzeit(schein.getLaufzeit());
		kostenDto.setMittwoch(schein.getIsmittwoch());
		kostenDto.setSamstag(schein.getIssamstag());
		kostenDto.setSpiel77(schein.getIsspiel77());
		kostenDto.setSuper6(schein.getIssuper6());

		kostenDetailedDto = kostenErmittelnLocal.kostenErmittelnDetailed(kostenDto);

		int kosten = kostenDetailedDto.getGesamtbetrag();

		List<Adresse> adreseeList = adresseCache
			.getAdresseListByKundeId(schein.getKundeid());
		Auftrag auftrag = new Auftrag(schein.getBelegnummer(), schein.getKundeid()
			.getName(), schein.getLaufzeit(), kosten, schein.getVersion(),
			schein.getLosnummer(), schein.getIsspiel77(), schein.getIssuper6(),
			schein.getIsmittwoch(), schein.getIssamstag(),
			schein.getTipps().length / 8);

		AuftragKunde auftragKunde = new AuftragKunde(schein.getKundeid(),
			adreseeList);

		StringBuilder sbContent = new StringBuilder("");
		LocalDate localDatum = datum.toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDate();

		sbContent.append("\t\t\t\t\t\t Dresden, den "
			+ localDatum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
			+ "\n\n\n");

		sbContent.append("Hallo Frau/Herr " + schein.getKundeid()
			.getName() + ",\n");
		sbContent.append("\n");
		sbContent.append(
			"Anbei erhalten Sie den Rechnungsbeleg zu Ihrem gebuchten Lottotipp.\n\n");
		sbContent.append(
			"WBS Lotto hofft Sie auch bald wieder als einen unserer Kunden begrüssen zu dürfen.\n");
		sbContent.append("\n\n\n");
		sbContent.append("Mit freundlichen Grüssen\n\n");
		sbContent.append("\n\n");
		sbContent.append("Ihr Team von WBS Lotto");

		mailQueueSender.sendEmail("lottouser@lotto.test",
			"Zusendung des Rechnungsbeleges Ihres gebuchten Lottotipps",
			sbContent.toString(),

			PdfQuittungGenerator.createPDFAsByteArray(auftrag, auftragKunde,
				kostenDetailedDto));
		int nr = 1;
		for (Date date : dateList) {
			ziehung = dBCacheLocal.ziehungByDatum(date);
			lottoscheinziehung = new Lottoscheinziehung();
			lottoscheinziehung.setZiehungid(ziehung);
			lottoscheinziehung.setLottoscheinid(schein);
			lottoscheinziehung.setGewinnklasseidspiel77(null);
			lottoscheinziehung.setGewinnklasseidsuper6(null);
			lottoscheinziehung.setIsabgeschlossen(false);
			lottoscheinziehung.setZiehungnr(nr);
			lottoscheinziehung.setCreated(LottoDatum8Util.date2LocalDateTime(datum));
			lottoscheinziehung
				.setLastmodified(LottoDatum8Util.date2LocalDateTime(datum));
			lottoscheinziehung.setIsletzteziehung(nr == (dateList.size()));
			lottoscheinziehungFacadeLocal.create(lottoscheinziehung);
			nr++;

		}
	}

}
