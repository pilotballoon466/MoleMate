package com.master.molemate.LoginProcess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText mailView;
    private EditText passwordView;
    private Button loginButton;
    private Button registrationButton;

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
                signIn(mailView.getText().toString().trim(), passwordView.getText().toString().trim());
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
                            successfulLogin(tmpMail);

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

    private void successfulLogin(String mail) {

        Log.d(TAG, "signInWithEmail:success");

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("mail", mail);
        startActivity(intent);
    }
}
