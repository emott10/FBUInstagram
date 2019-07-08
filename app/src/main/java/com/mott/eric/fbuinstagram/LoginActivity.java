package com.mott.eric.fbuinstagram;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin;
    private Button btSignUp;
    private String usrnm;
    private String passwrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            setContentView(R.layout.activity_login);

            etUsername =findViewById(R.id.etUsername);
            etPassword = findViewById(R.id.etPassword);
            btLogin = findViewById(R.id.btnLogin);
            btSignUp = findViewById(R.id.btnSignUp);

            //Makes "Sign up." on the sign up button bold
            String logInBtnString = "Don't have an account? Sign up.";
            SpannableString spanString = new SpannableString(logInBtnString);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 23, 31, 0);
            btSignUp.setText(spanString);

            //Adds a text listener to the edit text boxes to make sure the user has inputted text
            etUsername.addTextChangedListener(textWatcher);
            etPassword.addTextChangedListener(textWatcher);

            btLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login(usrnm, passwrd);

                }
            });

            btSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            usrnm = etUsername.getText().toString();
            passwrd = etPassword.getText().toString();

            btLogin.setEnabled(!usrnm.isEmpty() && !passwrd.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private void login(String usrnm, String passwrd) {
        ParseUser.logInInBackground(usrnm, passwrd, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null) {
                    Log.d("LoginActivity", "Login Successful");

                    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Failed to login please try again", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "Login Failed");
                    e.printStackTrace();
                }
            }
        });
    }
}
