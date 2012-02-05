package pendulum;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JComponent;
import lombok.val;
import org.jbox2d.common.Vec2;

/**
 * Display a pendulum system.
 */
public class Viewer extends JComponent {

    private static final long serialVersionUID = 1L;

    private static final double SCALE = 20;
    private static final Shape BOB =
        new Ellipse2D.Float(-Pendulum.BOB_RADIUS, -Pendulum.BOB_RADIUS,
                            Pendulum.BOB_RADIUS * 2, Pendulum.BOB_RADIUS * 2);
    private static final Stroke ROD_STROKE = new BasicStroke(0.1f);

    private final Pendulum last;

    /**
     * Create a viewer displaying the given pendulum chain.
     * @param last    the final pendulum in a pendulum chain
     * @param radius  the size of the viewing area
     */
    public Viewer(final Pendulum last, final double radius) {
        val size = new Dimension((int) (SCALE * 2 * radius),
                                 (int) (SCALE * 2 * radius));
        setPreferredSize(size);
        this.last = last;
        setBackground(Color.BLACK);
        setOpaque(true);
    }

    @Override
    public final void paintComponent(final Graphics graphics) {
        /* Prepare the drawing space. */
        Graphics2D g = (Graphics2D) graphics;
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(getWidth() / 2, getHeight() / 2);
        g.scale(SCALE, -SCALE);

        /* Draw each pendulum. */
        g.setColor(Color.WHITE);
        g.setStroke(ROD_STROKE);
        Pendulum current = last;
        while (current != null) {
            Pendulum next = current.getParent();
            Vec2 pos = current.getBob().getPosition();
            val at = AffineTransform.getTranslateInstance(pos.x, pos.y);
            g.fill(at.createTransformedShape(BOB));
            Vec2 pivot;
            if (next != null) {
                pivot = next.getBob().getPosition();
            } else {
                pivot = new Vec2(0, 0);
            }
            val rod = new Line2D.Float(pos.x, pos.y, pivot.x, pivot.y);
            g.draw(rod);
            current = next;
        }
    }
}
