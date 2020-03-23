/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.populatedb;

import javax.ejb.Local;

/**
 *
 * @author gz1
 */
@Local
public interface PopulateSpielUndGewinnklasseLocal {
    public void populateSpielUndGewinnklasse();
}
