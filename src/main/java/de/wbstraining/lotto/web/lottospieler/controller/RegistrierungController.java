/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.web.lottospieler.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.Pattern;

import org.primefaces.event.FlowEvent;

import de.wbstraining.lotto.persistence.dao.KundeFacadeLocal;
import de.wbstraining.lotto.persistence.dao.UserRolesFacadeLocal;
import de.wbstraining.lotto.persistence.dao.Users2FacadeLocal;
import de.wbstraining.lotto.persistence.model.Adresse;
import de.wbstraining.lotto.persistence.model.Bankverbindung;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Users2;
import de.wbstraining.lotto.util.UtilSecurity;

/**
 *
 * @author gz1
 */

@Named(value = "registrierungController")
/* @RequestScoped */
@SessionScoped

public class RegistrierungController implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private KundeFacadeLocal kundeFacadeLocal; // nur private KundeFacadeLocal
																							// kundeFacadeLocal => weil die

	@EJB
	private UserRolesFacadeLocal userRolesFacadeLocal;

	@EJB
	Users2FacadeLocal users2FacadeLocal;
	// untergeordneten Tabellen mit angelegt werden

	// Tabelle Kunde
	private String name;
	private String vorname;
	// oder @Email
	@Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	private String email;
	private String benutzername;
	private String passwort;
	private Kunde kunde;

	// Tabelle Adresse
	private String adresszusatz;
	private String strasse;
	private String hausnummer;
	private String plz;
	private String ort;
	private String land;

	// Tabelle Bankverbindung
	private String iban;
	private String bic;
	private String bankname;
	private String kontoinhaber;

	private boolean skip;

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	private String successMessage;

	public Kunde getKunde() {
		return kunde;
	}

	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBenutzerName(String benutzername) {
		this.benutzername = benutzername;
	}

	public String getBenutzerName() {
		return benutzername;
	}

	public String getName() {
		return name;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getVorname() {
		return vorname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public String getAdresszusatz() {
		return adresszusatz;
	}

	public void setAdresszusatz(String adresszusatz) {
		this.adresszusatz = adresszusatz;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setHausnummer(String hausnummer) {
		this.hausnummer = hausnummer;
	}

	public String getHausnummer() {
		return hausnummer;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getPlz() {
		return plz;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getOrt() {
		return ort;
	}

	public void setLand(String land) {
		this.land = land;
	}

	public String getLand() {
		return land;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getIban() {
		return iban;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getBic() {
		return bic;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBankname() {
		return bankname;
	}

	public void setKontoinhaber(String kontoinhaber) {
		this.kontoinhaber = kontoinhaber;
	}

	public String getKontoinhaber() {
		return kontoinhaber;
	}

	public String senden() {

		LocalDateTime now = LocalDateTime.now();

		// Kundendaten
		Kunde kunde = new Kunde();
		kunde.setDispo(0L);
		kunde.setGesperrt(null);
		kunde.setGuthaben(0L);
		kunde.setIsannahmestelle(Boolean.FALSE);
		kunde.setName(name);
		kunde.setVorname(vorname);
		kunde.setEmail(email);
		kunde.setCreated(now);
		kunde.setLastmodified(now);

		// Adressdaten
		Adresse adresse;
		kunde.setAdresseList(new ArrayList<Adresse>());
		adresse = new Adresse();
		adresse.setKunde(kunde);
		adresse.setAdressenr(1);
		adresse.setStrasse(strasse);
		adresse.setHausnummer(hausnummer);
		adresse.setAdresszusatz(adresszusatz);
		adresse.setPlz(plz);
		adresse.setOrt(ort);
		adresse.setLand(land);
		adresse.setCreated(now);
		adresse.setLastmodified(now);
		kunde.setAdresseList(Arrays.asList(adresse));

		// Bankverbindung anlegen
		Bankverbindung bankverbindung;
		bankverbindung = new Bankverbindung();
		bankverbindung.setKunde(kunde);
		bankverbindung.setBankverbindungnr(1);
		bankverbindung.setIban(iban);
		bankverbindung.setBic(bic);
		bankverbindung.setName(bankname);
		bankverbindung.setKontoinhaber(kunde.getVorname() + " " + kunde.getName());
		bankverbindung.setCreated(now);
		bankverbindung.setLastmodified(now);
		kunde.setBankverbindungList(Arrays.asList(bankverbindung));

		Users2 user = new Users2();
		user.setUsername(name);
		String securePassword = UtilSecurity.str2Md(passwort);
		System.out.println(securePassword);
		user.setPassword(securePassword);
//		user.setKundeid(kunde);

		kundeFacadeLocal.create(kunde);
		user.setKunde(kunde);
		users2FacadeLocal.create(user);

		// ToDo Verbindung User zu UserRoles aufbauen; entsprechend UserRoles
		// persistieren
		// -----------------------------------------------------------------------------------
//		user.setUserRolesList(Arrays.asList(userRoles)); 		
//		UserRoles userRoles = new UserRoles();
//		userRoles.setRole("Benutzer");
//		userRoles.setUserName(benutzername);
//		userRoles.setUserid(user);
//		userRolesFacadeLocal.create(userRoles);

		save();

		return "ok";

	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String onFlowProcess(FlowEvent event) {
		if (skip) {
			skip = false; // reset in case user goes back
			return "confirm";
		} else {
			return event.getNewStep();
		}
	}

	public void save() {
		// FacesMessage msg = new FacesMessage("Erfolgreiche Registrierung",
		// "Herzlichen Willkommen bei WBS Lotto :" + getVorname()+" "+getName());
		// FacesContext.getCurrentInstance().addMessage(null, msg);

		successMessage = "Herzlich Willkommen bei WBS Lotto : " + getVorname() + " "
				+ getName() + " !!";
	}

	public String getBenutzername() {
		return benutzername;
	}

	public void setBenutzername(String benutzername) {
		this.benutzername = benutzername;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

}
