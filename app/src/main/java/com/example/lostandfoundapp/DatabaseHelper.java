package com.example.lostandfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LostFound.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "adverts";

    public static final String COL_ID = "id";
    public static final String COL_TYPE = "type";
    public static final String COL_NAME = "name";
    public static final String COL_PHONE = "phone";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_LOCATION = "location";
    public static final String COL_CATEGORY = "category";
    public static final String COL_IMAGE = "image";
    public static final String COL_TIMESTAMP = "timestamp";
    public static final String COL_LATITUDE = "latitude";
    public static final String COL_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TYPE + " TEXT, " +
                COL_NAME + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_IMAGE + " TEXT, " +
                COL_TIMESTAMP + " TEXT, " +
                COL_LATITUDE + " REAL, " +
                COL_LONGITUDE + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertAdvert(String type, String name, String phone, String description,
                                String location, String category, String image, String timestamp,
                                double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TYPE, type);
        values.put(COL_NAME, name);
        values.put(COL_PHONE, phone);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_LOCATION, location);
        values.put(COL_CATEGORY, category);
        values.put(COL_IMAGE, image);
        values.put(COL_TIMESTAMP, timestamp);
        values.put(COL_LATITUDE, latitude);
        values.put(COL_LONGITUDE, longitude);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    private Advert createAdvertFromCursor(Cursor cursor) {
        return new Advert(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TIMESTAMP)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LONGITUDE))
        );
    }

    public ArrayList<Advert> getAllAdverts() {
        ArrayList<Advert> advertList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                advertList.add(createAdvertFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return advertList;
    }

    public ArrayList<Advert> getAdvertsByCategory(String category) {
        ArrayList<Advert> advertList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COL_CATEGORY + " = ?" +
                        " ORDER BY " + COL_ID + " DESC",
                new String[]{category}
        );

        if (cursor.moveToFirst()) {
            do {
                advertList.add(createAdvertFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return advertList;
    }

    public Advert getAdvertById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        Advert advert = null;

        if (cursor.moveToFirst()) {
            advert = createAdvertFromCursor(cursor);
        }

        cursor.close();
        return advert;
    }

    public boolean deleteAdvert(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_NAME,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }
}