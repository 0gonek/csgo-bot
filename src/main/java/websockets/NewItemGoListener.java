package websockets;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import pojo.Item;

import java.io.IOException;

import static resources.Props.WSS;

public class NewItemGoListener {

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

                //Извлечение json из поля data
                char[] arr = new char[message.length() - 32]; //30 число символов до нужной { + 2 лишние символа в конце
                int j = 0;
                for (int i = 30; i < message.length() - 2; ++i) {
                    if (message.charAt(i) == '\\' && (message.charAt(i + 1) == '\\' || message.charAt(i + 1) == '\"')) {
                        continue;
                    } else {

                    }
                    arr[j++] = message.charAt(i);


                }

                while (j < arr.length) {
                    arr[j++] = ' ';
                }

                //Извлекли
                //todo написать обработку item
//                System.out.println(new Gson().fromJson(message, Item.class));
                System.out.println(new String(arr));
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
