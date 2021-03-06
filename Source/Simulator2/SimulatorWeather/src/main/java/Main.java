import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

public class Main {


    private static MqttClient client;

    public static void main(String[] args) throws MqttException, InterruptedException {
        client = new MqttClient("tcp://localhost:1883",MqttClient.generateClientId());
        client.connect();

        //initDB();
        //Thread.sleep(300 * 1000);
        try {
            while (true) {
                int[] a = getValuesForDate();
                LocalDateTime t = LocalDateTime.now();

                MqttMessage messageTemp = new MqttMessage();
                messageTemp.setPayload(makeJsonTemp(a[0], t).getBytes());
                client.publish("p4/1",messageTemp);

                MqttMessage messageWind = new MqttMessage();
                messageWind.setPayload(makeJsonWind(a[3],getWindDir(), t).getBytes());
                client.publish("p4/1",messageWind);

                MqttMessage messageAir = new MqttMessage();
                messageAir.setPayload(makeJsonAir(a[1],a[4], t).getBytes());
                client.publish("p4/1", messageAir);

                MqttMessage messageRain = new MqttMessage();
                messageRain.setPayload(makeJsonRain(t).getBytes());
                client.publish("p4/1", messageRain);

                System.out.println();

                Thread.sleep(2 * 1000 + 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.disconnect();
    }

    public static void initDB(){
        //2016-07-15 07:01:00
        DateTime dateTime = new DateTime(2014, 1, 1, 0, 1, 0);
        try {
            while (dateTime.year().get() < 2017) {
                LocalDateTime t = LocalDateTime.parse(dateTime.toLocalDateTime().toString());
                int[] a = getValuesInitialDBINIT(t);

                MqttMessage messageTemp = new MqttMessage();
                messageTemp.setPayload(makeJsonTemp(a[0], t).getBytes());
                client.publish("p4/1",messageTemp);

                MqttMessage messageWind = new MqttMessage();
                messageWind.setPayload(makeJsonWind(a[3],getWindDir(), t).getBytes());
                client.publish("p4/1",messageWind);

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
            temp = random.nextInt(5)+ 17;//17-22
            hum = random.nextInt(21)+70;//70-90%
            //rainfall = random.nextInt(20);

        }
        else if(time > 6 && time<= 12){
            temp = random.nextInt(6)+ 20;//20 -26
            hum = random.nextInt(28)+ 53;//53-80%
            //rainfall=random.nextInt(10);
        }
        else if(time > 12 && time<= 18){
            temp = random.nextInt(6)+ 24;//24 -30
            hum = random.nextInt(21)+ 30;//30-50%
            //rainfall = random.nextInt(5);
        }
        else if(time > 18 && time<= 23){
            temp = random.nextInt(4)+ 20;//20-24
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
            temp = random.nextInt(5)+ 17;//17-22
            hum = random.nextInt(21)+70;//70-90%
            //rainfall = random.nextInt(20);

        }
        else if(time > 6 && time<= 12){
            temp = random.nextInt(6)+ 20;//20 -26
            hum = random.nextInt(28)+ 53;//53-80%
            //rainfall=random.nextInt(10);
        }
        else if(time > 12 && time<= 18){
            temp = random.nextInt(6)+ 24;//24 -30
            hum = random.nextInt(21)+ 30;//30-50%
            //rainfall = random.nextInt(5);
        }
        else if(time > 18 && time<= 23){
            temp = random.nextInt(4)+ 20;//20-24
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

        json.put("amount",rain);
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
    static public int[] getValuesForDate(){

        int temp = 0;
        int minTemp=0;
        int maxTemp=0;
        int hum = 0;
        int minHum;
        int rainfall = 0;
        int wind;
        int airpressure;
        int month = LocalDateTime.now().getMonth().getValue();

        int time = LocalDateTime.now().getHour();
        Random random = new Random();

        wind = random.nextInt(2)+10;
        airpressure = random.nextInt(3)+ 1013;

        switch (month) {
            case 1:
                minTemp = -5;
                maxTemp= 3;

                if ( time <= 6 ){
                    temp = random.nextInt(4)+ minTemp;
                    hum = random.nextInt(21)+70;
                    //rainfall = random.nextInt(20);

                }
                else if(time > 6 && time<= 12){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(28)+ 53;
                    //rainfall=random.nextInt(10);

                }
                else if(time > 12 && time<= 18){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(21)+ 30;
                    //rainfall = random.nextInt(5);
                }
                else if(time > 18 && time<= 23){
                    temp = random.nextInt(3)+ minTemp;
                    hum = random.nextInt(21)+ 50;
                    //rainfall = random.nextInt(15);
                }
                break;
            case 2:
                minTemp = -3;
                maxTemp = 4;
                if ( time <= 6 ){
                    temp = random.nextInt(2)+ minTemp;
                    hum = random.nextInt(21)+70;
                    //rainfall = random.nextInt(20);

                }
                else if(time > 6 && time<= 12){
                    temp = random.nextInt(4)+ minTemp;
                    hum = random.nextInt(28)+ 53;
                    //rainfall=random.nextInt(10);

                }
                else if(time > 12 && time<= 18){
                    temp = random.nextInt(5)+ minTemp;
                    hum = random.nextInt(21)+ 30;
                    //rainfall = random.nextInt(5);
                }
                else if(time > 18 && time<= 23){
                    temp = random.nextInt(3)+ minTemp;
                    hum = random.nextInt(21)+ 50;
                    //rainfall = random.nextInt(15);
                }
                break;
            case 3:
                minTemp = 1;
                maxTemp = 10;
                if ( time <= 6 ){
                    temp = random.nextInt(2)+ minTemp;
                    hum = random.nextInt(21)+70;
                    //rainfall = random.nextInt(20);

                }
                else if(time > 6 && time<= 12){
                    temp = random.nextInt(3)+ minTemp;
                    hum = random.nextInt(28)+ 53;
                    //rainfall=random.nextInt(10);

                }
                else if(time > 12 && time<= 18){
                    temp = random.nextInt(4)+ minTemp;
                    hum = random.nextInt(21)+ 30;
                    //rainfall = random.nextInt(5);
                }
                else if(time > 18 && time<= 23){
                    temp = random.nextInt(2)+ minTemp;
                    hum = random.nextInt(21)+ 50;
                    //rainfall = random.nextInt(15);
                }
                break;
            case 4:
                minTemp = 5;
                maxTemp = 15;

                if ( time <= 6 ){
                    temp = random.nextInt(2)+ minTemp;
                    hum = random.nextInt(21)+70;
                    //rainfall = random.nextInt(20);

                }
                else if(time > 6 && time<= 12){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(28)+ 53;
                    //rainfall=random.nextInt(10);

                }
                else if(time > 12 && time<= 18){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(21)+ 30;
                    //rainfall = random.nextInt(5);
                }
                else if(time > 18 && time<= 23){
                    temp = random.nextInt(3)+ minTemp;
                    hum = random.nextInt(21)+ 50;
                    //rainfall = random.nextInt(15);
                }
                break;
            case 5:
                minTemp = 13;
                maxTemp = 25;

                if ( time <= 6 ){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(21)+70;
                    //rainfall = random.nextInt(20);

                }
                else if(time > 6 && time<= 12){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(28)+ 53;
                    //rainfall=random.nextInt(10);

                }
                else if(time > 12 && time<= 18){
                    temp = maxTemp - random.nextInt(5);
                    hum = random.nextInt(21)+ 30;
                    //rainfall = random.nextInt(5);
                }
                else if(time > 18 && time<= 23){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(21)+ 50;
                    //rainfall = random.nextInt(15);
                }
                break;
            case 6:
                minTemp = 12;
                maxTemp = 27;

                if ( time <= 6 ){
                    temp = random.nextInt(9)+ minTemp;
                    hum = random.nextInt(21)+70;
                    //rainfall = random.nextInt(20);

                }
                else if(time > 6 && time<= 12){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(28)+ 53;
                    //rainfall=random.nextInt(10);

                }
                else if(time > 12 && time<= 18){
                    temp = random.nextInt(11)+ minTemp;
                    hum = random.nextInt(21)+ 30;
                    //rainfall = random.nextInt(5);
                }
                else if(time > 18 && time<= 23){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(21)+ 50;
                    //rainfall = random.nextInt(15);
                }
                break;
            case 7:
                minTemp = 14;
                maxTemp = 30;

                if ( time <= 6 ){
                    temp = random.nextInt(9)+ minTemp;
                    hum = random.nextInt(21)+70;
                    //rainfall = random.nextInt(20);

                }
                else if(time > 6 && time<= 12){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(28)+ 53;
                    //rainfall=random.nextInt(10);

                }
                else if(time > 12 && time<= 18){
                    temp = random.nextInt(11)+ minTemp;
                    hum = random.nextInt(21)+ 30;
                    //rainfall = random.nextInt(5);
                }
                else if(time > 18 && time<= 23){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(21)+ 50;
                    //rainfall = random.nextInt(15);
                }
                break;
            case 8:
                minTemp = 14;
                maxTemp = 26;

                if ( time <= 6 ){
                    temp = random.nextInt(9)+ minTemp;
                    hum = random.nextInt(21)+70;
                    //rainfall = random.nextInt(20);

                }
                else if(time > 6 && time<= 12){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(28)+ 53;
                    //rainfall=random.nextInt(10);

                }
                else if(time > 12 && time<= 18){
                    temp = random.nextInt(11)+ minTemp;
                    hum = random.nextInt(21)+ 30;
                    //rainfall = random.nextInt(5);
                }
                else if(time > 18 && time<= 23){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(21)+ 50;
                    //rainfall = random.nextInt(15);
                }
                break;
            case 9:
                minTemp = 14;
                maxTemp = 23;

                if ( time <= 6 ){
                    temp = random.nextInt(9)+ minTemp;
                    hum = random.nextInt(21)+70;
                    //rainfall = random.nextInt(20);

                }
                else if(time > 6 && time<= 12){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(28)+ 53;
                    //rainfall=random.nextInt(10);

                }
                else if(time > 12 && time<= 18){
                    temp = random.nextInt(11)+ minTemp;
                    hum = random.nextInt(21)+ 30;
                    //rainfall = random.nextInt(5);
                }
                else if(time > 18 && time<= 23){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(21)+ 50;
                    //rainfall = random.nextInt(15);
                }
                break;
            case 10:
                minTemp = 10;
                maxTemp = 20;

                if ( time <= 6 ){
                    temp = random.nextInt(9)+ minTemp;
                    hum = random.nextInt(21)+70;
                    //rainfall = random.nextInt(20);

                }
                else if(time > 6 && time<= 12){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(28)+ 53;
                    //rainfall=random.nextInt(10);

                }
                else if(time > 12 && time<= 18){
                    temp = random.nextInt(11)+ minTemp;
                    hum = random.nextInt(21)+ 30;
                    //rainfall = random.nextInt(5);
                }
                else if(time > 18 && time<= 23){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(21)+ 50;
                    //rainfall = random.nextInt(15);
                }
                break;
            case 11:
                minTemp = 6;
                maxTemp = 8;

                if ( time <= 6 ){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(21)+70;
                    //rainfall = random.nextInt(20);

                }
                else if(time > 6 && time<= 12){
                    temp = random.nextInt(9)+ minTemp;
                    hum = random.nextInt(28)+ 53;
                    //rainfall=random.nextInt(10);

                }
                else if(time > 12 && time<= 18){
                    temp = random.nextInt(11)+ minTemp;
                    hum = random.nextInt(21)+ 30;
                    //rainfall = random.nextInt(5);
                }
                else if(time > 18 && time<= 23){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(21)+ 50;
                    //rainfall = random.nextInt(15);
                }
                break;
            case 12:
                minTemp = 1;
                maxTemp= -2;
                if ( time <= 6 ){
                    temp = random.nextInt(2)+ minTemp;
                    hum = random.nextInt(21)+20;
                    //rainfall = random.nextInt(20);

                }
                else if(time > 6 && time<= 12){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(28)+ 53;
                    //rainfall=random.nextInt(10);

                }
                else if(time > 12 && time<= 18){
                    temp = random.nextInt(11)+ minTemp;
                    hum = random.nextInt(21)+ 30;
                    //rainfall = random.nextInt(5);
                }
                else if(time > 18 && time<= 23){
                    temp = random.nextInt(6)+ minTemp;
                    hum = random.nextInt(21)+ 50;
                    //rainfall = random.nextInt(15);
                }
                break;
        }


        int[] res = new int[5];
        res[0] = temp;
        res[1] = hum - (temp/2);
        res[2] = rainfall;
        res[3] = wind;
        res[4] = airpressure;

        return res;
    }

}
