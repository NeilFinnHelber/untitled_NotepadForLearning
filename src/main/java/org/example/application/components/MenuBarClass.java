package org.example.application.components;

import org.example.application.LandingPage;
import org.example.application.PageView;
import org.example.dataReader.ObjectClasses.Header;
import org.example.dataReader.ObjectClasses.Subheader;
import org.example.dataReader.Reader;
import org.example.dataReader.Writer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MenuBarClass {
    JMenuBar menuBar = new JMenuBar();

    // top-level items
    JMenu fileMenu = new JMenu("File");
    JMenu helpMenu = new JMenu("Help");


    // low-level items
    JMenu newItemNester = new JMenu("New"); //nested new menu
    JMenu aboutItemNester = new JMenu("About");
    JMenu removeNester = new JMenu("Remove");
    JMenuItem savePageToXML = new JMenuItem("Save");
    JMenuItem open = new JMenuItem("Open");
    JMenuItem exit = new JMenuItem("Exit");
    JMenuItem closePage = new JMenuItem("Close Page");
    JMenuItem keyBoardShortCuts = new JMenuItem("Key Board ShortCuts");

    //nested items
    JMenuItem newPage = new JMenuItem("New Page");
    JMenuItem newHeader = new JMenuItem("New Header");
    JMenuItem newSubheader = new JMenuItem("New Subheader");
    JMenuItem removePage = new JMenuItem("Remove Page");
    JMenuItem removeHeader = new JMenuItem("Remove Header");
    JMenuItem removeSubheader = new JMenuItem("Remove Subheader");
    JMenuItem creatorsGithub =  new JMenuItem("Creators Github");
    JMenuItem appGithub =  new JMenuItem("Github of this App");
    JMenuItem website =  new JMenuItem("Website");


    Writer writer = new Writer();
    Reader reader = new Reader();
    String pageFileName = "";


    public JMenuBar menuBarFromClass(LandingPage landingPage, PageView pageView) {
        if (pageView != null) {
            pageFileName = pageView.file_fromFilePath.getName().substring(0, pageView.file_fromFilePath.getName().length() - 4);
        }

        //top-level items
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);


        //low-level items
        fileMenu.add(newItemNester);
        fileMenu.add(open);
        fileMenu.add(removeNester);
        fileMenu.add(savePageToXML);

        fileMenu.addSeparator();

        fileMenu.add(closePage);
        fileMenu.add(exit);

        //----
        helpMenu.add(keyBoardShortCuts);

        helpMenu.addSeparator();
        helpMenu.add(aboutItemNester);


        //nesting
        newItemNester.add(newPage);
        newItemNester.add(newHeader);
        newItemNester.add(newSubheader);
        removeNester.add(removePage);
        removeNester.add(removeHeader);
        removeNester.add(removeSubheader);
        aboutItemNester.add(appGithub);
        aboutItemNester.add(creatorsGithub);
        aboutItemNester.add(website);


        //ActionListeners
        //-- new nesting --
        newPage.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(landingPage, "please enter a name for the page: ", "enter page name", JOptionPane.QUESTION_MESSAGE);

            if (input != null) {
                if (input.isBlank())
                    JOptionPane.showMessageDialog(landingPage, "cant create file without name", "warning", JOptionPane.WARNING_MESSAGE);
                else {
                    writer = new Writer(input);

                    landingPage.Load_Reload_PagePanel();
                }
            }
        });
        newHeader.addActionListener(e -> {
            pageView.createHeader();
        });
        newSubheader.addActionListener(e -> {
            Header header = selectAHeader(pageView);

            if (header != null) {


                String input = JOptionPane.showInputDialog("Enter Subheader Title");

                if (input != null) {
                    if (input.isBlank()) {
                        JOptionPane.showMessageDialog(null, "wont create subheader without title", "warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        pageView.saveDataToPage();

                        System.out.println("create subheader for header " + header.getTitle());
                        System.out.println(header);
                        writer.writeToFile_createSubheader_usingHeader(pageFileName, header, input);

                        pageView.load_reload_mainPanel(pageView.file_fromFilePath);
                        pageView.load_reload_ExpandablePanel(pageView.file_fromFilePath);
                    }

                }
            }
        });

        //-- remove nesting --
        removePage.addActionListener(e -> {
            ArrayList<String> dropdownOptions = new ArrayList<>();
            //System.out.println(pageView.headers.size());
            for (File file : reader.getAllFilesInPageDirectory()) {
                dropdownOptions.add(file.getName());
            }
            String[] optionsArray = dropdownOptions.toArray(new String[0]);

            String selected = (String) JOptionPane.showInputDialog(
                    null,
                    "select a page",
                    "select page",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    optionsArray,
                    optionsArray[0]
            );

            if (selected != null) {
                int confirmRemove_OfPage = JOptionPane.showConfirmDialog(landingPage, "do you really want to delete this page? ", "remove page?: " + selected.substring(0, selected.length() -4),  JOptionPane.YES_NO_OPTION);

                File file = new File(reader.file_folderUnderFullPath, selected);
                if (confirmRemove_OfPage == JOptionPane.YES_OPTION) {
                    writer.writeToFile_removePage(file);
                    landingPage.Load_Reload_PagePanel();
                }

            }
        });
        removeHeader.addActionListener(e -> {
            Header header = selectAHeader(pageView);
            if (header != null) {
                int input = JOptionPane.showConfirmDialog(pageView, "do you want to remove the header from the page?", "removes header: " + header.getTitle(), JOptionPane.YES_NO_OPTION);
                if (input == JOptionPane.YES_OPTION) {
                    writer.writeToFile_removeHeader(pageFileName, pageView.headers.indexOf(header));

                    pageView.load_reload_mainPanel(pageView.file_fromFilePath);
                    pageView.load_reload_ExpandablePanel(pageView.file_fromFilePath);

                    JOptionPane.showMessageDialog(null, "Header removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        });
        removeSubheader.addActionListener(e -> {
            Header header = selectAHeader(pageView);

            if (header != null) {
                Subheader subheader = selectASubheader(pageView, header);

                if (subheader != null) {

                    int input = JOptionPane.showConfirmDialog(pageView, "do you want to remove the subheader from the page?", "removes subheader: " + subheader.getTitle(), JOptionPane.YES_NO_OPTION);
                if (input == JOptionPane.YES_OPTION) {
                    writer.writeToFile_removeSubheader(pageFileName,
                            pageView.headers.indexOf(header),
                            header.getSubheaders().indexOf(subheader)
                    );

                    pageView.load_reload_mainPanel(pageView.file_fromFilePath);
                    pageView.load_reload_ExpandablePanel(pageView.file_fromFilePath);

                    JOptionPane.showMessageDialog(null, "Subheader removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                }
            }
        });


        //save
        savePageToXML.addActionListener(e -> {
            pageView.saveDataToPage();
            JOptionPane.showMessageDialog(null, "info", "file has been saved!", JOptionPane.INFORMATION_MESSAGE);
        });


        //simple texts
        open.addActionListener(e -> {
           JOptionPane.showMessageDialog(null,  "backup is not yet available", "available soon", JOptionPane.PLAIN_MESSAGE);
        });
        keyBoardShortCuts.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "ALT + F/H/...  using the first letter of menu bars words will call this part" + "\n" +
                    "STRG/CTRL + S    to save the Pages content",
                    "Shortcuts",
                    JOptionPane.PLAIN_MESSAGE);
        });
        creatorsGithub.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/NeilFinnHelber"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });
        appGithub.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/NeilFinnHelber/untitled_NotepadForLearning"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });
        website.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.wolf0fdev.me/"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });



        //-- exit & close --
        exit.addActionListener(e -> {
            System.exit(0);
        });
        closePage.addActionListener(e -> {
            pageView.dispose();
        });


        //set the mnemonics (first letter highlighted)
        fileMenu.setMnemonic(KeyEvent.VK_F);
        helpMenu.setMnemonic(KeyEvent.VK_H);
        aboutItemNester.setMnemonic(KeyEvent.VK_A);
        newItemNester.setMnemonic(KeyEvent.VK_N);
        open.setMnemonic(KeyEvent.VK_O);
        exit.setMnemonic(KeyEvent.VK_X);
        closePage.setMnemonic(KeyEvent.VK_C);
        removeNester.setMnemonic(KeyEvent.VK_R);


        //enabling
        boolean hasPageView = pageView != null; //true if page has been given

        savePageToXML.setEnabled(hasPageView);
        newHeader.setEnabled(hasPageView);
        newSubheader.setEnabled(hasPageView);
        removeHeader.setEnabled(hasPageView);
        removeSubheader.setEnabled(hasPageView);
        closePage.setEnabled(hasPageView);

        removePage.setEnabled(!hasPageView);
        newPage.setEnabled(!hasPageView);
        open.setEnabled(!hasPageView);

        return menuBar;
    }


    public Header selectAHeader(PageView pageView) {
        ArrayList<String> dropdownOptions = new ArrayList<>();
        if (pageView.headers.isEmpty()) {
            JOptionPane.showMessageDialog(null, "no headers found in page", "no headers in page", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        for (Header header : pageView.headers) {
            dropdownOptions.add(header.getTitle() + " ID:" + pageView.headers.indexOf(header));
        }
        String[] optionsArray = dropdownOptions.toArray(new String[0]);

        String selected = (String) JOptionPane.showInputDialog(
                null,
                "select a header for the subheader",
                "select header",
                JOptionPane.QUESTION_MESSAGE,
                null,
                optionsArray,
                optionsArray[0]
        );

        if (selected != null) {
            int headerIndex = Integer.parseInt(selected.split(":")[1]);
            System.out.println(headerIndex);
            Header header = pageView.headers.get(headerIndex);
            return header;
        }
        return null;
    }



    public Subheader selectASubheader(PageView pageView, Header header) {
        ArrayList<String> dropdownOptions = new ArrayList<>();

        if (header.getSubheaders().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "no subheaders found in header", "no subheaders", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        for (Subheader subheader : header.getSubheaders()) {
            dropdownOptions.add(subheader.getTitle() + " ID:" + header.getSubheaders().indexOf(subheader));
        }
        String[] optionsArray = dropdownOptions.toArray(new String[0]);

        String selected = (String) JOptionPane.showInputDialog(
                null,
                "select a subheader",
                "select subheader",
                JOptionPane.QUESTION_MESSAGE,
                null,
                optionsArray,
                optionsArray[0]
        );

        if (selected != null) {
            int subheaderIndex = Integer.parseInt(selected.split(":")[1]);
            System.out.println(subheaderIndex);
            Subheader subheader = header.getSubheaders().get(subheaderIndex);
            return subheader;
        }
        return null;
    }
}
