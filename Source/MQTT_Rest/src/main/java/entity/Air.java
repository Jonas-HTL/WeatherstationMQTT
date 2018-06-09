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
    @NamedQuery(name = Air.getInDate, query = "Select a from Air a where a.dateTime BETWEEN :startDate AND :endDate and a.weatherstation.village = :village")
})
public class Air implements Serializable {
    public final static String getInDate = "getInDateAir";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    
    private Double humidity;
    private Double pressure;
    private Timestamp dateTime;
    
    @ManyToOne
    private Weatherstation weatherstation;

    public Air() {
    }

    public Air(Double humidity, Double pressure, Timestamp dateTime, Weatherstation weatherstation) {
        this.humidity = humidity;
        this.pressure = pressure;
        this.dateTime = dateTime;
        this.weatherstation = weatherstation;
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
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
        return "Air=" + "Id:" + Id + ",humidity:" + humidity + ",pressure:" + pressure + ",dateTime:" + dateTime.toString();
    }
}
