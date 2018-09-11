package db;

import pojo.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BuyHistoryService {
    private final Connection connection;

    public BuyHistoryService() throws SQLException {
        DBUtils.reset();
        this.connection = DBUtils.createConnection();

    }

    public void insert(Item item) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(item.toSQLInsert());
            statement.close();
        } catch (SQLException e) {
            //todo переписать
            e.printStackTrace();
        }

    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            //todo переписать
            e.printStackTrace();
        }
    }
}
