package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONObject;


public class Repository {

    private static Repository instance;
    private static Connection connection;


    private Repository() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/weatherstation", "root", "root");
    }

    public static Repository getInstance() throws SQLException {
        if (Repository.instance == null) {
            Repository.instance = new Repository();
        }
        return Repository.instance;
    }

    public void reset() throws SQLException {
        try {
            Statement init = connection.createStatement();
            init.executeUpdate("TRUNCATE TABLE air");
            init.executeUpdate("TRUNCATE TABLE wind");
            init.executeUpdate("TRUNCATE TABLE temp");
            init.executeUpdate("TRUNCATE TABLE rain");

            connection.commit();
        } catch (Exception ex) {
            System.out.println("Fehler beim DB reset!");
            ex.printStackTrace();
        }
    }

    public String getAllWeatherstations() throws SQLException {
        String sql = "SELECT ws_id, location FROM weatherstations;";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        String result = "{\"list\": [";
        String help;
        resultSet.next();
        help = String.format("%2d, \"%s\" ", resultSet.getInt("ws_id"), resultSet.getString("location"));
        result = result + help;
        while (resultSet.next()){
            help = String.format(", %2d, \"%s\" ", resultSet.getInt("ws_id"), resultSet.getString("location"));
            result = result + help;
        }
        result = result + "] }";
        return result;
    }

    public String getAnualTemperatur(JSONObject o) {
        String result = "[-4,-1,-7,-12,-8,-5,-4,0,2,4,5,7,9,9,10,13,15,15,13,16,17,18,19,21,24,25,24,21,24,25,25,26,26,22,19,18,19,15,13,14,11,9,8,7,8,5,2,0,1,-1,-2,-3]";
        //sql = ""
        return result;
    }

    public String getAnualRain(JSONObject o) {
        String result = "[9.6,8.3,2.1,17.3,13.4,10.5,12.4,12.5,4.0,7.2,3.1,2.7,4.2,10.6,8.3,4.2,12.9,28.9,20.5,24.8,40.0,35.6,10.6,12.6,26.7,37.5,50.9,30.2,22.4,7.6,4.4,4.6,10.9,15.7,12.8,10.6,8.8,14.1,14.0,13.5,10.2,16.4,13.4,7.9,6.8,15.6,7.0,3.9,8.9,9.6,5.3,5.7]";
        return result;
    }
}
