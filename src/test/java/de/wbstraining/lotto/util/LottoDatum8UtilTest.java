package de.wbstraining.lotto.util;

import static de.wbstraining.lotto.util.LottoDatum8Util.ersterZiehungstag;
import static de.wbstraining.lotto.util.LottoDatum8Util.naechsterZiehungstag;
import static de.wbstraining.lotto.util.LottoDatum8Util.ziehungsTage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

public class LottoDatum8UtilTest {

	final int ziehungszeitMi = 18; // Standardzeit
	final int ziehungszeitSa = 15;
	final LocalDate so = LocalDate.of(2020, 03, 1); // So 18Uhr
	final LocalTime abgSchluMi = LocalTime.of(ziehungszeitMi, 0);
	final LocalTime abgSchluSa = LocalTime.of(ziehungszeitSa, 0);
	final int laufzeit = 3;

	@Test
	public void naechsterZiehungstagRichtig() {

		List<LocalDateTime> abgaben = Arrays.asList(
			LocalDateTime.of(so, abgSchluMi), // So
			LocalDateTime.of(so.plusDays(3), abgSchluMi.minusHours(1)), // Mi 17Uhr
			LocalDateTime.of(so.plusDays(3), abgSchluMi.plusHours(1)), // Mi 18Uhr
			LocalDateTime.of(so.plusDays(4), abgSchluMi), // Do 18Uhr
			LocalDateTime.of(so.plusDays(5), abgSchluMi), // Fr 18Uhr
			LocalDateTime.of(so.plusDays(6), abgSchluMi.minusHours(1)), // Sa 17Uhr
			LocalDateTime.of(so.plusDays(6), abgSchluMi)); // Sa 18Uhr

		LocalDate sa200307 = LocalDate.of(2020, 03, 07);
		List<LocalDate> ziehungsDatums = Arrays.asList(
			// Mi (fuer So, Mi17Uhr)
			LocalDate.of(2020, 03, 04), LocalDate.of(2020, 03, 04),
			// Sa (fuer Mi18Uhr, Do, Fr, Sa)
			sa200307, sa200307, sa200307, sa200307,
			// Mi fuer Sa 18Uhr
			LocalDate.of(2020, 03, 11));

		for (int i = 0; i < ziehungsDatums.size(); i++) {
			assertEquals(ziehungsDatums.get(i), naechsterZiehungstag(abgaben.get(i)
				.toLocalDate(),
				abgaben.get(i)
					.toLocalTime()));
		}
	}

	@Test
	public void ersterZiehungstagRichtig() {

		List<LocalDateTime> abgaben = Arrays.asList(
			LocalDateTime.of(so, abgSchluMi), // So
			LocalDateTime.of(so.plusDays(3), abgSchluMi.minusHours(1)), // Mi 17Uhr
			LocalDateTime.of(so.plusDays(3), abgSchluMi), // Mi 18Uhr
			LocalDateTime.of(so.plusDays(6), abgSchluSa.minusHours(1)), // Sa 14Uhr
			LocalDateTime.of(so.plusDays(6), abgSchluSa)); // Sa 15Uhr

		List<LocalDate> ziehungsDatums = Arrays.asList(
			// Mi (fuer So, Mi)
			LocalDate.of(2020, 03, 04), LocalDate.of(2020, 03, 04),
			// Sa (fuer Mi18Uhr, Sa)
			LocalDate.of(2020, 03, 07), LocalDate.of(2020, 03, 07),
			// Mi fuer Sa 18Uhr
			LocalDate.of(2020, 03, 11));

		IntStream.iterate(0, i -> ++i)
			.limit(abgaben.size())
			.forEach(i -> {
				assertTrue(ziehungsDatums.get(i)
					.equals(ersterZiehungstag(abgaben.get(i)
						.toLocalDate(),
						abgaben.get(i)
							.toLocalTime(),
						true, true, ziehungszeitMi, ziehungszeitSa)));
			});
	}

	@Test // fuer Coverage
	public void ersterZiehungstagNurMittwochRichtig() {
		assertTrue(so.plusDays(3) // Mi 18Uhr
			.equals(ersterZiehungstag(so.plusDays(3), abgSchluMi.minusHours(2), true,
				false, ziehungszeitMi, ziehungszeitSa)));
	}

	@Test
	public void ersterZiehungstagAllExc() {
//	@formatter:off		
//	isMittwoch + isSamstag
		try {
			ersterZiehungstag(so, abgSchluMi, false, false, ziehungszeitMi,
				ziehungszeitSa).toString();
			fail("isMittwoch und isSamstag failed...");
		} catch (IllegalArgumentException e) {
			assertEquals("invalid isMittwoch and isSamstag...", e.getMessage());
		}
//	abgabeschlussMi
		try {
			ersterZiehungstag(so, abgSchluMi, true, true, -1, ziehungszeitSa);
			fail("abgabeschlussMi -1 failed...");
		} catch (IllegalArgumentException e) {}
		try {
			ersterZiehungstag(so, abgSchluMi, true, true, 25, ziehungszeitSa);
			fail("abgabeschlussMi 25 failed...");
		} catch (IllegalArgumentException e) {}
		
//	abgabeschlussSa
		try {
			ersterZiehungstag(so, abgSchluMi, true, true, ziehungszeitMi, -1);
			fail("abgabeschlussSa -1 failed...");
		} catch (IllegalArgumentException e) {}
		try {
			ersterZiehungstag(so, abgSchluMi, true, true, ziehungszeitMi, 25);
			fail("abgabeschlussSa 25 failed...");
		} catch (IllegalArgumentException e) {}
//	@formatter:on
	}

	@Test
	public void ziehungsTageRichtig() {
		List<LocalDate> ziehungenSaMi3Wo = Arrays.asList(
			// Wo1: Mi + Sa
			LocalDate.of(2020, 03, 04), LocalDate.of(2020, 03, 07),
			// Wo2: Mi + Sa
			LocalDate.of(2020, 03, 11), LocalDate.of(2020, 03, 14),
			// Wo3: Mi + Sa
			LocalDate.of(2020, 03, 18), LocalDate.of(2020, 03, 21));

		assertEquals(ziehungenSaMi3Wo, // ..... 12 Uhr
			ziehungsTage(so, abgSchluMi.minusHours(6), true, true, ziehungszeitMi,
				ziehungszeitSa, laufzeit));

//		
		List<LocalDate> ziehungenMi5Wo = Arrays.asList(//
			LocalDate.of(2020, 03, 04), // Wo1
			LocalDate.of(2020, 03, 11), // Wo2
			LocalDate.of(2020, 03, 18), // Wo3
			LocalDate.of(2020, 03, 25), // Wo4
			LocalDate.of(2020, 04, 01));// Wo5

		assertEquals(ziehungenMi5Wo, ziehungsTage(so, abgSchluMi.minusHours(6),
			true, false, ziehungszeitMi, ziehungszeitSa, 5));

//		
		List<LocalDate> ziehungenSa2Wo = Arrays.asList(//
			LocalDate.of(2020, 03, 07), // Wo1
			LocalDate.of(2020, 03, 14)); // Wo2

		assertEquals(ziehungenSa2Wo, ziehungsTage(so, abgSchluMi.minusHours(6),
			false, true, ziehungszeitMi, ziehungszeitSa, 2));
	}

	@Test
	public void ziehungsTageAllExc() {
//	@formatter:off
//		abgabeschlussMi
		try {
			ziehungsTage(so, abgSchluMi, true, true, -1, ziehungszeitSa, laufzeit);
			fail("adgabeschlussMi -1 failed...");
		} catch (IllegalArgumentException e) {}
		try {
			ziehungsTage(so, abgSchluMi, true, true, 25, ziehungszeitSa, laufzeit);
			fail("adgabeschlussMi 25 failed...");
		} catch (IllegalArgumentException e) {}
//		abgabeschlussSa
		try {
			ziehungsTage(so, abgSchluMi, true, true, ziehungszeitMi, -1, laufzeit);
			fail("adgabeschlussSa -1 failed...");
		} catch (IllegalArgumentException e) {}
		try {
			ziehungsTage(so, abgSchluMi, true, true, ziehungszeitMi, 25, laufzeit);
			fail("adgabeschlussSa 25 failed...");
		} catch (IllegalArgumentException e) {}
//		@formatter:on
	}

	@Test(expected = IllegalArgumentException.class)
	public void ziehungsTageLaufzeitIllArgExc() {
		ziehungsTage(so, abgSchluMi, true, true, ziehungszeitMi, ziehungszeitSa, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void ziehungsTageIsMiIsSaIllArgExc() {
		ziehungsTage(so, abgSchluMi, false, false, ziehungszeitMi, ziehungszeitSa,
			laufzeit);
	}
}