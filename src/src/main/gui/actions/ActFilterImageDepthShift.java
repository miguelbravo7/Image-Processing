package main.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;

import main.filters.point.ColorDepth;
import main.gui.MenuBar;


public class ActFilterImageDepthShift implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
        JTextField depth = new JTextField(1);

        JFrame popup = new JFrame("Digitalizacion");
        popup.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        popup.setLayout(new GridLayout(3, 1));

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Numero de bits"));
        panel.add(depth);
        popup.add(panel);

        JButton okbutton = new JButton("Ok");

        okbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuBar.program.addToPane(
                        ColorDepth.colorDepthShift(MenuBar.program.currentImage(), Integer.valueOf(depth.getText())),
                        depth.getText() + " bit depth");
                popup.dispose();
            }
        });
        popup.add(okbutton);
        popup.pack();
        popup.setVisible(true);
    }
}
