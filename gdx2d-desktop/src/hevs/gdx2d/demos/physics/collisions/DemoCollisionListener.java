package hevs.gdx2d.demos.physics.collisions;

import hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.PortableApplication;
import hevs.gdx2d.lib.physics.DebugRenderer;
import hevs.gdx2d.lib.physics.PhysicsWorld;

import com.badlogic.gdx.Gdx;
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
	DebugRenderer dbgRenderer;	
	
	int time = 0;
	
	BumpyBall b1, b2, b3, b4; 
	
	@Override
	public void onInit() {
		dbgRenderer = new DebugRenderer();
		setTitle("Collision demo for box2d, mui 2013");
		
		new PhysicsScreenBoundaries(getWindowWidth(), getWindowHeight());
		
		// A BumpyBall has redefined its collision method.
		b1 = new BumpyBall("ball 1", new Vector2(100, 250), 30);
		
		// Indicate that the ball should be informed for collisions
		b1.enableCollisionListener();			
	};
	
	@Override
	public void onGraphicRender(GdxGraphics g) {
		g.clear();
		
		synchronized (world) {
//			dbgRenderer.render(world, g.getCamera().combined);			
			b1.draw(g);
			
			if(b2 != null)
				b2.draw(g);
			
			if(b3 != null)
				b3.draw(g);
			
			if(b4 != null)
				b4.draw(g);
			
			
			// Add balls from time to time
			if(time == 100){
				b2 = new BumpyBall("ball 2", new Vector2(105, 300), 40);
				b2.enableCollisionListener();
			}
			
			if(time == 150){
				b3 = new BumpyBall("ball 3", new Vector2(120, 300), 20);
				b3.enableCollisionListener();
			}
			
			if(time == 200){
				b4 = new BumpyBall("ball 4", new Vector2(130, 310), 30);
				b4.enableCollisionListener();
			}
		}
		
		PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime());
		
		g.drawSchoolLogoUpperRight();
		g.drawFPS();
		time++;				
	}
	
	public DemoCollisionListener(boolean onAndroid){
		super(onAndroid);
	}
	
	public static void main(String args[]){
		new DemoCollisionListener(false);
	}
}
