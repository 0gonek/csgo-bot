package db;

import pojo.ItemHistory;
import pojo.PriceTime;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HistoryStatsService {

    private final Connection connection;

    public HistoryStatsService() {
            this.connection = DBUtils.createConnection();
    }

    public List<PriceTime> getHistory(Long c_classid, Long c_instanceid) throws SQLException {
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
}
