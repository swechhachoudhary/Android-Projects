package com.example.dictionaryapp;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.dictionaryapp.VocabDBContract.VocabDbEntry;

/**
 * Created by cuti-pie on 16/01/17.
 */

public class VocabDataBase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VocabDB.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + VocabDbEntry.TABLE_NAME + " (" +
                    VocabDbEntry._ID + " INTEGER PRIMARY KEY," +
                    VocabDbEntry.COLUMN_WORD + " TEXT," +
                    VocabDbEntry.COLUMN_DETAILS + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + VocabDbEntry.TABLE_NAME;

    public VocabDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);

    }
}
