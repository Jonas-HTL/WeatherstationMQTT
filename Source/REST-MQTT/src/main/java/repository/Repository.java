package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

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
        JSONObject json = new JSONObject();
        String ws_id = "";
        String villagename = "";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        String result = "{\"list\": [";
        resultSet.next();
        ws_id = String.valueOf(resultSet.getInt("ws_id"));
        villagename = resultSet.getString("location");
        json.put("ws_id",ws_id);
        json.put("villagename",villagename);
        String s = json.toString();

        result = "[" + s;
        while (resultSet.next()){
            ws_id = String.valueOf(resultSet.getInt("ws_id"));
            villagename = resultSet.getString("location");
            json.put("ws_id",ws_id);
            json.put("villagename",villagename);
            s = "," + json.toString();
            result = result + s;
        }
        result = result + "]";
        return result;
    }

    public String getAnualTemperatur(JSONObject o){
        String result = "[-4,-1,-7,-12,-8,-5,-4,0,2,4,5,7,9,9,10,13,15,15,13,16,17,18,19,21,24,25,24,21,24,25,25,26,26,22,19,18,19,15,13,14,11,9,8,7,8,5,2,0,1,-1,-2,-3]";
        try {
            String sql = sql = String.format("SELECT AVG(temperature) AS temperature, record_time FROM temperature WHERE weatherstation = %2d and YEAR(record_time) = %2d GROUP BY WEEK(record_time)"
                    ,o.getInt("ws_id"), o.getInt("year"));
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Double> resultList = new LinkedList<>();

            for(int i = 0; i < 52; i++)
            {
                resultSet.next();
                resultList.add(resultSet.getDouble("temperature"));
            }
            String resultString = "[" + String.valueOf((int)Math.round(resultList.get(0)));
            for (int i = 1; i < resultList.size(); i++){
                resultString = resultString + "," + String.valueOf((int)Math.round(resultList.get(i)));
            }
            resultString = resultString + "]";
            //result = resultString;
            System.out.println(resultString);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            return result;
        }
    }

    public String getAnualRain(JSONObject o){
        String result = "[9.6,8.3,2.1,17.3,13.4,10.5,12.4,12.5,4.0,7.2,3.1,2.7,4.2,10.6,8.3,4.2,12.9,28.9,20.5,24.8,40.0,35.6,10.6,12.6,26.7,37.5,50.9,30.2,22.4,7.6,4.4,4.6,10.9,15.7,12.8,10.6,8.8,14.1,14.0,13.5,10.2,16.4,13.4,7.9,6.8,15.6,7.0,3.9,8.9,9.6,5.3,5.7]";
        try {
            String sql = sql = String.format("SELECT SUM(amount) AS amount, record_time FROM rain WHERE weatherstation = %2d and YEAR(record_time) = %2d GROUP BY WEEK(record_time)"
                    ,o.getInt("ws_id"), o.getInt("year"));
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Double> resultList = new LinkedList<>();

            for(int i = 0; i < 52; i++)
            {
                resultSet.next();
                resultList.add(resultSet.getDouble("amount"));
            }
            String resultString = "[" + String.valueOf(Double.parseDouble(String.format("%.1f", resultList.get(0))));
            for (int i = 1; i < resultList.size(); i++){
                resultString = resultString + "," + String.valueOf(Double.parseDouble(String.format("%.1f", resultList.get(i))));
            }
            resultString = resultString + "]";
            //result = resultString;
            System.out.println(resultString);

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            return result;
        }
    }
}
