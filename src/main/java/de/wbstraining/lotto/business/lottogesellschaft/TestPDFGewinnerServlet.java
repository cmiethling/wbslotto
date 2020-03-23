package de.wbstraining.lotto.business.lottogesellschaft;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Ziehung;

/**
 * Servlet implementation class TestPDFGewinnerServlet
 */
@WebServlet("/TestPDFGewinnerServlet")
public class TestPDFGewinnerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	@EJB
	private ZiehungAuswertenLocal ziehungAuswertenLocal;
	@EJB
	private ZiehungFacadeLocal ziehungFacade;

	public TestPDFGewinnerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

    	response.setContentType("text/html);charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet sendEmail_Gewinner</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet sendEmail_Gewinner at" + request.getContextPath() + "</h1>");
			out.println("sendEmail_Gewinner...<br>");

			Ziehung ziehung = ziehungFacade.find(20L);     
	    	ziehungAuswertenLocal.sendEmail_Gewinner(ziehung);

			out.println("done...");
			out.println("</body>");
			out.println("</html>");

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
