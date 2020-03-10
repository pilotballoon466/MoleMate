package com.master.molemate.RoomDB.DAO_Interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.master.molemate.RoomDB.Entities.EntityMix_User_MoleLib;
import com.master.molemate.RoomDB.Entities.Entity_Users;

import java.util.List;

@Dao
public interface DAO_Interface_Users {

    @Query("SELECT * FROM users_table WHERE uid LIKE :uid")
    Entity_Users getUserFromId(int uid);

    @Query("SELECT * FROM users_table WHERE uid = 0")
    Entity_Users getDefaultUser();

    @Query("SELECT * FROM users_table")
    LiveData<List<Entity_Users>> getAllUser();

    @Insert
    void insert(Entity_Users user);

    @Update
    void update(Entity_Users user);

    @Delete
    void delete(Entity_Users user);

    @Query("DELETE FROM users_table")
    void deleteAll();

}
