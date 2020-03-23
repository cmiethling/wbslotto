package de.wbstraining.lotto.persistence.metamodel;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Gewinnklasse;
import de.wbstraining.lotto.persistence.model.Jackpot;
import de.wbstraining.lotto.persistence.model.Ziehung;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Jackpot.class)
public class Jackpot_ { 

    public static volatile SingularAttribute<Jackpot, Long> betragkumuliert;
    public static volatile SingularAttribute<Jackpot, Integer> anzahlziehungen;
    public static volatile SingularAttribute<Jackpot, Long> jackpotid;
    public static volatile SingularAttribute<Jackpot, Gewinnklasse> gewinnklasseid;
    public static volatile SingularAttribute<Jackpot, Date> created;
    public static volatile SingularAttribute<Jackpot, Date> lastmodified;
    public static volatile SingularAttribute<Jackpot, Long> betrag;
    public static volatile SingularAttribute<Jackpot, Integer> version;
    public static volatile SingularAttribute<Jackpot, Ziehung> ziehungid;

}