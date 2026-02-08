package com.example.yourcars.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import com.example.yourcars.model.CarModel;

import java.io.ByteArrayOutputStream;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE="YourCars.db";
    private static final int VERSION=2;

    private static final String TABLE_CAR="cars";
    private static final String TABLE_USER="allUsers";
    private static final String COL_ID="id";
    private static final String COL_IMAGE="image";
    private static final String COL_BRAND ="brand";
    private static final String COL_TITLE="title";
    private static final String COL_PRICE="price";
    private static final String COL_DATE="date";
    private static final String COL_DESCRIPTION="description";

    public DBHelper(@Nullable Context context){
        super(context, DATABASE,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table allusers (email TEXT primary key,contact_num TEXT,password TEXT)");

        //  add carmodel tabel
        String createcarsModels = "CREATE TABLE " + TABLE_CAR + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_IMAGE + " BLOB, " +
                COL_BRAND + " TEXT , " +
                COL_TITLE + " TEXT , " +
                COL_PRICE + " TEXT, " +
                COL_DATE + " TEXT , " +
                COL_DESCRIPTION + " TEXT)";
        sqLiteDatabase.execSQL(createcarsModels);
// testdrive booking tabel
        String createBookings = "CREATE TABLE bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT," +
                "contact TEXT," +
                "brand TEXT," +
                "date TEXT," +
                "time TEXT," +
                "status TEXT DEFAULT 'Pending')"; // Pending, Approved, Rejected
        sqLiteDatabase.execSQL(createBookings);

    }

    @Override
    public  void onUpgrade(SQLiteDatabase sqLiteDatabase,int i,int i1){
        sqLiteDatabase.execSQL("drop table if exists allUsers");
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_CAR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS bookings");
        onCreate(sqLiteDatabase);
    }

    public boolean insertUserData(String email,String contact_num, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email",email);
        contentValues.put("contact_num",contact_num);
        contentValues.put("password",password);
        long result = MyDatabase.insert("allUsers",null,contentValues);
        return result != -1;

    }

    public Boolean checkEmail(String email){
        SQLiteDatabase MYDatabase = this.getWritableDatabase();
        Cursor cursor = MYDatabase.rawQuery("Select * from allusers where email =?",new String[]{email});
        return cursor.getCount() > 0;
    }

    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MYDatabase = this.getWritableDatabase();
        Cursor cursor = MYDatabase.rawQuery("Select * from allusers where email = ? and password = ?",new String[]{email,password});
        return cursor.getCount() > 0;
    }

    public boolean insertCarData(byte[] image, String brand, String title, String price, String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_IMAGE, image);
        values.put(COL_BRAND, brand);
        values.put(COL_TITLE, title);
        values.put(COL_PRICE, price);
        values.put(COL_DATE, date);
        values.put(COL_DESCRIPTION, description);
        return db.insert(TABLE_CAR, null, values) != -1;
    }


    public boolean updateCarData(int id, byte[] image, String brand, String title, String price, String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_IMAGE, image);
        values.put(COL_BRAND, brand);
        values.put(COL_TITLE, title);
        values.put(COL_PRICE, price);
        values.put(COL_DATE, date);
        values.put(COL_DESCRIPTION, description);
        return db.update(TABLE_CAR, values, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }


    public Cursor getAllCars(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_CAR,null);

    }
    // Get count of car models
    public int getcarModelsCount() {
        Cursor cursor = getAllCars();
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // Get all users
    public Cursor getAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_USER,null);

    }

    //  smaller thumbnail for images
    public static byte[] getThumbnail(byte[] imageData, int width, int height) {
        if (imageData == null) return null;
        Bitmap original = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        if (original == null) return null;
        Bitmap scaled = Bitmap.createScaledBitmap(original, width, height, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public CarModel getCarById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, image,"+
                        COL_BRAND +",title,date,description,price "+
                        " FROM " + TABLE_CAR + " WHERE id = ?",
                new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE));
            String carbrand = cursor.getString(cursor.getColumnIndexOrThrow(COL_BRAND));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRICE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
            String des = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
            cursor.close();

            return new CarModel(userId, image, carbrand,title,price,date,des);
        }
        return null;
    }

    public boolean deleteCar(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CAR, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // Insert a new booking
    public boolean insertBooking(String name, String email, String contact, String brand, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("contact", contact);
        values.put("brand", brand);
        values.put("date", date);
        values.put("time", time);

        long result = db.insert("bookings", null, values);
        return result != -1;
    }

    // Update booking status
    public boolean updateBookingStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        return db.update("bookings", values, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // Get all pending bookings (or all bookings)
    public Cursor getAllBookings() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM bookings", null);
    }

    // Delete a booking by ID
    public boolean deleteBooking(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("bookings", "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor getCarsByBrand(String brand) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT " + COL_ID + " AS _id, " +
                        COL_IMAGE + ", " +
                        COL_BRAND + ", " +
                        COL_TITLE + ", " +
                        COL_DATE + ", " +
                        COL_DESCRIPTION +
                        " FROM " + TABLE_CAR +
                        " WHERE " + COL_BRAND + " = ?",
                new String[]{brand}
        );

    }
}
