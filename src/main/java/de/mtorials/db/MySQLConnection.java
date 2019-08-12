package de.mtorials.db;

import de.mtorials.misc.Logger;

import java.sql.*;

public class MySQLConnection {

    private String host = "";
    private String database = "";
    private String user = "";
    private String password = "";

    private Connection connection;

    public MySQLConnection(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
        connect();
    }

    public MySQLConnection(String host, String database, String user) {
        this.host = host;
        this.database = database;
        this.user = user;
        connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database + "?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin", user, password);

            Logger.log("Connection established", 0);

        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            Logger.log("Connection failure!", 1);
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.log("Connection failure!", 1);
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            connect();
            e.printStackTrace();
        }
    }

    public boolean update(String sql) {
        try {
            if (connection != null) {
                Statement st = connection.createStatement();
                st.execute(sql);
            } else {
                connect();
            }
            return true;
        } catch (SQLException e) {
            connect();
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet query(String query) {

        ResultSet rs = null;
        Statement st;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            connect();
            e.printStackTrace();
        }
        return rs;
    }
}
