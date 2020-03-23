package de.wbstraining.lotto.persistence.metamodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import de.wbstraining.lotto.persistence.model.Groups;
import de.wbstraining.lotto.persistence.model.Users;


@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-31T16:43:06")
@StaticMetamodel(Groups.class)
public class Groups_ { 

    public static volatile SingularAttribute<Groups, String> groupName;
    public static volatile ListAttribute<Groups, Users> usersList;
    public static volatile SingularAttribute<Groups, String> groupDesc;
    public static volatile SingularAttribute<Groups, Integer> groupId;

}