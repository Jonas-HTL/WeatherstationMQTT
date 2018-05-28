import org.json.JSONObject;

import java.sql.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Persistance {

    private static Persistance instance;
    private static Connection connection;


    private Persistance() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/weatherstation", "root", "root");
    }

    public static Persistance getInstance() throws SQLException {
        if (Persistance.instance == null) {
            Persistance.instance = new Persistance();
        }
        return Persistance.instance;
    }


    public boolean persist(JSONObject parsedMessage) throws ParseException {
        boolean outcome = true;
        String sql = "";

        try {
            switch (parsedMessage.getInt("type")) {
                case 2:
                    sql = String.format("INSERT INTO temperature(record_time, weatherstation, temperature) VALUES ('%s', %2d, %2d)"
                            , parsedMessage.getString("time"), parsedMessage.getInt("id_ws"), parsedMessage.getInt("temp"));
                    break;
                case 3:
                    sql = String.format("INSERT INTO air(record_time, weatherstation, pressure, humidity) VALUES ('%s', %2d, %2d, %2d)"
                            , parsedMessage.getString("time"), parsedMessage.getInt("id_ws"), parsedMessage.getInt("press"), parsedMessage.getInt("hum"));
                    break;
                case 1:
                    sql = String.format("INSERT INTO wind(record_time, weatherstation, intensity, direction) VALUES ('%s', %2d, %2d, '%s')"
                            , parsedMessage.getString("time"), parsedMessage.getInt("id_ws"), parsedMessage.getInt("int"), parsedMessage.getString("dir"));
                    break;
                case 4:
                    sql = String.format("INSERT INTO rain(record_time, weatherstation, amount) VALUES ('%s', %2d, %s)"
                            , parsedMessage.getString("time"), parsedMessage.getInt("id_ws"), String.valueOf(parsedMessage.getDouble("amount")));
                    break;
                default:
                    outcome = false;
            }

            if (outcome) {
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
                connection.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            outcome = false;
        } finally {
            return outcome;
        }
    }
}