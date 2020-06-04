package com.master.molemate.ImageFileStorage.SupporterClasses;


public class RecyclerViewMoleImageItem {

    private Uri moleThumbnailUri;
    private String cardViewDate;

    private String cardviewText;
    private String cardviewPosText;
    private int moleId;
    private double moleDiagnosis;


    public RecyclerViewMoleImageItem(int moleThumbnail, String cardViewTitle, String cardviewText) {
        this.moleThumbnail = moleThumbnail;
        this.cardViewTitle = cardViewTitle;
        this.cardviewText = cardviewText;
        this.cardviewPosText = cardviewPosText;
        this.moleId = moleId;
        this.moleDiagnosis = moleDiagnosis;
    }

    public int  getMoleThumbnail() {
        return moleThumbnail;
    }

    public void setMoleThumbnail(int moleThumbnail) {
        this.moleThumbnail = moleThumbnail;
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
}
