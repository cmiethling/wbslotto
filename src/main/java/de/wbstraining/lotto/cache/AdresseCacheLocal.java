package de.wbstraining.lotto.cache;

import java.util.List;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Adresse;
import de.wbstraining.lotto.persistence.model.Kunde;



@Local
public interface AdresseCacheLocal {
	public List<Adresse> getAdresseListByKundeId(Kunde kunde);
}
