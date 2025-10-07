package org.example.application;

import org.example.dataReader.ObjectClasses.Header;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PageView extends JFrame implements ActionListener {
JPanel mainPanel =  new JPanel();
JPanel expandableHeaders_subheadersPanel = new JPanel();


    public PageView() {
        this.setTitle("title of page");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(800, 500);



        JScrollPane mainScrollPane = new JScrollPane(
                mainPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollPane expandableHeaders_subheadersScrollPane = new JScrollPane(
                expandableHeaders_subheadersPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.setBackground(Color.BLUE);
        expandableHeaders_subheadersPanel.setBackground(Color.GREEN);

        mainScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        mainScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        expandableHeaders_subheadersScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        expandableHeaders_subheadersScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        expandableHeaders_subheadersScrollPane.setPreferredSize(new Dimension(300, 80));


        this.setLayout(new BorderLayout());

        this.add(mainScrollPane, BorderLayout.CENTER);
        this.add(expandableHeaders_subheadersScrollPane, BorderLayout.EAST);


        this.setVisible(true);
    }

    public void load_reload_PageViewPanel(){
        mainPanel.removeAll();
        expandableHeaders_subheadersPanel.removeAll();

        //for (Header header : )

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        PageView view = new PageView();
    }
}
