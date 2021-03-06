package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;

import Constants.Constants;

/**
 * Created by david_000 on 8/18/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context){
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LIST_TABLE = "CREATE TABLE " + Constants.TABLE_LIST + "(" + Constants.IDEA_TITLE +
                " TEXT," + Constants.IDEA_CATEGORY + " TEXT," + Constants.IDEA_TEXT + " TEXT" + ")";
        db.execSQL(CREATE_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop Older Table
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_LIST);

        //Create tables again
        onCreate(db);
    }

    //Adds new thing to the database
    public void addListItem(Map<String, String> listItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.IDEA_TITLE, listItem.get("ideaTitle"));
        values.put(Constants.IDEA_CATEGORY, listItem.get("ideaCategory"));
        values.put(Constants.IDEA_TEXT, listItem.get("ideaText"));
        db.insert(Constants.TABLE_LIST, null, values);
        db.close();
    }

    //Gets all the list items
    public Cursor getListItem(){
        String selectQuery = "SELECT * FROM " + Constants.TABLE_LIST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }


    //delete something form the database
    public boolean deleteItem(String ideaText){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs = new String[] {ideaText};
        return db.delete(Constants.TABLE_LIST, Constants.IDEA_TEXT + "=?", whereArgs)>0;
    }

    public boolean editIdea(String ideaText, String editedTitle, String editedCategory, String editedText){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String[] whereArgs = new String[]{ideaText};
        cv.put("ideaTitle", editedTitle);
        cv.put("ideaCategory", editedCategory);
        cv.put("ideaText", editedText);
        return db.update(Constants.TABLE_LIST, cv, Constants.IDEA_TEXT + "=?", whereArgs) > 0;
    }
}
