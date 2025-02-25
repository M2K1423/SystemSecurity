package com.example.webbongden.dao.db;
import com.example.webbongden.dao.db.DBProperties;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;
import java.util.List;

public class JDBIConnect {
    static String url = "jdbc:mysql://" + DBProperties.host() + ":" + DBProperties.port() + "/" + DBProperties.dbname() + "?" + DBProperties.option();
    static Jdbi jdbi;

    public static Jdbi get() {
        if (jdbi == null) makeConnect();
        return jdbi;
    }

    private static void makeConnect() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL(url);
        ds.setUser(DBProperties.username());
        ds.setPassword(DBProperties.password());
        try {
            ds.setUseCompression(true);
            ds.setAutoReconnect(true);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        jdbi = Jdbi.create(ds);
    }
}
