package de.wbstraining.lotto.persistence.metamodel;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Gewinnklasse;
import de.wbstraining.lotto.persistence.model.Gewinnklasseziehungquote;
import de.wbstraining.lotto.persistence.model.Ziehung;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Gewinnklasseziehungquote.class)
public class Gewinnklasseziehungquote_ { 

    public static volatile SingularAttribute<Gewinnklasseziehungquote, Integer> anzahlgewinner;
    public static volatile SingularAttribute<Gewinnklasseziehungquote, Long> quote;
    public static volatile SingularAttribute<Gewinnklasseziehungquote, Gewinnklasse> gewinnklasseid;
    public static volatile SingularAttribute<Gewinnklasseziehungquote, Date> created;
    public static volatile SingularAttribute<Gewinnklasseziehungquote, Date> lastmodified;
    public static volatile SingularAttribute<Gewinnklasseziehungquote, Long> gewinnklasseziehungquoteid;
    public static volatile SingularAttribute<Gewinnklasseziehungquote, Integer> version;
    public static volatile SingularAttribute<Gewinnklasseziehungquote, Ziehung> ziehungid;

}