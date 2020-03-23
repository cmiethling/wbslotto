/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.web.lottospieler.controller;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.validation.constraints.Pattern;

import org.primefaces.event.FlowEvent;

import de.wbstraining.lotto.persistence.dao.GroupsFacadeLocal;
import de.wbstraining.lotto.persistence.dao.KundeFacadeLocal;
import de.wbstraining.lotto.persistence.dao.UsersFacadeLocal;
import de.wbstraining.lotto.persistence.model.Adresse;
import de.wbstraining.lotto.persistence.model.Bankverbindung;
import de.wbstraining.lotto.persistence.model.Kunde;

/**
 *
 * @author gz1
 */

@Named(value = "registrierungController")
/*@RequestScoped*/
@SessionScoped

public class RegistrierungController implements Serializable{

	private static final long serialVersionUID = 1L;

    @EJB
    private KundeFacadeLocal kundeFacadeLocal;  //nur private KundeFacadeLocal kundeFacadeLocal => weil die untergeordneten Tabellen mit angelegt werden
    
    // TODO cachen
    @EJB
    private GroupsFacadeLocal groupsFacade;
    
    @EJB
    UsersFacadeLocal usersFacade;
    
    //Tabelle Kunde
    private String name;
    private String vorname;
    // oder @Email
    @Pattern(regexp="^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    private String benutzername;
    private String passwort;
    
    //Tabelle Adresse
    private String adresszusatz;
    private String strasse;
    private String hausnummer;
    private String plz;
    private String ort;
    private String land;
    
    //Tabelle Bankverbindung
    private String iban;
    private String bic;
    private String bankname;
    private String kontoinhaber;
    
    private boolean skip;
    
 

    public void setName(String name) {
        this.name = name;
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
    	
        Date date = new Date();
        
        //Kundendaten
    	Kunde kunde = new Kunde();   
    	kunde.setDispo(BigInteger.ZERO);
    	kunde.setGesperrt(null);
    	kunde.setGuthaben(BigInteger.ZERO);
    	kunde.setIsannahmestelle(Boolean.FALSE);
        kunde.setName(name);
        kunde.setVorname(vorname);
        kunde.setEmail(email);
        kunde.setCreated(date);
        kunde.setLastmodified(date);
        
        //Adressdaten
        Adresse adresse;
        kunde.setAdresseList(new ArrayList<Adresse>());
        adresse = new Adresse();
        adresse.setKundeid(kunde);
        adresse.setAdressenr(1);
        adresse.setStrasse(strasse);
        adresse.setHausnummer(hausnummer);
        adresse.setAdresszusatz(adresszusatz);
        adresse.setPlz(plz);
        adresse.setOrt(ort);
        adresse.setLand(land);
        adresse.setCreated(date);
        adresse.setLastmodified(date);
        kunde.setAdresseList(Arrays.asList(adresse));
        

        // Bankverbindung anlegen
        Bankverbindung bankverbindung;
        bankverbindung = new Bankverbindung();
        bankverbindung.setKundeid(kunde);
        bankverbindung.setBankverbindungnr(1);
        bankverbindung.setIban(iban);
        bankverbindung.setBic(bic);
        bankverbindung.setName(bankname);
        bankverbindung.setKontoinhaber(kunde.getVorname() + " " + kunde.getName());
        bankverbindung.setCreated(date);
        bankverbindung.setLastmodified(date);
        kunde.setBankverbindungList(Arrays.asList(bankverbindung));
        
        // verbindung zu users und groups
        // TODO bearbeiten, harte codierung der id rausnehmen, cachen
        // TODO allgemeine cache-strategie umsetzen
        
        /*
        Groups group = groupsFacade.find(0L);
        
        Users user = new Users();
        user.setKunde(kunde);
        
        // TODO hash-algorithmus auf passwort anwenden
        user.setPassword(passwort);
        List<Groups> groupsList = new ArrayList<>();
        groupsList.add(group);
        user.setGroupsList(groupsList);
        */
        
        kundeFacadeLocal.create(kunde);
		
        return "ok";
       
    }
    public boolean isSkip() {
        return skip;
    }
 
    public void setSkip(boolean skip) {
        this.skip = skip;
    }
    
    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }
    
    public void save() {
    	FacesMessage msg = new FacesMessage("Successful", "Welcome :" + getVorname());
        FacesContext.getCurrentInstance().addMessage(null, msg);
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
