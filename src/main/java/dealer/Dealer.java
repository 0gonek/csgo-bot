package dealer;

import db.FeedService;

import java.sql.SQLException;

public class Dealer implements Runnable {

    private FeedService feedService = new FeedService();

    private int i, rowsCount;

    public Dealer() {
        this.i = 0;
        this.rowsCount = 0;
    }

    public void run() {
        while (true) {
            selectItemId();
            getItemHistory();
            getBestSellOffer();
            decideAndBuy();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void selectItemId() {
        if (i < rowsCount)
            i++;
        else {
            i = 0;
            try {
                rowsCount = feedService.getFeedRowCount();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void getItemHistory() {
    }

    private void getBestSellOffer() {
    }

    private void decideAndBuy() {
    }
}
