package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by david_000 on 8/19/2015.
 */
public class Utils extends AppCompatActivity {

    public DatabaseHandler db;

    public Utils(){

    }

    public void postIdea(Context context, String ideaTitle, String ideaCategory, String ideaText){
        Map listItem = packageData(ideaTitle, ideaCategory, ideaText);
        db = new DatabaseHandler(context);
        db.addListItem(listItem);
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success!")
                .setContentText("Idea Successfully Posted!")
                .show();
    }

    public void postIdeaImage(Context context, String ideaTitle, String ideaCategory, String ideaText, Bitmap ideaImage){
        Map listItem = packageData(ideaTitle, ideaCategory, ideaText);
        db = new DatabaseHandler(context);
        db.addListItemBitmap(listItem, ideaImage);
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success!")
                .setContentText("Idea Successfully Posted!")
                .show();
    }

    public Map<String, String> packageData(String ideaTitle, String ideaCategory, String ideaText) {
        Map<String, String> ideaMap = new HashMap<>();
        ideaMap.put("ideaTitle", ideaTitle);
        ideaMap.put("ideaCategory", ideaCategory);
        ideaMap.put("ideaText", ideaText);
        return ideaMap;
    }
}
