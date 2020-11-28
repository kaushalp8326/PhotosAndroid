package com.example.androidphotos10.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

/**
 * Class that stores a picture and its relevant data.
 * @author John Hoban
 * @author Kaushal Patel
 *
 */
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    private String path;

    //private transient Image image;

    public String caption;

    /**
     * Tags for a Picture.
     */
    private HashMap<String, ArrayList<String>> tags=new HashMap<String, ArrayList<String>>();

    private LocalDateTime timestamp;

    /**
     * Picture constructor. Loads the image from the file system given a path.
     * @param path The picture's filepath.
     * @throws FileNotFoundException if the given filepath doesn't exist.
     */
    public Picture(String path) throws FileNotFoundException {
        this.path = path;
        long time = new File(path).lastModified(); // possible FNFE here
        this.timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), TimeZone.getDefault().toZoneId());
    }

    /**
     * Fetch this picture's path.
     * @return The path as a string.
     */
    public String getPath() {
        return path;
    }

    /**
     * Fetch this picture's {@code Image} representation. Since {@code Image} isn't serializable, the {@code Image} gets
     * loaded from the file system when it's first accessed, but is stored inside the class until the end of the session.
     * @return This picture's {@code Image}.
     */
    /*public Image getImage() {
        if(image == null) {
            try {
                this.image = new Image(new FileInputStream(path));
            }catch(FileNotFoundException e) {
                e.printStackTrace(); // should not occur, since you can't have a picture with an invalid path
            }
        }
        return image;
    }*/

    /**
     * Fetch this picture's timestamp.
     * @return Timestamp as a {@code LocalDateTime} object.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Get all the tags for a given picture.
     * @return ArrayList containing the pictures' tags in the format tag+":\n"+value.
     */
    public ArrayList<String> getTags(){
        ArrayList<String> tagPairs=new ArrayList<String>();
        if(tags == null || tags.size()==0) {
            return tagPairs;
        }
        Set<Entry<String, ArrayList<String>>> setOfTags = tags.entrySet();
        for(Entry<String, ArrayList<String>> entry : setOfTags){
            for(int i=0; i<entry.getValue().size(); i++) {
                tagPairs.add(entry.getKey()+":\n"+entry.getValue().get(i));
            }
        }
        return tagPairs;
    }

    /**
     * Pretty print method for tags. Used when printing tags for a picture in a slide show.
     * @return String used to print all the tags for a picture.
     */
    public String printTags() {
        String ret="";
        ArrayList<String> tagPairs=this.getTags();
        if(tagPairs==null) {
            return ret;
        }
        for(String s:tagPairs) {
            String tag=s.substring(0,s.indexOf(":"));
            String value=s.substring(s.indexOf("\n")+1);
            ret+=tag+": "+value+", ";
        }
        if(!ret.equalsIgnoreCase("")){
            ret=ret.substring(0, ret.length()-2);
        }
        return ret;
    }

    /**
     * Adds a given tag to the picture.
     * @param tag Tag to add to (ex. Location)
     * @param value Tag value to add (ex. New York)
     */
    public void addTag(String tag, String value) {
        if(tags.get(tag)==null) {
            ArrayList<String> toAdd=new ArrayList<String>();
            toAdd.add(value);
            tags.put(tag, toAdd);
        }else {
            ArrayList<String> current=tags.get(tag);
            if(current.contains(value)) {
                //tag, value combination already present
                //TODO error dialog
                //throw new IllegalArgumentException();
            }else {
                tags.get(tag).add(value);
            }
        }
    }

    /**
     * Removes a given tag from the picture.
     * @param tag Tag to remove from (ex. Location)
     * @param value Tag value to remove (ex. New York)
     */
    public void removeTag(String tag, String value) {
        if(tags.get(tag)==null) {
            //tag was not present
        }else {
            tags.get(tag).remove(value);
        }
    }

}
