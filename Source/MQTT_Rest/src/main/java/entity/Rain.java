/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
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
    @NamedQuery(name = Rain.getInDate, query = "Select r from Rain r where r.dateTime BETWEEN :startDate AND :endDate and r.weatherstation.village = :village")
})
public class Rain implements Serializable {
    public final static String getInDate = "getInDateRain";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    
    private Double amountLast5min;
    private Timestamp dateTime;
    
    @ManyToOne
    private Weatherstation weatherstation;

    public Rain() {}

    public Rain(Double amountLast5min, Timestamp dateTime, Weatherstation weatherstation) {
        this.amountLast5min = amountLast5min;
        this.dateTime = dateTime;
        this.weatherstation = weatherstation;
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public Double getAmountLast5min() {
        return amountLast5min;
    }

    public void setAmountLast5min(Double amountLast5min) {
        this.amountLast5min = amountLast5min;
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
        return "Rain=" + "Id:" + Id + ",amountLast5min:" + amountLast5min + ",dateTime:" + dateTime.toString();
    }
}
