package main.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;

import main.filters.point.GammaCorrection;
import main.gui.Menu;

public class ActImageGammaCorrection implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
        JTextField gamma = new JTextField(1);

        JFrame popup = new JFrame("Correccion de gamma");
        popup.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        popup.setLayout(new GridLayout(3, 1));

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Gamma"));
        panel.add(gamma);
        popup.add(panel);

        JButton okbutton = new JButton("Ok");

        okbutton.addActionListener((ActionEvent e) -> {
            Menu.addImageToPane(
                    GammaCorrection.gammaCorrection(Menu.currentImage(), Double.valueOf(gamma.getText())),
                    "Gamma");
            popup.dispose();
        });
        popup.add(okbutton);
        popup.pack();
        popup.setVisible(true);
    }
}
