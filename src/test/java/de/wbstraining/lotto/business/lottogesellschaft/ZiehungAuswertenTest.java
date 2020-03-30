package de.wbstraining.lotto.business.lottogesellschaft;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.wbstraining.lotto.persistence.dao.SpielFacadeLocal;
import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Spiel;
import de.wbstraining.lotto.persistence.model.Ziehung;

@RunWith(Arquillian.class)
public class ZiehungAuswertenTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackages(true, "de.wbstraining.lotto.persistence", "de.wbstraining.lotto.populatedb",
						"de.wbstraining.lotto.util", "de.wbstraining.lotto.business.lottospieler",
						"de.wbstraining.lotto.cache", "de.wbstraining.lotto.mail", "de.wbstraining.lotto.dto",
						"de.wbstraining.lotto.business.lottogesellschaft")
				.addAsResource("META-INF/persistence.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@EJB
	private ZiehungAuswertenLocal ziehungAuswerten;

	@EJB
	private ZiehungFacadeLocal ziehungFacade;

	@EJB
	private SpielFacadeLocal spielFacade;

	@Test
	public void ziehungAuswerten() {
		List<Spiel> spiele = spielFacade.findAll();
		Map<String, Spiel> spielMap = new HashMap<>();
		long anzahlSuper6TotalActual;
		long anzahlSpiel77TotalActual;
		long anzahlSuper6TotalExpected = 21;
		long anzahlSpiel77TotalExpected = 28;
		spiele.stream().forEach(s -> spielMap.put(s.getName(), s));
		Ziehung ziehung = ziehungFacade.find(1L);
		ziehungAuswerten.ziehungAuswerten(ziehung);
		List<Lottoscheinziehung> lzList = ziehung.getLottoscheinziehungList();
		anzahlSuper6TotalActual = lzList.stream().filter(
				lz -> lz.getGewinnklasseidspiel77().getSpielid()
				.getName().equals("Super 6")).count();
		anzahlSpiel77TotalActual = lzList.stream().filter(
				lz -> lz.getGewinnklasseidsuper6().getSpielid()
				.getName().equals("Spiel77")).count();
		assertTrue(anzahlSpiel77TotalActual == anzahlSpiel77TotalExpected
				&& anzahlSuper6TotalActual == anzahlSuper6TotalExpected);
	}
}
