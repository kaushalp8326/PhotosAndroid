package com.example.androidphotos10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.androidphotos10.model.*;

import java.util.stream.Collectors;

public class ChooseAlbum extends AppCompatActivity {

    // Sesh
    public User user;
    public Album album;

    // Widgets
    private ListView lstAlbums;
    private ArrayAdapter<Album> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_album);
        setTitle("Choose a Destination Album");

        // Retrieve bundle
        Bundle bundle = getIntent().getExtras();
        user = (User)bundle.getSerializable(Photos.USER);
        album = (Album)bundle.getSerializable(Photos.ALBUM);

        // Set listeners
        lstAlbums = findViewById(R.id.lstAlbums);
        adapter = new ArrayAdapter<Album>(this, R.layout.text_entry,
                user.getAlbums().stream()
                                .filter(a -> !a.getName().equals(album.getName()))
                                .collect(Collectors.toList()));
        lstAlbums.setAdapter(adapter);
        lstAlbums.setOnItemClickListener(((parent, view, position, id) -> albumClicked(position)));

    }

    /**
     * Return the name of the album that the photo is moving to.
     * @param index Index in album list
     */
    protected void albumClicked(int index) {
        Album target = (Album)lstAlbums.getItemAtPosition(index);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Photos.USER, user);
        bundle.putSerializable(Photos.ALBUM, album);
        bundle.putString(Photos.ALBUM_NAME, target.getName());
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}