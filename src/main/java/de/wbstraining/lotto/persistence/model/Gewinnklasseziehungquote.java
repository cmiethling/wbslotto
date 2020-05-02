/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.model;

import java.io.Serializable;
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
@Table(name = "gewinnklasseziehungquote")
@NamedQueries({
	@NamedQuery(name = "Gewinnklasseziehungquote.findAll", query = "SELECT g FROM Gewinnklasseziehungquote g"),
	@NamedQuery(name = "Gewinnklasseziehungquote.findByGewinnklasseziehungquoteid", query = "SELECT g FROM Gewinnklasseziehungquote g WHERE g.gewinnklasseziehungquoteid = :gewinnklasseziehungquoteid"),
	@NamedQuery(name = "Gewinnklasseziehungquote.findByAnzahlgewinner", query = "SELECT g FROM Gewinnklasseziehungquote g WHERE g.anzahlgewinner = :anzahlgewinner"),
	@NamedQuery(name = "Gewinnklasseziehungquote.findByQuote", query = "SELECT g FROM Gewinnklasseziehungquote g WHERE g.quote = :quote"),
	@NamedQuery(name = "Gewinnklasseziehungquote.findByCreated", query = "SELECT g FROM Gewinnklasseziehungquote g WHERE g.created = :created"),
	@NamedQuery(name = "Gewinnklasseziehungquote.findByLastmodified", query = "SELECT g FROM Gewinnklasseziehungquote g WHERE g.lastmodified = :lastmodified"),
	@NamedQuery(name = "Gewinnklasseziehungquote.findByVersion", query = "SELECT g FROM Gewinnklasseziehungquote g WHERE g.version = :version") })
public class Gewinnklasseziehungquote implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "gewinnklasseziehungquoteid")
	private Long gewinnklasseziehungquoteid;
	@Basic(optional = false)
	@NotNull
	@Column(name = "anzahlgewinner")
	private int anzahlgewinner;
	@Basic(optional = false)
	@NotNull
	@Column(name = "quote")
	private long quote;
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
	@JoinColumn(name = "gewinnklasseid", referencedColumnName = "gewinnklasseid")
	@ManyToOne(optional = false)
	private Gewinnklasse gewinnklasseid;
	@JoinColumn(name = "ziehungid", referencedColumnName = "ziehungid")
	@ManyToOne(optional = false)
	private Ziehung ziehungid;

	public Gewinnklasseziehungquote() {
	}

	public Gewinnklasseziehungquote(Long gewinnklasseziehungquoteid) {
		this.gewinnklasseziehungquoteid = gewinnklasseziehungquoteid;
	}

	public Gewinnklasseziehungquote(Long gewinnklasseziehungquoteid,
		int anzahlgewinner, long quote, LocalDateTime created,
		LocalDateTime lastmodified) {
		this.gewinnklasseziehungquoteid = gewinnklasseziehungquoteid;
		this.anzahlgewinner = anzahlgewinner;
		this.quote = quote;
		this.created = created;
		this.lastmodified = lastmodified;
	}

	public Long getGewinnklasseziehungquoteid() {
		return gewinnklasseziehungquoteid;
	}

	public void setGewinnklasseziehungquoteid(Long gewinnklasseziehungquoteid) {
		this.gewinnklasseziehungquoteid = gewinnklasseziehungquoteid;
	}

	public int getAnzahlgewinner() {
		return anzahlgewinner;
	}

	public void setAnzahlgewinner(int anzahlgewinner) {
		this.anzahlgewinner = anzahlgewinner;
	}

	public long getQuote() {
		return quote;
	}

	public void setQuote(long quote) {
		this.quote = quote;
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

	public Gewinnklasse getGewinnklasseid() {
		return gewinnklasseid;
	}

	public void setGewinnklasseid(Gewinnklasse gewinnklasseid) {
		this.gewinnklasseid = gewinnklasseid;
	}

	public Ziehung getZiehungid() {
		return ziehungid;
	}

	public void setZiehungid(Ziehung ziehungid) {
		this.ziehungid = ziehungid;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (gewinnklasseziehungquoteid != null
			? gewinnklasseziehungquoteid.hashCode()
			: 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not
		// set
		if (!(object instanceof Gewinnklasseziehungquote)) {
			return false;
		}
		Gewinnklasseziehungquote other = (Gewinnklasseziehungquote) object;
		if ((this.gewinnklasseziehungquoteid == null
			&& other.gewinnklasseziehungquoteid != null)
			|| (this.gewinnklasseziehungquoteid != null
				&& !this.gewinnklasseziehungquoteid
					.equals(other.gewinnklasseziehungquoteid))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "wbs.corejpa.persistence.Gewinnklasseziehungquote[ gewinnklasseziehungquoteid="
			+ gewinnklasseziehungquoteid + " ]";
	}

}
