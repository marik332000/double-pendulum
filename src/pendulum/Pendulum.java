package pendulum;

import lombok.Getter;
import lombok.val;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

/**
 * A single pendulum attached to an arbitrary body.
 */
public final class Pendulum {

    private static final float BOB_DENSITY = 1f;

    /** The radius of a pendulum bob. */
    public static final float BOB_RADIUS = 0.5f;

    /** The lenth of a pendulum rod. */
    public static final float ROD_LENGTH = 5f;

    @Getter private final Pendulum parent;
    @Getter private final Body bob;

    /**
     * Create a pendulum attached to another pendulum.
     * @param parent  the parent pendulum
     */
    public Pendulum(final Pendulum parent) {
        this.parent = parent;
        bob = create(parent.getBob());
    }

    /**
     * Create a pendulum attached to a body.
     * @param pivot  pivot body for this pendulum
     */
    public Pendulum(final Body pivot) {
        this.parent = null;
        bob = create(pivot);
    }

    /**
     * Create the pendulum body and attach it to the pivot body.
     * @param pivot  the pivot body
     * @return the pendulum bob body
     */
    private Body create(final Body pivot) {
        World world = pivot.getWorld();
        double angle = Math.random() * Math.PI;
        float x = (float) Math.cos(angle) * ROD_LENGTH;
        float y = (float) Math.sin(angle) * ROD_LENGTH;
        Vec2 origin = pivot.getWorldCenter();
        Body mass = newBob(world, new Vec2(origin.x + x, origin.y + y));

        /* Attach to the pivot. */
        val joint = new RevoluteJointDef();
        joint.initialize(pivot, mass, pivot.getWorldCenter());
        joint.enableMotor = true;
        world.createJoint(joint);
        return mass;
    }

    /**
     * Create a new bob body.
     * @param world     the world to place the body in
     * @param position  the starting position of the bob
     * @return the new bob
     */
    private Body newBob(final World world, final Vec2 position) {
        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        def.position = position;
        CircleShape circle = new CircleShape();
        circle.m_radius = BOB_RADIUS;
        FixtureDef fix = new FixtureDef();
        fix.shape = circle;
        fix.density = BOB_DENSITY;
        fix.friction = 0f;
        fix.isSensor = true;
        Body mass = world.createBody(def);
        mass.createFixture(fix);
        return mass;
    }
}
