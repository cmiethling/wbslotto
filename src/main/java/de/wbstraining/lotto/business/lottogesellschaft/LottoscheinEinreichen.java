package de.wbstraining.lotto.business.lottogesellschaft;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.wbstraining.lotto.business.lottospieler.KostenErmittelnLocal;
import de.wbstraining.lotto.cache.AdresseCacheLocal;
import de.wbstraining.lotto.cache.DBCacheLocal;
import de.wbstraining.lotto.cache.GebuehrenCacheLocal;
import de.wbstraining.lotto.dto.KostenDetailedDto;
import de.wbstraining.lotto.dto.KostenDto;
import de.wbstraining.lotto.mail.MailQueueSenderLocal;
import de.wbstraining.lotto.persistence.dao.GebuehrFacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinFacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinziehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Adresse;
import de.wbstraining.lotto.persistence.model.Gebuehr;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Ziehung;
import de.wbstraining.lotto.util.ByteLongConverter;
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
// 

        @EJB
        private GebuehrenCacheLocal gebuehrenCacheLocal;

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
                dateList = LottoDatumUtil.ziehungsTage(schein.getAbgabedatum(), schein.getIsmittwoch(), schein.getIssamstag(),
                                18, 19, schein.getLaufzeit());

                // TODO

                int kosten = kostenErmittelnLocal.kostenErmitteln(schein);

                kostenDto.setAbgabeDatum(schein.getAbgabedatum());
                kostenDto.setAnzahlTipps(ByteLongConverter.byteToLong(schein.getTipps()).length);
                kostenDto.setLaufzeit(schein.getLaufzeit());
                kostenDto.setMittwoch(schein.getIsmittwoch());
                kostenDto.setSamstag(schein.getIssamstag());
                kostenDto.setSpiel77(schein.getIsspiel77());
                kostenDto.setSuper6(schein.getIssuper6());

                kostenDetailedDto = kostenErmittelnLocal.kostenErmittelnDetailed(kostenDto);

                List<Gebuehr> gebuehren = gebuehrFacadeLocal.findAll();
                Map<LocalDate, Gebuehr> mapGebuehren = createGebuehrenMap(dateList, gebuehren);

                kosten = kostenDetailedDto.getGesamtbetrag();

                List<Adresse> adreseeList = adresseCache.getAdresseListByKundeId(schein.getKundeid());
                Auftrag auftrag = new Auftrag(schein.getBelegnummer(), schein.getKundeid().getName(), schein.getLaufzeit(),
                                kosten, schein.getVersion(), schein.getLosnummer(), schein.getIsspiel77(), schein.getIssuper6(),
                                schein.getIsmittwoch(), schein.getIssamstag(), schein.getTipps().length / 8);

                AuftragKunde auftragKunde = new AuftragKunde(schein.getKundeid(), adreseeList);
                mailQueueSender.sendEmail("lottouser@lotto.test", "new message from wbslotto", "new receipt from wbs lotto",
                                PdfQuittungGenerator.createPDFAsByteArray(auftrag, auftragKunde, mapGebuehren));
                // PdfQuittungGenerator.createPDFAsByteArray(auftrag,auftragKunde,kostenDetailedDto));
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
                        lottoscheinziehung.setCreated(datum);
                        lottoscheinziehung.setLastmodified(datum);
                        lottoscheinziehung.setIsletzteziehung(nr == (dateList.size()));
                        lottoscheinziehungFacadeLocal.create(lottoscheinziehung);
                        nr++;

                        // Map<LocalDate, Gebuehr> gebuerenKosten =
                        // lottoscheinEinreichenKostenErmitteln.getGebuerenKosten();
                        // int kosten = lottoscheinEinreichenKostenErmitteln.kostenermitteln(schein);
                        // schein.setKosten(kosten);

                        // Map<LocalDate, Gebuehr> gebuerenKosten =
                        // lottoscheinEinreichenKostenErmitteln.getGebuerenKosten();

                        // TODO
//                        Map<LocalDate, Gebuehr> gebuerenKosten = null;
//                        
//                        mailQueueSender.sendEmail("lottouser@lotto.test", "new message from wbslotto", "new receipt from wbs lotto",
//                                        //PdfQuittungGenerator.createPdfFromAuftrag(auftrag));
//                                        PdfQuittungGenerator.createPDFAsByteArray(auftrag,auftragKunde,gebuerenKosten));
                }
        }

        // ============================================

        private Map<LocalDate, Gebuehr> createGebuehrenMap(List<Date> dateList, List<Gebuehr> gebuehren) {

                Map<LocalDate, Gebuehr> gebuehrenMap = new HashMap<>();

                for (Date spielTag : dateList) {

                        Optional<Gebuehr> optGebuerForSpielTag = gebuehren.stream().filter(g -> g.getGueltigbis().after(spielTag))
                                        .filter(g -> g.getGueltigab().before(spielTag))
                                        .max((g1, g2) -> g1.getGueltigab().compareTo(g2.getGueltigab()));

                        Gebuehr gebuerForSpielTag = optGebuerForSpielTag
                                        .orElseThrow(() -> new IllegalArgumentException("no record in gebuehr..."));

                        LocalDate localSpielTag = spielTag.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        gebuehrenMap.put(localSpielTag, gebuerForSpielTag);

                }
                return gebuehrenMap;
        }

}