package com.hfad.cs63d;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


class DictionaryDatabaseHelper extends SQLiteOpenHelper {
    private final String TAG = getClass().toString();
    private Context mHelperContex;
    private SQLiteDatabase sqlDatabase;
    int count = 0;

    private static final String DB_NAME = "csdictionary";
    private static final int DB_VERSION = 1;

    //package private table and column names
    static final String DICTIONARY_TABLE = "dictionary";
    static final String ID_COL = "_id";
    static final String TERM_COL = "term";
    static final String DEFINITION_COL = "definition";
    static final String CATEGORY_COL = "category";
    static final String FAVORITES_COL = "favorites";
    static final String NOTES_COL = "notes";

    DictionaryDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mHelperContex = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sqlDatabase = db;
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE " + DICTIONARY_TABLE + " (" +
                    ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_COL + " TEXT, " +
                    DEFINITION_COL + " TEXT, " +
                    CATEGORY_COL + " TEXT, " +
                    FAVORITES_COL + " NUMERIC, " +
                    NOTES_COL + " TEXT);");
            Log.v(TAG, "in updateMyDatabase()");
            loadTerms();
        }
    }

    private void loadTerms() {
        Log.d(TAG, "in loadTerms()");
        Log.d(TAG, "Loading words...");
        Resources resources = mHelperContex.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.cs63dict_singleline_def);
        BufferedReader buffReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line;
            while ((line = buffReader.readLine()) != null) {
                String[] strings = TextUtils.split(line, "⏣");
                if (strings.length < 3) continue;
                long id = addTerm(strings[0].trim(), strings[1].trim(), strings[2].trim());
                count++;
                if (id < 0) {
                    Log.d(TAG, "Unable to add word: " + strings[0].trim());
                }
            }
            buffReader.close();
        } catch (IOException e) {
            Log.d(TAG, "Unable to load database");
        }
    }

    private long addTerm(String term, String definition, String category) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TERM_COL, term);
        contentValues.put(DEFINITION_COL, definition);
        contentValues.put(CATEGORY_COL, category);
        Log.d(TAG, "Row #" + count + " populated: " + contentValues);
        return sqlDatabase.insert(DICTIONARY_TABLE, null, contentValues);
    }
}