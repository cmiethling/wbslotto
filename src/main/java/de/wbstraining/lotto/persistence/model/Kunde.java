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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.wbstraining.lotto.persistence.util.LocalDateTimeAttributeConverter;

/**
 *
 * @author Günter
 */
@Entity
@Table(name = "kunde")
@NamedQueries({
	@NamedQuery(name = "Kunde.findAll", query = "SELECT k FROM Kunde k"),
	@NamedQuery(name = "Kunde.findByKundeid", query = "SELECT k FROM Kunde k WHERE k.kundeid = :kundeid"),
	@NamedQuery(name = "Kunde.findByName", query = "SELECT k FROM Kunde k WHERE k.name = :name"),
	@NamedQuery(name = "Kunde.findByEmail", query = "SELECT k FROM Kunde k WHERE k.email = :email"),
	@NamedQuery(name = "Kunde.findByVorname", query = "SELECT k FROM Kunde k WHERE k.vorname = :vorname"),
	@NamedQuery(name = "Kunde.findByGuthaben", query = "SELECT k FROM Kunde k WHERE k.guthaben = :guthaben"),
	@NamedQuery(name = "Kunde.findByDispo", query = "SELECT k FROM Kunde k WHERE k.dispo = :dispo"),
	@NamedQuery(name = "Kunde.findByGesperrt", query = "SELECT k FROM Kunde k WHERE k.gesperrt = :gesperrt"),
	@NamedQuery(name = "Kunde.findByIsannahmestelle", query = "SELECT k FROM Kunde k WHERE k.isannahmestelle = :isannahmestelle"),
	@NamedQuery(name = "Kunde.findByCreated", query = "SELECT k FROM Kunde k WHERE k.created = :created"),
	@NamedQuery(name = "Kunde.findByLastmodified", query = "SELECT k FROM Kunde k WHERE k.lastmodified = :lastmodified"),
	@NamedQuery(name = "Kunde.findByVersion", query = "SELECT k FROM Kunde k WHERE k.version = :version") })
public class Kunde implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "kundeid")
	private Long kundeid;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "name")
	private String name;
	// @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	// message="Invalid email")//if the field contains email address consider
	// using this annotation to enforce field validation
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "email")
	private String email;
	@Size(max = 255)
	@Column(name = "vorname")
	private String vorname;
	@Column(name = "guthaben")
	private BigInteger guthaben;
	@Column(name = "dispo")
	private BigInteger dispo;
	@Column(name = "gesperrt")
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime gesperrt;
	@Column(name = "isannahmestelle")
	private Boolean isannahmestelle;
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
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "kundeid")
	private List<Lottoschein> lottoscheinList;
	@OneToOne(mappedBy = "kundeid")
	private Users users;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "kundeid")
	private List<Bankverbindung> bankverbindungList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "kundeid")
	private List<Adresse> adresseList;

	public Kunde() {
	}

	public Kunde(Long kundeid) {
		this.kundeid = kundeid;
	}

	public Kunde(Long kundeid, String name, String email, LocalDateTime created,
		LocalDateTime lastmodified) {
		this.kundeid = kundeid;
		this.name = name;
		this.email = email;
		this.created = created;
		this.lastmodified = lastmodified;
	}

	public Long getKundeid() {
		return kundeid;
	}

	public void setKundeid(Long kundeid) {
		this.kundeid = kundeid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public BigInteger getGuthaben() {
		return guthaben;
	}

	public void setGuthaben(BigInteger guthaben) {
		this.guthaben = guthaben;
	}

	public BigInteger getDispo() {
		return dispo;
	}

	public void setDispo(BigInteger dispo) {
		this.dispo = dispo;
	}

	public LocalDateTime getGesperrt() {
		return gesperrt;
	}

	public void setGesperrt(LocalDateTime gesperrt) {
		this.gesperrt = gesperrt;
	}

	public Boolean getIsannahmestelle() {
		return isannahmestelle;
	}

	public void setIsannahmestelle(Boolean isannahmestelle) {
		this.isannahmestelle = isannahmestelle;
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

	public List<Lottoschein> getLottoscheinList() {
		return lottoscheinList;
	}

	public void setLottoscheinList(List<Lottoschein> lottoscheinList) {
		this.lottoscheinList = lottoscheinList;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public List<Bankverbindung> getBankverbindungList() {
		return bankverbindungList;
	}

	public void setBankverbindungList(List<Bankverbindung> bankverbindungList) {
		this.bankverbindungList = bankverbindungList;
	}

	public List<Adresse> getAdresseList() {
		return adresseList;
	}

	public void setAdresseList(List<Adresse> adresseList) {
		this.adresseList = adresseList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (kundeid != null ? kundeid.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not
		// set
		if (!(object instanceof Kunde)) {
			return false;
		}
		Kunde other = (Kunde) object;
		if ((this.kundeid == null && other.kundeid != null)
			|| (this.kundeid != null && !this.kundeid.equals(other.kundeid))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "wbs.corejpa.persistence.Kunde[ kundeid=" + kundeid + " ]";
	}

}
