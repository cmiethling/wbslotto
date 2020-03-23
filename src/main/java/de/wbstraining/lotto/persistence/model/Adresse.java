/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "adresse")
@NamedQueries({
    @NamedQuery(name = "Adresse.findAll", query = "SELECT a FROM Adresse a"),
    @NamedQuery(name = "Adresse.findByAdresseid", query = "SELECT a FROM Adresse a WHERE a.adresseid = :adresseid"),
    @NamedQuery(name = "Adresse.findByAdressenr", query = "SELECT a FROM Adresse a WHERE a.adressenr = :adressenr"),
    @NamedQuery(name = "Adresse.findByStrasse", query = "SELECT a FROM Adresse a WHERE a.strasse = :strasse"),
    @NamedQuery(name = "Adresse.findByHausnummer", query = "SELECT a FROM Adresse a WHERE a.hausnummer = :hausnummer"),
    @NamedQuery(name = "Adresse.findByAdresszusatz", query = "SELECT a FROM Adresse a WHERE a.adresszusatz = :adresszusatz"),
    @NamedQuery(name = "Adresse.findByPlz", query = "SELECT a FROM Adresse a WHERE a.plz = :plz"),
    @NamedQuery(name = "Adresse.findByOrt", query = "SELECT a FROM Adresse a WHERE a.ort = :ort"),
    @NamedQuery(name = "Adresse.findByLand", query = "SELECT a FROM Adresse a WHERE a.land = :land"),
    @NamedQuery(name = "Adresse.findByCreated", query = "SELECT a FROM Adresse a WHERE a.created = :created"),
    @NamedQuery(name = "Adresse.findByLastmodified", query = "SELECT a FROM Adresse a WHERE a.lastmodified = :lastmodified"),
    @NamedQuery(name = "Adresse.findByVersion", query = "SELECT a FROM Adresse a WHERE a.version = :version")})
public class Adresse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "adresseid")
    private Long adresseid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "adressenr")
    private int adressenr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "strasse")
    private String strasse;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "hausnummer")
    private String hausnummer;
    @Size(max = 255)
    @Column(name = "adresszusatz")
    private String adresszusatz;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "plz")
    private String plz;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ort")
    private String ort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "land")
    private String land;
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
    @JoinColumn(name = "kundeid", referencedColumnName = "kundeid")
    @ManyToOne(optional = false)
    private Kunde kundeid;

    public Adresse() {
    }

    public Adresse(Long adresseid) {
        this.adresseid = adresseid;
    }

    public Adresse(Long adresseid, int adressenr, String strasse, String hausnummer, String plz, String ort, String land, Date created, Date lastmodified) {
        this.adresseid = adresseid;
        this.adressenr = adressenr;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
        this.land = land;
        this.created = created;
        this.lastmodified = lastmodified;
    }

    public Long getAdresseid() {
        return adresseid;
    }

    public void setAdresseid(Long adresseid) {
        this.adresseid = adresseid;
    }

    public int getAdressenr() {
        return adressenr;
    }

    public void setAdressenr(int adressenr) {
        this.adressenr = adressenr;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getAdresszusatz() {
        return adresszusatz;
    }

    public void setAdresszusatz(String adresszusatz) {
        this.adresszusatz = adresszusatz;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
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

    public Kunde getKundeid() {
        return kundeid;
    }

    public void setKundeid(Kunde kundeid) {
        this.kundeid = kundeid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (adresseid != null ? adresseid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Adresse)) {
            return false;
        }
        Adresse other = (Adresse) object;
        if ((this.adresseid == null && other.adresseid != null) || (this.adresseid != null && !this.adresseid.equals(other.adresseid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "wbs.corejpa.persistence.Adresse[ adresseid=" + adresseid + " ]";
    }
    
}
