package de.wbstraining.lotto.business.lottogesellschaft;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import de.wbstraining.lotto.persistence.model.Adresse;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.persistence.model.Users;



public class AuftragKunde implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Kunde kunde;
	private List<Adresse> adresseList;
	public AuftragKunde(Kunde kunde , List<Adresse> adresseList) {
		this.kunde = kunde;
		this.adresseList = adresseList;
	}

	public Long getKundeid() {
		return kunde.getKundeid();
	}

	public String getName() {
		return kunde.getName();
	}

	public String getEmail() {
		return kunde.getEmail();
	}

	public String getVorname() {
		return kunde.getVorname();
	}

	public BigInteger getGuthaben() {
		return kunde.getGuthaben();
	}

	public BigInteger getDispo() {
		return kunde.getDispo();
	}

	public Date getGesperrt() {
		return kunde.getGesperrt();
	}

	public Boolean getIsannahmestelle() {
		return kunde.getIsannahmestelle();
	}

	public Date getCreated() {
		return kunde.getCreated();
	}

	public Date getLastmodified() {
		return kunde.getLastmodified();
	}

	public List<Lottoschein> getLottoscheinList() {
		return kunde.getLottoscheinList();
	}

	public Users getUsers() {
		return kunde.getUsers();
	}

	public List<Adresse> getAdresseList() {
		return adresseList;
	}
	
	

}
