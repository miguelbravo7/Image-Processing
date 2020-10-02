package main.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.awt.Container;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.filters.point.LinealTransform;
import main.gui.Menu;

public class ActLinealTransform implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
        JFrame popup = new JFrame("Diferencia de imagen");
        popup.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        JPanel panelx = new JPanel();
        JPanel panely = new JPanel();
        panelx.setLayout(new BoxLayout(panelx, BoxLayout.Y_AXIS));
        panely.setLayout(new BoxLayout(panely, BoxLayout.Y_AXIS));
        panelx.add(new JTextField("x:", 6));
        panely.add(new JTextField("y:", 6));
        popup.setLayout(new FlowLayout());

        JButton addpoint = new JButton("Anadir punto");

        addpoint.addActionListener((ActionEvent e) -> {
            panelx.add(new JTextField(6));
            panelx.revalidate();
            panelx.repaint();

            panely.add(new JTextField(6));
            panely.revalidate();
            panely.repaint();
        });
        popup.add(addpoint);
        JPanel panels = new JPanel();
        panels.add(panelx);
        panels.add(panely);

        JButton okbutton = new JButton("Ok");

        okbutton.addActionListener((ActionEvent e) -> {
            Menu.addToPane(LinealTransform.transform(Menu.currentImage(),
                    iterateOverJTextFields(panelx, panely)), "Trans. lineal");
            popup.dispose();
        });
        popup.add(okbutton);
        popup.add(panels);

        popup.pack();
        popup.setVisible(true);
    }

    private ArrayList<Point> iterateOverJTextFields(Container xcontainer, Container ycontainer) {
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();
        for (Component component : xcontainer.getComponents()) {
            if (component instanceof JTextField) {
                x.add(Integer.parseInt(((JTextField) component).getText()));
            }
        }
        for (Component component : ycontainer.getComponents()) {
            if (component instanceof JTextField) {
                points.add(new Point(x.remove(0), Integer.parseInt(((JTextField) component).getText())));
            }
        }
        return points;
    }
}