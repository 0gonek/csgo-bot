package main;

import db.FeedService;

import java.sql.SQLException;

public class Worker {
    public static void main(String[] args) throws SQLException {
        FeedService feedService = new FeedService();
        feedService.reset();
    }
}
