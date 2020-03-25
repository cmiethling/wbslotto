/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.util;

import java.util.List;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Gewinnklasse;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Ziehung;

/**
 *
 * @author gz1
 */
@Local
public interface JPAQueriesLocal {
    public List<Kunde> dynamic_untyped_query();
    public List<Kunde> dynamic_typed_query();
    public List<Kunde> named_query();
    public List<Kunde> named_query_with_parameters();
    public List<Kunde> criteria_api_1();
    public List<Kunde> criteria_api_2();
    public List<Gewinnklasse> findAllGewinnklasse();
    
    List<Gewinnklasse> findGewinnklassenForZiehung(Ziehung ziehung);
  
    
}
