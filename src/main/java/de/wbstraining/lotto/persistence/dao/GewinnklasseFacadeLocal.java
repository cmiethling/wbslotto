/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Gewinnklasse;

/**
 *
 * @author Günter
 */
@Local
public interface GewinnklasseFacadeLocal {

    void create(Gewinnklasse gewinnklasse);

    void edit(Gewinnklasse gewinnklasse);

    void remove(Gewinnklasse gewinnklasse);

    Gewinnklasse find(Object id);

    List<Gewinnklasse> findAll();

    List<Gewinnklasse> findRange(int[] range);

    int count();
    
}
