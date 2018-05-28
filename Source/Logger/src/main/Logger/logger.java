import org.eclipse.paho.client.mqttv3.*;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.tyrus.server.Server;
import org.json.JSONObject;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.net.URI;
import java.sql.SQLException;

import static java.net.URI.create;


public class Logger implements MqttCallback {

    private MqttClient client;
    private Persistance persistance = Persistance.getInstance();
    private static Server server;


    public Logger() throws SQLException {
    }


    public static void main(String[] args) throws MqttException, DeploymentException, SQLException {
        server = new Server("localhost", 8025, "/websocket", WebsocketEndpoint.class);
        server.start();
        new Logger().init();
        while (true) {
        }
    }

    public void init() {
        try {
            client = new MqttClient("tcp://localhost:1883", "Sending");
            client.connect();
            client.setCallback(this);
            client.subscribe("p4/#");
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
        String[] splitTopics = topic.split("/");

        try {
            JSONObject parsedMessage = new JSONObject(s);

            if (persistance.persist(parsedMessage) ) {
                System.out.println("Mesaage was persisted!");
                for (Session session : WebsocketEndpoint.sessions) {
                    int test = WebsocketEndpoint.register.get(session);
                    String test2 = splitTopics[1];
                    if(WebsocketEndpoint.register.get(session).equals(Integer.parseInt(splitTopics[1]))) {
                        session.getAsyncRemote().sendText(s);
                    }
                }
            } else {
                System.out.println("An error occurred!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}
