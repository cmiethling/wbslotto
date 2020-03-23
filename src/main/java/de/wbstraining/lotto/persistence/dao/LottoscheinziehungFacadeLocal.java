/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;

/**
 *
 * @author Günter
 */
@Local
public interface LottoscheinziehungFacadeLocal {

    void create(Lottoscheinziehung lottoscheinziehung);

    void edit(Lottoscheinziehung lottoscheinziehung);

    void remove(Lottoscheinziehung lottoscheinziehung);

    Lottoscheinziehung find(Object id);

    List<Lottoscheinziehung> findAll();

    List<Lottoscheinziehung> findRange(int[] range);

    int count();
    
}
