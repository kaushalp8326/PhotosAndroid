package com.example.androidphotos10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.androidphotos10.model.Album;
import com.example.androidphotos10.model.User;

public class Slideshow extends AppCompatActivity {
    public User user;
    public Album album;

    private Button cmdLeft;
    private Button cmdRight;

    private static int selectedIndex;
    private static ImageView displayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slideshow_view);

        Bundle bundle = getIntent().getExtras();
        user = (User)bundle.getSerializable(Photos.USER);
        album = (Album)bundle.getSerializable(Photos.ALBUM);
        setTitle(album.getName());
        selectedIndex=0;

        displayed = findViewById(R.id.displayPicture);
        displayed.setImageURI(album.getPictures().get(0).getUri());

        cmdLeft = findViewById(R.id.cmdLeft);
        cmdLeft.setOnClickListener((view) -> left());

        cmdRight = findViewById(R.id.cmdRight);
        cmdRight.setOnClickListener((view) -> right());
    }

    public static int getSelectedIndex(){
        return selectedIndex;
    }

    public static void setSelectedIndex(int index){
        selectedIndex=index;
    }

    protected void left(){
        if (selectedIndex>0){
            selectedIndex--;
            displayed.setImageURI(album.getPictures().get(selectedIndex).getUri());
        }
    }

    protected void right(){
        if (selectedIndex<album.getSize()-1){
            selectedIndex++;
            displayed.setImageURI(album.getPictures().get(selectedIndex).getUri());
        }
    }
}