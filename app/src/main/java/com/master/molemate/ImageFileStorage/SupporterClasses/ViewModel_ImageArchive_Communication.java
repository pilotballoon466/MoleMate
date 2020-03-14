package com.master.molemate.ImageFileStorage.SupporterClasses;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.master.molemate.RoomDB.Entities.EntityMix_User_MoleLib;

import java.util.List;

public class ViewModel_ImageArchive_Communication extends ViewModel {

    private MutableLiveData<String> selectedBodypart = new MutableLiveData<>();
    private MutableLiveData<Integer> currentUser = new MutableLiveData<>();
    private MutableLiveData<List<EntityMix_User_MoleLib>> userMoleLib = new MutableLiveData<>();

    public LiveData<String> getSelectedBodypart() {
        return selectedBodypart;
    }

    public void setSelectedBodypart(String selectedBodypart) {
        this.selectedBodypart.setValue(selectedBodypart);
    }

    public Integer getCurrentUser() {
        return currentUser.getValue();
    }

    public void setCurrentUser(Integer currentUser) {
        this.currentUser.setValue(currentUser);
    }

    public LiveData<List<EntityMix_User_MoleLib>> getUserMoleLib() {
        return userMoleLib;
    }

    public void setUserMoleLib(List<EntityMix_User_MoleLib> userMoleLib) {
        this.userMoleLib.setValue(userMoleLib);
    }
}
