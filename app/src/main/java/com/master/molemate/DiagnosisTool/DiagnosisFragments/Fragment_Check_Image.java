package com.master.molemate.DiagnosisTool.DiagnosisFragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.master.molemate.DiagnosisTool.Diagnosis_SharedViewModel;
import com.master.molemate.DiagnosisTool.Diagnosis_Tool;
import com.master.molemate.R;

public class Fragment_Check_Image extends Fragment {

    ImageView takenImage;
    Button back;
    Button continueWithDiagnose;
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
                ((Diagnosis_Tool)getActivity()).selectFragmentToShow(0);
            }
        });

        continueWithDiagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Diagnosis_Tool)getActivity()).selectFragmentToShow(2);
            }
        });

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataContainer = new ViewModelProvider(getActivity()).get(Diagnosis_SharedViewModel.class);
        dataContainer.getMoleImage().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(@Nullable Uri moleImageUri) {
                takenImage.setImageURI(moleImageUri);
            }
        });
    }
}
