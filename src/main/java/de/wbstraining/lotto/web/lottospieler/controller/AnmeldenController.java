/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.web.lottospieler.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import de.wbstraining.lotto.persistence.dao.KundeFacadeLocal;

/**
 *
 * @author gz1
 */
@Named(value = "anmeldenController")
@RequestScoped

public class AnmeldenController implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private KundeFacadeLocal kundeFacadeLocal; // nur private KundeFacadeLocal kundeFacadeLocal => weil die
												// untergeordneten Tabellen mit angelegt werden

	// oder @Email
	// @Pattern(regexp="^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	private String email;

	private String passwort;

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
