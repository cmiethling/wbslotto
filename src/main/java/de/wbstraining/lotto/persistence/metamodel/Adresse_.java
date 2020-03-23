package de.wbstraining.lotto.persistence.metamodel;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Adresse;
import de.wbstraining.lotto.persistence.model.Kunde;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Adresse.class)
public class Adresse_ { 

    public static volatile SingularAttribute<Adresse, String> ort;
    public static volatile SingularAttribute<Adresse, String> strasse;
    public static volatile SingularAttribute<Adresse, String> adresszusatz;
    public static volatile SingularAttribute<Adresse, Date> created;
    public static volatile SingularAttribute<Adresse, Date> lastmodified;
    public static volatile SingularAttribute<Adresse, Long> adresseid;
    public static volatile SingularAttribute<Adresse, String> hausnummer;
    public static volatile SingularAttribute<Adresse, String> land;
    public static volatile SingularAttribute<Adresse, Integer> version;
    public static volatile SingularAttribute<Adresse, Integer> adressenr;
    public static volatile SingularAttribute<Adresse, Kunde> kundeid;
    public static volatile SingularAttribute<Adresse, String> plz;

}