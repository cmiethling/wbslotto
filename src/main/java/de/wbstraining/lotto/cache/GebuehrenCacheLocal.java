package de.wbstraining.lotto.cache;

import java.time.LocalDate;
import java.util.Map;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Gebuehr;

@Local
public interface GebuehrenCacheLocal {
	
	public Gebuehr gebuehrByDatum(LocalDate datum);

	public boolean hasGebuehrenWechsel(LocalDate datumErsteZiehung, LocalDate datumLetzteZiehung);

	public void aktualisieremap();

	public Map<LocalDate, Gebuehr> getGebuehrenmap();
	
	public void init();

}
