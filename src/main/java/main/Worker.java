package main;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import jobs.HistorySaver;
import pojo.Item;
import websockets.ServerListener;

import java.io.IOException;
import java.sql.SQLException;

import static resources.Props.WSS;

public class Worker {
    public static void main(String[] args) throws SQLException {
//        FeedService feedService = new FeedService();
//        feedService.reset();
//        feedService.dropFeedTable();
//        feedService.uploadCsvToFeed();
//        Thread tr = new Thread(new UpdateDB());
//        tr.start();
//        Thread historySaver = new Thread(new HistorySaver());
//        historySaver.start();

//        Thread tr = new Thread(new ServerListener());
//        tr.start();

//        Thread modeSetter = new Thread(new ModeSetter());
//        modeSetter.start();
//        feedService.reset();

    }
}
