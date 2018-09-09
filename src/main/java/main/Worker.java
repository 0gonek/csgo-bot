package main;

import db.FeedService;
import db.UpdateDB;

import java.sql.SQLException;

public class Worker {
    public static void main(String[] args) throws SQLException {
//        FeedService feedService = new FeedService();
//        feedService.reset();
//        feedService.dropFeedTable();
//        feedService.uploadCsvToFeed();
        Thread tr = new Thread(new UpdateDB());
        tr.start();
    }
}
