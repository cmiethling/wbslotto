package de.wbstraining.lotto.web.rest.service;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.Consumes;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/*
@RunWith(Arquillian.class)
public class ZiehungFacadeRESTTest {

	@Deployment // (testable = true)
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "ZiehungFacadeRESTTest.war")
				.addAsResource("META-INF/persistence.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	ZiehungFacadeREST ziehungFacadeREST;
	@ArquillianResource
	private URL base;

	private static WebTarget targetCount, targetFindAll;

	@Before
	public void setUpClass() throws MalformedURLException {
		Client client = ClientBuilder.newClient();
		targetCount = client.target(
				URI.create(new URL(base, "http://localhost:8080/wbslotto/lottoapi/ziehungen/count").toExternalForm()));
		targetFindAll = client.target(
				URI.create(new URL(base, "http://localhost:8080/wbslotto/lottoapi/ziehungen").toExternalForm()));
	}

	@Test
	@RunAsClient
	@Consumes(MediaType.APPLICATION_JSON)
	public void countREST_Test(@ArquillianResource URL base) {

		System.err.println("ehab : " + targetCount.request().get().readEntity(String.class));
		// das count zahl muss händlich eingegeben! in mein beispiel ist 41
		// umgeschrieben...
		String count = targetCount.request().get().readEntity(String.class);
		assertTrue(Integer.parseInt(count) > 0);

		// Assert.assertTrue(ziehungFacadeREST.findAll() != null);
		// .equals(String.valueOf("21")));
	}

	@Test
	@RunAsClient
	@Consumes(MediaType.APPLICATION_JSON)
	public void findAllREST_Test(@ArquillianResource URL base) {
		// System.err.println("ehab1 : " + targetFindAll.request().get().readEntity(String.class));
		Assert.assertTrue(targetFindAll.request().get().readEntity(String.class) != null);

	}
}

*/
