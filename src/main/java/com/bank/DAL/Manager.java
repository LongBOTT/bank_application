package com.bank.DAL;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Manager extends MySQL {
    private final String tableName;
    private final List<String> columnNames;

    public Manager(String tableName, List<String> columnNames) {
        super();
        this.tableName = tableName;
        this.columnNames = columnNames;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    // INSERT INTO [dbo].[column_name] ([], [], [], [], [], []) VALUES (12, 'BA010', '0', '2024-01-01', 600, 10)

    public int create(Object... values) throws SQLException, IOException {
        if (values == null || values.length != columnNames.size()) {
            throw new IllegalArgumentException("Invalid number of arguments.");
        }

        String query = "INSERT INTO [dbo].[" + tableName + "] (["+ String.join("], [", columnNames) + "]) VALUES(?" + ", ?".repeat(values.length - 1) + ");";
        return executeUpdate(query, values);
    }

    // SELECT * FROM [dbo].[column_name]

    public List<List<String>> read(String... conditions) throws SQLException, IOException {
        String query = "SELECT * FROM [dbo].[" + tableName + "]";
        if (conditions != null && conditions.length > 0) {
            query += " WHERE " + String.join(" AND ", conditions);
        }

        query += ";";
        return executeQuery(query);
    }

    // UPDATE [dbo].[column_name] SET [] = '1' WHERE [id] = 12

    public int update(List<Object> updateValues, String... conditions) throws SQLException, IOException {
        if (updateValues == null || updateValues.isEmpty()) {
            throw new IllegalArgumentException("Dữ liệu cập nhật không được trống");
        }

        int conditionsLength = 0;
        if (conditions != null && conditions.length > 0) {
            conditionsLength = conditions.length;
        }

        String setClause;
        if (updateValues.size() == 1) {
            // only update the DELETED
            setClause = "DELETED = ?";
        } else {
            setClause = "[" + String.join("] = ?, [", columnNames) + "] = ?";
        }

        String query = "UPDATE [dbo].[" + tableName + "] SET " + setClause;

        if (conditionsLength > 0) {
            query += " WHERE " + String.join(" AND ", conditions);
        }
        query += ";";
        return executeUpdate(query, updateValues.toArray());
    }

    // DELETE FROM [dbo].[column_name] WHERE [] = 10

    public int delete(String... conditions) throws SQLException, IOException {
        String query = "DELETE FROM [dbo].[" + tableName + "]";
        List<Object> values = new ArrayList<>();

        if (conditions != null && conditions.length > 0) {
            query += " WHERE " + String.join(" AND ", conditions);
            values = Arrays.asList((Object[]) conditions);
        }

        query += ";";
        return executeUpdate(query, values);
    }

    public <T> List<T> convert(List<List<String>> data, Function<List<String>, T> converter) {
        List<T> list = new ArrayList<>();
        for (List<String> row : data) {
            T object = converter.apply(row);
            list.add(object);
        }
        return list;
    }
}
