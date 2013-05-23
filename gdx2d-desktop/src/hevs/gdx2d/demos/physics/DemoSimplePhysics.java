package hevs.gdx2d.demos.physics;

import hevs.gdx2d.components.physics.PhysicsBox;
import hevs.gdx2d.components.physics.PhysicsStaticBox;
import hevs.gdx2d.components.physics.utils.PhysicsConstants;
import hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import hevs.gdx2d.components.physics.utils.PhysicsWorld;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.PortableApplication;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Demonstrates the basic usage of the physics with a simple demo
 * @author Pierre-André Mudry (mui)
 * @version 1.01
 */
public class DemoSimplePhysics extends PortableApplication{

	// Contains all the objects that will be simulated
	World world = PhysicsWorld.getInstance();
	PhysicsBox box;
	Box2DDebugRenderer debugRenderer;
	
	public DemoSimplePhysics(boolean onAndroid) {	
		super(onAndroid);
	}
	
	@Override
	public void onInit() {
		setTitle("Simple physics simulation, mui 2013");					
		
		int w = getWindowWidth(), h = getWindowHeight();
		
		// Build the walls around the screen
		new PhysicsScreenBoundaries(w, h);
		
		// The slope on which the objects roll
		new PhysicsStaticBox("slope", new Vector2(w/2, h/2), w/3, 2, (float)Math.PI / 40.0f);		
		
		// Build the ball
		box = new PhysicsBox("ball", new Vector2(w/2, h-0.1f*h), 25, 25, 15, 0.3f, 0.3f);		
		box.body.setLinearVelocity(20, 2);
		
		// Build the dominoes
		int nDominoes = 20;
		int dominoSpace = (w - 80) / nDominoes;
			
		for (int i = 0; i < nDominoes ; i++) {
			new PhysicsBox("box" + i, new Vector2(80+i*dominoSpace, 120), 3, 30, 0.1f, 0.1f, 0.3f);			
		}
		
		debugRenderer = new Box2DDebugRenderer();		
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		g.clear();
		
		/**
		 *  The synchronized primitive is required because we use an object in two
		 *  different threads
		 */		
		debugRenderer.render(world, g.getCamera().combined);
		PhysicsWorld.updatePhysics(Gdx.graphics.getRawDeltaTime());		
		
		g.drawSchoolLogoUpperRight();
		g.drawFPS();
	}	

	
	public static void main(String[] args) {
		new DemoSimplePhysics(false);
	}

}
