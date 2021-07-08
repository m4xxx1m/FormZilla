package ru.raptors.team.formzilla.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.FormStatus;
import ru.raptors.team.formzilla.models.Question;
import ru.raptors.team.formzilla.models.QuestionType;

public class FormsDatabase {
    private static final String DATABASE_NAME = "forms.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "SavedForms";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_QUESTIONS= "questions";
    private static final String COLUMN_STAFF = "staff";
    private static final String COLUMN_USER_ANSWERS_COUNT = "userAnswersCount";
    // userAnswers можно не сохранять, там храняться временные значения

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_STATUS = 1;
    private static final int NUM_COLUMN_TITLE = 2;
    private static final int NUM_COLUMN_QUESTIONS = 3;
    private static final int NUM_COLUMN_STAFF = 4;
    private static final int NUM_COLUMN_USER_ANSWERS_COUNT = 5;

    private SQLiteDatabase database;

    private Context context;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " STRING PRIMARY KEY," +
                    COLUMN_STATUS + " TEXT," +
                    COLUMN_TITLE + " TEXT," +
                    COLUMN_QUESTIONS + " TEXT," +
                    COLUMN_STAFF + " TEXT," +
                    COLUMN_USER_ANSWERS_COUNT + " INTEGER)";

    public FormsDatabase (Context context)
    {
        FormsDatabase.OpenHelper openHelper = new FormsDatabase.OpenHelper(context);
        database = openHelper.getWritableDatabase();
        this.context = context;
    }

    public long insert(Form form) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, form.getID());
        contentValues.put(COLUMN_STATUS, new FormStatus(form.getStatus()).toString());
        contentValues.put(COLUMN_TITLE, form.title);
        contentValues.put(COLUMN_QUESTIONS, form.packQuestions());
        contentValues.put(COLUMN_STAFF, form.packStaff());
        contentValues.put(COLUMN_USER_ANSWERS_COUNT, form.getUserAnswersCount());

        return database.insert(TABLE_NAME, null, contentValues);
    }

    public int update(Form form) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, new FormStatus(form.getStatus()).toString());
        contentValues.put(COLUMN_TITLE, form.title);
        contentValues.put(COLUMN_QUESTIONS, form.packQuestions());
        contentValues.put(COLUMN_STAFF, form.packStaff());
        contentValues.put(COLUMN_USER_ANSWERS_COUNT, form.getUserAnswersCount());

        return database.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?",new String[] { String.valueOf(form.getID())});
    }

    public void deleteAll() {
        database.delete(TABLE_NAME, null, null);
    }

    public void delete(String id) {
        database.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public Form select(String id) {
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        if(cursor.getCount() == 0) return null;
        String status = cursor.getString(NUM_COLUMN_STATUS);
        String title = cursor.getString(NUM_COLUMN_TITLE);
        String questions = cursor.getString(NUM_COLUMN_QUESTIONS);
        String staff = cursor.getString(NUM_COLUMN_STAFF);
        int userAnswersCount = cursor.getInt(NUM_COLUMN_USER_ANSWERS_COUNT);
        Form result = new Form(id);
        result.setStatus(new FormStatus(status).formStatusEnum);
        result.title = title;
        result.unpackQuestions(questions, context);
        result.unpackStaff(staff);
        result.setUserAnswersCount(userAnswersCount);
        return result;
    }

    public ArrayList<Form> selectAll() {
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Form> arrayList = new ArrayList<Form>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                String id = cursor.getString(NUM_COLUMN_ID);
                String status = cursor.getString(NUM_COLUMN_STATUS);
                String title = cursor.getString(NUM_COLUMN_TITLE);
                String questions = cursor.getString(NUM_COLUMN_QUESTIONS);
                String staff = cursor.getString(NUM_COLUMN_STAFF);
                int userAnswersCount = cursor.getInt(NUM_COLUMN_USER_ANSWERS_COUNT);
                Form result = new Form(id);
                result.setStatus(new FormStatus(status).formStatusEnum);
                result.title = title;
                result.unpackQuestions(questions, context);
                result.unpackStaff(staff);
                result.setUserAnswersCount(userAnswersCount);
                arrayList.add(result);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    public boolean hasForm(String id) {
        boolean result = true;
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.getCount() == 0) result = false;
        return result;
    }

    public void close()
    {
        database.close();
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
