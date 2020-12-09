package com.example.androidphotos10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.androidphotos10.model.Album;
import com.example.androidphotos10.model.Picture;
import com.example.androidphotos10.model.User;

public class SearchView extends AppCompatActivity {
    // Sesh
    public User user;
    public Album album;

    // Widgets
    private ListView lstAlbums;
    private PhotoAdapter adapter;
    private Button cmdSlideshow;

    private static int selectedIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        // Retrieve bundle
        Bundle bundle = getIntent().getExtras();
        user = (User)bundle.getSerializable(Photos.USER);
        album = (Album)bundle.getSerializable(Photos.ALBUM);
        setTitle(album.getName());

        // Set listeners
        lstAlbums = findViewById(R.id.lstAlbums);
        adapter = new PhotoAdapter(this, album.getPictures());
        lstAlbums.setAdapter(adapter);
        lstAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //AlbumView.setSelectedIndex(lstAlbums.getSelectedItemPosition());
                setSelectedIndex(i);
                photoClicked(i);
            }
        });

        cmdSlideshow = findViewById(R.id.cmdSlideshow);
        cmdSlideshow.setOnClickListener((view) -> startSlideshow(album));
    }

    public static int getSelectedIndex(){
        return selectedIndex;
    }

    public static void setSelectedIndex(int index){
        selectedIndex=index;
    }

    protected void photoClicked(int index){
        Picture picture = album.getPictures().get(index);
        ImageView image = new ImageView(this);
        image.setImageURI(picture.getUri());
        //image.setContentDescription(picture.printTags());
        new AlertDialog.Builder(SearchView.this)
                .setView(image)
                .setNegativeButton("Close", (dlg, i) -> dlg.cancel())
                .show();
    }

    protected void startSlideshow(Album a){
        if (album.getPictures().size()==0){
            new AlertDialog.Builder(SearchView.this)
                    .setTitle("Slideshow")
                    .setMessage("There are no photos in this album to display.")
                    .setNegativeButton("OK", (dlg, i) -> dlg.cancel())
                    .show();
        }else{
            Bundle bundle = new Bundle();
            bundle.putSerializable(Photos.USER, user);
            bundle.putSerializable(Photos.ALBUM, a);
            Intent intent = new Intent(this, Slideshow.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}