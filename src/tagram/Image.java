package tagram;

import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * A name and tag system for Image.
 */
public class Image {

    /**
     * directory of image
     */
    private String directory;

    /**
     * name of image
     */
    private String name;

    /**
     * extension of image
     */
    private String extension;

    /**
     * list of tags added to image
     */
    private ArrayList<String> tags = new ArrayList<>();

    /**
     * list of past names of image
     */
    private ArrayList<String> pastNames;


    /**
     * Class constructor with given path,
     * specifies the directory, name, extension and path of Image.
     *
     * @param directory the parent directory of Image
     * @param name      the image name
     * @param extension the extension of the image
     * @param tags      tags of an image
     */
    Image(String directory, String name, String extension, ArrayList<String> tags) {
        this(directory + "/", name, extension, tags, new ArrayList<>());
    }

    /**
     * Class constructor with directory, name, extension, tags and pastNames.
     *
     * @param directory directory of image
     * @param name      name of image
     * @param extension extension of image
     * @param tags      tags included by image
     * @param pastNames the past names of image
     */
    Image(String directory, String name, String extension, ArrayList<String> tags, ArrayList<String> pastNames) {
        this.directory = directory;
        this.name = name;
        this.extension = extension;
        this.tags = tags;
        this.pastNames = pastNames;
    }

    /**
     * Add a new tag to this image. Do nothing if this image
     * contains this tag.
     *
     * @param tag the tag
     */

    void addTag(String tag) {
        if (!tag.equals("")) {
            String preName = nameGenerator();
            if (!tags.contains(tag)) {
                this.tags.add(tag);
                TagManager.createTag(tag, this);
                if (!pastNames.contains(preName))
                    this.pastNames.add(preName);
                if (pastNames.contains(this.nameGenerator())) {
                    this.pastNames.remove(this.nameGenerator());
                }
                Rename.rename(directory + preName + extension, pathGenerator());
            }
        }
    }


    /**
     * Delete an existing tag from this image.
     * Do nothing if this image does not have this tag.
     *
     * @param tag the tag
     */
    void deleteTag(String tag) {
        String preName = nameGenerator();
        if (tags.contains(tag)) {
            tags.remove(tag);
            if (!pastNames.contains(preName))
                this.pastNames.add(preName);
            if (pastNames.contains(this.nameGenerator())) {
                this.pastNames.remove(this.nameGenerator());
            }
            Rename.rename(directory + preName + extension, pathGenerator());
        }
    }

    /**
     * add an ObservableList of tags to this image
     *
     * @param selectedTags the tag to be deleted.
     */
    void addTags(ObservableList<String> selectedTags) {
        String preName = nameGenerator();
        Boolean changed = false;
        for (String tag : selectedTags) {
            if (!tag.equals("")) {
                if (!tags.contains(tag)) {
                    this.tags.add(tag);
                    TagManager.createTag(tag, this);
                    changed = true;
                }
            }
        }
        if (changed) {
            if (!pastNames.contains(preName))
                this.pastNames.add(preName);
            if (pastNames.contains(this.nameGenerator())) {
                this.pastNames.remove(this.nameGenerator());
            }
            Rename.rename(directory + preName + extension, pathGenerator());
        }
    }

    /**
     * delete an ObservableList of tags from this image
     *
     * @param selectedTags the tag to be deleted.
     */
    void deleteTags(ObservableList<String> selectedTags) {
        String preName = nameGenerator();
        Boolean changed = false;
        for (String tag : selectedTags) {
            if (tags.contains(tag)) {
                tags.remove(tag);
                TagManager.allTags.get(tag).remove(this);
                changed = true;
            }
        }
        if (changed) {
            if (!pastNames.contains(preName))
                this.pastNames.add(preName);
            if (pastNames.contains(this.nameGenerator())) {
                this.pastNames.remove(this.nameGenerator());
            }
            Rename.rename(directory + preName + extension, pathGenerator());
        }
    }

    /**
     * Edit an existing tag from the tagList. All images which contain this tag will
     * have this tag changed to the edited one.
     *
     * @param oldTag the old tag
     * @param newTag the new tag
     */
    void editTag(String oldTag, String newTag) {
        String preName = nameGenerator();
        if (tags.contains(oldTag)) {
            tags.set(tags.indexOf(oldTag), newTag);
            if (!pastNames.contains(preName))
                this.pastNames.add(preName);
            Rename.rename(directory + preName + extension, pathGenerator());
            if (pastNames.contains(this.nameGenerator())) {
                pastNames.remove(this.nameGenerator());
            }
        }
    }

    /**
     * Generate the path of image
     *
     * @return String from of image path
     */

    String pathGenerator() {
        return directory + nameGenerator() + extension;
    }

    /**
     * Generate the name of image based on the tags the image contains.
     *
     * @return name of image
     */
    private String nameGenerator() {

        if (tags.size() == 0) {
            return name;
        } else {
            StringBuilder sum = new StringBuilder();
            sum.append(name);

            for (String tag : tags) {
                sum.append(" @").append(tag);
            }
            return sum.toString();
        }
    }

    /**
     * change to past name.
     */
    void ChangeToPastName(Image oldImage) {
        if (!this.pastNames.contains(this.nameGenerator())) {
            this.pastNames.add(this.nameGenerator());
        }
        String oldPath = this.pathGenerator();
        for (String tag : this.tags) {
            TagManager.deleteTag(tag, this);
        }
        this.tags = new ArrayList<>(oldImage.getTags());
        for (String tag : this.tags) {
            TagManager.createTag(tag, this);
        }
        this.pastNames.remove(this.nameGenerator());
        Rename.rename(oldPath, this.pathGenerator());
    }

    /**
     * get original name of image
     *
     * @return name of the image without tags
     */
    String getName() {
        return this.name;
    }


    /**
     * get the name of image after adding tag
     *
     * @return full name of the image with the tags
     */
    String getFullName() {
        return nameGenerator();
    }

    /**
     * directory getter
     *
     * @return directory of image
     */
    String getDirectory() {
        return this.directory;
    }

    /**
     * directory setter
     */
    void setDirectory(String newDirectory) {
        this.directory = newDirectory;
    }

    /**
     * extension getter
     *
     * @return extension of image
     */
    String getExtension() {
        return this.extension;
    }

    /**
     * originalName getter
     *
     * @return tags of an image
     */
    ArrayList<String> getTags() {
        return this.tags;
    }

    /**
     * past names getter
     *
     * @return ArrayList of pastNames
     */
    ArrayList<String> getPastNames() {
        return pastNames;
    }

    /**
     * Override String method
     *
     * @return String form of this image = name of image
     */
    @Override
    public String toString() {
        return nameGenerator();
    }

}
