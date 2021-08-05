package com.example.firebase_codewithtea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button callSignUp, login_btn;
    ImageView imageView;
    TextView logotext, slogantext;
    TextInputLayout username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //REmove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        callSignUp = findViewById(R.id.signup);
        login_btn = findViewById(R.id.login);
        imageView = findViewById(R.id.logoImage);
        logotext = findViewById(R.id.logoName);
        slogantext = findViewById(R.id.sloganName);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);


        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, SignUp.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View,String>(imageView, "logo_image");
                pairs[1] = new Pair<View,String>(logotext, "logo_text");
                pairs[2] = new Pair<View,String>(slogantext, "logo_desc");
                pairs[3] = new Pair<View,String>(username, "username_tran");
                pairs[4] = new Pair<View,String>(password, "password_tran");
                pairs[5] = new Pair<View,String>(login_btn, "button_tran");
                pairs[6] = new Pair<View,String>(callSignUp, "login_signup_tran");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                    startActivity(i, options.toBundle());//tobundle gets the animation and intent calls the sign up screen
                }

            }
        });
    }

    public Boolean validateUsername(){
        String val = username.getEditText().getText().toString();

        if(val.isEmpty()){
            username.setError(" Cannot be empty");
            return false;
        }
        else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean validatePassword(){
        String val = password.getEditText().getText().toString();

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true ;
        }
    }

    public void loginUser(View view){

        //Validate the info
        if(!validateUsername() | !validatePassword()) {
            return;
        }
        else{
            isUser();
        }
    }

    private void isUser(){
        String userEnteredUsername = username.getEditText().getText().toString().trim();
        String userEnteredPassword = password.getEditText().getText().toString().trim();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");

        //Check if firebase to see if the user has entered the same name as the one stored in the users node
        Query checkUser = dbRef.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //fetch password of the user with this username
                if(snapshot.exists()){

                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if(passwordFromDB.equals(userEnteredPassword)){

                        username.setError(null);
                        username.setErrorEnabled(false);

                        //fetch teh users data stored in these variables
                        String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String poneNoFromDB = snapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);

                        Intent i = new Intent(getApplicationContext(), UserProfile.class);
                        i.putExtra("name", nameFromDB);
                        i.putExtra("username", usernameFromDB);
                        i.putExtra("phoneNo", poneNoFromDB);
                        i.putExtra("email", emailFromDB);
                        startActivity(i);
                    }
                    else{
                        password.setError("Incorrect Password");
                        password.requestFocus();
                    }
                }
                else{
                    username.setError("User doesn't exist");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //CallSignupScreen
    public void callSignUpScreen(){

    }
}