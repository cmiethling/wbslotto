package de.wbstraining.lotto.business.lottogesellschaft;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

// TODO

// die spalten der tabelle auftrag festlegen
// die tabelle auftrag erstellen
// die nachstehende klasse mit jpa annotations auf die tabelle mappen
// dao-interface erstellen
// dao-implementierung erstellen

// ggf. erweiteterungen bestehender entity- klassen

@Entity
@Table(name = "auftrag")
@NamedQueries({
    @NamedQuery(name = "Auftrag.findAll", query = "SELECT k FROM Auftrag k"),
//    @NamedQuery(name = "Auftrag.findByKundeId", query = "SELECT k FROM Auftrag k WHERE k.kundeId = :kundeId"),
    
    // richtig?
    @NamedQuery(name = "Auftrag.findByName", query = "SELECT k FROM Auftrag k WHERE k.name = :name"),
    
    @NamedQuery(name = "Auftrag.findByBelegnummer", query = "SELECT l FROM Auftrag l WHERE l.belegnummer = :belegnummer"),
    @NamedQuery(name = "Auftrag.findByLosnummer", query = "SELECT l FROM Auftrag l WHERE l.losnummer = :losnummer"),
    @NamedQuery(name = "Auftrag.findByIsSpiel77", query = "SELECT l FROM Auftrag l WHERE l.isSpiel77 = :isSpiel77"),
    @NamedQuery(name = "Auftrag.findByIsSuper6", query = "SELECT l FROM Auftrag l WHERE l.isSuper6 = :isSuper6"),
    @NamedQuery(name = "Auftrag.findByIsMittwoch", query = "SELECT l FROM Auftrag l WHERE l.isMittwoch = :isMittwoch"),
    @NamedQuery(name = "Auftrag.findByIsSamstag", query = "SELECT l FROM Auftrag l WHERE l.isSamstag = :isSamstag"),
    @NamedQuery(name = "Auftrag.findByLaufzeit", query = "SELECT l FROM Auftrag l WHERE l.laufzeit = :laufzeit"),    
    @NamedQuery(name = "Auftrag.findByAbgabeDatum", query = "SELECT l FROM Lottoschein l WHERE l.abgabedatum = :abgabedatum"),
    @NamedQuery(name = "Auftrag.findByKosten", query = "SELECT l FROM Lottoschein l WHERE l.kosten = :kosten"),
    @NamedQuery(name = "Auftrag.findByVersion", query = "SELECT k FROM Auftrag k WHERE k.version = :version")})
public class Auftrag implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "Belegnummer")
	private BigInteger belegnummer;
	@Basic(optional = false)
	
	// ????
	@Column(name = "KundeId")
	private String name;
	
	public BigInteger getBelegnummer() {
		return belegnummer;
	}
	public void setBelegnummer(BigInteger belegnummer) {
		this.belegnummer = belegnummer;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public int getLosnummer() {
		return losnummer;
	}
	public void setLosnummer(int losnummer) {
		this.losnummer = losnummer;
	}
	public Boolean getIsSpiel77() {
		return isSpiel77;
	}
	public void setIsSpiel77(Boolean isSpiel77) {
		this.isSpiel77 = isSpiel77;
	}
	public Boolean getIsSuper6() {
		return isSuper6;
	}
	public void setIsSuper6(Boolean isSuper6) {
		this.isSuper6 = isSuper6;
	}
	public Boolean getIsMittwoch() {
		return isMittwoch;
	}
	public void setIsMittwoch(Boolean isMittwoch) {
		this.isMittwoch = isMittwoch;
	}
	public Boolean getIsSamstag() {
		return isSamstag;
	}
	public void setIsSamstag(Boolean isSamstag) {
		this.isSamstag = isSamstag;
	}
	public int getAnzahlTipps() {
		return anzahlTipps;
	}
	public void setAnzahlTipps(int anzahlTipps) {
		this.anzahlTipps = anzahlTipps;
	}
	@Column(name = "Laufzeit")
	private int laufzeit;
	@Column(name = "Kosten")
	private int kosten;
	@Column(name = "Version")
	private Integer version;
	@Basic(optional = false)
	@NotNull
	@Column(name = "Losnummer")
	private int losnummer;
	@Column(name = "IsSpiel77")
	private Boolean isSpiel77;
	@Column(name = "IsSuper6")
	private Boolean isSuper6;
	@Column(name = "IsMittwoch")
	private Boolean isMittwoch;
	@Column(name = "IsSamstag")
	private Boolean isSamstag;
	@Column(name = "AnzahlTipps")
	private int anzahlTipps;
	
	public Auftrag() {
		
	}
	
	public Auftrag(BigInteger belegnummer, String name, int laufzeit, int kosten, Integer version,
			@NotNull int losnummer, Boolean isSpiel77, Boolean isSuper6, Boolean isMittwoch, Boolean isSamstag,
			int anzahlTipps) {
		super();
		this.belegnummer = belegnummer;
		this.name = name;
		this.laufzeit = laufzeit;
		this.kosten = kosten;
		this.version = version;
		this.losnummer = losnummer;
		this.isSpiel77 = isSpiel77;
		this.isSuper6 = isSuper6;
		this.isMittwoch = isMittwoch;
		this.isSamstag = isSamstag;
		this.anzahlTipps = anzahlTipps;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLaufzeit() {
		return laufzeit;
	}
	public void setLaufzeit(int laufzeit) {
		this.laufzeit = laufzeit;
	}
	public int getKosten() {
		return kosten;
	}
	public void setKosten(int kosten) {
		this.kosten = kosten;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anzahlTipps;
		result = prime * result + ((belegnummer == null) ? 0 : belegnummer.hashCode());
		result = prime * result + ((isMittwoch == null) ? 0 : isMittwoch.hashCode());
		result = prime * result + ((isSamstag == null) ? 0 : isSamstag.hashCode());
		result = prime * result + ((isSpiel77 == null) ? 0 : isSpiel77.hashCode());
		result = prime * result + ((isSuper6 == null) ? 0 : isSuper6.hashCode());
		result = prime * result + kosten;
		result = prime * result + laufzeit;
		result = prime * result + losnummer;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Auftrag other = (Auftrag) obj;
		if (anzahlTipps != other.anzahlTipps)
			return false;
		if (belegnummer == null) {
			if (other.belegnummer != null)
				return false;
		} else if (!belegnummer.equals(other.belegnummer))
			return false;
		if (isMittwoch == null) {
			if (other.isMittwoch != null)
				return false;
		} else if (!isMittwoch.equals(other.isMittwoch))
			return false;
		if (isSamstag == null) {
			if (other.isSamstag != null)
				return false;
		} else if (!isSamstag.equals(other.isSamstag))
			return false;
		if (isSpiel77 == null) {
			if (other.isSpiel77 != null)
				return false;
		} else if (!isSpiel77.equals(other.isSpiel77))
			return false;
		if (isSuper6 == null) {
			if (other.isSuper6 != null)
				return false;
		} else if (!isSuper6.equals(other.isSuper6))
			return false;
		if (kosten != other.kosten)
			return false;
		if (laufzeit != other.laufzeit)
			return false;
		if (losnummer != other.losnummer)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Auftrag [belegnummer=" + belegnummer + "]";
	}
	
}
