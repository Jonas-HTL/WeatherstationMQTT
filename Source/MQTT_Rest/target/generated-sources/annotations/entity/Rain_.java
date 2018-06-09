package entity;

import entity.Weatherstation;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2018-05-16T08:41:06")
@StaticMetamodel(Rain.class)
public class Rain_ { 

    public static volatile SingularAttribute<Rain, LocalDateTime> dateTime;
    public static volatile SingularAttribute<Rain, Weatherstation> weatherstation;
    public static volatile SingularAttribute<Rain, Double> amountLast5min;
    public static volatile SingularAttribute<Rain, Long> Id;

}