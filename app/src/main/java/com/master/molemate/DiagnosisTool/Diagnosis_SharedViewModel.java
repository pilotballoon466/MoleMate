package com.master.molemate.DiagnosisTool;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import java.util.Date;

public class Diagnosis_SharedViewModel extends ViewModel {

    private static final String TAG = "SharedViewModel_for_Fra";

    private MutableLiveData<Uri> imageMole = new MutableLiveData<Uri>();
    private MutableLiveData<Uri> imageMolePosition = new MutableLiveData<Uri>();
    private MutableLiveData<String> molePosInWords = new MutableLiveData<String>();
    private MutableLiveData<Integer> molePosColorCode = new MutableLiveData<Integer>();
    private MutableLiveData<Date> moleImageCreationDate = new MutableLiveData<Date>();
    private MutableLiveData<Boolean> isFront = new MutableLiveData<Boolean>();
    private MutableLiveData<String> diagnosis = new MutableLiveData<String>();
    private MutableLiveData<MoleMateDB_ViewModel> moleUserViewModel = new MutableLiveData<MoleMateDB_ViewModel>();

    public LiveData<MoleMateDB_ViewModel> getMoleUserViewModel() {
        return moleUserViewModel;
    }

    public void setMoleUserViewModel(MoleMateDB_ViewModel moleUserViewModel) {
        this.moleUserViewModel.setValue(moleUserViewModel);
    }

    public void setMoleImage(Uri moleImage){
        imageMole.setValue(moleImage);
    }

    public LiveData<Uri> getMoleImage(){
        Log.d(TAG, "getMoleImage: " + imageMole.getValue());
        return imageMole;
    }

    public void setMolePosImage(Uri imageMolePositionUri) {
        imageMolePosition.setValue(imageMolePositionUri);
    }

    public LiveData<Uri> getMolePosImage(){
        Log.d(TAG, "getMolePosImage: " + imageMolePosition.getValue());
        return imageMolePosition;
    }

    public void setMolePosInWords(String molePosString){
        molePosInWords.setValue(molePosString);
    }

    public LiveData<String> getMolePosInWords(){
        return molePosInWords;
    }

    public void setMolePosColorCode(int pixel) {
        molePosColorCode.setValue(pixel);
    }

    public LiveData<Integer> getMolePosColorCode(){
        Log.d(TAG, "getMolePosColorCode: " + molePosColorCode.getValue());
        return molePosColorCode;
    }

    public void setMoleImageCreationDate(Date creationDate){
        moleImageCreationDate.setValue(creationDate);
    }

    public LiveData<Date> getMoleImageCreationDate(){
        Log.d(TAG, "getMoleImageCreationDate: " + moleImageCreationDate.getValue());
        return moleImageCreationDate;
    }

    public void setIsFront(boolean isFront) {
        this.isFront.setValue(isFront);
    }

    public LiveData<Boolean> getIsFront(){
        return isFront;
    }

    public void setDiagnosis(String diagnosis){
        this.diagnosis.setValue(diagnosis);
    }

    public LiveData<String> getDiagnosis(){
        return diagnosis;
    }

    public void deleteAllValues(){
        Log.d(TAG, "deleteAllValues is called");
        imageMole.setValue(null);
        imageMolePosition.setValue(null);
        molePosInWords.setValue("");
        molePosColorCode.setValue(0);
        moleImageCreationDate.setValue(null);
        isFront.setValue(true);
        diagnosis.setValue("");

    }
}
