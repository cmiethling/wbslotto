/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.wbstraining.lotto.persistence.dao.KundeFacadeLocal;
import de.wbstraining.lotto.persistence.model.Kunde;

/**
 *
 * @author Günter
 */
@Stateless
public class KundeFacade extends AbstractFacade<Kunde> implements KundeFacadeLocal {

    @PersistenceContext(unitName = "corejsfPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KundeFacade() {
        super(Kunde.class);
    }
    
    @Override
	public Kunde find(Object id) {
		Kunde kunde = super.find(id);
		kunde.getAdresseList().get(0);
		return kunde;
	}
    
}
