package de.wbstraining.lotto.persistence.metamodel;

import java.math.BigInteger;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Gewinnklasseziehungquote;
import de.wbstraining.lotto.persistence.model.Jackpot;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Ziehung;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Ziehung.class)
public class Ziehung_ { 

    public static volatile ListAttribute<Ziehung, Gewinnklasseziehungquote> gewinnklasseziehungquoteList;
    public static volatile SingularAttribute<Ziehung, BigInteger> einsatzspiel77;
    public static volatile SingularAttribute<Ziehung, BigInteger> einsatzlotto;
    public static volatile SingularAttribute<Ziehung, Date> created;
    public static volatile SingularAttribute<Ziehung, BigInteger> zahlenalsbits;
    public static volatile SingularAttribute<Ziehung, Date> ziehungsdatum;
    public static volatile SingularAttribute<Ziehung, Integer> version;
    public static volatile SingularAttribute<Ziehung, Integer> spiel77;
    public static volatile ListAttribute<Ziehung, Jackpot> jackpotList;
    public static volatile ListAttribute<Ziehung, Lottoscheinziehung> lottoscheinziehungList;
    public static volatile SingularAttribute<Ziehung, Date> lastmodified;
    public static volatile SingularAttribute<Ziehung, Integer> super6;
    public static volatile SingularAttribute<Ziehung, BigInteger> einsatzsuper6;
    public static volatile SingularAttribute<Ziehung, Integer> superzahl;
    public static volatile SingularAttribute<Ziehung, Long> ziehungid;
    public static volatile SingularAttribute<Ziehung, Integer> status;

}