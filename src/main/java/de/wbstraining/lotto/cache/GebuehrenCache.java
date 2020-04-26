package de.wbstraining.lotto.cache;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import de.wbstraining.lotto.persistence.dao.GebuehrFacadeLocal;
import de.wbstraining.lotto.persistence.model.Gebuehr;
import de.wbstraining.lotto.util.LottoDatum8Util;
import de.wbstraining.lotto.util.LottoDatumUtil;

@Singleton
public class GebuehrenCache implements GebuehrenCacheLocal {

	@EJB
	private GebuehrFacadeLocal gebuehrfacadelocal;

	private LocalDate date;

	private Map<LocalDate, Gebuehr> gebuehrenmap;

	private List<Gebuehr> gebuehrenliste;
	private Gebuehr aktuellegebuehr;

	@Override
	@PostConstruct
	public void init() {
		gebuehrenmap = new TreeMap<LocalDate, Gebuehr>();
		LocalTime timeNow = LocalTime.now();
		date = LottoDatum8Util.ersterZiehungstag(LocalDate.now(), timeNow, true, true,
			18, 19);

		// Nächstes Ziehungsdatum
		gebuehrenliste = gebuehrfacadelocal.findAll(); // Importieren aller Gebühren
																										// aus Datenbank

		for (int i = 0; i < 48; i++) { // Schleife für maximale Anzahl möglicher
																		// Teilnahmen
			for (int j = 0; j < gebuehrenliste.size(); j++) { // Durchlaufen Datenbank

				if (date.isBefore( // aktuelles Gebührenobject finden
					gebuehrenliste.get(j)
						.getGueltigbis())
					&& date.isAfter(gebuehrenliste.get(j)
						.getGueltigab())) {
					aktuellegebuehr = gebuehrenliste.get(j); // Gebührenspeicherung
					break;
				}
			}
			gebuehrenmap.putIfAbsent(date, aktuellegebuehr); // Übergabe in die Map

			// naechstes Ziehungsdatum für die naechste Schleife
			date = LottoDatum8Util.ersterZiehungstag(date.plusDays(1), timeNow, true,
				true, 18, 19);
		}
	}

	@Override
	public Gebuehr gebuehrByDatum(LocalDate datum) {
		return gebuehrenmap.get(toLocalDate(
			LottoDatumUtil.ersterZiehungstag(toDate(datum), true, true, 18, 19)));
	}

	@Override
	public boolean hasGebuehrenWechsel(LocalDate datumErsteZiehung,
		LocalDate datumLetzteZiehung) {
		if (gebuehrenmap.get(datumErsteZiehung)
			.getGebuehrid()
			.compareTo((gebuehrenmap.get(datumLetzteZiehung)).getGebuehrid()) == 0) {
			return false;
		} else { // Wenn die beiden Gebuehrenobjekte der ersten und letzten Ziehung
							// identisch sind, gibt es keinen Gebührenwechsel
			return true;
		}
	}

	@Override
	public void aktualisieremap() {
		this.init();
	}

	@Override
	public Map<LocalDate, Gebuehr> getGebuehrenmap() {
		return gebuehrenmap;
	}

	private LocalDate toLocalDate(Date dateToConvert) {
		return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
	}

	private Date toDate(LocalDate dateToConvert) {
		return java.sql.Date.valueOf(dateToConvert);
	}

}
