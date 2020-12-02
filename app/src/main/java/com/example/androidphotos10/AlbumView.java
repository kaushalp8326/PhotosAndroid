package com.example.androidphotos10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.androidphotos10.model.*;

public class AlbumView extends AppCompatActivity {

    // Sesh
    public User user;
    public Album album;

    // Widgets
    private ListView lstAlbums;
    private PhotoAdapter adapter;
    private Button cmdAddPhoto;
    private Button cmdSlideshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view);

        // Retrieve bundle
        Bundle bundle = getIntent().getExtras();
        user = (User)bundle.getSerializable(Photos.USER);
        album = (Album)bundle.getSerializable(Photos.ALBUM);
        setTitle(album.getName());

        // Set listeners
        lstAlbums = findViewById(R.id.lstAlbums);
        adapter = new PhotoAdapter(this, album.getPictures());
        lstAlbums.setAdapter(adapter);
        // TODO
        // lstAlbums.setOnItemClickListener();

        cmdAddPhoto = findViewById(R.id.cmdAddPhoto);
        // TODO
        // cmdAddPhoto.setOnClickListener();
        cmdSlideshow = findViewById(R.id.cmdSlideshow);
        // TODO
        // cmdSlideshow.setOnClickListener();
    }
}