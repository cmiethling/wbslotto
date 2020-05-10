package de.wbstraining.lotto.business.lottogesellschaft;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

import de.wbstraining.lotto.persistence.model.Gewinnklasseziehungquote;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung6aus49;
import de.wbstraining.lotto.persistence.model.Ziehung;

public class AuftragGewinner implements Serializable {

	private static final long serialVersionUID = 1L;

	private Gewinnklasseziehungquote gewinnklasseziehungquote;
	private Lottoschein lottoschein;
	private Kunde kunde;
	private Ziehung ziehung;
	private Lottoscheinziehung lottoscheinziehung;
	private Lottoscheinziehung6aus49 lottoscheinziehung6aus49;

	public AuftragGewinner(Lottoschein lottoschein, Kunde kunde, Ziehung ziehung,
			Lottoscheinziehung lottoscheinziehung,
			Lottoscheinziehung6aus49 lottoscheinziehung6aus49) {
		this.lottoschein = lottoschein;
		this.lottoscheinziehung = lottoscheinziehung;
		this.kunde = kunde;
		this.ziehung = ziehung;
		this.lottoscheinziehung6aus49 = lottoscheinziehung6aus49;
	}

	public BigInteger getBelegnummer() {
		return lottoschein.getBelegnummer();
	}

	public int getLosnummer() {
		return lottoschein.getLosnummer();
	}

	public LocalDate getZiehungDatum() {
		return ziehung.getZiehungsdatum();
	}

	public String getName() {
		return kunde.getName();
	}

	public String getVorname() {
		return kunde.getVorname();
	}

	public String getEmail() {
		return kunde.getEmail();
	}

	public long getGewinnklsseid() {
		return gewinnklasseziehungquote.getGewinnklasse()
				.getGewinnklasseid();
	}

	public long getQuote() {
		return gewinnklasseziehungquote.getQuote();
	}

	public long getGewinnspiel77() {
		if (lottoscheinziehung.getGewinnspiel77() != null)
			return lottoscheinziehung.getGewinnspiel77();
		return 0;
	}

	public long getGewinnsuper6() {
		if (lottoscheinziehung.getGewinnsuper6() != null)
			return lottoscheinziehung.getGewinnsuper6()
					.longValue();
		return 0;
	}

	public long getGewinn() {
		if (lottoscheinziehung6aus49 != null)
			return lottoscheinziehung6aus49.getGewinn()
					.longValue();
		return 0;
	}

}
