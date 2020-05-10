/*
 * To change this license header, choose License Headers in Project Properties.
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Martin
 */
@Entity
@Table(name = "user_roles")
@NamedQueries({
		@NamedQuery(name = "UserRoles.findAll", query = "SELECT ur FROM UserRoles ur"),
		@NamedQuery(name = "UserRoles.findByUserRoleId", query = "SELECT ur FROM UserRoles ur WHERE ur.userRolesId = :userRolesId"),
		@NamedQuery(name = "UserRoles.findByRole", query = "SELECT ur FROM UserRoles ur WHERE ur.role = :role"),
		@NamedQuery(name = "UserRoles.findByUserName", query = "SELECT ur FROM UserRoles ur WHERE ur.userName = :userName") })
public class UserRoles implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "user_rolesid")
	private Long userRolesId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "role")
	private String role;
	@Size(max = 200)
	@Column(name = "username")
	private String userName;
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	@ManyToOne(optional = false)
	private Users2 users2;

	public UserRoles() {
	}

	public UserRoles(Long userRolesId) {
		this.userRolesId = userRolesId;
	}

	public UserRoles(Long userRolesId, String role) {
		this.userRolesId = userRolesId;
		this.role = role;
	}

	public UserRoles(Long userRolesId, String role, String userName) {
		this.userRolesId = userRolesId;
		this.role = role;
		this.userName = userName;
	}

	public Long getUserRolesId() {
		return userRolesId;
	}

	public void setUserRolesId(Long userRolesId) {
		this.userRolesId = userRolesId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Users2 getUsers2() {
		return users2;
	}

	public void setUsers2(Users2 userId) {
		this.users2 = userId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (userRolesId != null ? userRolesId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not
		// set
		if (!(object instanceof UserRoles)) {
			return false;
		}
		UserRoles other = (UserRoles) object;
		if ((this.userRolesId == null && other.userRolesId != null)
				|| (this.userRolesId != null
						&& !this.userRolesId.equals(other.userRolesId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "wbs.corejpa.persistence.UserRoles[ userRolesId=" + userRolesId
				+ " ]";
	}

}
