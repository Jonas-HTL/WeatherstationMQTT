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
    @NamedQuery(name = Temperatur.getInDate, query = "Select t from Temperatur t where t.dateTime BETWEEN :startDate AND :endDate and t.weatherstation.village = :village")
})
public class Temperatur implements Serializable {
    public final static String getInDate = "getInDateTemperatur";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    
    private Double temp;
    private String tempUnit;
    private Timestamp dateTime;
    
    @ManyToOne
    private Weatherstation weatherstation;

    public Temperatur() {
    }

    public Temperatur(Double temp, String tempUnit, Timestamp dateTime, Weatherstation weatherstation) {
        this.temp = temp;
        this.tempUnit = tempUnit;
        this.dateTime = dateTime;
        this.weatherstation = weatherstation;
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
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
        return "Temperatur=" + "Id:" + Id + ",temperatur:" + temp + ",tempUnit:" + tempUnit + ",dateTime:" + dateTime.toString();
    }
}
