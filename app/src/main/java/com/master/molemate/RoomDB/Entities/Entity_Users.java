package com.master.molemate.RoomDB.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users_table")
public class Entity_Users {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "mail")
    private String mail;

    public Entity_Users(String userName, String firstName, String lastName, String mail) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMail() {
        return mail;
    }
}
