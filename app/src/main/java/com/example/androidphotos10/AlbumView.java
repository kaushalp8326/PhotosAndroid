package com.example.androidphotos10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.androidphotos10.model.Album;
import com.example.androidphotos10.model.User;

public class AlbumView extends AppCompatActivity {

    // Sesh
    public User user;
    public Album album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view);
        Bundle bundle = getIntent().getExtras();
        user = (User)bundle.getSerializable(Photos.USER);
        album = (Album)bundle.getSerializable(Photos.ALBUM);
        setTitle(album.getName());
    }
}