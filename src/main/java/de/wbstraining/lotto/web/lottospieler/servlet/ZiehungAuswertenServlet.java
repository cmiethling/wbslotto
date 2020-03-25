package de.wbstraining.lotto.web.lottospieler.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.wbstraining.lotto.business.lottogesellschaft.ZiehungAuswertenLocal;
import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Ziehung;

@WebServlet(name = "ZiehungAuswertenServlet", urlPatterns = { "/ZiehungAuswertenServlet" })
public class ZiehungAuswertenServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger log = Logger.getLogger("wbs.servlet.ZiehungAuswerten");

//	@EJB
//	private PopulateDatabaseLocal populateDatabase;
//
	@EJB(beanName = "ZiehungAuswerten")
	private ZiehungAuswertenLocal ziehungAuswerten;
//
//	@EJB
//	private CleanDatabaseLocal cleanDatabase;

	@EJB
	private ZiehungFacadeLocal ziehungFacade;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet ZiehungAuswertenServlet</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet ZiehungAuswertenServlet at " + request.getContextPath() + "</h1>");
			out.println("Ziehung Auswerten...<br>");
			long t1; // Zeit1
			long t2;// Zeit2

//			log.log(Level.INFO, "cleaning Database...");
//			cleanDatabase.cleanDatabase("mydbtest");
//
//			log.log(Level.INFO, "populating Database...");
//			t1 = System.currentTimeMillis();
//			populateDatabase.populateDatabase();
//			t2 = System.currentTimeMillis();
//			log.log(Level.INFO, "populateDB benoetigte Zeit: " + (t2 - t1) + "ms");
//			out.println("populateDB benoetigte Zeit: " + (t2 - t1) + "ms<br>");

			log.log(Level.INFO, "ziehungAuswerten...");
			Ziehung zie = ziehungFacade.findAll().get(1);
			t1 = System.currentTimeMillis();
			ziehungAuswerten.ziehungAuswerten(zie);
			t2 = System.currentTimeMillis();
			out.println("ZiehungAuswerten benoetigte Zeit: " + (t2 - t1) + "ms<br>");
			log.log(Level.INFO, "ZiehungAuswerten benoetigte Zeit: " + (t2 - t1) + "ms");

			out.println("done...");
			out.println("</body>");
			out.println("</html>");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}

}