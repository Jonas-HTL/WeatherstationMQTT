import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class logger implements MqttCallback {

    MqttClient client;
    Connection connection;

    public logger() throws SQLException, MqttException {
        //connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/weatherstation", "root", "root");

        /*
        Statement init = connection.createStatement();
        init.executeUpdate("TRUNCATE TABLE air");
        init.executeUpdate("TRUNCATE TABLE wind");
        init.executeUpdate("TRUNCATE TABLE temp");
        init.executeUpdate("TRUNCATE TABLE rain");

        connection.commit();
        */
    }

    public static void main(String[] args) throws SQLException, MqttException {
        new logger().doDemo();
        while (true){}
    }

    public void doDemo(){
        try {
            client = new MqttClient("tcp://localhost:1883", "Sending");
            client.connect();
            client.setCallback(this);
            client.subscribe("test");
            MqttMessage message = new MqttMessage();
            //message.setPayload("A single message from my computer fff".getBytes());
            //client.publish("test", message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println(message);

        String s = message.toString();
        JSONObject parsedMessage = new JSONObject(s);

        if(persist(parsedMessage))
            System.out.println("Mesaage was persisted!");
        else
             System.out.println("An error occurred!");
    }


    private boolean persist(JSONObject parsedMessage) throws ParseException {
        boolean outcome = true;
        String sql = "";

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date result = (Date) df.parse(parsedMessage.getString("time"));

        //Date timestamp = DateparsedMessage.get("sdf");
        try {
            switch (parsedMessage.getInt("type")){
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
                    sql = String.format("INSERT INTO rain(amount) VALUES (%2d)", parsedMessage.getInt("amount"));
                    break;
                default:
                    outcome = false;
            }

            if (outcome) {
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
                connection.commit();
            }
        }
        catch (Exception e){
            System.out.println(e.getStackTrace());
            outcome = false;
        }
        finally {
            return outcome;
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) { }
}
