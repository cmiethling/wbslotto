package de.wbstraining.lotto.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "kostenDto")
public class KostenDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date abgabeDatum;
	private int laufzeit;
	private int anzahlTipps;
	private boolean isMittwoch;
	private boolean isSamstag;
	private boolean isSpiel77;
	private boolean isSuper6;
	
	public Date getAbgabeDatum() {
		return abgabeDatum;
	}
	public void setAbgabeDatum(Date abgabeDatum) {
		this.abgabeDatum = abgabeDatum;
	}
	public int getLaufzeit() {
		return laufzeit;
	}
	public void setLaufzeit(int laufzeit) {
		this.laufzeit = laufzeit;
	}
	public int getAnzahlTipps() {
		return anzahlTipps;
	}
	public void setAnzahlTipps(int anzahlTipps) {
		this.anzahlTipps = anzahlTipps;
	}
	public boolean isMittwoch() {
		return isMittwoch;
	}
	public void setMittwoch(boolean isMittwoch) {
		this.isMittwoch = isMittwoch;
	}
	public boolean isSamstag() {
		return isSamstag;
	}
	public void setSamstag(boolean isSamstag) {
		this.isSamstag = isSamstag;
	}
	public boolean isSpiel77() {
		return isSpiel77;
	}
	public void setSpiel77(boolean isSpiel77) {
		this.isSpiel77 = isSpiel77;
	}
	public boolean isSuper6() {
		return isSuper6;
	}
	public void setSuper6(boolean isSuper6) {
		this.isSuper6 = isSuper6;
	}
}
