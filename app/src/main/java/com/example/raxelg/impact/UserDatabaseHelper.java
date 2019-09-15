package com.example.raxelg.impact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.sql.SQLInput;
import java.util.HashMap;
import java.util.Map;


public class UserDatabaseHelper extends SQLiteOpenHelper{

    private static final String TAG = "DatabaseHelper";
    public static final String DATABASE = "USER.db";
    public static final String TABLE_NAME = "USERS";

    public static final int DATABASE_VERSION = 0;
    public static final String KEY_ID = "id";
    public static final String FULL_NAME = "Name";
    public static final String EMAIL = "Email";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String TYPE = "Type";
    SQLiteDatabase db;

    public UserDatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 2);
    }

//    private static final String SCRIPT_CREATE_DATABASE =
//            "CREATE TABLE " + TABLE_NAME + " ("
//                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + USERNAME + " TEXT, "
//                    + PASSWORD + " TEXT, "
//                    + TYPE + " TEXT);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FULL_NAME + " TEXT, " + EMAIL + " TEXT, " + USERNAME + " TEXT, " + PASSWORD + " TEXT, " + TYPE +" TEXT)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        SQLiteStatement stmt = db.compileStatement("DROP TABLE IF EXISTS " + TABLE_NAME);
        stmt.execute();
        onCreate(db);
    }

    public boolean addData(String fullName, String email, String username, String password, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO " + TABLE_NAME + "(" + FULL_NAME + " ," + EMAIL + " ," + USERNAME + " ," + PASSWORD + " ," + TYPE + ")" + "VALUES(?,?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(query);
        statement.bindString(1,fullName);
        statement.bindString(2,email);
        statement.bindString(3,username);
        statement.bindString(4,password);
        statement.bindString(5,userType);
        long rowId = statement.executeInsert();

        if (rowId == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemID(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_ID + " FROM " + TABLE_NAME +" WHERE " + USERNAME + " = ?";
        Cursor data = db.rawQuery(query, new String[] {username});
        return data;
    }

    public void updateRow(int id, String columnName, String newData){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + columnName + "=? WHERE " + KEY_ID + " = ?";
        SQLiteStatement statement = db.compileStatement(query);
        statement.bindString(1,newData);
        statement.bindLong(2,id);
        int numberOfRowsAffected = statement.executeUpdateDelete();
    }

    public void deleteUser(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            String sql = "DELETE FROM " + TABLE_NAME +
                    " WHERE " + KEY_ID + " = ?";
            SQLiteStatement statement = db.compileStatement(sql);

            statement.clearBindings();
            statement.bindLong(1, id);
            statement.executeUpdateDelete();

            db.setTransactionSuccessful();

        } catch (SQLException e) {
            Log.w("Exception:", e);
        } finally {
            db.endTransaction();
        }
    }
    
    public Map<String,String> getUserData()
    {
        Map<String,String> usernamePassword = new HashMap<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        data.moveToFirst();

        while (data.isAfterLast() == false)
        {
            usernamePassword.put(data.getString(data.getColumnIndexOrThrow(USERNAME)),data.getString(data.getColumnIndexOrThrow(PASSWORD)));
            data.moveToNext();
        }
        return usernamePassword;
    }

    public Map<String,String> getEmailData()
    {
        Map<String,String> usernamePassword = new HashMap<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        data.moveToFirst();

        while (data.isAfterLast() == false)
        {
            usernamePassword.put(data.getString(data.getColumnIndexOrThrow(EMAIL)),data.getString(data.getColumnIndexOrThrow(PASSWORD)));
            data.moveToNext();
        }
        return usernamePassword;
    }
}