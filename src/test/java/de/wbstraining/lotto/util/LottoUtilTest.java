package de.wbstraining.lotto.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class LottoUtilTest {

	@Test
	public void testRandomTipp() {
		// wie sieht ein aussagekräftiger test der methode randomTipp() aus
		// es muss genau 6 einserbits geben
		// die niedrigste erlaubte bitposition ist 1 (nicht 0 ...!)
		// die höchste erlaubte bitposition ist 49
		boolean ok = false;
		long tippAsBits = LottoUtil.randomTipp();
		ok = tippAsBits > 0 && Long.bitCount(tippAsBits) == 6 && Long.lowestOneBit(tippAsBits) > 1L
				&& Long.highestOneBit(tippAsBits) < (1L << 50);
		assertTrue(ok);
	}
	
	@Test
	public void testCreateTipp() {
				
		long tippGenerated;
		long myTipp = 0;
		int matchCounter;
		boolean ok;
		
		try {
			LottoUtil.createTipp(1,2,3,4,5,6,7);
			fail("check of number of arguments failed");
		}
		catch(IllegalArgumentException e) {
		}
		try {
			LottoUtil.createTipp(1,2,50,4);
			fail("check of range failed");
		}
		catch(IllegalArgumentException e) {
		}
		try {
			LottoUtil.createTipp(1,2,5,2);
			fail("check of duplicates failed");
		}
		catch(IllegalArgumentException e) {
		}
		
		myTipp |= (1L << 1);
		myTipp |= (1L << 2);
		myTipp |= (1L << 3);
		myTipp |= (1L << 49);
		myTipp |= (1L << 48);
		myTipp |= (1L << 47);
		
		tippGenerated = LottoUtil.createTipp(1,2,3);
		
		matchCounter = Long.bitCount(myTipp & tippGenerated);
		
		ok = tippGenerated > 0
				&& Long.bitCount(tippGenerated) == 6
				&& Long.lowestOneBit(tippGenerated) > 1L
				&& Long.highestOneBit(tippGenerated) < (1L << 50)
				&& matchCounter >= 3;
		assertTrue(ok);
		
	}

	
	@Test
	public void testGkl6Aus49() {
		boolean ok = false;
		long ziehungsZahlen = LottoUtil.createTipp(1,2,3,4,5,6);
		// der index entspricht der trefferzahl, der wert der gewinnklasse
		int[] expectedNoMatchingSuperzahl = {
			0,0,0,8,6,4,2	
		};
		int[] expectedMatchingSuperzahl = {
				0,0,9,7,5,3,1	
			};
		long[] tipps = {
			LottoUtil.createTipp(49,48,47,46,45,44),
			LottoUtil.createTipp(1,48,47,46,45,44),
			LottoUtil.createTipp(1,2,47,46,45,44),
			LottoUtil.createTipp(1,2,3,46,45,44),
			LottoUtil.createTipp(1,2,3,4,45,44),
			LottoUtil.createTipp(1,2,3,4,5,44),
			LottoUtil.createTipp(1,2,3,4,5,6)
		};
		int[] actualNoMatchingSuperzahl= new int[7];
		int[] actualMatchingSuperzahl= new int[7];
		for(int i = 0;i < 7;i++ ) {
			actualNoMatchingSuperzahl[i] = LottoUtil.gkl6Aus49(ziehungsZahlen, tipps[i], false);
			actualMatchingSuperzahl[i] = LottoUtil.gkl6Aus49(ziehungsZahlen, tipps[i], true);
		}
		ok = Arrays.equals(expectedNoMatchingSuperzahl, actualNoMatchingSuperzahl)
				&& Arrays.equals(expectedMatchingSuperzahl, actualMatchingSuperzahl);
		assertTrue(ok);
	}

	@Test
	public void testGklSpiel77() {
		int spiel77 = 1234567;
		int[] losnummern = {
			9999999,
			1234567,
			9234567,
			9934567,
			9994567,
			9999567,
			9999967,
			9999997
		};
		int[] expected = {
				0,1,2,3,4,5,6,7
		};
		int[] actual = new int[8];
		for(int i = 0;i < 8;i++) {
			actual[i] = LottoUtil.gklSpiel77(spiel77, losnummern[i]);
		}
		assertTrue(Arrays.equals(expected, actual));
	}

	@Test
	public void testGklSuper6() {
		int spiel77 = 123456;
		int[] losnummern = {
			999999,
			123456,
			923456,
			993456,
			999456,
			999956,
			999996
		};
		int[] expected = {
				0,1,2,3,4,5,6
		};
		int[] actual = new int[7];
		for(int i = 0;i < 7;i++) {
			actual[i] = LottoUtil.gklSuper6(spiel77, losnummern[i]);
		}
		assertTrue(Arrays.equals(expected, actual));
	}

}

