package com.master.molemate.DiagnosisTool.DiagnosisFragments;

import android.app.Activity;
import android.content.Intent;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.master.molemate.DiagnosisTool.Diagnosis_SharedViewModel;
import com.master.molemate.DiagnosisTool.Diagnosis_Tool;
import com.master.molemate.R;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Fragment_Take_Picture extends Fragment {

    private static final String TAG = "Fragment_Take_Picture";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int TAKE_PHOTO = 1;


    private Diagnosis_SharedViewModel viewModel;
    private MoleMateDB_ViewModel moleDBHandler;

    private ImageView image;
    private String currentPhotoPath;
    private Uri moleImageUri;
    private Button btnTakePic;
    private Date dateOfCreation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_take_picture, container, false);

        btnTakePic = (Button) layout.findViewById(R.id.btnTakePicture);
        image = (ImageView) layout.findViewById(R.id.logoTakePic);

        settingUpTakePicButton();

        dispatchTakeMoleImage();

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        moleDBHandler = new ViewModelProvider(getActivity()).get(MoleMateDB_ViewModel.class);

        viewModel = new ViewModelProvider(getActivity()).get(Diagnosis_SharedViewModel.class);
        viewModel.getMoleImage().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(@Nullable Uri uriMoleImage) {

                if(uriMoleImage != null) {
                    image.setImageURI(uriMoleImage);
                }else{
                    Toast.makeText(getActivity(), "Es konnte kein Bild gefunden werden.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {
                image.setImageURI(moleImageUri);

                Log.d(TAG, "onActivityResult: moleImageUri" + moleImageUri);
                viewModel.setMoleImage(moleImageUri);
                Log.d(TAG, "onActivityResult: dateOfCreation" + dateOfCreation.toString());
                viewModel.setMoleImageCreationDate(dateOfCreation);
                viewModel.setMoleUserViewModel(moleDBHandler);

                ((Diagnosis_Tool)getActivity()).selectFragmentToShow(1);
            }
        }
        //image.setImageURI(moleImageUri);
    }


    private void settingUpTakePicButton() {
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void dispatchTakeMoleImage() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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

        dateOfCreation = new Date();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmsss").format(dateOfCreation);
        String moleImageFileName = "MOLE_" + timeStamp + "_";


        //TODO what is @notNullable or @Nullable
        File storageMoleDir = ((Diagnosis_Tool)getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        Log.d(TAG, "createImageFile: storeMoleDir " + storageMoleDir.getAbsolutePath());
        Log.d(TAG, "createImageFile: moleImageFileName " + moleImageFileName);

        File moleImageFile = new File(""+storageMoleDir+"/"+moleImageFileName+".jpg");

        currentPhotoPath = moleImageFile.getAbsolutePath();

        Log.d(TAG, "createImageFile: currentPhotoPath: " + currentPhotoPath);

        return moleImageFile;
    }



}
