package com.example.androidphotos10.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import android.content.Context;

/**
 * User class that functions mostly as a wrapper class for several albums.
 * This class's data can be serialized, and should be loaded from the file system
 * when a user logs in.
 * @author John Hoban
 * @author Kaushal Patel
 *
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Data file path
     */
    public String path;

    /**
     * User's name.
     */
    private final String name;

    /**
     * User context.
     */
    private static Context context;

    /**
     * List of the user's albums. Hidden outside this package.
     */
    protected ArrayList<Album> albums;

    /**
     * Unique tag names this user has used.
     */
    public HashSet<String> uniqueTags=new HashSet<String>();

    /**
     * Get this user's username.
     * @return The username.
     */
    public String getName() {
        return name;
    }

    /**
     * Get a read-only copy of this user's albums. The albums themselves are mutable,
     * but adds and deletes are not allowed.
     * @return An unmodifiable {@code List} instance containing each album this user owns.
     */
    public List<Album> getAlbums(){
        return albums;
    }

    /**
     * Assign an Android application context used to load and save user data.
     * @param ctx Application context
     */
    public static void setContext(Context ctx){
        context = ctx;
    }

    /**
     * Creates a new album for this user. A user cannot own two albums with the same name.
     * @param name Album name.
     * @throws IllegalArgumentException if an existing album has this name.
     * @return Instance of the new Album.
     */
    public Album createAlbum(String name) throws IllegalArgumentException {
        return new Album(name, this);
    }

    /**
     * Deletes an album from the user's collection.
     * @param album The album to be deleted.
     * @return {@code true} if delete was successful, {@code false} otherwise.
     */
    public boolean deleteAlbum(Album album) {
        return albums.remove(album);
    }

    /**
     * Create a new user by name.
     * @param name Username
     */
    public User(String name) {
        this.name = name;
        this.path = context.getFilesDir() + "/" + name + ".dat";
        System.out.println(path);

        this.albums = new ArrayList<Album>();
        File f = new File(path);
        try {
            f.createNewFile();
            save();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save this user's data to the filesystem via serialization.
     */
    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(this);
            oos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Load a user object from a data file, given the name of the desired user.
     * @param name Username.
     * @return A user object.
     */
    public static User load(String name) {
        String path = context.getFilesDir() + "/" + name + ".dat";
        User u;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            u = (User) ois.readObject();
            ois.close();
        }catch(ClassNotFoundException | IOException e){
            return null;
        }
        return u;
    }

}
