/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.web.lottospieler.controller;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.component.tabview.Tab;
import org.primefaces.event.TabChangeEvent;

import de.wbstraining.lotto.business.lottogesellschaft.LottoscheinEinreichenLocal;
import de.wbstraining.lotto.persistence.dao.KundeFacadeLocal;

/**
 *
 * @author gz1
 */

enum Zahlverfahren{BANKEINZUG, KREDITKARTE, PAYPAL};

@RequestScoped
@Named
public class ZahlungsAbwicklungController {

    @EJB
    private LottoscheinEinreichenLocal lottoscheinEinreichenLocal;

    @EJB
    private KundeFacadeLocal kundeFacadeLocal;
    

    private String zahlungsoption = "Bank";
    private String iban;
    private String bic;
    private String bankName;
    private String kontoinhaber;
    
    private String kreditkarteName;
    private String kreditkarteNummer;
    private String kreditkarteAblaufdatum;
    
    private Zahlverfahren zahlverfahren = Zahlverfahren.BANKEINZUG; 
    //private 
    
    public String getIban() {
		return iban;
	}


	public void setIban(String iban) {
		this.iban = iban;
	}


	public String getBic() {
		return bic;
	}


	public void setBic(String bic) {
		this.bic = bic;
	}


	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getKontoinhaber() {
		return kontoinhaber;
	}


	public void setKontoinhaber(String kontoinhaber) {
		this.kontoinhaber = kontoinhaber;
	}


	public String getKreditkarteName() {
		return kreditkarteName;
	}


	public void setKreditkarteName(String kreditkarteName) {
		this.kreditkarteName = kreditkarteName;
	}


	public String getKreditkarteNummer() {
		return kreditkarteNummer;
	}


	public void setKreditkarteNummer(String kreditkarteNummer) {
		this.kreditkarteNummer = kreditkarteNummer;
	}


	public String getKreditkarteAblaufdatum() {
		return kreditkarteAblaufdatum;
	}


	public void setKreditkarteAblaufdatum(String kreditkarteAblaufdatum) {
		this.kreditkarteAblaufdatum = kreditkarteAblaufdatum;
	}


	public String getPaypalName() {
		return paypalName;
	}


	public void setPaypalName(String paypalName) {
		this.paypalName = paypalName;
	}



	private String paypalName;

    public String getZahlungsoption() {
		return zahlungsoption;
	}


	public void setZahlungsoption(String zahlungsoption) {
		this.zahlungsoption = zahlungsoption;
	}
	
	public String onChange(TabChangeEvent event) {
		Tab activeTab = event.getTab();
		
		if (activeTab.getId().contentEquals("bank")) zahlverfahren = Zahlverfahren.BANKEINZUG;
		if (activeTab.getId().contentEquals("creditcard")) zahlverfahren = Zahlverfahren.KREDITKARTE;
		if (activeTab.getId().contentEquals("paypal")) zahlverfahren = Zahlverfahren.PAYPAL;
		
		System.out.println(zahlverfahren);
		
		return "ok";
	}
	
    public String senden() {
    	
      return "ok";
     
  }






	
}
