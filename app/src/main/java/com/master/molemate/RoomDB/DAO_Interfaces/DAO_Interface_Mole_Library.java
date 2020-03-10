package com.master.molemate.RoomDB.DAO_Interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.master.molemate.RoomDB.Entities.EntityMix_User_MoleLib;
import com.master.molemate.RoomDB.Entities.Entity_Mole_Library;

import java.util.List;

@Dao
public interface DAO_Interface_Mole_Library {

    @Transaction
    @Query("SELECT * FROM mole_library_table WHERE uid_of_mole_user = :uid")
    LiveData<List<EntityMix_User_MoleLib>> getAllMolesFromUser(int uid);

    @Query("SELECT * FROM mole_library_table")
    List<Entity_Mole_Library> getAllMoles();

    @Query("SELECT * FROM mole_library_table WHERE moleID IN (:id)")
    List<Entity_Mole_Library> loadAllMolesFromMoleIds(int[] id);

    @Query("SELECT mole_pos_color_code FROM mole_library_table WHERE mole_image_uri LIKE :moleImageUri")
    int getColorCodeFromUri(String moleImageUri);

    @Insert
    void insert(Entity_Mole_Library moleEntry);

    @Delete
    void delete(Entity_Mole_Library moleEntry);

    @Query("DELETE FROM mole_library_table WHERE uid_of_mole_user = :uid" )
    void deleteAllMoles(int uid);

    @Update
    void update(Entity_Mole_Library moleEntry);
}
