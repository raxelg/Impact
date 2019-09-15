package com.example.raxelg.impact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText usernameInput, passwordInput;
    TextView signUp;
    private UserDatabaseHelper mUserDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.login_button);
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        signUp = findViewById(R.id.sign_up);

        mUserDatabaseHelper = new UserDatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                Cursor data = mUserDatabaseHelper.getData();

                boolean realAccount = isRealAccount(username,password);

                if (realAccount){
                    Message.message(getApplicationContext(),"Successful Login");
                } else {
                    Message.message(getApplicationContext(),"Invalid username or password");
                }
            };
        });

        signUp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent registerIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    public boolean isRealAccount(String username, String password){
        Map<String,String> data = mUserDatabaseHelper.getUserData();

        if(data.containsKey(username) && data.get(username).equals(password)){
            return true;
        } else {
            return false;
        }
    }

}
