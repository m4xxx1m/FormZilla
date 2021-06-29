package ru.raptors.team.formzilla.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;

import ru.raptors.team.formzilla.models.Question;
import ru.raptors.team.formzilla.models.QuestionType;

public class QuestionsDatabase implements Serializable {
    private static final String DATABASE_NAME = "questions.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "SavedQuestions";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_QUESTION_TYPE = "questionType";
    private static final String COLUMN_ON_ANSWERED_LISTENERS = "onAnsweredListeners";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_QUESTION = 1;
    private static final int NUM_COLUMN_QUESTION_TYPE = 2;
    private static final int NUM_COLUMN_ON_ANSWERED_LISTENERS = 3;

    private SQLiteDatabase database;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " STRING PRIMARY KEY," +
                    COLUMN_QUESTION + " TEXT," +
                    COLUMN_QUESTION_TYPE + " TEXT," +
                    COLUMN_ON_ANSWERED_LISTENERS + " TEXT)";

    public QuestionsDatabase (Context context)
    {
        OpenHelper openHelper = new OpenHelper(context);
        database = openHelper.getWritableDatabase();
    }

    public long insert(Question question) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, question.getID());
        contentValues.put(COLUMN_QUESTION, question.question);
        contentValues.put(COLUMN_QUESTION_TYPE, new QuestionType(question.questionType).toString());
        contentValues.put(COLUMN_ON_ANSWERED_LISTENERS, question.packOnAnsweredListeners());

        return database.insert(TABLE_NAME, null, contentValues);
    }

    public int update(Question question) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, question.getID());
        contentValues.put(COLUMN_QUESTION, question.question);
        contentValues.put(COLUMN_QUESTION_TYPE, new QuestionType(question.questionType).toString());
        contentValues.put(COLUMN_ON_ANSWERED_LISTENERS, question.packOnAnsweredListeners());

        return database.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?",new String[] { String.valueOf(question.getID())});
    }

    public void deleteAll() {
        database.delete(TABLE_NAME, null, null);
    }

    public void delete(String id) {
        database.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public Question select(String id) {
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        if(cursor.getCount() == 0) return null;
        String question = cursor.getString(NUM_COLUMN_QUESTION);
        String questionType = cursor.getString(NUM_COLUMN_QUESTION_TYPE);
        String onAnsweredListeners = cursor.getString(NUM_COLUMN_ON_ANSWERED_LISTENERS);
        Question result = new Question(id);
        result.question = question;
        result.questionType = new QuestionType(questionType).questionTypeEnum;
        result.unpackOnAnsweredListeners(onAnsweredListeners);
        return result;
    }

    public ArrayList<Question> selectAll() {
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Question> arrayList = new ArrayList<Question>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                String id = cursor.getString(NUM_COLUMN_ID);
                String question = cursor.getString(NUM_COLUMN_QUESTION);
                String questionType = cursor.getString(NUM_COLUMN_QUESTION_TYPE);
                String onAnsweredListeners = cursor.getString(NUM_COLUMN_ON_ANSWERED_LISTENERS);
                Question result = new Question(id);
                result.question = question;
                result.questionType = new QuestionType(questionType).questionTypeEnum;
                result.unpackOnAnsweredListeners(onAnsweredListeners);
                arrayList.add(result);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    public boolean hasQuestion(String id) {
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
