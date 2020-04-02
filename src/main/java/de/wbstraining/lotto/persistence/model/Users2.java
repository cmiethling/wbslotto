/*
 https://github.com/join * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Alem, Martin
 */
@Entity
@Table(name = "users2")
@NamedQueries({
    @NamedQuery(name = "Users2.findAll", query = "SELECT u FROM Users2 u"),
    @NamedQuery(name = "Users2.findByUserId", query = "SELECT u FROM Users2 u WHERE u.userId = :userId"),    
    @NamedQuery(name = "Users2.findByUsername", query = "SELECT u FROM Users2 u WHERE u.username = :username"),
    @NamedQuery(name = "Users2.findByPassword", query = "SELECT u FROM Users2 u WHERE u.password = :password")})
public class Users2 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "username")
    private String username;
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "password")
    private String password;
    
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
//	private List<UserRoles> userRolesList;

    @JoinColumn(name = "kundeid", referencedColumnName = "kundeid")
    @OneToOne
    private Kunde kundeid; 
    
    public Users2() {
    }

    public Users2(Integer userId) {
        this.userId = userId;
    }

    public Users2(Integer userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Kunde getKundeid() {
        return kundeid;
    }

    public void setKundeid(Kunde kundeid) {
        this.kundeid = kundeid;
    } 

//    public List<UserRoles> getUserRolesList() {
//        return userRolesList;
//    }
//
//    public void setUserRolesList(List<UserRoles> userRolesList) {
//        this.userRolesList = userRolesList;
//    }    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users2 other = (Users2) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "wbs.corejpa.persistence.Users[ userId=" + userId + " ]";
    }
    
    
}
