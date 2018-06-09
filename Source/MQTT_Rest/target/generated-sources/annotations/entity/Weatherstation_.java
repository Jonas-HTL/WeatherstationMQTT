package entity;

import entity.Air;
import entity.Rain;
import entity.Temperatur;
import entity.Wind;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2018-05-16T08:41:06")
@StaticMetamodel(Weatherstation.class)
public class Weatherstation_ { 

    public static volatile ListAttribute<Weatherstation, Rain> rain;
    public static volatile SingularAttribute<Weatherstation, String> address;
    public static volatile SingularAttribute<Weatherstation, Long> Id;
    public static volatile ListAttribute<Weatherstation, Air> air;
    public static volatile SingularAttribute<Weatherstation, String> village;
    public static volatile ListAttribute<Weatherstation, Wind> wind;
    public static volatile ListAttribute<Weatherstation, Temperatur> temperatur;

}