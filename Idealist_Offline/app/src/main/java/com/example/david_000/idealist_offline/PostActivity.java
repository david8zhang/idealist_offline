package com.example.david_000.idealist_offline;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import utils.Global;
import utils.Utils;

/**
 * Created by david_000 on 8/19/2015.
 */
public class PostActivity extends AppCompatActivity{

    public String ideaTitle;
    public String ideaCategory;
    public String ideaText;
    public boolean sketch;

    public Utils utils;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);

        //Instantiate Utilities
        utils = new Utils();



        //Instantiate the Drawing Pad button
        ImageButton sketch_pad = (ImageButton)findViewById(R.id.sketch_button);
        sketch_pad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, DrawingPadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PostActivity.this.startActivity(intent);
            }
        });

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                sketch = false;
            } else {
                sketch = extras.getBoolean("Drawn");
            }
        } else {
            sketch = (Boolean)savedInstanceState.getSerializable("Drawn");
        }

        //Checks if the user saved their drawing
        if(sketch){
            sketch_pad.setImageBitmap(Global.img);
        }

        //Instantiate the actual post button
        Button post_button = (Button)findViewById(R.id.postIdea);
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Grab the needed textviews
                TextView ideaTitleView = (TextView)findViewById(R.id.post_idea_title);
                TextView ideaCategoryView = (TextView)findViewById(R.id.post_idea_category);
                TextView ideaTextView = (TextView)findViewById(R.id.post_idea_text);

                //Get the needed Strings
                if(ideaTitleView.getText() != null){
                    if(ideaCategoryView.getText() != null){
                        if(ideaTextView.getText() != null){
                            ideaTitle = ideaTitleView.getText().toString();
                            ideaCategory = ideaCategoryView.getText().toString();
                            ideaText = ideaTextView.getText().toString();

                            if(Global.img != null){
                                System.out.println("There is an image");
                                utils.postIdeaImage(PostActivity.this, ideaTitle, ideaCategory, ideaText, Global.img);
                                Global.img = null;
                            } else {
                                System.out.println("No Image");
                                utils.postIdea(PostActivity.this, ideaTitle, ideaCategory, ideaText);
                            }
                            ideaTitleView.setText("");
                            ideaCategoryView.setText("");
                            ideaTextView.setText("");

                            //Redirect back to the MainActivity
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(PostActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PostActivity.this.startActivity(intent);
                                }
                            }, 1000);
                        } else {
                            new SweetAlertDialog(PostActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("You must enter a description of your idea!")
                                    .show();
                        }
                    } else {
                        new SweetAlertDialog(PostActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("You must enter a category for your idea")
                                .show();
                    }
                } else {
                    new SweetAlertDialog(PostActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setContentText("You must enter a title for your idea!")
                            .show();
                }
            }
        });

    }
}
