import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class P5Shape {
    private ArrayList<Point2D.Double> vertices;
    public boolean complete = false;

    public P5Shape() {
        vertices = new ArrayList<>();
    }

    public void addVertex(double x, double y){
        vertices.add(new Point2D.Double(x, y));
    }

    public void draw(Graphics2D g2){
        int[] xpoints = new int[vertices.size()];
        int[] ypoints = new int[vertices.size()];

        for(int i = 0; i < vertices.size(); i++) {
            xpoints[i] = (int)vertices.get(i).getX();
            ypoints[i] = (int)vertices.get(i).getY();
        }

        g2.fillPolygon(xpoints, ypoints, vertices.size());
    }
}
