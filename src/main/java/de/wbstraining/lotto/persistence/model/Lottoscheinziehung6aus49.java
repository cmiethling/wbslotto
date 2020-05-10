/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.wbstraining.lotto.persistence.util.LocalDateTimeAttributeConverter;

/**
 *
 * @author Günter
 */
@Entity
@Table(name = "lottoscheinziehung6aus49")
@NamedQueries({
	@NamedQuery(name = "Lottoscheinziehung6aus49.findAll", query = "SELECT l FROM Lottoscheinziehung6aus49 l"),
	@NamedQuery(name = "Lottoscheinziehung6aus49.findByLottoscheinziehung6aus49id", query = "SELECT l FROM Lottoscheinziehung6aus49 l WHERE l.lottoscheinziehung6aus49id = :lottoscheinziehung6aus49id"),
	@NamedQuery(name = "Lottoscheinziehung6aus49.findByTippnr", query = "SELECT l FROM Lottoscheinziehung6aus49 l WHERE l.tippnr = :tippnr"),
	@NamedQuery(name = "Lottoscheinziehung6aus49.findByCreated", query = "SELECT l FROM Lottoscheinziehung6aus49 l WHERE l.created = :created"),
	@NamedQuery(name = "Lottoscheinziehung6aus49.findByLastmodified", query = "SELECT l FROM Lottoscheinziehung6aus49 l WHERE l.lastmodified = :lastmodified"),
	@NamedQuery(name = "Lottoscheinziehung6aus49.findByVersion", query = "SELECT l FROM Lottoscheinziehung6aus49 l WHERE l.version = :version"),
	@NamedQuery(name = "Lottoscheinziehung6aus49.findByGewinn", query = "SELECT l FROM Lottoscheinziehung6aus49 l WHERE l.gewinn = :gewinn") })
public class Lottoscheinziehung6aus49 implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "lottoscheinziehung6aus49id")
	private Long lottoscheinziehung6aus49id;
	@Basic(optional = false)
	@NotNull
	@Column(name = "tippnr")
	private int tippnr;
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
	@Column(name = "gewinn")
	private BigInteger gewinn;
	@JoinColumn(name = "gewinnklasseid", referencedColumnName = "gewinnklasseid")
	@ManyToOne(optional = false)
	private Gewinnklasse gewinnklasse;
	@JoinColumn(name = "lottoscheinziehungid", referencedColumnName = "lottoscheinziehungid")
	@ManyToOne(optional = false)
	private Lottoscheinziehung lottoscheinziehung;

	public Lottoscheinziehung6aus49() {
	}

	public Lottoscheinziehung6aus49(Long lottoscheinziehung6aus49id) {
		this.lottoscheinziehung6aus49id = lottoscheinziehung6aus49id;
	}

	public Lottoscheinziehung6aus49(Long lottoscheinziehung6aus49id, int tippnr,
		LocalDateTime created, LocalDateTime lastmodified) {
		this.lottoscheinziehung6aus49id = lottoscheinziehung6aus49id;
		this.tippnr = tippnr;
		this.created = created;
		this.lastmodified = lastmodified;
	}

	public Long getLottoscheinziehung6aus49id() {
		return lottoscheinziehung6aus49id;
	}

	public void setLottoscheinziehung6aus49id(Long lottoscheinziehung6aus49id) {
		this.lottoscheinziehung6aus49id = lottoscheinziehung6aus49id;
	}

	public int getTippnr() {
		return tippnr;
	}

	public void setTippnr(int tippnr) {
		this.tippnr = tippnr;
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

	public BigInteger getGewinn() {
		return gewinn;
	}

	public void setGewinn(BigInteger gewinn) {
		this.gewinn = gewinn;
	}

	public Gewinnklasse getGewinnklasse() {
		return gewinnklasse;
	}

	public void setGewinnklasse(Gewinnklasse gewinnklasseid) {
		this.gewinnklasse = gewinnklasseid;
	}

	public Lottoscheinziehung getLottoscheinziehung() {
		return lottoscheinziehung;
	}

	public void setLottoscheinziehung(Lottoscheinziehung lottoscheinziehungid) {
		this.lottoscheinziehung = lottoscheinziehungid;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (lottoscheinziehung6aus49id != null
			? lottoscheinziehung6aus49id.hashCode()
			: 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not
		// set
		if (!(object instanceof Lottoscheinziehung6aus49)) {
			return false;
		}
		Lottoscheinziehung6aus49 other = (Lottoscheinziehung6aus49) object;
		if ((this.lottoscheinziehung6aus49id == null
			&& other.lottoscheinziehung6aus49id != null)
			|| (this.lottoscheinziehung6aus49id != null
				&& !this.lottoscheinziehung6aus49id
					.equals(other.lottoscheinziehung6aus49id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "wbs.corejpa.persistence.Lottoscheinziehung6aus49[ lottoscheinziehung6aus49id="
			+ lottoscheinziehung6aus49id + " ]";
	}

}
