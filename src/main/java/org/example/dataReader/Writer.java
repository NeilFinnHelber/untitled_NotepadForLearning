package org.example.dataReader;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.example.dataReader.ObjectClasses.Header;
import org.example.dataReader.ObjectClasses.Page;
import org.example.dataReader.ObjectClasses.Subheader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Writer {
    String localAppData = System.getenv("LOCALAPPDATA"); //get users %LocalAppData% path
    File folderPath = new File(localAppData, "untitled-learnApp");
    File file_fromFilePath;
    Reader reader = new Reader();

    ObjectMapper mapper = new XmlMapper();

    Page page;
    Header header;
    Subheader subheader;

    public Writer() {
    }

    public Writer(String pageFileName) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        writeToFile_CreateFile(pageFileName, null);
    }


    private void check_and_create_AppComponents() {
        if (!folderPath.exists()) folderPath.mkdirs();  //create the folder for the AppsData if it doesn't exist

    }

    private File validateFileName(String fileName, boolean creatingNewFile) {
        if (fileName == null || fileName.isBlank()) fileName = "untitled";
        if (fileName.matches("[A-Za-z0-9_ .#A-Z-öäü]+")) {

            file_fromFilePath = new File(folderPath, fileName + ".xml");

            if (file_fromFilePath.exists() && creatingNewFile) {

                int count = 1;
                while (file_fromFilePath.exists()) {
                    String tempFileName = fileName + "(" + count + ")" + ".xml";

                    file_fromFilePath = new File(folderPath, tempFileName);
                    count++;
                }
                System.out.println(file_fromFilePath.getAbsolutePath());

            }

        } else {
            throw new RuntimeException("Invalid file name");
        }

        if (!file_fromFilePath.exists() && !creatingNewFile) throw new RuntimeException("File not found");
        return file_fromFilePath;
    }

    //this writes the title for the first creation of the file
    public void writeToFile_CreateFile(String pageFileName, List<Header> headers) {

        file_fromFilePath = validateFileName(pageFileName, true);


        check_and_create_AppComponents(); //for now just creates folder


        try {
            file_fromFilePath.createNewFile();

            if (file_fromFilePath.exists()) {
                page = new Page();

                page.setTitle(pageFileName);
                page.setText("write text for page here");
                page.setHeaders(headers);


                mapper.writeValue(file_fromFilePath, page);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    //this updates parts of the page
    public void writeToFile_updatePage(String pageFileName, String pageTitle, String pageText) {

        file_fromFilePath = validateFileName(pageFileName, false);

        page = new Page();
        page = reader.readDataFromXMLFile(file_fromFilePath);

        if (pageTitle != null) page.setTitle(pageTitle);
        if (pageText != null) page.setText(pageText);


        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file_fromFilePath, page);

        } catch (Exception e) {
            throw new RuntimeException(e);
            //unexpected EOF might just mean that the xml file is empty
        }
    }


    public void writeToFile_createHeader(String pageFileName, String headerTitle) {

        file_fromFilePath = validateFileName(pageFileName, false);

        page = new Page();
        page = reader.readDataFromXMLFile(file_fromFilePath);

        List<Header> headers = page.getHeaders(); //add the old headers to the array

        headers.add(new Header(headerTitle)); //add the new header
        page.setHeaders(headers); //update the page component

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file_fromFilePath, page);
            //actually write it

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void writeToFile_updateHeader(String pageFileName, int headerID, String headerTitle, String headerText, ArrayList<String> headerSearchTerms) {
        file_fromFilePath = validateFileName(pageFileName, false);

        page = new Page();
        page = reader.readDataFromXMLFile(file_fromFilePath);

        List<Header> headers = page.getHeaders(); //set headers

        if (headers.get(headerID) != null) {

            if (headerTitle != null) headers.get(headerID).setTitle(headerTitle);
            if (headerText != null) headers.get(headerID).setText(headerText);
            if (headerSearchTerms != null) headers.get(headerID).setSearchTerm(headerSearchTerms);

            page.setHeaders(headers);

            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(file_fromFilePath, page);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        } else { //if (however that were to happen) the header doesn't exist, create a new one in its image
            writeToFile_createHeader(pageFileName, headerTitle);
        }
    }

    public void writeToFile_createSubheader(String pageFileName, int headerID, String subheaderTitle) {
        file_fromFilePath = validateFileName(pageFileName, false);

        page = new Page();
        page = reader.readDataFromXMLFile(file_fromFilePath);
        List<Header> headers = page.getHeaders();

        if (headers.get(headerID) != null) {
            List<Subheader> subheaders = headers.get(headerID).getSubheaders(); //set the subheaders of the current header

            subheaders.add(new Subheader(subheaderTitle)); //add new subheaders

            page.setHeaders(headers); //update the headers with the new subheader attached

            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(file_fromFilePath, page);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writeToFile_createSubheader_usingHeader(String pageFileName, Header thisHeader, String subheaderTitle) {
        file_fromFilePath = validateFileName(pageFileName, false);

        page = reader.readDataFromXMLFile(file_fromFilePath);
        List<Header> headers = page.getHeaders();

        if (thisHeader != null) {
            for (Header header : headers) {
                if (header.getTitle().equals(thisHeader.getTitle())) {
                    header.getSubheaders().add(new Subheader(subheaderTitle));
                    break;
                }
            }

            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(file_fromFilePath, page);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void writeToFile_updateSubheader(String pageFileName, int headerID, int subheaderID, String subheaderTitle, String subheaderText, ArrayList<String> subheaderSearchTerms) {
        file_fromFilePath = validateFileName(pageFileName, false);

        page = new Page();
        page = reader.readDataFromXMLFile(file_fromFilePath);

        List<Header> headers = page.getHeaders();
        List<Subheader> subheaders = headers.get(headerID).getSubheaders();

        if (subheaderTitle != null) subheaders.get(subheaderID).setTitle(subheaderTitle);
        if (subheaderText != null) subheaders.get(subheaderID).setText(subheaderText);
        if (subheaderSearchTerms != null) subheaders.get(subheaderID).setSearchTerm(subheaderSearchTerms);

        page.setHeaders(headers);


        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file_fromFilePath, page);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        Writer W = new Writer();

        ArrayList<Header> temp = new ArrayList<>();
        temp.add(new Header("headerOne"));
        temp.add(new Header("headerTwo"));
        temp.add(new Header("headerThree"));
        temp.add(new Header("headerFour"));

        ArrayList<String> tempSearchTerms = new ArrayList<>();
        tempSearchTerms.add("tag1");
        tempSearchTerms.add("tag2");
        tempSearchTerms.add("tag3");
        tempSearchTerms.add("tag4");


        //File file = W.validateFileName("");
        //System.out.println(file.getAbsolutePath());

        //W.writeToFile_CreateFile("title", temp);
        //W.writeToFile_updateFile("title.xml", "title", "this is an example text", temp);

        //W.writeToFile_updatePage("title", "untitled honk honk", "hello goose");

        //W.writeToFile_createHeader("title", "welcome to the world");

        //W.writeToFile_updateHeader("title", 0, "new header", "", tempSearchTerms);

        //W.writeToFile_createSubheader("title", 0, "a tasty pizza the action");

        //W.writeToFile_updateSubheader("title", 0, 0, "new subheader title", "",tempSearchTerms);
    }
}
