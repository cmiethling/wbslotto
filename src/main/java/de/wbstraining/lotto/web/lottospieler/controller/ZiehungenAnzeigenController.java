/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.web.lottospieler.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Ziehung;
import de.wbstraining.lotto.util.LottoUtil;

 
@Named
@ApplicationScoped  //wenn deployment wird eine Instanz der Tabelle erzeugt
public class ZiehungenAnzeigenController implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB  //Einspritzung von ZiehungFacadeLocal damit auf der Datenbank zugegriffen werden kann
    private ZiehungFacadeLocal ziehungFacadeLocal;
     
    private List<Ziehung> ziehungen;  //erstell eine List von Ziehungen
     
    @PostConstruct  //wird genau einmal aufgerufen
    public void init() {
        ziehungen = ziehungFacadeLocal.findAll();  //geh in die Datenbank und gib alle ziehungen in die Liste ein
    }
     
    public List<Ziehung> getZiehungen() {  //gib die Liste Ziehungen aus
        return ziehungen;
    }
    
    public String ziehungszahlen(Ziehung ziehung) {
        return LottoUtil.tippAsString(ziehung.getZahlenalsbits().longValue());
    }
}