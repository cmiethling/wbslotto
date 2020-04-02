/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.wbstraining.lotto.persistence.dao.Users2FacadeLocal;
import de.wbstraining.lotto.persistence.model.Users2;

/**
 *
 * @author Alem, Martin
 */
@Stateless
public class Users2Facade extends AbstractFacade<Users2> implements Users2FacadeLocal {

    @PersistenceContext(unitName = "corejsfPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    
    @Override
	public Users2 find(Object id) {
		Users2 users2 = super.find(id);
		return users2;
	}    
    
    public Users2Facade() {
        super(Users2.class);
    }
    
}
