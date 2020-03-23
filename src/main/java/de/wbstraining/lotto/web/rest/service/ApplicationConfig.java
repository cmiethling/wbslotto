/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.web.rest.service;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author gz1
 */

// url-beginn: http://localhost:8080/wbslotto/lottoapi/
// corejpa: context-path
// lottoapi: application-path

// http://localhost:8080/wbslotto/lottoapi/ziehungen

@ApplicationPath("/lottoapi")
public class ApplicationConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new java.util.HashSet<>();
		addRestResourceClasses(resources);
		return resources;
	}

	private void addRestResourceClasses(Set<Class<?>> resources) {
		// für jede klasse, die als REST service eingesetzt wird, ein add()....
		resources.add(ZiehungFacadeREST.class);
		resources.add(KostenDetailedREST.class);
		resources.add(GewinnErmittelnREST.class);
		resources.add(LottoscheinEinreichenREST.class);

	}

}
