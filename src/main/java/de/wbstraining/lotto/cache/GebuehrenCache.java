package de.wbstraining.lotto.cache;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import de.wbstraining.lotto.persistence.dao.GebuehrFacadeLocal;
import de.wbstraining.lotto.persistence.model.Gebuehr;
import de.wbstraining.lotto.util.LottoDatumUtil;

// Wir überlegen uns wie man ermitteln kann, wie hoch die kosten für das einreichen eines lottoscheins sind
// Wir müssen berücksichtigen: die gültigen gebühren können sich während der laufzeit ändern.
// Datenbankzugriffe sind teuer
// Wie hoch schätzen sie den aufwand, wie hoch ist das Risiko, dass sie sich stark verschätzen?

/* Datenbankzugriff und abgreifen aller Gebührensätze die innerhalb des Zeitraums des Lottoscheins gültig sind, 
 * anfangen oder aufhören. (Frage ob Startzeitpunkt  vor und Endzeitpunkt nach einer Ziehung liegt)
 * 
 * (GETALL Gebühr WHERE FirstZiehnungdatum <= Gültigbis 
 * AND (FirstZiehnungdatum > Gültigab OR Gültigab <= Lastziehungsdatum) )
 * 
 * Eintragen dieser Gebührensätze in einer Liste, prüfen ob die Liste mehr als ein Element hat
 Wenn nicht Berechnung der Kosten nach Standard Muster (Multiplikation jedes einzelnen Kostenpunktes
 mit der Anzahl ihres auftretens, addition dieser Ergebnisse. Ausgabe Gesamtwert oder einer Map je nach Anforderung)
 
 Falls es verschiedene Gebührensätze sind, dann aufteilung Anzahl der Ziehungen in einem Gebührenzeitraum 
 und ausrechnen nach Standard Muster.
*/

// Von Herrn Zäpernick:
/*
 * eine singleton ejb baut in @PostConstruct eine Map auf:
 * - key: LocalDate; value: der für dieses datum gültige record der tabelle gebühr
 * 
 * sie bietet die folgenden methoden an:
 * - gebuehrByDatum()
 * - has GeguehrenWechesl(datumErsteZiehung, datumLetzteZiehung)
 * 
 * diese map wird bei anlegen neuer records in der tabelle gebuehr aktualisiert
 * (oder über einen ejb timer service)
 * 
 * AbgabeKostenErmitteln lässt sich eine referenz auf diese singleton ejb einspritzen
 * und wird in LottoscheinEinreichen eingespritzt, das seinerseits in LottoscheinEinreichenController
 * eingespritzt wird
 * 
 * aufwand: stunden
 * risiko: gering
 */

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
		date = LottoDatumUtil.naechsterZiehungstag(new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); 
		// Nächstes Ziehungsdatum
		gebuehrenliste = gebuehrfacadelocal.findAll(); // Importieren aller Gebühren aus Datenbank

		for (int i = 0; i < 48; i++) { // Schleife für maximale Anzahl möglicher Teilnahmen
			for (int j = 0; j < gebuehrenliste.size(); j++) { // Durchlaufen Datenbank

				if (date.isBefore( // aktuelles Gebührenobject finden
						gebuehrenliste.get(j).getGueltigbis().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
						&& date.isAfter(gebuehrenliste.get(j).getGueltigab().toInstant().atZone(ZoneId.systemDefault())
								.toLocalDate())) {
					aktuellegebuehr = gebuehrenliste.get(j); // Gebührenspeicherung
					break;
				}
			}
			gebuehrenmap.putIfAbsent(date, aktuellegebuehr); // Übergabe in die Map

			date = new java.sql.Date(LottoDatumUtil.naechsterZiehungstag(java.sql.Date.valueOf(date)).getTime())
					.toLocalDate(); // nächstes Ziehungsdatum für die nächste Schleife
		}
	}

	public Gebuehr gebuehrByDatum(LocalDate datum) {
		return gebuehrenmap.get(toLocalDate(LottoDatumUtil.ersterZiehungstag(toDate(datum), true, true, 18, 19)));
	}

	@Override
	public boolean hasGebuehrenWechsel(LocalDate datumErsteZiehung, LocalDate datumLetzteZiehung) {
		if (gebuehrenmap.get(datumErsteZiehung).getGebuehrid()
				.compareTo((gebuehrenmap.get(datumLetzteZiehung)).getGebuehrid()) == 0) {
			return false;
		} else { // Wenn die beiden Gebuehrenobjekte der ersten und letzten Ziehung identisch
					// sind,
					// gibt es keinen Gebührenwechsel
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
