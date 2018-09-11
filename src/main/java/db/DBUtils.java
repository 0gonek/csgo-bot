package db;

import java.sql.*;
import java.util.Properties;

public class DBUtils {

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
                    "  price bigint,\n" +
                    "  c_classid bigint,\n" +
                    "\tc_instanceid bigint\n" +
                    ");";

    private static final String CREATE_HISTORY_INDEX_SQL =
            //language=sql
            "CREATE INDEX name_index ON HISTORY (c_classid, c_instanceid);";

    private static final String CREATE_STATS_SQL =
            //language=sql
            "CREATE TABLE STATS(\n" +
                    "  id serial primary key,\n" +
                    "  first_update_time bigint,\n" +
                    "  min bigint,\n" +
                    "  max bigint,\n" +
                    "  avg bigint,\n" +
                    "  num int,\n" +
                    "  c_classid bigint,\n" +
                    "\tc_instanceid bigint\n" +
                    ");";

    private static final String CREATE_STATS_INDEX_SQL =
            //language=sql
            "CREATE INDEX name_status_index ON STATS (c_classid, c_instanceid);";

    private static final String CREATE_CONSTS_TABLE_SQL =
            //language=sql
            "CREATE TABLE CONSTS(" +
                    " const_key VARCHAR PRIMARY KEY," +
                    " long_value bigint," +
                    " string_value VARCHAR " +
                    ");";

    private static final String CREATE_CONSTS_INDEX_SQL =
            //language=sql
            "CREATE INDEX key_consts_index ON CONSTS (const_key);";

    private static final String CREATE_BUY_HISTORY =
            //language=sql
            "create table buy_history(\n" +
                    "  id bigserial primary key,\n" +
                    "  i_classid bigint,\n" +
                    "  i_instanceid bigint,\n" +
                    "  i_quality text,\n" +
                    "  i_name_color varchar(6),\n" +
                    "  i_market_hash_name text,\n" +
                    "  stickers text,\n" +
                    "  ui_price real,\n" +
                    "  w_price real\n" +
                    ");";

    //Пример триггера todo повесить тириггер на таблицы для добавление записи без проверки на существование
    private static final String INSERT_TRIGGER =
            //language=sql
            "CREATE FUNCTION foo_name() RETURNS trigger AS $foo_name$\n" +
                    "BEGIN\n" +
                    "\n" +
                    "IF (exists(\n" +
                    "\tselect 1 \n" +
                    "\tfrom buy_history\n" +
                    "\twhere )) \n" +
                    "THEN\n" +
                    "  update buy_history \n" +
                    "  set  \n" +
                    "  where ;\n" +
                    "\treturn null;\n" +
                    "END IF;\n" +
                    "  \n" +
                    "RETURN NEW;\n" +
                    "END;\n" +
                    "$foo_name$ LANGUAGE plpgsql;\n" +
                    "CREATE TRIGGER check_history BEFORE INSERT ON t_name \n" +
                    "FOR EACH ROW EXECUTE PROCEDURE foo_name();";

    private static final String CREATE_GOOD_PRICE_SQL =
            //language=sql
            "CREATE TABLE GOOD_PRICE( c_classid bigint, c_instanceid bigint, price bigint, update_time bigint, " +
                    "PRIMARY KEY (c_classid, c_instanceid));";

    //TODO: Добавить удаление старых таблиц. С трай кечем - если таблиц не было, просто работать дальше.
    public static void reset() throws SQLException {
        Connection connection = createConnection();
        DBUtils.createFeedTable(connection);
        DBUtils.createHistoryTable(connection);
        DBUtils.createStatsTable(connection);
        DBUtils.createConstsTable(connection);
        DBUtils.createGoodPriceTable(connection);
        DBUtils.setCreateBuyHistoryTable(connection);
    }

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

    static void createStatsTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_STATS_SQL);
        statement.execute(CREATE_STATS_INDEX_SQL);
        System.out.println("Stats table has been created successfully.");
    }

    static void createConstsTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_CONSTS_TABLE_SQL);
        statement.execute(CREATE_CONSTS_INDEX_SQL);
        System.out.println("Consts table has been created successfully.");
    }

    static void setCreateBuyHistoryTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_BUY_HISTORY);
        System.out.println("Buy_history table has been created successfully.");
    }

    static void createGoodPriceTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_GOOD_PRICE_SQL);
        System.out.println("Good_price table has been created successfully.");
    }

    static void dropFeedTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(DROP_FEED_SQL);
        System.out.println("FeedService table has been droped successfully.");
    }

    static int getFeedRowCount(Connection connection) throws SQLException {
        Statement statement = connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        ResultSet resultSet = statement.executeQuery("SELECT * FROM FEED;");
        resultSet.last();
        return resultSet.getRow();
    }

    static Connection createConnection() {
        try {
            Properties props = new Properties();
            props.setProperty("user", DBUtils.CSDatabaseConfig.user);
            props.setProperty("password", DBUtils.CSDatabaseConfig.password);
            props.setProperty("ddl", DBUtils.CSDatabaseConfig.ddl);
            Connection conn = DriverManager.getConnection(DBUtils.CSDatabaseConfig.url, props);
            return conn;
        } catch (SQLException e) {
            //todo переписать
            e.printStackTrace();
            throw new RuntimeException("Ошибка подключения к базе данных\n");
        }
    }

    private static class CSDatabaseConfig {
        private static final String url = "jdbc:postgresql://localhost:5432/cs";
        private static final String user = "postgres";
        private static final String password = "FFadmin";
        private static final String ddl = "true";
    }
}
