package hevs.gdx2d.demos.physics.rocket;

import hevs.gdx2d.components.physics.PhysicsStaticBox;
import hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import hevs.gdx2d.components.physics.utils.PhysicsWorld;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.PortableApplication;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * FIXME : encapsulate world with appropriate primities for synchronized
 * FIXME: finish this
 * Demonstrates the use of applyForce to physics objects
 * 
 * 
 * @author Pierre-André Mudry
 * @version 1.0
 */
public class DemoPhysicsRocket extends PortableApplication {
	Box2DDebugRenderer dbgRenderer;
	World world = PhysicsWorld.getInstance();
	Spaceship ship;
	
	static final boolean DEBUG = true;

	DemoPhysicsRocket(boolean onAndroid) {
		super(onAndroid);
	}

	@Override
	public void onInit() {
		world.setGravity(new Vector2(0, 0));
		dbgRenderer = new Box2DDebugRenderer();
		new PhysicsScreenBoundaries(getWindowWidth(), getWindowHeight());
		new PhysicsStaticBox("mur", new Vector2(250, 250), 100, 10);
		ship = new Spaceship(new Vector2(getWindowWidth()/2, getWindowHeight()/2));
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		g.clear();

		if (DEBUG) {
			synchronized (world) {
				dbgRenderer.render(world, g.getCamera().combined);
			}
		}
		
		ship.draw(g);
	}

	@Override
	public void onKeyUp(int keycode) {
		switch (keycode) {
		case Input.Keys.LEFT:
			ship.thrustLeft = false;
			break;
		case Input.Keys.RIGHT:
			ship.thrustRight = false;
			break;
		case Input.Keys.UP:
			ship.thrustUp = 0;
			break;
		default:
			break;
		}

	}

	@Override
	public void onKeyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.LEFT:
			ship.thrustLeft = true;
			break;
		case Input.Keys.RIGHT:
			ship.thrustRight = true;
			break;
		case Input.Keys.UP:
			ship.thrustUp = 100000;
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void onGameLogicUpdate() {
		synchronized (world) {
			world.step(1 / 60f, 7, 7);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DemoPhysicsRocket(false);
	}

}
