/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.populatedb;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

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
		LocalDateTime now = LocalDateTime.now();
		LocalDate cal = LocalDate.of(2015, Month.JANUARY, 1);

		Gebuehr gebuehr = new Gebuehr();
		gebuehr.setGrundgebuehr(60);
		gebuehr.setEinsatzprotipp(100);
		gebuehr.setEinsatzspiel77(250);
		gebuehr.setEinsatzsuper6(125);
		gebuehr.setGueltigab(cal);
		gebuehr.setGueltigbis(cal.plusYears(10)
			.minusDays(1));
		gebuehr.setCreated(now);
		gebuehr.setLastmodified(now);
		gebuehrFacadeLocal.create(gebuehr);
	}
}
