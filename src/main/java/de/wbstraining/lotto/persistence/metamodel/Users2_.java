package de.wbstraining.lotto.persistence.metamodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.UserRoles;
import de.wbstraining.lotto.persistence.model.Users2;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Users2.class)
public class Users2_ { 

    public static volatile SingularAttribute<Users2, String> password;
//    public static volatile ListAttribute<Users2, UserRoles> userRolesList;
    public static volatile SingularAttribute<Users2, Integer> userId;
    public static volatile SingularAttribute<Users2, Kunde> kundeid;
    public static volatile SingularAttribute<Users2, String> username;

}