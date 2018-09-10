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
    private StringBuffer history_go = new StringBuffer();
    private int count = 0;
    public void run() {
        try {

            WebSocketFactory factory = new WebSocketFactory();
            WebSocket ws = factory.createSocket(WSS, 500);
            ws.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket webSocket, String message) {
                    char[] arr = message.toCharArray();
                    for (int i = 0; i < 30; ++i) {
                        arr[i] = 0;
                    }
                    arr[arr.length - 1] = 0;
                    arr[arr.length - 2] = 0;

                    for (int i = 30; i < arr.length - 1; ++i) {
                        if (arr[i] == '\\' && (arr[i + 1] == '\\' || arr[i + 1] == '\"')) arr[i] = 0;
                    }
//                    char[] arr1 = new char[arr.length - 32];
//                    int j = 0, i = 30;
//                    while (i < arr.length - 2) {
//                        if (arr[i] == '\\' && (arr[i + 1] == '\\' || arr[i + 1] == '\"')) {
//                            ++i;
//                        } else {
//                            arr1[j++] = arr[i++];
//                        }
//                    }
                    Gson gson = new Gson();
                    Item item = gson.fromJson(new String(arr).replaceAll(" ", ""), Item.class);
                    System.out.println(item);

                }
            });
            ws.connect();
            ws.sendText("newitems_go");
            for (int i = 0; i < 1; i++) {
                ws.sendPing();
                Thread.sleep(30000);
                System.out.println(count);
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("D:\\newitems_go.txt"));
            for (String line: history_go.toString().split(",")) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();
            bufferedWriter.close();
            ws.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
