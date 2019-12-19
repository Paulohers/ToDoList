package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todolist.db.TaskContract;
import com.example.todolist.db.TaskDbHelper;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button button;
    private EditText usernameEditText;
    private EditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.Login_btn);
        button.setOnClickListener(this);
        usernameEditText = findViewById(R.id.username_et);
        passwordEditText = findViewById(R.id.password_et);
    }

    @Override
    public void onClick(View view){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean isError = false;
        if (TextUtils.isEmpty(username)){
            usernameEditText.setError(getString(R.string.this_field_is_required));
            isError = true;
        }
        if (TextUtils.isEmpty(password)){
            passwordEditText.setError(getString(R.string.this_field_is_required));
            isError = true;
        }
        if(!isError){
            login(username,password);

        }
    }

    private void login(String username, String password){

            AsyncTask<String, Integer, Boolean> asyncTask = new AsyncTask<String, Integer, Boolean>() {
                @Override
                protected Boolean doInBackground(String... strings) {
                    String username = strings[0];
                    String password = strings[1];
                    for (int i = 0; i < 100; i++) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        publishProgress(i);
                    }

                    return username.equals("test") && password.equals("test");

                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                    Log.d(TAG, "Progress:"+values[0]);
                    button.setText(String.valueOf(values[0]));
                }

                @Override
                protected void onPostExecute(Boolean logged) {
                    super.onPostExecute(logged);
                    button.setEnabled(true);
                    if (logged){
                        Toast.makeText(getApplicationContext(),"Login OK",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),TodoListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    button.setEnabled(false);
                }
            };
            asyncTask.execute(username, password);
    }
}
