package com.example.restaurant_reviews;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restaurants.db";
    private static final String TABLE_NAME = "Restaurants";
    private static final String COLUMN_ID = "idRestaurant";
    private static final int DATABASE_VERSION = 1;

    public RestaurantDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nomRestaurant TEXT," +
                "adresseRestaurant TEXT," +
                "qualitePlats TEXT," +
                "qualiteService TEXT," +
                "experience REAL," +
                "prix TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertRestaurant(String nom, String adresse, String qualitePlats, String qualiteService, float experience, String prix) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nomRestaurant", nom);
        values.put("adresseRestaurant", adresse);
        values.put("qualitePlats", qualitePlats);
        values.put("qualiteService", qualiteService);
        values.put("experience", experience);
        values.put("prix", prix);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<String[]> getAllRestaurants() {
        List<String[]> restaurantList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String nom = cursor.getString(cursor.getColumnIndex("nomRestaurant"));
                @SuppressLint("Range") String adresse = cursor.getString(cursor.getColumnIndex("adresseRestaurant"));
                @SuppressLint("Range") String qualitePlats = cursor.getString(cursor.getColumnIndex("qualitePlats"));
                @SuppressLint("Range") String qualiteService = cursor.getString(cursor.getColumnIndex("qualiteService"));
                @SuppressLint("Range") String experience = cursor.getString(cursor.getColumnIndex("experience"));
                @SuppressLint("Range") String prix = cursor.getString(cursor.getColumnIndex("prix"));

                restaurantList.add(new String[]{id, nom, adresse, qualitePlats, qualiteService, experience, prix});
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return restaurantList;
    }

    public boolean deleteRestaurant(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{id});
        db.close();
        return deleted > 0;
    }

    public boolean updateRestaurant(String id, String adresse, String qualitePlats, String qualiteService, float experience, String prix) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("adresseRestaurant", adresse);
        values.put("qualitePlats", qualitePlats);
        values.put("qualiteService", qualiteService);
        values.put("experience", experience);
        values.put("prix", prix);
        int updated = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{id});
        db.close();
        return updated > 0;
    }
}