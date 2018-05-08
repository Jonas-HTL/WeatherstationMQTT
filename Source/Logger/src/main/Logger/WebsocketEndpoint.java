import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/chat")
public class WebsocketEndpoint {

    public static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println(session.getId() + "Has connected!");
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println(session.getId() + "Has disconnected!");
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message) {
        for (Session session : sessions) {
            session.getAsyncRemote().sendText(message);
        }
    }
}
