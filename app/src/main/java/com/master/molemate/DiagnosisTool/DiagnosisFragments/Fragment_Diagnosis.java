package com.master.molemate.DiagnosisTool.DiagnosisFragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.master.molemate.DiagnosisTool.Diagnosis_SharedViewModel;
import com.master.molemate.DiagnosisTool.Diagnosis_Tool;
import com.master.molemate.R;
import com.master.molemate.RoomDB.Entities.Entity_Mole_Library;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

public class Fragment_Diagnosis extends Fragment {

    private Button saveButton;
    private Button cancelButton;
    private ImageView moleImageView;
    private TextView diagnosisTextView;
    private TextView diagnosisProcentHeaderView;
    private TextView diagnosisProcentView;
    private TextView diagnosisTextHeader;

    private Diagnosis_SharedViewModel dataContainer;
    private MoleMateDB_ViewModel dbHandler;

    private int tmpMolePosColorCode;
    private Uri tmpUriMoleImage;
    private String tmpDiagnosisText;
    private float tmpDiagnosisProb;
    private Uri tmpUriMolePosBitmap;
    private String tmpDateOfCreation;
    private String tmpMolePosText;
    private int tmpUserOfMole;
    private boolean tmpIsFrontside;
    private Entity_Mole_Library moleEntry;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_diagnosis, container, false);

        saveButton = layout.findViewById(R.id.fragment_go_to_save_button);
        cancelButton = layout.findViewById(R.id.fragment_save_cancelButton);
        moleImageView = layout.findViewById(R.id.fragment_diagnosis_mole_image);
        diagnosisTextView = layout.findViewById(R.id.fragment_diagnosis_text);

        settingUpSaveButton();


        return layout;

    }

    private void settingUpSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataContainer.setDiagnosis("Dies ist die Diagnose!");
                ((Diagnosis_Tool)getActivity()).selectFragmentToShowWithTitle("check_and_save");
            }
        });
    }

    private boolean checkData() {
        if(dbHandler == null){
            return false;
        }else if(tmpUriMoleImage == null){
            return false;

        }else if(tmpDateOfCreation == null){
            return false;


        }else if(tmpDiagnosisText == null){
            return false;


        }else if(tmpMolePosColorCode == 0){
            return false;


        }else if(tmpUriMolePosBitmap == null){
            return false;

        }else if(tmpDiagnosisProb == 0.0){
            return false;

        }else if(tmpMolePosText.equals("")){
            return false;

        } else {
            return true;
        }
    }

    private void crawlingMoleData() {

        if(dataContainer != null ){

            dataContainer.getMoleUserViewModel().observe(getViewLifecycleOwner(), new Observer<MoleMateDB_ViewModel>() {
                @Override
                public void onChanged(MoleMateDB_ViewModel moleUserViewModel_for_db) {
                    dbHandler = moleUserViewModel_for_db;
                }
            });

            dataContainer.getMoleImage().observe(getViewLifecycleOwner(), new Observer<Uri>() {
                @Override
                public void onChanged(@Nullable Uri uriImageMole) {
                    tmpUriMoleImage = uriImageMole;
                    Log.d(TAG, "onChanged: URIMole: " + tmpUriMoleImage);
                }
            });

            //TODO Wrong text! Needs to be changed
            dataContainer.getDiagnosis().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    tmpDiagnosisText = s;
                    Log.d(TAG, "onChanged: MolePosinWords: " + s);
                }
            });

            dataContainer.getMolePosImage().observe(getViewLifecycleOwner(), new Observer<Uri>() {
                @Override
                public void onChanged(@Nullable Uri molePosImageUri) {
                    tmpUriMolePosBitmap= molePosImageUri;
                    Log.d(TAG, "onChanged: URIPos: " + tmpUriMolePosBitmap);
                    //TODO Das Bild scheint nicht geladen zu werden, ist nur schwearz mit nem Punkt
                }
            });

            dataContainer.getMolePosColorCode().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
                    tmpMolePosColorCode = integer;
                    Log.d(TAG, "onChanged: molePosColor: " + tmpMolePosColorCode);
                }
            });

            dataContainer.getIsFront().observe(getViewLifecycleOwner(), new Observer<Boolean>(){

                @Override
                public void onChanged(Boolean aBoolean) {
                    tmpIsFrontside = aBoolean;
                    Log.d(TAG, "onChanged: isFront? " + tmpIsFrontside);
                }
            });

            dataContainer.getMoleImageCreationDate().observe(getViewLifecycleOwner(), new Observer<Date>() {
                @Override
                public void onChanged(Date date) {
                    if(date != null) {
                        tmpDateOfCreation = date.toString();
                        Log.d(TAG, "onChanged: date: " + tmpDateOfCreation);
                    }
                    Log.d(TAG, "onChanged: date: " + tmpDateOfCreation);
                }
            });

            dataContainer.getMolePosInWords().observe(getViewLifecycleOwner(), new Observer<String>() {

                @Override
                public void onChanged(String molePosText) {
                    if(molePosText != null) {
                        tmpMolePosText = molePosText;
                        Log.d(TAG, "onChanged: date: " + molePosText);
                    }
                    Log.d(TAG, "onChanged: date: " + molePosText);
                }
            });

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataContainer = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(Diagnosis_SharedViewModel.class);

        crawlingMoleData();

        startDiagnosis();

    }

    private void startDiagnosis() {
        dataContainer.getMoleImage().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(@Nullable Uri moleImageUri) {

                if(moleImageUri != null) {
                    moleImageView.setImageURI(moleImageUri);
                    moleImageView.setRotation(moleImageView.getRotation() + 90);

                    if (moleImageView != null) {
                        // get current bitmap from imageView
                        Bitmap bitmap_orig = ((BitmapDrawable) moleImageView.getDrawable()).getBitmap();
                        // resize the bitmap to the required input size to the CNN
                        Bitmap bitmap = getResizedBitmap(bitmap_orig, IMAGE_WIDTH, IMAGE_HEIGHT);
                        // convert bitmap to byte array
                        convertBitmapToByteBuffer(bitmap);
                        // pass byte data to the graph
                        if (quant) {
                            tflite.run(imageData, labelProbArrayByte);
                        } else {
                            tflite.run(imageData, labelProbArray);
                        }
                        // display the results
                        printTopKLabels();
                    } else {
                        Toast.makeText(getContext(), "Bild konnte derzeit noch nicht gefunden werden.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Bild konnte derzeit noch nicht gefunden werden.", Toast.LENGTH_LONG).show();
                }
            }
        });


        imageInInteger = new int[IMAGE_HEIGHT*IMAGE_WIDTH];

        try{
            tflite = new Interpreter(loadModelFile(), tfliteOptions);
            labelList = loadLabelList();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        // initialize byte array. The size depends if the input data needs to be quantized or not
        if(quant){
            imageData =
                    ByteBuffer.allocateDirect(
                            IMAGE_HEIGHT * IMAGE_WIDTH * IMAGE_PIXEL_SIZE);
        } else {
            imageData =
                    ByteBuffer.allocateDirect(
                            4 * IMAGE_HEIGHT * IMAGE_WIDTH * IMAGE_PIXEL_SIZE);
        }

        imageData.order(ByteOrder.nativeOrder());



        // initialize probabilities array. The datatypes that array holds depends if the input data needs to be quantized or not
        if(quant){
            labelProbArrayByte= new byte[1][labelList.size()];
        } else {
            labelProbArray = new float[1][labelList.size()-1];
        }

        // initialize array to hold top labels
        topLables = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence = new String[RESULTS_TO_SHOW];
    }

    private void setCancelButton() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataContainer.deleteAllValues();
                ((Diagnosis_Tool) Objects.requireNonNull(getActivity())).selectFragmentToShowWithTitle(Diagnosis_Tool.TAKE_IMAGE);
            }
        });
    }
}
