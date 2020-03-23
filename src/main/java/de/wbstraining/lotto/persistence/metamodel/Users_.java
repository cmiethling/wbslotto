package de.wbstraining.lotto.persistence.metamodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Groups;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Users;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile SingularAttribute<Users, String> firstName;
    public static volatile SingularAttribute<Users, String> lastName;
    public static volatile SingularAttribute<Users, String> password;
    public static volatile SingularAttribute<Users, String> middleName;
    public static volatile ListAttribute<Users, Groups> groupsList;
    public static volatile SingularAttribute<Users, Integer> userId;
    public static volatile SingularAttribute<Users, Kunde> kundeid;
    public static volatile SingularAttribute<Users, String> username;

}