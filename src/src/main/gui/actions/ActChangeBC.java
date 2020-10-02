package main.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.filters.point.BrightContrast;
import main.gui.Menu;

public class ActChangeBC implements ActionListener {
    JTextField brillo;
    JTextField contraste;

    public void actionPerformed(ActionEvent evt) {
        brillo = new JTextField(Menu.currentHist().med[0].toString());

        contraste = new JTextField(Menu.currentHist().dev[0].toString());

        JFrame popup = new JFrame("Cambio brillo-contraste");
        popup.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        popup.setLayout(new GridLayout(3, 1));

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Brillo"));
        panel.add(brillo);
        popup.add(panel);
        JPanel panel2 = new JPanel(new GridLayout(2, 1));
        panel2.add(new JLabel("Contraste"));
        panel2.add(contraste);
        popup.add(panel2);

        JButton okbutton = new JButton("Ok");

        okbutton.addActionListener((ActionEvent e) -> {
            Menu
                    .addToPane(
                            BrightContrast.adjustImg(Menu.currentImage(), Menu.currentHist(),
                                    Double.valueOf(brillo.getText()), Double.valueOf(contraste.getText())),
                            "Conversion");
            popup.dispose();
        });
        popup.add(okbutton);
        popup.pack();
        popup.setVisible(true);
    }
}