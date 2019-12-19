package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.db.TaskContract;
import com.example.todolist.db.TaskDbHelper;

import java.util.ArrayList;

public class TodoListActivity extends AppCompatActivity {
    private static final String TAG = "TodoListActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(android.R.id.list);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE, new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null,null,null,null,null);
        while (cursor.moveToNext()){
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);

            Log.d(TAG,"Task: "+cursor.getString(idx));
        }
        cursor.close();
        db.close();
        updateGUI();
    }
    /**
     * Este método tiene como objetivo refrescar el layout donde se mostrará la lista.
     * */
    public void updateGUI(){
        /**
         * @param taskList es una variable de tipo ArrayList. Tiene como función guardar datos dentro de la lista, datos de tipo string.
         * @param taskList2 es una variable de tipo ArrayList. Tiene como función guardar datos dentro de la lista, datos de tipo string.
         * @param db es un varaible instanciada a un objeto de tipo SQLiteDatabase, es una variable con la función de llamar metodos para ejecutar en la BD.
         * */
        ArrayList<String> taskList = new ArrayList<>();
        ArrayList<String> taskList2 = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_CHECK},
                null,null,null,null,null);
        while (cursor.moveToNext()){
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            int ids = cursor.getColumnIndex(TaskContract.TaskEntry.COL_CHECK);
            taskList.add(cursor.getString(idx));
            taskList2.add(cursor.getString(ids));
        }
        if (mAdapter == null){
            mAdapter = new ArrayAdapter<>(this,R.layout.list_item,R.id.txt_list,taskList);
            mTaskListView.setAdapter(mAdapter);
        }else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){


        getMenuInflater().inflate(R.menu.menu_todo_list, menu);


        return true;
    }

    /**
     * Método en el cual se recibe un parametro el cual esta relacionado directamente con la lista de despliegue de opciones.
     * @param item este parámetro tiene como objetivo recibir el id para la selección de opción.
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.action_logout:
                finish();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_new:

                Intent intent2 = new Intent(getApplicationContext(), AddTodoActivity.class);
                startActivityForResult(intent2, REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
    /**
     * Este método tiene como objetivo recibir el contenido de la tarea que se agregara en la lista, como el contenido de texto y el checkbox.
     * @param  requestCode este parámetro tiene como funcion recibir un código para devolver un tipo de código, dependiendo de este resultado se ejecutara un acción.
     * @param  resultCode este parámetro tiene como objetivo reciibir un tipo de resultado del codigo que tenga el parametro "requestCode".
      */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Todo todo = (Todo) data.getSerializableExtra(AddTodoActivity.TODO);
                String task = String.valueOf(todo.content);
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                values.put(TaskContract.TaskEntry.COL_CHECK, task);
                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null,values,SQLiteDatabase.CONFLICT_REPLACE);
                db.close();
                updateGUI();
                Toast.makeText(getApplicationContext(), "Result ok! content:"+todo.content,Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Result canceled",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
