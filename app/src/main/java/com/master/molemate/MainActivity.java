package com.master.molemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import com.master.molemate.HomeScreen.HomeScreen;
import com.master.molemate.LoginProcess.LoginActivity;
import com.master.molemate.RoomDB.DAO_Interfaces.DAO_Interface_Mole_Library;
import com.master.molemate.RoomDB.Entities.EntityMix_User_MoleLib;
import com.master.molemate.RoomDB.Entities.Entity_Mole_Library;
import com.master.molemate.RoomDB.Entities.Entity_Users;
import com.master.molemate.RoomDB.MoleMateDB;
import com.master.molemate.RoomDB.MoleMateDB_Repository;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MoleMateDB_ViewModel moleMateDBViewModel;
    private Entity_Users user;
    private List<EntityMix_User_MoleLib> moleUserMix;
    private String userMail;
    private static int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Erstelle statische Klasse für die unteren drei Aufrufe
        //dbFactory in der Art, die die Instance immer wieder anfragt

        Intent tmpIntent = getIntent();

        userMail = tmpIntent.getStringExtra("mail");

        moleMateDBViewModel = new ViewModelProvider(this).get(MoleMateDB_ViewModel.class);

        moleMateDBViewModel.getUserByMail(userMail).observe(this, new Observer<Entity_Users>() {
            @Override
            public void onChanged(Entity_Users entity_users) {
                user = entity_users;
                if(user != null) {

                    uid = user.getUid();

                    Log.d(TAG, "collectedAllUsers() size: " + user.getUserName() + " with id " + uid);

                    getMolesForUser(uid);

                }else{
                    Log.d(TAG, "No User!");

                    new PopulatDefaultUserToDBAsyncTask(moleMateDBViewModel).execute();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        Log.d(TAG, "onCreate: This should be the User! " + uid);

    }

    private void getMolesForUser(int uid) {
        final int tmpUid = uid;
        moleMateDBViewModel.getAllMolesFromUser(uid).observe(this, new Observer<List<EntityMix_User_MoleLib>>() {
            @Override
            public void onChanged(List<EntityMix_User_MoleLib> entityMix_user_moleLibs) {
                moleUserMix = entityMix_user_moleLibs;
                if(moleUserMix.size() == 0){
                    //new PopulateMoleDBAsyncTask(moleMateDBViewModel, tmpUid).execute();

                }

                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                intent.putExtra("currentUser", tmpUid);
                startActivity(intent);


            }
        });

    }

    private static class PopulateMoleDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private MoleMateDB_ViewModel moleLibDao;
        private int uid;

        private PopulateMoleDBAsyncTask(MoleMateDB_ViewModel moleUserDB, int uid){
            this.moleLibDao = moleUserDB;
            this.uid = uid;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            moleLibDao.insertMole(new Entity_Mole_Library(
                    "12.01.12 13:12.14",
                    uid,
                    "2131165277",
                    "2131165277",
                    -10000,
                    "Bein",
                    false,
                    true,
                    "Es scheint alles OK zu sein",
                    99.9
            ));
            moleLibDao.insertMole(new Entity_Mole_Library(
                    "20.01.12 13:12.14",
                    uid,
                    "2131165317",
                    "2131165317",
                    -20000,
                    "Hand",
                    false,
                    true,
                    "Es scheint alles OK zu sein",
                    90.9
            ));
            moleLibDao.insertMole(new Entity_Mole_Library(
                    "28.01.12 13:12.14",
                    uid,
                    "2131165295",
                    "2131165295",
                    -30000,
                    "Bauch",
                    false,
                    true,
                    "Es scheint alles OK zu sein",
                    87.9
            ));
            moleLibDao.insertMole(new Entity_Mole_Library(
                    "30.01.12 13:12.14",
                    uid,
                    "2131165318",
                    "2131165318",
                    -40000,
                    "Kopf",
                    false,
                    true,
                    "Es scheint nicht alles OK zu sein",
                    22.9
            ));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: inserted example Mole Entries");
        }
    }

    private static class PopulatDefaultUserToDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private MoleMateDB_ViewModel moleLibDao;

        private PopulatDefaultUserToDBAsyncTask(MoleMateDB_ViewModel moleUserDB){
            this.moleLibDao = moleUserDB;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            moleLibDao.insertUser(new Entity_Users(
                    "default",
                    "default",
                    "default",
                    "default@default.com"
            ));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: inserted default User");
        }
    }


}
