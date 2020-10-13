package main.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;

import main.filters.geometric.Rotation;
import main.gui.Menu;

public class ActGeomRotate implements ActionListener {
    String function;

    public ActGeomRotate(String function) {
        this.function = function;
    }

    public void actionPerformed(ActionEvent evt) {
        JTextField angle = new JTextField(0);

        JFrame popup = new JFrame("Rotacion");
        popup.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        popup.setLayout(new GridLayout(3, 1));

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Angulo"));
        panel.add(angle);
        popup.add(panel);

        JButton okbutton = new JButton("Ok");

        okbutton.addActionListener((ActionEvent e) -> {
            Menu.addImageToPane(
                    Rotation.rotate(Menu.currentImage(), Double.valueOf(angle.getText()), function),
                    angle.getText() + "deg_" + function);
            popup.dispose();
        });
        popup.add(okbutton);
        popup.pack();
        popup.setVisible(true);
    }
}
