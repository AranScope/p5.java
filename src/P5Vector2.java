
public class P5Vector2 {
    public double x, y;

    public P5Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static double dist(P5Vector2 vect1, P5Vector2 vect2) {
        return Math.sqrt((vect1.x - vect2.x) * (vect1.x - vect2.x)) + ((vect1.y - vect2.y) * (vect1.y - vect2.y));
    }

    public static P5Vector2 sub(P5Vector2 vect1, P5Vector2 vect2) {
        return new P5Vector2(vect1.x - vect2.x, vect1.y - vect2.y);
    }

    public double mag() {
        return Math.sqrt((x*x) + (y*y));
    }

    public void mult(double d) {
        this.x *= d;
        this.y *= d;
    }

    public void div(double d) {
        this.x /= d;
        this.y /= d;
    }

    public void add(P5Vector2 vect) {
        this.x += vect.x;
        this.y += vect.y;
    }

    public void sub(P5Vector2 vect) {
        this.x -= vect.x;
        this.y -= vect.y;
    }

    public void normalize() {
        double mag = mag();
        this.x = x / mag;
        this.y = y / mag;
    }

    public void limit(double max) {
        if(mag() > max) {
            normalize();
            mult(max);
        }
    }

    public double heading() {
        return Math.atan(y/x);
    }
}
