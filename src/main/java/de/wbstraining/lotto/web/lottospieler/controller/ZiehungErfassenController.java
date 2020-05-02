/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.web.lottospieler.controller;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Ziehung;

/**
 *
 * @author gz1
 */

@RequestScoped
@Named
public class ZiehungErfassenController {

	@EJB
	private ZiehungFacadeLocal ziehungFacadeLocal;

	private LocalDate ziehungsDatum;
	private String zahlenAlsBits;
	@Min(1)
	@Max(9)
	private int superzahl;
	@Pattern(regexp = "\\d{7}")
	private String spiel77;
	@Pattern(regexp = "\\d{6}")
	private String super6;

	public String senden() {

		Ziehung ziehung = new Ziehung();

		LocalDateTime date = LocalDateTime.now();
		ziehung.setEinsatzlotto(BigInteger.ZERO);
		ziehung.setEinsatzspiel77(BigInteger.ZERO);
		ziehung.setEinsatzsuper6(BigInteger.ZERO);
		ziehung.setSpiel77(Integer.parseInt(spiel77));
		ziehung.setSuper6(Integer.parseInt(super6));
		ziehung.setSuperzahl(superzahl);
		ziehung.setZahlenalsbits(BigInteger.valueOf(convertToLong(zahlenAlsBits)));
		ziehung.setStatus(0);
		ziehung.setZiehungsdatum(ziehungsDatum);
		ziehung.setCreated(date);
		ziehung.setLastmodified(date);

		ziehungFacadeLocal.create(ziehung);

		return "ok";
	}

	private long convertToLong(String zahlenAlsString) {
		long zahlenAlsLong = 0;
		boolean isOk = true;
		String[] tokens = zahlenAlsString.split(",");
		if (tokens.length == 6) {
			for (String token : tokens)
				try {
					zahlenAlsLong |= (1L << Integer.parseInt(token));
				} catch (NumberFormatException e) {
					isOk = false;
					break;
				}
		} else {
			isOk = false;
		}
		if (isOk) {
			if (Long.bitCount(zahlenAlsLong) != 6
				|| Long.highestOneBit(zahlenAlsLong) > (1L << 49)
				|| zahlenAlsLong % 2 == 1 || zahlenAlsLong < 0) {
				isOk = false;
			}
		}
		return isOk ? zahlenAlsLong : -1;
	}

	public void validate(FacesContext context, UIComponent component, Object obj)
		throws ValidatorException {
		String zahlenAlsString = (String) obj;
		long zahlenAlsLong = convertToLong(zahlenAlsString);
		if (zahlenAlsLong < 0) {
			Locale locale = FacesContext.getCurrentInstance()
				.getViewRoot()
				.getLocale();
			String msg = ResourceBundle.getBundle("messages", locale)
				.getString("errZiehungszahlen");
			FacesMessage message = new FacesMessage(msg);
			throw new ValidatorException(message);
		}

	}

	public LocalDate getZiehungsDatum() {
		return ziehungsDatum;
	}

	public void setZiehungsDatum(LocalDate ziehungsDatum) {
		this.ziehungsDatum = ziehungsDatum;
	}

	public String getZahlenAlsBits() {
		return zahlenAlsBits;
	}

	public void setZahlenAlsBits(String zahlenAlsBits) {
		this.zahlenAlsBits = zahlenAlsBits;
	}

	public int getSuperzahl() {
		return superzahl;
	}

	public void setSuperzahl(int superzahl) {
		this.superzahl = superzahl;
	}

	public String getSpiel77() {
		return spiel77;
	}

	public void setSpiel77(String spiel77) {
		this.spiel77 = spiel77;
	}

	public String getSuper6() {
		return super6;
	}

	public void setSuper6(String super6) {
		this.super6 = super6;
	}
}
