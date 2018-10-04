package tagram;

import java.io.File;

class Rename {

    static void rename(String prePath, String newPath) {
        File oldFile = new File(prePath);
        File newFile = new File(newPath);
        String preName = oldFile.getName();
        String newName = newFile.getName();
        Logr.renameLog(oldFile.renameTo(newFile), preName, newName);

    }
}
