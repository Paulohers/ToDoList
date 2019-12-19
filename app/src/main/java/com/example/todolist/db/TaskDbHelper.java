package com.example.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDbHelper extends SQLiteOpenHelper {

    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    /**
     * Este método tiene como objetivo recibir un parámetro para acceder a la base de datos y ejecutar un query.
     * @param  db este parametro tiene como objetivo para ejecutar metodos o el query dentro en la BD.*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COL_CHECK + " TEXT NOT NULL); ";

        db.execSQL(createTable);
    }

    /**
     * Este método tiene como objetivo borrar la tabla si llega  a existir en el contexto.
     * @param db este parámetro tiene como función acceder a los metodos de la BD, y recibir la tabla.
     * @param oldVersion este parámetro tiene como función manejar la versión de la BD.
     * @param newVersion  este parámetro tiene como función manejar la version mas nueva de la BD, para comparar con las otras versiones.*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }
}
