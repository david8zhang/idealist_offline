package com.example.david_000.idealist_offline;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;
import java.util.List;

import Constants.Constants;
import cn.pedant.SweetAlert.SweetAlertDialog;
import model.FeedItem;
import model.FeedListAdapter;
import utils.DatabaseHandler;

public class MainActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    //ListView, adapter, toolbars
    private ObservableListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> ideaList;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeLayout;
    private String autoRefresh;

    //Database handler
    private DatabaseHandler db;

    //Deletion parameters
    private String textToDelete;
    private int positionToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiate new databasehandler
        db = new DatabaseHandler(this);

        //Hide the toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Instantiate the ideaList to store individual ideaList views
        ideaList = new ArrayList<FeedItem>();

        //Instantiate ArrayList, List View and Adapter
        listView = (ObservableListView)findViewById(R.id.list);
        listView.setScrollViewCallbacks(this);
        listAdapter = new FeedListAdapter(this, ideaList);
        listView.setAdapter(listAdapter);

        //Get the ideas from the SQLite Database
        getIdeas();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView ideaTextView = (TextView)view.findViewById(R.id.ideaText);
                textToDelete = ideaTextView.getText().toString();
                positionToDelete = position;
                deleteIdea();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewDetail = new Intent(MainActivity.this, ExtendedViewActivity.class);

                //Get the text from the associated textView
                TextView ideaTitleText = (TextView)view.findViewById(R.id.ideaTitle);
                TextView ideaCategoryText = (TextView)view.findViewById(R.id.ideaCategory);
                TextView ideaTextExtra = (TextView)view.findViewById(R.id.ideaText);

                //Send this data to the new intent
                String title = ideaTitleText.getText().toString();
                String cat = ideaCategoryText.getText().toString();
                String text = ideaTextExtra.getText().toString();

                //Send this stuff through
                viewDetail.putExtra("ideaTitle", title);
                viewDetail.putExtra("ideaCategory", cat);
                viewDetail.putExtra("ideaText", text);

                MainActivity.this.startActivity(viewDetail);
            }
        });

        //Instantiate the postActivity button
        ImageButton post_button = (ImageButton)findViewById(R.id.newIdea);
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    //Get stuff from the Database
    public void getIdeas(){
        Cursor cursor = db.getListItem();
        if(cursor != null){
            while(cursor.moveToNext()){
                FeedItem item = new FeedItem();
                if(cursor.getColumnIndex(Constants.IDEA_TITLE) > -1){
                    String ideaTitle = cursor.getString(cursor.getColumnIndex(Constants.IDEA_TITLE));
                    System.out.println(ideaTitle);
                    item.setIdeaTitle(ideaTitle);
                }
                if(cursor.getColumnIndex(Constants.IDEA_CATEGORY) > -1){
                    String ideaCategory = cursor.getString(cursor.getColumnIndex(Constants.IDEA_CATEGORY));
                    System.out.println(ideaCategory);
                    item.setIdeaCategory(ideaCategory);
                }
                if(cursor.getColumnIndex(Constants.IDEA_TEXT) > -1){
                    String ideaText = cursor.getString(cursor.getColumnIndex(Constants.IDEA_TEXT));
                    System.out.println(ideaText);
                    item.setIdeaText(ideaText);
                }
                ideaList.add(item);
            }
            cursor.moveToNext();
            listAdapter.notifyDataSetChanged();
        }
    }

    //Deleting Ideas
    public void deleteIdea(){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are You Sure?")
                .setContentText("You may not be able to recover this idea!")
                .setCancelText("No Cancel it!")
                .setConfirmText("Yes, Delete it!").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if(db.deleteItem(textToDelete)){
                        //Delete the thing from the list view
                        ideaList.remove(positionToDelete);
                        listAdapter.notifyDataSetChanged();

                        sweetAlertDialog.setTitleText("Deleted!")
                                .setContentText("Idea has been deleted!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    } else {
                        sweetAlertDialog.setTitleText("Error!")
                                .setContentText("There was an error deleting the idea...")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                }
        }).showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                }).show();
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
