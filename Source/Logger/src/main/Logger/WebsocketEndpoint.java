import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;


@ServerEndpoint("/weatherstation")
public class WebsocketEndpoint {

    public static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    public static Map<Session, Integer> register = new HashMap<Session, Integer>();
    public static int ws_id = 0;

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println(session.getId() + " has connected!");
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        register.remove(session);
        System.out.println(session.getId() + " has disconnected!");
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try{
            JSONObject o = new JSONObject(message);
            if(o.getInt("type") == 10){
                ws_id = Integer.parseInt(o.getString("ws_id"));
                register.put(session, ws_id);
                System.out.println(session.getId() + " has registered to ws_id " + ws_id);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println("Fehler!");
        }
    }
}
