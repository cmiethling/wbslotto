/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.wbstraining.lotto.persistence.dao.AdresseFacadeLocal;
import de.wbstraining.lotto.persistence.model.Adresse;

/**
 *
 * @author Günter
 */

// default
@TransactionManagement(TransactionManagementType.CONTAINER)

// das TransactionAttribute der von AbstractFacade geerbten methoden
// ist per default TransactionAttributeType.REQUIRED

@Stateless
public class AdresseFacade extends AbstractFacade<Adresse> implements AdresseFacadeLocal {

    @PersistenceContext(unitName = "corejsfPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdresseFacade() {
        super(Adresse.class);
    }
    
}
