package jobs;

import db.FeedService;

public class Seller implements Runnable {

    FeedService feedService;

    public Seller() {
        this.feedService = new FeedService();
    }

    public void run() {

    }
}
