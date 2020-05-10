package de.wbstraining.lotto.web.rest.service;

import java.time.LocalDateTime;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import de.wbstraining.lotto.business.lottogesellschaft.LottoscheinEinreichenLocal;
import de.wbstraining.lotto.dto.LottoscheinEinreichenDto;
import de.wbstraining.lotto.persistence.dao.KundeFacadeLocal;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.util.LottoUtil;

@Stateless
@Path("/einreichen")
public class LottoscheinEinreichenREST {

	@EJB
	private LottoscheinEinreichenLocal lottoscheinEinreichen;

	@EJB
	private KundeFacadeLocal kundeFacade;

	// hilfsmethode zur generierung eins xml-descriptors
	// der parameter id wird ignoriert...
	// aufruf mit ausgabeumlenkung
	// kann dann wieder weggeworfen werden...
	/*
	 * @GET
	 * 
	 * @Path("{id}")
	 * 
	 * @Produces({ "application/xml", "application/json" })
	 * public LottoscheinEinreichenDto einreichen() {
	 * LottoscheinEinreichenDto dto = new LottoscheinEinreichenDto();
	 * dto.setAbgabeDatum(new Date());
	 * dto.setKundeid(1L);
	 * dto.setMittwoch(true);
	 * dto.setSamstag(true);
	 * dto.setSpiel77(true);
	 * dto.setSuper6(true);
	 * dto.setLaufzeit(5);
	 * byte[] tipps = LottoUtil.randomTippsAsByteArray(6);
	 * dto.setTippsBase64(LottoUtil.encodeTippsBase64(tipps));
	 * return dto;
	 * }
	 */

	@PUT
	@Consumes({ "application/xml", "application/json" })
	public void einreichen(LottoscheinEinreichenDto dto) {
		LocalDateTime date = LocalDateTime.now();
		Long kundid = dto.getKundeid();
		Kunde kunde = kundeFacade.find(kundid);
		Lottoschein schein = new Lottoschein();
		schein.setKunde(kunde);
		schein.setAbgabedatum(dto.getAbgabeDatum());
		schein.setBelegnummer((long) (Math.random() * 100_000_000_000L));
		schein.setCreated(date);
		schein.setLastmodified(date);
		schein.setIsabgeschlossen(Boolean.FALSE);
		schein.setIsmittwoch(dto.isMittwoch());
		schein.setIssamstag(dto.isSamstag());
		schein.setIsspiel77(dto.isSpiel77());
		schein.setIssuper6(dto.isSuper6());
		schein.setLaufzeit(dto.getLaufzeit());
		schein.setKosten(0);
		schein.setLosnummer(dto.getLosnummer());
		schein.setTipps(LottoUtil.decodeTippsBase64(dto.getTippsBase64()));

		lottoscheinEinreichen.lottoscheinEinreichen(schein);
	}
}