import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by xvacl on 25/08/2016.
 */
public class P5Circle extends P5Shape {
    double cx, cy, r;
    Ellipse2D.Double circle;

    public P5Circle(double cx, double cy, double r) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        circle = new Ellipse2D.Double(cx - r/2, cy - r/2, r, r);
        super.complete = true;
    }
    public void draw(Graphics2D g2){
        g2.draw(circle);
    }
}
