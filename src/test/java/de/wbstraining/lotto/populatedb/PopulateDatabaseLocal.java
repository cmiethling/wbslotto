package de.wbstraining.lotto.populatedb;

import javax.ejb.Local;

@Local
public interface PopulateDatabaseLocal {
	public void populateDatabase();
}
