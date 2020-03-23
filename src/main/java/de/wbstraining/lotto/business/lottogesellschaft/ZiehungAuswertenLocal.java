package de.wbstraining.lotto.business.lottogesellschaft;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Ziehung;

@Local
public interface ZiehungAuswertenLocal {

	public void ziehungAuswerten(Ziehung ziehung);

	public void createJackpots(Ziehung ziehung);
	
	public void lottoScheineVerarbeiten(Ziehung ziehung);
	public void ReportGeneration(Ziehung ziehung);
	public void sendEmail_Gewinner(Ziehung ziehung);
	
	
}
