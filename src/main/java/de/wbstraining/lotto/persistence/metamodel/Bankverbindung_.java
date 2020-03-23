package de.wbstraining.lotto.persistence.metamodel;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Bankverbindung;
import de.wbstraining.lotto.persistence.model.Kunde;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Bankverbindung.class)
public class Bankverbindung_ { 

    public static volatile SingularAttribute<Bankverbindung, Integer> bankverbindungnr;
    public static volatile SingularAttribute<Bankverbindung, Date> created;
    public static volatile SingularAttribute<Bankverbindung, Date> lastmodified;
    public static volatile SingularAttribute<Bankverbindung, String> iban;
    public static volatile SingularAttribute<Bankverbindung, String> name;
    public static volatile SingularAttribute<Bankverbindung, Long> bankverbindungid;
    public static volatile SingularAttribute<Bankverbindung, String> kontoinhaber;
    public static volatile SingularAttribute<Bankverbindung, String> bic;
    public static volatile SingularAttribute<Bankverbindung, Integer> version;
    public static volatile SingularAttribute<Bankverbindung, Kunde> kundeid;

}