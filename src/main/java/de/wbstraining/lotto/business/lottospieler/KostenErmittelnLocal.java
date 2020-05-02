package de.wbstraining.lotto.business.lottospieler;

import java.time.LocalDate;
import java.util.List;

import de.wbstraining.lotto.dto.KostenDetailedDto;
import de.wbstraining.lotto.dto.KostenDto;
import de.wbstraining.lotto.persistence.model.Gebuehr;
import de.wbstraining.lotto.persistence.model.Lottoschein;

public interface KostenErmittelnLocal {
	public int kostenErmitteln(Lottoschein schein);

// vom Client uebergebene Daten zur Kostenberechnung, und NUR diese Daten
	public int kostenErmitteln(KostenDto kosten);

	public KostenDetailedDto kostenErmittelnDetailed(KostenDto kosten);

	public Gebuehr findGebuehrForSpielTag(List<Gebuehr> gebuehren,
		LocalDate spielTag);
}