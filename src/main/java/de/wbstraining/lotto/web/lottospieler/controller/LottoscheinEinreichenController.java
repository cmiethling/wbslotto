/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.web.lottospieler.controller;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import de.wbstraining.lotto.business.lottogesellschaft.LottoscheinEinreichenLocal;
import de.wbstraining.lotto.business.lottospieler.KostenErmittelnLocal;
import de.wbstraining.lotto.dto.KostenDetailedDto;
import de.wbstraining.lotto.persistence.dao.KundeFacadeLocal;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.util.LottoUtil;

/**
 *
 * @author gz1
 */
@SessionScoped
@Named
public class LottoscheinEinreichenController implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private KostenErmittelnLocal lottoscheinEinreichenKostenErmitteln;

	@EJB
	private LottoscheinEinreichenLocal lottoscheinEinreichenLocal;

	@EJB
	private KundeFacadeLocal kundeFacadeLocal;

	private Long kundeId;
	@Pattern(regexp = "\\d{7}")
	private String losnummer;
	private List<String> ziehungstage = new ArrayList<>(); // "isSamstag";
	private List<String> spiele = new ArrayList<>();
	@Pattern(regexp = "[123458]")
	private String laufzeit = "1";
	@Min(1)
	@Max(12)
	private int anzahlTipps = 1;
	private String kostenAsString = "EUR 0.00";
	private Kunde kunde;

	// TODO
	// zustandsmodell entwickeln und als state chart dokumentieren
	private boolean isRegistriert = false;
	private boolean isAngemeldet = false;

	// TODO
	// s. zustandsmodell
	@Inject
	RegistrierungController registrierungController;

	public boolean isRegistriert() {
		return isRegistriert;
	}

	public void setRegistriert(boolean isRegistriert) {
		this.isRegistriert = isRegistriert;
	}

	public boolean isAngemeldet() {
		return isAngemeldet;
	}

	public void setAngemeldet(boolean isAngemeldet) {
		this.isAngemeldet = isAngemeldet;
	}

	public String getKostenAsString() {
		return kostenAsString;
	}

	public void setKostenAsString(String kostenAsString) {
		this.kostenAsString = kostenAsString;
	}

	public List<String> getSpiele() {
		return spiele;
	}

	public void setSpiele(List<String> spiele) {
		this.spiele = spiele;
	}

	public Long getKundeId() {
		return kundeId;
	}

	public void setKundeId(Long kundeId) {
		this.kundeId = kundeId;
		kunde = kundeFacadeLocal.find(kundeId);
	}

	public String getLosnummer() {
		return losnummer;
	}

	public void setLosnummer(String losnummer) {
		this.losnummer = losnummer;
	}

	public List<String> getZiehungstage() {
		return ziehungstage;
	}

	public void setZiehungstage(List<String> ziehungstage) {
		this.ziehungstage = ziehungstage;
	}

	public String getLaufzeit() {
		return laufzeit;
	}

	public void setLaufzeit(String laufzeit) {
		this.laufzeit = laufzeit;
	}

	public int getAnzahlTipps() {
		return anzahlTipps;
	}

	public void setAnzahlTipps(int anzahlTipps) {
		this.anzahlTipps = anzahlTipps;
	}

	public void validateKundeId(FacesContext context, UIComponent toValidate, Object value) {
		Long kundeId = (Long) value;
		List<Kunde> allKundeId = kundeFacadeLocal.findAll();

		if (allKundeId.parallelStream().noneMatch(k -> k.getKundeid().equals(kundeId))) {
			((UIInput) toValidate).setValid(false);

			FacesMessage message = new FacesMessage("Invalid KundeId");
			context.addMessage(toValidate.getClientId(context), message);
		}

	}

	public String senden() {

		Kunde kunde = null;

		// TODO registrierung und einreichen des lottoscheins entkoppeln
		/*
		 * if (!isRegistriert()) { registrierungController.senden(); kunde =
		 * registrierungController.getKunde(); }
		 */
		kunde = kundeFacadeLocal.find(kundeId);

		System.out.println("KundeId: " + kundeId);
		System.out.println("Kunde: " + kunde);

		Date datum = new Date();
		Lottoschein schein = new Lottoschein();

		schein.setKundeid(kunde);
		schein.setAbgabedatum(datum);
		schein.setBelegnummer((BigInteger.valueOf((long) (Math.random() * 1_000_000_000))));
		schein.setCreated(datum);
		schein.setLastmodified(datum);
		schein.setIsabgeschlossen(Boolean.FALSE);
		schein.setIsspiel77(spiele.contains("spiel77"));
		schein.setIssuper6(spiele.contains("super6"));
		schein.setLaufzeit(Integer.parseInt(laufzeit));
		schein.setIsmittwoch(Boolean.FALSE);
		schein.setIssamstag(Boolean.FALSE);
		if (ziehungstage.contains("isSamstag"))
			schein.setIssamstag(Boolean.TRUE);
		if (ziehungstage.contains("isMittwoch"))
			schein.setIsmittwoch(Boolean.TRUE);

		schein.setKosten(0);

		schein.setLosnummer(Integer.parseInt(losnummer));
		schein.setTipps(LottoUtil.randomTippsAsByteArray(anzahlTipps));

		lottoscheinEinreichenLocal.lottoscheinEinreichen(schein);

		return "ok";

	}

	public String onChange(AjaxBehaviorEvent event) {

		System.out.println(LocalDate.now());
		System.out.println(laufzeit);
		System.out.println(ziehungstage.contains("isMittwoch"));
		System.out.println(ziehungstage.contains("isSamstag"));
		System.out.println(anzahlTipps);
		System.out.println(spiele.contains("super6"));
		System.out.println(spiele.contains("spiel77"));

		if (!laufzeit.isEmpty() && (ziehungstage.contains("isMittwoch") || ziehungstage.contains("isSamstag"))
				&& anzahlTipps > 0) {
			// TODO
			KostenDetailedDto dto = new KostenDetailedDto();
			dto.setAnzahlTipps(0);
			dto.setAbgabeDatum(null);
			// ...
			/*
			 * int kosten =
			 * lottoscheinEinreichenKostenErmitteln.kostenermitteln(LocalDate.now(),
			 * Integer.parseInt(laufzeit), ziehungstage.contains("isMittwoch"),
			 * ziehungstage.contains("isSamstag"), anzahlTipps, spiele.contains("super6"),
			 * spiele.contains("spiel77"));
			 */
			int kosten = 123;
			kostenAsString = String.format("EUR %.2f", kosten / 100.0);
		}

		System.out.println("Kosten : " + kostenAsString);

		return "ok";
	}

	public Kunde getKunde() {
		return kunde;
	}

	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}
}
