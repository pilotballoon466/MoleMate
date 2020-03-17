package com.master.molemate.DiagnosisTool.DiagnosisFragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.master.molemate.DiagnosisTool.Diagnosis_SharedViewModel;
import com.master.molemate.DiagnosisTool.Diagnosis_Tool;
import com.master.molemate.R;
import com.master.molemate.RoomDB.Entities.Entity_Mole_Library;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import java.util.Date;

public class Fragment_Save_Mole_Diagnosis extends Fragment {

    private static final String TAG = "Fragment_Save_Mole_Imag";

    private TextView molePosText; //needed??
    private TextView diagnosisTextView;
    private Button cancelButton;
    private Button saveButton;
    private ImageView moleImageView;
    private ImageView molePosView; //TODO

    private Diagnosis_SharedViewModel dataContainer;
    private MoleMateDB_ViewModel dbHandler;

    private int molePosColorCode;
    private Uri tmpUriMoleImage;
    private String diagnosis;
    private Uri uriMolePosBitmap;
    private String dateOfCreation;
    private int userOfMole;
    boolean isFrontside;
    private Entity_Mole_Library moleEntry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_save_mole_diagnosis, container, false);

        cancelButton = (Button) layout.findViewById(R.id.fragment_save_cancelButton);
        saveButton = (Button) layout.findViewById(R.id.fragment_save_saveButton);
        molePosView = (ImageView) layout.findViewById(R.id.fragment_save_mole_pos_image);
        diagnosisTextView = (TextView) layout.findViewById(R.id.fragment_save_diagnosis_text);
        moleImageView = (ImageView) layout.findViewById(R.id.fragment_save_mole_image);

        setCancelButton();
        setSaveButton();

        //bei der TextView für Körperposition muss nen Button rein für ändern, der auf das entsprechende Fragment zurückführt

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataContainer = new ViewModelProvider(getActivity()).get(Diagnosis_SharedViewModel.class);

        dataContainer.getMoleUserViewModel().observe(getViewLifecycleOwner(), new Observer<MoleMateDB_ViewModel>() {
            @Override
            public void onChanged(MoleMateDB_ViewModel moleUserViewModel_for_db) {
                dbHandler = moleUserViewModel_for_db;
            }
        });

        dataContainer.getMoleImage().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(@Nullable Uri uriImageMole) {
                moleImageView.setImageURI(uriImageMole);
                tmpUriMoleImage = uriImageMole;
                Log.d(TAG, "onChanged: URIMole: " + tmpUriMoleImage);
            }
        });

        //TODO Wrong text! Needs to be changed
        dataContainer.getMolePosInWords().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                diagnosisTextView.setText(s);
                Log.d(TAG, "onChanged: MolePosinWords: " + s);
            }
        });

        dataContainer.getMolePosImage().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(@Nullable Uri molePosImageUri) {
                molePosView.setImageURI(molePosImageUri);
                uriMolePosBitmap = molePosImageUri;
                Log.d(TAG, "onChanged: URIPos: " + uriMolePosBitmap);
                //TODO Das Bild scheint nicht geladen zu werden, ist nur schwearz mit nem Punkt
            }
        });

        dataContainer.getMolePosColorCode().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                molePosColorCode = integer;
                Log.d(TAG, "onChanged: molePosColor: " + molePosColorCode);
            }
        });

        dataContainer.getIsFront().observe(getViewLifecycleOwner(), new Observer<Boolean>(){

            @Override
            public void onChanged(Boolean aBoolean) {
                isFrontside = aBoolean;
                Log.d(TAG, "onChanged: isFront? " + isFrontside);
            }
        });

        dataContainer.getMoleImageCreationDate().observe(getViewLifecycleOwner(), new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                if(date != null) {
                    dateOfCreation = date.toString();
                    Log.d(TAG, "onChanged: date: " + dateOfCreation);
                }else
                    dateOfCreation = "";
                Log.d(TAG, "onChanged: date: " + dateOfCreation);
            }
        });

        dataContainer.getDiagnosis().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                diagnosis = s;
                Log.d(TAG, "onChanged: diagnosis: " + diagnosis);
            }
        });

        //TODO: Need the users UID here!

    }


    private void setSaveButton() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moleEntry = new Entity_Mole_Library(
                        dateOfCreation,
                        0,
                        tmpUriMoleImage.getPath(),
                        uriMolePosBitmap.getPath(),
                        molePosColorCode,
                        isFrontside,
                        diagnosis,
                        99.9);


                //Log.d(TAG, "onClick: userID in db " + dbHandler.getDefaultUser().getUid());

                //dbHandler.insertMole(moleEntry);

                dataContainer.deleteAllValues();

                //((TakeMolePicAssistent)getActivity()).selectFragmentToShow(0);

            }
        });
    }

    private void setCancelButton() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Diagnosis_Tool)getActivity()).selectFragmentToShow(3);
            }
        });
    }
}
