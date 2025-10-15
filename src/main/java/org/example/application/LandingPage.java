package org.example.application;

import org.example.application.components.MenuBarClass;
import org.example.dataReader.Reader;
import org.example.dataReader.Writer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class LandingPage extends JFrame implements ActionListener {
    Reader reader = new Reader();
    Writer writer;

    JButton createPageButton = new JButton("Create Page");
    JPanel pagePanel;
    JButton PageButton;


    public LandingPage() {
        this.setTitle("untitled Learn App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(800, 500);

        MenuBarClass MenuBarClass = new MenuBarClass();
        this.setJMenuBar(MenuBarClass.menuBarFromClass(this, null));


        JPanel topPanel = new JPanel();
        pagePanel = new JPanel(new GridLayout(0, 5, 10, 10));


        createPageButton.setFocusable(false);


        topPanel.add(createPageButton, BorderLayout.EAST);



        Load_Reload_PagePanel();


        JScrollPane scrollPane = new JScrollPane(pagePanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        createPageButton.setPreferredSize(new Dimension(500, 80));

        createPageButton.addActionListener(this);


        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(topPanel, BorderLayout.NORTH);

        this.setVisible(true);
    }


    public File Load_Reload_PagePanel() {
        pagePanel.removeAll();

        for (File file : reader.getAllFilesInPageDirectory()) {
            String simpleFileName = file.getName().replace(".xml", "");

            PageButton = new JButton(simpleFileName);
            PageButton.addActionListener(e -> {
                PageView pageView = new PageView(file.getAbsoluteFile());
                pageView.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

                pageView.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
int choice = pageView.checkIfAlreadySaved();

                        if (choice == JOptionPane.YES_OPTION || choice == JOptionPane.NO_OPTION) pageView.dispose();

                    }
                });

            });


            JPanel buttonPanel = new JPanel(new BorderLayout());
            PageButton.setPreferredSize(new Dimension(100, 300));
            JButton removePageButton = new JButton("Remove Page");
            removePageButton.setPreferredSize(new Dimension(100, 50));
            removePageButton.setFocusable(false);


            buttonPanel.add(removePageButton, BorderLayout.NORTH);
            buttonPanel.add(PageButton, BorderLayout.CENTER);

            pagePanel.add(buttonPanel);


            removePageButton.addActionListener(e -> {
                writer = new Writer();

                int confirmRemove_OfPage = JOptionPane.showConfirmDialog(this, "do you really want to delete this page? ", "remove page?: " + simpleFileName,  JOptionPane.YES_NO_OPTION);

                if (confirmRemove_OfPage == JOptionPane.YES_OPTION) {
                    writer.writeToFile_removePage(file);
                    Load_Reload_PagePanel();
                }
            });

        }
        pagePanel.revalidate();
        pagePanel.repaint();

        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createPageButton) {
            String input = JOptionPane.showInputDialog(this, "please enter a name for the page: ", "enter page name", JOptionPane.QUESTION_MESSAGE);

            if (input != null) {
                if (input.isBlank()) JOptionPane.showMessageDialog(this, "cant create file without name", "warning", JOptionPane.WARNING_MESSAGE);
                else {
                    writer = new Writer(input);

                    Load_Reload_PagePanel();
                }
            }
        }


    }
}