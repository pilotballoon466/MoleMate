package com.master.molemate.ImageFileStorage.SupporterClasses;


import android.net.Uri;

public class RecyclerViewMoleImageItem {

    private Uri moleThumbnailUri;
    private String cardViewDate;
    private String cardviewText;
    private String cardviewPosText;
    private int moleId;
    private double moleDiagnosis;
    private boolean isHandeld;


    public RecyclerViewMoleImageItem(Uri moleThumbnail,
                                     String cardViewDate,
                                     String cardviewText,
                                     String cardviewPosText,
                                     int moleId,
                                     double moleDiagnosis,
                                     boolean isHandled
                                    )
    {
        this.moleThumbnailUri = moleThumbnail;
        this.cardViewDate = cardViewDate;
        this.cardviewText = cardviewText;
        this.cardviewPosText = cardviewPosText;
        this.moleId = moleId;
        this.moleDiagnosis = moleDiagnosis;
        this.isHandeld = isHandled;
    }

    public Uri  getMoleThumbnail() {
        return moleThumbnailUri;
    }

    public void setMoleThumbnail(Uri moleThumbnail) {
        this.moleThumbnailUri = moleThumbnail;
    }

    public String getCardViewDate() {
        return cardViewDate;
    }

    public void setCardViewTitle(String cardViewTitle) {
        this.cardViewDate = cardViewTitle;
    }

    public String getCardviewText() {
        return cardviewText;
    }

    public void setCardviewText(String cardviewText) {
        this.cardviewText = cardviewText;
    }

    public String getCardviewPosText() {
        return cardviewPosText;
    }

    public void setCardviewPosText(String cardviewPosText) {
        this.cardviewPosText = cardviewPosText;
    }

    public int getMoleId() {
        return moleId;
    }

    public void setMoleId(int moleId) {
        this.moleId = moleId;
    }

    public double getMoleDiagnosis() {
        return moleDiagnosis;
    }

    public void setMoleDiagnosis(double moleDiagnosis) {
        this.moleDiagnosis = moleDiagnosis;
    }

    public boolean isHandeld() {
        return isHandeld;
    }

    public void setHandeld(boolean handeld) {
        isHandeld = handeld;
    }

    public void setCardViewDate(String cardViewDate) {
        this.cardViewDate = cardViewDate;
    }
}
