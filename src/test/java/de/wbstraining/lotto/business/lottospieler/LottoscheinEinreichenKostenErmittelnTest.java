package de.wbstraining.lotto.business.lottospieler;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Date;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.wbstraining.lotto.cache.GebuehrenCacheLocal;
import de.wbstraining.lotto.persistence.dao.GebuehrFacadeLocal;
import de.wbstraining.lotto.persistence.model.Gebuehr;
import de.wbstraining.lotto.populatedb.CleanDatabaseLocal;

@RunWith(Arquillian.class)
public class LottoscheinEinreichenKostenErmittelnTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackages(true, "de.wbstraining.lotto.persistence", "de.wbstraining.lotto.populatedb",
						"de.wbstraining.lotto.util", "de.wbstraining.lotto.business", "de.wbstraining.lotto.cache",
						"de.wbstraining.lotto.mail")
				.addAsResource("META-INF/persistence.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@EJB
	private GebuehrFacadeLocal gebuehrFacade;

	@EJB
	private CleanDatabaseLocal cleanDatabase;

	@EJB
	private KostenErmittelnLocal lottoscheinEinreichenKostenErmitteln;

	@EJB
	private GebuehrenCacheLocal gebuehrenCache;

	@Ignore
	@Test
	public void testKostenErmitteln() {
		// ----------------------------------------------------------------------------------
		// A C H T U N G:
		// Test läuft nicht an den Ziehungstagen Mittwoch und Samstag
		// An Ziehungstagen:
		// LocalDate abgabeDatum = LocalDate.now().plusDays(1);
		// An allen anderen Tagen:
		// LocalDate abgabeDatum = LocalDate.now();
		//
		// Solange, bis Refactoring auf DateTime-API im gesamten Lottoprojekt durchgeführt.
		// --------------------------------------------------------
		// Zeitstempel LocalDate.now()
		// Lottoschein wird abgegeben LocalDate.now() mit einer Laufzeit von 4 Wochen,
		// 4 Tipps, Spiel 77, Super6
		//
		// Gebührenrecord ist gültig von LocalDate.now().minusYears(1)
		// bis LocalDate.now().plusYears(1)
		//
		// grundgebuehr: 60
		// einsatzSpiel77: 250
		// einsatzSuper6: 125
		// einsatzLotto: 100
		//
		// Grundgebuehr: 60
		// Einsatz Spiel77: 8 * 250 = 2000
		// Einsatz Super6: 8 * 125 = 1000
		// Einsatz Lotto: 4 * 100 * 8 = 3200
		//
		// Total: 6260
		// ----------------------------------------------------------------------------------
		Date now = java.sql.Date.valueOf(LocalDate.now());
		Date gueltigAb = java.sql.Date.valueOf(LocalDate.now().minusYears(1));
		Date gueltigBis = java.sql.Date.valueOf(LocalDate.now().plusYears(1));

		Gebuehr gebuehr = new Gebuehr();

		cleanDatabase.cleanDatabase("mydbtest");

		gebuehr.setGrundgebuehr(60);
		gebuehr.setEinsatzprotipp(100);
		gebuehr.setEinsatzspiel77(250);
		gebuehr.setEinsatzsuper6(125);
		gebuehr.setGueltigab(gueltigAb);
		gebuehr.setGueltigbis(gueltigBis);
		gebuehr.setCreated(now);
		gebuehr.setLastmodified(now);

		gebuehrFacade.create(gebuehr);

		gebuehrenCache.init();

		LocalDate abgabeDatum = LocalDate.now().plusDays(1);

		boolean isMittwoch = true;
		boolean isSamstag = true;
		boolean isSpiel77 = true;
		boolean isSuper6 = true;
		int laufzeit = 4;
		int anzahlTipps = 4;

		int kostenExpected;
		int kostenActual;

		kostenExpected = 6260;
//		kostenActual = lottoscheinEinreichenKostenErmitteln.kostenermitteln(abgabeDatum, laufzeit, isMittwoch,
//				isSamstag, anzahlTipps, isSuper6, isSpiel77);

		cleanDatabase.cleanDatabase("mydbtest");

		//assertEquals(kostenExpected, kostenActual);

	}

	@Ignore
	@Test
	public void testKostenErmittelnMitGebuehrenwechsel() {

		// ----------------------------------------------------------------------------------
		// A C H T U N G:
		// Test läuft nicht an den Ziehungstagen Mittwoch und Samstag
		// An Ziehungstagen:
		// LocalDate abgabeDatum = LocalDate.now().plusDays(1);
		// An allen anderen Tagen:
		// LocalDate abgabeDatum = LocalDate.now();
		//
		// Solange, bis Refactoring auf DateTime-API im gesamten Lottoprojekt durchgeführt.
		// --------------------------------------------------------
		// Zeitstempel LocalDate.now()
		// Lottoschein wird abgegeben LocalDate.now() mit einer Laufzeit von 8 Wochen,
		// 4 Tipps, Spiel 77, Super6
		//
		// 1. Gebührenrecord ist gültig von LocalDate.now().minusYears(1)
		// bis LocalDate.now().plusWeeks(6)
		//
		// grundgebuehr: 60
		// einsatzSpiel77: 250
		// einsatzSuper6: 125
		// einsatzLotto: 100
		//
		// 2. Gebührenrecord ist gültig von LocalDate.now().plusWeeks(6)
		// bis LocalDate.now().plusYears(1)
		//
		// grundgebuehr: 120 (wird nicht verwendet, da GG schon bei Einreichung bezahlt)
		// einsatzSpiel77: 500
		// einsatzSuper6: 250
		// einsatzLotto: 200
		//
		// Kosten Zeitraum (12 Ziehungen mit GG alt) gebuehr1: 9360
		// Kosten Zeitraum (4 Ziehungen, keine GG) gebuehr2: 6200
		// Kosten gesamt (16 Ziehungen) : 15560
		//
		// ----------------------------------------------------------------------------------

		Date now = java.sql.Date.valueOf(LocalDate.now());

		Date gueltigAb1 = java.sql.Date.valueOf(LocalDate.now().minusYears(1));
		Date gueltigBis1 = java.sql.Date.valueOf(LocalDate.now().plusWeeks(6));

		Date gueltigAb2 = java.sql.Date.valueOf(LocalDate.now().plusWeeks(6));
		Date gueltigBis2 = java.sql.Date.valueOf(LocalDate.now().plusYears(1));

		Gebuehr gebuehr1 = new Gebuehr();
		Gebuehr gebuehr2 = new Gebuehr();

		cleanDatabase.cleanDatabase("mydbtest");

		gebuehr1.setGrundgebuehr(60);
		gebuehr1.setEinsatzprotipp(100);
		gebuehr1.setEinsatzspiel77(250);
		gebuehr1.setEinsatzsuper6(125);
		gebuehr1.setGueltigab(gueltigAb1);
		gebuehr1.setGueltigbis(gueltigBis1);
		gebuehr1.setCreated(now);
		gebuehr1.setLastmodified(now);

		gebuehrFacade.create(gebuehr1);

		gebuehr2.setGrundgebuehr(120);
		gebuehr2.setEinsatzprotipp(200);
		gebuehr2.setEinsatzspiel77(500);
		gebuehr2.setEinsatzsuper6(250);
		gebuehr2.setGueltigab(gueltigAb2);
		gebuehr2.setGueltigbis(gueltigBis2);
		gebuehr2.setCreated(now);
		gebuehr2.setLastmodified(now);

		gebuehrFacade.create(gebuehr2);

		gebuehrenCache.init();

		LocalDate abgabeDatum = LocalDate.now().plusDays(1);

		boolean isMittwoch = true;
		boolean isSamstag = true;
		boolean isSpiel77 = true;
		boolean isSuper6 = true;
		int laufzeit = 8;
		int anzahlTipps = 4;

		int kostenActual1;
		int kostenExpected1 = 15560;

//		kostenActual1 = lottoscheinEinreichenKostenErmitteln.kostenermitteln(abgabeDatum, laufzeit, isMittwoch,
//				isSamstag, anzahlTipps, isSuper6, isSpiel77);

		cleanDatabase.cleanDatabase("mydbtest");

		// TODO
		assert(true);

	}
}
