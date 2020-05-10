package de.wbstraining.lotto.cache;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import de.wbstraining.lotto.persistence.dao.AdresseFacadeLocal;
import de.wbstraining.lotto.persistence.model.Adresse;
import de.wbstraining.lotto.persistence.model.Kunde;


@Singleton
public class AdresseCache implements AdresseCacheLocal {
	@EJB
	private AdresseFacadeLocal adresseFacade;
	
	private List<Adresse> adressen;
	@PostConstruct
	public void init() {
		//System.out.println("im AdresseCacheInit");
		adressen = adresseFacade.findAll(); 
	}

	@Override
	public List<Adresse> getAdresseListByKundeId(Kunde kunde) {
		//System.out.println("im AdresseCacheMethode");
		List<Adresse> adresseList = new ArrayList<Adresse>();
		adressen.stream().filter(a-> a.getKunde().getKundeid() == kunde.getKundeid()).forEach(a -> adresseList.add(a));
		return adresseList;
	}

}
