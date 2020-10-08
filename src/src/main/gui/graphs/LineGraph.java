package main.gui.graphs;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import main.filters.point.LinealTransform;

public class LineGraph extends JLabel {
    private final Color lineColor = new Color(44, 102, 230, 180);
    private final Color pointColor = new Color(100, 100, 100, 180);
    private final Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private static final long serialVersionUID = 1L;
    private List<Point> points;
    private final int[] xLabelPoints = { 0, 51, 102, 153, 204, 255 };
    private static final int PADDING = 25;
    private static final int LABEL_PADDING = 25;
    private static final int POINT_WIDTH = 4;
    private static final int Y_DIVISIONS = 10;
    private static final int MAX_VALUE = 255;
    private static final int MIN_VALUE = 0;

    public LineGraph(List<Point> points) {
        this.points = points;
        this.setMinimumSize(new Dimension(150, 150));
        this.setPreferredSize(new Dimension(200, 200));
        this.setOpaque(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        points.sort((a, b) -> a.x > b.x ? 1 : -1);
        List<Point> filledPoints = LinealTransform.fillPoints(this.points);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < Y_DIVISIONS + 1; i++) {
            int x0 = PADDING + LABEL_PADDING;
            int x1 = POINT_WIDTH + PADDING + LABEL_PADDING;
            int y0 = getHeight() - ((i * (getHeight() - PADDING * 2 - LABEL_PADDING)) / Y_DIVISIONS + PADDING + LABEL_PADDING);
            int y1 = y0;
            if (filledPoints.isEmpty()) {
                g2d.setColor(gridColor);
                g2d.drawLine(PADDING + LABEL_PADDING + 1 + POINT_WIDTH, y0, getWidth() - PADDING, y1);
                g2d.setColor(Color.BLACK);
                String yLabel = ((int) ((MIN_VALUE + (MAX_VALUE - MIN_VALUE) * ((i * 1.0) / Y_DIVISIONS)) * 100))
                        / 100.0 + "";
                FontMetrics metrics = g2d.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2d.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2d.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < xLabelPoints.length; i++) {
            if (xLabelPoints.length > 1) {
                int x0 = i * (getWidth() - PADDING * 2 - LABEL_PADDING) / (xLabelPoints.length - 1) + PADDING
                        + LABEL_PADDING;
                int x1 = x0;
                int y0 = getHeight() - PADDING - LABEL_PADDING;
                int y1 = y0 - POINT_WIDTH;
                if ((i % ((int) (xLabelPoints.length / 20.0) + 1)) == 0) {
                    g2d.setColor(gridColor);
                    g2d.drawLine(x0, getHeight() - PADDING - LABEL_PADDING - 1 - POINT_WIDTH, x1, PADDING);
                    g2d.setColor(Color.BLACK);
                    String xLabel = xLabelPoints[i] + "";
                    FontMetrics metrics = g2d.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2d.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2d.drawLine(x0, y0, x1, y1);
            }
        }
        g2d.translate(PADDING + LABEL_PADDING, getHeight() - PADDING - LABEL_PADDING);
        g2d.scale(1, -1);

        // create x and y axes
        g2d.drawLine(0, 0, getWidth() - PADDING * 2 - LABEL_PADDING, 0);
        g2d.drawLine(0, 0, 0, getHeight() - PADDING * 2 - LABEL_PADDING);

        double xScale = ((double) getWidth() - 2 * PADDING - LABEL_PADDING) / (MAX_VALUE - 1);
        double yScale = ((double) getHeight() - 2 * PADDING - LABEL_PADDING) / (MAX_VALUE - MIN_VALUE);

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < filledPoints.size(); i++) {
            int x1 = (int) (filledPoints.get(i).x * xScale);
            int y1 = (int) (filledPoints.get(i).y * yScale);
            graphPoints.add(new Point(x1, y1));
        }
        Stroke oldStroke = g2d.getStroke();
        g2d.setColor(lineColor);
        g2d.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2d.drawLine(x1, y1, x2, y2);
        }

        g2d.setStroke(oldStroke);
        g2d.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - POINT_WIDTH / 2;
            int y = graphPoints.get(i).y - POINT_WIDTH / 2;
            int ovalW = POINT_WIDTH;
            int ovalH = POINT_WIDTH;
            g2d.fillOval(x, y, ovalW, ovalH);
        }
        g2d.dispose();
    }
}