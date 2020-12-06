package com.example.androidphotos10.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Class that stores a picture and its relevant data.
 * @author John Hoban
 * @author Kaushal Patel
 *
 */
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient Uri uri;
    private String path;

    public String caption;

    private final String PERSON = "Person";
    private final String LOCATION = "Location";

    /**
     * Tags for a Picture.
     */
    private HashMap<String, ArrayList<String>> tags=new HashMap<String, ArrayList<String>>();

    /**
     * Picture constructor. Loads the image from the file system given a path.
     * @param uri The picture's URI representation.
     */
    public Picture(Uri uri) {
        this.uri = uri;
        this.path = uri.toString();
    }

    /**
     * Fetch this picture's URI.
     * @return URI object.
     */
    public Uri getUri() {
        if(uri == null){
            uri = Uri.parse(path);
        }
        return uri;
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

    public String[] getTagArray(){
        Set<Entry<String, ArrayList<String>>> setOfTags = tags.entrySet();
        int size=0;
        for(Entry<String, ArrayList<String>> entry : setOfTags){
            for(int i=0; i<entry.getValue().size(); i++) {
                size++;
            }
        }
        String[] tagPairs=new String[size];
        if(tags == null || tags.size()==0) {
            return tagPairs;
        }
        int index=0;
        for(Entry<String, ArrayList<String>> entry : setOfTags){
            for(int i=0; i<entry.getValue().size(); i++) {
                tagPairs[index+i]=(entry.getKey()+":\n"+entry.getValue().get(i));
            }
            index+=entry.getValue().size();
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

    @Override
    public String toString(){
        return uri.toString();
    }

}
