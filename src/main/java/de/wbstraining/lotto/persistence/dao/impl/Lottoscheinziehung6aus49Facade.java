/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.wbstraining.lotto.persistence.dao.Lottoscheinziehung6aus49FacadeLocal;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung6aus49;

/**
 *
 * @author Günter
 */
@Stateless
public class Lottoscheinziehung6aus49Facade extends AbstractFacade<Lottoscheinziehung6aus49> implements Lottoscheinziehung6aus49FacadeLocal {

    @PersistenceContext(unitName = "corejsfPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Lottoscheinziehung6aus49Facade() {
        super(Lottoscheinziehung6aus49.class);
    }
    
}
