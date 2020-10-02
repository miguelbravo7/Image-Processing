package main.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;

import main.filters.geometric.Scale;
import main.gui.Menu;

public class ActGeomScale implements ActionListener {
    String function;

    public ActGeomScale(String function) {
        this.function = function;
    }

    public void actionPerformed(ActionEvent evt) {
        JTextField width = new JTextField();

        JTextField height = new JTextField();

        JFrame popup = new JFrame("Cambio de escala");
        popup.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        popup.setLayout(new GridLayout(3, 1));

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Ancho"));
        panel.add(width);
        popup.add(panel);
        JPanel panel2 = new JPanel(new GridLayout(2, 1));
        panel2.add(new JLabel("Alto"));
        panel2.add(height);
        popup.add(panel2);

        JButton okbutton = new JButton("Ok");

        okbutton.addActionListener((ActionEvent e) -> {
                Menu.addToPane(Scale.scale(Menu.currentImage(), Double.valueOf(width.getText()),
                        Double.valueOf(height.getText()), function), "Bilinear");
                popup.dispose();
        });
        popup.add(okbutton);
        popup.pack();
        popup.setVisible(true);
    }
}
