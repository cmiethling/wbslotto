package de.wbstraining.lotto.persistence.metamodel;

import java.math.BigInteger;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Lottoschein;
import de.wbstraining.lotto.persistence.model.Lottoscheinziehung;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Lottoschein.class)
public class Lottoschein_ { 

    public static volatile SingularAttribute<Lottoschein, Integer> losnummer;
    public static volatile SingularAttribute<Lottoschein, Integer> laufzeit;
    public static volatile SingularAttribute<Lottoschein, Integer> kosten;
    public static volatile SingularAttribute<Lottoschein, Date> created;
    public static volatile SingularAttribute<Lottoschein, Boolean> isabgeschlossen;
    public static volatile SingularAttribute<Lottoschein, BigInteger> belegnummer;
    public static volatile SingularAttribute<Lottoschein, byte[]> tipps;
    public static volatile SingularAttribute<Lottoschein, Integer> version;
    public static volatile SingularAttribute<Lottoschein, Boolean> issuper6;
    public static volatile SingularAttribute<Lottoschein, Boolean> isspiel77;
    public static volatile SingularAttribute<Lottoschein, Long> lottoscheinid;
    public static volatile ListAttribute<Lottoschein, Lottoscheinziehung> lottoscheinziehungList;
    public static volatile SingularAttribute<Lottoschein, Date> lastmodified;
    public static volatile SingularAttribute<Lottoschein, Boolean> ismittwoch;
    public static volatile SingularAttribute<Lottoschein, Date> abgabedatum;
    public static volatile SingularAttribute<Lottoschein, Boolean> issamstag;
    public static volatile SingularAttribute<Lottoschein, Kunde> kundeid;
    public static volatile SingularAttribute<Lottoschein, Integer> status;

}