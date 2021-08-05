package com.example.firebase_codewithtea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile extends AppCompatActivity {

    TextInputLayout fullName, email, phoneNo, password;
    TextView fullNameLabel, usernameLabel;

    String user_username, user_name, user_email, user_phoneNo, user_password;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);

        ref = FirebaseDatabase.getInstance().getReference("users");

        fullNameLabel = findViewById(R.id.full_name);
        usernameLabel = findViewById(R.id.username_label);
        fullName = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.email_profile);
        phoneNo = findViewById(R.id.phoneNo_profile);
        password = findViewById(R.id.password_profile);

        //show all data
        showAllUserData();
    }

    private void showAllUserData() {
        Intent i = getIntent();
        user_username = i.getStringExtra("username");
        user_name = i.getStringExtra("name");
        user_email = i.getStringExtra("email");
        user_phoneNo = i.getStringExtra("phoneNo");
        user_password = i.getStringExtra("password");

        fullNameLabel.setText(user_name);
        usernameLabel.setText(user_username);
        fullName.getEditText().setText(user_name);
        email.getEditText().setText(user_email);
        phoneNo.getEditText().setText(user_phoneNo);
        password.getEditText().setText(user_password);
    }

    public void update(View view) {

        if (isNameChanged() || isPasshwordChanged()) {
            Toast.makeText(this, "Data Updated", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Data cannot be Updated", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNameChanged() {
        if (!user_name.equals(fullName.getEditText().getText().toString())) {
            ref.child(user_name).child("name").setValue(fullName.getEditText().getText().toString());
            user_name.equals(fullName.getEditText().getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isPasshwordChanged() {

        if (!user_password.equals(password.getEditText().getText().toString())) {
            ref.child(user_name).child("password").setValue(password.getEditText().getText().toString());
            user_password.equals(password.getEditText().getText().toString());
            return true;
        } else {
            return false;
        }
    }

}