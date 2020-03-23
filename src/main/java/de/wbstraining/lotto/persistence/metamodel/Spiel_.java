package de.wbstraining.lotto.persistence.metamodel;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Gewinnklasse;
import de.wbstraining.lotto.persistence.model.Spiel;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Spiel.class)
public class Spiel_ { 

    public static volatile ListAttribute<Spiel, Gewinnklasse> gewinnklasseList;
    public static volatile SingularAttribute<Spiel, Date> created;
    public static volatile SingularAttribute<Spiel, Date> lastmodified;
    public static volatile SingularAttribute<Spiel, String> name;
    public static volatile SingularAttribute<Spiel, Long> spielid;
    public static volatile SingularAttribute<Spiel, String> beschreibung;
    public static volatile SingularAttribute<Spiel, Integer> version;
    public static volatile SingularAttribute<Spiel, String> pfadanleitung;

}