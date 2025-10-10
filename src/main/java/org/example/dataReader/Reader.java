package org.example.dataReader;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.dataReader.ObjectClasses.Header;
import org.example.dataReader.ObjectClasses.Page;
import org.example.dataReader.ObjectClasses.Subheader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    public String localAppData = System.getenv("LOCALAPPDATA"); //get users %LocalAppData% path
    public File file_folderUnderFullPath = new File(localAppData, "untitled-learnApp");
    List<File> filesInDirectory = new ArrayList<>();


    XmlMapper mapper = new XmlMapper();
    Page page;


    public Reader() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    public List<File> getAllFilesInPageDirectory() {
        filesInDirectory.clear();
        if (file_folderUnderFullPath.exists() && file_folderUnderFullPath.isDirectory()) {
            File[] files = file_folderUnderFullPath.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        filesInDirectory.add(file);
                    }
                }
                return filesInDirectory;
            }
        } else {
            System.out.println("No Directory under this Path " + file_folderUnderFullPath.getAbsolutePath());
        }
        return null;
    }


    Page readDataFromXMLFile(File file_fromFilePath) {

        try {
            if (file_fromFilePath.exists()) {
                page = mapper.readValue(file_fromFilePath, Page.class);
                return page;
            }
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Page();
    }

    public List<Header> getAllHeadersFromPage(File file_fromFilePath) {
        if (!file_fromFilePath.exists()) throw new RuntimeException("File does not exist");

        page = readDataFromXMLFile(file_fromFilePath);

        return page.getHeaders();
    }

    public List<Subheader> getAllSubheadersFromHeader(File file_fromFilePath, int headerID) {
        if (!file_fromFilePath.exists()) throw new RuntimeException("File does not exist");

        page = readDataFromXMLFile(file_fromFilePath);
        List<Header> headers = getAllHeadersFromPage(file_fromFilePath);
        if (headers.get(headerID) == null) throw new RuntimeException("Header with ID " + headerID + " doesnt exists");


        return headers.get(headerID).getSubheaders();
    }


    public static void main(String[] args) {

    }
}


