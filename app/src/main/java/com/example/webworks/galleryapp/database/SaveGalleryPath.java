package com.example.webworks.galleryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SaveGalleryPath extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "paths.db";
    private static final String TABLE_NAME = "gallery_path";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "LOCATION";

    public SaveGalleryPath(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,LOCATION TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String galleryPathLocation) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, galleryPathLocation);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, "id=?", new String[]{id});

    }

    public Cursor fetchData(int position) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE ID="+(position+1), null);
        result.moveToFirst();
        return result;
    }

    public int deletePathFromDB(int position) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME,"id=?",new String[] {String.valueOf(position+1)});
    }
}
