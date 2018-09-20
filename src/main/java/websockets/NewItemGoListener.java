package websockets;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import jobs.Buyer;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import static resources.Props.WSS;

public class NewItemGoListener {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private WebSocket newItemGo;

    public NewItemGoListener() {
        try {
            WebSocketFactory factory = new WebSocketFactory();
            this.newItemGo = factory.createSocket(WSS);
            listenNewItemGo();
        } catch (IOException e) {
            //todo: переписать
            e.printStackTrace();
        }

    }

    private void listenNewItemGo() {
        newItemGo.addListener(new WebSocketAdapter() {
            @Override
            public void onTextMessage(WebSocket webSocket, String message) {
                executorService.execute(new Buyer(message));
            }
        });
    }

    public void connect() {
        try {
            newItemGo.setPingInterval(1000);
            newItemGo.connect();
            newItemGo.sendText("newitems_go");
            System.out.println("NewItemGoListener was connected");
        } catch (WebSocketException e) {
            //todo переписать
            e.printStackTrace();
            throw new RuntimeException("Не удалось установить соединение с сокетом");
        }
    }

    public void disconnect() {
        System.out.println("NewItemGoListener was disconnected");
        newItemGo.disconnect();
    }


}
