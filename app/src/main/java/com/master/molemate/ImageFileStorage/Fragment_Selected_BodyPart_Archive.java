package com.master.molemate.ImageFileStorage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.master.molemate.Adapter.MoleMateFragmentStatePagerAdapter;
import com.master.molemate.Adapter.MoleMateRecyclerViewAdpter;
import com.master.molemate.ImageFileStorage.SupporterClasses.RecyclerViewMoleImageItem;
import com.master.molemate.ImageFileStorage.SupporterClasses.ViewModel_ImageArchive_Communication;
import com.master.molemate.R;
import com.master.molemate.RoomDB.Entities.EntityMix_User_MoleLib;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Fragment_Selected_BodyPart_Archive extends Fragment {

    private static final String TAG = "Fragment_Selected_BodyP";

    private Toolbar toolbar;
    private RecyclerView moleRecyclerView;
    private MoleMateRecyclerViewAdpter moleRecyclerViewAdapter;
    private RecyclerView.LayoutManager moleRecyclerViewLayoutManager;
    private ArrayList<RecyclerViewMoleImageItem> moleItemList;
    private ViewModel_ImageArchive_Communication fragmentCom;

    private MoleMateDB_ViewModel dbCom;

    private String selectedBodyPart;
    private List<EntityMix_User_MoleLib> moleLibCurrentUser;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_selected_bodypart_archive, container, false);

        moleItemList = new ArrayList<>();
        //moleItemList.add(new RecyclerViewMoleImageItem(R.drawable.arm_svg, "12.1.19 Oberarm", "Diagnose war ok"));
        //moleItemList.add(new RecyclerViewMoleImageItem(R.drawable.leg_svg, "12.1.19 Bein", "Diagnose war ok"));
        //moleItemList.add(new RecyclerViewMoleImageItem(R.drawable.head_svg, "12.1.19 Kopf", "Diagnose war ok"));
        //moleItemList.add(new RecyclerViewMoleImageItem(R.drawable.female_upper_body_svg, "12.1.19 Oberkörper", "Diagnose war ok"));

        moleRecyclerView = layout.findViewById(R.id.recyclerViewMoleImages);


        int currentUser = ((ImageFileArchive) Objects.requireNonNull(getActivity())).currentUser;


        fragmentCom =
                new ViewModelProvider(getActivity()).get(ViewModel_ImageArchive_Communication.class);

        extractingSelectedBodyPart();


        //TODO: Visueller Effekt einfügen für bessere Wiedererkennung, ob Hautläsion gefährlich oder nicht


        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        //((ImageFileArchive) Objects.requireNonNull(getActivity())).showBackButton(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ImageFileArchive)getActivity()).showBackButton(true);
    }

    private void extractingImagesAccordingBodyPart(String selectedBodyPart, List<EntityMix_User_MoleLib> moleLibCurrentUser) {

        moleItemList.clear();

        int colorCode = decodeColorCode(selectedBodyPart);

        if(colorCode != 0){

            for(EntityMix_User_MoleLib moleEntry : moleLibCurrentUser){
                if(moleEntry.mole_library.getMolePosColorCode() == colorCode){

                    moleItemList.add(new RecyclerViewMoleImageItem(
                            Integer.parseInt(moleEntry.mole_library.getMoleImageUri()),
                            moleEntry.mole_library.getDateMoleImageCreation(),
                            moleEntry.mole_library.getDiagnosis()));
                }
            }
            moleRecyclerViewAdapter.notifyDataSetChanged();

        }else{
            Toast.makeText(getActivity(), "Für die " + selectedBodyPart +" hast du noch keine Aufnahmen!", Toast.LENGTH_LONG).show();
        }

    }

    private int decodeColorCode(String selectedBodyPart) {
        int colorCode = 0;

        switch (selectedBodyPart){
            case Fragment_SelectBodyPart.BODYPART_HEAD:
                colorCode = -10000;
                break;
            case Fragment_SelectBodyPart.BODYPART_ARMS:
                colorCode = -20000;
                break;
            case Fragment_SelectBodyPart.BODYPART_BODY:
                colorCode = -30000;
                break;
            case Fragment_SelectBodyPart.BODYPART_LEGS:
                colorCode = -40000;
                break;
        }

        Log.d(TAG, " decodeColorCode: selectedBodyPart: " + selectedBodyPart + " plus corresponding ID " + colorCode);

        return colorCode;
    }

    private void extractingMoleLib() {
        fragmentCom.getUserMoleLib().observe(getViewLifecycleOwner(), new Observer<List<EntityMix_User_MoleLib>>() {
            @Override
            public void onChanged(List<EntityMix_User_MoleLib> entityMix_user_moleLibs) {
                moleLibCurrentUser = entityMix_user_moleLibs;
                extractingImagesAccordingBodyPart(selectedBodyPart, moleLibCurrentUser);

            }
        });
    }

    private void extractingSelectedBodyPart() {
        fragmentCom.getSelectedBodypart().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                selectedBodyPart = s;
                extractingMoleLib();
            }
        });
    }







}
