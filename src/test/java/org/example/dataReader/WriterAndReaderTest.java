package org.example.dataReader;

import org.example.dataReader.ObjectClasses.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriterAndReaderTest {
    Writer writer;
    Reader reader = new Reader();
    List<File> filesInDirectory = new ArrayList<File>();
    File file;

    Page page = new Page();

    String localAppData = reader.localAppData;
    String fullPathToFolder = reader.file_folderUnderFullPath.getAbsolutePath();


    @BeforeEach
//this happens before each test
//this is a pre-testing cleanup test
    void setUp() {
        writer = new Writer("title");


        for (File file : readAllFilesInDirectory()) {
            file.delete(); //delete all files
        }
    }

    static List<String> testWeirdNames() {
        //this is the list of things that will be tested
        return List.of(
                //names with symbols
                "test", //""smoke test""
                "hello world",
                "hello-world",
                "hello_world",
                "hello#world",

                "öööäääüüü",
                "fihwerhstfuierghserihghksehkhaospfhseorgpbesribgfsepjfhsweshfaiefosberdhgsebrbfs",

                //symbols
                "test",
                "####",
                "...p",
                "-----------------------------------------------------------------------",
                "_____"
        );
    }

    @ParameterizedTest(name = "File name test: \"{0}\" ")
    @MethodSource("testWeirdNames")
    void createFilesWithWeirdNames(String fileName) {
        //run duplicate times if needed
        if (fileName.equals("test")) {
            for (int i = 0; i < 20; i++) {
                testNames(fileName);
            }
        } else {
            testNames(fileName);
        }
    }

    void testNames(String fileName) {
        writer.writeToFile_CreateFile(fileName, null);
        File file = new File(fullPathToFolder, fileName + ".xml");

        assertTrue(file.exists(), "File doesn't exist: " + file.getAbsolutePath());
    }


    // Example stub – replace with your real implementation
    private File[] readAllFilesInDirectory() {
        return new File(fullPathToFolder).listFiles();
    }


    @Test
    void checkForIsValideNameCheck() {
        writer = new Writer("**+++");
        file = new File(fullPathToFolder, "**+++.txt");
        assertFalse(file.exists(), "The valideFileName function should get it and disallow its existences");

        file = new File(fullPathToFolder, "untitled.xml");
        assertTrue(file.exists(), "the function will instead create a untitled file");
    }

}




