import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

public class Main {

    /*
     * Todo
     * Windrichtung
     * Windgeschwindigkeit
     * Temperatur
     * regen
     * luftfeuchtigkeit
     * luftdruck*/

    private static MqttClient client;

    public static void main(String[] args) throws MqttException {
        client = new MqttClient("tcp://localhost:1883",MqttClient.generateClientId());
        client.connect();

        //nur um werte zu checken
        /*
        for (int i =0; i<100;i++){
            System.out.println("Temp: "+getValuesInitial()[0]+
                    "°C, Hum:"+getValuesInitial()[1] + "%, Rainfall: " + getValuesInitial()[2] + "%, Wind: "
                    + getValuesInitial()[3]+"km/h");
        }
        */

        try {
            while (true) {
                initDB();
                int[] a = getValuesInitial();
                LocalDateTime t = LocalDateTime.now();

                MqttMessage messageTemp = new MqttMessage();
                messageTemp.setPayload(makeJsonTemp(a[0], t).getBytes());
                client.publish("p4/1/",messageTemp);

                MqttMessage messageWind = new MqttMessage();
                messageWind.setPayload(makeJsonWind(a[3],getWindDir(), t).getBytes());
                client.publish("p4/1/",messageWind);

                MqttMessage messageAir = new MqttMessage();
                messageAir.setPayload(makeJsonAir(a[1],a[4], t).getBytes());
                client.publish("p4/1", messageAir);

                Thread.sleep(300 * 1000);  //Alle 5 min werden werte generiert
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.disconnect();
    }

    public static void initDB(){
        DateTime dateTime = new DateTime(2017, 1, 1, 0, 1, 0);
        try {
            while (dateTime.year().get() < 2018) {
                LocalDateTime t = LocalDateTime.parse(dateTime.toLocalDateTime().toString());
                int[] a = getValuesInitialDBINIT(t);

                MqttMessage messageTemp = new MqttMessage();
                messageTemp.setPayload(makeJsonTemp(a[0], t).getBytes());
                client.publish("p4/1/",messageTemp);

                MqttMessage messageWind = new MqttMessage();
                messageWind.setPayload(makeJsonWind(a[3],getWindDir(), t).getBytes());
                client.publish("p4/1/",messageWind);

                MqttMessage messageAir = new MqttMessage();
                messageAir.setPayload(makeJsonAir(a[1],a[4], t).getBytes());
                client.publish("p4/1", messageAir);

                MqttMessage messageRain = new MqttMessage();
                messageRain.setPayload(makeJsonRain(t).getBytes());
                client.publish("p4/1", messageRain);

                dateTime = dateTime.plusMinutes(60);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int[] getValuesInitial(){

        int temp = 0;
        int hum = 0;
        int rainfall = 0;
        int wind;
        int airpressure;

        int time = LocalDateTime.now().getHour();
        Random random = new Random();

        wind = random.nextInt(2)+10;
        airpressure = random.nextInt(3)+ 1013;

        if ( time <= 6 ){
            temp = random.nextInt(9)+ 9;//9-17
            hum = random.nextInt(21)+70;//70-90%
            //rainfall = random.nextInt(20);

        }
        else if(time > 6 && time<= 12){
            temp = random.nextInt(6)+ 12;//12 -18
            hum = random.nextInt(28)+ 53;//53-80%
            //rainfall=random.nextInt(10);
        }
        else if(time > 12 && time<= 18){
            temp = random.nextInt(11)+ 19;//19 -30
            hum = random.nextInt(21)+ 30;//30-50%
            //rainfall = random.nextInt(5);
        }
        else if(time > 18 && time<= 23){
            temp = random.nextInt(6)+ 12;//12 -18
            hum = random.nextInt(21)+ 50;//50-70%
            //rainfall = random.nextInt(15);
        }

        int[] res = new int[5];
        res[0] = temp;
        res[1] = hum - (temp/2);
        res[2] = rainfall;
        res[3] = wind;
        res[4] = airpressure;

        return res;
    }

    public static int[] getValuesInitialDBINIT(LocalDateTime input){

        int temp = 0;
        int hum = 0;
        int rainfall = 0;
        int wind;
        int airpressure;

        int time = input.getHour();
        Random random = new Random();

        wind = random.nextInt(2)+10;
        airpressure = random.nextInt(3)+ 1013;

        if ( time <= 6 ){
            temp = random.nextInt(9)+ 9;//9-17
            hum = random.nextInt(21)+70;//70-90%
            //rainfall = random.nextInt(20);

        }
        else if(time > 6 && time<= 12){
            temp = random.nextInt(6)+ 12;//12 -18
            hum = random.nextInt(28)+ 53;//53-80%
            //rainfall=random.nextInt(10);
        }
        else if(time > 12 && time<= 18){
            temp = random.nextInt(11)+ 19;//19 -30
            hum = random.nextInt(21)+ 30;//30-50%
            //rainfall = random.nextInt(5);
        }
        else if(time > 18 && time<= 23){
            temp = random.nextInt(6)+ 12;//12 -18
            hum = random.nextInt(21)+ 50;//50-70%
            //rainfall = random.nextInt(15);
        }

        int[] res = new int[5];
        res[0] = temp;
        res[1] = hum - (temp/2);
        res[2] = rainfall;
        res[3] = wind;
        res[4] = airpressure;

        return res;
    }

    static String makeJsonTemp(int temp, LocalDateTime t){

        String message;
        JSONObject json = new JSONObject();
        json.put("temp",temp);
        json.put("time", t.toString());
        json.put("id_ws", "1");
        json.put("type", "2");

        message = json.toString();
        return message;
    }
    static String makeJsonWind( int windInt, String dir, LocalDateTime t){

        String message;
        JSONObject json = new JSONObject();

        json.put("dir",dir);
        json.put("int",windInt);
        json.put("time", t.toString());
        json.put("id_ws", "1");
        json.put("type", "1");

        message = json.toString();
        return message;
    }
    static String makeJsonAir( int hum, int press, LocalDateTime t){

        String message;
        JSONObject json = new JSONObject();

        json.put("hum",hum);
        json.put("press",press);
        json.put("time", t.toString());
        json.put("id_ws", "1");
        json.put("type", "3");

        message = json.toString();
        return message;
    }

    static String makeJsonRain(LocalDateTime t){

        String message;
        JSONObject json = new JSONObject();

        Random random = new Random();

        double rain = random.nextInt(3) / 10.0 ;

        json.put("rain",rain);
        json.put("time", t.toString());
        json.put("id_ws", "1");
        json.put("type", "4");

        message = json.toString();
        return message;
    }

    static String getWindDir(){

        Random r  = new Random();
        int i = r.nextInt(8)+1;
        String x = "";

        switch (i) {
            case 1:
                x= "w";
                break;
            case 2:
                x= "sw";
                break;
            case 3:
                x=  "s";
                break;
            case 4:
                x=  "se";
                break;
            case 5:
                x= "e";
                break;
            case 6:
                x=  "ne";
                break;
            case 7:
                x=  "n";
                break;
            case 8:
                x=  "nw";
                break;
        }

        return x;
    }
}
