package com.bank.DAL;

import com.bank.utils.Database;
import javafx.util.Pair;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLServer {
    public SQLServer() {
    }

    public List<List<String>> executeQuery(String query, Object... values) throws SQLException, IOException {
        Connection connection = Database.getConnection();
        if (connection == null)
            return new ArrayList<>();
        List<List<String>> result;
        try (Statement statement = connection.createStatement()) {
            String formattedQuery = formatQuery(query, values);
            result = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(formattedQuery);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                List<String> row = new ArrayList<>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getObject(i).toString());
                }
                result.add(row);
            }
            System.out.println(formattedQuery + "\n");
        }
        Database.closeConnection(connection);
        return result;
    }

    public int executeUpdate(String query, Object... values) throws SQLException, IOException {
        Connection connection = Database.getConnection();
        if (connection == null)
            return 0;
        int numOfRows;
        try (Statement statement = connection.createStatement()) {
            String formattedQuery = formatQuery(query, values);
            numOfRows = statement.executeUpdate(formattedQuery);
            System.out.println(formattedQuery + "\n");
        }
        Database.closeConnection(connection);
        return numOfRows;
    }

    public String formatQuery(String query, Object... values) {
        String stringValue;
        for (Object value : values) {
            if (value instanceof Date day) {
                stringValue = "'" + day + "'";
            } else if (value instanceof String || value instanceof Character) {
                stringValue = "N'" + value + "'";
            } else if (value instanceof Boolean) {
                stringValue = (boolean) value ? "1" : "0";
            } else if (value instanceof Number) {
                stringValue = value.toString();
            } else if (value instanceof LocalDateTime) {
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                stringValue = "'" + ((LocalDateTime) value).format(myFormatObj) + "'";
            } else {
                stringValue = "'" + value + "'";
            }
            query = query.replaceFirst("\\?", stringValue);
        }
        return query;
    }

    public static List<List<String>> executeQueryStatistic(String query) throws SQLException, IOException {
        Connection connection = Database.getConnection();
        if (connection == null)
            return new ArrayList<>();
        List<List<String>> result;
        try (Statement statement = connection.createStatement()) {
            result = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                List<String> row = new ArrayList<>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    if (resultSet.getObject(i) == null)
                        row.add("0");
                    else
                        row.add(resultSet.getObject(i).toString());
                }
                result.add(row);
            }
            System.out.println(query + "\n");
        }
        Database.closeConnection(connection);
        return result;
    }

    // EXECUTE [dbo].[sp_CheckLogin] @username = 'admin'

    public List<List<String>> executeProcedure(String procedureName , Pair<String, Object>... conditions) throws SQLException, IOException {
        String query = "";
        if (procedureName.equals("sp_GetAutoID"))
            query = "DECLARE @return_value int, @newId bigint EXECUTE @return_value = [dbo].[" + procedureName + "]";
        else
            query = "EXECUTE [dbo].[" + procedureName + "]";

        if (conditions != null && conditions.length > 0) {
            String[] strings = new String[0];
            for (Pair condition: conditions) {
                strings = Arrays.copyOf(strings, strings.length + 1);
                if (condition.getValue() instanceof String || condition.getValue() instanceof Character) {
                    strings[strings.length - 1] = "@" + condition.getKey() + " = N'" + condition.getValue() + "'";
                }
                else if (condition.getValue() instanceof Integer || condition.getValue() instanceof Double || condition.getValue() instanceof BigDecimal) {
                    strings[strings.length - 1] = "@" + condition.getKey() + " = " + condition.getValue();
                }
                 else if (condition.getValue() instanceof Boolean) {
                    String value =  Boolean.parseBoolean(condition.getValue().toString()) ? "1" : "0";
                    strings[strings.length - 1] = "@" + condition.getKey() + " = " + value;
                }
                else if (condition.getValue() instanceof java.util.Date) {
                    strings[strings.length - 1] = "@" + condition.getKey() + " = '" + condition.getValue() + "'";
                }
//                else if (condition.getValue() instanceof LocalDateTime) {
//                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
//                    strings[strings.length - 1] =  "@" + condition.getKey() + " = '" + (LocalDateTime.parse(condition.getValue().toString())).format(myFormatObj) + "'";
//                }
            }
            query += " " + String.join(", ", strings);
        }
        if (procedureName.equals("sp_GetAutoID"))
            query += ", @newId = @newId OUTPUT SELECT @newId";
        query += ";";
        return executeQuery(query);
    }

    public List<List<String>> getStatement(String bank_account_number) {
        try {
            return executeProcedure("sp_GetStatement", new Pair<>("bank_account_number", bank_account_number));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in SQPServer.getStatement(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<List<String>> getStatement_By_Date(String bank_account_number, String start_date, String end_date) {
        try {
            return executeProcedure("sp_GetStatement_By_Date", new Pair<>("bank_account_number", bank_account_number), new Pair<>("start_date", start_date), new Pair<>("end_date", end_date));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in SQPServer.getStatement_By_Date(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
