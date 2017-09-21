package com.github.liurui;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setDriverClassName("org.apache.phoenix.jdbc.PhoenixDriver");
        dataSource.setUrl("jdbc:phoenix:localhost:2181");

        DruidPooledConnection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from stock_symbol");

        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
    }
}
