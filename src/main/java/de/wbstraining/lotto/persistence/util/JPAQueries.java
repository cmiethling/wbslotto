/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.persistence.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.wbstraining.lotto.persistence.metamodel.Kunde_;
import de.wbstraining.lotto.persistence.model.Gewinnklasse;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Ziehung;


/**
 *
 * @author gz1
 */


@Stateless
public class JPAQueries implements JPAQueriesLocal {

	@PersistenceContext(unitName = "corejsfPU")
	private EntityManager em;

	// dynamic query
	// untyped

	@SuppressWarnings("unchecked")
	@Override
	public List<Kunde> dynamic_untyped_query() {
		Query query = em.createQuery("SELECT k FROM Kunde k");
		List<Kunde> kunden = query.getResultList();
		return kunden;
	}

	// dynamic query
	// typed

	// @Interceptors(AroundInterceptorLogging.class)
	@Override
	public List<Kunde> dynamic_typed_query() {
		TypedQuery<Kunde> query = em.createQuery("SELECT k FROM Kunde k", Kunde.class);
		List<Kunde> kunden = query.getResultList();
		return kunden;
	}

	// named query
	@Override
	public List<Kunde> named_query() {
		TypedQuery<Kunde> query = em.createNamedQuery("Kunde.findAll", Kunde.class);
		List<Kunde> kunden = query.getResultList();
		return kunden;
	}

	// named query with parameters
	@Override
	public List<Kunde> named_query_with_parameters() {
		TypedQuery<Kunde> query = em.createNamedQuery("Kunde.findByKundeid", Kunde.class).setParameter("kundeid", 1L);
		List<Kunde> kunden = query.getResultList();
		return kunden;
	}

	// criteria api
	@Override
	public List<Kunde> criteria_api_1() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Kunde> criteriaQuery = builder.createQuery(Kunde.class);
		Root<Kunde> k = criteriaQuery.from(Kunde.class);
		criteriaQuery.select(k).where(builder.greaterThan(k.get(Kunde_.kundeid), 3L));
		List<Kunde> kunden = em.createQuery(criteriaQuery).getResultList();
		return kunden;
	}

	@Override
	public List<Kunde> criteria_api_2() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Kunde> criteriaQuery = builder.createQuery(Kunde.class);
		Root<Kunde> k = criteriaQuery.from(Kunde.class);
		criteriaQuery.where(builder.and(builder.greaterThan(k.get(Kunde_.kundeid), 3L),
				builder.lessThan(k.get(Kunde_.kundeid), 7L)));
		List<Kunde> kunden = em.createQuery(criteriaQuery).getResultList();
		return kunden;
	}

	@Override
	public List<Gewinnklasse> findAllGewinnklasse() {
		TypedQuery<Gewinnklasse> query = em.createNamedQuery("Gewinnklasse.findAll", Gewinnklasse.class);
		List<Gewinnklasse> gewinnklassen = query.getResultList();
		return gewinnklassen;
	}

	@Override
	public List<Gewinnklasse> findGewinnklassenForZiehung(Ziehung ziehung) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Gewinnklasse> criteriaQuery = builder.createQuery(Gewinnklasse.class);
		Root<Gewinnklasse> gwcls = criteriaQuery.from(Gewinnklasse.class);

		criteriaQuery = criteriaQuery.select(gwcls)
				.where(builder.and(builder.greaterThanOrEqualTo(gwcls.get("gueltigbis"), ziehung.getZiehungsdatum()),
						builder.lessThanOrEqualTo(gwcls.get("gueltigab"), ziehung.getZiehungsdatum())));
		List<Gewinnklasse> gewinnklassen = em.createQuery(criteriaQuery).getResultList();
//              System.out
//                      .println("/n---------- gewinnklassen.len: ____" + gewinnklassen.size());
		if (gewinnklassen.stream().count() != gewinnklassen.stream().map((gkl) -> {
			String str = "" + gkl.getSpiel().getSpielid();
			str += "_" + gkl.getGewinnklassenr();
			return str;
		}).distinct().count()) {

			Map<String, Gewinnklasse> map = new HashMap<String, Gewinnklasse>();

			for (Gewinnklasse gwnKl : gewinnklassen) {
				String strKey = "" + (Long) gwnKl.getSpiel().getSpielid() + "_" + gwnKl.getGewinnklassenr();
				if (map.containsKey(strKey)) {
					map.compute(strKey, (k, v) -> v.getGueltigab().compareTo(gwnKl.getGueltigab()) < 0 ? v = gwnKl : v);
				} else {
					map.put(strKey, gwnKl);
				}
			}
			gewinnklassen = new ArrayList<Gewinnklasse>(map.values());
		}
//              System.out
//                      .println("/n---------- gewinnklassen2.len: ____" + gewinnklassen.size());
		return gewinnklassen;
	}

}
