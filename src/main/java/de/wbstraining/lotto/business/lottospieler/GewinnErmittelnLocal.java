package de.wbstraining.lotto.business.lottospieler;

import java.time.LocalDate;

import javax.ejb.Local;

import de.wbstraining.lotto.dto.GewinnDetailedDto;

@Local
public interface GewinnErmittelnLocal {
	public GewinnDetailedDto gewinn(long belegNr, LocalDate ziehungsDatum);
}
