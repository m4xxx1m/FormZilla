package ru.raptors.team.formzilla.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;

import ru.raptors.team.formzilla.models.Filter;
import ru.raptors.team.formzilla.models.Question;
import ru.raptors.team.formzilla.models.QuestionType;

public class FiltersDatabase implements Serializable {
    private static final String DATABASE_NAME = "filters.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "SavedFilters";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FILTER = "filter";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_STAFF = "staff";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_FILTER = 1;
    private static final int NUM_COLUMN_CATEGORY = 2;
    private static final int NUM_COLUMN_STAFF = 3;

    private SQLiteDatabase database;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " STRING PRIMARY KEY," +
                    COLUMN_FILTER + " TEXT," +
                    COLUMN_CATEGORY + " TEXT," +
                    COLUMN_STAFF + " TEXT)";

    public FiltersDatabase (Context context)
    {
        FiltersDatabase.OpenHelper openHelper = new FiltersDatabase.OpenHelper(context);
        database = openHelper.getWritableDatabase();
    }

    public long insert(Filter filter) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, filter.ID);
        contentValues.put(COLUMN_FILTER, filter.filter);
        contentValues.put(COLUMN_CATEGORY, filter.category);
        contentValues.put(COLUMN_STAFF, filter.packStaff());

        return database.insert(TABLE_NAME, null, contentValues);
    }

    public int update(Filter filter) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FILTER, filter.filter);
        contentValues.put(COLUMN_CATEGORY, filter.category);
        contentValues.put(COLUMN_STAFF, filter.packStaff());

        return database.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?",new String[] { String.valueOf(filter.ID)});
    }

    public void deleteAll() {
        database.delete(TABLE_NAME, null, null);
    }

    public void delete(String id) {
        database.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public Filter select(String id) {
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        if(cursor.getCount() == 0) return null;
        String filter = cursor.getString(NUM_COLUMN_FILTER);
        String category = cursor.getString(NUM_COLUMN_CATEGORY);
        String staff = cursor.getString(NUM_COLUMN_STAFF);
        Filter result = new Filter(id);
        result.filter = filter;
        result.category = category;
        result.unpackStaff(staff);

        return result;
    }

    public ArrayList<Filter> selectAll() {
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Filter> arrayList = new ArrayList<Filter>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                String id = cursor.getString(NUM_COLUMN_ID);
                String filter = cursor.getString(NUM_COLUMN_FILTER);
                String category = cursor.getString(NUM_COLUMN_CATEGORY);
                String staff = cursor.getString(NUM_COLUMN_STAFF);
                Filter result = new Filter(id);
                result.filter = filter;
                result.category = category;
                result.unpackStaff(staff);
                arrayList.add(result);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    public boolean hasFilter(String id) {
        boolean result = true;
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
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
