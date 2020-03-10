package com.master.molemate.RoomDB.Entities;

import androidx.room.Embedded;
import androidx.room.Relation;

public class EntityMix_User_MoleLib {
    @Embedded public Entity_Mole_Library mole_library;
    @Relation(
            parentColumn = "uid_of_mole_user",
            entityColumn = "uid"
    )
    public Entity_Users user;
}
