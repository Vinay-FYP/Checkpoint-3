package com.example.firebase_codewithtea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity {
    //VAriables
    TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword;
    Button regBtn, regToLoginBtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        regBtn = findViewById(R.id.reg_btn);
        regToLoginBtn = findViewById(R.id.reg_to_login);
        regName = findViewById(R.id.full_name);
        regUsername = findViewById(R.id.username);
        regEmail = findViewById(R.id.email);
        regPhoneNo = findViewById(R.id.phone_no);
        regPassword = findViewById(R.id.password);

        //Save data in firebase on the button reg click

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseDatabase = FirebaseDatabase.getInstance(); //calls to root node
                dbRef = firebaseDatabase.getReference("Users");

                //Get values from the form
                String name = regName.getEditText().getText().toString();
                String username = regUsername.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneNo = regPhoneNo.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                registerUser(v);

                UserHelper user = new UserHelper(name, username, email, phoneNo, password);
                dbRef.child(username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "data wrote to firebase", Toast.LENGTH_SHORT).show();
                    }
                });



//                Intent intent = new Intent(SignUp.this, UserProfile.class);
//                startActivity(intent);




                //rootNode = FirebaseDatabase.getInstance(); //call to the db root instance /table name
                //reference = rootNode.getReference("users");
                //reference = FirebaseDatabase.getInstance().getReference("Users");
                //reference = FirebaseDatabase.getInstance().getReference();
                //UserHelper user1 = new UserHelper("jeff", "jk123", "jeff@dit.ie", "0867654234", "jeff123");
                //reference.setValue(user1);
                //reference.setValue("First data storage");

                //FirebaseDatabase.getInstance().getReference().child("Users").child("userName").setValue("Vinay");
                // Write a message to the database

                //String key = reference.push().getKey();
                //UserHelper helperClass = new UserHelper(name, username, email, phoneNo, password);
                //reference.child("users").child(key).setValue(helperClass);
                //reference.push().setValue(helperClass);
                //reference.child(key).setValue(helperClass);
                //UserHelper helperClass = new UserHelper(name, username, email, phoneNo, password);

                //reference.child(phoneNo).setValue(helperClass);
            }
        });
    }

    public void registerUser(View view){

        if (!validateName() | !validateEmail() | !validatepassword() | !validatePhoneNo() | !validateUsername()){
            return;
        }

        //Get values from the form
        String name = regName.getEditText().getText().toString();
        String username = regUsername.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phoneNo = regPhoneNo.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        Intent i = new Intent(getApplicationContext(), VerifyPhoneNo.class);
        i.putExtra("phoneNo", phoneNo);
        startActivity(i);

//        UserHelper user = new UserHelper(name, username, email, phoneNo, password);
//        dbRef.child(username).setValue(user);
//
//        Toast.makeText(this, "Successfuly created account", Toast.LENGTH_SHORT).show();
//
//        Intent i = new Intent(getApplicationContext(), Login.class);
//        startActivity(i);
//        finish();

    }

    private Boolean validateName(){
        String val = regName.getEditText().getText().toString();

        if(val.isEmpty()){
            regName.setError("Cannot leave empty");
            return false;
        }else{
            regName.setError(null);//hide error
            regName.setErrorEnabled(false); //remove space
            return true;
        }
    }

    private Boolean validateUsername(){
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace ="\\A\\w{4,20}\\z";
        //String noWhiteSpace = "(?=//s+$)";
        //alternat regex - "\\A\\w{4,20}\\z

        if(val.isEmpty()){
            regUsername.setError("Cannot leave empty");
            return false;
        }
        else if (val.length()>=15){
            regUsername.setError("Username is too long");
            return false;
        }
        else if (!val.matches(noWhiteSpace)){
            regUsername.setError("Cannot have white space");
            return false;
        }
        else{
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
                //"[a-zA-Z0-9._-]+@[a-z]+\\.+[a-]+"; //regex

        if(val.isEmpty()){
            regEmail.setError("Cannot leave empty");
            return false;
        }
        else if (!val.matches(emailPattern)){
            regEmail.setError("Invalid email address");
            return false;
        }
        else{
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo(){
        String val = regPhoneNo.getEditText().getText().toString();

        if(val.isEmpty()){
            regPhoneNo.setError("Cannot leave empty");
            return false;
        }else{
            regPhoneNo.setError(null);//hide error
            regPhoneNo.setErrorEnabled(false); //remove space
            return true;
        }
    }

    private Boolean validatepassword(){
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "\\A\\w{4,20}\\z";
                //"(?=\\s+$)";
                //= "^"+
               //  "(?=.*[0-9])" +           At least 1 digit
              //  "(?=.*[a-z])" +            At last 1 lower case letter
                //  "(?=.*[A-Z])"            At least 1 upper case letter
//               "(?=.[a-zA-Z])" +          //AnyLetter
//                "(?=.*[@#$%^&+=])" +         //At least 1 special character
//                "(?=\\s+$)" +               //No white space
//                ".{4,}" +                    //At least 4 characters
//                "$" ;
        if(val.isEmpty()){
            regPassword.setError("Cannot leave empty");
            return false;
        }
        else if (!val.matches(passwordVal)){
            //regPassword.setError("Password is too weak");
            regPassword.setError("Password can't have whitespace");
            return false;
        }
        else{
            regPassword.setError(null);//hide error
            regPassword.setErrorEnabled(false); //remove space
            return true;
        }
    }


}

