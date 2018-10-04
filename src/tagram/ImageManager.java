package tagram;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * This class manages the behavior of Images.
 */
public class ImageManager {

    /**
     * A data Structure used to store all the Images.
     */
    static ArrayList<Image> ImageMap = new ArrayList<>();


    /**
     * Return an ObservableList of images contained in given directory.
     * If root is true, then ObservableList will include images in sub-directories as well.
     *
     * @param directory a file containing images
     * @param root      indicate that if this file is a root directory or not
     * @return ObservableList of images inside file
     */
    public static ObservableList<Image> listImageNames(File directory, Boolean root) {
        ObservableList<Image> names = FXCollections.observableArrayList();

        if (directory != null) { // if the user chose something:
            File[] imagePaths = directory.listFiles();

            if (imagePaths != null) {
                for (File file : imagePaths) {
                    String path = file.getPath();
                    // see if it is a file
                    if (path.lastIndexOf(".") != -1) {
                        String extension = path.substring(path.lastIndexOf("."));
                        // see if it is an image
                        if (extension.equals(".jpg") || extension.equals(".png") ||
                                extension.equals(".bmp") || extension.equals(".gif")) {
                            Image newImage = nameDecoder(file.getParent(), file.getName());
                            HashMap<String, Image> namesList = new HashMap<>();

                            for (Image exists : ImageManager.ImageMap) {
                                namesList.put(exists.getDirectory() + exists.getName(), exists);
                            }
                            if (namesList.containsKey(newImage.getDirectory() + newImage.getName())) {
                                synchronize(newImage, namesList.get(newImage.getDirectory() + newImage.getName()));
                                names.add(namesList.get(newImage.getDirectory() + newImage.getName()));
                            } else {
                                for (String tag : newImage.getTags()) {
                                    TagManager.createTag(tag, newImage);
                                }
                                ImageManager.ImageMap.add(newImage);
                                names.add(newImage);
                            }
                        }
                    } else {
                        if (root)
                            names.addAll(listImageNames(file, true));
                    }
                }
            }
        }
        return names;
    }


    /**
     * Synchronize the Image object in the ImageMap with Image scanned.
     * If the user adds a new tag to the Image by renaming outside the program, this tag will be added too.
     * If the user adds a duplicate tag by renaming, the duplicate tag will be removed and the file name will be
     * changed.
     *
     * @param newImage     Image detects in the directory
     * @param existedImage Image in the ImageMap
     */
    static void synchronize(Image newImage, Image existedImage) {
        ArrayList<String> newImageTags = new ArrayList<>();
        newImageTags.addAll(newImage.getTags());
        ArrayList<String> existedImageTags = new ArrayList<>();
        existedImageTags.addAll(existedImage.getTags());
        for (String tag : newImageTags) {
            if (!existedImageTags.contains(tag)) {
                existedImage.addTag(tag);
            }
        }
        for (String tag : existedImageTags) {
            if (!newImageTags.contains(tag)) {
                existedImage.deleteTag(tag);
            }
        }
        if (!Objects.equals(newImage.pathGenerator(), existedImage.pathGenerator())) {
            Rename.rename(newImage.pathGenerator(), existedImage.pathGenerator());
        }
    }


    /**
     * Move the image to given directory
     * Print "File not found!" the directory is not exist
     *
     * @param newDirectory new directory for image to move to
     * @param image        the image need to move
     * @throws IOException the given newDirectory may not be found.
     *                     moveDirectory() method in Controller will catch the IOException and deal with it.
     */
    public static void moveImage(File newDirectory, Image image) throws IOException {
        Path oldPath = new File(image.pathGenerator()).toPath();
        image.setDirectory(newDirectory.getPath() + "/");
        Path target = new File(image.pathGenerator()).toPath();
        Files.move(oldPath, target);
    }


    /**
     * List the past names of selected image.
     *
     * @param selected the selected image.
     * @return the observable list of past names.
     */
    public static ObservableList<String> listPastNames(Image selected) {
        ObservableList<String> past = FXCollections.observableArrayList();
        try {
            ArrayList<String> p = selected.getPastNames();
            if (p.size() > 0) {
                past.addAll(p);
            } else {
                past.add("No past names available");
            }
        } catch (NullPointerException e) {
            past.add("No past names available");
        }
        return past;
    }


    /**
     * return the selected image's tags
     *
     * @param selected the selected image.
     * @return ArrayList of tags of an image.
     */
    public static ObservableList<String> getSelectedImageTags(Image selected) {
        ObservableList<String> tags = FXCollections.observableArrayList();
        try {
            ArrayList<String> lst = selected.getTags();
            tags.addAll(lst);
        } catch (NullPointerException e) {
            tags.add("No tags available");
        }
        return tags;
    }


    /**
     * add the tag to the image
     *
     * @param selected the selected image.
     * @param tagName  the tag to be added.
     */
    public static void addTag(Image selected, String tagName) {
        selected.addTag(tagName);
    }


    /**
     * add an ObservableList of tags to an image
     *
     * @param selected     the selected image.
     * @param selectedTags the tag to be deleted.
     */
    public static void addTags(Image selected, ObservableList<String> selectedTags) {
        selected.addTags(selectedTags);
    }

    /**
     * delete an ObservableList of tags from an image
     *
     * @param selected     the selected image.
     * @param selectedTags the tag to be deleted.
     */
    public static void deleteTags(Image selected, ObservableList<String> selectedTags) {
        selected.deleteTags(selectedTags);
    }


    /**
     * edit a tag from an image
     *
     * @param selected the selected image.
     * @param oldTag   the tag to be edited.
     * @param newTag   the tag to be edited to.
     */
    public static void editTag(Image selected, String oldTag, String newTag) {
        selected.editTag(oldTag, newTag);
    }


    /**
     * change an image to past name
     *
     * @param selected     the selected image.
     * @param selectedName the past name.
     */
    public static void changeToPastName(Image selected, String selectedName) {
        Image pastImage = nameDecoder(selected.getDirectory(), selectedName + selected.getExtension());
        selected.ChangeToPastName(pastImage);
    }


    /**
     * return an Image object according to its name
     *
     * @param directory         the directory the image is in
     * @param nameWithExtension the image file name with its extension
     * @return An Image object
     */
    static Image nameDecoder(String directory, String nameWithExtension) {
        String name = nameWithExtension.substring(0, nameWithExtension.lastIndexOf("."));
        String extension = nameWithExtension.substring(nameWithExtension.lastIndexOf("."));
        String[] parts = name.split(" @");
        if (parts.length == 1) {
            return new Image(directory, name, extension, new ArrayList<>());
        } else {
            ArrayList<String> tags = new ArrayList<>();
            tags.addAll(Arrays.asList(parts).subList(1, parts.length));
            return new Image(directory, parts[0], extension, tags);
        }
    }


    /**
     * return the ObservableList of ImageNames that contains the selectedTag
     *
     * @param selectedTag the tag selected by user
     * @return An ObservableList that can be added to ListView relateImages
     */
    public static ObservableList<Image> listRelatedName(String selectedTag) {
        ObservableList<Image> related = FXCollections.observableArrayList();
        if (TagManager.allTags.containsKey(selectedTag)) {
            related.addAll(TagManager.allTags.get(selectedTag));
        }
        return related;
    }


    /**
     * return the ObservableList of Images' names in the directory were chosen by user before.
     *
     * @return An ObservableList that can be added to ListView historyImage
     */
    public static ObservableList<Image> listHistoryNames() {
        ObservableList<Image> history = FXCollections.observableArrayList();
        history.addAll(ImageMap);
        return history;
    }


    /**
     * return the image path of an image
     *
     * @param image the selected image.
     * @return the path of an image.
     */
    public static String getImagePath(Image image) {
        return image.pathGenerator();
    }


}
