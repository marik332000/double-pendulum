package pendulum;

import javax.swing.JFrame;
import lombok.extern.java.Log;
import lombok.val;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 * Launches the application.
 */
@Log
public final class Launcher {

    private static final int FPS = 30;
    private static final int V_ITERATIONS = 8;
    private static final int P_ITERATIONS = 3;
    private static final Vec2 GRAVITY = new Vec2(0, -25f);

    private static final double VIEWSCALE = 1.1;
    private static int bobs = 2;

    /**
     * Hidden constructor.
     */
    private Launcher() {
    }

    /**
     * Launch the application.
     * @param args  command line arguments
     */
    public static void main(final String[] args) {
        /* Fix for poor OpenJDK performance. */
        System.setProperty("sun.java2d.pmoffscreen", "false");

        val world = new World(GRAVITY, true);
        Body root = world.createBody(new BodyDef());
        FixtureDef fix = new FixtureDef();
        fix.shape = new CircleShape();
        fix.friction = 0f;
        root.createFixture(fix);

        Pendulum last = new Pendulum(root);
        for (int i = 1; i < bobs; i++) {
            last = new Pendulum(last);
        }

        val frame = new JFrame("Pendulum");
        val viewer = new Viewer(last, Pendulum.ROD_LENGTH * bobs * VIEWSCALE);
        frame.add(viewer);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while (true) {
            world.step(1f / FPS, V_ITERATIONS, P_ITERATIONS);
            viewer.repaint();
            try {
                Thread.sleep((long) (1000f / FPS));
            } catch (InterruptedException e) {
                log.warning("interrupted");
            }
        }
    }
}
