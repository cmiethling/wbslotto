package de.wbstraining.lotto.persistence.metamodel;

import java.math.BigInteger;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Adresse;
import de.wbstraining.lotto.persistence.model.Bankverbindung;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.persistence.model.Users;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Kunde.class)
public class Kunde_ { 

    public static volatile SingularAttribute<Kunde, Date> created;
    public static volatile SingularAttribute<Kunde, String> vorname;
    public static volatile SingularAttribute<Kunde, BigInteger> guthaben;
    public static volatile ListAttribute<Kunde, Adresse> adresseList;
    public static volatile SingularAttribute<Kunde, Integer> version;
    public static volatile SingularAttribute<Kunde, Users> users;
    public static volatile SingularAttribute<Kunde, Date> gesperrt;
    public static volatile ListAttribute<Kunde, Bankverbindung> bankverbindungList;
    public static volatile SingularAttribute<Kunde, Boolean> isannahmestelle;
    public static volatile SingularAttribute<Kunde, Date> lastmodified;
    public static volatile SingularAttribute<Kunde, String> name;
    public static volatile ListAttribute<Kunde, Lottoschein> lottoscheinList;
    public static volatile SingularAttribute<Kunde, Long> kundeid;
    public static volatile SingularAttribute<Kunde, String> email;
    public static volatile SingularAttribute<Kunde, BigInteger> dispo;

}