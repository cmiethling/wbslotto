package de.wbstraining.lotto.persistence.metamodel;

import java.math.BigInteger;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Gewinnklasse;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung6aus49;
import de.wbstraining.lotto.persistence.model.Ziehung;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Lottoscheinziehung.class)
public class Lottoscheinziehung_ { 

    public static volatile SingularAttribute<Lottoscheinziehung, BigInteger> gewinnspiel77;
    public static volatile SingularAttribute<Lottoscheinziehung, Date> created;
    public static volatile SingularAttribute<Lottoscheinziehung, Boolean> isabgeschlossen;
    public static volatile SingularAttribute<Lottoscheinziehung, Integer> ziehungnr;
    public static volatile SingularAttribute<Lottoscheinziehung, Integer> version;
    public static volatile SingularAttribute<Lottoscheinziehung, BigInteger> gewinnsuper6;
    public static volatile SingularAttribute<Lottoscheinziehung, Gewinnklasse> gewinnklasseidspiel77;
    public static volatile SingularAttribute<Lottoscheinziehung, Lottoschein> lottoscheinid;
    public static volatile ListAttribute<Lottoscheinziehung, Lottoscheinziehung6aus49> lottoscheinziehung6aus49List;
    public static volatile SingularAttribute<Lottoscheinziehung, Date> lastmodified;
    public static volatile SingularAttribute<Lottoscheinziehung, Long> lottoscheinziehungid;
    public static volatile SingularAttribute<Lottoscheinziehung, Gewinnklasse> gewinnklasseidsuper6;
    public static volatile SingularAttribute<Lottoscheinziehung, Boolean> isletzteziehung;
    public static volatile SingularAttribute<Lottoscheinziehung, Ziehung> ziehungid;

}