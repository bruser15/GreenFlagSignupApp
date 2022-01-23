package com.example.greenflagsignup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText passwordRepeat;
    private ConstraintLayout emailErrorBox;
    private ConstraintLayout passwordErrorBox;
    private TextView passwordErrorText;
    private Boolean emailCheck;
    private Boolean passwordCheck;
    private Boolean passwordMatch;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Navigate back to the splash screen
        ImageButton backButt = findViewById(R.id.ibtn_back_splash);
        backButt.setOnClickListener(view -> {
            Intent back = new Intent();
            back.setClass(SignupActivity.this, MainActivity.class);
            startActivity(back);
        });

        // Email input check
        emailErrorBox = findViewById(R.id.cl_email_error);
        emailCheck = false;
        email = findViewById(R.id.et_email);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(checkEmail()){
                        emailIsCorrect();
                    }
                    else{
                        emailError();
                    }
                }
            }
        });


        passwordErrorBox = findViewById(R.id.cl_password_error);
        passwordErrorText = findViewById(R.id.tv_error_password);
        passwordCheck = false;
        password = findViewById(R.id.et_password);
        /*
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(checkPassword()){
                        passwordIsCorrect();
                    }
                    else{
                        passwordError();
                    }
                }
            }
        });
         */

        passwordMatch = false;
        passwordRepeat = findViewById(R.id.et_password_repeat);
        passwordRepeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(passwordRepeat.getText().toString().equals(password.getText().toString())){
                        passwordsMatch();
                        if (checkPassword()) {
                            passwordIsCorrect();
                        }
                        else{
                            passwordError();
                        }
                    }
                    else{
                        passwordsMismatch();
                    }
                }
            }
        });

        nextButton = findViewById(R.id.btn_next_account);
        nextButton.setOnClickListener(view -> {
            if(nextButton.isEnabled()) {
                Intent next = new Intent();
                next.setClass(SignupActivity.this, ListActivity.class);
                next.putExtra("email", email.getText().toString());
                next.putExtra("password", password.getText().toString());
                startActivity(next);
            }
        });
    }

    private boolean checkEmail() {
        File path = this.getFilesDir();
        File emails = new File(path, "emails.txt");
        int l = (int) emails.length();
        byte[] b = new byte[l];
        try{
            FileInputStream ins = new FileInputStream(emails);
            try{
                ins.read(b);

            } finally {
                ins.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String emailList = new String(b);

        String input = email.getText().toString();
        return (input.contains("@") && !emailList.toLowerCase().contains(input));
    }

    private void passwordsMismatch() {
        passwordMatch = false;
        passwordErrorBox.setVisibility(View.VISIBLE);
        passwordErrorText.setText(R.string.match_error);
        passwordRepeat.setForeground(this.getDrawable(R.drawable.outline));
        passwordRepeat.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.cross,0);
        password.setForeground(this.getDrawable(R.drawable.outline));
        password.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.cross, 0);
        buttonDisable();
    }

    private void buttonDisable() {
        nextButton.setEnabled(false);
        nextButton.setAlpha(0.5F);
    }

    private void buttonActive() {
        if(emailCheck && passwordCheck && passwordMatch){
            nextButton.setEnabled(true);
            nextButton.setAlpha(1);
        }
    }

    private void passwordsMatch() {
        passwordMatch = true;
        passwordErrorBox.setVisibility(View.GONE);
        passwordRepeat.setForeground(this.getDrawable(R.drawable.green_outline));
        passwordRepeat.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.tick,0);
        password.setForeground(this.getDrawable(R.drawable.green_outline));
        password.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.tick,0);
        buttonActive();
    }


    private void passwordError() {
        passwordCheck = false;
        passwordErrorBox.setVisibility(View.VISIBLE);
        passwordErrorText.setText(R.string.password_error);
        password.setForeground(this.getDrawable(R.drawable.outline));
        password.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.cross, 0);
        passwordRepeat.setForeground(this.getDrawable(R.drawable.outline));
        passwordRepeat.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.cross,0);
        buttonDisable();
    }

    private void passwordIsCorrect() {
        passwordCheck = true;
        passwordErrorBox.setVisibility(View.GONE);
        password.setForeground(this.getDrawable(R.drawable.green_outline));
        password.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.tick,0);
        passwordRepeat.setForeground(this.getDrawable(R.drawable.green_outline));
        passwordRepeat.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.tick,0);
        buttonActive();
    }

    private boolean checkPassword() {
        String pass = password.getText().toString();
        if(pass.length() < 8){
            return false;
        }
        Pattern p;
        Matcher m;
        p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{4,}$");
        m = p.matcher(pass);
        return m.matches();
    }

    private void emailError() {
        emailCheck = false;
        emailErrorBox.setVisibility(View.VISIBLE);
        email.setForeground(this.getDrawable(R.drawable.outline));
        email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cross,0);
        buttonDisable();
    }

    private void emailIsCorrect() {
        emailCheck = true;
        emailErrorBox.setVisibility(View.GONE);
        email.setForeground(this.getDrawable(R.drawable.green_outline));
        email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0);
        buttonActive();
    }
}