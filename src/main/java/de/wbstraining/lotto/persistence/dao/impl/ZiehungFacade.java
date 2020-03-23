/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Ziehung;

/**
 *
 * @author Günter
 */
@Stateless
public class ZiehungFacade extends AbstractFacade<Ziehung> implements ZiehungFacadeLocal {

    @PersistenceContext(unitName = "corejsfPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZiehungFacade() {
        super(Ziehung.class);
    }

	@Override
	public Ziehung find(Object id) {
		// TODO Auto-generated method stub
		Ziehung ziehung = super.find(id);
		ziehung.getLottoscheinziehungList().get(0);
		return ziehung;
	}
    
    
    
}
