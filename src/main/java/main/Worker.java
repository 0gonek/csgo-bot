package main;

import db.BuyHistoryService;
import pojo.Item;
import cashe.GoodPriceCashe;
import db.DBUtils;
import db.FeedService;
import jobs.ModeSetter;
import websockets.NewItemGoListener;

import java.sql.SQLException;

public class Worker {
    public static void main(String[] args) throws SQLException {
//        FeedService feedService = new FeedService();
//        DBUtils dbUtils = new DBUtils();
//        dbUtils.reset();
//        feedService.dropFeedTable();
//        feedService.uploadCsvToFeed();
//        Thread tr = new Thread(new UpdateDBService());
//        tr.start();
//        Thread historySaver = new Thread(new HistorySaver());
//        historySaver.start();

//            NewItemGoListener nl = new NewItemGoListener();
//            nl.connect();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e ) {
//                e.printStackTrace();
//            }
//
//        BuyHistoryService buy = new BuyHistoryService();
//        buy.insert(new Item("1111", "1111", "111111", "11111", "111111", "1111111", 8, 9 ));
//        buy.closeConnection();
//            nl.disconnect();
//        Thread modeSetter = new Thread(new ModeSetter());
//        modeSetter.start();
//        feedService.reset();

    }
}
