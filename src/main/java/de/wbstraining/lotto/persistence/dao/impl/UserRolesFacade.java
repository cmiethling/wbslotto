/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.wbstraining.lotto.persistence.dao.UserRolesFacadeLocal;
import de.wbstraining.lotto.persistence.model.UserRoles;

/**
 *
 * @author Martin
 */
@Stateless
public class UserRolesFacade extends AbstractFacade<UserRoles> implements UserRolesFacadeLocal {

    @PersistenceContext(unitName = "corejsfPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserRolesFacade() {
        super(UserRoles.class);
    }
    
}
