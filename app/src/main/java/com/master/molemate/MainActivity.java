package com.master.molemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

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

    private MoleMateDB moleMateDB;
    private MoleMateDB_Repository moleMateDBRepository;
    private MoleMateDB_ViewModel moleMateDBViewModel;
    private List<Entity_Users>users;
    private List<EntityMix_User_MoleLib> moleUserMix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Erstelle statische Klasse für die unteren drei Aufrufe
        //dbFactory in der Art, die die Instance immer wieder anfragt

        moleMateDBViewModel = new ViewModelProvider(this).get(MoleMateDB_ViewModel.class);

        moleMateDBViewModel.getAllUsers().observe(this, new Observer<List<Entity_Users>>() {
            @Override
            public void onChanged(List<Entity_Users> entity_users) {
                users = entity_users;
                if(users.size() != 0) {
                    Log.d(TAG, "collectedAllUsers() size: " + users.size() + " with id " + users.get(0).getUid());

                    getMolesForUser(users.get(0).getUid());

                }else{
                    Log.d(TAG, "No User!");
                }

            }
        });
    }

    private void getMolesForUser(int uid) {
        final int tmpUid = uid;
        moleMateDBViewModel.getAllMolesFromUser(uid).observe(this, new Observer<List<EntityMix_User_MoleLib>>() {
            @Override
            public void onChanged(List<EntityMix_User_MoleLib> entityMix_user_moleLibs) {
                moleUserMix = entityMix_user_moleLibs;
                if(moleUserMix.size() == 0){
                    new PopulateMoleDBAsyncTask(moleMateDBViewModel, tmpUid).execute();

                }

                Intent intent = new Intent(getApplicationContext(), ChooseActionScreen.class);
                intent.putExtra("currentUser", users.get(0).getUid());
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


}
