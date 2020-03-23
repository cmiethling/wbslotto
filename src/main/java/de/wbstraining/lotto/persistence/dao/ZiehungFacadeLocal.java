/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Ziehung;

/**
 *
 * @author Günter
 */
@Local
public interface ZiehungFacadeLocal {

    void create(Ziehung ziehung);

    void edit(Ziehung ziehung);

    void remove(Ziehung ziehung);

    Ziehung find(Object id);

    List<Ziehung> findAll();

    List<Ziehung> findRange(int[] range);

    int count();
    
}
