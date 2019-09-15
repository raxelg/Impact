package com.example.raxelg.impact;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class RegistrationActivity extends AppCompatActivity{

    Button registerButton;
    EditText nameInput, emailInput,usernameInput, passwordInput, retypePasswordInput;
    private UserDatabaseHelper mUserDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = findViewById(R.id.register_button);
        nameInput = findViewById(R.id.full_name);
        emailInput = findViewById(R.id.email);
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        retypePasswordInput = findViewById(R.id.retype_password);

        mUserDatabaseHelper = new UserDatabaseHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInput.getText().toString();
                String email = emailInput.getText().toString();
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String retypePassword = retypePasswordInput.getText().toString();

                Cursor data =  mUserDatabaseHelper.getData();

                if (textFieldsFilled(name,email,username,password,retypePassword)) {
                    if (password.equals(retypePassword)) {
                        if(usernameExists(username)){
                            Message.message(getApplicationContext(), "Username is in use");
                        } else if (emailExists(email)) {
                            Message.message(getApplicationContext(), "Account with email exists");
                        } else {
                            AddUser(name, email, username, password, "IMPACTER");
                            Message.message(getApplicationContext(), "Successfully registered!");
                            nameInput.setText("");
                            emailInput.setText("");
                            usernameInput.setText("");
                            passwordInput.setText("");
                            retypePasswordInput.setText("");
                            //change activities **logged in**
                        }
                    } else {
                        Message.message(getApplicationContext(), "Passwords don't match");
                    }
                } else {
                    Message.message(getApplicationContext(),"Please fill out all the information");
                }
            };
        });
    }

    public void AddUser(String name, String email, String username, String password, String userType) {
        boolean insertUser = mUserDatabaseHelper.addData(name, email, username,password,userType);
        if (insertUser) {
            Message.message(getApplicationContext(),"Successfully registered");
        } else {
            Message.message(getApplicationContext(),"Something went wrong! Please try again.");
        }
    }

    public static boolean textFieldsFilled(String name, String email, String username, String password, String retypePassword){
        if (name.length() != 0 && email.length() != 0 && username.length() != 0 && password.length() != 0 && retypePassword.length() != 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean usernameExists(String username){
        Map<String,String> data = mUserDatabaseHelper.getUserData();

        if(data.containsKey(username)){
            return true;
        } else {
            return false;
        }
    }

    public boolean emailExists(String email){
        Map<String,String> data = mUserDatabaseHelper.getEmailData();

        if(data.containsKey(email)){
            return true;
        } else {
            return false;
        }
    }
}
