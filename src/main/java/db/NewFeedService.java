package db;

import resources.Props;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class NewFeedService {

    private static final String CREATE_NEW_FEED =
            //language=sql
            "CREATE TABLE NEW_FEED(\n" +
                    "\tc_classid bigint,\n" +
                    "\tc_instanceid bigint,\n" +
                    "\tc_price integer,\n" +
                    "\tc_offers integer,\n" +
                    "\tc_popularity integer,\n" +
                    "\tc_rarity varchar(256),\n" +
                    "\tc_quality varchar(256),\n" +
                    "\tc_heroid integer,\n" +
                    "\tc_slot varchar(256),\n" +
                    "\tc_stickers varchar,\n" +
                    "\tc_market_name text,\n" +
                    "\tc_market_name_en text,\n" +
                    "\tc_market_hash_name text,\n" +
                    "\tc_name_color varchar(6),\n" +
                    "\tc_price_updated integer,\n" +
                    "\tc_pop integer,\n" +
                    "\tc_base_id integer\n" +
                    ");";

    private static final String DROP_NEW_FEED =
            //language=sql
            "drop table new_feed;";

    private static final String CSV_TO_NEW_FEED =
            //language=sql
            "copy new_feed(c_classid,c_instanceid,c_price,c_offers,c_popularity,c_rarity," +
                    "c_quality,c_heroid,c_slot,c_stickers,c_market_name,c_market_name_en," +
                    "c_market_hash_name,c_name_color,c_price_updated,c_pop,c_base_id) \n" +
                    "FROM '" + Props.CSV_FILE + "' WITH  DELIMITER ';' CSV HEADER";


    private static final String MERGE_WITH_FEED =
            //language=sql
            "INSERT INTO feed (c_classid, c_instanceid, c_price, c_offers, c_popularity, c_rarity, c_quality, c_heroid, c_slot, c_stickers, c_market_name, c_market_name_en, c_market_hash_name, c_name_color, c_price_updated, c_pop, c_base_id)\n" +
                    "SELECT * FROM new_feed where not exists(select 1 from feed where new_feed.c_classid = feed.c_classid and new_feed.c_instanceid = feed.c_instanceid);";

    private Connection connection;


    public NewFeedService() {
    }

    public void uploadCsvToNewFeed() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CSV_TO_NEW_FEED);
        System.out.println("CSV uploaded to new_feed successfully.");
    }

    public void createConnection() {
        this.connection = DBUtils.createConnection();
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    void createNewFeed() throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.execute(CREATE_NEW_FEED);
        System.out.println("New_feed table has been created successfully.");
    }

    void dropNewFeed() throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.execute(DROP_NEW_FEED);
        System.out.println("New_feed table has been droped successfully.");
    }

    void mergeWithFeed() throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.execute(MERGE_WITH_FEED);
        System.out.println("Table new_feed was merged with feed.");
    }
}

