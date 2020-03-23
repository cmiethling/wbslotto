package de.wbstraining.lotto.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class LottoUtil {

	// neue von Alexander Reichl
	public static List<Integer> tippAsArrayList(long tippAsBits) {
		List<Integer> sb = new ArrayList<>();
		for (int n = 0; n < 50; n++) {
			if ((tippAsBits & (1L << n)) != 0) {
				sb.add(n);
			}
		}
		return sb;
	}

	// wir schreiben die methode randomTipp().
	// sie liefert einen long, in dem genau 6 bits an bitpositionen
	// zwischen 1 und 49 auf 1 gesetzt sind. die einserbits
	// sollen nach dem zufallsprinzip verteilt sein.

	public static long randomTipp() {
		long tippAsBits = 0;
		int zahl;
		while (Long.bitCount(tippAsBits) != 6) {
			// oder ThreadLocalRandom...
			zahl = (int) (Math.random() * 49 + 1);
			tippAsBits |= (1L << zahl);
		}
		return tippAsBits;
	}

	// wir schreiben die methode countMatches(). sie zählt die
	// anzahl der übereinstimmenden einsers-bits der beiden long-parameter.
	public static int countMatches(long tipp1, long tipp2) {
		return Long.bitCount(tipp1 & tipp2);
	}

	// wir schreiben die methode createTipp(). sie erzeugt einen long-wert
	// mit 6 einserbits. der aufrufer kann bis zu 6 zahlen vorgeben. die
	// fehlenden zahlen werden per randomizer erzeugt.

	// einige korrekte aufrufe von createTipp():
	// bsp.: createTipp(1,2,3)
	// bsp.: createTipp(1,2,3,4,5)

	// einige inkorrekte aufrufe von createTipp():
	// createTipp(1,3,50)
	// createTipp(1,2,3,4,5,6,7);
	// createTipp(1,2,3,1);

	public static long createTipp(int... zahlen) throws IllegalArgumentException {
		if (zahlen.length > 6) {
			throw new IllegalArgumentException("es sind höchstens 6 zahlen erlaubt...");
		}
		long tipp = 0;
		for (int zahl : zahlen) {
			if (zahl < 1 || zahl > 49) {
				throw new IllegalArgumentException("alle zahlen müssen zwischen 1 und 49 liegen...");
			}
			if ((tipp & (1L << zahl)) != 0) {
				throw new IllegalArgumentException("duplikate sind nicht erlaubt...");
			}
			tipp |= (1L << zahl);
		}
		while (Long.bitCount(tipp) != 6) {
			tipp |= (1L << (int) (Math.random() * 49 + 1));
		}
		return tipp;
	}

	// wir schreiben die methode gkl6Aus49(). sie ermittelt, zu welcher gewinnklasse
	// ein tipp im spiel 6 aus 49 gehört.
	public static int gkl6Aus49(long ziehungszahlenAlsBits, long tippAlsBits, boolean hasMatchingSuperzahl) {
		int gewinnKlasse = 0;
		int treffer = Long.bitCount(ziehungszahlenAlsBits & tippAlsBits);

		if ((treffer == 2 && hasMatchingSuperzahl) || treffer > 2) {
			if (hasMatchingSuperzahl) {
				gewinnKlasse = 13 - (treffer * 2);
			} else {
				gewinnKlasse = 12 - ((treffer - 1) * 2);
			}
		}
		return gewinnKlasse;

	}

	// wir schreiben die methode gklSpiel77(). sie ermittelt, zu welcher
	// gewinnklasse ein lottoschein im spiel 77 gehört.
	public static int gklSpiel77(int ziehungSpiel77, int losNummer) {
		int teiler = 10;
		int gewinnKlasse = 0;
		for (; teiler <= 10_000_000 && ziehungSpiel77 % teiler == losNummer % teiler; teiler *= 10) {
		}
		if (teiler > 10) {
			gewinnKlasse = 10 - Integer.toString(teiler).length();

		}
		return gewinnKlasse;
	}

	// wir schreiben die methode gklSuper6(). sie ermittelt, zu welcher gewinnklasse
	// ein lottoschein im spiel super 6 gehört.
	public static int gklSuper6(int ziehungSuper6, int losNummer) {
		int teiler = 10;
		int gewinnKlasse = 0;
		for (; teiler <= 1_000_000 && ziehungSuper6 % teiler == losNummer % teiler; teiler *= 10) {
		}
		if (teiler > 10) {
			gewinnKlasse = 9 - Integer.toString(teiler).length();
		}
		return gewinnKlasse;
	}

	// wir schreiben die methode tippAsString(). sie liefert zu einem als long-wert
	// gegebenen tipp (mit einserbits, die zahlen repräsentieren) eine brauchbare
	// string-repräsentation
	public static String tippAsString(long tippAsBits) {
		StringBuilder sb = new StringBuilder();
		for (int n = 1, counter = 0; counter <= 6 && n < 64; n++) {
			if ((tippAsBits & (1L << n)) != 0) {
				sb.append(n).append(" ");
				counter++;
			}
		}
		return sb.toString().trim();

	}

	public static byte[] randomTippsAsByteArray(int anzahl) {
		long[] tipps = new long[anzahl];
		for (int i = 0; i < anzahl; i++) {
			tipps[i] = randomTipp();
		}
		return ByteLongConverter.longToByte(tipps);
	}
	
	public static String encodeTippsBase64(byte[] tipps) {
		return Base64.getEncoder().encodeToString(tipps);
	}
	
	public static byte[] decodeTippsBase64(String tipps) {
		return Base64.getDecoder().decode(tipps);
	}
}
