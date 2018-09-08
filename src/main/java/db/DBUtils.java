package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

class DBUtils {

    private static final String DROP_FEED_SQL =
            //language=sql
            "drop table FEED;";
    private static final String CREATE_FEED_SQL =
            //language=sql
            "CREATE TABLE FEED(\n" +
                    "\tid serial primary key,\n" +
                    "\tmode smallint DEFAULT 0,\n" +
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

    private static final String CREATE_HISTORY_SQL =
            //language=sql
            "CREATE TABLE HISTORY(\n" +
                    "  id serial primary key,\n" +
                    "  update_time bigint,\n" +
                    "  c_classid bigint,\n" +
                    "\tc_instanceid bigint\n" +
                    ");";

    private static final String CREATE_HISTORY_INDEX_SQL =
            //language=sql
            "CREATE INDEX name_index ON HISTORY (c_classid, c_instanceid);";

    static void createFeedTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_FEED_SQL);
        System.out.println("Feed table has been created successfully.");
    }

    static void createHistoryTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_HISTORY_SQL);
        statement.execute(CREATE_HISTORY_INDEX_SQL);
        System.out.println("History table has been created successfully.");
    }

    static void dropFeedTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(DROP_FEED_SQL);
        System.out.println("FeedService table has been droped successfully.");
    }

}
