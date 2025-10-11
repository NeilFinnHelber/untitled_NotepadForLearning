package org.example.application;

import org.example.dataReader.Reader;
import org.example.dataReader.Writer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LandingPage extends JFrame implements ActionListener {
    Reader reader = new Reader();
    Writer writer;
    JButton openBackupButton = new JButton("Open Backup");
    JButton createPageButton = new JButton("Create Page");
    JPanel pagePanel;

    public LandingPage() {
        this.setTitle("Title");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(800, 500);

        JPanel topPanel = new JPanel();
        pagePanel = new JPanel(new GridLayout(0, 5, 10, 10));


        createPageButton.setFocusable(false);

        openBackupButton.setFocusable(false);
        topPanel.add(createPageButton, BorderLayout.EAST);
        topPanel.add(openBackupButton, BorderLayout.WEST);


        Load_Reload_PagePanel();


        JScrollPane scrollPane = new JScrollPane(pagePanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        createPageButton.setPreferredSize(new Dimension(500, 80));
        openBackupButton.setPreferredSize(new Dimension(200, 80));

        createPageButton.addActionListener(this);
        openBackupButton.addActionListener(this);

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(topPanel, BorderLayout.NORTH);

        this.setVisible(true);
    }


    public File Load_Reload_PagePanel() {
        pagePanel.removeAll();

        for (File file : reader.getAllFilesInPageDirectory()) {
            JButton PageButton = new JButton(file.getName().replace(".xml", ""));

            PageButton.setPreferredSize(new Dimension(100, 350));
            pagePanel.add(PageButton);

        }
        pagePanel.revalidate();
        pagePanel.repaint();

        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createPageButton) {
            writer = new Writer("title");
            Load_Reload_PagePanel();
        }
    }
}


class BorderLayouts {


    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(400, 400);
        //frame.setLayout(new BorderLayout(10,10)); //sets a gap if wanted, between layers


        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panel5 = new JPanel();
        JPanel panel6 = new JPanel();
        JPanel panel7 = new JPanel();

        panel1.setBackground(Color.RED);
        panel2.setBackground(Color.BLUE);
        panel3.setBackground(Color.GREEN);
        panel4.setBackground(Color.PINK);
        panel5.setBackground(Color.BLACK);

        panel1.setPreferredSize(new Dimension(100, 100));
        panel2.setPreferredSize(new Dimension(100, 100));
        panel3.setPreferredSize(new Dimension(100, 100));
        panel4.setPreferredSize(new Dimension(100, 100));
        panel5.setPreferredSize(new Dimension(100, 100));

        panel6.setPreferredSize(new Dimension(100, 100));
        panel7.setPreferredSize(new Dimension(100, 100));

        // can also add panels inside panels
        panel6.add(panel7, BorderLayout.NORTH);


        //this defines where the panels are
        frame.add(panel1, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.CENTER);
        frame.add(panel3, BorderLayout.SOUTH);
        frame.add(panel4, BorderLayout.EAST);
        frame.add(panel5, BorderLayout.WEST);


    }
}

