package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import tagram.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This class controls the UI and interacts with user.
 */
public class Controller implements Initializable {

    /**
     * The directory chose by the user.
     */
    @FXML
    private File directory;

    /**
     * A List of the images under the directory chose by the user.
     */
    @FXML
    private ListView<tagram.Image> imageNames;

    /**
     * A list of images that contains the selected tags.
     */
    @FXML
    private ListView<tagram.Image> relatedImages;

    /**
     * A list of images that were selected by user before.
     */
    @FXML
    private ListView<tagram.Image> historyImages;

    /**
     * A List of past names of the selected image.
     */
    @FXML
    private ListView<String> pastName;

    /**
     * A List of all available tags created by the user.
     */
    @FXML
    private ListView<String> tags;

    /**
     * The place where to display the image.
     */
    @FXML
    private ImageView imageView;

    /**
     * The pane contains the absolutePath of the selected image.
     */
    @FXML
    private Pane absolutePath;

    /**
     * A List of all tags added to the selected image.
     */
    @FXML
    private ListView<String> selectedImageTag;

    /**
     * The tag the user wants to create.
     */
    @FXML
    private TextField newTag;

    /**
     * The name of the tag the user wants to change to.
     */
    @FXML
    private TextField replaceTag;

    /**
     * log text
     */
    @FXML
    private TextArea logsText;

    /**
     * The image chose by the user.
     */
    private javafx.scene.image.Image image;

    /**
     * The selected ImageName from the imageNames
     */
    private tagram.Image selected;

    /**
     * The flag indicates whether the user selects a directory or a root directory.
     */
    private static Boolean root;

    /**
     * Initialize the program by loading the data.
     *
     * @param location  Location
     * @param resources Resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoadSave.loadImagesAndTags();
        Logr.initialize();
        Refresh();
    }

    /**
     * Choose a folder
     * This only displays images inside folder, and exclude those in sub folders.
     */
    public void chooseFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a Directory of images.");
        Stage stage = new Stage();
        File newDirectory = chooser.showDialog(stage);
        if (newDirectory != null) {
            directory = newDirectory;
        }
        root = false;
        Refresh();
    }

    /**
     * Choose a parent directory
     * This displays images anywhere under the chosen folder.
     */
    public void chooseRoot() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a Directory of images.");
        Stage stage = new Stage();
        directory = chooser.showDialog(stage);
        root = true;
        Refresh();
    }

    /**
     * List all available images's names under the current directory.
     */
    private void listImageNamesAction() {
        ObservableList<tagram.Image> names = ImageManager.listImageNames(directory, root);
        imageNames.getItems().clear();
        imageNames.setItems(names);
    }

    /**
     * Display the image chose by the user in ImageView.
     */
    private void displayImage() {
        if (selected != null) {
            try {
                image = new javafx.scene.image.Image(
                        new FileInputStream(ImageManager.getImagePath(selected)));
            } catch (FileNotFoundException fe) {
                absolutePath.getChildren().add(new Text("Please select an image."));
            }
        } else {
            image = null;
        }
        imageView.setImage(image);
        Refresh();
    }

    /**
     * Display the absolutePath of the image chose by the user in ImageView.
     */
    private void displayAbsolutePath() {
        absolutePath.getChildren().clear();
        Text text;
        if (selected != null) {
            try {
                text = new Text(ImageManager.getImagePath(selected));
            } catch (NullPointerException ne) {
                text = new Text("Please select an image.");
            }
        } else {
            text = new Text("Please select an image.");
        }
        text.setX(10);
        text.setY(20);
        absolutePath.getChildren().add(text);
    }

    /**
     * Move the selected image to another directory.
     */
    public void moveDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        Stage stage = new Stage();
        File newDirectory = chooser.showDialog(stage);
        if (newDirectory != null && selected != null) { //user chooses a directory
            try {
                ImageManager.moveImage(newDirectory, selected);
            } catch (IOException ie) {
                absolutePath.getChildren().add(new Text("Directory does not exist"));
            }
            Refresh();
        }
    }

    /**
     * Open the directory that contains the selected image.
     */
    public void openCurrentDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(directory);
        Stage stage = new Stage();
        directory = chooser.showDialog(stage);
    }

    /**
     * List the past names used by the selected image in ListView pastName.
     */
    public void listPastNamesAction() {
        ObservableList<String> past = ImageManager.listPastNames(selected);
        pastName.setItems(past);
    }

    /**
     * Change the selected image's name to one of the past names selected by the user.
     */
    public void changeToPastNameAction() {
        String selectedName = pastName.getSelectionModel().getSelectedItem();
        try {
            ImageManager.changeToPastName(selected, selectedName);
            Refresh();
        } catch (NullPointerException e) {
            absolutePath.getChildren().clear();
            Text text = new Text("Please select a past name");
            text.setY(20);
            absolutePath.getChildren().add(text);
        }
    }

    /**
     * List all available tags in ListView tags.
     */
    private void showTags() {
        ObservableList<String> tagNames = FXCollections.observableArrayList();
        ArrayList<String> existingTags = TagManager.displayExisting();
        tagNames.addAll(existingTags);
        tags.setItems(tagNames);
        tags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Display the selected image's tags in ListView selectedImageTag.
     */
    private void showSelectedImageTag() {
        ObservableList<String> tagNames = FXCollections.observableArrayList();
        if (selected != null) {
            tagNames = ImageManager.getSelectedImageTags(selected);
            selectedImageTag.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
        selectedImageTag.setItems(tagNames);
    }

    /**
     * Add the new tag to the selected image.
     * If the new tag entered contains symbols '@' ':' '/',  tag will not be added.
     */
    public void addTagAction() {
        if (selected == null) {
            TagManager.createTag(newTag.getText());
        } else {
            ObservableList<String> selectedTags = tags.getSelectionModel().getSelectedItems();
            if (selectedTags.size() < 2) {
                String tagName = newTag.getText();
                if (tagName.contains("@") || tagName.contains(":") || tagName.contains("/")) {
                    newTag.setText("Please don't give me tags includes '@' ':' '/'!");
                } else {
                    ImageManager.addTag(selected, tagName);

                }
            } else {
                ImageManager.addTags(selected, selectedTags);
            }
        }
        Refresh();
    }

    /**
     * Delete the selected tag from the selected image.
     */
    public void deleteTagAction() {
        ObservableList<String> selectedImageTags =
                selectedImageTag.getSelectionModel().getSelectedItems();
        if (selected != null) {
            ImageManager.deleteTags(selected, selectedImageTags);
        }
        Refresh();
    }

    /**
     * Delete this tag from available tags.
     * Any image that contains this tag will no longer have it any more.
     */
    public void deleteTagsAction() {
        ObservableList<String> selectedTags = tags.getSelectionModel().getSelectedItems();
        Set<Image> images = new HashSet<>();
        for (String selectedTag : selectedTags) {
            if (TagManager.allTags.containsKey(selectedTag)) {
                images.addAll(TagManager.allTags.get(selectedTag));
            }
        }
        for (Image image : images) {
            ImageManager.deleteTags(image, selectedTags);
        }
        TagManager.deleteTag(selectedTags);
        Refresh();
    }

    /**
     * Edit an existing tag into given input.
     * If the new tag entered contains symbols '@' ':' '/',  tag will not be edited.
     */
    public void editTagAction() {
        ObservableList<String> selectedTags = tags.getSelectionModel().getSelectedItems();
        if (selectedTags.size() == 1) {
            String tagName = replaceTag.getText();
            String oldTag = selectedTags.get(0);
            if (tagName.contains("@") || tagName.contains(":") || tagName.contains("/")) {
                replaceTag.setText("Tags cannot contain '@' ':' '/'");
            } else if (TagManager.allTags.containsKey(tagName)) {
                replaceTag.setText("This tag already exists.");
            } else {
                if (TagManager.allTags.containsKey(oldTag)) {
                    ArrayList<tagram.Image> imageList = new ArrayList<>();
                    imageList.addAll(TagManager.allTags.get(oldTag));
                    TagManager.allTags.remove(oldTag);
                    TagManager.allTags.put(tagName, imageList);
                    for (tagram.Image i : imageList) {
                        ImageManager.editTag(i, oldTag, tagName);
                    }
                }
            }
        }
        Refresh();
    }

    /**
     * Display the new tag the user wants to add.
     * If multiple tags are selected, then the last chosen tag will be displayed.
     */
    private void displayPromptText() {
        String selectedTag = tags.getSelectionModel().getSelectedItem();
        if (selectedTag != null)
            newTag.setText(selectedTag);
        else {
            String tagName = newTag.getText();
            newTag.setText(tagName);
        }
    }

    /**
     * Display the images' names that contains selected tags.
     */
    private void listRelatedNamesAction() {
        ObservableList<String> selectedTags = tags.getSelectionModel().getSelectedItems();

        ObservableList<tagram.Image> related = ImageManager.listRelatedName(selectedTags.get(0));
        if (selectedTags.size() > 1) {
            for (String tag : selectedTags.subList(1, selectedTags.size())) {
                related.retainAll(ImageManager.listRelatedName(tag));
            }
        }
        relatedImages.getItems().clear();
        relatedImages.setItems(related);
    }

    /**
     * Display the images' names in the directory were chosen by user before.
     */
    private void listHistoryNamesAction() {
        ObservableList<tagram.Image> history = ImageManager.listHistoryNames();
        historyImages.getItems().clear();
        historyImages.setItems(history);
    }

    /**
     * Display image in imageView, when it's selected from imageNames listView.
     */
    public void displayMain() {
        tagram.Image main = imageNames.getSelectionModel().getSelectedItem();
        if (main != selected) {
            selected = main;
        } else {
            selected = null;
        }
        displayImage();
    }

    /**
     * Display image in imageView, when it's selected from relatedImages listView.
     */
    public void displayRelate() {
        tagram.Image relate = relatedImages.getSelectionModel().getSelectedItem();
        if (relate != selected) {
            selected = relate;
        } else {
            selected = null;
        }
        displayImage();
    }

    /**
     * Display image in imageView, when it's selected from historyImages listView.
     */
    public void displayHistory() {
        tagram.Image history = historyImages.getSelectionModel().getSelectedItem();
        if (history != selected) {
            selected = history;
        } else {
            selected = null;
        }
        displayImage();
    }

    /**
     * Update the tags relate actions.
     * Display the last selected tag, and images that contain selected tags.
     */
    public void availableTagsController() {
        displayPromptText();
        listRelatedNamesAction();
    }

    /**
     * Refresh the program in order to show the most updated names and tags.
     */
    private void Refresh() {
        listImageNamesAction();
        listRelatedNamesAction();
        listPastNamesAction();
        showTags();
        showSelectedImageTag();
        displayAbsolutePath();
        viewLog();
        listHistoryNamesAction();
    }

    /**
     * Open a txt file which records all the changes made by the user
     * and some extra information
     */
    public void viewLog() {
        String cur = System.getProperty("user.dir");
        cur = cur + "/imageLog.txt";
        File file = new File(cur);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            StringBuilder logString = new StringBuilder();
            ArrayList<String> logList = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String timeStamp = scanner.nextLine() + "\n";
                String info = scanner.nextLine() + "\n";
                logList.add(info);
                logList.add(timeStamp);
            }
            Collections.reverse(logList);
            for (String log : logList) {
                logString.append(log);
            }
            logsText.setText(logString.toString());

        } catch (FileNotFoundException e) {
            logsText.setText("No logs available.");
        }
    }

    /**
     * list some info about the group.
     */
    public void about() {
        Stage stage = new Stage();
        stage.setTitle("About");
        StackPane layout = new StackPane();
        stage.setScene(new Scene(layout, 300, 250));
        TextArea about = new TextArea();
        about.setText("Tagram" + "\n" +
                "CSC207 group_0523" + "\n" + "\n" +
                "JIAJUN CHEN" + "\n" +
                "CHU CHUNTONG" + "\n" +
                "ZIYI GONG" + "\n" +
                "HENRY LIAO" + "\n");
        about.setEditable(false);
        layout.getChildren().add(about);
        stage.show();
    }
}