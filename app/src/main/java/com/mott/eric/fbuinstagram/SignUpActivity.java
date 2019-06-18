package com.mott.eric.fbuinstagram;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;
    private Button btSubmit;
    private String name;
    private String usrnm;
    private String psswrd;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etName = findViewById(R.id.etSignUpName);
        etUsername = findViewById(R.id.etSignUpHandle);
        etPassword = findViewById(R.id.etSignUpPassword);
        etEmail = findViewById(R.id.etSignUpEmail);
        btSubmit = findViewById(R.id.btnSubmitSignUp);

        etName.addTextChangedListener(textWatcher);
        etUsername.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);



        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isValidEmail(email)){
                    Toast.makeText(SignUpActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                }
                else {
                    signUp(name, usrnm, psswrd, email);
                }
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            name = etName.getText().toString();
            usrnm = etUsername.getText().toString();
            psswrd = etPassword.getText().toString();
            email = etEmail.getText().toString();

            btSubmit.setEnabled(!name.isEmpty() && !usrnm.isEmpty() && !psswrd.isEmpty() && !email.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private void signUp(String name, final String usrnm, final String psswrd, String email){
        // Create the ParseUser
        ParseUser user = new ParseUser();

        // Set core properties
        user.setUsername(usrnm);
        user.setPassword(psswrd);
        user.setEmail(email);
        // Set custom properties
        user.put("name", name);

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    //Sign up successful
                    Log.d("SignUpActivity", "Sign up Successful");

                    login(usrnm,psswrd);
                }
                else{
                    //Sign up failed
                    Log.e("SignUpActivity", "Sign up Failed");
                    e.printStackTrace();
                }
            }
        });
    }

    private void login(String usrnm, String passwrd) {
        ParseUser.logInInBackground(usrnm, passwrd, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null) {
                    Log.d("SignUpActivity", "Login Successful");

                    final Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Log.e("SignUpActivity", "Login Failed");
                    e.printStackTrace();
                }
            }
        });
    }

    //email validation
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
