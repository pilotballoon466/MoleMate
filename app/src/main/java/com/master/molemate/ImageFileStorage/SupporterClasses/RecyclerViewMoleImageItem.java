package com.master.molemate.ImageFileStorage.SupporterClasses;


public class RecyclerViewMoleImageItem {

    private int  moleThumbnail;
    private String cardViewTitle;
    private String cardviewText;


    public RecyclerViewMoleImageItem(int moleThumbnail, String cardViewTitle, String cardviewText) {
        this.moleThumbnail = moleThumbnail;
        this.cardViewTitle = cardViewTitle;
        this.cardviewText = cardviewText;
    }

    public int  getMoleThumbnail() {
        return moleThumbnail;
    }

    public void setMoleThumbnail(int moleThumbnail) {
        this.moleThumbnail = moleThumbnail;
    }

    public String getCardViewTitle() {
        return cardViewTitle;
    }

    public void setCardViewTitle(String cardViewTitle) {
        this.cardViewTitle = cardViewTitle;
    }

    public String getCardviewText() {
        return cardviewText;
    }

    public void setCardviewText(String cardviewText) {
        this.cardviewText = cardviewText;
    }
}
