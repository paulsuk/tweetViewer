package com.example.minkangpaulsuk.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.minkangpaulsuk.myapplication.R;
import com.fedorvlasov.lazylist.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom tweetAdapter to put both text and images in our listview
 */
public class tweetAdapter extends SimpleAdapter {
    private static ImageLoader mImageLoader;
    private int mResource;
    private LayoutInflater inflater;
    public static class ViewHolder {
        public ImageView thumbnail;
        public TextView tweetcontent;
        public TextView tweetusername;
    }

    public tweetAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mResource = resource;
        mImageLoader = new ImageLoader(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        Map<String, String> item = (Map<String, String>) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.content_tweet, null);
            holder      = new ViewHolder();
            holder.tweetusername  = (TextView) convertView.findViewById(R.id.tweetusername);
            holder.tweetcontent  = (TextView) convertView.findViewById(R.id.tweetcontent);
            holder.thumbnail  = (ImageView) convertView.findViewById(R.id.thumbnail);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tweetusername.setText(item.get("username"));
        holder.tweetcontent.setText(item.get("content"));
        mImageLoader.DisplayImage(item.get("thumbnail"), holder.thumbnail);
        return convertView;
    }

    public static ImageLoader getImageLoader(){
        return mImageLoader;
    }
}