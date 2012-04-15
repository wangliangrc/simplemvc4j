package com.clark.app;

import java.util.Arrays;

import SQLite.Callback;
import SQLite.Database;
import SQLite.SQLiteException;
import SQLite.TableResult;

public class Main implements Callback {

    public static void main(String[] args) throws SQLiteException {
        Main main = new Main();

        Database db = Database.open("test.db",
                SQLite.Constants.SQLITE_OPEN_READWRITE
                        | SQLite.Constants.SQLITE_OPEN_CREATE);
        System.out.println(db.dbversion());

        String sql = "drop table if exists domain;"
                + "create table domain (i int, n numberic, t text, b blob);"
                + "insert into domain values (3.142, 3.142, 3.142, 3.142);"
                + "insert into domain values ('3.142', '3.142', '3.142', '3.142');"
                + "insert into domain values (3142, 3142, 3142, 3142);"
                + "insert into domain values (x'3142', x'3142', x'3142', x'3142');"
                + "insert into domain values (null, null, null, null);";
        db.exec(sql, main);
        TableResult table = db
                .get_table("select rowid, typeof(i), typeof(n), typeof(t), typeof(b) from domain;");
        for (String[] ss : table.rows) {
            System.out.println(Arrays.toString(ss));
        }

        db.close();
    }

    @Override
    public void columns(String[] coldata) {
        System.out.println("Main.columns()");
        System.out.println(Arrays.toString(coldata));
    }

    @Override
    public void types(String[] types) {
        System.out.println("Main.types()");
        System.out.println(Arrays.toString(types));
    }

    @Override
    public boolean newrow(String[] rowdata) {
        System.out.println("Main.newrow()");
        System.out.println(Arrays.toString(rowdata));
        return false;
    }

}
