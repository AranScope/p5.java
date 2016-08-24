public class Main extends P5 {

    private double yoff = 0.0;

    public static void main(String[] args) {
        new Main();
    }

    @Override
    void setup() {
        createCanvas(710, 400);
    }

    @Override
    void draw(double delta) {
        background(51);
        fill(255);
        beginShape();

        double xoff = 0;
        for(double x = 0; x < getWidth(); x+= 10) {
            double y = map(noise(xoff, yoff), 0, 1, 200, 300);

            vertex(x, y);

            xoff += 0.05;
        }

        yoff += 0.01;

        vertex(getWidth(), getHeight());
        vertex(0, getHeight());

        endShape();
    }
}
