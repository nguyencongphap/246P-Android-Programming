package com.example.todolistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoAppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "todo";
    private static final int DB_VERSION = 1;

    ToDoAppDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TASK (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TITLE TEXT," +
                "DESCRIPTION TEXT," +
                "STATUS TEXT" +
                ");");

        insertTask(db, "Task 1", "Description 1", Task.TODO);
        insertTask(db, "Task 2", "Description 2", Task.DOING);
        insertTask(db, "Task 3", "Description 3", Task.DONE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private static void insertTask(SQLiteDatabase db, String title, String description,
                                   String status) {
        ContentValues taskValues = new ContentValues();
        taskValues.put("TITLE", title);
        taskValues.put("DESCRIPTION", description);
        taskValues.put("STATUS", status);
        db.insert("TASK", null, taskValues);
    }



}
