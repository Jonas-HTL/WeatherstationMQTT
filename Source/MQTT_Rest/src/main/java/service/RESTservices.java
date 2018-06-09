package service;

import entity.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.*;
import javax.ws.rs.*;
import org.json.JSONObject;
import repository.Repository;

/**
 * REST Web Service
 *
 * @author H. Lackinger
 */

@Path("rest")
public class RESTservices {


    Repository repo;

    /**
     * Creates a new instance of TeamStudentResource
     */
    public RESTservices() {
        repo = Repository.getRepo();
    }


    /**
     * Echo
     */
    @Path("init")
    @GET
    public void init(){
        Weatherstation w1 = new Weatherstation("Leonding", "Limesstraße 12");
        w1 = repo.persist(w1);
        Air a1 = new Air(20.0, 14.8, repo.convertToDatabaseColumn(LocalDateTime.now()), w1);
        repo.persist(a1);
        Air a2 = new Air(8.3, 20.1, repo.convertToDatabaseColumn(LocalDateTime.now()), w1);
        repo.persist(a2);
        Air a3 = new Air(2.3, 2.1, repo.convertToDatabaseColumn(LocalDateTime.now()), w1);
        repo.persist(a3);
        Air a4 = new Air(23.0, 17.1, repo.convertToDatabaseColumn(LocalDateTime.now()), w1);
        repo.persist(a4);
        
        Weatherstation w2 = new Weatherstation("Villach", "Villacherstr. 3");
        w2 = repo.persist(w2);
        Rain r1 = new Rain(20.0, repo.convertToDatabaseColumn(LocalDateTime.now()), w2);
        repo.persist(r1);
        Rain r2 = new Rain(10.9, repo.convertToDatabaseColumn(LocalDateTime.now()), w2);
        repo.persist(r2);
        Rain r3 = new Rain(6.7, repo.convertToDatabaseColumn(LocalDateTime.now()), w2);
        repo.persist(r3);
        Rain r4 = new Rain(35.1, repo.convertToDatabaseColumn(LocalDateTime.now()), w2);
        repo.persist(r4);
        
        Weatherstation w3 = new Weatherstation("Leoben", "Hauswegstr. 17");
        w3 = repo.persist(w3);
        Temperatur t1 = new Temperatur(17.2, "Celsius", repo.convertToDatabaseColumn(LocalDateTime.now()), w3);
        repo.persist(t1);
        Temperatur t2 = new Temperatur(12.8, "Celsius", repo.convertToDatabaseColumn(LocalDateTime.now()), w3);
        repo.persist(t2);
        Temperatur t3 = new Temperatur(-20.8, "Celsius", repo.convertToDatabaseColumn(LocalDateTime.now()), w3);
        repo.persist(t3);
        Temperatur t4 = new Temperatur(45.9, "Celsius", repo.convertToDatabaseColumn(LocalDateTime.now()), w3);
        repo.persist(t4);
        
        Weatherstation w4 = new Weatherstation("Berlin", "Hauptplatz 27");
        w4 = repo.persist(w4);
        Wind wi1 = new Wind("Norden", 23.8, repo.convertToDatabaseColumn(LocalDateTime.now()), w4);
        repo.persist(wi1);
        Wind wi2 = new Wind("Südwest", 12.4, repo.convertToDatabaseColumn(LocalDateTime.now()), w4);
        repo.persist(wi2);
        Wind wi3 = new Wind("Westen", 5.9, repo.convertToDatabaseColumn(LocalDateTime.now()), w4);
        repo.persist(wi3);
        Wind wi4 = new Wind("Nodost", 17.4, repo.convertToDatabaseColumn(LocalDateTime.now()), w4);
        repo.persist(wi4);
        Wind wi5 = new Wind("Westen", 13.2, repo.convertToDatabaseColumn(LocalDateTime.now()), w4);
        repo.persist(wi5);
    }
    
    
    @Path("CreateWeatherstation")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Weatherstation createWeatherstation(Weatherstation weatherstation) {
        if(weatherstation.getAddress().isEmpty() || weatherstation.getVillage().isEmpty()){
            return null;
        }
        weatherstation = repo.persist(weatherstation);
        return weatherstation;
    }
    
    @Path("GetWeatherstation/{id}")
    @GET
    public String getWeatherstationPerId(@PathParam("id") Long id) {
        return repo.getWeatherstation(id).toString();
    }
    
    @Path("GetWeatherstationsInVillage")
    @POST
    public String getWeatherstationsPerVillage(String jStr) {
        JSONObject job = new JSONObject(jStr);
        String village = job.getString("village");
        List<Weatherstation> wl = repo.getWeatherstationsByVillage(village);
        return wl.toString();
    }
    
    @Path("GetAllWeatherstations") 
    @GET
    public String getAllWeatherstations() {
        List<Weatherstation> weatherstations = repo.getAll("Weatherstation");
        List<String> villages = new LinkedList<>();
        for(Weatherstation w : weatherstations){
            villages.add(w.getVillage());
        }
        JSONObject job = new JSONObject();
        job.put("villages", villages);
        return job.toString();
    }
    
    @Path("GetRainPerWeek")
    @POST
    //public List<Rain> getRainPerWeek(int weekNumber, int year, String village) {
    public String getRainPerWeek(String jStr) {
        JSONObject job = new JSONObject(jStr);
        int weekNumber = job.getInt("weekNumber");
        int year = job.getInt("year");
        String village = job.getString("village");
        List<Rain> rl = repo.getPerWeek("Rain", weekNumber, year, village);
        return rl.toString();
    }
    
    @Path("GetRainPerDay")
    @POST
    public String getRainPerDay(String jStr) {
        JSONObject job = new JSONObject(jStr);
        String dateStr = job.getString("date");
        dateStr += " 00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = null;
        date = LocalDateTime.parse(dateStr, formatter);
        String village = job.getString("village");
        List<Rain> rainlist = repo.getPerDay("Rain", date, village);
        Collections.sort(rainlist, (Rain r1, Rain r2) -> r1.getTimestamp().compareTo(r2.getTimestamp()));
        List<Rain> toRemove = new LinkedList<>();
        for(int i = 1; i < rainlist.size(); i++){
            if(rainlist.get(i-1).getTimestamp().equals(rainlist.get(i).getTimestamp())){
                toRemove.add(rainlist.get(i));
            }
        }
        for(Rain r : toRemove){
            rainlist.remove(r);
        }
        return rainlist.toString();
    }
    
    
    @Path("GetRainPerYear")
    @POST
    public String getRainPerYear(String jobStr) {
        JSONObject job = new JSONObject(jobStr);
        int year = job.getInt("year");
        String village = job.getString("village");
        List<Rain> rainlist = repo.getRainPerYear(year, village);
        Collections.sort(rainlist, (Rain r1, Rain r2) -> r1.getTimestamp().compareTo(r2.getTimestamp()));
        List<Rain> toRemove = new LinkedList<>();
        for(int i = 1; i < rainlist.size(); i++){
            if(rainlist.get(i-1).getTimestamp().equals(rainlist.get(i).getTimestamp())){
                toRemove.add(rainlist.get(i));
            }
        }
        for(Rain r : toRemove){
            rainlist.remove(r);
        }
        return rainlist.toString();
    }
    
    @Path("GetTemperaturPerWeek")
    @POST
    public String getTemperaturPerWeek(String jStr) {
        JSONObject job = new JSONObject(jStr);
        int weekNumber = job.getInt("weekNumber");
        int year = job.getInt("year");
        String village = job.getString("village");
        List<Temperatur> templist = repo.getPerWeek("Temperatur", weekNumber, year, village);
        Collections.sort(templist, (Temperatur t1, Temperatur t2) -> t1.getTimestamp().compareTo(t2.getTimestamp()));
        List<Temperatur> toRemove = new LinkedList<>();
        for(int i = 1; i < templist.size(); i++){
            if(templist.get(i-1).getTimestamp().equals(templist.get(i).getTimestamp())){
                toRemove.add(templist.get(i));
            }
        }
        for(Temperatur t : toRemove){
            templist.remove(t);
        }
        return templist.toString();
    }
    
    @Path("GetTemperaturPerDay")
    @POST
    public String getTemperaturPerDay(String jobStr) {
        JSONObject job = new JSONObject(jobStr);
        String dateStr = job.getString("date");
        dateStr += " 00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(dateStr, formatter);
        String village = job.getString("village");
        
        List<Temperatur> templist = repo.getPerDay("Temperatur", date, village);
        Collections.sort(templist, (Temperatur t1, Temperatur t2) -> t1.getTimestamp().compareTo(t2.getTimestamp()));
        List<Temperatur> toRemove = new LinkedList<>();
        for(int i = 1; i < templist.size(); i++){
            if(templist.get(i-1).getTimestamp().equals(templist.get(i).getTimestamp())){
                toRemove.add(templist.get(i));
            }
        }
        for(Temperatur t : toRemove){
            templist.remove(t);
        }
        return templist.toString();
    }
    
    @Path("GetTemperaturPerYear")
    @POST
    public String getTemperaturPerYear(String jobStr) {
        JSONObject job = new JSONObject(jobStr);
        int year = job.getInt("year");
        String village = job.getString("village");
        
        List<Temperatur> templist = repo.getTemperaturPerYear(year, village);
        Collections.sort(templist, (Temperatur t1, Temperatur t2) -> t1.getTimestamp().compareTo(t2.getTimestamp()));
        List<Temperatur> toRemove = new LinkedList<>();
        for(int i = 1; i < templist.size(); i++){
            if(templist.get(i-1).getTimestamp().equals(templist.get(i).getTimestamp())){
                toRemove.add(templist.get(i));
            }
        }
        for(Temperatur t : toRemove){
            templist.remove(t);
        }
        return templist.toString();
    }
    
    @Path("GetAirPerWeek")
    @POST
    public String getAirPerWeek(String jStr) {
        JSONObject job = new JSONObject(jStr);
        int weekNumber = job.getInt("weekNumber");
        int year = job.getInt("year");
        String village = job.getString("village");
        List<Air> airlist = repo.getPerWeek("Air", weekNumber, year, village);
        Collections.sort(airlist, (Air a1, Air a2) -> a1.getTimestamp().compareTo(a2.getTimestamp()));
        List<Air> toRemove = new LinkedList<>();
        for(int i = 1; i < airlist.size(); i++){
            if(airlist.get(i-1).getTimestamp().equals(airlist.get(i).getTimestamp())){
                toRemove.add(airlist.get(i));
            }
        }
        for(Air a : toRemove){
            airlist.remove(a);
        }
        return airlist.toString();
    }
    
    @Path("GetAirPerDay")
    @POST
    public String getAirPerDay(String jobStr) {
        JSONObject job = new JSONObject(jobStr);
        String dateStr = job.getString("date");
        dateStr += " 00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(dateStr, formatter);
        String village = job.getString("village");
        
        List<Air> airlist = repo.getPerDay("Air", date, village);
        Collections.sort(airlist, (Air a1, Air a2) -> a1.getTimestamp().compareTo(a2.getTimestamp()));
        List<Air> toRemove = new LinkedList<>();
        for(int i = 1; i < airlist.size(); i++){
            if(airlist.get(i-1).getTimestamp().equals(airlist.get(i).getTimestamp())){
                toRemove.add(airlist.get(i));
            }
        }
        for(Air a : toRemove){
            airlist.remove(a);
        }
        return airlist.toString();
    }
    
    @Path("GetAirPerYear")
    @POST
    public String getAirPerYear(String jobStr) {
        JSONObject job = new JSONObject(jobStr);
        int year = job.getInt("year");
        String village = job.getString("village");
        
        List<Air> airlist = repo.getAirPerYear(year, village);
        Collections.sort(airlist, (Air a1, Air a2) -> a1.getTimestamp().compareTo(a2.getTimestamp()));
        List<Air> toRemove = new LinkedList<>();
        for(int i = 1; i < airlist.size(); i++){
            if(airlist.get(i-1).getTimestamp().equals(airlist.get(i).getTimestamp())){
                toRemove.add(airlist.get(i));
            }
        }
        for(Air a : toRemove){
            airlist.remove(a);
        }
        return airlist.toString();
    }
    
    @Path("GetWindPerWeek")
    @POST
    public String getWindPerWeek(String jStr) {
        JSONObject job = new JSONObject(jStr);
        int weekNumber = job.getInt("weekNumber");
        int year = job.getInt("year");
        String village = job.getString("village");
        List<Wind> windlist = repo.getPerWeek("Wind", weekNumber, year, village);
        Collections.sort(windlist, (Wind w1, Wind w2) -> w1.getTimestamp().compareTo(w2.getTimestamp()));
        List<Wind> toRemove = new LinkedList<>();
        for(int i = 1; i < windlist.size(); i++){
            if(windlist.get(i-1).getTimestamp().equals(windlist.get(i).getTimestamp())){
                toRemove.add(windlist.get(i));
            }
        }
        for(Wind w : toRemove){
            windlist.remove(w);
        }
        return windlist.toString();
    }
    
    @Path("GetWindPerDay")
    @POST
    public String getWindPerDay(String jobStr) {
        JSONObject job = new JSONObject(jobStr);
        String dateStr = job.getString("date");
        dateStr += " 00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(dateStr, formatter);
        String village = job.getString("village");
    
        List<Wind> windlist = repo.getPerDay("Wind", date, village);
        Collections.sort(windlist, (Wind w1, Wind w2) -> w1.getTimestamp().compareTo(w2.getTimestamp()));
        List<Wind> toRemove = new LinkedList<>();
        for(int i = 1; i < windlist.size(); i++){
            if(windlist.get(i-1).getTimestamp().equals(windlist.get(i).getTimestamp())){
                toRemove.add(windlist.get(i));
            }
        }
        for(Wind w : toRemove){
            windlist.remove(w);
        }
        return windlist.toString();
    }
    
    @Path("GetWindPerYear")
    @POST
    public String getWindPerYear(String jobStr) {
        JSONObject job = new JSONObject(jobStr);
        int year = job.getInt("year");
        String village = job.getString("village");
        
        List<Wind> windlist = repo.getWindPerYear(year, village);
        Collections.sort(windlist, (Wind w1, Wind w2) -> w1.getTimestamp().compareTo(w2.getTimestamp()));
        List<Wind> toRemove = new LinkedList<>();
        for(int i = 1; i < windlist.size(); i++){
            if(windlist.get(i-1).getTimestamp().equals(windlist.get(i).getTimestamp())){
                toRemove.add(windlist.get(i));
            }
        }
        for(Wind w : toRemove){
            windlist.remove(w);
        }
        return windlist.toString();
    }
}