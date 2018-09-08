package main;

import db.Feed;

import java.sql.SQLException;

public class Worker {
    public static void main(String[] args) throws SQLException {
        Feed feed = new Feed();
        feed.createFeedTable();
    }
}
