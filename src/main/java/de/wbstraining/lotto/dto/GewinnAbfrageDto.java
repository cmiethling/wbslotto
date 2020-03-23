package de.wbstraining.lotto.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "gewinnAbfrageDto")
public class GewinnAbfrageDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long belegNr;
	private Date ziehungsDatum;
	
	public long getBelegNr() {
		return belegNr;
	}
	
	public void setBelegNr(long belegNr) {
		this.belegNr = belegNr;
	}
	
	public Date getZiehungsDatum() {
		return ziehungsDatum;
	}
	
	public void setZiehungsDatum(Date ziehungsDatum) {
		this.ziehungsDatum = ziehungsDatum;
	}
}
