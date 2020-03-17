package com.master.molemate.LoginProcess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.master.molemate.LoginProcess.LoginActivity;
import com.master.molemate.MainActivity;
import com.master.molemate.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegistration extends AppCompatActivity {

    private static final String TAG = "UserRegistration";

    private EditText mailView;
    private EditText mailConfirmView;
    private EditText passwordView;
    private Button registrateButton;
    private Button backButton;

    private FirebaseAuth authDB;

    private String mail;
    private String password;

    View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        mailView = findViewById(R.id.userMailText);
        mailConfirmView = findViewById(R.id.userMailConfirmation);
        passwordView = findViewById(R.id.userPassword);
        registrateButton = findViewById(R.id.registrateButton);
        backButton = findViewById(R.id.backButton);

        authDB = FirebaseAuth.getInstance();

        settingUpRegistrateButton();
        settingUpBackButton();

    }

    private void settingUpBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void settingUpRegistrateButton() {
        registrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInputData()){
                    registrateUserInFirebase();
                }else{
                    focusView.requestFocus();
                }
            }
        });
    }

    private void registrateUserInFirebase() {
        authDB.createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        String uid = authDB.getCurrentUser().getUid();

                        successfulRegistration(uid);
                    } else {
                        // If sign in fails, display a message to the user.
                        focusView = null;
                        mailView.setError(getString(R.string.error_invalid_email));
                        focusView = mailView;
                        focusView.requestFocus();

                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    }

                }
            });
    }

    private void successfulRegistration(String uid) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    /**
     *
     * first_registration_step() collects the input Data from the perspective fields and verifies the data
     * if the Data is correct it passes the Data to Firebase which tries to create a new Account
     * otherwise an error is thrown and will be shown on the screen, so that the user can adapt the input
     */
    private boolean checkInputData() {
        mailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.

        mail = mailView.getText().toString().trim();
        password = passwordView.getText().toString().trim();
        String confirmationMail = mailConfirmView.getText().toString().trim();

        boolean cancel = false;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmationMail)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;

        } else if (!isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;

        } else if(!mail.equals(confirmationMail)){
            mailView.setError(getString(R.string.error_mails_not_equals));
            focusView = mailView;
            cancel = true;
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(mail)) {
            mailView.setError(getString(R.string.error_field_required));
            focusView = mailView;
            cancel = true;
        } else if (!isEmailValid(mail)) {
            mailView.setError(getString(R.string.error_invalid_email));
            focusView = mailView;
            cancel = true;
        }

        return !cancel;

    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$", Pattern.CASE_INSENSITIVE);

    /**
     * Method to check the validity of the Email
     * @param email
     * @return boolean
     */
    private boolean isEmailValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    /**
     * Method to validate the password
     * @param password
     * @return boolean
     */
    private boolean isPasswordValid(String password) {
        Matcher match = VALID_PASSWORD_REGEX.matcher(password);
        return match.matches();
    }

}
