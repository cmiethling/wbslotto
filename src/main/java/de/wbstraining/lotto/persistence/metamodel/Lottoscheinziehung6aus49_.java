package de.wbstraining.lotto.persistence.metamodel;

import java.math.BigInteger;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Gewinnklasse;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung6aus49;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Lottoscheinziehung6aus49.class)
public class Lottoscheinziehung6aus49_ { 

    public static volatile SingularAttribute<Lottoscheinziehung6aus49, Gewinnklasse> gewinnklasseid;
    public static volatile SingularAttribute<Lottoscheinziehung6aus49, Date> created;
    public static volatile SingularAttribute<Lottoscheinziehung6aus49, Date> lastmodified;
    public static volatile SingularAttribute<Lottoscheinziehung6aus49, Integer> tippnr;
    public static volatile SingularAttribute<Lottoscheinziehung6aus49, BigInteger> gewinn;
    public static volatile SingularAttribute<Lottoscheinziehung6aus49, Lottoscheinziehung> lottoscheinziehungid;
    public static volatile SingularAttribute<Lottoscheinziehung6aus49, Integer> version;
    public static volatile SingularAttribute<Lottoscheinziehung6aus49, Long> lottoscheinziehung6aus49id;

}