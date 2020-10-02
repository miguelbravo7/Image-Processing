package main.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.filters.point.SpecHist;
import main.gui.Menu;

public class ActImgHistSpec implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
        ArrayList<String> pestanas = new ArrayList<>();

        for (int i = 0; i < Menu.tabbedPane.getTabCount(); i++) {
            pestanas.add(Menu.tabbedPane.getTitleAt(i));
        }

        JComboBox<Object> img1 = new JComboBox<>(pestanas.toArray());

        JComboBox<Object> img2 = new JComboBox<>(pestanas.toArray());

        JFrame popup = new JFrame("Especificacion de histograma.");
        popup.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        popup.setLayout(new GridLayout(3, 1));

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Imagen"));
        panel.add(img1);
        popup.add(panel);
        JPanel panel2 = new JPanel(new GridLayout(2, 1));
        panel2.add(new JLabel("Histograma de referencia"));
        panel2.add(img2);
        popup.add(panel2);

        JButton okbutton = new JButton("Ok");

        okbutton.addActionListener((ActionEvent e) -> {
            popup.dispose();
            Menu.addToPane(SpecHist.convertHist(Menu.imagelist.get(img1.getSelectedIndex()),
                    Menu.imagehist.get(img1.getSelectedIndex()),
                    Menu.imagehist.get(img2.getSelectedIndex())), "Spec. hist.");
        });
        popup.add(okbutton);
        popup.pack();
        popup.setVisible(true);
    }
}