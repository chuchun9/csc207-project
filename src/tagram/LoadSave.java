package tagram;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Class to save Image information when exist program
 * and load information when reopen it.
 */
public class LoadSave {

    /**
     * Save Image information e.g. tags and pastNames when exist program
     */
    public static void saveImages() {
        try {
            FileWriter save = new FileWriter("documentedImage.txt");
            BufferedWriter out = new BufferedWriter(save);
            for (String tag : TagManager.allTags.keySet()) {
                out.write("@" + tag);
            }
            out.newLine();
            for (Image imageName : ImageManager.ImageMap) {
                out.write(imageName.getName());
                out.newLine();
                out.write(imageName.getDirectory());
                out.newLine();
                out.write(imageName.getExtension());
                out.newLine();
                for (String tag : imageName.getTags()) {
                    out.write("@" + tag);
                }
                out.newLine();
                for (String pastName : imageName.getPastNames()) {
                    out.write(":" + pastName);
                }
                out.newLine();

            }
            out.close();
        } catch (IOException e) {
            Logr.saveLog();
        }

    }

    /**
     * Load Image information e.g. tags and pastNames when open programg
     */

    public static void loadImagesAndTags() {
        File file = new File("documentedImage.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                String tags = scanner.nextLine();
                String[] tagsArray = tags.split("@");
                for (int i = 1; i < tagsArray.length; i++) {
                    TagManager.createTag(tagsArray[i]);
                }
            }
            while (scanner.hasNextLine()) {
                ArrayList<String> store = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    if (scanner.hasNextLine()) {

                        store.add(scanner.nextLine());
                    }
                }
                String Tags = store.get(3);
                String[] TagsArray = Tags.split("@");
                List<String> TagsList = Arrays.asList(TagsArray);
                ArrayList<String> TagsArrayList = new ArrayList<>();
                TagsArrayList.addAll(TagsList);
                TagsArrayList.remove(0);

                String pastNames = store.get(4);
                String[] pastNamesArray = pastNames.split(":");
                List<String> pastNamesList = Arrays.asList(pastNamesArray);
                ArrayList<String> pastNameArrayList = new ArrayList<>();
                pastNameArrayList.addAll(pastNamesList);
                pastNameArrayList.remove(0);
                Image recreatedImage = new Image(store.get(1),
                        store.get(0),
                        store.get(2),
                        TagsArrayList,
                        pastNameArrayList);
                ImageManager.ImageMap.add(recreatedImage);
                for (String tag : TagsArrayList) {
                    TagManager.createTag(tag, recreatedImage);
                }

            }
        } catch (FileNotFoundException e) {
            Logr.firstOpenProgramLog();
        }

    }

}
