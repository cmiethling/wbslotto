/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Kunde;

/**
 *
 * @author Günter
 */
@Local
public interface KundeFacadeLocal {

    void create(Kunde kunde);

    void edit(Kunde kunde);

    void remove(Kunde kunde);

    Kunde find(Object id);

    List<Kunde> findAll();

    List<Kunde> findRange(int[] range);

    int count();
    
}
