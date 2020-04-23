package de.wbstraining.lotto.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * 1)
 * für die auftragsbestätigung, die dem lottospieler
 * nach dem einreichen eines lottoscheins zugeschickt wird,
 * muss detailliert aufgelistet werden, wie sich der
 * gesamtbetrag aus einzelnen positionen zusammensetzt.
 * 
 * ebenso sollte ein rest-client die option haben,
 * die kosten, die beim einreichen eines lottoscheins
 * entstehen würden, detailliert aufgelistet zu bekommen.
 * 
 * welche felder müsste die klasse KostenDetailedDto
 * dazu zur verfügung stellen?
 * -laufzeit, mi+sa, AnzTipps, Grundgebuehr erster Ziehungstag
 * -pro aktuelle Gebuehr: KostenProTipp + Spiel77, Super6 (+anzahl ziehungstage)
 */
@XmlRootElement
public class KostenDetailedDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public static class Item implements Serializable {
		private static final long serialVersionUID = 1L;
		// einsatz (einsatzProTipp, einsatzSpiel77, einsatzSuper6)
		private int grundEinsatz;
// -77+6:   (grundEinsatz * anzahlZiehungen) 
// -6aus49: (grundEinsatz * anzahlZiehungen * anzahlTippsProSchein)
		private int gesamtEinsatz;
		// anzahl der ziehungen, für die dieser einsatz gilt
		private int anzahlZiehungen;

		public int getGrundEinsatz() {
			return grundEinsatz;
		}

		public void setGrundEinsatz(int grundEinsatz) {
			this.grundEinsatz = grundEinsatz;
		}

		public int getGesamtEinsatz() {
			return gesamtEinsatz;
		}

		public void setGesamtEinsatz(int gesamtEinsatz) {
			this.gesamtEinsatz = gesamtEinsatz;
		}

		public int getAnzahlZiehungen() {
			return anzahlZiehungen;
		}

		public void setAnzahlZiehungen(int anzahlZiehungen) {
			this.anzahlZiehungen = anzahlZiehungen;
		}
	}

	private Date abgabeDatum;
	private Date datumErsteZiehung;

	private int laufzeit;
	private int anzahlTipps;
	private boolean isMittwoch;
	private boolean isSamstag;
	private boolean isSpiel77;
	private boolean isSuper6;

	private int grundgebuehr;
//        CM
	private int gesamtbetrag;

	// wir lassen zu, dass es während der laufzeit des schein
	// auch mehrere gebuehrenwechsel geben kann.

	// key: gueltigAb von gebuehr
	// value: Item
	private Map<LocalDate, Item> einsatzTipps;
	private Map<LocalDate, Item> einsatzSpiel77;
	private Map<LocalDate, Item> einsatzSuper6;

//        Konstruktor
	public KostenDetailedDto() {
		this.einsatzTipps = new HashMap<>();
		this.einsatzSpiel77 = new HashMap<>();
		this.einsatzSuper6 = new HashMap<>();
	}

	public void putEinsatz(Map<LocalDate, Item> einsatz, LocalDate gueltigAb,
		int grundEinsatz, int anzahlZiehungen) {

		KostenDetailedDto.Item item = new Item();
		final int gesamtEinsatz = einsatz == einsatzTipps
			? grundEinsatz * anzahlZiehungen * anzahlTipps // 6aus49
			: grundEinsatz * anzahlZiehungen; // Spiel77 + Super6

		item.setGesamtEinsatz(gesamtEinsatz);
		item.setAnzahlZiehungen(anzahlZiehungen);
		item.setGrundEinsatz(grundEinsatz);

		einsatz.put(gueltigAb, item);
	}

//        Getter+Setter
	public Date getAbgabeDatum() {
		return abgabeDatum;
	}

	public void setAbgabeDatum(Date abgabeDatum) {
		this.abgabeDatum = abgabeDatum;
	}

	public Date getDatumErsteZiehung() {
		return datumErsteZiehung;
	}

	public void setDatumErsteZiehung(Date datumErsteZiehung) {
		this.datumErsteZiehung = datumErsteZiehung;
	}

	public int getLaufzeit() {
		return laufzeit;
	}

	public void setLaufzeit(int laufzeit) {
		this.laufzeit = laufzeit;
	}

	public int getAnzahlTipps() {
		return anzahlTipps;
	}

	public void setAnzahlTipps(int anzahlTipps) {
		this.anzahlTipps = anzahlTipps;
	}

	public boolean isMittwoch() {
		return isMittwoch;
	}

	public void setMittwoch(boolean isMittwoch) {
		this.isMittwoch = isMittwoch;
	}

	public boolean isSamstag() {
		return isSamstag;
	}

	public void setSamstag(boolean isSamstag) {
		this.isSamstag = isSamstag;
	}

	public boolean isSpiel77() {
		return isSpiel77;
	}

	public void setSpiel77(boolean isSpiel77) {
		this.isSpiel77 = isSpiel77;
	}

	public boolean isSuper6() {
		return isSuper6;
	}

	public void setSuper6(boolean isSuper6) {
		this.isSuper6 = isSuper6;
	}

	public int getGrundgebuehr() {
		return grundgebuehr;
	}

	public void setGrundgebuehr(int grundgebuehr) {
		this.grundgebuehr = grundgebuehr;
	}

	public Map<LocalDate, Item> getEinsatzTipps() {
		return einsatzTipps;
	}

	public void setEinsatzTipps(Map<LocalDate, Item> einsatzTipps) {
		this.einsatzTipps = einsatzTipps;
	}

	public Map<LocalDate, Item> getEinsatzSpiel77() {
		return einsatzSpiel77;
	}

	public void setEinsatzSpiel77(Map<LocalDate, Item> einsatzSpiel77) {
		this.einsatzSpiel77 = einsatzSpiel77;
	}

	public Map<LocalDate, Item> getEinsatzSuper6() {
		return einsatzSuper6;
	}

	public void setEinsatzSuper6(Map<LocalDate, Item> einsatzSuper6) {
		this.einsatzSuper6 = einsatzSuper6;
	}

//        CM
	public int getGesamtbetrag() {
		return gesamtbetrag;
	}

	public void setGesamtbetrag(int gesamtbetrag) {
		this.gesamtbetrag = gesamtbetrag;
	}

}
