package com.example.schae.opentodo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todos.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + Contract.Entry.TABLE_NAME + " ("
                + Contract.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.Entry.COLUMN_TODO + " TEXT NOT NULL, "
                + Contract.Entry.COLUMN_CHECKBOX + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
