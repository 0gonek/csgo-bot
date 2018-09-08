package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class FeedService {

    private static final String CSV_TO_FEED =
            //language=sql
            "COPY FEED(c_classid,c_instanceid,c_price,c_offers,c_popularity,c_rarity," +
                    "c_quality,c_heroid,c_slot,c_stickers,c_market_name,c_market_name_en," +
                    "c_market_hash_name,c_name_color,c_price_updated,c_pop,c_base_id) \n" +
                    "FROM 'D:\\cs-go-bot\\csv\\Feed.csv' WITH  DELIMITER ';' CSV HEADER";

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
        CreateDB.createFeedTable(connection);
        CreateDB.createHistoryTable(connection);
    }

    public void uploadCsvToFeed() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CSV_TO_FEED);
        System.out.println("CSV uploaded successfully.");
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
