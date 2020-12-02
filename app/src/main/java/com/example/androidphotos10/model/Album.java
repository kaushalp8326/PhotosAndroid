package com.example.androidphotos10.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Container class for Pictures.
 * @author John Hoban
 * @author Kaushal Patel
 *
 */
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Album name.
     */
    private String name;

    /**
     * Instance of the user that owns this album.
     */
    private User owner;

    /**
     * List of pictures that are contained by this album.
     */
    protected ArrayList<Picture> pictures = new ArrayList<Picture>();

    /**
     * Get this album's owner.
     * @return User instance that owns this album.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Get a read-only copy of this album's pictures. The pictures themselves are mutable,
     * but adds and deletes are not allowed.
     * @return An unmodifiable {@code List} instance containing each picture in this album.
     */
    public List<Picture> getPictures(){
        return pictures;
    }

    /**
     * Add a picture to this album.
     * @param picture The picture to add.
     * @return Whether the add was successful.
     */
    public boolean addPicture(Picture picture) {
        return pictures.add(picture);
    }

    /**
     * Remove a picture from this album.
     * @param picture The picture to remove.
     * @return Whether the remove was successful.
     */
    public boolean removePicture(Picture picture) {
        return pictures.remove(picture);
    }

    /**
     * A more direct way of accessing this album's size, rather than referencing {@code this.pictures}.
     * @return The number of pictures in this album.
     */
    public int getSize() {
        return pictures.size();
    }

    /**
     * Access this album's name.
     * @return Album name
     */
    public String getName() {
        return name;
    }

    /**
     * Renames this album as long as the new name does not conflict with another one
     * of this user's albums.
     * @param name New album name.
     * @return {@code true} if the name is set successfully, {@code false} otherwise.
     */
    public boolean setName(String name) {
        for(Album a: owner.albums) {
            if(a.name.equals(name)) {
                return false;
            }
        }
        this.name = name;
        return true;
    }

    /**
     * Base constructor for the album. Gives the album a name without adding any pictures.
     * Doesn't allow for two albums owned by the same user to have the same name.
     * @param name Name for the album.
     * @param owner The album's owner.
     * @throws IllegalArgumentException if an existing album owned by the same user has this name.
     */
    protected Album(String name, User owner) throws IllegalArgumentException {
        for(Album a: owner.albums) {
            if(a.name.equals(name)) {
                throw new IllegalArgumentException();
            }
        }
        this.name = name;
        this.owner = owner;
        this.owner.albums.add(this);
    }

    /**
     * Constructor for the album that gives it a name and also adds some pictures.
     * Most useful when creating an album from a list of search results.
     * @param name Name for the album.
     * @param owner The album's owner.
     * @param pictures List of pictures that will be added to this album.
     * @throws IllegalArgumentException if an existing album owned by the same user has this name.
     */
    public Album(String name, User owner, List<Picture> pictures) throws IllegalArgumentException {
        this(name, owner);
        this.pictures.addAll(pictures);
    }

    /**
     * Constructor for the album that gives it a name and also adds some pictures.
     * The album is not tied to a User instance.
     * Most useful when creating an album to represent a list of search results, that may or may not be saved as a new album.
     * @param name Name for the album.
     * @param pictures List of pictures that will be added to this album.
     */
    public Album(String name, HashSet<Picture> pictures) {
        this.name=name;
        this.pictures.addAll(pictures);
    }

    public String toString() {
        return name;
    }

}
