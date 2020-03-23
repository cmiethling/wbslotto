/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Jackpot;

/**
 *
 * @author Günter
 */
@Local
public interface JackpotFacadeLocal {

    void create(Jackpot jackpot);

    void edit(Jackpot jackpot);

    void remove(Jackpot jackpot);

    Jackpot find(Object id);

    List<Jackpot> findAll();

    List<Jackpot> findRange(int[] range);

    int count();
    
}
