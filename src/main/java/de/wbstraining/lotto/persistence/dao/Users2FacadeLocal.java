/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Users2;

/**
 *
 * @author Günter
 */
@Local
public interface Users2FacadeLocal {

    void create(Users2 users);

    void edit(Users2 users);

    void remove(Users2 users);

    Users2 find(Object id);

    List<Users2> findAll();

    List<Users2> findRange(int[] range);

    int count();
    
}
