package com.master.molemate.DiagnosisTool.DiagnosisFragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.master.molemate.DiagnosisTool.ColorCodeConverter;
import com.master.molemate.DiagnosisTool.Diagnosis_SharedViewModel;
import com.master.molemate.DiagnosisTool.Diagnosis_Tool;
import com.master.molemate.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Fragment_Determine_Mole_Position extends Fragment {

    private static final String TAG = "Fragment_Determine_Mole_Pos";

    private Diagnosis_SharedViewModel dataContainer;
    private Date dateOfCreation;

    private Button save;
    private ImageView blackBodyView;
    private ImageView coloredBodyView;
    private TextView front;
    private TextView spine;
    private TextView selectionOfBodyPart;

    private Resources res;
    private Drawable rectangle;
    private Bitmap coloredBodyBitmap;
    private Bitmap blackBodyBitmap;
    private int pixel;

    private String molePosString;
    private int colorPrimary;
    private int btnText;    //Color of Button Text
    private boolean isFront; // Boolean for checking whether it is the Front or the Back of the Body
    private boolean isMolePosSet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_determine_mole_position, container, false);

        save = layout.findViewById(R.id.button_save);

        coloredBodyView = layout.findViewById(R.id.fragment_colored_body_position);
        blackBodyView = layout.findViewById(R.id.fragment_body_position);

        front = layout.findViewById(R.id.butto_front_body);
        spine = layout.findViewById(R.id.button_back_body);
        selectionOfBodyPart = layout.findViewById(R.id.fragment_clicked_body_part);

        coloredBodyBitmap = ((BitmapDrawable)coloredBodyView.getDrawable()).getBitmap();
        blackBodyBitmap = ((BitmapDrawable) blackBodyView.getDrawable()).getBitmap();

        isFront = true;
        isMolePosSet = false;
        res = getResources();

        rectangle = ResourcesCompat.getDrawable(res, R.drawable.box_borders, null);
        btnText = ResourcesCompat.getColor(res, R.color.btnTextColor, null);
        colorPrimary = ResourcesCompat.getColor(res, R.color.colorPrimary, null);

        setUpSaveButton();
        setUpFrontToBackTextViewsOnClick();
        setUpMolePosTouchListener();

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: getActivity can be null, need a try catch or something similar
        dataContainer = new ViewModelProvider(getActivity()).get(Diagnosis_SharedViewModel.class);

        dataContainer.getMoleImageCreationDate().observe(getViewLifecycleOwner(), new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                dateOfCreation = date;
            }
        });
    }

    private void setUpMolePosTouchListener() {
        blackBodyView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Bitmap copyBlackBodyBitmap = blackBodyBitmap.copy(Bitmap.Config.ARGB_8888, true);

                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                pixel = coloredBodyBitmap.getPixel(x,y);

                //Creating white Dot on Body
                for(int xCoord = x-8; xCoord<x+8; xCoord++){
                    for(int yCoord = y-8; yCoord<y+8; yCoord++ ){
                        if(yCoord > 0 && yCoord < blackBodyBitmap.getHeight() && xCoord > 0 && xCoord < blackBodyBitmap.getWidth()) {
                            copyBlackBodyBitmap.setPixel(xCoord, yCoord, Color.WHITE);
                        }
                    }
                }

                if(pixel != 0){
                    String molePos = setMolePosString(pixel);
                    if(molePos != "") {
                        selectionOfBodyPart.setText("Ausgewählter Bereich: " + molePos);
                        if (selectionOfBodyPart.getVisibility() == View.INVISIBLE) {
                            selectionOfBodyPart.setVisibility(View.VISIBLE);
                        }
                        blackBodyView.setImageBitmap(copyBlackBodyBitmap);
                    }else{
                        if (selectionOfBodyPart.getVisibility() == View.VISIBLE) {
                            selectionOfBodyPart.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                return false;
            }
        });
    }


    private void setUpSaveButton() {

        //TODO: Fehler bei erstmaligen Auawählen der Position, muss manchmal zweimal ausgewählt werden! Meist bei Hüfte oder Bauch!
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pixel != 0 && isMolePosSet) {
                    ((Diagnosis_Tool)getActivity()).selectFragmentToShowWithTitle("diagnosis");

                    Bitmap molePosBitmap = ((BitmapDrawable)blackBodyView.getDrawable()).getBitmap();
                    Uri molePosImageUri = saveMolePosBitmapAsJPG(molePosBitmap);

                    dataContainer.setMolePosInWords(molePosString);
                    dataContainer.setMolePosColorCode(pixel);
                    Log.d(TAG, "onClick: MolsPosColor: " + pixel);
                    dataContainer.setIsFront(isFront);
                    if(molePosImageUri != null) {
                        dataContainer.setMolePosImage(molePosImageUri);
                    }else{
                        Toast.makeText(
                                getActivity(),
                                "Die Position des Muttermals konnte nicht abgespeichert werden!",
                                Toast.LENGTH_LONG).show();
                        ((Diagnosis_Tool)getActivity()).selectFragmentToShow(0);
                    }

                }
                else {
                    Toast.makeText(getActivity(), "Du hast die Position deiner Hautläsion noch nicht bestimmt!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private Uri saveMolePosBitmapAsJPG(Bitmap molePosBitmap) {

        Uri molePosImageUri;

        try {
            if (molePosBitmap != null) {

                File storageMoleDir = ((Diagnosis_Tool)getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                String moleImageFileName;

                if(dateOfCreation == null){
                    throw new RuntimeException("Es konnte kein Erschaffungsdatum ausgelesen werden");
                }else{
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmsss").format(dateOfCreation);
                    moleImageFileName = "MOLE_POS" + timeStamp + "_";

                }

                File molePosBitmapToJPGFile =new File (""+storageMoleDir+ "/" + moleImageFileName +".jpg");

                molePosImageUri = FileProvider.getUriForFile(
                        getActivity(),
                        "com.example.cancerfree.fileprovider",
                        molePosBitmapToJPGFile
                );

                try(FileOutputStream fileOutputStream = new FileOutputStream(molePosBitmapToJPGFile)) {

                    if (molePosBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                        Toast saved = Toast.makeText(getActivity(), "Image saved.", Toast.LENGTH_SHORT);
                        saved.show();
                        return molePosImageUri;
                    } else {
                        Toast unsaved = Toast.makeText(getActivity(), "Image not save.", Toast.LENGTH_SHORT);
                        unsaved.show();
                        return null;
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }catch(Exception e){
            Log.e("saveMolePosBitmapAsJPG()", e.getMessage());
            return null;
        }
        return null;
    }


    private String setMolePosString(Integer pixel) {
        String[]molePosFrontAndSpine;

        molePosFrontAndSpine = ColorCodeConverter.getBodyPartFromColor(pixel);

        if(molePosFrontAndSpine != null){
            molePosString = (isFront)? molePosFrontAndSpine[0]: molePosFrontAndSpine[1];
            isMolePosSet = true;
        }else{
            Toast.makeText(getActivity(), "Deine Auswahl konnte keim Körperteil zugeordnet werden!", Toast.LENGTH_LONG).show();
            isMolePosSet = false;
            molePosString = "";
        }

        return molePosString;
    }


    /**
     * Setting up the TextViews to Change appearance on click + adjusting boolean according to the clicked TextView
     */
    private void setUpFrontToBackTextViewsOnClick() {
        //TODO Bei Änderungen muss das Textfeld auch geupdatet werden!
        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                front.setTextColor(btnText);
                front.setBackgroundResource(R.color.colorPrimary);
                spine.setTextColor(colorPrimary);
                spine.setBackgroundResource(R.color.btnTextColor);

                if(rectangle != null){
                    spine.setBackground(rectangle);
                }

                isFront = true;

            }
        });

        spine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spine.setTextColor(btnText);
                spine.setBackgroundResource(R.color.colorPrimary);
                front.setTextColor(colorPrimary);
                front.setBackgroundResource(R.color.btnTextColor);

                if(rectangle != null){
                    front.setBackground(rectangle);
                }

                isFront = false;
            }
        });

    }
}
