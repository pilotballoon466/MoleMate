package com.master.molemate.DiagnosisTool.DiagnosisFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.master.molemate.DiagnosisTool.Diagnosis_SharedViewModel;
import com.master.molemate.DiagnosisTool.Diagnosis_Tool;
import com.master.molemate.R;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;

public class Fragment_Take_Picture extends Fragment {

    private static final String TAG = "Fragment_Take_Picture";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_LOAD_IMG = 2;

    private Date dateOfCreation = new Date();
    private String timeStamp;

    private Diagnosis_SharedViewModel viewModel;
    private MoleMateDB_ViewModel moleDBHandler;

    private String currentPhotoPath;
    private Uri moleImageUri;
    private CardView takePicCardV;
    private CardView loadPicCardV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_take_picture, container, false);

        takePicCardV = layout.findViewById(R.id.cardviewTakePic);
        loadPicCardV = layout.findViewById(R.id.cardviewLoadPic);

        settingUpButtons();
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmsss").format(dateOfCreation);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        moleDBHandler = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(MoleMateDB_ViewModel.class);

        viewModel = new ViewModelProvider(getActivity()).get(Diagnosis_SharedViewModel.class);
        viewModel.getMoleImage().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(@Nullable Uri uriMoleImage) {

                if(uriMoleImage != null) {
                    Toast.makeText(getActivity(), "Es ist bereits ein Bild aufgenommen worden.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Es konnte kein Bild gefunden werden.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                //image.setImageURI(moleImageUri);
                Log.d(TAG, "onActivityResult: moleImageUri" + moleImageUri);
                viewModel.setMoleImage(moleImageUri);
                Log.d(TAG, "onActivityResult: dateOfCreation" + dateOfCreation.toString());
                viewModel.setMoleImageCreationDate(dateOfCreation);
                viewModel.setMoleUserViewModel(moleDBHandler);
                ((Diagnosis_Tool) Objects.requireNonNull(getActivity())).selectFragmentToShowWithTitle(Diagnosis_Tool.CHECK_IMAGE);


            } else if (requestCode == RESULT_LOAD_IMG) {

                final Uri imageUri = data.getData();

                if(imageUri != null){
                    Log.d(TAG, "onActivityResult: moleImageUri" + imageUri);
                    viewModel.setMoleImage(imageUri);
                    Log.d(TAG, "onActivityResult: dateOfCreation" + dateOfCreation.toString());
                    viewModel.setMoleImageCreationDate(dateOfCreation);
                    viewModel.setMoleUserViewModel(moleDBHandler);
                    ((Diagnosis_Tool) Objects.requireNonNull(getActivity())).selectFragmentToShowWithTitle(Diagnosis_Tool.CHECK_IMAGE);
                } else {
                    Toast.makeText(getActivity(), "Du hast wohl kein Bild ausgewählt",Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(getActivity(), "Da ist ein Fehler aufgetreten, versuche es bitte nochmal",Toast.LENGTH_LONG).show();
            }

        }
        //image.setImageURI(moleImageUri);
    }


    private void settingUpButtons() {
        takePicCardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeMoleImage();
            }
        });

        loadPicCardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Es ist bereits ein Bild aufgenommen worden.", Toast.LENGTH_SHORT).show();

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
    }


    private void dispatchTakeMoleImage() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            File moleImageFile = null;

            try {
                moleImageFile = createImageFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (NullPointerException nullPointerException) {
                nullPointerException.printStackTrace();
            }

            if (moleImageFile != null) {
                moleImageUri = FileProvider.getUriForFile(
                        getActivity(),
                        "com.master.molemate.fileprovider",
                        moleImageFile);

                Log.d(TAG, "dispatchTakeMoleImage: MoleImageURI " + moleImageUri);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, moleImageUri);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);

                // Tell the media scanner about the new file so that it is
                // immediately available to the user.
                MediaScannerConnection.scanFile(getActivity(), new String[]{moleImageFile.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
                //((TakeMolePicAssistent) getActivity()).selectFragmentToShow(0);
            }
        }

    }


    private File createImageFile() throws IOException, NullPointerException {


        String moleImageFileName = "MOLE_" + timeStamp + "_";


        //TODO what is @notNullable or @Nullable
        File storageMoleDir = ((Diagnosis_Tool) Objects.requireNonNull(getActivity())).getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        Log.d(TAG, "createImageFile: storeMoleDir " + Objects.requireNonNull(storageMoleDir).getAbsolutePath());
        Log.d(TAG, "createImageFile: moleImageFileName " + moleImageFileName);

        File moleImageFile = new File(""+storageMoleDir+"/"+moleImageFileName+".jpg");

        currentPhotoPath = moleImageFile.getAbsolutePath();

        Log.d(TAG, "createImageFile: currentPhotoPath: " + currentPhotoPath);

        return moleImageFile;
    }



}
