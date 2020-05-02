package de.wbstraining.lotto.web.rest.service;

import java.time.LocalDate;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import de.wbstraining.lotto.business.lottospieler.GewinnErmittelnLocal;
import de.wbstraining.lotto.dto.GewinnAbfrageDto;
import de.wbstraining.lotto.dto.GewinnDetailedDto;

@Stateless
@Path("/gewinne")
public class GewinnErmittelnREST {

	@EJB
	private GewinnErmittelnLocal gewinnErmitteln;

	/*
	 * @GET
	 * 
	 * @Produces({ "application/xml", "application/json" })
	 * public GewinnDetailedDto gewinn() {
	 * GewinnDetailedDto dto = new GewinnDetailedDto();
	 * dto.setBelegNr(11112222333L);
	 * dto.setZiehungsDatum(new Date());
	 * dto.setGklSpiel77(1);
	 * dto.setQuoteSpiel77(77000);
	 * dto.setGklSuper6(2);
	 * dto.setQuoteSuper6(6666);
	 * dto.addPair6Aus49(1, 3, 333);
	 * dto.addPair6Aus49(6, 1, 100000);
	 * return dto;
	 * }
	 */
	@GET
	@Produces({ "application/xml", "application/json" })
	@Consumes({ "application/xml", "application/json" })
	public GewinnDetailedDto gewinn(GewinnAbfrageDto abfrage) {
		LocalDate date = abfrage.getZiehungsDatum();
		GewinnDetailedDto dto = gewinnErmitteln.gewinn(abfrage.getBelegNr(), date);
		return dto;
	}
}