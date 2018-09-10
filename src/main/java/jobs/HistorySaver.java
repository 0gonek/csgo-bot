package jobs;

import com.google.gson.Gson;
import db.ConstsService;
import db.FeedService;
import db.HistoryStatsService;
import javafx.util.Pair;
import pojo.ItemHistory;
import resources.Props;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import static resources.Props.KEY;


public class HistorySaver implements Runnable {

    private static final String URL_PREFIX = "https://market.csgo.com/api/";


    private static final long MIN_PAUSE_BETWEEN_REQUESTS = 300;

    private FeedService feedService;
    private HistoryStatsService historyStatsService;
    private ConstsService constsService;

    public HistorySaver() {
        this.feedService = new FeedService();
        this.historyStatsService = new HistoryStatsService();
        this.constsService = new ConstsService();
    }

    private List<Pair<Long, Long>> keys;
    private int iteration;
    private int globalIteration = 0;
    private long lastRequestTime = 0;

    public ItemHistory getItemHistory(long classid, long instanceid) throws SQLException {
        ItemHistory itemHistory = downloadItemHistory(classid, instanceid);
        historyStatsService.deleteOldHistory(classid, instanceid);
        historyStatsService.deleteOldStats(classid, instanceid);
        historyStatsService.saveItemHistoryAndStats(itemHistory, classid, instanceid);
        return itemHistory;
    }

    public void run() {
        System.out.println("History saver started.");
        while (true) {
            try {
                keys = feedService.getAllFeedKeys();
                iteration = 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
                continue;
            }

            for (Pair<Long, Long> entityId :
                    keys) {
                if (entityId == null || entityId.getKey() == null || entityId.getValue() == null) {
                    System.out.println("Bad entity key: " + entityId.getKey() + " : " + entityId.getValue());
                    continue;
                }
                while (System.currentTimeMillis() - lastRequestTime < MIN_PAUSE_BETWEEN_REQUESTS) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                ItemHistory itemHistory = downloadItemHistory(entityId.getKey(), entityId.getValue());
                lastRequestTime = System.currentTimeMillis();
                try {
                    historyStatsService.deleteOldHistory(entityId.getKey(), entityId.getValue());
                    historyStatsService.deleteOldStats(entityId.getKey(), entityId.getValue());
                    historyStatsService.saveItemHistoryAndStats(itemHistory, entityId.getKey(), entityId.getValue());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                iteration++;
                if (iteration % 500 == 0)
                    System.out.println("History saver iteration = " + iteration);
            }
            try {
                if (constsService.getConst(Props.HISTORY_LAST_UPDATE_TIME) != null) {
                    constsService.updateConst(Props.HISTORY_LAST_UPDATE_TIME, System.currentTimeMillis(), null);
                }
                else {
                    constsService.addConst(Props.HISTORY_LAST_UPDATE_TIME, System.currentTimeMillis(), null);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            globalIteration++;
            System.out.println("Global iteration " + globalIteration + " of story saving have been done.");
        }
    }

    private ItemHistory downloadItemHistory(long classid, long instanceid) {
        String url = URL_PREFIX + "ItemHistory/" + classid + "_" + instanceid + "/?key=" + KEY;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            try {
                ItemHistory itemHistory = gson.fromJson(response.toString(), ItemHistory.class);
                return itemHistory;
            } catch (NullPointerException ex) {
                return null;
            }
        } catch (IOException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
