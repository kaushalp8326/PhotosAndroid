package com.example.androidphotos10;

import androidx.appcompat.app.AppCompatActivity;
import com.example.androidphotos10.model.*;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

public class Photos extends AppCompatActivity {

    // Sesh
    public User user;

    // Widgets
    ListView lstAlbums;
    ArrayAdapter<Album> adapter;
    Button cmdCreate, cmdFind;

    // Input dialog response
    private String albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos);

        // Load user data. Create the save file if there is none
        User.setContext(Photos.this);
        user = User.load("sesh");
        if(user == null){
            try {
                user = new User("sesh");
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        // Set listeners
        lstAlbums = findViewById(R.id.lstAlbums);
        adapter = new ArrayAdapter<>(this, R.layout.listview_entry, user.getAlbums());
        lstAlbums.setAdapter(adapter);
        lstAlbums.setOnItemClickListener(((parent, view, position, id) -> albumClicked(position)));

        cmdCreate = findViewById(R.id.cmdCreate);
        cmdCreate.setOnClickListener((view) -> getAlbumName());
        cmdFind = findViewById(R.id.cmdFind);
        cmdFind.setOnClickListener((view) -> findPhotos());

    }

    /**
     * Give the user the option to open or delete an album after clicking on it.
     */
    protected void albumClicked(int index) {
        // This dialog design can definitely be improved
        Album target = user.getAlbums().get(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(Photos.this);
        builder.setTitle(target.getName())
                .setItems(R.array.album_options_array, (dlg, i) -> {
                    if(i == 0){ // Open
                        openAlbum(target);
                    }else{ // Delete
                        deleteAlbum(target);
                    }
                });
        builder.setNegativeButton("Cancel", (dlg, i) -> dlg.cancel());
        builder.show();
    }

    /**
     * Transitions to the album viewer activity.
     * @param a Album
     */
    protected void openAlbum(Album a) {
        // TODO
    }

    /**
     * Deletes an album.
     * @param a Album
     */
    protected void deleteAlbum(Album a) {
        user.deleteAlbum(a);
        adapter.notifyDataSetChanged();
    }

    /**
     * Helper method to get an album name before creating it. This is needed
     * because Android has no blocking I/O.
     */
    protected void getAlbumName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Photos.this);
        builder.setTitle("Create New Album")
                .setMessage("Enter a new album name:");

        final EditText input = new EditText(Photos.this);
        builder.setView(input);

        builder.setPositiveButton("OK", (dlg, i) -> {
            albumName = input.getText().toString();
            createAlbum();
        });
        builder.setNegativeButton("Cancel", (dlg, i) -> dlg.cancel());
        albumName = null;
        builder.show();
    }

    /**
     * Create an album. No duplicate album names are allowed.
     */
    protected void createAlbum() {
        if(albumName == null){
            return;
        }
        try {
            user.createAlbum(albumName);
            adapter.notifyDataSetChanged();
        }catch(IllegalArgumentException e){
            Toast.makeText(this, "An album with this name already exists", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Find photos by tags.
     */
    protected void findPhotos() {
        // TODO
    }

}