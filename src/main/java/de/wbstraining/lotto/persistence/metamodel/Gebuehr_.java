package de.wbstraining.lotto.persistence.metamodel;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Gebuehr;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Gebuehr.class)
public class Gebuehr_ { 

    public static volatile SingularAttribute<Gebuehr, Long> gebuehrid;
    public static volatile SingularAttribute<Gebuehr, Integer> einsatzspiel77;
    public static volatile SingularAttribute<Gebuehr, Date> created;
    public static volatile SingularAttribute<Gebuehr, Date> lastmodified;
    public static volatile SingularAttribute<Gebuehr, Integer> grundgebuehr;
    public static volatile SingularAttribute<Gebuehr, Date> gueltigab;
    public static volatile SingularAttribute<Gebuehr, Date> gueltigbis;
    public static volatile SingularAttribute<Gebuehr, Integer> einsatzsuper6;
    public static volatile SingularAttribute<Gebuehr, Integer> version;
    public static volatile SingularAttribute<Gebuehr, Integer> einsatzprotipp;

}