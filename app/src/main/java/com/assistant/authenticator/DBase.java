package com.assistant.authenticator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBase extends SQLiteOpenHelper {
 
    // Logcat tag
    //private static final String LOG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "Authenticator";
 
    // Table Names
    private static final String TABLE_USER = "Users";
    private static final String TABLE_QANDA = "QandA";
 
    // Common column names
    private static final String KEY_ID = "id";
    private static final String USERNAME = "Username";
 
    // QandA Table - column names
    private static final String QNO = "Qno";
    private static final String QUESTION = "Question";
    private static final String ANSWER = "Answer";
 
    // Table Create Statements
    // User table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + USERNAME
            + " TEXT "+")";
 
    // QandA table create statement
    private static final String CREATE_TABLE_QANDA = "CREATE TABLE " + TABLE_QANDA
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + USERNAME + " TEXT," + QNO + " INTEGER, " 
            + QUESTION + " TEXT, " + ANSWER + " TEXT "+")"; 
 
    public DBase(Context context) {
        super(context, Environment.getExternalStorageDirectory()+ File.separator+"Authenticator"+File.separator+DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
        // creating required tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_QANDA);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QANDA);
 
        // create new tables
        onCreate(db);
    }
    
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
    
    public void createUser(User u) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(USERNAME, u.getUserName());
        //values.put(KEY_ID,u.getID());
     
        // insert row
        u.setID(db.insert(TABLE_USER, null, values));
    }
    
    public boolean checkUser(String uname){
    	SQLiteDatabase db = this.getReadableDatabase();
    	if(db.rawQuery("SELECT "+USERNAME+" FROM "+TABLE_USER+" WHERE "+USERNAME+" = "+"\'"+uname+"\'",null).getCount()==0)
    		return false;
    	else return true;
    }
    
    public void createQandA(QandA qa) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(USERNAME, qa.getUserName());
        values.put(QNO, qa.getQno());
        values.put(QUESTION, qa.getQ());
        values.put(ANSWER, qa.getA());
     
        // insert row
        qa.setID(db.insert(TABLE_QANDA, null, values));
    }
    
    public List<QandA> getAllQandA(String username) {
        List<QandA> qa = new ArrayList<QandA>();
        String selectQuery = "SELECT  * FROM " + TABLE_QANDA+" WHERE "+ USERNAME + " = "+"\'"+username+"\'";   
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);   
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                QandA q = new QandA();
                q.setUserName(c.getString((c.getColumnIndex(USERNAME))));
                q.setQno((c.getInt(c.getColumnIndex(QNO))));
                q.setQ(c.getString(c.getColumnIndex(QUESTION)));
                q.setA(c.getString(c.getColumnIndex(ANSWER)));
     
                // adding to todo list
                qa.add(q);
            } while (c.moveToNext());
        }
     
        return qa;
    }
}