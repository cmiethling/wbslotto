/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Gewinnklasseziehungquote;

/**
 *
 * @author Günter
 */
@Local
public interface GewinnklasseziehungquoteFacadeLocal {

    void create(Gewinnklasseziehungquote gewinnklasseziehungquote);

    void edit(Gewinnklasseziehungquote gewinnklasseziehungquote);

    void remove(Gewinnklasseziehungquote gewinnklasseziehungquote);

    Gewinnklasseziehungquote find(Object id);

    List<Gewinnklasseziehungquote> findAll();

    List<Gewinnklasseziehungquote> findRange(int[] range);

    int count();
    
}
