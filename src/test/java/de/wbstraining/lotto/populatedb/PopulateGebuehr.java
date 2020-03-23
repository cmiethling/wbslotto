/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.populatedb;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.wbstraining.lotto.persistence.dao.GebuehrFacadeLocal;
import de.wbstraining.lotto.persistence.model.Gebuehr;



/**
 *
 * @author gz1
 */
@Stateless
public class PopulateGebuehr implements PopulateGebuehrLocal {

    @EJB
    private GebuehrFacadeLocal gebuehrFacadeLocal;
    
    @Override
    public void populateGebuehr() {
        Date date = new Date();
        GregorianCalendar cal = new GregorianCalendar(2022, Calendar.DECEMBER, 31);
        Gebuehr gebuehr = new Gebuehr();
        gebuehr.setGrundgebuehr(60);
        gebuehr.setEinsatzprotipp(100);
        gebuehr.setEinsatzspiel77(250);
        gebuehr.setEinsatzsuper6(125);
        gebuehr.setGueltigab(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime());
        gebuehr.setGueltigbis(cal.getTime());
        gebuehr.setCreated(date);
        gebuehr.setLastmodified(date);
        gebuehrFacadeLocal.create(gebuehr);
    }
}
