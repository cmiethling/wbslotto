package de.wbstraining.lotto.populatedb;

import javax.ejb.EJB;
import javax.ejb.Stateless;


@Stateless
public class PopulateDatabase implements PopulateDatabaseLocal {

	@EJB
	private PopulateKundeLocal populateKunde;
	
	@EJB
	private PopulateGebuehrLocal populateGebuehr;
	
	@EJB
	private PopulateSpielUndGewinnklasseLocal populateSpielUndGewinnklasse;
	
	@EJB
	private PopulateZiehungUndLottoscheinLocal populateZiehungUndLottoschein;
	
	@Override
	public void populateDatabase() {
		populateGebuehr.populateGebuehr();
		populateKunde.populateKunde();
		populateSpielUndGewinnklasse.populateSpielUndGewinnklasse();
		populateZiehungUndLottoschein.populateZiehungUndLottoschein();
	}
}
