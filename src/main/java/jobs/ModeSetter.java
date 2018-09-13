package jobs;

import db.FeedService;
import db.HistoryStatsService;
import javafx.util.Pair;
import pojo.PriceTime;

import java.sql.SQLException;
import java.util.List;

public class ModeSetter implements Runnable {

    private static final int MIN_VALID_HISTORY_SIZE = 500;  // <= 500
    private static final long MIN_VALID_HISTORY_OLD = 0;
    private static final long MAX_VALID_HISTORY_OLD = 3888000000L; // 1 Month in milliseconds.
    private static final long MIN_VALID_AV_PRICE = 500; // 5 рублей
    private static final long MAX_VALID_AV_PRICE = 100000;  // 1000 рублей

    private FeedService feedService;
    private HistoryStatsService historyStatsService;
    private PriceSetter priceSetter;

    public ModeSetter() {
        this.feedService = new FeedService();
        this.historyStatsService = new HistoryStatsService();
        this.priceSetter = new PriceSetter();
    }

    public void run() {
        System.out.println("Mode setter started.");
        try {
            setAllMods();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Изменить мод у сущности
    public int setMode(Pair<Long, Long> key) throws SQLException {
        return changeMode(key);
    }

    // Изменить мод у всех сущнностей в Feed
    private void setAllMods() throws SQLException {
        while (true) {
            List<Pair<Long, Long>> keys = feedService.getAllFeedKeys();
            System.out.println("Mode setter found " + keys.size() + " keys. Start saving mods.");
            int iteration = 0;
            for (Pair<Long, Long> key :
                    keys) {
                changeMode(key);
                iteration++;
                if (iteration % 1000 == 0)
                    System.out.println("Mode setter set row number " + iteration);
            }
            System.out.println("All " + iteration + " mods have been set. Sleep for 30 minutes.");
            try {
                Thread.sleep(1000*60*30);    // 30 минут ждать между обновлениями
            }
            catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    public int changeMode(Pair<Long, Long> key) throws SQLException {
        if (key == null) {
            feedService.setUpModeOnDB(key.getKey(), key.getValue(), 9);
            priceSetter.removeKey(key);
            return 9;
        }

        List<PriceTime> history = historyStatsService.getHistory(key.getKey(), key.getValue());
        if (history == null) {
            feedService.setUpModeOnDB(key.getKey(), key.getValue(), 8);
            priceSetter.removeKey(key);
            return 8;
        }
        if (history.size() < MIN_VALID_HISTORY_SIZE) {
            feedService.setUpModeOnDB(key.getKey(), key.getValue(), 7);
            priceSetter.removeKey(key);
            return 7;
        }
        long firstSell = System.currentTimeMillis();
        long minPrice = 100000000;
        long maxPrice = 0;
        long avPrice = 0;
        for (PriceTime priceTime :
                history) {
            avPrice += priceTime.getL_price();

            // TODO: Вероятно достаточно просто взять последний элемент листа, надо тестить!!
            if (priceTime.getL_time() < firstSell)
                firstSell = priceTime.getL_time();

            if (priceTime.getL_price() > maxPrice)
                maxPrice = priceTime.getL_price();

            if (priceTime.getL_price() < minPrice)
                minPrice = priceTime.getL_price();
        }
        avPrice /= history.size();

        if (System.currentTimeMillis() - firstSell * 1000 > MAX_VALID_HISTORY_OLD) {
            feedService.setUpModeOnDB(key.getKey(), key.getValue(), 6);
            priceSetter.removeKey(key);
            return 6;
        }
        if (avPrice > MAX_VALID_AV_PRICE) {
            feedService.setUpModeOnDB(key.getKey(), key.getValue(), 5);
            priceSetter.removeKey(key);
            return 5;
        }
        if (avPrice < MIN_VALID_AV_PRICE) {
            feedService.setUpModeOnDB(key.getKey(), key.getValue(), 4);
            priceSetter.removeKey(key);
            return 4;
        }

        // Хотим покупать
        feedService.setUpModeOnDB(key.getKey(), key.getValue(), 1);
        priceSetter.setGoodPrice(key, history);
        return 1;
    }
}
