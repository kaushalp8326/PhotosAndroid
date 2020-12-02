package com.example.androidphotos10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidphotos10.model.Picture;

import java.util.List;

/**
 * Overrides the default ArrayAdapter class to show picture data in a ListView.
 */
public class PhotoAdapter extends ArrayAdapter<Picture> {

    private Context context;
    private List<Picture> data;

    public PhotoAdapter(Context context, List<Picture> data){
        super(context, R.layout.photo_entry, data); // TODO - change
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listEntry = inflater.inflate(R.layout.photo_entry, null, true);
        ImageView imgUri = listEntry.findViewById(R.id.imgPhoto);
        TextView lblUri = listEntry.findViewById(R.id.lblUri);

        Picture pic = data.get(position);
        imgUri.setImageURI(pic.getUri());
        lblUri.setText(pic.toString());

        return listEntry;
    }

}
