package main.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Point;
import java.awt.Container;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.text.NumberFormatter;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

import main.filters.point.LinealTransform;
import main.gui.Menu;
import main.gui.graphs.LineGraph;

public class ActLinealTransform implements ActionListener {
    List<Point> mappings;
    JPanel xPanel;
    JPanel yPanel;
    LineGraph graph;

    public void actionPerformed(ActionEvent evt) {
        JFrame popup = new JFrame("Transformacion lineal");
        xPanel = new JPanel();
        yPanel = new JPanel();
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.Y_AXIS));
        yPanel.setLayout(new BoxLayout(yPanel, BoxLayout.Y_AXIS));
        xPanel.add(new JLabel("Valor original"));
        xPanel.add(numberSpinner(200));
        xPanel.add(numberSpinner(210));
        yPanel.add(new JLabel("Valor objetivo"));
        yPanel.add(numberSpinner(62));
        yPanel.add(numberSpinner(55));

        JPanel coordPanel = new JPanel();
        coordPanel.add(xPanel);
        coordPanel.add(yPanel);
        popup.add(coordPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        mappings = iterateOverJTextFields(xPanel, yPanel);
        graph = new LineGraph(mappings);
        // Put constraints on different buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        rightPanel.add(graph, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JButton addPoint = new JButton("Anadir punto");
        addPoint.addActionListener((ActionEvent e) -> {
            xPanel.add(numberSpinner(0));
            yPanel.add(numberSpinner(0));
            mappings.clear();
            mappings.addAll(iterateOverJTextFields(xPanel, yPanel));
            graph.repaint();
            popup.pack();
        });
        rightPanel.add(addPoint, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton delPoint = new JButton("Eliminar punto");
        delPoint.addActionListener((ActionEvent e) -> {
            if(mappings.size() > 2) {
                xPanel.remove(mappings.size());
                yPanel.remove(mappings.size());
                mappings.remove(0);
                graph.repaint();
                popup.pack();
            }
        });
        rightPanel.add(delPoint, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton okButton = new JButton("Ok");
        okButton.addActionListener((ActionEvent e) -> {
            Menu.addImageToPane(LinealTransform.transform(Menu.currentImage(), iterateOverJTextFields(xPanel, yPanel)),
                    "Trans. lineal");
            popup.dispose();
        });
        rightPanel.add(okButton, gbc);

        popup.add(rightPanel);

        popup.setLayout(new FlowLayout());
        popup.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        popup.pack();
        popup.setVisible(true);
    }

    private JSpinner numberSpinner(int value) {
        SpinnerModel model = new SpinnerNumberModel(value, 0, 255, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));
        spinner.addChangeListener((ChangeEvent c) -> {
            mappings.clear();
            mappings.addAll(iterateOverJTextFields(xPanel, yPanel));
            graph.repaint();
        });
        JFormattedTextField textField = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
        ((NumberFormatter) textField.getFormatter()).setAllowsInvalid(false);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                mappings.clear();
                mappings.addAll(iterateOverJTextFields(xPanel, yPanel));
                graph.repaint();
            }
        });
        return spinner;
    }

    private ArrayList<Point> iterateOverJTextFields(Container xContainer, Container yContainer) {
        ArrayList<Integer> xCoordinates = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();
        for (Component component : xContainer.getComponents()) {
            if (component instanceof JSpinner) {
                xCoordinates.add((Integer) ((JSpinner) component).getValue());
            }
        }
        for (Component component : yContainer.getComponents()) {
            if (component instanceof JSpinner) {
                points.add(new Point(xCoordinates.remove(0), (Integer) ((JSpinner) component).getValue()));
            }
        }
        return points;
    }
}