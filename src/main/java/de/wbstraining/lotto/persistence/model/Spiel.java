/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.model;

import java.io.Serializable;
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
import javax.validation.constraints.Size;

/**
 *
 * @author Günter
 */
@Entity
@Table(name = "spiel")
@NamedQueries({
    @NamedQuery(name = "Spiel.findAll", query = "SELECT s FROM Spiel s"),
    @NamedQuery(name = "Spiel.findBySpielid", query = "SELECT s FROM Spiel s WHERE s.spielid = :spielid"),
    @NamedQuery(name = "Spiel.findByName", query = "SELECT s FROM Spiel s WHERE s.name = :name"),
    @NamedQuery(name = "Spiel.findByBeschreibung", query = "SELECT s FROM Spiel s WHERE s.beschreibung = :beschreibung"),
    @NamedQuery(name = "Spiel.findByPfadanleitung", query = "SELECT s FROM Spiel s WHERE s.pfadanleitung = :pfadanleitung"),
    @NamedQuery(name = "Spiel.findByCreated", query = "SELECT s FROM Spiel s WHERE s.created = :created"),
    @NamedQuery(name = "Spiel.findByLastmodified", query = "SELECT s FROM Spiel s WHERE s.lastmodified = :lastmodified"),
    @NamedQuery(name = "Spiel.findByVersion", query = "SELECT s FROM Spiel s WHERE s.version = :version")})
public class Spiel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "spielid")
    private Long spielid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1023)
    @Column(name = "beschreibung")
    private String beschreibung;
    @Size(max = 255)
    @Column(name = "pfadanleitung")
    private String pfadanleitung;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spielid")
    private List<Gewinnklasse> gewinnklasseList;

    public Spiel() {
    }

    public Spiel(Long spielid) {
        this.spielid = spielid;
    }

    public Spiel(Long spielid, String name, String beschreibung, Date created, Date lastmodified) {
        this.spielid = spielid;
        this.name = name;
        this.beschreibung = beschreibung;
        this.created = created;
        this.lastmodified = lastmodified;
    }

    public Long getSpielid() {
        return spielid;
    }

    public void setSpielid(Long spielid) {
        this.spielid = spielid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getPfadanleitung() {
        return pfadanleitung;
    }

    public void setPfadanleitung(String pfadanleitung) {
        this.pfadanleitung = pfadanleitung;
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

    public List<Gewinnklasse> getGewinnklasseList() {
        return gewinnklasseList;
    }

    public void setGewinnklasseList(List<Gewinnklasse> gewinnklasseList) {
        this.gewinnklasseList = gewinnklasseList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (spielid != null ? spielid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Spiel)) {
            return false;
        }
        Spiel other = (Spiel) object;
        if ((this.spielid == null && other.spielid != null) || (this.spielid != null && !this.spielid.equals(other.spielid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "wbs.corejpa.persistence.Spiel[ spielid=" + spielid + " ]";
    }
    
}
