package com.example.androidphotos10;

import androidx.appcompat.app.AppCompatActivity;
import com.example.androidphotos10.model.*;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Photos extends AppCompatActivity {

    // Sesh
    public User user;

    // Widgets
    ListView lstAlbums;
    ArrayAdapter<Album> adapter;
    Button cmdCreate, cmdFind;

    // Input dialog response
    private String albumName;

    // Album option flags
    private final int OPEN = 0;
    private final int RENAME = 1;
    private final int DELETE = 2;

    // Bundle constants
    public static final String USER = "user";
    public static final String ALBUM = "album";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos);

        // Load user data. Create the save file if there is none
        User.setContext(Photos.this);
        user = User.load("sesh");
        if(user == null){
            user = new User("sesh");
        }

        // Set listeners
        lstAlbums = findViewById(R.id.lstAlbums);
        adapter = new ArrayAdapter<>(this, R.layout.text_entry, user.getAlbums());
        lstAlbums.setAdapter(adapter);
        lstAlbums.setOnItemClickListener(((parent, view, position, id) -> albumClicked(position)));

        cmdCreate = findViewById(R.id.cmdCreate);
        cmdCreate.setOnClickListener((view) -> getCreatedAlbumName());
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
                    switch(i){
                        case OPEN:
                            openAlbum(target);
                            break;

                        case RENAME:
                            getReplacementAlbumName(target);
                            break;

                        case DELETE:
                            deleteAlbum(target);
                            break;
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
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        bundle.putSerializable(ALBUM, a);
        Intent intent = new Intent(this, AlbumView.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Helper method to get a replacement album name before renaming it. This is needed
     * because Android has no blocking I/O.
     */
    protected void getReplacementAlbumName(Album a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Photos.this);
        builder.setTitle("Rename Album")
                .setMessage("Enter a new album name:");

        final EditText input = new EditText(Photos.this);
        builder.setView(input);

        builder.setPositiveButton("OK", (dlg, i) -> {
            albumName = input.getText().toString();
            renameAlbum(a);
        });
        builder.setNegativeButton("Cancel", (dlg, i) -> dlg.cancel());
        albumName = null;
        builder.show();
    }

    /**
     * Renames an album, if the name isn't a duplicate.
     * @param a Album
     */
    protected void renameAlbum(Album a) {
        if(a.setName(albumName)){
            user.save();
           adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this, "An album with this name already exists", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Deletes an album.
     * @param a Album
     */
    protected void deleteAlbum(Album a) {
        user.deleteAlbum(a);
        user.save();
        adapter.notifyDataSetChanged();
    }

    /**
     * Helper method to get an album name before creating it. This is needed
     * because Android has no blocking I/O.
     */
    protected void getCreatedAlbumName() {
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
            user.save();
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