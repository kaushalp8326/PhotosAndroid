package com.example.androidphotos10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.androidphotos10.model.*;

import java.util.ArrayList;

public class AlbumView extends AppCompatActivity {
    // Sesh
    public User user;
    public Album album;

    // Widgets
    private ListView lstAlbums;
    private PhotoAdapter adapter;
    private Button cmdAddPhoto;
    private Button cmdRemovePhoto;
    private Button cmdSlideshow;

    private final String PERSON = "Person";
    private final String LOCATION = "Location";

    private static int selectedIndex=0;

    private final int ADD_PHOTO = 0;
    private final int ADD_TAG = 0;
    private final int REMOVE_TAG = 1;
    private final int MOVE_PHOTO = 2;
    private final int PERSON_TAG = 0;
    private final int LOCATION_TAG = 1;

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
        lstAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //AlbumView.setSelectedIndex(lstAlbums.getSelectedItemPosition());
                setSelectedIndex(i);
                photoClicked(i);
            }
        });

        cmdAddPhoto = findViewById(R.id.cmdAddPhoto);
        cmdAddPhoto.setOnClickListener((view) -> addPhoto());

        cmdRemovePhoto = findViewById(R.id.cmdRemovePhoto);
        cmdRemovePhoto.setOnClickListener((view) -> removePhoto());

        cmdSlideshow = findViewById(R.id.cmdSlideshow);
        // TODO
        // cmdSlideshow.setOnClickListener();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Photos.ALBUM, album);
        intent.putExtras(bundle);
        setResult(Photos.ALBUM_DATA, intent);
        finish();
    }

    public static int getSelectedIndex(){
        return selectedIndex;
    }

    public static void setSelectedIndex(int index){
        selectedIndex=index;
    }

    protected void photoClicked(int index){
        Picture picture = album.getPictures().get(index);
        new AlertDialog.Builder(AlbumView.this)
                .setItems(R.array.photo_options_array, (dlg, i) -> {
                    switch (i){
                        case ADD_TAG:
                            addTag(picture);
                            break;

                        case REMOVE_TAG:
                            removeTag(picture);
                            break;

                        case MOVE_PHOTO:
                            //TODO
                            break;
                    }
                })
                .setNegativeButton("Cancel", (dlg, i) -> dlg.cancel())
                .show();
    }

    protected void addTag(Picture picture){
        new AlertDialog.Builder(AlbumView.this)
                .setItems(R.array.tag_types_array, (dlg, i) -> {
                    switch (i){
                        case PERSON_TAG:
                            addPersonTag(picture);
                            break;

                        case LOCATION_TAG:
                            addLocationTag(picture);
                            break;
                    }
                })
                .setNegativeButton("Cancel", (dlg, i) -> dlg.cancel())
                .show();
    }

    protected void addPersonTag(Picture picture){
        final EditText tagInput = new EditText(AlbumView.this);
        new AlertDialog.Builder(AlbumView.this)
                .setTitle("Add Person Tag")
                .setMessage("Enter a new name:")
                .setView(tagInput)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temp=tagInput.getText().toString();
                        if(!temp.equalsIgnoreCase("")){
                            picture.addTag(PERSON, temp);
                        }
                    }
                })
                .setNegativeButton("Cancel", (dlg, i) -> dlg.cancel())
                .show();
    }

    protected void addLocationTag(Picture picture){
        final EditText tagInput = new EditText(AlbumView.this);
        new AlertDialog.Builder(AlbumView.this)
                .setTitle("Add Location Tag")
                .setMessage("Enter a new location:")
                .setView(tagInput)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temp=tagInput.getText().toString();
                        if(!temp.equalsIgnoreCase("")){
                            picture.addTag(LOCATION, temp);
                        }
                    }
                })
                .setNegativeButton("Cancel", (dlg, i) -> dlg.cancel())
                .show();
    }

    protected void removeTag(Picture picture){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Remove Tag")
                .setNegativeButton("Cancel", (dlg, i) -> dlg.cancel());
        builder.setItems(picture.getTagArray(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String tag=picture.getTagArray()[i];
                String tagType=tag.substring(0,tag.indexOf(":"));
                String tagValue=tag.substring(tag.indexOf("\n")+1);
                picture.removeTag(tagType, tagValue);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void addPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Photo"), ADD_PHOTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case ADD_PHOTO:
                    Picture toAdd = new Picture(imageReturnedIntent.getData());
                    album.addPicture(toAdd);
                    user.save();
                    adapter.notifyDataSetChanged();
            }
        }
    }

    protected void removePhoto(){
        // lstAlbums.getItemAtPosition(AlbumView.getSelectedIndex()) != null
        // lstAlbums.getSelectedItem() != null
        if(album.getPictures().size() != 0) {
            album.removePicture((Picture) lstAlbums.getItemAtPosition(getSelectedIndex()));
            setSelectedIndex(0);
            user.save();
            adapter.notifyDataSetChanged();
        }else{
            new AlertDialog.Builder(AlbumView.this)
                    .setTitle("Remove Photo")
                    .setMessage("There are no photos in this album to remove")
                    .setNegativeButton("OK", (dlg, i) -> dlg.cancel())
                    .show();
        }
    }
}