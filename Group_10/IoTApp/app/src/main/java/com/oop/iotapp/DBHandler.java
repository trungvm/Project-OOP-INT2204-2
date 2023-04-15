package com.oop.iotapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "userdb";
    private static final int DB_VERSION = 1;
    private static final String USER_TABLE_NAME = "user";
    private static final String USER_ID_COL = "id";
    private static final String USER_NAME_COL = "name";
    private static final String USER_EMAIL_COL = "email";
    private static final String USER_PASSWORD_COL = "password";

    private static final String TEMPERATURE_TABLE_NAME = "temperature";
    private static final String TEMPERATURE_ID_COL = "id";
    private static final String TEMPERATURE_USER_ID_COL = "user_id";
    private static final String TEMPERATURE_STATUS_COL = "status";
    private static final String TEMPERATURE_TEMP_COL = "temper";
    private static final String TEMPERATURE_FANSPEED_COL = "fan_speed";
    private static final String TEMPERATURE_ECO_COL = "eco_mode";
    private static final String TEMPERATURE_TIMER_COL = "timer_mode";
    private static final String TEMPERATURE_TIMER_START_COL = "timer_start";
    private static final String TEMPERATURE_TIMER_STOP_COL = "timer_stop";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String userQuery = "CREATE TABLE " + USER_TABLE_NAME + " ("
                + USER_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_NAME_COL + " TEXT,"
                + USER_EMAIL_COL + " TEXT,"
                + USER_PASSWORD_COL + " TEXT)";
        sqLiteDatabase.execSQL(userQuery);

        String temperatureQuery = "CREATE TABLE " + TEMPERATURE_TABLE_NAME + " ("
                + TEMPERATURE_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TEMPERATURE_USER_ID_COL + " INTEGER,"
                + TEMPERATURE_STATUS_COL + " INTEGER,"
                + TEMPERATURE_TEMP_COL + " INTEGER,"
                + TEMPERATURE_FANSPEED_COL + " INTEGER,"
                + TEMPERATURE_ECO_COL + " INTEGER,"
                + TEMPERATURE_TIMER_COL + " INTEGER,"
                + TEMPERATURE_TIMER_START_COL + " TEXT,"
                + TEMPERATURE_TIMER_STOP_COL + " TEXT)";
        sqLiteDatabase.execSQL(temperatureQuery);
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_NAME_COL, user.getUsername());
        values.put(USER_EMAIL_COL, user.getEmail());
        values.put(USER_PASSWORD_COL, user.getPassword());

        db.insert(USER_TABLE_NAME, null, values);

        db.close();
    }

    public boolean userCheckMatch(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME, null);
        if (cursor.moveToFirst()){
            do {
                String db_email = cursor.getString(2);
                if (db_email.equals(email)){
                    return false;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return true;
    }

    public boolean userCheckMatch(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME, null);
        if (cursor.moveToFirst()){
            do {
                String db_email = cursor.getString(2);
                String db_password = cursor.getString(3);
                if (db_email.equals(email) && db_password.equals(password)){
                    return true;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
