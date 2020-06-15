package com.master.molemate.ImageFileStorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.master.molemate.Adapter.MoleMateFragmentStatePagerAdapter;
import com.master.molemate.DiagnosisTool.Diagnosis_Tool;
import com.master.molemate.ImageFileStorage.SupporterClasses.RecyclerViewMoleImageItem;
import com.master.molemate.R;
import com.master.molemate.RoomDB.Entities.EntityMix_User_MoleLib;
import com.master.molemate.RoomDB.Entities.Entity_Mole_Library;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageArchiveShowMole extends AppCompatActivity {

    private static final String TAG = "ImageArchiveShowMole";

    private ImageView moleImageView;
    private TextView diagnosisPropHeaderView;
    private TextView diagnosisPropTextView;
    private TextView diagnosisTextHeaderView;
    private TextView diagnosisTextView;
    private Button deleteMoleButton;
    private Button sendToDocButton;


    Toolbar toolbar;
    private MoleMateDB_ViewModel dbCom;
    private Entity_Mole_Library molesById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_archive_show_mole);

        moleImageView = findViewById(R.id.show_diagnosis_mole_image);
        diagnosisPropHeaderView = findViewById(R.id.show_diagnosis_procent_header);
        diagnosisPropTextView = findViewById(R.id.show_diagnosis_procent);
        diagnosisTextHeaderView = findViewById(R.id.show_diagnosis_text_header);
        diagnosisTextView = findViewById(R.id.show_diagnosis_text);
        deleteMoleButton = findViewById(R.id.show_takenCareOf_button);
        sendToDocButton = findViewById(R.id.show_send_to_Doc_button);

        //Adding Toolbar and Title to Toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mole Diagnose");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        final int moleID = intent.getIntExtra("mole_id", -1);
        Log.d(TAG, "onCreate: MoleIDs: " + moleID);


        dbCom = new ViewModelProvider(this).get(MoleMateDB_ViewModel.class);

        if(moleID != -1) {
            dbCom.getAllMolesFromMoleIds(new int[]{moleID}).observe(this, new Observer<List<Entity_Mole_Library>>() {
                @Override
                public void onChanged(List<Entity_Mole_Library> entity_mole_libraries) {

                    if(entity_mole_libraries.size() != 0){
                        molesById = entity_mole_libraries.get(0);

                        if(molesById != null) {
                            Log.d(TAG, "onChanged: MolesByIDs: " + molesById.getMoleImageUri());
                            settingUpImages();
                            settingUpTextViews();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Derzeit hast du noch keine Aufnahmen gemacht!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Hier ist leider ein Fehler unterlaufen.", Toast.LENGTH_LONG).show();

            intent = new Intent(this, ImageFileArchive.class);
            startActivity(intent);
        }

    }

    private void settingUpImages() {

        Log.d(TAG, "settingUpImages: imageUri: " + molesById.getMoleImageUri());

        moleImageView.setImageURI(Uri.parse(molesById.getMoleImageUri()));
        moleImageView.setRotation(90f);

    }

    private void settingUpTextViews() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        double tmpDiagnosisProb = molesById.getDiagnosedPropability();

        diagnosisPropHeaderView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width * 0.08));
        diagnosisPropTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width * 0.1));
        diagnosisTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width*0.04));
        diagnosisTextHeaderView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width*0.08));

        Log.d(TAG, "settingUpTextViews: prop: " + tmpDiagnosisProb);

        if(tmpDiagnosisProb<40.0){
            diagnosisPropHeaderView.setText(R.string.lookInto);
            diagnosisPropTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorNegativ, null));
            diagnosisTextView.setText(molesById.getDiagnosis() + "\n\nPosition: " + molesById.getMolePosText() );
            String topConfidence = String.valueOf(100-Math.round(tmpDiagnosisProb));
            diagnosisPropTextView.setText(topConfidence + "%");

        }else{
            diagnosisPropHeaderView.setText(R.string.begnin);
            diagnosisPropTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPositiv, null));
            diagnosisTextView.setText(molesById.getDiagnosis() + "\n\nPosition: " + molesById.getMolePosText());
            String topConfidence = String.valueOf(Math.round(tmpDiagnosisProb));
            diagnosisPropTextView.setText(topConfidence + "%");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_storage, menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){

                    case R.id.menu_item_share:
                        shareMoleImage();
                        break;
                    case R.id.menu_item_sendToDoc:
                        sendToDoc();
                        break;
                    case R.id.menu_item_takenCareOf:
                        takenCareOfPopUp();
                        break;
                    case R.id.menu_item_delete:
                        deleteMole();
                        break;
                }

                return false;
            }
        });

        return true;
    }

    private void shareMoleImage() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(molesById.getMoleImageUri()));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "Share images to.."));

    }

    private void sendToDoc() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("application/image");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"bkamp@uos.de"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Test Subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(molesById.getMoleImageUri()));
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }


    public void getIndexInfo(View view) {

    }

    public void buttonAction(View view) {
        switch (view.getId()){
            case R.id.show_takenCareOf_button:
                takenCareOfPopUp();
                break;
            case R.id.show_send_to_Doc_button:
                sendToDoc();
                break;
            case R.id.show_diagnosis_procent:
                break;

        }
    }

    private void takenCareOfPopUp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vollzogene Behandlung");
        builder.setMessage("Die Hautläsion wurde entfernt. Bitte gebe hier das Ergebnis der Untersuchung an, damit wir das Ergebnis entsprechend anpassen können!");

        builder.setPositiveButton("Gutartig", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        molesById.setHandled(true);
                        molesById.setDiagnosedPropability(99.0);
                        molesById.setDiagnosis("Dieses Muttermal wurde bereits von einem Arzt untersucht und ist als gutartig eingestuft worden!");
                        dbCom.updateMole(molesById);
                    }
                });

        builder.setNegativeButton("Bösartig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                molesById.setHandled(true);
                molesById.setDiagnosedPropability(1.0);
                molesById.setDiagnosis("Dieses Muttermal wurde bereits von einem Arzt untersucht und ist als bösartig eingestuft worden!");
                dbCom.updateMole(molesById);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteMole() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Löschen");
        builder.setMessage("Möchtest du diese Aufnahme löschen?");

        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbCom.deleteMole(molesById);
                Intent intent = new Intent(getApplication(), ImageFileArchive.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
