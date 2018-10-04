package tagram;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageManagerTest {
    @Test
    void TestSynchronize() {
        Image existed = new Image("/user", "myImage", ".jpg", new ArrayList<>());
        Image newImage = new Image("/user", "myImage", ".jpg", new ArrayList<>());
        newImage.addTag("first");
        newImage.addTag("second");
        existed.addTag("third");
        ImageManager.synchronize(newImage, existed);

        assertTrue(existed.pathGenerator().equals(newImage.pathGenerator()));
    }

    @Test
    void TestGetSelectedImageTags() {
        Image tst = new Image("/user", "myImage", ".jpg", new ArrayList<>());
        tst.addTag("first");
        tst.addTag("second");
        tst.addTag("third");
        tst.deleteTag("first");
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("second", "third"));

        assertEquals(ImageManager.getSelectedImageTags(tst), expected);
    }

    @Test
    void TestChangeToPastName() {
        Image test = new Image("/user/", "myImage", ".jpg",
                new ArrayList<>(Arrays.asList("first", "third")),
                new ArrayList<>(Arrays.asList("myImage @first", "myImage @first @second"
                        , "myImage @first @second @third")));
        ArrayList<String> tag = new ArrayList<>(Arrays.asList("first", "second", "third"));
        ArrayList<String> pastNames = new ArrayList<>(Arrays.asList("myImage @first", "myImage @first @second",
                "myImage @first @third"));
        ImageManager.changeToPastName(test, "myImage @first @second @third");
        System.out.println(test.getFullName());
        System.out.println(test.getTags());
        System.out.println(test.getPastNames());
        assertTrue(test.getName().equals("myImage")
                && new HashSet<>(test.getTags()).equals(new HashSet<>(tag))
                && new HashSet<>(test.getPastNames()).equals(new HashSet<>(pastNames)));
    }

    @Test
    void TestNameDecoder() {
        String dir = "/user";
        String name = "MyImage @first @second @third.jpg";
        ArrayList<String> tag = new ArrayList<>(Arrays.asList("first", "second", "third"));
        Image actual = ImageManager.nameDecoder(dir, name);
        Image expected = new Image(dir, "MyImage", ".jpg", tag);
        assertEquals(expected.pathGenerator(), actual.pathGenerator());

    }

}