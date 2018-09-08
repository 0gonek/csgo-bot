package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Feed {

    private static final String CREATE_FEES_SQL =

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

    private final Connection connection;

    public Feed() {
        try {
            this.connection = getConnection();
        }
        catch (SQLException ex){
            throw new IllegalArgumentException("Can't create connection to Feed db");
        }
    }

    private Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", CSDatabaseConfig.user);
        props.setProperty("password", CSDatabaseConfig.password);
        props.setProperty("ddl", CSDatabaseConfig.ddl);
        Connection conn = DriverManager.getConnection(CSDatabaseConfig.url, props);
        return conn;
    }

    public void createFeedTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_FEES_SQL);
        System.out.println("Feed table has been created successfully.");
    }

    private static class CSDatabaseConfig {
        private static final String url = "jdbc:postgresql://localhost:5432/cs";
        private static final String user = "postgres";
        private static final String password = "FFadmin";
        private static final String ddl = "true";
    }
}
