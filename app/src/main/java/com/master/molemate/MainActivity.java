package com.master.molemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "The User " + users.get(0).getUid()+"", Toast.LENGTH_LONG).show();

                moleMateDBViewModel.getAllMolesFromUser(users.get(0).getUid()).observe(MainActivity.this, new Observer<List<EntityMix_User_MoleLib>>() {
                    @Override
                    public void onChanged(List<EntityMix_User_MoleLib> entityMix_user_moleLibs) {
                        moleUserMix = entityMix_user_moleLibs;
                        Toast.makeText(getApplicationContext(), "The mole"
                                + moleUserMix.get(0).mole_library.getMoleID()
                                + " and the corresponding User "
                                + moleUserMix.get(0).user.getUid()
                                + "", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button buttonMoles = findViewById(R.id.button2);
        buttonMoles.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                    Toast.makeText(getApplicationContext(), "The mole"
                            + moleUserMix.get(0).mole_library.getMoleID()
                            + " and the corresponding User "
                            + moleUserMix.get(0).user.getUid()
                            + " and the size of moleMix "
                            + moleUserMix.size()
                            + ""
                            , Toast.LENGTH_SHORT).show();

            }
        });

        moleMateDB = moleMateDB.getInstance(this);
        moleMateDBRepository = new MoleMateDB_Repository(moleMateDB);
        moleMateDBViewModel = new MoleMateDB_ViewModel(moleMateDBRepository);

        moleMateDBViewModel.getAllUsers().observe(this, new Observer<List<Entity_Users>>() {
            @Override
            public void onChanged(List<Entity_Users> entity_users) {
                users = entity_users;
                new PopulateExampleMolesAsyncTask(moleMateDBViewModel).execute(users.get(0).getUid());
                Toast.makeText(MainActivity.this, "onChange() " + users.get(0).getUid() +"", Toast.LENGTH_LONG).show();

            }
        });
    }

    private static class PopulateExampleMolesAsyncTask extends AsyncTask<Integer, Void, Void> {
        private MoleMateDB_ViewModel moleLibrary;

        private PopulateExampleMolesAsyncTask(MoleMateDB_ViewModel moleUserDB_ViewModelProvider){
            this.moleLibrary = moleUserDB_ViewModelProvider;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            moleLibrary.insertMole(new Entity_Mole_Library(
                    "default",
                    integers[0],
                    "default",
                    "default",
                    0,
                    true,
                    "default"));
            moleLibrary.insertMole(new Entity_Mole_Library(
                    "default",
                    integers[0],
                    "default",
                    "default",
                    0,
                    true,
                    "default"));
            moleLibrary.insertMole(new Entity_Mole_Library(
                    "default",
                    integers[0],
                    "default",
                    "default",
                    0,
                    true,
                    "default"));
            moleLibrary.insertMole(new Entity_Mole_Library(
                    "default",
                    integers[0],
                    "default",
                    "default",
                    0,
                    true,
                    "default"));

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: inserted default User");
        }
    }
}
