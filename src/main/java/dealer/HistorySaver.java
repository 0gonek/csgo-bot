package dealer;

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

public class HistorySaver implements Runnable {

    private static final String URL_PREFIX = "https://market.csgo.com/api/";
    private static final String KEY = "cAhlC0P31b7Ywu7F819mZmT973DYlX9";

    private FeedService feedService = new FeedService();

    private int i, rowsCount;
    private Pair<Long, Long> entityId = new Pair<Long, Long>(0L, 0L);

    public HistorySaver() {
        this.i = 0;
        this.rowsCount = 0;
    }

    public void run() {
        while (true) {
            selectItemId();
            try {
                entityId = feedService.getClassIdEntityIdByRowNum(i);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            ItemHistory itemHistory = getItemHistory(entityId.getKey(), entityId.getValue());
            try {
                feedService.deleteOldHistory(entityId.getKey(), entityId.getValue());
                feedService.deleteOldStats(entityId.getKey(), entityId.getValue());
                feedService.saveItemHistoryAndStats(itemHistory, entityId.getKey(), entityId.getValue());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
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

    private void selectItemId() {
        if (i < rowsCount) {
            i++;
            if (i % 1000 == 0)
                System.out.println("History saver downloading row number " + (i + 1));
        } else {
            System.out.println("History saver: i = " + i + " -> 0, Row count in feed = " + rowsCount);
            i = 0;
            try {
                rowsCount = feedService.getFeedRowCount();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
