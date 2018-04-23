import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
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
    public static void main(String[] args) throws MqttException {

        MqttClient client = new MqttClient("tcp://localhost:1883",MqttClient.generateClientId());
        client.connect();
        MqttMessage message = new MqttMessage();
        message.setPayload("blba".getBytes());
        client.publish("iot_data",message);
        client.disconnect();

        for (int i =0; i<100;i++){
            System.out.println("Temp: "+getValuesInitial()[0]+
                    "°C, Hum:"+getValuesInitial()[1] + "%, Rainfall: " + getValuesInitial()[2] + "%, Wind: "
            + getValuesInitial()[3]+"km/h");
        }
        /*int[] a = test(10,30,3,2);
        for (int i = 0; i < 3; i++) {
            System.out.println(a[i]);
        }*/
    }

   static public int[] getValuesInitial(){

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
    static int[] test(int min, int max, int cnt, int var){

        Random r = new Random();
        int b = r.nextInt(max)+min;
        int[] a = new int[cnt];

        for (int i = 0; i < cnt ; i++) {
             b += r.nextInt(var)+min;
            a[i] = b;
        }
        return a;
    }

    static String makeJsonTemp(int temp){

        String message;
        JSONObject json = new JSONObject();
        json.put("type", "2");
        json.put("id_ws", "001");
        json.put("time:", Timestamp.valueOf(LocalDateTime.now()).toString());
        json.put("temp",temp);

        message = json.toString();
        return message;
    }
    static String makeJsonWind( int windInt, String dir  ){

        String message;
        JSONObject json = new JSONObject();
        json.put("type", "1");
        json.put("id_ws", "001");
        json.put("time:", Timestamp.valueOf(LocalDateTime.now()).toString());
        json.put("int",windInt);
        json.put("dir",dir);

        message = json.toString();
        return message;
    }
    /*static String makeJsonAir( int hum, String press  ){

        String message;
        JSONObject json = new JSONObject();
        json.put("type", "1");
        json.put("id_ws", "001");
        json.put("time:", Timestamp.valueOf(LocalDateTime.now()).toString());
        json.put("int",windInt);
        json.put("dir",dir);

        message = json.toString();
        return message;
    }*/
    /*static public String getTemp(double temp){

        int time = LocalDateTime.now().getHour();
        int deg = 3;
        Random random = new Random();
        if ( time <= 6 ){
            temp = random.nextInt(deg)+ temp;//9-17
        }
        else if(time > 6 && time<= 12){
            temp = random.nextInt(deg)+ temp;//12 -18
        }
        else if(time > 12 && time<= 18){
            temp = random.nextInt(deg)+ temp;//19 -30
        }
        else if(time > 18 && time<= 23){
            temp = random.nextInt(deg)+ temp;//12 -18
        }
        return Double.toString(temp);
    }*/


}
