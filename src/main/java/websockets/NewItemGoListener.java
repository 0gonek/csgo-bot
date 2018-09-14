package websockets;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import db.BuyHistoryService;
import pojo.Item;

import java.io.IOException;
import java.sql.SQLException;

import static cashe.GoodPriceCasheService.getGoodPriceCash;
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

                //Проверяем в кэше котим ли мы покупать этот предмет
                Item item = new Gson().fromJson(new String(arr),Item.class);
                Long cashPrice = getGoodPriceCash.get(item.getPair());
                item.setW_price((double) cashPrice);
                if (item.getW_price() >= item.getUi_price()) {
                    try {
                        BuyHistoryService buy = new BuyHistoryService();
                        buy.insert(item);
                        buy.closeConnection();
                        System.out.println(item + "хорошая цена");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println(item + "плохая цена");
                }
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
