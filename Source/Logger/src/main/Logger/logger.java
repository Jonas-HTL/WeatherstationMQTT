import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;
import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.sql.*;
import java.text.ParseException;


public class logger implements MqttCallback {

    MqttClient client;
    static Connection connection;
    static Server server;

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

    public static void main(String[] args) throws SQLException, MqttException, DeploymentException {
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/weatherstation", "root", "root");
        server  = new Server("localhost", 8025, "/websocket", WebsocketEndpoint.class);
        server.start();
        new logger().doDemo();
        while (true){}
    }

    public void doDemo(){
        try {
            client = new MqttClient("tcp://localhost:1883", "Sending");
            client.connect();
            client.setCallback(this);
            client.subscribe("/p4/");
            MqttMessage message = new MqttMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println(message);
        String s = message.toString();

        try{
            JSONObject parsedMessage = new JSONObject(s);

            if(persist(parsedMessage))
            {
                System.out.println("Mesaage was persisted!");
                for (Session session : WebsocketEndpoint.sessions) {
                    session.getAsyncRemote().sendText(s);
                }
            }
            else
                System.out.println("An error occurred!");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private boolean persist(JSONObject parsedMessage) throws ParseException {
        boolean outcome = true;
        String time  = parsedMessage.getString("time:");
        String sql = "";

        try {
            switch (parsedMessage.getInt("type")){
                case 2:
                    sql = String.format("INSERT INTO temperature(record_time, weatherstation, temperature) VALUES ('%s', %2d, %2d)"
                            , parsedMessage.getString("time:"), parsedMessage.getInt("id_ws"), parsedMessage.getInt("temp"));
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
        catch (Exception ex){
            ex.printStackTrace();
            outcome = false;
        }
        finally {
            return outcome;
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {}
}
