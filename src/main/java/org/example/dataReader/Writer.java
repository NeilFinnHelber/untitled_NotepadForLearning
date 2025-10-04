package org.example.dataReader;

import org.example.dataReader.ObjectClasses.Header;
import org.example.dataReader.ObjectClasses.Page;
import org.example.dataReader.ObjectClasses.Subheader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Writer {
String localAppData = System.getenv("LOCALAPPDATA"); //get users %LocalAppData% path
File folderPath = new File(localAppData, "untitled-learnApp");
File file_fromFilePath;

ObjectMapper mapper = new XmlMapper();

Page page;
Header header;
Subheader subheader;


    public Writer(String pageFileName, String title) {

        writePageFolder(pageFileName, title);
    }


    private void writePageFolder(String pageFileName, String title) {

    try {
        if (pageFileName == null || pageFileName.isBlank()) {
            pageFileName = "untitled";
        }

        String pageFileName_with_extension = pageFileName + ".xml";

        file_fromFilePath = new File(folderPath, pageFileName_with_extension);
        if (!folderPath.exists()) folderPath.mkdirs();  //create the folder for the AppsData if it doesn't exist

        if (!file_fromFilePath.exists()) file_fromFilePath.createNewFile(); //creates the .xml file for the page with the given name
        else {
            int count = 1;
            while (file_fromFilePath.exists()) {
                pageFileName_with_extension = pageFileName + "(" + count + ")" + ".xml" ;
                file_fromFilePath = new File(folderPath,  pageFileName_with_extension);
                count++;
            }
            System.out.println("file already exists");

            file_fromFilePath = new File(folderPath, pageFileName_with_extension);
            file_fromFilePath.createNewFile();
        }

        writeToFile_firstTime(pageFileName_with_extension, title);

    } catch (Exception e){
        e.printStackTrace();
        System.out.println("error in writer");
    }
}

//this writes the title for the first creation of the file
private void writeToFile_firstTime(String pageFileName, String title) {
        file_fromFilePath = new File(folderPath, pageFileName);
        if (file_fromFilePath.exists()) {
            page = new Page();
            if (title == null || title.isBlank()) title = "untitled";
            //write the given title and standard xml components
            page.setTitle(title);
            page.setText("write text here");
            page.setHeaders(null);


            //write the title in the given file
            try {
                mapper.writeValue(file_fromFilePath, page);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}

//here text of a file can be updated once the file already exists
public void writeToFile_updateFile(String pageFileName, String title, String text, Header header) {
        file_fromFilePath = new File(folderPath, pageFileName);
        if (file_fromFilePath.exists()) {
            page = new Page();
            page.setTitle(title);
            page.setText(text);
            page.setHeaders((List<Header>) header);

            try {
                mapper.writeValue(file_fromFilePath, page);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
}



    public static void main(String[] args){
        Writer W = new Writer("title", "title");
        W.writeToFile_updateFile("title.xml", "title", "this is an example text", W.header);
    }
}
