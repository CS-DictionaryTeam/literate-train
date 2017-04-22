package com.hfad.cs63d;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class DictionaryDatabaseUtilities {
    //???
    /*
    SELECT term FROM dictionary ORDER BY term
     */
    public static Cursor getWord(SQLiteDatabase db, int rowId, String wordQuery) {

        return db.query(DictionaryDatabaseHelper.DICTIONARY_TABLE,//Table//SQL FROM
                new String[]{DictionaryDatabaseHelper.TERM_COL},// SELECT all columns//???
                wordQuery,//selection//???
                null,//selectionArgs
                null,//groupBy
                null,// having
                DictionaryDatabaseHelper.TERM_COL,// orderBY
                null// limit
        );
    }

    /*
    SELECT columns FROM dictionary WHERE category = 'keyword'
     */

    public static Cursor getColumns(SQLiteDatabase db, String keyword) {
        return db.query(DictionaryDatabaseHelper.DICTIONARY_TABLE,//Table//SQL FROM
                null,// SELECT all columns//yes
                DictionaryDatabaseHelper.CATEGORY_COL + " = " + keyword,//selection//
                null,//selectionArgs//???
                null,//groupBy
                null,// having
                null,// orderBY//
                null// limit
        );

    }
    //???
    /*
    SELECT DISTINCT category FROM dictionary
    */
    public static Cursor getCategory(SQLiteDatabase db, String categoryEntry) {
        return db.query(true, // boolean DISTINCT
                DictionaryDatabaseHelper.DICTIONARY_TABLE,//Table//SQL FROM
                new String[]{DictionaryDatabaseHelper.CATEGORY_COL},// SELECT all columns//NO
                DictionaryDatabaseHelper.CATEGORY_COL + " = " + categoryEntry,//selection
                null,//selectionArgs
                null,//groupBy
                null,// having
                null,// orderBY
                null// limit
        );
    }


    /*
    SELECT DISTINCT term FROM history ORDER BY _id DESC LIMIT 50
     */
//    public static Cursor getHistory(SQLiteDatabase db) {
//        return db.query(true,//boolean DISTINCT
//                DictionaryDatabaseHelper.HISTORY_TABLE,//Table//SQL FROM
//                null,// SELECT all columns
//                DictionaryDatabaseHelper.TERM_COL,//selection
//                null,//selectionArgs
//                null,//groupBy
//                null,// having
//                DictionaryDatabaseHelper.ID_COL + " DESC",// orderBY
//                50// limit
//        );
//    }
}
