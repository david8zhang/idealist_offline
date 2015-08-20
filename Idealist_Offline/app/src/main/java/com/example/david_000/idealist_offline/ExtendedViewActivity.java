package com.example.david_000.idealist_offline;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import utils.DatabaseHandler;

/**
 * Created by david_000 on 8/19/2015.
 */
public class ExtendedViewActivity extends AppCompatActivity {

    //Strings to insert into textview
    private String ideaTitle;
    private String ideaCategory;
    private String ideaText;
    private String editedTitle;
    private String editedCategory;
    private String editedText;


    //database handler
    DatabaseHandler db;

    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idea_detail);

        db = new DatabaseHandler(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                ideaTitle = null;
                ideaCategory = null;
                ideaText = null;
            } else {
                ideaTitle = extras.getString("ideaTitle");
                ideaCategory = extras.getString("ideaCategory");
                ideaText = extras.getString("ideaText");
            }
        } else {
            ideaTitle = (String)savedInstanceState.getSerializable("ideaTitle");
            ideaCategory = (String)savedInstanceState.getSerializable("ideaCategory");
            ideaText = (String)savedInstanceState.getSerializable("ideaText");
        }

        TextView titleView = (TextView)findViewById(R.id.idea_title);
        TextView catView = (TextView)findViewById(R.id.idea_category);
        TextView textView = (TextView)findViewById(R.id.idea_text);

        titleView.setText(ideaTitle);
        catView.setText(ideaCategory);
        textView.setText(ideaText);

        //Configuring an edit text alert dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Idea");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText newTitle = new EditText(this);
        final EditText newCategory = new EditText(this);
        final EditText newDescription = new EditText(this);

        newTitle.setHint(ideaTitle);
        newCategory.setHint(ideaCategory);
        newDescription.setHint(ideaText);

        layout.addView(newTitle);
        layout.addView(newCategory);
        layout.addView(newDescription);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Get the edited text out of the text boxes
                editedTitle = newTitle.getText().toString();
                editedCategory = newCategory.getText().toString();
                editedText = newDescription.getText().toString();

                //Check if any of them are null. If they are, set them to the original values
                if (editedTitle.equals(""))
                    editedTitle = newTitle.getHint().toString();
                if (editedCategory.equals(""))
                    editedCategory = newCategory.getHint().toString();
                if (editedText.equals(""))
                    editedText = newDescription.getHint().toString();
                if (db.editIdea(ideaText, editedTitle, editedCategory, editedText)) {
                    new SweetAlertDialog(ExtendedViewActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success!")
                            .setContentText("Idea Successfully Posted!")
                            .show();
                }
                //Redirect back to the MainActivity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ExtendedViewActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ExtendedViewActivity.this.startActivity(intent);
                    }
                }, 2000);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(ExtendedViewActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Edit this idea?")
                        .setContentText("You will not be able to revert these changes")
                        .setCancelText("No, please don't!")
                        .setConfirmText("Yes, edit it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                builder.show();
                            }
                        })
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).show();
            }
        });
    }
}
