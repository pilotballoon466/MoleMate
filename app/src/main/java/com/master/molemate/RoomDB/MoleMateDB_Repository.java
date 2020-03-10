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
    private static int uid;

    public MoleMateDB_Repository(MoleMateDB moleMateDB) {
        moleLibDao = moleMateDB.moleLibDAO();
        userDao = moleMateDB.usersDAO();

        users = userDao.getAllUser();

    }

    public void insertUser(Entity_Users userEntry){
        new InsertUserAsyncTask(userDao).execute(userEntry);
    }

    public LiveData<List<Entity_Users>> getAllUsers(){
        return users;
    }

    public void insertMole(Entity_Mole_Library moleLib_entry) {
        new InsertMoleAsyncTask(moleLibDao).execute(moleLib_entry);
    }

    public LiveData<List<EntityMix_User_MoleLib>> getAllMolesOfUser(int uid){
        return moleLibDao.getAllMolesFromUser(uid);
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

    private static class InsertMoleAsyncTask extends AsyncTask<Entity_Mole_Library,Void,Void>{

        private DAO_Interface_Mole_Library moleLibDao;

        private InsertMoleAsyncTask(DAO_Interface_Mole_Library moleLibDao){
            this.moleLibDao = moleLibDao;
        }

        @Override
        protected Void doInBackground(Entity_Mole_Library... entity_mole_libraries) {
            moleLibDao.insert(entity_mole_libraries[0]);
            return null;
        }
    }
}