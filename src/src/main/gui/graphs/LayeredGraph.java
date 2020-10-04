package main.gui.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JLabel;

public class LayeredGraph extends JLabel {
    private static final long serialVersionUID = 1L;
    private List<Graph> graphs;

    public LayeredGraph(List<Map<Integer, Double>> hmArray, Double[] med) {
        this.setMinimumSize(new Dimension(256, 100));
        this.setPreferredSize(new Dimension(256, 200));
        this.graphs = new ArrayList<>();
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
        double[] max = new double[hmArray.size()];
        double biggestMax = 1;
        for(int i = 0; i < hmArray.size(); i++){
            Optional<Map.Entry<Integer, Double>> entry = hmArray.get(i).entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1);
            max[i] = entry.isPresent() ? entry.get().getValue() : 1;
            biggestMax = Math.max(biggestMax, max[i]);
        }

        for (int i = 0; i < hmArray.size(); i++) {
            this.graphs.add(new Graph(hmArray.get(i), colors[i], med[i].intValue()));
            this.graphs.get(i).heightScale = (float) (max[i] / biggestMax);
        }
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for (Graph graph : graphs) {
                    graph.dispatchEvent(e);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Graph graph : this.graphs) {
            graph.setBounds(this.getVisibleRect());
            graph.paintComponent(g);
        }
        g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
    }
}
