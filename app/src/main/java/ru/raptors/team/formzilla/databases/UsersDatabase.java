package ru.raptors.team.formzilla.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;

import ru.raptors.team.formzilla.models.Gender;
import ru.raptors.team.formzilla.models.User;

public class UsersDatabase {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "SavedUsers";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_LAST_NAME = "lastName";
    private static final String COLUMN_COMPANY = "company";
    private static final String COLUMN_COMPANY_ID = "companyID";
    private static final String COLUMN_FORMS = "forms";
    private static final String COLUMN_STAFF = "staff";
    private static final String COLUMN_FILTERS = "filters";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_GENDER = 1;
    private static final int NUM_COLUMN_FIRST_NAME = 2;
    private static final int NUM_COLUMN_LAST_NAME = 3;
    private static final int NUM_COLUMN_COMPANY = 4;
    private static final int NUM_COLUMN_COMPANY_ID = 5;
    private static final int NUM_COLUMN_FORMS = 6;
    private static final int NUM_COLUMN_STAFF = 7;
    private static final int NUM_COLUMN_FILTERS = 8;
    private static final int NUM_COLUMN_LOGIN = 9;
    private static final int NUM_COLUMN_PASSWORD = 10;

    private SQLiteDatabase database;

    private Context context;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " STRING PRIMARY KEY," +
                    COLUMN_GENDER + " TEXT," +
                    COLUMN_FIRST_NAME + " TEXT," +
                    COLUMN_LAST_NAME + " TEXT," +
                    COLUMN_COMPANY + " TEXT," +
                    COLUMN_COMPANY_ID + " TEXT," +
                    COLUMN_FORMS + " TEXT," +
                    COLUMN_STAFF + " TEXT," +
                    COLUMN_FILTERS + " TEXT," +
                    COLUMN_LOGIN + " TEXT," +
                    COLUMN_PASSWORD + " TEXT)";

    public UsersDatabase (Context context)
    {
        UsersDatabase.OpenHelper openHelper = new UsersDatabase.OpenHelper(context);
        database = openHelper.getWritableDatabase();
        this.context = context;
    }

    public long insert(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, user.getID());
        if(user.getGender() != null) contentValues.put(COLUMN_GENDER, new Gender(user.getGender()).toString());
        contentValues.put(COLUMN_FIRST_NAME, user.getFirstName());
        contentValues.put(COLUMN_LAST_NAME, user.getLastName());
        contentValues.put(COLUMN_COMPANY, user.getCompany());
        contentValues.put(COLUMN_COMPANY_ID, user.getCompanyID());
        if(user.getLogin() != null && !user.getLogin().isEmpty()) contentValues.put(COLUMN_LOGIN, user.getLogin());
        if(user.getPassword() != null && !user.getPassword().isEmpty()) contentValues.put(COLUMN_PASSWORD, user.getPassword());
        if(user.getForms() != null && !user.getForms().isEmpty()) contentValues.put(COLUMN_FORMS, user.packForms());
        if(user.getStaff() != null && !user.getStaff().isEmpty()) contentValues.put(COLUMN_STAFF, user.packStaff());
        if(user.getFilters() != null && !user.getFilters().isEmpty())contentValues.put(COLUMN_FILTERS, user.packFilters(context));

        return database.insert(TABLE_NAME, null, contentValues);
    }

    public int update(User user) {
        ContentValues contentValues = new ContentValues();
        if(user.getGender() != null) contentValues.put(COLUMN_GENDER, new Gender(user.getGender()).toString());
        contentValues.put(COLUMN_FIRST_NAME, user.getFirstName());
        contentValues.put(COLUMN_LAST_NAME, user.getLastName());
        contentValues.put(COLUMN_COMPANY, user.getCompany());
        contentValues.put(COLUMN_COMPANY_ID, user.getCompanyID());
        if(user.getLogin() != null && !user.getLogin().isEmpty()) contentValues.put(COLUMN_LOGIN, user.getLogin());
        if(user.getPassword() != null && !user.getPassword().isEmpty()) contentValues.put(COLUMN_PASSWORD, user.getPassword());
        if(user.getForms() != null && !user.getForms().isEmpty()) contentValues.put(COLUMN_FORMS, user.packForms());
        if(user.getStaff() != null && !user.getStaff().isEmpty()) contentValues.put(COLUMN_STAFF, user.packStaff());
        if(user.getFilters() != null && !user.getFilters().isEmpty()) contentValues.put(COLUMN_FILTERS, user.packFilters(context));

        return database.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?",new String[] { String.valueOf(user.getID())});
    }

    public void deleteAll() {
        database.delete(TABLE_NAME, null, null);
    }

    public void delete(String id) {
        database.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public User select(String id) {
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        if(cursor.getCount() == 0) return null;
        String gender = cursor.getString(NUM_COLUMN_GENDER);
        String firstName = cursor.getString(NUM_COLUMN_FIRST_NAME);
        String lastName = cursor.getString(NUM_COLUMN_LAST_NAME);
        String company = cursor.getString(NUM_COLUMN_COMPANY);
        String companyID = cursor.getString(NUM_COLUMN_COMPANY_ID);
        String forms = cursor.getString(NUM_COLUMN_FORMS);
        String staff = cursor.getString(NUM_COLUMN_STAFF);
        String filters = cursor.getString(NUM_COLUMN_FILTERS);
        String login = cursor.getString(NUM_COLUMN_LOGIN);
        String password = cursor.getString(NUM_COLUMN_PASSWORD);
        User result = new User(id);
        if(gender != null) result.setGender(new Gender(gender).getGender());
        result.setFirstName(firstName);
        result.setLastName(lastName);
        result.setCompany(company);
        result.setCompanyID(companyID);
        result.setLogin(login);
        result.setPassword(password);
        if(forms != null && !forms.isEmpty()) result.unpackForms(forms, context);
        if(staff != null && !staff.isEmpty()) result.unpackStaff(staff, context);
        if(filters != null && !filters.isEmpty()) result.unpackFilters(filters, context);
        return result;
    }

    public ArrayList<User> selectAll() {
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<User> arrayList = new ArrayList<User>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                String id = cursor.getString(NUM_COLUMN_ID);
                String gender = cursor.getString(NUM_COLUMN_GENDER);
                String firstName = cursor.getString(NUM_COLUMN_FIRST_NAME);
                String lastName = cursor.getString(NUM_COLUMN_LAST_NAME);
                String company = cursor.getString(NUM_COLUMN_COMPANY);
                String companyID = cursor.getString(NUM_COLUMN_COMPANY_ID);
                String forms = cursor.getString(NUM_COLUMN_FORMS);
                String staff = cursor.getString(NUM_COLUMN_STAFF);
                String filters = cursor.getString(NUM_COLUMN_FILTERS);
                String login = cursor.getString(NUM_COLUMN_LOGIN);
                String password = cursor.getString(NUM_COLUMN_PASSWORD);
                User result = new User(id);
                if(gender != null) result.setGender(new Gender(gender).getGender());
                result.setFirstName(firstName);
                result.setLastName(lastName);
                result.setCompany(company);
                result.setCompanyID(companyID);
                result.setLogin(login);
                result.setPassword(password);
                if(forms != null && !forms.isEmpty()) result.unpackForms(forms, context);
                if(staff != null && !staff.isEmpty()) result.unpackStaff(staff, context);
                if(filters != null && !filters.isEmpty()) result.unpackFilters(filters, context);
                arrayList.add(result);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    public boolean hasUser(String id) {
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
