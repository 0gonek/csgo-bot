package db;

import javafx.util.Pair;
import resources.Props;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FeedService {

    private static final String CSV_TO_FEED =
            //language=sql
            "COPY FEED(c_classid,c_instanceid,c_price,c_offers,c_popularity,c_rarity," +
                    "c_quality,c_heroid,c_slot,c_stickers,c_market_name,c_market_name_en," +
                    "c_market_hash_name,c_name_color,c_price_updated,c_pop,c_base_id) \n" +
                    "FROM '" + Props.CSV_FILE + "' WITH  DELIMITER ';' CSV HEADER";

    private final Connection connection;

    public FeedService() {
            this.connection = DBUtils.createConnection();
    }

    public int getFeedRowCount() throws SQLException {
        return DBUtils.getFeedRowCount(connection);
    }

    public void uploadCsvToFeed() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CSV_TO_FEED);
        System.out.println("CSV uploaded successfully.");
    }

    public List<Pair<Long, Long>> getAllFeedKeys() throws SQLException {
        Statement statement = connection.createStatement();
        String query =
                //language=sql
                "SELECT c_classid, c_instanceid\n" +
                        "FROM feed ORDER BY id" + ";";
        ResultSet resultSet = statement.executeQuery(query);
        List<Pair<Long, Long>> keys = new ArrayList<Pair<Long, Long>>();
        while (resultSet.next()) {
            keys.add(new Pair<Long, Long>(resultSet.getLong("c_classid"), resultSet.getLong("c_instanceid")));
        }
        return keys;
    }

    public void setUpModeOnDB(Long c_classid, Long c_instanceid, int mode) throws SQLException {
        Statement statement = connection.createStatement();
        String query =
                //language=sql
                "UPDATE feed \n" +
                        "SET mode = " + mode +
                        "WHERE c_classid = " + c_classid + " AND c_instanceid =" + c_instanceid + ";";
        statement.executeUpdate(query);
    }

}

