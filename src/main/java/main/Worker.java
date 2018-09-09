package main;

import dealer.HistorySaver;

import java.sql.SQLException;

public class Worker {
    public static void main(String[] args) throws SQLException {
//        FeedService feedService = new FeedService();
//        feedService.reset();
//        feedService.dropFeedTable();
        //feedService.uploadCsvToFeed();
//        Thread tr = new Thread(new UpdateDB());
//        tr.start();
        Thread thread = new Thread(new HistorySaver());
        thread.start();
    }
}
