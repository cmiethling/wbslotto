/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.wbstraining.lotto.persistence.util.LocalDateTimeAttributeConverter;

/**
 *
 * @author Günter
 */
@Entity
@Table(name = "lottoschein")
@NamedQueries({
	@NamedQuery(name = "Lottoschein.findAll", query = "SELECT l FROM Lottoschein l"),
	@NamedQuery(name = "Lottoschein.findByLottoscheinid", query = "SELECT l FROM Lottoschein l WHERE l.lottoscheinid = :lottoscheinid"),
	@NamedQuery(name = "Lottoschein.findByBelegnummer", query = "SELECT l FROM Lottoschein l WHERE l.belegnummer = :belegnummer"),
	@NamedQuery(name = "Lottoschein.findByLosnummer", query = "SELECT l FROM Lottoschein l WHERE l.losnummer = :losnummer"),
	@NamedQuery(name = "Lottoschein.findByIsspiel77", query = "SELECT l FROM Lottoschein l WHERE l.isspiel77 = :isspiel77"),
	@NamedQuery(name = "Lottoschein.findByIssuper6", query = "SELECT l FROM Lottoschein l WHERE l.issuper6 = :issuper6"),
	@NamedQuery(name = "Lottoschein.findByIsmittwoch", query = "SELECT l FROM Lottoschein l WHERE l.ismittwoch = :ismittwoch"),
	@NamedQuery(name = "Lottoschein.findByIssamstag", query = "SELECT l FROM Lottoschein l WHERE l.issamstag = :issamstag"),
	@NamedQuery(name = "Lottoschein.findByLaufzeit", query = "SELECT l FROM Lottoschein l WHERE l.laufzeit = :laufzeit"),
	@NamedQuery(name = "Lottoschein.findByIsabgeschlossen", query = "SELECT l FROM Lottoschein l WHERE l.isabgeschlossen = :isabgeschlossen"),
	@NamedQuery(name = "Lottoschein.findByAbgabedatum", query = "SELECT l FROM Lottoschein l WHERE l.abgabedatum = :abgabedatum"),
	@NamedQuery(name = "Lottoschein.findByKosten", query = "SELECT l FROM Lottoschein l WHERE l.kosten = :kosten"),
	@NamedQuery(name = "Lottoschein.findByStatus", query = "SELECT l FROM Lottoschein l WHERE l.status = :status"),
	@NamedQuery(name = "Lottoschein.findByCreated", query = "SELECT l FROM Lottoschein l WHERE l.created = :created"),
	@NamedQuery(name = "Lottoschein.findByLastmodified", query = "SELECT l FROM Lottoschein l WHERE l.lastmodified = :lastmodified"),
	@NamedQuery(name = "Lottoschein.findByVersion", query = "SELECT l FROM Lottoschein l WHERE l.version = :version") })
public class Lottoschein implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "lottoscheinid")
	private Long lottoscheinid;
	@Column(name = "belegnummer")
	private BigInteger belegnummer;
	@Basic(optional = false)
	@NotNull
	@Column(name = "losnummer")
	private int losnummer;
	@Column(name = "isspiel77")
	private Boolean isspiel77;
	@Column(name = "issuper6")
	private Boolean issuper6;
	@Column(name = "ismittwoch")
	private Boolean ismittwoch;
	@Column(name = "issamstag")
	private Boolean issamstag;
	@Column(name = "laufzeit")
	private Integer laufzeit;
	@Lob
	@Column(name = "tipps")
	private byte[] tipps;
	@Column(name = "isabgeschlossen")
	private Boolean isabgeschlossen;
	@Basic(optional = false)
	@NotNull
	@Column(name = "abgabedatum")
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime abgabedatum;
	@Column(name = "kosten")
	private Integer kosten;
	@Column(name = "status")
	private Integer status;
	@Basic(optional = false)
	@NotNull
	@Column(name = "created")
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime created;
	@Basic(optional = false)
	@NotNull
	@Column(name = "lastmodified")
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime lastmodified;
	@Column(name = "version")
	private Integer version;
	@JoinColumn(name = "kundeid", referencedColumnName = "kundeid")
	@ManyToOne(optional = false)
	private Kunde kundeid;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lottoscheinid")
	private List<Lottoscheinziehung> lottoscheinziehungList;

	public Lottoschein() {
	}

	public Lottoschein(Long lottoscheinid) {
		this.lottoscheinid = lottoscheinid;
	}

	public Lottoschein(Long lottoscheinid, int losnummer,
		LocalDateTime abgabedatum, LocalDateTime created,
		LocalDateTime lastmodified) {
		this.lottoscheinid = lottoscheinid;
		this.losnummer = losnummer;
		this.abgabedatum = abgabedatum;
		this.created = created;
		this.lastmodified = lastmodified;
	}

	public Long getLottoscheinid() {
		return lottoscheinid;
	}

	public void setLottoscheinid(Long lottoscheinid) {
		this.lottoscheinid = lottoscheinid;
	}

	public BigInteger getBelegnummer() {
		return belegnummer;
	}

	public void setBelegnummer(BigInteger belegnummer) {
		this.belegnummer = belegnummer;
	}

	public int getLosnummer() {
		return losnummer;
	}

	public void setLosnummer(int losnummer) {
		this.losnummer = losnummer;
	}

	public Boolean getIsspiel77() {
		return isspiel77;
	}

	public void setIsspiel77(Boolean isspiel77) {
		this.isspiel77 = isspiel77;
	}

	public Boolean getIssuper6() {
		return issuper6;
	}

	public void setIssuper6(Boolean issuper6) {
		this.issuper6 = issuper6;
	}

	public Boolean getIsmittwoch() {
		return ismittwoch;
	}

	public void setIsmittwoch(Boolean ismittwoch) {
		this.ismittwoch = ismittwoch;
	}

	public Boolean getIssamstag() {
		return issamstag;
	}

	public void setIssamstag(Boolean issamstag) {
		this.issamstag = issamstag;
	}

	public Integer getLaufzeit() {
		return laufzeit;
	}

	public void setLaufzeit(Integer laufzeit) {
		this.laufzeit = laufzeit;
	}

	public byte[] getTipps() {
		return tipps;
	}

	public void setTipps(byte[] tipps) {
		this.tipps = tipps;
	}

	public Boolean getIsabgeschlossen() {
		return isabgeschlossen;
	}

	public void setIsabgeschlossen(Boolean isabgeschlossen) {
		this.isabgeschlossen = isabgeschlossen;
	}

	public LocalDateTime getAbgabedatum() {
		return abgabedatum;
	}

	public void setAbgabedatum(LocalDateTime abgabedatum) {
		this.abgabedatum = abgabedatum;
	}

	public Integer getKosten() {
		return kosten;
	}

	public void setKosten(Integer kosten) {
		this.kosten = kosten;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(LocalDateTime lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Kunde getKundeid() {
		return kundeid;
	}

	public void setKundeid(Kunde kundeid) {
		this.kundeid = kundeid;
	}

	public List<Lottoscheinziehung> getLottoscheinziehungList() {
		return lottoscheinziehungList;
	}

	public void setLottoscheinziehungList(
		List<Lottoscheinziehung> lottoscheinziehungList) {
		this.lottoscheinziehungList = lottoscheinziehungList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (lottoscheinid != null ? lottoscheinid.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not
		// set
		if (!(object instanceof Lottoschein)) {
			return false;
		}
		Lottoschein other = (Lottoschein) object;
		if ((this.lottoscheinid == null && other.lottoscheinid != null)
			|| (this.lottoscheinid != null
				&& !this.lottoscheinid.equals(other.lottoscheinid))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "wbs.corejpa.persistence.Lottoschein[ lottoscheinid=" + lottoscheinid
			+ " ]";
	}

}
