/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.web.lottospieler.controller;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import de.wbstraining.lotto.business.lottogesellschaft.LottoscheinEinreichenLocal;

/**
 *
 * @author gz1
 */
@RequestScoped
@Named
public class StartSeiteController {

    @EJB
    private LottoscheinEinreichenLocal lottoscheinEinreichenLocal;




    public String senden() {
    	


  

        
		
        return "ok";
       
    }
}
