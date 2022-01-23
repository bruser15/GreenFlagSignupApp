package com.example.greenflagsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("UseCompatLoadingForDrawables")
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
        email.setOnFocusChangeListener((view, b) -> {
            if(!b){
                if(emailValid(email.getText().toString())){
                    emailIsValid();
                    if(checkEmail()){
                        emailIsCorrect();
                    }
                    else{
                        emailError();
                    }
                }
                else{
                    emailIsInvalid();
                }
            }
        });

        passwordErrorBox = findViewById(R.id.cl_password_error);
        passwordErrorText = findViewById(R.id.tv_error_password);
        passwordCheck = false;
        password = findViewById(R.id.et_password);
        passwordMatch = false;
        passwordRepeat = findViewById(R.id.et_password_repeat);
        password.setOnFocusChangeListener((view, b) -> {
            if(!b){
                if(passwordRepeat.getText().toString().equals(password.getText().toString())) {
                    passwordsMatch();
                    if (checkPassword()) {
                        passwordIsCorrect();

                    } else {
                        passwordError();
                    }
                }
                else{
                    passwordsMismatch();
                }
            }
        });
        passwordRepeat.setOnFocusChangeListener((view, b) -> {
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


    private void emailIsInvalid() {
        emailCheck = false;
        emailErrorBox.setVisibility(View.VISIBLE);
        email.setForeground(this.getDrawable(R.drawable.outline));
        email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cross,0);
        TextView emailErrorText = findViewById(R.id.tv_error_email);
        emailErrorText.setText(R.string.email_invalid);
        buttonDisable();
    }

    private void emailIsValid() {
        TextView emailErrorText = findViewById(R.id.tv_error_email);
        emailErrorText.setText(R.string.email_error);
    }

    private boolean emailValid(String input) {
        return (!input.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(input).matches());
    }

    private boolean checkEmail() {
        File path = this.getFilesDir();
        File emails = new File(path, "emails.txt");
        int l = (int) emails.length();
        byte[] b = new byte[l];
        try{
            try (FileInputStream ins = new FileInputStream(emails)) {
                ins.read(b);

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String emailList = new String(b);
        if(emailList.isEmpty()){
            //No email addresses are registered, input can't already exist
            return true;
        }

        String input = email.getText().toString();
        String[] eList = emailList.split("\n");
        for (String itEmail : eList) {
            if (itEmail.equalsIgnoreCase(input)) {
                return false;
            }
        }
        return true;
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

    @Override
    protected void onRestart() {
        super.onRestart();
        email.setText("");
        emailIsInvalid();
        buttonDisable();
    }
}