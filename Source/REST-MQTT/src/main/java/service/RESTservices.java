package service;

import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.json.JSONObject;
import repository.Repository;


@Path("rest")
public class RESTservices {

    Repository persistance = Repository.getInstance();

    public RESTservices() throws SQLException {
    }

    @GET
    @Path("reset")
    public void init() throws SQLException {
        persistance.reset();
    }

    @GET
    @Path("getAllWeatherstations")
    public String getAllWeatherstations() throws SQLException {
        return persistance.getAllWeatherstations();
    }

    @POST
    @Path("getAnualTemperatur")
    public String getAnualTemperatur(String input) {
        String output = "ERROR!";
        try {
            JSONObject o = new JSONObject(input);
            if (Integer.toString(o.getInt("year")) != "" && Integer.toString(o.getInt("ws_id")) != "") {
                output = persistance.getAnualTemperatur(o);
            }
        } catch (Exception ex) {
            System.out.println("Fehlerhafter input!");
            ex.printStackTrace();
        } finally {
            return output;
        }
    }

    @POST
    @Path("getAnualRain")
    public String getAnualRain(String input) {
        String output = "ERROR!";
        try {
            JSONObject o = new JSONObject(input);
            if (Integer.toString(o.getInt("year")) != "" && Integer.toString(o.getInt("ws_id")) != "") {
                output = persistance.getAnualRain(o);
            }
        } catch (Exception ex) {
            System.out.println("Fehlerhafter input!");
            ex.printStackTrace();
        } finally {
            return output;
        }
    }
}
