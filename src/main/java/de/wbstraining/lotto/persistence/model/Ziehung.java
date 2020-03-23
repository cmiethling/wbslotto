/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Günter
 */
// @XmlRootElement(name = "ziehung")
@Entity
@Table(name = "ziehung")
@NamedQueries({
    @NamedQuery(name = "Ziehung.findAll", query = "SELECT z FROM Ziehung z"),
    @NamedQuery(name = "Ziehung.findByZiehungid", query = "SELECT z FROM Ziehung z WHERE z.ziehungid = :ziehungid"),
    @NamedQuery(name = "Ziehung.findByZiehungsdatum", query = "SELECT z FROM Ziehung z WHERE z.ziehungsdatum = :ziehungsdatum"),
    @NamedQuery(name = "Ziehung.findByZahlenalsbits", query = "SELECT z FROM Ziehung z WHERE z.zahlenalsbits = :zahlenalsbits"),
    @NamedQuery(name = "Ziehung.findBySuperzahl", query = "SELECT z FROM Ziehung z WHERE z.superzahl = :superzahl"),
    @NamedQuery(name = "Ziehung.findBySpiel77", query = "SELECT z FROM Ziehung z WHERE z.spiel77 = :spiel77"),
    @NamedQuery(name = "Ziehung.findBySuper6", query = "SELECT z FROM Ziehung z WHERE z.super6 = :super6"),
    @NamedQuery(name = "Ziehung.findByEinsatzlotto", query = "SELECT z FROM Ziehung z WHERE z.einsatzlotto = :einsatzlotto"),
    @NamedQuery(name = "Ziehung.findByEinsatzspiel77", query = "SELECT z FROM Ziehung z WHERE z.einsatzspiel77 = :einsatzspiel77"),
    @NamedQuery(name = "Ziehung.findByEinsatzsuper6", query = "SELECT z FROM Ziehung z WHERE z.einsatzsuper6 = :einsatzsuper6"),
    @NamedQuery(name = "Ziehung.findByStatus", query = "SELECT z FROM Ziehung z WHERE z.status = :status"),
    @NamedQuery(name = "Ziehung.findByCreated", query = "SELECT z FROM Ziehung z WHERE z.created = :created"),
    @NamedQuery(name = "Ziehung.findByLastmodified", query = "SELECT z FROM Ziehung z WHERE z.lastmodified = :lastmodified"),
    @NamedQuery(name = "Ziehung.findByVersion", query = "SELECT z FROM Ziehung z WHERE z.version = :version")})
public class Ziehung implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ziehungid")
    private Long ziehungid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ziehungsdatum")
    @Temporal(TemporalType.DATE)
    private Date ziehungsdatum;
    @Column(name = "zahlenalsbits")
    private BigInteger zahlenalsbits;
    @Column(name = "superzahl")
    private Integer superzahl;
    @Column(name = "spiel77")
    private Integer spiel77;
    @Column(name = "super6")
    private Integer super6;
    @Column(name = "einsatzlotto")
    private BigInteger einsatzlotto;
    @Column(name = "einsatzspiel77")
    private BigInteger einsatzspiel77;
    @Column(name = "einsatzsuper6")
    private BigInteger einsatzsuper6;
    @Column(name = "status")
    private Integer status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lastmodified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastmodified;
    @Column(name = "version")
    private Integer version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ziehungid")
    private List<Lottoscheinziehung> lottoscheinziehungList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ziehungid")
    private List<Jackpot> jackpotList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ziehungid")
    private List<Gewinnklasseziehungquote> gewinnklasseziehungquoteList;

    public Ziehung() {
    }

    public Ziehung(Long ziehungid) {
        this.ziehungid = ziehungid;
    }

    public Ziehung(Long ziehungid, Date ziehungsdatum, Date created, Date lastmodified) {
        this.ziehungid = ziehungid;
        this.ziehungsdatum = ziehungsdatum;
        this.created = created;
        this.lastmodified = lastmodified;
    }

    public Long getZiehungid() {
        return ziehungid;
    }

    public void setZiehungid(Long ziehungid) {
        this.ziehungid = ziehungid;
    }

    public Date getZiehungsdatum() {
        return ziehungsdatum;
    }

    public void setZiehungsdatum(Date ziehungsdatum) {
        this.ziehungsdatum = ziehungsdatum;
    }

    public BigInteger getZahlenalsbits() {
        return zahlenalsbits;
    }

    public void setZahlenalsbits(BigInteger zahlenalsbits) {
        this.zahlenalsbits = zahlenalsbits;
    }

    public Integer getSuperzahl() {
        return superzahl;
    }

    public void setSuperzahl(Integer superzahl) {
        this.superzahl = superzahl;
    }

    public Integer getSpiel77() {
        return spiel77;
    }

    public void setSpiel77(Integer spiel77) {
        this.spiel77 = spiel77;
    }

    public Integer getSuper6() {
        return super6;
    }

    public void setSuper6(Integer super6) {
        this.super6 = super6;
    }

    public BigInteger getEinsatzlotto() {
        return einsatzlotto;
    }

    public void setEinsatzlotto(BigInteger einsatzlotto) {
        this.einsatzlotto = einsatzlotto;
    }

    public BigInteger getEinsatzspiel77() {
        return einsatzspiel77;
    }

    public void setEinsatzspiel77(BigInteger einsatzspiel77) {
        this.einsatzspiel77 = einsatzspiel77;
    }

    public BigInteger getEinsatzsuper6() {
        return einsatzsuper6;
    }

    public void setEinsatzsuper6(BigInteger einsatzsuper6) {
        this.einsatzsuper6 = einsatzsuper6;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(Date lastmodified) {
        this.lastmodified = lastmodified;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Lottoscheinziehung> getLottoscheinziehungList() {
        return lottoscheinziehungList;
    }

    public void setLottoscheinziehungList(List<Lottoscheinziehung> lottoscheinziehungList) {
        this.lottoscheinziehungList = lottoscheinziehungList;
    }

    public List<Jackpot> getJackpotList() {
        return jackpotList;
    }

    public void setJackpotList(List<Jackpot> jackpotList) {
        this.jackpotList = jackpotList;
    }

    public List<Gewinnklasseziehungquote> getGewinnklasseziehungquoteList() {
        return gewinnklasseziehungquoteList;
    }

    public void setGewinnklasseziehungquoteList(List<Gewinnklasseziehungquote> gewinnklasseziehungquoteList) {
        this.gewinnklasseziehungquoteList = gewinnklasseziehungquoteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ziehungid != null ? ziehungid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ziehung)) {
            return false;
        }
        Ziehung other = (Ziehung) object;
        if ((this.ziehungid == null && other.ziehungid != null) || (this.ziehungid != null && !this.ziehungid.equals(other.ziehungid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "wbs.corejpa.persistence.Ziehung[ ziehungid=" + ziehungid + " ]";
    }
    
}
