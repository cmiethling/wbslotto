package de.wbstraining.lotto.business.lottogesellschaft;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Ziehung;

@Local
public interface ZiehungAuswertenLocal {

        void ziehungAuswerten(Ziehung zie);
}
