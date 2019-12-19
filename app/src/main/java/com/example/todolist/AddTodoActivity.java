package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.todolist.db.TaskContract;
import com.example.todolist.db.TaskDbHelper;

public class AddTodoActivity extends AppCompatActivity {

    private EditText contentEditText;
    private CheckBox doneCheckBox;
    private Button saveButton;
    public  static final String TODO = "todo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        contentEditText = (EditText) findViewById(R.id.content_et);
        doneCheckBox = (CheckBox) findViewById(R.id.done_cb);
        saveButton = (Button) findViewById(R.id.save_btn);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = contentEditText.getText().toString();
                boolean isDone = doneCheckBox.isChecked();

                Todo todo = new Todo();
                todo.content = content;
                if (isDone == true){
                    todo.done = "Done";
                }else {
                    todo.done = "No";
                }


                Intent intent = new Intent();
                intent.putExtra(TODO, todo);

                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_todo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
