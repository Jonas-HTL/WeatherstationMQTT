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
        System.out.println(session.getId() + " Has connected!");
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        register.remove(session);
        System.out.println(session.getId() + " Has disconnected!");
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
                ws_id = o.getInt("ws_id");
                register.put(session, ws_id);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println("Fehler!");
        }
    }
}
