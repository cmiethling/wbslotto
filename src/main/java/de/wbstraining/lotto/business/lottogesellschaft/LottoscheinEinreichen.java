package de.wbstraining.lotto.business.lottogesellschaft;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.wbstraining.lotto.cache.AdresseCacheLocal;
import de.wbstraining.lotto.cache.DBCacheLocal;
import de.wbstraining.lotto.mail.MailQueueSenderLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinFacadeLocal;
import de.wbstraining.lotto.persistence.dao.LottoscheinziehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Adresse;
import de.wbstraining.lotto.persistence.model.Gebuehr;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Ziehung;
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
    private MailQueueSenderLocal mailQueueSender;
    
    @EJB 
	private AdresseCacheLocal adresseCache;
   
    @Override
    public void lottoscheinEinreichen(Lottoschein schein) {
    	Ziehung ziehung;
        Lottoscheinziehung lottoscheinziehung;
        Date datum = new Date();
        List<Date> dateList;
        lottoscheinFacadeLocal.create(schein);
        dateList = LottoDatumUtil.ziehungsTage(schein.getAbgabedatum(), schein.getIsmittwoch(),
                schein.getIssamstag(), 18, 19, schein.getLaufzeit());
        
     	// TODO
        int kosten = 12345;
        List<Adresse> adreseeList = adresseCache.getAdresseListByKundeId(schein.getKundeid());
		Auftrag auftrag = new Auftrag(schein.getBelegnummer(), schein.getKundeid().getName(),
				schein.getLaufzeit(), kosten, schein.getVersion(), schein.getLosnummer(), schein.getIsspiel77(),
				schein.getIssuper6(), schein.getIsmittwoch(), schein.getIssamstag(), schein.getTipps().length / 8);
		
		
		AuftragKunde auftragKunde = new AuftragKunde(schein.getKundeid(),adreseeList);
		mailQueueSender.sendEmail("lottouser@lotto.test", "new message from wbslotto", "new receipt from wbs lotto",
				//PdfQuittungGenerator.createPdfFromAuftrag(auftrag));
				PdfQuittungGenerator.createPDFAsByteArray(auftrag,auftragKunde,null));
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
            
            // new
            
            // Map<LocalDate, Gebuehr> gebuerenKosten = lottoscheinEinreichenKostenErmitteln.getGebuerenKosten();
 			// int kosten = lottoscheinEinreichenKostenErmitteln.kostenermitteln(schein);
			// schein.setKosten(kosten);
 		
			// Map<LocalDate, Gebuehr> gebuerenKosten = lottoscheinEinreichenKostenErmitteln.getGebuerenKosten();
			
			// TODO
//			Map<LocalDate, Gebuehr> gebuerenKosten = null;
//			
//			mailQueueSender.sendEmail("lottouser@lotto.test", "new message from wbslotto", "new receipt from wbs lotto",
//					//PdfQuittungGenerator.createPdfFromAuftrag(auftrag));
//					PdfQuittungGenerator.createPDFAsByteArray(auftrag,auftragKunde,gebuerenKosten));
        }
    }
}
