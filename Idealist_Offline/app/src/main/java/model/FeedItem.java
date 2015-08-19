package model;

/**
 * Created by david_000 on 8/18/2015.
 */
public class FeedItem {
    private String ideaTitle, ideaCategory, ideaText, imageURL, postingDate;

    //Constructor for a feeditem
    public FeedItem(){

    }

    public FeedItem(String ideaTitle, String ideaCategory, String ideaText){
        super();
        this.ideaTitle = ideaTitle;
        this.ideaCategory = ideaCategory;
        this.ideaText = ideaText;
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

}
