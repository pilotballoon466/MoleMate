package com.master.molemate.RoomDB.Entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity
public class Entity_Mole_Library {

    /* Will be the Date given by the file name (ConstantString_Date_RandomNumer.jpg) */
    @PrimaryKey(autoGenerate = true)
    private int moleID;

    @ColumnInfo(name = "date_mole_image_creation")
    private String dateMoleImageCreation;

    @ColumnInfo(name = "uid_of_mole_user")
    private int userToMole;

    @ColumnInfo(name = "mole_image_uri")
    private String moleImageUri;

    @ColumnInfo(name = "mole_pos_bitmap_uri")
    private String molePosBitmapUri;

    @ColumnInfo(name = "mole_pos_color_code")
    private int molePosColorCode;

    @ColumnInfo(name = "body_frontside")
    private boolean isBodyFrontside;

    private String diagnosis;

    public Entity_Mole_Library(String dateMoleImageCreation,
                               int userToMole, String moleImageUri,
                               String molePosBitmapUri,
                               int molePosColorCode,
                               boolean isBodyFrontside,
                               String diagnosis)
    {
        this.dateMoleImageCreation = dateMoleImageCreation;
        this.userToMole = userToMole;
        this.moleImageUri = moleImageUri;
        this.molePosBitmapUri = molePosBitmapUri;
        this.molePosColorCode = molePosColorCode;
        this.isBodyFrontside = isBodyFrontside;
        this.diagnosis = diagnosis;
    }

    public void setMoleID(int moleID) {
        this.moleID = moleID;
    }

    public int getMoleID() {
        return moleID;
    }

    public void setDateMoleImageCreation(String dateMoleImageCreation) {
        this.dateMoleImageCreation = dateMoleImageCreation;
    }

    public void setUserToMole(int userToMole) {
        this.userToMole = userToMole;
    }

    public void setMoleImageUri(String moleImageUri) {
        this.moleImageUri = moleImageUri;
    }

    public void setMolePosBitmapUri(String molePosBitmapUri) {
        this.molePosBitmapUri = molePosBitmapUri;
    }

    public void setMolePosColorCode(int molePosColorCode) {
        this.molePosColorCode = molePosColorCode;
    }

    public void setBodyFrontside(boolean bodyFrontside) {
        isBodyFrontside = bodyFrontside;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String  getDateMoleImageCreation() {
        return dateMoleImageCreation;
    }

    public int getUserToMole() {
        return userToMole;
    }

    public String getMoleImageUri() {
        return moleImageUri;
    }

    public String getMolePosBitmapUri() {
        return molePosBitmapUri;
    }

    public int getMolePosColorCode() {
        return molePosColorCode;
    }

    public boolean isBodyFrontside() {
        return isBodyFrontside;
    }

    public String getDiagnosis() {
        return diagnosis;
    }
}
