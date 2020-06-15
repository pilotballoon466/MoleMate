package com.master.molemate.DiagnosisTool.DiagnosisFragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.master.molemate.DiagnosisTool.Diagnosis_SharedViewModel;
import com.master.molemate.DiagnosisTool.Diagnosis_Tool;
import com.master.molemate.R;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class Fragment_Check_Image extends Fragment {

    private static final int CROP_IMAGE_REQ = 1;
    private static final String TAG = "Fragment_Check_Image";
    private ImageView takenImage;
    private Button back;
    private Button continueWithDiagnose;
    private Diagnosis_SharedViewModel dataContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_check_image, container, false);

        takenImage = layout.findViewById(R.id.viewTakenImage);
        back = layout.findViewById(R.id.backToTakePic);
        continueWithDiagnose = layout.findViewById(R.id.continueWithDiagnose);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Diagnosis_Tool) Objects.requireNonNull(getActivity())).selectFragmentToShowWithTitle(Diagnosis_Tool.TAKE_IMAGE);
            }
        });

        continueWithDiagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((Diagnosis_Tool) Objects.requireNonNull(getActivity())).selectFragmentToShowWithTitle(Diagnosis_Tool.MOLE_POSITION);
            }
        });

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataContainer = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(Diagnosis_SharedViewModel.class);
        ((Diagnosis_Tool)getActivity()).showBackButton(true);
        dataContainer.getMoleImage().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(@Nullable Uri moleImageUri) {
                takenImage.setImageURI(moleImageUri);
                takenImage.setRotation(90f);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CROP_IMAGE_REQ && resultCode == RESULT_OK){
            if(data != null) {
                Bundle bundel = data.getExtras();
                Bitmap bitmap = bundel.getParcelable("data");
                takenImage.setImageBitmap(bitmap);
            }
        }
    }
}
