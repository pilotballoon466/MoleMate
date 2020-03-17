package com.master.molemate.DiagnosisTool.DiagnosisFragments;

import android.net.Uri;
import android.os.Bundle;
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

public class Fragment_Diagnosis extends Fragment {

    private Button saveButton;
    private ImageView moleImageView;
    private TextView diagnosisTextView;
    private Diagnosis_SharedViewModel dataContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_diagnosis, container, false);

        saveButton = layout.findViewById(R.id.fragment_go_to_save_button);
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataContainer = new ViewModelProvider(getActivity()).get(Diagnosis_SharedViewModel.class);
        dataContainer.getMoleImage().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(@Nullable Uri moleImageUri) {
                moleImageView.setImageURI(moleImageUri);
            }
        });

        //TODO: Diagnosis must be filled in here
        dataContainer.getMolePosInWords().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                diagnosisTextView.setText(s);
            }
        });
    }
}
