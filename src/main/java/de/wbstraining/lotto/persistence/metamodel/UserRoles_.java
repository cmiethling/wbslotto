package de.wbstraining.lotto.persistence.metamodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.UserRoles;
import de.wbstraining.lotto.persistence.model.Users2;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(UserRoles.class)
public class UserRoles_ { 

    public static volatile SingularAttribute<UserRoles, String> role;
    public static volatile ListAttribute<UserRoles, String> userName;
    public static volatile SingularAttribute<UserRoles, Integer> userId;
    public static volatile SingularAttribute<UserRoles, Long> userRolesId;	

}