package com.clark.app;

import static com.clark.func.Functions.closeQuietly;
import static com.clark.func.Functions.print;
import static com.clark.func.Functions.println;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager
                .getConnection("jdbc:sqlite:sina_weibo.db");
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("PRAGMA database_list;");
        ResultSetMetaData metaData = rs.getMetaData();
        try {
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                if (i > 1) {
                    print("\t");
                }
                print(metaData.getColumnName(i));
                print("(");
                print(metaData.getColumnTypeName(i));
                print(")");
            }
            println();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) {
                        print("\t");
                    }
                    print(rs.getString(i));
                }
                println();
            }
        } finally {
            closeQuietly(rs);
            closeQuietly(conn);
        }
    }

}
