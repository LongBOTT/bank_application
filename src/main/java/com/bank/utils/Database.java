package com.bank.utils;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;


public class Database {
    public static int headquarter_id = 1;
    public static Connection getConnection() throws IOException {
        do {
            try {
                FileInputStream fileInputStream;
                if (headquarter_id == 0)
                    fileInputStream = new FileInputStream(Objects.requireNonNull(Resource.getAbsolutePath(Settings.DATABASE_SERVER_FILE)));
                else if (headquarter_id == 1)
                    fileInputStream = new FileInputStream(Objects.requireNonNull(Resource.getAbsolutePath(Settings.DATABASE_HO_CHI_MINH_FILE)));
                else if (headquarter_id == 2)
                    fileInputStream = new FileInputStream(Objects.requireNonNull(Resource.getAbsolutePath(Settings.DATABASE_HA_NOI_FILE)));
                else if (headquarter_id == 3)
                    fileInputStream = new FileInputStream(Objects.requireNonNull(Resource.getAbsolutePath(Settings.DATABASE_HAI_PHONG_FILE)));
                else if (headquarter_id == 4)
                    fileInputStream = new FileInputStream(Objects.requireNonNull(Resource.getAbsolutePath(Settings.DATABASE_DA_NANG_FILE)));
                else
                    fileInputStream = new FileInputStream(Objects.requireNonNull(Resource.getAbsolutePath(Settings.DATABASE_OTHERS_FILE)));

                Properties properties = new Properties();
                properties.load(fileInputStream);
                String dbPort = properties.getProperty("db.port");
                String dbInstance = properties.getProperty("db.instance");
                String dbDatabase = properties.getProperty("db.database");
                String dbUsername = properties.getProperty("db.username");
                String dbPassword = properties.getProperty("db.password");
                fileInputStream.close();
                if (dbDatabase == null)
                    throw new RuntimeException();
                String dbUrl = String.format("jdbc:sqlserver://%s:%s;user=%s;password=%s;database=%s;encrypt=true;trustServerCertificate=true;", dbInstance, dbPort, dbUsername, dbPassword, dbDatabase);
                return DriverManager.getConnection(dbUrl);
            } catch (RuntimeException | SQLException e) {
                System.out.println(e.getMessage());
                initializeDatabase();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (true);
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void initializeDatabase() throws IOException {
        if (SwingUtilities.isEventDispatchThread()) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến cơ sở dữ liệu.\nVui lòng khởi động lại chương trình", "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        JOptionPane.showMessageDialog(null, "Không thể kết nối đến cơ sở dữ liệu.\nVui lòng cấu hình lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
