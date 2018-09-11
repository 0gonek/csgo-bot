package db;

import javafx.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConstsService {

    private final Connection connection;

    public ConstsService() {
            this.connection = DBUtils.createConnection();
    }

    public void addConst(String key, Long longValue, String stringValue) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM consts WHERE const_key = '" + key + "';";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next())
            throw new RuntimeException("Key " + key + " already exists in consts table");
        query =
                //language=sql
                "INSERT INTO consts (const_key, long_value, string_value)" +
                        " VALUES ('" + key +
                        "', " + longValue +
                        ", '" + stringValue +
                        "');";
        statement.executeUpdate(query);
    }

    public void updateConst(String key, Long longValue, String stringValue) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM consts WHERE const_key = '" + key + "';";
        ResultSet resultSet = statement.executeQuery(query);
        if (!resultSet.next())
            throw new RuntimeException("Key " + key + " doesn't exist in consts table");
        query =
                //language=sql
                "UPDATE consts \n" +
                        "SET long_value = " + longValue + ", string_value = '" + stringValue +
                        "' WHERE const_key = '" + key + "';";
        statement.executeUpdate(query);
    }

    public Pair<Long, String> getConst(String key) throws SQLException {
        Statement statement = connection.createStatement();
        String query =
                //language=sql
                "SELECT long_value, string_value\n" +
                        "FROM consts WHERE const_key = '" + key + "';";
        ResultSet resultSet = statement.executeQuery(query);
        if (!resultSet.next())
            return null;
        return new Pair<Long, String>(resultSet.getLong("long_value"), resultSet.getString("string_value"));
    }
}
