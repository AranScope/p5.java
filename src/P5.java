import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class P5 extends JPanel implements MouseInputListener, Runnable{
    private JFrame frame;
    private boolean running = true;
    private long lastFpsTime = 0;
    private long fps = 0;
    private Graphics2D g2;
    private Color color = Color.white;
    private Color bgColor = Color.black;

    private P5Simplex simplex;
    private ConcurrentLinkedDeque<P5Shape> shapes;
    private Random ra;

    private boolean setup = false;

    public P5(){
        super();
        shapes = new ConcurrentLinkedDeque<>();
        simplex = new P5Simplex();
        ra = new Random();
        System.out.println("Calling setup");
        setup();
        System.out.println("Setup called");
        this.addMouseMotionListener(this);

        setup = true;
    }

    @Override
    public void run() {
        loop();
    }

    public void createCanvas(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        frame = new JFrame("p5j");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);
        frame.pack();

        new Thread(this).start();
    }

    public void background(int brightness) {
        bgColor = new Color(brightness, brightness, brightness);
        g2.setColor(bgColor);
        g2.fillRect(0,0,getWidth(),getHeight());
    }

    public void fill(int brightness) {
        color = new Color(brightness, brightness, brightness);
    }

    public void beginShape(){
        shapes.addFirst(new P5Shape());
    }

    public void endShape() {

        shapes.getFirst().complete = true;
    }

    public void circle(double cx, double cy, double r) {
        shapes.addFirst(new P5Circle(cx, cy, r));
    }

    public void vertex(double x, double y) {
        shapes.getFirst().addVertex(x, y);
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
                System.out.println("(DELTA: "+delta+" ms)");
                lastFpsTime = 0;
                fps = 0;
            }

            if(setup){
                draw(delta);
                this.repaint();
            }


            try{
                Thread.sleep( Math.abs((lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000));
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
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(color);

        for(P5Shape shape: shapes) {
            if(shape.complete){
                shape.draw(g2);
            }
        }

        shapes.clear();
    }

    public void pop() {
        shapes.removeLast();
    }

    double map(double val, double min, double max, double newMin, double newMax) {
        return newMin + val * ((newMax-newMin) / (max-min));
    }

    double noise(double x, double y) {
        return simplex.eval(x, y);
    }

    abstract void setup();
    abstract void draw(double delta);

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseClicked(e.getX(), e.getY());
    }

    public void mouseClicked(int mouseX, int mouseY) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseDragged(e.getX(), e.getY());
    }

    public void mouseDragged(int mouseX, int mouseY){

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public P5Vector2 createVector(double x, double y){
        return new P5Vector2(x, y);
    }

    public double random(double min, double max) {
        return min + (max - min) * ra.nextDouble();
    }

    public double radians(double degrees){
        return Math.toRadians(degrees);
    }

}
