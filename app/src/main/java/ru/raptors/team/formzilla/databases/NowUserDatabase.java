package ru.raptors.team.formzilla.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SymbolTable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import ru.raptors.team.formzilla.models.Filter;
import ru.raptors.team.formzilla.models.User;

public class NowUserDatabase implements Serializable {
    private static final String DATABASE_NAME = "nowUser.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "SavedNowUser";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_USER_ID = "user_id";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_LOGIN = 1;
    private static final int NUM_COLUMN_PASSWORD = 2;
    private static final int NUM_COLUMN_USER_ID = 3;

    private SQLiteDatabase database;
    private Context context;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_LOGIN + " TEXT," +
                    COLUMN_PASSWORD + " TEXT," +
                    COLUMN_USER_ID + " TEXT)";

    public NowUserDatabase (Context context)
    {
        NowUserDatabase.OpenHelper openHelper = new NowUserDatabase.OpenHelper(context);
        database = openHelper.getWritableDatabase();
        this.context = context;
    }

    public long insert(String login, String password, String userID) {
        ContentValues contentValues = new ContentValues();
        if(!hasNowUser()) {
            contentValues.put(COLUMN_LOGIN, login);
            contentValues.put(COLUMN_PASSWORD, password);
            contentValues.put(COLUMN_USER_ID, userID);
        }
        return database.insert(TABLE_NAME, null, contentValues);

/*        else
        {
            update(login, password, userID);
            return -1;
        }*/
    }

    public int update(String login, String password, String userID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LOGIN, login);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_USER_ID, userID);

        return database.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?",new String[] { String.valueOf(1)});
    }

    public void deleteAll() {
        database.delete(TABLE_NAME, null, null);
    }

    public void delete(String id) {
        database.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public User select() {
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(1)}, null, null, null);

        cursor.moveToFirst();
        if(cursor.getCount() == 0) return null;
        String login = cursor.getString(NUM_COLUMN_LOGIN);
        String password = cursor.getString(NUM_COLUMN_PASSWORD);
        String userID = cursor.getString(NUM_COLUMN_USER_ID);

        UsersDatabase usersDatabase = new UsersDatabase(context);
        User result = usersDatabase.select(userID);

        return result;
    }

    public boolean hasNowUser() {
        boolean result = true;
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor.getCount() == 0) result = false;
        return result;
    }

    private class OpenHelper extends SQLiteOpenHelper implements Serializable {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
