package org.example.dataReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    String localAppData = System.getenv("LOCALAPPDATA"); //get users %LocalAppData% path
    File folderPath = new File(localAppData, "untitled-learnApp");
    List<File> filesInDirectory = new ArrayList<File>();

    public List<File> getAllFilesInPageDirectory() {
        if (folderPath.exists() && folderPath.isDirectory()) {
            File[] files = folderPath.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        filesInDirectory.add(file);
                    }
                }
                return filesInDirectory;
            }
        } else {
            System.out.println("No Directory under this Path " + folderPath.getAbsolutePath());
        }
        return null;
    }
}


