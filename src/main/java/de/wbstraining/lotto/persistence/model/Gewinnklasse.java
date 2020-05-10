/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.wbstraining.lotto.persistence.util.LocalDateAttributeConverter;
import de.wbstraining.lotto.persistence.util.LocalDateTimeAttributeConverter;

/**
 *
 * @author Günter
 */
@Entity
@Table(name = "gewinnklasse")
@NamedQueries({
	@NamedQuery(name = "Gewinnklasse.findAll", query = "SELECT g FROM Gewinnklasse g"),
	@NamedQuery(name = "Gewinnklasse.findByGewinnklasseid", query = "SELECT g FROM Gewinnklasse g WHERE g.gewinnklasseid = :gewinnklasseid"),
	@NamedQuery(name = "Gewinnklasse.findByGewinnklassenr", query = "SELECT g FROM Gewinnklasse g WHERE g.gewinnklassenr = :gewinnklassenr"),
	@NamedQuery(name = "Gewinnklasse.findByBezeichnunglatein", query = "SELECT g FROM Gewinnklasse g WHERE g.bezeichnunglatein = :bezeichnunglatein"),
	@NamedQuery(name = "Gewinnklasse.findByBeschreibung", query = "SELECT g FROM Gewinnklasse g WHERE g.beschreibung = :beschreibung"),
	@NamedQuery(name = "Gewinnklasse.findByIsabsolut", query = "SELECT g FROM Gewinnklasse g WHERE g.isabsolut = :isabsolut"),
	@NamedQuery(name = "Gewinnklasse.findByBetrag", query = "SELECT g FROM Gewinnklasse g WHERE g.betrag = :betrag"),
	@NamedQuery(name = "Gewinnklasse.findByGueltigab", query = "SELECT g FROM Gewinnklasse g WHERE g.gueltigab = :gueltigab"),
	@NamedQuery(name = "Gewinnklasse.findByGueltigbis", query = "SELECT g FROM Gewinnklasse g WHERE g.gueltigbis = :gueltigbis"),
	@NamedQuery(name = "Gewinnklasse.findByCreated", query = "SELECT g FROM Gewinnklasse g WHERE g.created = :created"),
	@NamedQuery(name = "Gewinnklasse.findByLastmodified", query = "SELECT g FROM Gewinnklasse g WHERE g.lastmodified = :lastmodified"),
	@NamedQuery(name = "Gewinnklasse.findByVersion", query = "SELECT g FROM Gewinnklasse g WHERE g.version = :version") })
public class Gewinnklasse implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "gewinnklasseid")
	private Long gewinnklasseid;
	@Basic(optional = false)
	@NotNull
	@Column(name = "gewinnklassenr")
	private int gewinnklassenr;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 5)
	@Column(name = "bezeichnunglatein")
	private String bezeichnunglatein;
	@Size(max = 255)
	@Column(name = "beschreibung")
	private String beschreibung;
	@Basic(optional = false)
	@NotNull
	@Column(name = "isabsolut")
	private boolean isabsolut;
	@Column(name = "betrag")
	private BigInteger betrag;
	@Column(name = "gueltigab")
	@Convert(converter = LocalDateAttributeConverter.class)
	private LocalDate gueltigab;
	@Column(name = "gueltigbis")
	@Convert(converter = LocalDateAttributeConverter.class)
	private LocalDate gueltigbis;

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
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "gewinnklasseid")
	private List<Lottoscheinziehung6aus49> lottoscheinziehung6aus49List;
	@OneToMany(mappedBy = "gewinnklasseidspiel77")
	private List<Lottoscheinziehung> lottoscheinziehungList;
	@OneToMany(mappedBy = "gewinnklasseidsuper6")
	private List<Lottoscheinziehung> lottoscheinziehungList1;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "gewinnklasseid")
	private List<Jackpot> jackpotList;
	@JoinColumn(name = "spielid", referencedColumnName = "spielid")
	@ManyToOne(optional = false)
	private Spiel spiel;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "gewinnklasseid")
	private List<Gewinnklasseziehungquote> gewinnklasseziehungquoteList;

	public Gewinnklasse() {
	}

	public Gewinnklasse(Long gewinnklasseid) {
		this.gewinnklasseid = gewinnklasseid;
	}

	public Gewinnklasse(Long gewinnklasseid, int gewinnklassenr,
		String bezeichnunglatein, boolean isabsolut, LocalDateTime created,
		LocalDateTime lastmodified) {
		this.gewinnklasseid = gewinnklasseid;
		this.gewinnklassenr = gewinnklassenr;
		this.bezeichnunglatein = bezeichnunglatein;
		this.isabsolut = isabsolut;
		this.created = created;
		this.lastmodified = lastmodified;
	}

	public Long getGewinnklasseid() {
		return gewinnklasseid;
	}

	public void setGewinnklasseid(Long gewinnklasseid) {
		this.gewinnklasseid = gewinnklasseid;
	}

	public int getGewinnklassenr() {
		return gewinnklassenr;
	}

	public void setGewinnklassenr(int gewinnklassenr) {
		this.gewinnklassenr = gewinnklassenr;
	}

	public String getBezeichnunglatein() {
		return bezeichnunglatein;
	}

	public void setBezeichnunglatein(String bezeichnunglatein) {
		this.bezeichnunglatein = bezeichnunglatein;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public boolean getIsabsolut() {
		return isabsolut;
	}

	public void setIsabsolut(boolean isabsolut) {
		this.isabsolut = isabsolut;
	}

	public BigInteger getBetrag() {
		return betrag;
	}

	public void setBetrag(BigInteger betrag) {
		this.betrag = betrag;
	}

	public LocalDate getGueltigab() {
		return gueltigab;
	}

	public void setGueltigab(LocalDate gueltigab) {
		this.gueltigab = gueltigab;
	}

	public LocalDate getGueltigbis() {
		return gueltigbis;
	}

	public void setGueltigbis(LocalDate gueltigbis) {
		this.gueltigbis = gueltigbis;
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

	public List<Lottoscheinziehung6aus49> getLottoscheinziehung6aus49List() {
		return lottoscheinziehung6aus49List;
	}

	public void setLottoscheinziehung6aus49List(
		List<Lottoscheinziehung6aus49> lottoscheinziehung6aus49List) {
		this.lottoscheinziehung6aus49List = lottoscheinziehung6aus49List;
	}

	public List<Lottoscheinziehung> getLottoscheinziehungList() {
		return lottoscheinziehungList;
	}

	public void setLottoscheinziehungList(
		List<Lottoscheinziehung> lottoscheinziehungList) {
		this.lottoscheinziehungList = lottoscheinziehungList;
	}

	public List<Lottoscheinziehung> getLottoscheinziehungList1() {
		return lottoscheinziehungList1;
	}

	public void setLottoscheinziehungList1(
		List<Lottoscheinziehung> lottoscheinziehungList1) {
		this.lottoscheinziehungList1 = lottoscheinziehungList1;
	}

	public List<Jackpot> getJackpotList() {
		return jackpotList;
	}

	public void setJackpotList(List<Jackpot> jackpotList) {
		this.jackpotList = jackpotList;
	}

	public Spiel getSpiel() {
		return spiel;
	}

	public void setSpiel(Spiel spielid) {
		this.spiel = spielid;
	}

	public List<Gewinnklasseziehungquote> getGewinnklasseziehungquoteList() {
		return gewinnklasseziehungquoteList;
	}

	public void setGewinnklasseziehungquoteList(
		List<Gewinnklasseziehungquote> gewinnklasseziehungquoteList) {
		this.gewinnklasseziehungquoteList = gewinnklasseziehungquoteList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (gewinnklasseid != null ? gewinnklasseid.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not
		// set
		if (!(object instanceof Gewinnklasse)) {
			return false;
		}
		Gewinnklasse other = (Gewinnklasse) object;
		if ((this.gewinnklasseid == null && other.gewinnklasseid != null)
			|| (this.gewinnklasseid != null
				&& !this.gewinnklasseid.equals(other.gewinnklasseid))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "wbs.corejpa.persistence.Gewinnklasse[ gewinnklasseid="
			+ gewinnklasseid + " ]";
	}

}
