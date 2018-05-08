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


        //nur um werte zu checken
        for (int i =0; i<100;i++){
            System.out.println("Temp: "+getValuesInitial()[0]+
                    "°C, Hum:"+getValuesInitial()[1] + "%, Rainfall: " + getValuesInitial()[2] + "%, Wind: "
                    + getValuesInitial()[3]+"km/h ");
        }

        try {
            while (true) {
                int[] a = getValuesInitial();

                MqttMessage messageTemp = new MqttMessage();
                messageTemp.setPayload(makeJsonTemp(a[0]).getBytes());
                client.publish("iot_data",messageTemp);

                MqttMessage messageWind = new MqttMessage();
                messageWind.setPayload(makeJsonWind(a[3],getWindDir()).getBytes());
                client.publish("iot_data",messageWind);

                MqttMessage messageAir = new MqttMessage();
                messageAir.setPayload(makeJsonAir(a[1],a[4]).getBytes());
                client.publish("iot_data", messageAir);

                /*MqttMessage message = new MqttMessage();
                message.setPayload("-------------".getBytes());
                client.publish("iot_data",message); Nur um beim reciven den überblick nicht zu verlieren*/


                Thread.sleep(5 * 1000);
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        client.disconnect();
        /*int[] a = test(10,30,3,2);
        for (int i = 0; i < 3; i++) {
            System.out.println(a[i]);
        }*/
    }

    static public int[] getValuesInitial(){

        int temp = 0;
        int minTemp=0;
        int hum = 0;
         int minHum;
        int rainfall = 0;
        int wind;
        int airpressure;
        int month = LocalDateTime.now().getMonth().getValue();

        switch (month) {
            case 1:
                minTemp = -4;
                break;
            case 2:
                minTemp = -3;
                break;
            case 3:
                minTemp = 1;
                break;
            case 4:
                minTemp = 5;
                break;
            case 5:
                minTemp = 10;
                break;
            case 6:
                minTemp = 12;
                break;
            case 7:
                minTemp = 14;
                break;
            case 8:
                minTemp = 14;
                break;
            case 9:
                minTemp = 14;
                break;
            case 10:
                minTemp = 10;
                break;
            case 11:
                minTemp = 6;
                break;
            case 12:
                minTemp = 1;
                break;
        }
        int time = LocalDateTime.now().getHour();
        Random random = new Random();

        wind = random.nextInt(2)+10;
        airpressure = random.nextInt(3)+ 1013;

        if ( time <= 6 ){
            temp = random.nextInt(9)+ minTemp;//9-17
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
    static int[] riseTemp(int min, int max, int cnt, int var){

        Random r = new Random();
        int b = r.nextInt(max)+min;
        int[] a = new int[cnt];

        for (int i = 0; i < cnt ; i++) {
            b += r.nextInt(var)+min;
            a[i] = b;
        }
        return a;
    }

    static int[] lowerTemp(int min, int max, int cnt, int var){

        Random r = new Random();
        int b = r.nextInt(max) + min;
        int[] a = new int [cnt];

        for (int i = 0; i < cnt; i++) {
            b -= r.nextInt(var)+min;
            a[i] = b;
        }
        return a;
    }

    static String makeJsonTemp(int temp){

        String message;
        JSONObject json = new JSONObject();
        json.put("temp",temp);
        json.put("time:", Timestamp.valueOf(LocalDateTime.now()).toString());
        json.put("id_ws", "001");
        json.put("type", "2");

        message = json.toString();
        return message;
    }
    static String makeJsonWind( int windInt, String dir  ){

        String message;
        JSONObject json = new JSONObject();

        json.put("dir",dir);
        json.put("int",windInt);
        json.put("time:", Timestamp.valueOf(LocalDateTime.now()).toString());
        json.put("id_ws", "001");
        json.put("type", "1");

        message = json.toString();
        return message;
    }
    static String makeJsonAir( int hum, int press  ){

        String message;
        JSONObject json = new JSONObject();

        json.put("hum",hum);
        json.put("press",press);
        json.put("time:", Timestamp.valueOf(LocalDateTime.now()).toString());
        json.put("id_ws", "001");
        json.put("type", "3");

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
