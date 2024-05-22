package com.example.todo_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * define method to create build  database files
 */
public class DatabaseBuilder  extends SQLiteOpenHelper  implements DatabaseInfo {
    public DatabaseBuilder(@Nullable Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables

        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_TABLE_REMAINDER);

        // add init  data for tables
//        db.execSQL(ADD_CATEGORY_INIT_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMAINDER);
        onCreate(db);
    }
}
