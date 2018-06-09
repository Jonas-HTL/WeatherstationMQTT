package entity;

import entity.Weatherstation;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2018-05-16T08:41:06")
@StaticMetamodel(Air.class)
public class Air_ { 

    public static volatile SingularAttribute<Air, LocalDateTime> dateTime;
    public static volatile SingularAttribute<Air, Weatherstation> weatherstation;
    public static volatile SingularAttribute<Air, Double> humidity;
    public static volatile SingularAttribute<Air, Long> Id;
    public static volatile SingularAttribute<Air, Double> pressure;

}