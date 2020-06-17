package com.master.molemate.LoginProcess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.master.molemate.Adapter.MoleMateRecyclerViewAdpter;
import com.master.molemate.HomeScreen.HomeScreen;
import com.master.molemate.MainActivity;
import com.master.molemate.R;
import com.master.molemate.RoomDB.Entities.Entity_Users;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import java.util.Objects;


public class PersonalInformation extends AppCompatActivity {

    private static final String TAG = "PersonalInformation";
    
    private Button sendButton;
    private Button backButton;

    private TextView helpTextView;
    private AlertDialog helpDialog;

    private EditText firstNameView;
    private EditText lastNameView;
    private EditText zipCodeView;
    private EditText ageView;
    private EditText skinTypeView;

    private FirebaseAuth authDB;
    private SharedPreferences sharedPref;

    private String mail;
    private String password;
    private Entity_Users user;


    View focusView = null;
    private MoleMateDB_ViewModel dbCom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        sendButton = findViewById(R.id.sendInfoButton);
        backButton = findViewById(R.id.backButton);
        helpTextView = findViewById(R.id.helpTextView);
        settingUpButtons();
        settingUpInfoBox();

        firstNameView = findViewById(R.id.firstNameText);
        lastNameView = findViewById(R.id.lastNameText);
        zipCodeView = findViewById(R.id.zipCodeText);
        ageView = findViewById(R.id.ageText);
        skinTypeView = findViewById(R.id.skinTypeView);

        authDB = FirebaseAuth.getInstance();

        sharedPref = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);


        mail = getIntent().getStringExtra("mail");
        password = getIntent().getStringExtra("password");

        Log.d(TAG, "onCreate: mail: " + mail + " password: " + password);

    }

    private boolean checkInputData() {
        firstNameView.setError(null);
        lastNameView.setError(null);
        zipCodeView.setError(null);
        ageView.setError(null);
        skinTypeView.setError(null);

        String firstName;
        String lastName;
        String zipCode;
        String ageText;
        String skinType;

        // Store values at the time of the login attempt.

        firstName = firstNameView.getText().toString().trim();
        lastName = lastNameView.getText().toString().trim();
        zipCode = zipCodeView.getText().toString().trim();
        ageText = ageView.getText().toString().trim();
        skinType = skinTypeView.getText().toString().trim();

        boolean cancel = false;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(firstName))
        {
            firstNameView.setError(getString(R.string.error_field_required));
            focusView = firstNameView;
            cancel = true;

        } else if (TextUtils.isEmpty(lastName) )
        {
            lastNameView.setError(getString(R.string.error_field_required));
            focusView = lastNameView;
            cancel = true;

        } else if (TextUtils.isEmpty(zipCode) )
        {
            zipCodeView.setError(getString(R.string.error_field_required));
            focusView = zipCodeView;
            cancel = true;

        } else if (TextUtils.isEmpty(ageText) )
        {
            ageView.setError(getString(R.string.error_field_required));
            focusView = ageView;
            cancel = true;

        } else if (TextUtils.isEmpty(skinType) )
        {
            skinTypeView.setError(getString(R.string.error_field_required));
            focusView = skinTypeView;
            cancel = true;

        } else if (zipCode.length()!= 5)
        {
            zipCodeView.setError(getString(R.string.error_invalid));
            focusView = zipCodeView;
            cancel = true;

        }

        if(!cancel) {
            Log.d(TAG, "checkInputData: " + ageText);
            if ((Integer.parseInt(ageText) > 120) || (Integer.parseInt(ageText) < 14)) {
                ageView.setError(getString(R.string.error_invalid));
                focusView = ageView;
                cancel = true;

            } else if ((Integer.parseInt(skinType) < 1) || (Integer.parseInt(skinType) > 4)) {
                skinTypeView.setError(getString(R.string.error_invalid));
                focusView = skinTypeView;
                cancel = true;
            }
        }

        if(!cancel) {
             user = new Entity_Users(
                    zipCode,
                    firstName,
                    lastName,
                    mail
            );
        }

        return !cancel;
    }

    private void settingUpInfoBox() {

        AlertDialog infoBox;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(R.string.infoUserData);
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        infoBox = alertBuilder.create();
        infoBox.show();
    }


    private void settingUpButtons() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInputData()) {
                    if(!mail.equals("") && !password.equals("")) {
                        registrateUserInFirebase();
                    }else{
                        errorWithRegisration();
                    }
                }else{
                    focusView.requestFocus();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), LoginActivity.class));
            }
        });

        helpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PersonalInformation.this);
                alertBuilder.setMessage(R.string.skinTypeHelp);
                alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                helpDialog = alertBuilder.create();
                helpDialog.show();

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
                            String uid = Objects.requireNonNull(authDB.getCurrentUser()).getUid();
                            Log.d(TAG, "createUserWithEmail:success with uid: " + uid);

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("key", uid);
                            editor.apply();

                            successfulRegistration(uid);

                        } else {
                            // If sign in fails, display a message to the user.
                            errorWithRegisration();

                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }

                    }
                });
    }

    private void errorWithRegisration() {
        Toast.makeText(getApplication(), "Leider ist bei der Erstellung des Kontos ein Fehler unterlaufen!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplication(), UserRegistration.class);
        startActivity(intent);
    }

    private void successfulRegistration(String uid) {

        dbCom = new ViewModelProvider(this).get(MoleMateDB_ViewModel.class);

        if(user != null){
            Log.d(TAG, "successfulRegistration: User: " + user.getMail());

            dbCom.insertUser(user);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("key", uid);
            intent.putExtra("mail", mail);
            startActivity(intent);

        }
    }
}
