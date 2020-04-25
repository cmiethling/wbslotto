package de.wbstraining.lotto.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * welche felder müsste die klasse GewinnDetailedDto
 * zur verfügung stellen, um die gewinnanfrage
 * eines clients detailliert beantworten zu können?
 * 
 * auf welche felder in welchen tabellen müsste der
 * prozess, der die gewinnanfrage beantwortet, zugreifen?
 */
@XmlRootElement(name = "gewinnDetailedDto")
public class GewinnDetailedDto implements Serializable {

	private static final long serialVersionUID = 1L;

	static class Pair6Aus49 implements Serializable {
		private static final long serialVersionUID = 1L;

		int gkl;
		long quote;

		public Pair6Aus49(int gkl, long quote) {
			this.gkl = gkl;
			this.quote = quote;
		}

		public int getGkl() {
			return gkl;
		}

		public void setGkl(int gkl) {
			this.gkl = gkl;
		}

		public long getQuote() {
			return quote;
		}

		public void setQuote(long quote) {
			this.quote = quote;
		}

	}

	private long belegNr;
	private LocalDate ziehungsDatum;
	private int gklSpiel77;
	private long quoteSpiel77;

	private int gklSuper6;
	private long quoteSuper6;

	// key: tippNr
	private Map<Integer, Pair6Aus49> gkl6Aus49 = new HashMap<>();

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

	public int getGklSpiel77() {
		return gklSpiel77;
	}

	public void setGklSpiel77(int gklSpiel77) {
		this.gklSpiel77 = gklSpiel77;
	}

	public long getQuoteSpiel77() {
		return quoteSpiel77;
	}

	public void setQuoteSpiel77(long quoteSpiel77) {
		this.quoteSpiel77 = quoteSpiel77;
	}

	public int getGklSuper6() {
		return gklSuper6;
	}

	public void setGklSuper6(int gklSuper6) {
		this.gklSuper6 = gklSuper6;
	}

	public long getQuoteSuper6() {
		return quoteSuper6;
	}

	public void setQuoteSuper6(long quoteSuper6) {
		this.quoteSuper6 = quoteSuper6;
	}

	public void addPair6Aus49(int tippNr, int gkl, long quote) {
		gkl6Aus49.put(tippNr, new Pair6Aus49(gkl, quote));
	}

	public Map<Integer, Pair6Aus49> getGkl6Aus49() {
		return gkl6Aus49;
	}

	public void setGkl6Aus49(Map<Integer, Pair6Aus49> gkl6Aus49) {
		this.gkl6Aus49 = gkl6Aus49;
	}
}
