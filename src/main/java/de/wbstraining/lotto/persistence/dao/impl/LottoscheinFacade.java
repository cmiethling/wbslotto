/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.wbstraining.lotto.persistence.dao.LottoscheinFacadeLocal;
import de.wbstraining.lotto.persistence.model.Lottoschein;

/**
 *
 * @author Günter
 */
@Stateless
public class LottoscheinFacade extends AbstractFacade<Lottoschein> implements LottoscheinFacadeLocal {

    @PersistenceContext(unitName = "corejsfPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LottoscheinFacade() {
        super(Lottoschein.class);
    }
    
}
