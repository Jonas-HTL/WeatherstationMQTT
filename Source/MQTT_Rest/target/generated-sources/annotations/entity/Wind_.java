package entity;

import entity.Weatherstation;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2018-05-16T08:41:06")
@StaticMetamodel(Wind.class)
public class Wind_ { 

    public static volatile SingularAttribute<Wind, LocalDateTime> dateTime;
    public static volatile SingularAttribute<Wind, Weatherstation> weatherstation;
    public static volatile SingularAttribute<Wind, Double> force;
    public static volatile SingularAttribute<Wind, Long> Id;
    public static volatile SingularAttribute<Wind, String> direction;

}