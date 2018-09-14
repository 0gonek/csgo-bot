package websockets;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import db.BuyHistoryService;
import jobs.Buyer;
import pojo.Item;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import static cashe.GoodPriceCasheService.getGoodPriceCash;
import static resources.Props.WSS;

public class NewItemGoListener {
    public static AtomicInteger countOfThreads = new AtomicInteger(0);
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
                if (countOfThreads.get() > 250) return;

                countOfThreads.incrementAndGet();
                Thread th = new Thread(new Buyer(message));
                th.setPriority(6);
                th.start();

            }
        });
    }

    public void connect() {
        try {
            newItemGo.setPingInterval(40 * 1000);
            newItemGo.connect();
            newItemGo.sendText("newitems_go");

        } catch (WebSocketException e) {
            //todo переписать
            e.printStackTrace();
        }
    }

    public void disconnect() {
        newItemGo.disconnect();
    }


}
