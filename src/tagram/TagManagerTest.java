package tagram;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TagManagerTest {
    @BeforeEach
    void initialize() {
        TagManager.allTags = new HashMap<>();
    }

    @Test
    void testCreateTag() {
        String tag = "first";
        TagManager.createTag(tag);
        assertTrue(TagManager.allTags.get(tag).equals(new ArrayList<>()));
    }

    @Test
    void testCreateTagWithImage() {
        String tag = "second";
        ArrayList<Image> test = new ArrayList<>();
        Image im = new Image("/user", "myImage", ".jpg", new ArrayList<>());
        TagManager.createTag(tag, im);
        test.add(im);
        assertTrue(TagManager.allTags.containsKey(tag) && test.equals(TagManager.allTags.get(tag)));
    }

    @Test
    void testDisplayExisting() {
        Image im = new Image("/user", "myImage", ".jpg", new ArrayList<>());
        String tag1 = "first";
        String tag2 = "second";
        String tag3 = "third";
        TagManager.createTag(tag1);
        TagManager.createTag(tag2);
        TagManager.createTag(tag3, im);
        ArrayList<String> test = new ArrayList<>(Arrays.asList("first", "second", "third"));
        assertTrue(new HashSet<>(test).equals(new HashSet<>(TagManager.displayExisting())));
    }

    @Test
    void TestDeleteImageInTag() {
        Image im = new Image("/user", "myImage", ".jpg", new ArrayList<>());
        Image im2 = new Image("/user", "myImage2", ".jpg", new ArrayList<>());
        TagManager.createTag("first", im);
        TagManager.createTag("first", im2);
        TagManager.deleteTag("first", im);
        assertTrue(TagManager.allTags.get("first").size() == 1
                && TagManager.allTags.get("first").get(0).equals(im2)
                && TagManager.allTags.containsKey("first"));
    }

    @Test
    void TestDeleteTag() {
        ObservableList<String> tst = FXCollections.observableArrayList();
        tst.add("first");
        Image im = new Image("/user", "myImage", ".jpg", new ArrayList<>());
        Image im2 = new Image("/user", "myImage2", ".jpg", new ArrayList<>());
        TagManager.createTag("first", im);
        TagManager.createTag("second", im2);
        TagManager.deleteTag(tst);
        assertTrue(TagManager.allTags.containsKey("second")
                && !TagManager.allTags.containsKey("first")
                && TagManager.allTags.get("second").get(0).equals(im2));
    }
}