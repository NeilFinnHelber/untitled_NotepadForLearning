package org.example.dataReader;

import org.example.dataReader.ObjectClasses.Header;
import org.example.dataReader.ObjectClasses.Page;
import org.example.dataReader.ObjectClasses.Subheader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Writer {
    String localAppData = System.getenv("LOCALAPPDATA"); //get users %LocalAppData% path
    File folderPath = new File(localAppData, "untitled-learnApp");
    File file_fromFilePath;

    ObjectMapper mapper = new XmlMapper();

    Page page;
    Header header;
    Subheader subheader;


    public Writer(String pageFileName) {

        writeToFile_firstTime(pageFileName);
    }


    private void check_and_create_AppComponents() {
        if (!folderPath.exists()) folderPath.mkdirs();  //create the folder for the AppsData if it doesn't exist

    }
    private String validateFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) return "untitled";
        if (fileName.matches("[A-Za-z0-9_ .A-Z-öäü #]+"))  {
            return fileName;
        }

        return "untitled";
    }

    //this writes the title for the first creation of the file
    public void writeToFile_firstTime(String pageFileName) {

        pageFileName = validateFileName(pageFileName);

        String title = pageFileName;
        check_and_create_AppComponents();

        try {
            String pageFileName_with_extension = pageFileName + ".xml";
            file_fromFilePath = new File(folderPath, pageFileName_with_extension);

            if (!file_fromFilePath.exists())
                file_fromFilePath.createNewFile(); //creates the .xml file for the page with the given name
            else {
                int count = 1;
                while (file_fromFilePath.exists()) {
                    pageFileName_with_extension = pageFileName + "(" + count + ")" + ".xml";
                    file_fromFilePath = new File(folderPath, pageFileName_with_extension);
                    count++;
                }
                System.out.println("file already exists");

                file_fromFilePath = new File(folderPath, pageFileName_with_extension);
                file_fromFilePath.createNewFile();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        file_fromFilePath = new File(folderPath, pageFileName);

        if (file_fromFilePath.exists()) {
            page = new Page();

            //write the given title and standard xml components
            page.setText("write text for page here");


        }
    }

    //here text of a file can be updated once the file already exists
    public void writeToFile_updateFile(String pageFileName, String title, String text, ArrayList<Header> header) {
        file_fromFilePath = new File(folderPath, pageFileName);
        if (file_fromFilePath.exists()) {
            page = new Page();
            page.setTitle(title);
            page.setText(text);
            page.setHeaders(header);

            try {
                mapper.writeValue(file_fromFilePath, page);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) {
        Writer W = new Writer("title");

        ArrayList<Header> temp = new ArrayList<>();
        temp.add(new Header("headerOne"));
        temp.add(new Header("headerTwo"));
        temp.add(new Header("headerThree"));
        temp.add(new Header("headerFour"));


        W.writeToFile_updateFile("title.xml", "title", "this is an example text", temp);
    }
}
