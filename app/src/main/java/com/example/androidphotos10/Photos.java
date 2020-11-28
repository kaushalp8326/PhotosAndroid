package com.example.androidphotos10;

import androidx.appcompat.app.AppCompatActivity;
import com.example.androidphotos10.model.*;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;

public class Photos extends AppCompatActivity {

    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos);

        // Load user data. Create the save file if there is none
        User.setContext(getApplicationContext());
        user = User.load("sesh");
        if(user == null){
            try {
                user = new User("sesh");
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        ListView lstAlbums = findViewById(R.id.lstAlbums);
        ArrayAdapter<Album> adapter =
                new ArrayAdapter<>(this, R.layout.listview_entry, user.getAlbums());
        lstAlbums.setAdapter(adapter);

    }
}