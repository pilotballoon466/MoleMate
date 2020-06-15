package com.master.molemate.DiagnosisTool.DiagnosisFragments;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.master.molemate.DiagnosisTool.Diagnosis_SharedViewModel;
import com.master.molemate.DiagnosisTool.Diagnosis_Tool;
import com.master.molemate.HomeScreen.HomeScreen;
import com.master.molemate.R;
import com.master.molemate.RoomDB.Entities.Entity_Mole_Library;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.TypeVariable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Fragment_Diagnosis extends Fragment {

    // presets for rgb conversion; Example Values from tensorflow.org
    private static final int RESULTS_TO_SHOW = 3;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    private int IMAGE_PIXEL_SIZE = 3;
    private int IMAGE_SIZE = 200;
    private int IMAGE_HEIGHT = IMAGE_SIZE, IMAGE_WIDTH = IMAGE_SIZE;

    //options for model Interpreter
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    //tfLite Graph
    private Interpreter tflite;
    private List<String> labelList;
    private ByteBuffer imageData=null;
    // holds the probabilities of each label for non-quantized graphs
    private float[][] labelProbArray = null;
    // holds the probabilities of each label for quantized graphs
    private byte[][] labelProbArrayByte = null;
    // array that holds the labels with the highest probabilities
    private String[] topLables = null;
    // array that holds the highest probabilities
    private String[] topConfidence = null;

    // selected classifier information received from extras
    private String chosen;
    private boolean quant;

    // int array to hold image data
    private int[] imageInInteger;


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

    // priority queue that will hold the top results from the CNN
    private PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_diagnosis, container, false);

        //just to test, whether i need this or not!
        quant = false;

        saveButton = layout.findViewById(R.id.fragment_go_to_save_button);
        cancelButton = layout.findViewById(R.id.fragment_save_cancelButton);
        moleImageView = layout.findViewById(R.id.fragment_diagnosis_mole_image);
        diagnosisTextView = layout.findViewById(R.id.fragment_diagnosis_text);
        diagnosisProcentHeaderView = layout.findViewById(R.id.fragment_diagnosis_procent_header);
        diagnosisProcentView = layout.findViewById(R.id.fragment_diagnosis_procent);
        diagnosisTextHeader = layout.findViewById(R.id.fragment_diagnosis_text_header);

        settingUpSaveButton();



        return layout;
    }

    private void printTopKLabels() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size()-1; ++i) {
            if(quant){
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayByte[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }

        String topLable = "";
        String topConfidence = "";

        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; ++i) {
            if(i==0) {
                Map.Entry<String, Float> label = sortedLabels.poll();
                topLable = label.getKey();
                tmpDiagnosisProb = label.getValue()*100;
                topConfidence = String.format("%.0f%%", tmpDiagnosisProb);
            }
        }

        dataContainer.setDiagnosis(topLable + " " + topConfidence);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Diagnosis_Tool) Objects.requireNonNull(getActivity())).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        diagnosisProcentHeaderView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width * 0.08));
        diagnosisProcentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width * 0.1));
        diagnosisTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width*0.03));
        diagnosisTextHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width*0.08));

        if(tmpDiagnosisProb>60){
            diagnosisProcentHeaderView.setText(R.string.lookInto);
            diagnosisProcentView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorNegativ, null));
            diagnosisTextView.setText(R.string.diagnosisMalignant);
        }else{
            diagnosisProcentHeaderView.setText(R.string.begnin);
            diagnosisProcentView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPositiv, null));
            diagnosisTextView.setText(R.string.diagnosisBegnin);
        }

        diagnosisProcentView.setText(topConfidence);
        tmpDiagnosisText=(String)diagnosisTextView.getText();

    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imageData == null) {
            return;
        }
        imageData.rewind();
        bitmap.getPixels(imageInInteger, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // loop through all pixels
        int pixel = 0;
        for (int i = 0; i < IMAGE_WIDTH; ++i) {
            for (int j = 0; j < IMAGE_HEIGHT; ++j) {
                final int val = imageInInteger[pixel++];
                // get rgb values from intValues where each int holds the rgb values for a pixel.
                // if quantized, convert each rgb value to a byte, otherwise to a float
                if(quant){
                    imageData.put((byte) ((val >> 16) & 0xFF));
                    imageData.put((byte) ((val >> 8) & 0xFF));
                    imageData.put((byte) (val & 0xFF));
                } else {
                    imageData.putFloat((((val >> 16) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                    imageData.putFloat((((val >> 8) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                    imageData.putFloat((((val) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                }

            }
        }
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    private List<String> loadLabelList() throws IOException{
        List<String> labelList = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(getContext().getAssets().open("labels.txt")));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    private MappedByteBuffer loadModelFile() throws IOException{
        AssetFileDescriptor fileDescriptor = getContext().getAssets().openFd("new_classification.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void settingUpSaveButton() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkData()){
                    moleEntry = new Entity_Mole_Library(
                            tmpDateOfCreation,
                            1,
                            tmpUriMoleImage.getPath(),
                            tmpUriMolePosBitmap.getPath(),
                            -10000,
                            tmpMolePosText,
                            false,
                            tmpIsFrontside,
                            tmpDiagnosisText,
                            100-tmpDiagnosisProb);

                    dbHandler.insertMole(moleEntry);

                    dataContainer.deleteAllValues();

                    ((Diagnosis_Tool) Objects.requireNonNull(getActivity())).selectFragmentToShowWithTitle(Diagnosis_Tool.TAKE_IMAGE);

                }
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

        }else if(tmpDiagnosisText.equals("")){
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

        ((Diagnosis_Tool)getActivity()).showBackButton(true);

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

                Intent intent = new Intent(getActivity(), HomeScreen.class);
                startActivity(intent);

            }
        });
    }
}
