package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

class CreateDB {
    private static final String CREATE_FEED_SQL =

            "create table FEED(\n" +
                    "\tid serial primary key,\n" +
                    "\tmode smallint,\n" +
                    "\t\n" +
                    "\tc_classid bigint,\n" +
                    "\tc_instanceid bigint,\n" +
                    "\tc_price integer,\n" +
                    "\tc_offers integer,\n" +
                    "\tc_popularity integer,\n" +
                    "\tc_rarity varchar(256),\n" +
                    "\tc_quality varchar(256),\n" +
                    "\tc_heroid integer,\n" +
                    "\tc_slot varchar(256),\n" +
                    "\tc_stickers smallint,\n" +
                    "\tc_market_name text,\n" +
                    "\tc_market_name_en text,\n" +
                    "\tc_name_color varchar(6),\n" +
                    "\tc_price_updated integer,\n" +
                    "\tc_pop integer\n" +
                    ");";

    static void createFeedTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_FEED_SQL);
        System.out.println("FeedService table has been created successfully.");
    }
}
