package com.master.molemate.RoomDB;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.master.molemate.RoomDB.DAO_Interfaces.DAO_Interface_Mole_Library;
import com.master.molemate.RoomDB.DAO_Interfaces.DAO_Interface_Users;
import com.master.molemate.RoomDB.Entities.Entity_Mole_Library;
import com.master.molemate.RoomDB.Entities.Entity_Users;

@Database(entities = {Entity_Mole_Library.class, Entity_Users.class}, version = 1)
public abstract class MoleMateDB extends RoomDatabase {

    private static final String TAG = "MoleUserDB";

    private static MoleMateDB mInstance;

    public abstract DAO_Interface_Mole_Library moleLibDAO();

    public abstract DAO_Interface_Users usersDAO();

    public static synchronized MoleMateDB getInstance(Context context){
        if(mInstance == null){
            mInstance = Room.databaseBuilder(context.getApplicationContext(),
                    MoleMateDB.class,
                    "HealthMateDB")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return mInstance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateUserDBAsyncTask(mInstance).execute();
        }
    };

    private static class PopulateUserDBAsyncTask extends AsyncTask<Void, Void, Void>{
        private DAO_Interface_Users userDAO;

        private PopulateUserDBAsyncTask(MoleMateDB moleUserDB){
            this.userDAO = moleUserDB.usersDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDAO.insert(new Entity_Users(
                    "default",
                    "default",
                    "default",
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
