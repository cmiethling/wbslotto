package de.wbstraining.lotto.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

import de.wbstraining.lotto.persistence.model.Ziehung;

// notwendig
@XmlRootElement(name = "ziehung")
public class ZiehungDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private LocalDate ziehungsDatum;
	private int[] ziehungsZahlen;
	private int spiel77;
	private int super6;
	private int superzahl;

	// muss
	public ZiehungDto() {

	}

	public ZiehungDto(Ziehung ziehung) {
		ziehungsDatum = ziehung.getZiehungsdatum();
		// TODO
		ziehungsZahlen = new int[] { 1, 2, 3, 4, 5, 6 };
		spiel77 = ziehung.getSpiel77();
		super6 = ziehung.getSuper6();
		superzahl = ziehung.getSuperzahl();
	}

	public LocalDate getZiehungsDatum() {
		return ziehungsDatum;
	}

	public void setZiehungsDatum(LocalDate ziehungsDatum) {
		this.ziehungsDatum = ziehungsDatum;
	}

	public int[] getZiehungsZahlen() {
		return ziehungsZahlen;
	}

	public void setZiehungsZahlen(int[] ziehungsZahlen) {
		// TODO
		this.ziehungsZahlen = ziehungsZahlen;
	}

	public int getSpiel77() {
		return spiel77;
	}

	public void setSpiel77(int spiel77) {
		this.spiel77 = spiel77;
	}

	public int getSuper6() {
		return super6;
	}

	public void setSuper6(int super6) {
		this.super6 = super6;
	}

	public int getSuperzahl() {
		return superzahl;
	}

	public void setSuperzahl(int superzahl) {
		this.superzahl = superzahl;
	}

	@Override
	public String toString() {
		return "ZiehungDto [ziehungsDatum=" + ziehungsDatum + ", ziehungsZahlen="
			+ Arrays.toString(ziehungsZahlen) + ", spiel77=" + spiel77 + ", super6="
			+ super6 + ", superzahl=" + superzahl + "]\n";
	}

}
