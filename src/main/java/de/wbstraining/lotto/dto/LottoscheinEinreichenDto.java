package de.wbstraining.lotto.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lottoscheinEinreichenDto")
public class LottoscheinEinreichenDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long kundeid;
	private Date abgabeDatum;
	private int laufzeit;
	private String tippsBase64;
	private boolean isMittwoch;
	private boolean isSamstag;
	private boolean isSpiel77;
	private boolean isSuper6;
	private int losnummer;
	
	public Long getKundeid() {
		return kundeid;
	}
	public void setKundeid(Long kundeid) {
		this.kundeid = kundeid;
	}
	public Date getAbgabeDatum() {
		return abgabeDatum;
	}
	public int getLosnummer() {
		return losnummer;
	}
	public void setLosnummer(int losnummer) {
		this.losnummer = losnummer;
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
	public String getTippsBase64() {
		return tippsBase64;
	}
	public void setTippsBase64(String tippsBase64) {
		this.tippsBase64 = tippsBase64;
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
