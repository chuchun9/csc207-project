package tagram;


import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;

public class TagManager {
    /**
     * The data structure that we use to store tag and its related images.
     */
    public static HashMap<String, ArrayList<Image>> allTags = new HashMap<>();

    /**
     * Create a tag that does not contain any image in its array list as key in map if
     * the map does not contain that tag.
     *
     * @param tagName Teh tag that's asked to add.
     */
    public static void createTag(String tagName) {
        if (!allTags.containsKey(tagName)) {
            allTags.put(tagName, new ArrayList<>());
        }
    }


    /**
     * Create a Tag as key in our map if the map does not contain the tag,
     * and add the related ImageName to the value.
     *
     * @param tagName The tag that's required to be added.
     * @param i       The ImageName which wants to add tagName
     */
    static void createTag(String tagName, Image i) {
        if (!allTags.containsKey(tagName)) {
            allTags.put(tagName, new ArrayList<>());
            allTags.get(tagName).add(i);
        } else {
            allTags.get(tagName).add(i);
        }
    }

    static void deleteTag(String tagName, Image i) {
        if (allTags.containsKey(tagName)) {
            allTags.get(tagName).remove(i);
        }
    }

    public static void deleteTag(ObservableList<String> tags) {
        for (String tagName : tags) {
            if (allTags.containsKey(tagName)) {
                allTags.remove(tagName);
            }
        }
    }

    /**
     * Display all the existing tags currently used by an image.
     *
     * @return An ArrayList that contains all the existing tags.
     */
    public static ArrayList<String> displayExisting() {
        ArrayList<String> existing = new ArrayList<>();
        existing.addAll(allTags.keySet());
        return existing;
    }

}

