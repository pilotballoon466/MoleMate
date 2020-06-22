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
import com.google.android.material.textfield.TextInputLayout;
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
                    Intent intent = new Intent(getApplication(), PersonalInformation.class);
                    intent.putExtra("mail", mail);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }else{
                    focusView.requestFocus();
                }
            }
        });
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

        } else if (!isPasswordValid(password)) {
            toCheckPassword.setError(getString(R.string.passwordRequirements));
            focusView = toCheckPassword;
            return false;

        }else {
            toCheckPassword.setError(null);
            return true;
        }
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
        //
        mail = mailView.getText().toString().trim();;
        password = passwordView.getText().toString().trim();;
        String confirmationMail = mailConfirmView.getText().toString().trim();;

        boolean valid = true;

        if(!validateMail(mailView) | !validateMail(mailConfirmView) | !validatePassword(passwordView)){
            valid = false;
        }else if (!mail.equals(confirmationMail)){
            mailView.setError(getString(R.string.error_mails_not_equals));
            mailConfirmView.setError(getString(R.string.error_mails_not_equals));
            valid = false;
        }

        return valid;

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
