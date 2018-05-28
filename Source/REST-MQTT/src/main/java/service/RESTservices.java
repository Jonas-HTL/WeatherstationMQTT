package service;

import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

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
    public Response getAllWeatherstations() throws SQLException {
        Response.ResponseBuilder response = Response.ok(persistance.getAllWeatherstations()).header("Access-Control-Allow-Origin", "*");
        return response.build();
    }

    @POST
    @Path("getAnualTemperature")
    public Response getAnualTemperatur(String input) {
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
            Response.ResponseBuilder response = Response.ok(output).header("Access-Control-Allow-Origin", "*");
            return response.build();
            //return output;
        }
    }

    @POST
    @Path("getAnualRain")
    public Response getAnualRain(String input) {
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
            Response.ResponseBuilder response = Response.ok(output).header("Access-Control-Allow-Origin", "*");
            return response.build();
            //return output;
        }
    }
}
