package tagram;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageTest {
    @Test
    void testPathGenerator() {
        Image newImage = new Image("/user", "name", ".jpg", new ArrayList<>());
        String path = newImage.pathGenerator();
        assertEquals("/user/name.jpg", path);
    }

    @Test
    void testGetDirectory() {
        Image newImage = new Image("/user", "name", ".jpg", new ArrayList<>());
        assertEquals("/user/", newImage.getDirectory());
    }

    @Test
    void testSetDirectory() {
        Image newImage = new Image("/user", "name", ".jpg", new ArrayList<>());
        newImage.setDirectory("/image");
        assertEquals("/image", newImage.getDirectory());
    }

    @Test
    void testGetExtension() {
        Image newImage = new Image("/user", "name", ".jpg", new ArrayList<>());
        assertEquals(".jpg", newImage.getExtension());
    }

    @Test
    void testAddNewTag() {
        String newTag = "a tag";
        ArrayList<String> oldTags = new ArrayList<>();
        ArrayList<String> oldNames = new ArrayList<>();
        ArrayList<String> expectedTags = new ArrayList<>();
        expectedTags.add(newTag);
        Image newImage = new Image("/user", "name", ".jpg", oldTags, oldNames);
        newImage.addTag(newTag);
        assertArrayEquals(expectedTags.toArray(), newImage.getTags().toArray());
    }

    @Test
    void testAddExistedTag() {
        String newTag = "a tag";
        ArrayList<String> oldNames = new ArrayList<>();
        ArrayList<String> oldTags = new ArrayList<>();
        ArrayList<String> expectedTags = new ArrayList<>();
        expectedTags.add(newTag);
        oldTags.add(newTag);
        Image newImage = new Image("/user", "name @a tag", ".jpg", oldTags, oldNames);
        newImage.addTag(newTag);
        assertArrayEquals(expectedTags.toArray(), newImage.getTags().toArray());
        assertEquals(1, newImage.getTags().size());
    }

    @Test
    void testDeleteTag() {
        String newTag = "a tag";
        ArrayList<String> oldNames = new ArrayList<>();
        ArrayList<String> oldTags = new ArrayList<>();
        oldTags.add(newTag);
        Image newImage = new Image("/user", "name @a tag", ".jpg", oldTags, oldNames);
        newImage.deleteTag(newTag);
        assertArrayEquals(new ArrayList<>().toArray(), newImage.getTags().toArray());
    }

    @Test
    void testDeleteNonExistedTag() {
        String newTag = "a tag";
        String targetTag = "empty tag";
        ArrayList<String> oldNames = new ArrayList<>();
        ArrayList<String> oldTags = new ArrayList<>();
        ArrayList<String> expectedTags = new ArrayList<>();
        expectedTags.add(newTag);
        oldTags.add(newTag);
        Image newImage = new Image("/user", "name @a tag", ".jpg", oldTags, oldNames);
        newImage.deleteTag(targetTag);
        assertArrayEquals(expectedTags.toArray(), newImage.getTags().toArray());
    }

    @Test
    void testEditTag() {
        String newTag = "a tag";
        String targetTag = "second tag";
        ArrayList<String> oldNames = new ArrayList<>();
        ArrayList<String> oldTags = new ArrayList<>();
        ArrayList<String> expectedTags = new ArrayList<>();
        expectedTags.add(targetTag);
        oldTags.add(newTag);
        Image newImage = new Image("/user", "name @a tag", ".jpg", oldTags, oldNames);
        newImage.editTag(newTag, targetTag);
        assertArrayEquals(expectedTags.toArray(), newImage.getTags().toArray());
    }

    @Test
    void testNameGeneratorAndGetFullNameByAddTag() {
        String newTag = "a tag";
        ArrayList<String> oldTags = new ArrayList<>();
        ArrayList<String> oldNames = new ArrayList<>();
        ArrayList<String> expectedTags = new ArrayList<>();
        expectedTags.add(newTag);
        Image newImage = new Image("/user", "name", ".jpg", oldTags, oldNames);
        newImage.addTag(newTag);
        assertEquals("name @a tag", newImage.getFullName());
    }

    @Test
    void testNameGeneratorAndGetFullNameByDeleteTag() {
        String newTag = "a tag";
        ArrayList<String> oldNames = new ArrayList<>();
        ArrayList<String> oldTags = new ArrayList<>();
        Image newImage = new Image("/user", "name", ".jpg", oldTags, oldNames);
        newImage.addTag(newTag);
        newImage.deleteTag(newTag);
        assertEquals("name", newImage.getFullName());
    }

    @Test
    void testNameGeneratorAndGetFullNameByEditTag() {
        String newTag = "a tag";
        String targetTag = "second tag";
        ArrayList<String> oldNames = new ArrayList<>();
        ArrayList<String> oldTags = new ArrayList<>();
        ArrayList<String> expectedTags = new ArrayList<>();
        expectedTags.add(targetTag);
        oldTags.add(newTag);
        Image newImage = new Image("/user", "name", ".jpg", oldTags, oldNames);
        newImage.editTag(newTag, targetTag);
        assertEquals("name @second tag", newImage.getFullName());
    }

    @Test
    void testGetPastNames() {
        String newTag = "a tag";
        String secondTag = "second tag";
        ArrayList<String> oldTags = new ArrayList<>();
        ArrayList<String> oldNames = new ArrayList<>();
        String[] expectedArray = new String[3];
        expectedArray[0] = "name @a tag";
        expectedArray[1] = "name @a tag @second tag";
        expectedArray[2] = "name @second tag";
        Image newImage = new Image("/user", "name", ".jpg", oldTags, oldNames);
        newImage.addTag(newTag);
        newImage.addTag(secondTag);
        newImage.deleteTag(newTag);
        newImage.deleteTag(secondTag);
        assertArrayEquals(expectedArray, newImage.getPastNames().toArray());
    }

    @Test
    void testChangeToPastName() {
        String newTag = "a tag";
        String secondTag = "second tag";
        ArrayList<String> newTags = new ArrayList<>();
        ArrayList<String> newNames = new ArrayList<>();
        ArrayList<String> oldTags = new ArrayList<>();
        ArrayList<String> oldNames = new ArrayList<>();
        Image newImage = new Image("/user", "name", ".jpg", newTags, newNames);
        newImage.addTag(newTag);
        Image pastName = new Image("/user", "name", ".jpg", oldTags, oldNames);
        pastName.addTag(newTag);
        newImage.addTag(secondTag);
        newImage.ChangeToPastName(pastName);
        System.out.println(newImage);
        assertEquals("name @a tag", newImage.getFullName());
    }
}