package de.wbstraining.lotto.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "gewinnAbfrageDto")
public class GewinnAbfrageDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long belegNr;
	private LocalDate ziehungsDatum;

	public long getBelegNr() {
		return belegNr;
	}

	public void setBelegNr(long belegNr) {
		this.belegNr = belegNr;
	}

	public LocalDate getZiehungsDatum() {
		return ziehungsDatum;
	}

	public void setZiehungsDatum(LocalDate ziehungsDatum) {
		this.ziehungsDatum = ziehungsDatum;
	}
}
