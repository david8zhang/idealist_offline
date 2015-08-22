package model;

import android.graphics.Bitmap;

/**
 * Created by david_000 on 8/18/2015.
 */
public class FeedItem {
    private String ideaTitle, ideaCategory, ideaText;
    private Bitmap image;

    //Constructor for a feeditem
    public FeedItem(){

    }

    public FeedItem(String ideaTitle, String ideaCategory, String ideaText, Bitmap image){
        super();
        this.ideaTitle = ideaTitle;
        this.ideaCategory = ideaCategory;
        this.ideaText = ideaText;
        this.image = image;
    }

    //Getter and setter methods
    public String getIdeaTitle() {
        return ideaTitle;
    }

    public void setIdeaTitle(String ideaTitle) {
        this.ideaTitle = ideaTitle;
    }

    public String getIdeaCategory() {
        return ideaCategory;
    }

    public void setIdeaCategory(String ideaCategory) {
        this.ideaCategory = ideaCategory;
    }

    public String getIdeaText() {
        return ideaText;
    }

    public void setIdeaText(String ideaText) {
        this.ideaText = ideaText;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

}
