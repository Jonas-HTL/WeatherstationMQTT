package entity;

import entity.Weatherstation;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2018-05-16T08:41:06")
@StaticMetamodel(Temperatur.class)
public class Temperatur_ { 

    public static volatile SingularAttribute<Temperatur, LocalDateTime> dateTime;
    public static volatile SingularAttribute<Temperatur, Double> temp;
    public static volatile SingularAttribute<Temperatur, Weatherstation> weatherstation;
    public static volatile SingularAttribute<Temperatur, String> tempUnit;
    public static volatile SingularAttribute<Temperatur, Long> Id;

}