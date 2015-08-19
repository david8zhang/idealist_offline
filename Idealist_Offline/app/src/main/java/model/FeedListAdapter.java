package model;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.david_000.idealist_offline.R;

import java.util.List;

/**
 * Created by david_000 on 8/18/2015.
 */
public class FeedListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> ideaList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FeedListAdapter(Activity activity, List<FeedItem> ideaList) {
        this.activity = activity;
        this.ideaList = ideaList;
    }

    @Override
    public int getCount() {
        return ideaList.size();
    }

    @Override
    public Object getItem(int position) {
        return ideaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);
        if(imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        //Instantiate Textviews
        TextView ideaTitle = (TextView)convertView.findViewById(R.id.ideaTitle);
        TextView ideaCategory = (TextView)convertView.findViewById(R.id.ideaCategory);
        TextView ideaText = (TextView)convertView.findViewById(R.id.ideaText);

        FeedItem item = ideaList.get(position);

        //Set the text into the textViews
        ideaTitle.setText(item.getIdeaTitle());
        ideaCategory.setText(item.getIdeaCategory());
        ideaText.setText(item.getIdeaText());

        if(!TextUtils.isEmpty(item.getIdeaCategory())){
            ideaCategory.setText(item.getIdeaCategory());
            ideaCategory.setVisibility(View.VISIBLE);
        } else {
            ideaCategory.setVisibility(View.GONE);
        }

        return convertView;
    }
}
