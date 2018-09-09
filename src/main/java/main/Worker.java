package main;

import db.FeedService;
import jobs.HistorySaver;
import jobs.ModeSetter;

import java.sql.SQLException;

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

        Thread modeSetter = new Thread(new ModeSetter());
        modeSetter.start();
    }
}
