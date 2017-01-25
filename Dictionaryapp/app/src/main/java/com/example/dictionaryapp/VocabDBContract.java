package com.example.dictionaryapp;

import android.provider.BaseColumns;

/**
 * Created by cuti-pie on 16/01/17.
 */

public class VocabDBContract {

    private VocabDBContract() {

    }

    /* Inner class that defines the table contents */
    public static class VocabDbEntry implements BaseColumns {

        public static final String TABLE_NAME = "Vocab";
        public static final String COLUMN_WORD = "Word";
        public static final String COLUMN_DETAILS = "Details";
    }
}
