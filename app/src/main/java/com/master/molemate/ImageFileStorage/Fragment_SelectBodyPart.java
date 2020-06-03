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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.master.molemate.ImageFileStorage.SupporterClasses.ViewModel_ImageArchive_Communication;
import com.master.molemate.R;

import java.util.Objects;


public class Fragment_SelectBodyPart extends Fragment {


    private ViewModel_ImageArchive_Communication fragmentCom;

    public static final String BODYPART_HEAD = "head";
    public static final String BODYPART_ARMS = "arms";
    public static final String BODYPART_LEGS = "legs";
    public static final String BODYPART_BODY = "upper_body";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_select_body_part, container, false);

        fragmentCom = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(ViewModel_ImageArchive_Communication.class);


        settingUpButtons();

        return layout ;
    }

    @Override
    public void onStart() {
        super.onStart();
        //((ImageFileArchive) Objects.requireNonNull(getActivity())).showBackButton(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //((ImageFileArchive) Objects.requireNonNull(getActivity())).showBackButton(false);
    }

    private void settingUpButtons() {


    }


}
