/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.testdatengenerierung;

import java.time.DayOfWeek;
import java.time.LocalDate;

import de.wbstraining.lotto.util.ByteLongConverter;

/**
 *
 * @author gz1
 */

public class TestdatenGeneratorUtil {

	public static boolean isMittwoch(LocalDate datum) {
		return datum.getDayOfWeek() == DayOfWeek.WEDNESDAY;
	}

	public static boolean isSamstag(LocalDate datum) {
		return datum.getDayOfWeek() == DayOfWeek.SATURDAY;
	}

	public static int generateLosnummer6Aus49(int superzahl, int spiel77,
		int super6, int gkl) {
		int losnummer = 1234560;
		// wenn die gewinnklasse ungerade ist, muss die
		// letzte ziffer der losnummer mit der superzahl übereinstimmen
		if (gkl % 2 == 1) {
			losnummer += superzahl;
		}
		// wenn die gewinnklasse gerade ist, muss die letzte ziffer der losnummer
		// von der superzahl verschieden sein und von den endziffern in spiel77
		// und super6
		else {
			int lastDigitSpiel77 = spiel77 % 10;
			int lastDigitSuper6 = super6 % 10;
			int lastDigit;
			do {
				lastDigit = (int) (Math.random() * 10);
			} while (lastDigit == superzahl || lastDigit == lastDigitSpiel77
				|| lastDigit == lastDigitSuper6);
			losnummer += lastDigit;
		}
		return losnummer;
	}

	private static long generateTipp(long zahlenAlsBits, int anzahlMatches) {
		long tipp = 0;
		long wanderBit = 2L;
		for (; Long.bitCount(tipp) != 6; wanderBit <<= 1) {
			if (((zahlenAlsBits & wanderBit) != 0 && anzahlMatches > 0)) {
				tipp |= wanderBit;
				anzahlMatches--;
			} else {
				if (((zahlenAlsBits & wanderBit) == 0 && anzahlMatches == 0)) {
					tipp |= wanderBit;
				}
			}
		}
		return tipp;
	}

	public static long generateTippGkl(long zahlenAlsBits, int gkl) {
		long tipp = 0;
		switch (gkl) {
		case 0:
			tipp = generateTipp(zahlenAlsBits, 0);
			break;
		case 1:
		case 2:
			tipp = generateTipp(zahlenAlsBits, 6);
			break;
		case 3:
		case 4:
			tipp = generateTipp(zahlenAlsBits, 5);
			break;
		case 5:
		case 6:
			tipp = generateTipp(zahlenAlsBits, 4);
			break;
		case 7:
		case 8:
			tipp = generateTipp(zahlenAlsBits, 3);
			break;
		case 9:
			tipp = generateTipp(zahlenAlsBits, 2);
			break;
		default:
			throw new AssertionError("invalid gkl 6 aus 49");
		}
		return tipp;
	}

	public static byte[] generateTippsFuerEinenSchein(long zahlenAlsBits,
		int gkl6Aus49, int anzahlTipps) {
		long[] tipps = new long[anzahlTipps];
		tipps[0] = generateTippGkl(zahlenAlsBits, gkl6Aus49);
		for (int i = 1; i < anzahlTipps; i++) {
			tipps[i] = generateTippGkl(zahlenAlsBits, 0);
		}
		return ByteLongConverter.longToByte(tipps);
	}

	private static int replaceDigit(int spiel77OrSuper6, int pos,
		boolean isSpiel77) {
		int len = isSpiel77 ? 7 : 6;
		String result = String.format("%0" + len + "d", spiel77OrSuper6);
		StringBuilder sb = new StringBuilder(result);
		char c = result.charAt(pos);
		char replace = (c == '2' ? '1' : '2');
		sb.setCharAt(pos, replace);
		return Integer.parseInt(sb.toString());
	}

	public static int[] generateLosnummernSpiel77(int spiel77) {
		int[] losnummernSpiel77 = new int[8];
		losnummernSpiel77[0] = replaceDigit(spiel77, 6, true);
		losnummernSpiel77[1] = spiel77;
		for (int i = 2; i < losnummernSpiel77.length; i++) {
			losnummernSpiel77[i] = replaceDigit(spiel77, i - 2, true);
		}
		return losnummernSpiel77;
	}

	public static int[] generateLosnummernSuper6(int super6) {
		int[] losnummernSuper6 = new int[7];
		losnummernSuper6[0] = replaceDigit(super6, 5, false);
		losnummernSuper6[1] = super6;
		for (int i = 2; i < losnummernSuper6.length; i++) {
			losnummernSuper6[i] = replaceDigit(super6, i - 2, false);
		}
		return losnummernSuper6;
	}

}
