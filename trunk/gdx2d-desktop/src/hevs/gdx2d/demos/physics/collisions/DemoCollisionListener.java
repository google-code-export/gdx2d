package hevs.gdx2d.demos.physics.collisions;

import hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import hevs.gdx2d.components.physics.utils.PhysicsWorld;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.PortableApplication;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Simple collision handling in box2d
 * @author Pierre-André Mudry (mui)
 * @version 1.0
 */
public class DemoCollisionListener extends PortableApplication{	
	World world = PhysicsWorld.getInstance();	
	Box2DDebugRenderer dbgRenderer;	
	int time = 0;
	
	@Override
	public void onInit() {
		dbgRenderer = new Box2DDebugRenderer();
		setTitle("Collision demo for box2d, mui 2013");
		
		new PhysicsScreenBoundaries(getWindowWidth(), getWindowHeight());
		
		// A Ball has redefined its collision method.
		Ball b = new Ball("ball 1", new Vector2(100, 250), 30);
		
		// Indicate that the ball should be informed for collisions
		b.enableCollisionListener();			
	};
	
	@Override
	public void onGraphicRender(GdxGraphics g) {
		g.clear();
		
		synchronized (world) {
			dbgRenderer.render(world, g.getCamera().combined);
			
			// After some time, add another ball
			if(time == 200)
				new Ball("ball 2", new Vector2(100, 250), 40).enableCollisionListener();
		}		
		
		time++;				
	}
	
	@Override
	public void onGameLogicUpdate() {	
		synchronized (world) {
			world.step(1/50f, 6, 4);			
		}
	}
	
	public DemoCollisionListener(boolean onAndroid){
		super(onAndroid);
	}
	
	public static void main(String args[]){
		new DemoCollisionListener(false);
	}
}
