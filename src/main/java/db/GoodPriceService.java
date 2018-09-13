package db;

import javafx.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class GoodPriceService {

    private final Connection connection;

    public GoodPriceService() {
            this.connection = DBUtils.createConnection();
    }

    public Map<Pair<Long, Long>, Long> getCasheFromTable() throws SQLException {
        Statement statement = connection.createStatement();
        String query =
                //language=sql
                "SELECT c_classid, c_instanceid, price" +
                        " FROM good_price;";
        ResultSet resultSet = statement.executeQuery(query);
        Map<Pair<Long, Long>, Long> result = new HashMap<Pair<Long, Long>, Long>();
        while (resultSet.next()) {
            result.put(
                    new Pair<Long, Long>(
                            resultSet.getLong("c_classid"),
                            resultSet.getLong("c_instanceid")
                    ),
                    resultSet.getLong("price"));
        }
        return result;
    }

    public boolean contains(Pair<Long, Long> pair) throws SQLException {
        Statement statement = connection.createStatement();
        String query =
                //language=sql
                "SELECT *" +
                        " FROM good_price " +
                        "WHERE c_classid = " + pair.getKey() + "AND c_instanceid = " + pair.getValue() + ";";
        ResultSet resultSet = statement.executeQuery(query);
        Map<Pair<Long, Long>, Long> result = new HashMap<Pair<Long, Long>, Long>();
        return resultSet.next();
    }

    public void addPrice(Pair<Long, Long> pair, Long price) throws SQLException {
        Statement statement = connection.createStatement();
        if (contains(pair))
            throw new SQLException("GoodPrice table already contains pair: " + pair.getKey() + "_" + pair.getValue());
        String query =
                //language=sql
                "INSERT INTO good_price (c_classid, c_instanceid, price, update_time)" +
                        " VALUES (" + pair.getKey() +
                        ", " + pair.getValue() +
                        ", " + price +
                        ", " + System.currentTimeMillis()/1000 +
                        ");";
        statement.executeUpdate(query);
    }

    public void updatePrice(Pair<Long, Long> pair, Long price) throws SQLException {
        Statement statement = connection.createStatement();
        if (!contains(pair))
            throw new SQLException("GoodPrice table doesn't contains pair: " + pair.getKey() + "_" + pair.getValue());
        String query =
                //language=sql
                "UPDATE good_price \n" +
                        "SET price = " + price + ", update_time = " + System.currentTimeMillis()/1000 +
                        " WHERE c_classid = " + pair.getKey() + "AND c_instanceid = " + pair.getValue() + ";";
        statement.executeUpdate(query);
    }

    public void deleteFromCashe(Pair<Long, Long> pair) throws SQLException {
        Statement statement = connection.createStatement();
        if (contains(pair)) {
            String query =
                    //language=sql
                    "DELETE FROM good_price WHERE c_classid = " + pair.getKey() +
                            " AND c_instanceid = " + pair.getValue() + ";";
            statement.executeUpdate(query);
        }
    }
}
