package de.wbstraining.lotto.business.lottospieler;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import de.wbstraining.lotto.dto.KostenDto;
import de.wbstraining.lotto.persistence.dao.GebuehrFacadeLocal;
import de.wbstraining.lotto.persistence.model.Gebuehr;

@RunWith(Arquillian.class)
public class LottoscheinEinreichenKostenErmittelnTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackages(true, "de.wbstraining.lotto.persistence",
						"de.wbstraining.lotto.populatedb", "de.wbstraining.lotto.util",
						"de.wbstraining.lotto.business.lottospieler",
						"de.wbstraining.lotto.cache", "de.wbstraining.lotto.mail",
						"de.wbstraining.lotto.dto",
						"de.wbstraining.lotto.business.lottogesellschaft")
				.addAsResource("META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@EJB
	private GebuehrFacadeLocal gebuehrFacade;

//	@EJB
//	private CleanDatabaseLocal cleanDatabase;

	@EJB
	private KostenErmittelnLocal lottoscheinEinreichenKostenErmitteln;

	@EJB
	private GebuehrenCacheLocal gebuehrenCache;

	@Ignore
	@Test
	public void testKostenErmitteln() {

		// einsatzLotto: 100
		//
		// Grundgebuehr: 60
		// Einsatz Spiel77: 8 * 250 = 2000
		// Einsatz Super6: 8 * 125 = 1000
		// Einsatz Lotto: 4 * 100 * 8 = 3200
		//
		// Total: 6260
		// ----------------------------------------------------------------------------------
		LocalDateTime now = LocalDateTime.now();
		LocalDate gueltigAb = now.minusYears(1)
				.toLocalDate();
		LocalDate gueltigBis = now.plusYears(1)
				.toLocalDate();

		Gebuehr gebuehr = new Gebuehr();

		// cleanDatabase.cleanDatabase("mydbtest");

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

		// LocalDate abgabeZeitpunkt = LocalDate.now().plusDays(1);

		boolean isMittwoch = true;
		boolean isSamstag = true;
		boolean isSpiel77 = true;
		boolean isSuper6 = true;
		int laufzeit = 4;
		int anzahlTipps = 4;

		int kostenExpected;
		int kostenActual;

		KostenDto dto = new KostenDto();
		dto.setAbgabeZeitpunkt(now);
		dto.setAnzahlTipps(anzahlTipps);
		dto.setLaufzeit(laufzeit);
		dto.setMittwoch(isMittwoch);
		dto.setSamstag(isSamstag);
		dto.setSpiel77(isSpiel77);
		dto.setSuper6(isSuper6);

		kostenExpected = 6260;
		kostenActual = lottoscheinEinreichenKostenErmitteln.kostenErmitteln(dto);

		// cleanDatabase.cleanDatabase("mydbtest");

		assertEquals(kostenExpected, kostenActual);

	}

}
