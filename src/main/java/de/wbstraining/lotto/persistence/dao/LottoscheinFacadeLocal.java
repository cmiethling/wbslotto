/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Lottoschein;

/**
 *
 * @author Günter
 */
@Local
public interface LottoscheinFacadeLocal {

    void create(Lottoschein lottoschein);

    void edit(Lottoschein lottoschein);

    void remove(Lottoschein lottoschein);

    Lottoschein find(Object id);

    List<Lottoschein> findAll();

    List<Lottoschein> findRange(int[] range);

    int count();
    
}
