package com.example.webworks.galleryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SaveGalleryPath extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "paths.db";
    private static final String TABLE_NAME = "gallery_path";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "GALLERY_PATH_LASTNAME";
    private static final String COL_3 = "GALLERY_PATH_LOCATION";


    public SaveGalleryPath(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,GALLERY_PATH_LASTNAME TEXT,GALLERY_PATH_LOCATION TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String galleryPath,String galleryLastName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,galleryLastName);
        contentValues.put(COL_3, galleryPath);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public void deleteData(String lastName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME + " WHERE " + COL_2 + " = '"+lastName+"'");
        db.close();
    }

    public Cursor fetchPathWithLastName(String lastName) {
        SQLiteDatabase db = getWritableDatabase();
            Cursor result = db.rawQuery("SELECT "+COL_3+" FROM "+TABLE_NAME+" WHERE "+COL_2+ "= '"+lastName+"'", null);
        return result;
    }

    public int deletePathFromDB(int position) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME,"id=?",new String[] {String.valueOf(position+1)});
    }
}
