package com.master.molemate.RoomDB;

import android.app.Application;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.master.molemate.RoomDB.DAO_Interfaces.DAO_Interface_Mole_Library;
import com.master.molemate.RoomDB.DAO_Interfaces.DAO_Interface_Users;
import com.master.molemate.RoomDB.Entities.EntityMix_User_MoleLib;
import com.master.molemate.RoomDB.Entities.Entity_Mole_Library;
import com.master.molemate.RoomDB.Entities.Entity_Users;

import java.util.ArrayList;
import java.util.List;

/*

responsible for data handling of RoomDB!
Used because it is good style using another layer between the actual app and the db doing the Data handling

If I'll use an API call I should do it again with an Repository file!

 */

public class MoleMateDB_Repository {

    private static final String TAG = "MoleMateDB_Repository";

    private DAO_Interface_Mole_Library moleLibDao;
    private DAO_Interface_Users userDao;

    //Have to check these being static, cause every change to these Vars even in other classes will effect
    //these vars
    private static Entity_Users defaultUser;
    private static Entity_Users currentUser;
    private LiveData<List<Entity_Users>> users;
    private LiveData<List<EntityMix_User_MoleLib>> moleLibFromUser;
    private LiveData<Entity_Users> activeUser;
    private LiveData<List<Entity_Mole_Library>> molesById;
    private static int uid;

    public MoleMateDB_Repository(Application application) {
        MoleMateDB moleMateDB = MoleMateDB.getInstance(application);
        moleLibDao = moleMateDB.moleLibDAO();
        userDao = moleMateDB.usersDAO();

        users = userDao.getAllUser();
    }

    void insertUser(Entity_Users userEntry){
        new InsertUserAsyncTask(userDao).execute(userEntry);
    }

    LiveData<Entity_Users> getUserByMail(String mail){
        if(uid == 0) {
            activeUser = userDao.getUserByMail(mail);
            if(activeUser.getValue() != null) {
                uid = activeUser.getValue().getUid();
                Log.d(TAG, "getUserByMail: uid " + uid);
            }
        }
        return activeUser;

    }

    void deleteUser(Entity_Users user){
        new DeleteUserAsyncTask(userDao).execute(user);
    }


    LiveData<List<Entity_Users>> getAllUsers(){
        if(users == null){
            users = userDao.getAllUser();
        }
        return users;
    }

    void insertMole(Entity_Mole_Library moleLib_entry) {
        new InsertMoleAsyncTask(moleLibDao).execute(moleLib_entry);
    }

    void updateMole(Entity_Mole_Library moleLib_entry){
        new UpdateMoleAsyncTask(moleLibDao).execute(moleLib_entry);
    }

    void deleteMole(Entity_Mole_Library moleLib_entry){
        new DeleteMoleAsyncTask(moleLibDao).execute(moleLib_entry);
    }

    LiveData<List<EntityMix_User_MoleLib>> getAllMolesFromUser(int uid){
        //TODO: sollte nur einmal zugewiesen werden!
        Log.d(TAG, "getAllMolesFromUser: Trigered getting all Moles");
        moleLibFromUser = moleLibDao.getAllMolesFromUser(uid);
        return moleLibFromUser;
    }

    LiveData<List<Entity_Mole_Library>> getAllMolesFromMoleIds(int[] moleIDs){
        molesById = moleLibDao.loadAllMolesFromMoleIds(moleIDs);
        return molesById;
    }

    private static class InsertUserAsyncTask extends AsyncTask<Entity_Users,Void,Void>{

        private DAO_Interface_Users userDao;

        private InsertUserAsyncTask(DAO_Interface_Users userDao) {
            this.userDao = userDao;

        }

        @Override
        protected Void doInBackground(Entity_Users... entity_users) {
            userDao.insert(entity_users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG, "onPostExecute: inserted user");
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<Entity_Users,Void,Void>{

        private DAO_Interface_Users userDao;
        private String userName;

        private DeleteUserAsyncTask(DAO_Interface_Users userDao) {
            this.userDao = userDao;

        }

        @Override
        protected Void doInBackground(Entity_Users... entity_users) {
            userName = entity_users[0].getUserName();
            userDao.delete(entity_users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG, "onPostExecute: deleted user" + userName);
        }
    }

    private static class InsertMoleAsyncTask extends AsyncTask<Entity_Mole_Library,Void,Void>{

        private DAO_Interface_Mole_Library moleLibDao;
        private String moleUri;

        private InsertMoleAsyncTask(DAO_Interface_Mole_Library moleLibDao){
            this.moleLibDao = moleLibDao;
        }

        @Override
        protected Void doInBackground(Entity_Mole_Library... entity_mole_libraries) {
            moleUri = entity_mole_libraries[0].getMoleImageUri();
            moleLibDao.insert(entity_mole_libraries[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG, "onPostExecute: Inserted Mole " + moleUri);
        }
    }

    private static class DeleteMoleAsyncTask extends AsyncTask<Entity_Mole_Library,Void,Void>{

        private DAO_Interface_Mole_Library moleLibDao;
        private String moleUri;

        private DeleteMoleAsyncTask(DAO_Interface_Mole_Library moleLibDao){
            this.moleLibDao = moleLibDao;
        }

        @Override
        protected Void doInBackground(Entity_Mole_Library... entity_mole_libraries) {
            moleUri = entity_mole_libraries[0].getMoleImageUri();
            moleLibDao.delete(entity_mole_libraries[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG, "onPostExecute: Deleted Mole " + moleUri);
        }
    }

    private static class UpdateMoleAsyncTask extends AsyncTask<Entity_Mole_Library, Void, Void>{

        private DAO_Interface_Mole_Library moleLibDao;
        private String moleUri;

        private UpdateMoleAsyncTask(DAO_Interface_Mole_Library moleLibDao){
            this.moleLibDao = moleLibDao;
        }

        @Override
        protected Void doInBackground(Entity_Mole_Library... entity_mole_libraries) {
            moleUri = entity_mole_libraries[0].getMoleImageUri();
            moleLibDao.update(entity_mole_libraries[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG, "onPostExecute: Update Mole " + moleUri);
        }
    }
}