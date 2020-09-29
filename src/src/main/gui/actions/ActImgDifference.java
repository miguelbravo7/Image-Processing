package main.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.GridLayout;

import main.filters.point.Difference;
import main.gui.MenuBar;


public class ActImgDifference implements ActionListener {
	static final int BRI_MIN = 0;
    static final int BRI_MAX = 255;
	JSlider umbral;
    
    public void actionPerformed(ActionEvent evt) {
        ArrayList<String> pestanas = new ArrayList<String>();

        for (int i = 0; i < MenuBar.program.tabbedPane.getTabCount(); i++) {
            pestanas.add(MenuBar.program.tabbedPane.getTitleAt(i));
        }

        JComboBox<Object> img1 = new JComboBox<Object>(pestanas.toArray());

        JComboBox<Object> img2 = new JComboBox<Object>(pestanas.toArray());

        JFrame popup = new JFrame("Diferencia de imagen.");
        popup.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        popup.setLayout(new GridLayout(3, 1));

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Imagen 1"));
        panel.add(img1);
        popup.add(panel);
        JPanel panel2 = new JPanel(new GridLayout(2, 1));
        panel2.add(new JLabel("Imagen 2"));
        panel2.add(img2);
        popup.add(panel2);

        JButton okbutton = new JButton("Ok");

        okbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                popup.dispose();

                MenuBar.program.addToPane(Difference.difference(MenuBar.program.imagelist.get(img1.getSelectedIndex()),
                MenuBar.program.imagelist.get(img2.getSelectedIndex())), "Diferencia");
                umbralPopup();
            }
        });
        popup.add(okbutton);
        popup.pack();
        popup.setVisible(true);
    }

    private void umbralPopup() {
        umbral = new JSlider(javax.swing.SwingConstants.HORIZONTAL, BRI_MIN, BRI_MAX, 255);
        umbral.setName("Brillo");

        // Turn on labels at major tick marks.
        umbral.setMajorTickSpacing(50);
        umbral.setMinorTickSpacing(1);
        umbral.setPaintTicks(true);
        umbral.setPaintLabels(true);
        umbral.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Difference.changeUmbral(MenuBar.program.currentImage(), umbral.getValue());
                MenuBar.program.doRedraw();
            }
        });

        JFrame popupUmbral = new JFrame("Diferencia de imagen");
        popupUmbral.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        popupUmbral.setLayout(new GridLayout(1, 1));

        JPanel panelUmbral = new JPanel(new GridLayout(2, 1));
        panelUmbral.add(new JLabel("Umbral"));
        panelUmbral.add(umbral);
        popupUmbral.add(panelUmbral);

        popupUmbral.pack();
        popupUmbral.setVisible(true);
    }
}