package websockets;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import pojo.Item;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static resources.Props.WSS;

public class ServerListener implements Runnable{

    public void run() {
        try {

            WebSocketFactory factory = new WebSocketFactory();
            WebSocket ws = factory.createSocket(WSS);
            ws.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket webSocket, String message) {
                    char c = '#';
                    char[] arr = message.toCharArray();
                    for (int i = 0; i < 30; ++i) {
                        arr[i] = c;
                    }
                    arr[arr.length - 1] = c;
                    arr[arr.length - 2] = c;

                    for (int i = 30; i < arr.length - 1; ++i) {
                        if (arr[i] == '\\' && (arr[i + 1] == '\\' || arr[i + 1] == '\"')) arr[i] = c;
                    }

                    Gson gson = new Gson();
                    Item item = gson.fromJson(new String(arr).replaceAll(c+"", ""), Item.class);

                    //todo:Написать обработку
                    System.out.println(item);

                }
            });
            ws.setPingInterval(40 * 1000);
            ws.connect();
            ws.sendText("newitems_go");
//            ws.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }
}
