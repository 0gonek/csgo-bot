package jobs;

import com.google.gson.Gson;
import db.FeedService;
import javafx.util.Pair;
import pojo.ItemHistory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class HistorySaver implements Runnable {

    private static final String URL_PREFIX = "https://market.csgo.com/api/";
    private static final String KEY = "cAhlC0P31b7Ywu7F819mZmT973DYlX9";

    private FeedService feedService = new FeedService();

    private List<Pair<Long, Long>> keys;
    private int iteration;
    private int globalIteration = 0;

    public void run() {
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
                ItemHistory itemHistory = getItemHistory(entityId.getKey(), entityId.getValue());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                try {
                    feedService.deleteOldHistory(entityId.getKey(), entityId.getValue());
                    feedService.deleteOldStats(entityId.getKey(), entityId.getValue());
                    feedService.saveItemHistoryAndStats(itemHistory, entityId.getKey(), entityId.getValue());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                iteration++;
                if (iteration % 500 == 0)
                    System.out.println("History saver iteration = " + iteration);
            }
            globalIteration++;
            System.out.println("Global iteration " + globalIteration + " of story saving have been done.");
        }
    }

    private ItemHistory getItemHistory(long classid, long instanceid) {
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
