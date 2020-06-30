package com.master.molemate.LoginProcess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.master.molemate.MainActivity;
import com.master.molemate.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText mailView;
    private EditText passwordView;
    private Button loginButton;
    private Button registrationButton;
    private SharedPreferences sharedPref;

    private String mail;
    private String password;
    private View focusView;

    private FirebaseAuth auth;

    private String uid;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mailView = findViewById(R.id.mail_login);
        passwordView = findViewById(R.id.password_login);
        loginButton = findViewById(R.id.loginButton);
        registrationButton = findViewById(R.id.registrationButton);

        mailView.setText("default@default.com");
        passwordView.setText("Passwort1234");

        auth = FirebaseAuth.getInstance();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file),Context.MODE_PRIVATE);

        progressBar = new ProgressBar(this);

        settingUpLoginButton();
        settingUpRegistrationButton();

    }

    private void settingUpRegistrationButton(){
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UserRegistration.class);
                startActivity(intent);
            }
        });

    }

    private void settingUpLoginButton() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkInputData()) {

                    mail = mailView.getText().toString().trim();
                    password = passwordView.getText().toString().trim();

                    signIn(mail, password);
                }
            }
        });
    }


    /**
     * Signs in a user manually.
     */
    private void signIn(String mail , String password) {

        Log.d(TAG, "signIn:" + mail);

        final String tmpMail = mail;

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            String uid = auth.getUid();

                            SharedPreferences sharedPref = getApplication().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("key", uid);
                            editor.apply();

                            successfulLogin(tmpMail, uid);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Passwort und/oder E-Mail-Adresse nicht korrekt.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void successfulLogin(String mail, String key) {

        Log.d(TAG, "signInWithEmail:success with key: " + key + " mail: " + mail);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("mail", mail);
        intent.putExtra("key", key);

        startActivity(intent);
    }

    /**
     *
     * first_registration_step() collects the input Data from the perspective fields and verifies the data
     * if the Data is correct it passes the Data to Firebase which tries to create a new Account
     * otherwise an error is thrown and will be shown on the screen, so that the user can adapt the input
     */
    private boolean checkInputData() {

        boolean valid = true;

        if(!validateMail(mailView)  | !validatePassword(passwordView)){
            valid = false;
        }

        return valid;

    }

    private boolean validateMail(EditText toCheckMail){

        String mail = toCheckMail.getText().toString().trim();
        // Check for a valid email address.
        if (mail.isEmpty()) {
            toCheckMail.setError(getString(R.string.error_field_required));
            focusView = toCheckMail;
            return false;
        } else if (!isEmailValid(mail)) {
            toCheckMail.setError(getString(R.string.error_invalid_email));
            focusView = toCheckMail;
            return false;
        } else {
            toCheckMail.setError(null);
            return true;
        }
    }

    private boolean validatePassword(EditText toCheckPassword) {
        // Check for a valid password, if the user entered one.

        String password = toCheckPassword.getText().toString().trim();

        if (password.isEmpty()) {
            toCheckPassword.setError(getString(R.string.error_field_required));
            focusView = toCheckPassword;
            return false;

        } else {
            toCheckPassword.setError(null);
            return true;
        }
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Method to check the validity of the Email
     * @param email
     * @return boolean
     */
    private boolean isEmailValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }




    @Override
    public void onBackPressed() {

    }
}
