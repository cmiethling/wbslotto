/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Spiel;

/**
 *
 * @author Günter
 */
@Local
public interface SpielFacadeLocal {

    void create(Spiel spiel);

    void edit(Spiel spiel);

    void remove(Spiel spiel);

    Spiel find(Object id);

    List<Spiel> findAll();

    List<Spiel> findRange(int[] range);

    int count();
    
}
