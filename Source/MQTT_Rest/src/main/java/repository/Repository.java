/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import entity.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;

/**
 *
 * @author dmayr
 */
public class Repository {
    EntityManagerFactory emf = null;
    EntityManager em = null;
    
    private static Repository repo = null;
    
    public static Repository getRepo(){
        if(repo == null){
            repo = new Repository();
        }
        return repo;
    }
    
    private Repository(){
        emf = Persistence.createEntityManagerFactory("Weatherstation-PU");
        em = emf.createEntityManager();
    }
    
    public <T> T persist(T entity) {
        try{
            em.getTransaction().begin();
            entity = em.merge(entity);
            em.getTransaction().commit();
        }catch(Exception e){
            printStackTrace(e);
            return null;
        }
        return entity;
    }
    
    public Weatherstation getWeatherstation(Long id) {
        return em.find(Weatherstation.class, id);
    }

    public List<Weatherstation> getWeatherstationsByVillage(String village) {
        return em.createNamedQuery(Weatherstation.getPerVillage, Weatherstation.class)
                .setParameter("village", village)
                .getResultList();
    }

    public <T> List<T> getAll(String object) {
        String query = "SELECT w FROM " + object + " w";
        return em.createQuery(query).getResultList();
    }

    public <T> List<T> getPerDay(String entity, LocalDateTime date, String village) {
        LocalDateTime endDate = date.plusDays(1);
        Timestamp startDateTsmp =  convertToDatabaseColumn(date);
        Timestamp endDateTsmp = convertToDatabaseColumn(endDate);
        Query q = em.createQuery("Select r from "+entity+" r where r.dateTime BETWEEN :startdate And :enddate And r.weatherstation.village = :village");
        q.setParameter("startdate", startDateTsmp);
        q.setParameter("enddate", endDateTsmp);
        q.setParameter("village", village);
        
        return q.getResultList();
    }
    
    public <T> List<T> getPerWeek(String entity, int weekNumber, int year, String village) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
        calendar.set(Calendar.YEAR, year);
        
        Date input = calendar.getTime();
        LocalDateTime startdate = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime enddate = startdate.plusDays(7);
        
        Timestamp startDateTsmp = convertToDatabaseColumn(startdate);
        Timestamp endDateTsmp = convertToDatabaseColumn(enddate);
        
        Query q = em.createQuery("Select r from "+entity+" r where r.dateTime BETWEEN :startdate And :enddate And r.weatherstation.village = :village");
        q.setParameter("startdate", startDateTsmp);
        q.setParameter("enddate", endDateTsmp);
        q.setParameter("village", village);
        return q.getResultList();
    }
    
    public List<Rain> getRainPerYear(int year, String village) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        
        Date input = calendar.getTime();
        LocalDateTime startdate = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime enddate = startdate.plusYears(1);
        
        Timestamp startdateTsmp = convertToDatabaseColumn(startdate);
        Timestamp enddateTsmp = convertToDatabaseColumn(enddate);
        
        List<Rain> rainInYear = em.createNamedQuery(Rain.getInDate, Rain.class)
                .setParameter("startDate", startdateTsmp)
                .setParameter("endDate", enddateTsmp)
                .setParameter("village", village).getResultList(); //Weatherstation id
        
        List<Rain> rainPerDay = new LinkedList<>();
        List<Rain> avgDayList = new LinkedList<>();
        for(int i = 0; i <= rainInYear.size(); i++){
            if(i == 0){
                rainPerDay.add(rainInYear.get(i));
            }
            else if(rainInYear.size() != i &&(convertToEntityAttribute(rainInYear.get(i-1).getTimestamp()).getDayOfYear() == 
                    convertToEntityAttribute(rainInYear.get(i).getTimestamp()).getDayOfYear())){
                rainPerDay.add(rainInYear.get(i));
            }
            else{
                int length = 0;
                Double sumAmounts = 0.0;
                
                LocalDateTime day = convertToEntityAttribute(rainPerDay.get(0).getTimestamp());
                day = day.minusNanos(day.getNano());
                day = day.minusSeconds(day.getSecond());
                day = day.minusMinutes(day.getMinute());
                day = day.minusHours(day.getHour());
                
                Weatherstation weatherstation = rainPerDay.get(0).getWeatherstation();
                
                for(Rain r : rainPerDay){
                    sumAmounts += r.getAmountLast5min();
                    length++;
                }
                //rainPerDay.add(rainInYear.get(i));
                
                Double avg = sumAmounts/length;
                avg = Math.round(avg * 100.0) / 100.0;
                Rain avgRain = new Rain(avg, convertToDatabaseColumn(day), weatherstation);
                avgDayList.add(avgRain);
            }
        }
        return avgDayList;
    }
    
    public List<Air> getAirPerYear(int year, String village) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        
        Date input = calendar.getTime();
        LocalDateTime startdate = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Timestamp startdateTsmp = convertToDatabaseColumn(startdate);
        LocalDateTime enddate = startdate.plusYears(1);
        Timestamp enddateTsmp = convertToDatabaseColumn(enddate);
        
        List<Air> airInYear = em.createNamedQuery(Air.getInDate, Air.class)
                .setParameter("startDate", startdateTsmp)
                .setParameter("endDate", enddateTsmp)
                .setParameter("village", village).getResultList();
        
        List<Air> airPerDay = new LinkedList<>();
        List<Air> avgDayList = new LinkedList<>();
        for(int i = 0; i <= airInYear.size(); i++){
            if(i == 0){
                airPerDay.add(airInYear.get(i));
            }
            else if(i != airInYear.size() && (convertToEntityAttribute(airInYear.get(i-1).getTimestamp()).getDayOfYear() == 
                    convertToEntityAttribute(airInYear.get(i).getTimestamp()).getDayOfYear())){
                airPerDay.add(airInYear.get(i));
            }
            else{
                int length = 0;
                Double sumHumidity = 0.0;
                Double sumPressure = 0.0;
                
                LocalDateTime day = convertToEntityAttribute(airPerDay.get(0).getTimestamp());
                day = day.minusNanos(day.getNano());
                day.minusSeconds(day.getSecond());
                day.minusMinutes(day.getMinute());
                day.minusHours(day.getHour());
                
                Weatherstation weatherstation = airPerDay.get(0).getWeatherstation();
                
                for(Air a : airPerDay){
                    sumHumidity += a.getHumidity();
                    sumPressure += a.getPressure();
                    length++;
                }
                //airPerDay.add(airInYear.get(i));
                
                Double avgHumidity = sumHumidity/length;
                avgHumidity = Math.round(avgHumidity * 100.0) / 100.0;
                Double avgPressure = sumPressure/length;
                avgPressure = Math.round(avgPressure * 100.0) / 100.0;
                Air avgAir = new Air(avgHumidity, avgPressure, convertToDatabaseColumn(day), weatherstation);
                avgDayList.add(avgAir);
            }
        }
        return avgDayList;
    }
    
    public List<Wind> getWindPerYear(int year, String village) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        
        Date input = calendar.getTime();
        LocalDateTime startdate = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Timestamp startdateTsmp = convertToDatabaseColumn(startdate);
        LocalDateTime enddate = startdate.plusYears(1);
        Timestamp enddateTsmp = convertToDatabaseColumn(enddate);
        
        List<Wind> windInYear = em.createNamedQuery(Wind.getInDate, Wind.class)
                .setParameter("startDate", startdateTsmp)
                .setParameter("endDate", enddateTsmp)
                .setParameter("village", village).getResultList();
        
        List<Wind> windPerDay = new LinkedList<>();
        List<Wind> avgDayList = new LinkedList<>();
        for(int i = 0; i <= windInYear.size(); i++){
            if(i == 0){
                windPerDay.add(windInYear.get(i));
            }
            else if(i != windInYear.size() && (convertToEntityAttribute(windInYear.get(i-1).getTimestamp()).getDayOfYear() == 
                    convertToEntityAttribute(windInYear.get(i).getTimestamp()).getDayOfYear())){
                windPerDay.add(windInYear.get(i));
            }
            else{
                int length = 0;
                Double sumForce = 0.0;
                List<String> sumDirection = new LinkedList<>();
                
                LocalDateTime day = convertToEntityAttribute(windPerDay.get(0).getTimestamp());
                day = day.minusNanos(day.getNano());
                day = day.minusSeconds(day.getSecond());
                day = day.minusMinutes(day.getMinute());
                day = day.minusHours(day.getHour());
                
                Weatherstation weatherstation = windPerDay.get(0).getWeatherstation();
                
                for(Wind w : windPerDay){
                    sumForce += w.getForce();
                    sumDirection.add(w.getDirection());
                    length++;
                }
                //windPerDay.add(windInYear.get(i));
                
                Double avgForce = sumForce/length;
                avgForce = Math.round(avgForce * 100.0) / 100.0;
                
                Collections.sort(sumDirection, (String s1, String s2) -> s1.compareTo(s2));
                String finalDirection = "";
                String helpDirection = "";
                int maxDirectionCounter = 0;
                int directionCounter = 0;
                for(int j = 0; j <= sumDirection.size(); j++){
                    if(j == 0){
                        helpDirection = sumDirection.get(j);
                        directionCounter++;
                    }
                    else if(j != sumDirection.size() && sumDirection.get(j-1).equals(sumDirection.get(j))){
                        directionCounter++;
                    }
                    else{
                        if(directionCounter > maxDirectionCounter){
                            maxDirectionCounter = directionCounter;
                            finalDirection = helpDirection;
                        }
                        directionCounter = 1;
                        helpDirection = (j != sumDirection.size()) ? sumDirection.get(j) : null;
                    }
                }
                
                Wind avgWind = new Wind(finalDirection, avgForce, convertToDatabaseColumn(day), weatherstation);
                avgDayList.add(avgWind);
            }
        }
        return avgDayList;
    }
    
    public List<Temperatur> getTemperaturPerYear(int year, String village) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        
        Date input = calendar.getTime();
        LocalDateTime startdate = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Timestamp startdateTsmp = convertToDatabaseColumn(startdate);
        LocalDateTime enddate = startdate.plusYears(1);
        Timestamp enddateTsmp = convertToDatabaseColumn(enddate);
        
        List<Temperatur> temperaturInYear = em.createNamedQuery(Temperatur.getInDate, Temperatur.class)
                .setParameter("startDate", startdateTsmp)
                .setParameter("endDate", enddateTsmp)
                .setParameter("village", village).getResultList();
        
        List<Temperatur> temperaturPerDay = new LinkedList<>();
        List<Temperatur> avgDayList = new LinkedList<>();
        for(int i = 0; i <= temperaturInYear.size(); i++){
            if(i == 0){
                temperaturPerDay.add(temperaturInYear.get(i));
            }
            else if(i != temperaturInYear.size() && (convertToEntityAttribute(temperaturInYear.get(i-1).getTimestamp()).getDayOfYear() == 
                    convertToEntityAttribute(temperaturInYear.get(i).getTimestamp()).getDayOfYear())){
                temperaturPerDay.add(temperaturInYear.get(i));
            }
            else{
                int length = 0;
                Double sumTemp = 0.0;
                
                LocalDateTime day = convertToEntityAttribute(temperaturPerDay.get(0).getTimestamp());
                day = day.minusNanos(day.getNano());
                day = day.minusSeconds(day.getSecond());
                day = day.minusMinutes(day.getMinute());
                day = day.minusHours(day.getHour());
                
                Weatherstation weatherstation = temperaturPerDay.get(0).getWeatherstation();
                
                for(Temperatur t : temperaturPerDay){
                    if(t.getTempUnit().equals("Celsius")){
                        sumTemp += t.getTemp();
                    }else if(t.getTempUnit().equals("Fahrenheit")){
                        Double tempInCelsius = (t.getTemp()-32)/1.8;
                        sumTemp += tempInCelsius;
                    }else{
                        sumTemp += t.getTemp();
                    }
                    length++;
                }
                //temperaturPerDay.add(temperaturInYear.get(i));
                
                Double avgTemp = sumTemp/length;
                avgTemp = Math.round(avgTemp * 100.0) / 100.0;
                Temperatur avgTemperatur = new Temperatur(avgTemp, "Celsius", convertToDatabaseColumn(day), weatherstation);
                avgDayList.add(avgTemperatur);
            }
        }
        return avgDayList;
    }
    
    public Timestamp convertToDatabaseColumn(LocalDateTime locDate) {
    	return (locDate == null ? null : Timestamp.valueOf(locDate));
    }

    public LocalDateTime convertToEntityAttribute(java.sql.Timestamp sqlDate) {
    	return (sqlDate == null ? null : sqlDate.toLocalDateTime());
    }
}
