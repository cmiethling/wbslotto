/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.wbstraining.lotto.persistence.dao.GewinnklasseFacadeLocal;
import de.wbstraining.lotto.persistence.model.Gewinnklasse;

/**
 *
 * @author Günter
 */
@Stateless
public class GewinnklasseFacade extends AbstractFacade<Gewinnklasse> implements GewinnklasseFacadeLocal {

    @PersistenceContext(unitName = "corejsfPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GewinnklasseFacade() {
        super(Gewinnklasse.class);
    }
    
}
