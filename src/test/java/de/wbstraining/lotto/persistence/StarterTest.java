package de.wbstraining.lotto.persistence;

import static org.junit.Assert.assertTrue;

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

import de.wbstraining.lotto.persistence.dao.KundeFacadeLocal;
import de.wbstraining.lotto.populatedb.CleanDatabaseLocal;
import de.wbstraining.lotto.populatedb.PopulateDatabaseLocal;


@RunWith(Arquillian.class)
public class StarterTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackages(true, "de.wbstraining.lotto.persistence", "de.wbstraining.lotto.populatedb",
						"de.wbstraining.lotto.util", "de.wbstraining.lotto.business.lottospieler", "de.wbstraining.lotto.cache",
						"de.wbstraining.lotto.mail","de.wbstraining.lotto.dto","de.wbstraining.lotto.business.lottogesellschaft" )
				.addAsResource("META-INF/persistence.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@EJB
	private KundeFacadeLocal kundeFacade;

	@EJB
	private CleanDatabaseLocal cleanDatabase;

	@EJB
	private PopulateDatabaseLocal populateDatabase;

	@Ignore
	@Test
	public void testCleanAndPopulateDatabase() {
		
		int expectedCountAfterDelete = 0;
		int countAfterDelete;
		int expectedCountAfterPopulate = 10;
		int countAfterPopulate;

		cleanDatabase.cleanDatabase("mydbtest");
		countAfterDelete = kundeFacade.count();
		
		populateDatabase.populateDatabase();
		countAfterPopulate = kundeFacade.count();

		assertTrue(expectedCountAfterDelete == countAfterDelete && expectedCountAfterPopulate == countAfterPopulate);
		
	}

}



