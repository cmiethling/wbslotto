package de.wbstraining.lotto.persistence.metamodel;

import java.math.BigInteger;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Gewinnklasse;
import de.wbstraining.lotto.persistence.model.Gewinnklasseziehungquote;
import de.wbstraining.lotto.persistence.model.Jackpot;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung6aus49;
import de.wbstraining.lotto.persistence.model.Spiel;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Gewinnklasse.class)
public class Gewinnklasse_ { 

    public static volatile ListAttribute<Gewinnklasse, Lottoscheinziehung> lottoscheinziehungList1;
    public static volatile ListAttribute<Gewinnklasse, Gewinnklasseziehungquote> gewinnklasseziehungquoteList;
    public static volatile SingularAttribute<Gewinnklasse, Long> gewinnklasseid;
    public static volatile SingularAttribute<Gewinnklasse, String> bezeichnunglatein;
    public static volatile SingularAttribute<Gewinnklasse, Date> created;
    public static volatile SingularAttribute<Gewinnklasse, BigInteger> betrag;
    public static volatile SingularAttribute<Gewinnklasse, Spiel> spielid;
    public static volatile SingularAttribute<Gewinnklasse, Integer> gewinnklassenr;
    public static volatile SingularAttribute<Gewinnklasse, String> beschreibung;
    public static volatile SingularAttribute<Gewinnklasse, Integer> version;
    public static volatile ListAttribute<Gewinnklasse, Jackpot> jackpotList;
    public static volatile ListAttribute<Gewinnklasse, Lottoscheinziehung6aus49> lottoscheinziehung6aus49List;
    public static volatile ListAttribute<Gewinnklasse, Lottoscheinziehung> lottoscheinziehungList;
    public static volatile SingularAttribute<Gewinnklasse, Date> lastmodified;
    public static volatile SingularAttribute<Gewinnklasse, Date> gueltigab;
    public static volatile SingularAttribute<Gewinnklasse, Date> gueltigbis;
    public static volatile SingularAttribute<Gewinnklasse, Boolean> isabsolut;

}