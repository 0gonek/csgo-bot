package db;

import javafx.util.Pair;
import pojo.ItemHistory;
import pojo.PriceTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static resources.Props.CSV_FILE;

public class FeedService {

    private static final int MIN_VALID_HISTORY_SIZE = 500;  // <= 500
    private static final long MIN_VALID_HISTORY_OLD = 0;
    private static final long MAX_VALID_HISTORY_OLD = 4382905000L; // 1 Month in milliseconds.
    private static final long MIN_VALID_AV_PRICE = 500; // 5 рублей
    private static final long MAX_VALID_AV_PRICE = 100000;  // 1000 рублей

    private static final String CSV_TO_FEED =
            //language=sql
            "COPY FEED(c_classid,c_instanceid,c_price,c_offers,c_popularity,c_rarity," +
                    "c_quality,c_heroid,c_slot,c_stickers,c_market_name,c_market_name_en," +
                    "c_market_hash_name,c_name_color,c_price_updated,c_pop,c_base_id) \n" +
                    "FROM '" + CSV_FILE + "' WITH  DELIMITER ';' CSV HEADER";

    private final Connection connection;

    public FeedService() {
        try {
            this.connection = createConnection();
        } catch (SQLException ex) {
            throw new RuntimeException("Can't create connection to FeedService db.\n" + ex.getMessage());
        }
    }

    //TODO: Добавить удаление старых таблиц. С трай кечем - если таблиц не было, просто работать дальше.
    public void reset() throws SQLException {
        DBUtils.createFeedTable(connection);
        DBUtils.createHistoryTable(connection);
        DBUtils.createStatsTable(connection);
    }

    public void dropFeedTable() throws SQLException {
        DBUtils.dropFeedTable(connection);
    }

    public int getFeedRowCount() throws SQLException {
        return DBUtils.getFeedRowCount(connection);
    }

    public void uploadCsvToFeed() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CSV_TO_FEED);
        System.out.println("CSV uploaded successfully.");
    }

    public Pair<Long, Long> getClassIdEntityIdByRowNum(int i) throws SQLException {
        Statement statement = connection.createStatement();
        String query =
                "SELECT c_classid, c_instanceid FROM (\n" +
                        "                SELECT c_classid, c_instanceid, ROW_NUMBER () OVER ()\n" +
                        "                FROM Feed\n" +
                        "              ) x WHERE ROW_NUMBER = " + (i + 1) + ";";
        ResultSet resultSet = statement.executeQuery(query);
        if (!resultSet.next())
            return null;
        return new Pair<Long, Long>(resultSet.getLong("c_classid"), resultSet.getLong("c_instanceid"));
    }

    private List<PriceTime> getHistory(Long c_classid, Long c_instanceid) throws SQLException {
        Statement statement = connection.createStatement();
        String query =
                //language=sql
                "SELECT price, update_time\n" +
                        "FROM history\n" +
                        "WHERE c_classid = " + c_classid + " AND c_instanceid = " + c_instanceid + ";";
        ResultSet resultSet = statement.executeQuery(query);
        List<PriceTime> history = new ArrayList<PriceTime>();
        while (resultSet.next()) {
            history.add(new PriceTime(resultSet.getLong("price"), resultSet.getLong("update_time")));
        }
        return history;
    }

    public void deleteOldHistory(Long c_classid, Long c_instanceid) throws SQLException {
        Statement statement = connection.createStatement();
        String query =
                //language=sql
                "DELETE FROM history WHERE c_classid = " + c_classid + " AND c_instanceid = " + c_instanceid + ";";
        statement.executeUpdate(query);
    }

    public void deleteOldStats(Long c_classid, Long c_instanceid) throws SQLException {
        Statement statement = connection.createStatement();
        String query =
                //language=sql
                "DELETE FROM stats WHERE c_classid = " + c_classid + " AND c_instanceid = " + c_instanceid + ";";
        statement.executeUpdate(query);
    }

    public void saveItemHistoryAndStats(ItemHistory itemHistory, Long c_classid, Long c_instanceid) throws SQLException {
        if (itemHistory == null || itemHistory.getHistory() == null)
            return;
        for (PriceTime priceTime :
                itemHistory.getHistory()) {
            Statement statement = connection.createStatement();
            String query =
                    //language=sql
                    "INSERT INTO history (update_time, price, c_classid, c_instanceid)" +
                            " VALUES (" + priceTime.getL_time() +
                            ", " + priceTime.getL_price() +
                            ", " + c_classid +
                            ", " + c_instanceid +
                            ");";
            statement.executeUpdate(query);
        }
        Statement statement = connection.createStatement();
        String query =
                //language=sql
                "INSERT INTO stats (first_update_time, min, max, avg, num, c_classid, c_instanceid)" +
                        " VALUES (" + itemHistory.getHistory()[itemHistory.getNumber() - 1].getL_time() +
                        ", " + itemHistory.getMin() +
                        ", " + itemHistory.getMax() +
                        ", " + itemHistory.getAverage() +
                        ", " + itemHistory.getNumber() +
                        ", " + c_classid +
                        ", " + c_instanceid +
                        ");";
        statement.executeUpdate(query);
    }

    public int changeMode(Pair<Long, Long> key) throws SQLException {
        if (key == null) {
            setUpModeOnDB(key.getKey(), key.getValue(), 9);
            return 9;
        }

        List<PriceTime> history = getHistory(key.getKey(), key.getValue());
        if (history == null) {
            setUpModeOnDB(key.getKey(), key.getValue(), 8);
            return 8;
        }
        if (history.size() < MIN_VALID_HISTORY_SIZE) {
            setUpModeOnDB(key.getKey(), key.getValue(), 7);
            return 7;
        }
        long firstSell = System.currentTimeMillis();
        long minPrice = 100000000;
        long maxPrice = 0;
        long avPrice = 0;
        for (PriceTime priceTime :
                history) {
            avPrice += priceTime.getL_price();

            // TODO: Вероятно достаттончо просто взять последний элемент листа, надо тестить!!
            if (priceTime.getL_time() < firstSell)
                firstSell = priceTime.getL_time();

            if (priceTime.getL_price() > maxPrice)
                maxPrice = priceTime.getL_price();

            if (priceTime.getL_price() < minPrice)
                minPrice = priceTime.getL_price();
        }
        avPrice /= history.size();

        if (System.currentTimeMillis() - firstSell * 1000 > MAX_VALID_HISTORY_OLD) {
            setUpModeOnDB(key.getKey(), key.getValue(), 6);
            return 6;
        }
        if (avPrice > MAX_VALID_AV_PRICE){
            setUpModeOnDB(key.getKey(), key.getValue(), 5);
            return 5;
        }
        if (avPrice < MIN_VALID_AV_PRICE){
            setUpModeOnDB(key.getKey(), key.getValue(), 4);
            return 4;
        }

        // Хотим покупать
        setUpModeOnDB(key.getKey(), key.getValue(), 1);
        return 1;
    }

    private void setUpModeOnDB(Long c_classid, Long c_instanceid, int mode) throws SQLException {
        Statement statement = connection.createStatement();
        String query =
                //language=sql
                "UPDATE feed \n" +
                        "SET mode = " + mode +
                        "WHERE c_classid = " + c_classid + " AND c_instanceid =" + c_instanceid + ";";
        statement.executeUpdate(query);
    }

    private static Connection createConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", CSDatabaseConfig.user);
        props.setProperty("password", CSDatabaseConfig.password);
        props.setProperty("ddl", CSDatabaseConfig.ddl);
        Connection conn = DriverManager.getConnection(CSDatabaseConfig.url, props);
        return conn;
    }

    private static class CSDatabaseConfig {
        private static final String url = "jdbc:postgresql://localhost:5432/cs";
        private static final String user = "postgres";
        private static final String password = "FFadmin";
        private static final String ddl = "true";
    }


}

