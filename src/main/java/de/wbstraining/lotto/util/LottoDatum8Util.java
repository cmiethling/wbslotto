package de.wbstraining.lotto.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public final class LottoDatum8Util {

	public static LocalDate ersterZiehungstag(LocalDate abgabeDatum,
		LocalTime abgabeZeit, boolean isMittwoch, boolean isSamstag,
		int abgabeschlussMittwoch, int abgabeschlussSamstag) {
		LocalDateTime abgabe = LocalDateTime.of(abgabeDatum, abgabeZeit);
		if (!isMittwoch && !isSamstag) {
			throw new IllegalArgumentException("invalid isMittwoch and isSamstag...");
		} else if (abgabeschlussMittwoch < 0 || abgabeschlussMittwoch > 24) {
			throw new IllegalArgumentException("invalid abgabeschlussMittwoch...");
		} else if (abgabeschlussSamstag < 0 || abgabeschlussSamstag > 24) {
			throw new IllegalArgumentException("invalid abgabeschlussSamstag...");
//			
		} else if (isMittwoch && isSamstag) {
			return mittwochUndSamstag(abgabe, abgabeschlussMittwoch,
				abgabeschlussSamstag);
		} else if (isMittwoch) {
			return nurEinTag(abgabe, abgabeschlussSamstag, DayOfWeek.WEDNESDAY);
		} else {
			return nurEinTag(abgabe, abgabeschlussSamstag, DayOfWeek.SATURDAY);
		}
	}

	public static LocalDate naechsterZiehungstag(LocalDate datum,
		LocalTime zeit) {
		return ersterZiehungstag(datum, zeit, true, true, 18, 18);
	}

	public static List<LocalDate> ziehungsTage(LocalDate datum, LocalTime zeit,
		boolean isMittwoch, boolean isSamstag, int abgabeschlussMittwoch,
		int abgabeschlussSamstag, int laufzeit) {

		if (laufzeit <= 0) {
			throw new IllegalArgumentException("invalid laufzeit...");
		}

		List<LocalDate> ziehungsTage = new ArrayList<>();
		LocalDate aktDatum = datum;
		int anzTage = 0;
		if (isSamstag && isMittwoch) {
			anzTage = laufzeit * 2;
		} else {
			anzTage = laufzeit;
		}
		for (int i = 0; i < anzTage; i++) {
			ziehungsTage.add(ersterZiehungstag(aktDatum, zeit, isMittwoch, isSamstag,
				abgabeschlussMittwoch, abgabeschlussSamstag));
			aktDatum = ziehungsTage.get(i)
				.plusDays(1);
		}
		return ziehungsTage;
	}

//##############################################################
//	########### Helper Meth ###############################

	private static LocalDate mittwochUndSamstag(LocalDateTime abgabeDatum,
		int abgabeschlussMittwoch, int abgabeschlussSamstag) {
		DayOfWeek mi = DayOfWeek.WEDNESDAY;
		DayOfWeek sa = DayOfWeek.SATURDAY;
		DayOfWeek tag = abgabeDatum.getDayOfWeek();
		int stunde = abgabeDatum.getHour();
		if (tag == DayOfWeek.WEDNESDAY && stunde < abgabeschlussMittwoch) {
			return abgabeDatum.toLocalDate();
		} else if (tag == sa && stunde < abgabeschlussSamstag) {
			return abgabeDatum.toLocalDate();
		} else if ((tag == mi && stunde >= abgabeschlussMittwoch)
			|| tag == DayOfWeek.THURSDAY || tag == DayOfWeek.FRIDAY) {
			return naechsterWochentag(abgabeDatum, sa);
		} else {
			return naechsterWochentag(abgabeDatum, mi);
		}
	}

	private static LocalDate nurEinTag(LocalDateTime abgabeDatum,
		int abgabeschlussTag, DayOfWeek tag) {

		if (abgabeDatum.getDayOfWeek() == tag
			&& abgabeDatum.getHour() < abgabeschlussTag) {
			return abgabeDatum.toLocalDate();
		} else {
			return naechsterWochentag(abgabeDatum, tag);
		}
	}

	private static LocalDate naechsterWochentag(LocalDateTime abgabeDatum,
		DayOfWeek tag) {
		return abgabeDatum.with(TemporalAdjusters.next(tag))
			.toLocalDate();
	}

}
