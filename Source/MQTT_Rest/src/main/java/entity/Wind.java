/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author dmayr
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Wind.getInDate, query = "Select w from Wind w where w.dateTime BETWEEN :startDate AND :endDate and w.weatherstation.village = :village")
})
public class Wind implements Serializable {
    public final static String getInDate = "getInDateWind";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    
    private String direction;
    private Double force;
    private Timestamp dateTime;
    
    @ManyToOne
    private Weatherstation weatherstation;

    public Wind() {}

    public Wind(String direction, Double force, Timestamp dateTime, Weatherstation weatherstation) {
        this.direction = direction;
        this.force = force;
        this.dateTime = dateTime;
        this.weatherstation = weatherstation;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Double getForce() {
        return force;
    }

    public void setForce(Double force) {
        this.force = force;
    }

    public Timestamp getTimestamp() {
        return dateTime;
    }

    public void setTimestamp(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public Weatherstation getWeatherstation() {
        return weatherstation;
    }

    public void setWeatherstation(Weatherstation weatherstation) {
        this.weatherstation = weatherstation;
    }

    @Override
    public String toString() {
        return "Wind=" + "Id:" + Id + ",direction=" + direction + ",force=" + force + ",dateTime=" + dateTime.toString();
    }
}
