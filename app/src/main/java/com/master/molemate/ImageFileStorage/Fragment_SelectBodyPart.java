package com.master.molemate.ImageFileStorage;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.master.molemate.ImageFileStorage.SupporterClasses.ViewModel_ImageArchive_Communication;
import com.master.molemate.R;


public class Fragment_SelectBodyPart extends Fragment {

    private Button headMoleImagesButton;
    private Button bodyMoleImagesButton;
    private Button armsMoleImagesButton;
    private Button legsMoleImagesButton;
    private ViewModel_ImageArchive_Communication fragmentCom;

    public static final String BODYPART_HEAD = "head";
    public static final String BODYPART_ARMS = "arms";
    public static final String BODYPART_LEGS = "legs";
    public static final String BODYPART_BODY = "upper_body";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_select_body_part, container, false);

        headMoleImagesButton = (Button) layout.findViewById(R.id.headFileStorageButton);
        bodyMoleImagesButton= (Button) layout.findViewById(R.id.bodyFileStorageButton);
        armsMoleImagesButton= (Button) layout.findViewById(R.id.armsFileStorageButton);
        legsMoleImagesButton = (Button) layout.findViewById(R.id.legsFileStorageButton);

        fragmentCom = new ViewModelProvider(getActivity()).get(ViewModel_ImageArchive_Communication.class);


        settingUpButtons();

        return layout ;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((ImageFileArchive)getActivity()).showBackButton(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ImageFileArchive)getActivity()).showBackButton(false);
    }

    private void settingUpButtons() {

        headMoleImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentCom.setSelectedBodypart(BODYPART_HEAD);

                ((ImageFileArchive)getActivity()).selectFragmentToShowWithTitle(ImageFileArchive.SELECTED_BODYPART);
            }
        });


        bodyMoleImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentCom.setSelectedBodypart(BODYPART_BODY);


                ((ImageFileArchive)getActivity()).selectFragmentToShowWithTitle(ImageFileArchive.SELECTED_BODYPART);

            }
        });


        armsMoleImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentCom.setSelectedBodypart(BODYPART_ARMS);

                ((ImageFileArchive)getActivity()).selectFragmentToShowWithTitle(ImageFileArchive.SELECTED_BODYPART);

            }
        });

        legsMoleImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentCom.setSelectedBodypart(BODYPART_LEGS);
                ((ImageFileArchive)getActivity()).selectFragmentToShowWithTitle(ImageFileArchive.SELECTED_BODYPART);

            }
        });
    }


}
