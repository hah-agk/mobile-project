package com.example.currencyconverter.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper {
//    public static final String DB_NAME = "users.db";
//
//    public DatabaseHelper(Context context) {
//        super(context, DB_NAME, null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(
//                "CREATE TABLE users (" +
//                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                        "email TEXT UNIQUE, " +
//                        "password TEXT, " +
//                        "username TEXT, " +
//                        "country TEXT, " +
//                        "profile_pic BLOB" +
//                        ")"
//        );
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS users");
//        onCreate(db);
//    }
//
//    // Insert user
//    public boolean registerUser(String email, String password) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put("email", email);
//        cv.put("password", password);
//
//        long result = db.insert("users", null, cv);
//        return result != -1; // returns true if inserted successfully
//    }
//
//    // Check login credentials
//    public boolean loginUser(String email, String password) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(
//                "SELECT * FROM users WHERE email=? AND password=?",
//                new String[]{email, password}
//        );
//
//        boolean exists = cursor.getCount() > 0;
//        cursor.close();
//        return exists;
//    }
}
