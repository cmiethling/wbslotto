/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.cache;

import java.util.Date;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Ziehung;

/**
 *
 * @author gz1
 */
@Local
public interface DBCacheLocal {
    
    public Kunde randomKunde();
    public Ziehung ziehungByDatum(Date datum);
    public void loadKundenUndZiehungen();
}
