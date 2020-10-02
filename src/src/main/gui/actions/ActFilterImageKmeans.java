package main.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.filters.point.kmeans.KmeansProcessor;
import main.gui.Menu;

public class ActFilterImageKmeans implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
        JTextField means = new JTextField(1);

        JFrame popup = new JFrame("K-means");
        popup.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        popup.setLayout(new GridLayout(3, 1));

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Number of means"));
        panel.add(means);
        popup.add(panel);

        JButton okbutton = new JButton("Ok");

        okbutton.addActionListener((ActionEvent e) -> {
            Menu.addToPane(
                    KmeansProcessor.renderkmeans(Menu.currentImage(), Integer.valueOf(means.getText())),
                    means.getText() + "-means");
            popup.dispose();
        });
        popup.add(okbutton);
        popup.pack();
        popup.setVisible(true);
    }
}