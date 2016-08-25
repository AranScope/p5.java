import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Boids extends P5{
    private Flock flock;

    private Boids() {
        System.out.println("Boids constructor");
    }

    @Override
    void setup() {
        createCanvas(640, 360);

        flock = new Flock();
        for(int i = 0; i < 100; i++) {
            Boid b = new Boid(getWidth()/2, getHeight()/2);
            flock.addBoid(b);
        }
    }

    @Override
    void draw(double delta) {
        background(51);
        flock.run();
    }

    private class Boid{

        private P5Vector2 acceleration, velocity, position;
        private double r, maxSpeed, maxForce;

        private Boid(double x, double y) {
            this.acceleration = createVector(0, 0);
            this.velocity = createVector(random(-1, 1), random(-1, 1));
            this.position = createVector(x, y);
            this.r = 15.0;
            this.maxSpeed = 3.0;
            this.maxForce = 0.05;
        }

        private void run(ConcurrentLinkedQueue<Boid> boids) {
            this.flock(boids);
            this.update();
            this.borders();
            this.render();
        }

        private void flock(ConcurrentLinkedQueue<Boid> boids) {
            P5Vector2 sep = this.separate(boids);
            P5Vector2 ali = this.align(boids);
            P5Vector2 coh = this.cohesion(boids);

            sep.mult(1.5);
            ali.mult(1.0);
            coh.mult(1.0);

            this.applyForce(sep);
            this.applyForce(ali);
            this.applyForce(coh);
        }

        private void applyForce(P5Vector2 force) {
            this.acceleration.add(force);
        }

        private P5Vector2 separate(ConcurrentLinkedQueue<Boid> boids) {
            double desiredSeparation = 25.0;
            P5Vector2 steer = createVector(0, 0);
            int count = 0;

            for(Boid boid: boids) {
                double d = P5Vector2.dist(this.position, boid.position);
                if((d > 0) && (d < desiredSeparation)) {
                    P5Vector2 diff = P5Vector2.sub(this.position, boid.position);
                    diff.normalize();
                    diff.div(d);
                    steer.add(diff);
                    count++;
                }
            }

            if (count > 0) {
                steer.div(count);
            }

            if (steer.mag() > 0) {
                steer.normalize();
                steer.mult(this.maxSpeed);
                steer.sub(this.velocity);
                steer.limit(this.maxForce);
            }

            return steer;
        }

        private P5Vector2 align(ConcurrentLinkedQueue<Boid> boids) {
            double neighbourDist = 50.0;
            P5Vector2 sum = createVector(0, 0);
            int count = 0;

            for(Boid boid: boids) {
                double d = P5Vector2.dist(this.position, boid.position);
                if((d > 0) && (d < neighbourDist)) {
                    sum.add(boid.velocity);
                    count++;
                }
            }

            if(count > 0) {
                sum.div(count);
                sum.normalize();
                sum.mult(this.maxSpeed);
                P5Vector2 steer = P5Vector2.sub(sum, this.velocity);
                steer.limit(this.maxForce);
                return steer;
            } else {
                return createVector(0, 0);
            }
        }

        private P5Vector2 cohesion(ConcurrentLinkedQueue<Boid> boids) {
            double neighbourDist = 50.0;
            P5Vector2 sum = createVector(0, 0);
            double count = 0;

            for(Boid boid: boids) {
                double d = P5Vector2.dist(this.position, boid.position);

                if((d > 0) && (d < neighbourDist)) {
                    sum.add(boid.position);
                    count++;
                }
            }

            if(count > 0) {
                sum.div(count);
                return this.seek(sum);
            } else {
                return createVector(0, 0);
            }
        }

        private P5Vector2 seek(P5Vector2 target) {
            P5Vector2 desired = P5Vector2.sub(target, this.position);
            desired.normalize();
            desired.mult(this.maxSpeed);
            P5Vector2 steer = P5Vector2.sub(desired, this.velocity);
            steer.limit(this.maxForce);
            return steer;
        }

        private void update() {
            this.velocity.add(this.acceleration);
            this.velocity.limit(this.maxSpeed);
            this.position.add(this.velocity);
            this.acceleration.mult(0);
        }

        private void borders() {
            if (this.position.x < -this.r) this.position.x = getWidth() + this.r;
            if (this.position.y < -this.r) this.position.y = getHeight() + this.r;
            if (this.position.x > getWidth() + this.r) this.position.x = -this.r;
            if (this.position.y > getHeight() + this.r) this.position.y = -this.r;
        }

        private void render() {
            double theta = this.velocity.heading() + radians(90);
            fill(127);
            circle(this.position.x, this.position.y, r);
        }

    }



    private class Flock{
        private ConcurrentLinkedQueue<Boid> boids;

        private Flock() {
            boids = new ConcurrentLinkedQueue<>();
        }

        private void addBoid(Boid boid) {
            boids.add(boid);
        }

        public void run() {
            for(Boid boid: boids) {
                boid.run(boids);
            }
        }
    }

    @Override
    public void mouseDragged(int mouseX, int mouseY) {
        Boid b = new Boid(mouseX, mouseY);
        flock.addBoid(b);
    }

    public static void main(String[] args){
        new Boids();
    }
}
