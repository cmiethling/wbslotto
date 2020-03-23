/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.web.rest.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import de.wbstraining.lotto.dto.ZiehungDto;
import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;

/**
 *
 * @author gz1
 */

// url-beginn: http://localhost:8080/corejpa/lottoapi/ziehungen
// corejpa: context-path
// lottoapi: application-path
// ziehungen: path

@Stateless
@Path("/ziehungen")
public class ZiehungFacadeREST {

	// ggf cache verwenden..
	@EJB
	private ZiehungFacadeLocal ziehungFacade;

	@GET
	@Produces({ "application/xml", "application/json" })
	public List<ZiehungDto> findAll() {
		List<ZiehungDto> ziehungen = new ArrayList<>();
		ziehungFacade.findAll().stream().forEach(z -> ziehungen.add(new ZiehungDto(z)));
		return ziehungen;
	}

	// @Interceptors (AroundInterceptorCountRecords.class)
	@GET
	@Path("count")
	@Produces("text/plain")
	public String countREST() {
		// return String.valueOf(findAll().size());
		return String.valueOf(ziehungFacade.count());
	}
}
