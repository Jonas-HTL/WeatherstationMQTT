/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author dmayr
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Weatherstation.getPerVillage, query = "Select w from Weatherstation w where LOWER(w.village) = LOWER(:village)"),
    
})
public class Weatherstation implements Serializable {
    public final static String getPerVillage = "getPerVillage";
    public final static String getPerVillageAndWeekNr = "getPerVillageAndWN";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    
    private String village;
    
    private String address;
    
    @OneToMany(mappedBy = "weatherstation")
    private List<Wind> wind;
    
    @OneToMany(mappedBy = "weatherstation")
    private List<Rain> rain;
    
    @OneToMany(mappedBy = "weatherstation")
    private List<Air> air;
    
    @OneToMany(mappedBy = "weatherstation")
    private List<Temperatur> temperatur;

    public Weatherstation() {
    }

    public Weatherstation(String village, String address, List<Wind> wind, List<Rain> rain, List<Air> air, List<Temperatur> temperatur) {
        this.village = village;
        this.address = address;
        this.wind = wind;
        this.rain = rain;
        this.air = air;
        this.temperatur = temperatur;
    }
    
    public Weatherstation(String village, String address){
        this.village = village;
        this.address = address;
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Wind> getWind() {
        return wind;
    }

    public void setWind(List<Wind> wind) {
        this.wind = wind;
    }

    public List<Rain> getRain() {
        return rain;
    }

    public void setRain(List<Rain> rain) {
        this.rain = rain;
    }

    public List<Air> getAir() {
        return air;
    }

    public void setAir(List<Air> air) {
        this.air = air;
    }

    public List<Temperatur> getTemperatur() {
        return temperatur;
    }

    public void setTemperatur(List<Temperatur> temperatur) {
        this.temperatur = temperatur;
    }

    @Override
    public String toString() {
        return "Weatherstation=" + "Id:" + this.Id + ",village:" + this.village + ",address:" + this.address;
    }
}
