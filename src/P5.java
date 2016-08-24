import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public abstract class P5 extends JPanel{
    private JFrame frame;
    private boolean running = true;
    private long lastFpsTime = 0;
    private long fps = 0;
    private Graphics2D g2;
    private Color color = Color.white;
    private Color bgColor = Color.black;

    private P5Simplex simplex;


    private Stack<P5Shape> shapes;

    public P5(){
        super();
        shapes = new Stack<>();
        simplex = new P5Simplex();
        setup();
    }

    public void createCanvas(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        frame = new JFrame("p5j");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);
        frame.pack();

        loop();
    }

    public void background(int brightness) {
        bgColor = new Color(brightness, brightness, brightness);
    }

    public void fill(int brightness) {
        color = new Color(brightness, brightness, brightness);
    }

    public void beginShape(){
        shapes.add(new P5Shape());
    }

    public void endShape() {
        shapes.peek().complete = true;
    }

    public void vertex(double x, double y) {
        shapes.peek().addVertex(x, y);
    }

    private void loop()
    {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        while (running)
        {

            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);

            lastFpsTime += updateLength;
            fps++;

            if (lastFpsTime >= 1000000000)
            {
                System.out.println("(FPS: "+fps+")");
                lastFpsTime = 0;
                fps = 0;
            }

            draw(delta);
            this.repaint();

            try{
                Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        this.g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());

        g2.setColor(color);
        if(!shapes.isEmpty()) {
            if (shapes.peek().complete) {
                shapes.peek().draw(g2);
            }
        }
    }

    double map(double val, double min, double max, double newMin, double newMax) {
        return newMin + val * ((newMax-newMin) / (max-min));
    }

    double noise(double x, double y) {
        return simplex.eval(x, y);
    }

    abstract void setup();
    abstract void draw(double delta);
}
