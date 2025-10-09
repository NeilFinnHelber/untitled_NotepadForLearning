package org.example.dataReader;

import org.example.dataReader.ObjectClasses.Header;
import org.example.dataReader.ObjectClasses.Page;
import org.example.dataReader.ObjectClasses.Subheader;
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


    ArrayList<String> tempSearchTerms = new ArrayList<>();
    ArrayList<Header> temp = new ArrayList<>();


    String pageTitle = "has he penned this page before";
    String pageText = "drowning under weight of legacy";
    String headerTitle = "if a single person falls out of line we all do";
    String headerText = "morning gathering or evening prayer, we battle for a season fair";
    String subheaderTitle = "nothing certain but death and taxes";
    String subheaderText = "ashes to ashes, nine to five, the light in the tunnel has been privatized";


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
    void setupCreatingFile_forEditTests() {
        writer = new Writer();

        temp.add(new Header("headerOne"));
        temp.add(new Header("headerTwo"));
        temp.add(new Header("headerThree"));
        temp.add(new Header("headerFour"));


        tempSearchTerms.add("tag1");
        tempSearchTerms.add("tag2");
        tempSearchTerms.add("tag3");
        tempSearchTerms.add("tag4");

        writer.writeToFile_CreateFile("testingEdits", null);
        file = new File(fullPathToFolder, "testingEdits.xml");

        assertTrue(file.exists(), "check that the file exists");

    }


    @Test
    void updatePageComponentsTest() {
        setupCreatingFile_forEditTests();
        writer = new Writer();
        reader = new Reader();

        file = new File(fullPathToFolder, "testingEdits.xml");

        writer.writeToFile_updatePage("testingEdits", pageTitle, pageText);

        page = reader.readDataFromXMLFile(file);
        assertEquals(page.getTitle(), pageTitle, "The title of the PageFile must equal the updated pageTitle");
        assertEquals(page.getText(), pageText, "The pageText of the PageFile must equal the updated pageText");
    }

    @Test
    void createHeaderTest() {
        setupCreatingFile_forEditTests();
        writer = new Writer();
        reader = new Reader();
        file = new File(fullPathToFolder, "testingEdits.xml");

        writer.writeToFile_createHeader("testingEdits", headerTitle);
        page = reader.readDataFromXMLFile(file); //import to get the data after we created the new header

        List<Header> headers = page.getHeaders();

        assertEquals(headers.getLast().getTitle(), headerTitle, "The title of the Header on the position 0 must equal the updated pageTitle");
        //this just add header, regardless if we test it once or a few times, the last header is always the one we want to check
    }

    @Test
    void updateHeaderTest() {
        setupCreatingFile_forEditTests();
        createHeaderTest();

        writer = new Writer();
        reader = new Reader();
        file = new File(fullPathToFolder, "testingEdits.xml");

        writer.writeToFile_updateHeader("testingEdits", 0, headerTitle, headerText, tempSearchTerms);
        page = reader.readDataFromXMLFile(file);
        List<Header> headers = page.getHeaders();

        assertEquals(headers.getFirst().getTitle(), headerTitle, "the headers title should be the set title");
        assertEquals(headers.getFirst().getText(), headerText, "the headers text should be the set text");
        assertEquals(headers.getFirst().getSearchTerm(), tempSearchTerms, "the headers searchTerm should be the set searchTerm");
    }

    @Test
    void createSubheaderTest() {
        setupCreatingFile_forEditTests();
        createHeaderTest();

        writer = new Writer();
        reader = new Reader();
        file = new File(fullPathToFolder, "testingEdits.xml");

        writer.writeToFile_createSubheader("testingEdits", 0, subheaderTitle);

        page = reader.readDataFromXMLFile(file);
        List<Header> headers = page.getHeaders();
        List<Subheader> subheaders = headers.getFirst().getSubheaders();

        assertEquals(subheaders.getLast().getTitle(), subheaderTitle, "The subheaders title should be the set title");
    }

    @Test
    void updateSubheaderTest() {
        setupCreatingFile_forEditTests();
        createHeaderTest();
        createSubheaderTest();

        writer = new Writer();
        reader = new Reader();
        file = new File(fullPathToFolder, "testingEdits.xml");

        writer.writeToFile_updateSubheader("testingEdits", 0, 0, subheaderTitle, subheaderText, tempSearchTerms);

        page = reader.readDataFromXMLFile(file);
        List<Header> headers = page.getHeaders();
        List<Subheader> subheaders = headers.getFirst().getSubheaders();

        assertEquals(subheaders.getFirst().getTitle(), subheaderTitle, "The subheaders title should be the set title");
        assertEquals(subheaders.getFirst().getText(), subheaderText, "The subheaders text should be the set text");
        assertEquals(subheaders.getFirst().getSearchTerm(), tempSearchTerms, "The subheaders searchTerm should be the set searchTerm");
    }


    @Test
    void testAllEditsTogheter() {
        setupCreatingFile_forEditTests();
        page = reader.readDataFromXMLFile(file);
        assertEquals("testingEdits", page.getTitle(), "the standard pageTitle is the files name");


        updatePageComponentsTest();
        assertEquals(pageTitle, page.getTitle(), "The title of the PageFile must equal the updated pageTitle");
        assertEquals(pageText, page.getText(), "The pageText of the PageFile must equal the updated pageText");


        createHeaderTest();
        page = reader.readDataFromXMLFile(file);
        List<Header> headers = page.getHeaders();

        assertEquals(pageTitle, page.getTitle(), "The title of the PageFile must equal the updated pageTitle");
        assertEquals(pageText, page.getText(), "The pageText of the PageFile must equal the updated pageText");
        assertEquals(headerTitle, headers.getLast().getTitle(), "The headers title should be the set title");


        updateHeaderTest();
        page = reader.readDataFromXMLFile(file);
        headers = page.getHeaders();

        assertEquals(pageTitle, page.getTitle(), "The title of the PageFile must equal the updated pageTitle");
        assertEquals(pageText, page.getText(), "The pageText of the PageFile must equal the updated pageText");
        assertEquals(headerTitle, headers.getFirst().getTitle(), "The headers title should be the set title");
        assertEquals(headerText, headers.getFirst().getText(), "The headers text should be the set text");
        assertEquals(tempSearchTerms, headers.getFirst().getSearchTerm(), "The headers searchTerm should be the set searchTerm");


        createSubheaderTest();
        page = reader.readDataFromXMLFile(file);
        headers = page.getHeaders();
        List<Subheader> subheaders = headers.getFirst().getSubheaders();

        assertEquals(pageTitle, page.getTitle(), "The title of the PageFile must equal the updated pageTitle");
        assertEquals(pageText, page.getText(), "The pageText of the PageFile must equal the updated pageText");
        assertEquals(headerTitle, headers.getFirst().getTitle(), "The headers title should be the set title");
        assertEquals(headerText, headers.getFirst().getText(), "The headers text should be the set text");
        assertEquals(subheaderTitle, subheaders.getFirst().getTitle(), "The subheaders title should be the set title");


        updateSubheaderTest();
        page = reader.readDataFromXMLFile(file);
        headers = page.getHeaders();
        subheaders = headers.getFirst().getSubheaders();

        assertEquals(pageTitle, page.getTitle(), "The title of the PageFile must equal the updated pageTitle");
        assertEquals(pageText, page.getText(), "The pageText of the PageFile must equal the updated pageText");
        assertEquals(headerTitle, headers.getFirst().getTitle(), "The headers title should be the set title");
        assertEquals(headerText, headers.getFirst().getText(), "The headers text should be the set text");
        assertEquals(subheaderTitle, subheaders.getFirst().getTitle(), "The subheaders title should be the set title");
        assertEquals(subheaderText, subheaders.getFirst().getText(), "The subheaders text should be the set text");
        assertEquals(tempSearchTerms, subheaders.getFirst().getSearchTerm(), "The subheaders searchTerm should be the set searchTerm");
    }
}




