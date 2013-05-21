package hevs.gdx2d.demos.physics.particle;

import hevs.gdx2d.components.physics.utils.PhysicsWorld;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.PortableApplication;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Demo for particle physics. There are no collisions in the physics and
 * no boundaries.
 * 
 * @author Pierre-Andre Mudry (mui)
 * @version 1.01
 */
public class DemoParticlePhysics extends PortableApplication {
	Box2DDebugRenderer dbgRenderer;
	World w = PhysicsWorld.getInstance();

	// Particle creation related
	boolean mouseActive = false;
	public int CREATION_RATE = 2;
	public final int MAX_AGE = 20;
	Vector2 position;

	@Override
	public void onInit() {
		setTitle("Particle physics, mui 2013");
		dbgRenderer = new Box2DDebugRenderer();
		w.setGravity(new Vector2(0,0));
		Gdx.app.log("[DemoParticlePhysics]", "Click on screen to create particles");
		Gdx.app.log("[DemoParticlePhysics]", "+/- change the creation rate of particles");
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		g.clear();
 
		synchronized (w) {			
			Vector<Particle> toBeRemoved = new Vector<Particle>();
			Iterator<Body> it = w.getBodies();

			while (it.hasNext()) {
				Body p = it.next();

				if (p.getUserData() instanceof Particle) {
					Particle thep = (Particle) p.getUserData();
					thep.step();
					thep.render(g);

					if(thep.shouldbeDestroyed()){
						toBeRemoved.add(thep);
					}
				}
			}

			// Destroy the particles from the world
			for (Particle particle : toBeRemoved) {
				particle.destroy();
			}

			// Remove the particles that shall be destroyed
			toBeRemoved.clear();

			if (mouseActive)
				createParticles();
			
			//dbgRenderer.render(world, g.getCamera().combined);
		}
		
		g.drawSchoolLogo();
		g.drawFPS();
	}

	@Override
	public void onGameLogicUpdate() {
		super.onGameLogicUpdate();

		synchronized (w) {
			w.step(1 / 60.0f, 6, 5);
		}

	}

	void createParticles() {
		Random rand = new Random();

		synchronized (w) {
			for(int i = 0; i < CREATION_RATE; i++){
				Particle c = new Particle(position, 5, MAX_AGE + rand.nextInt(MAX_AGE / 2));
				
				// Apply a vertical force with some random horizontal component
				Vector2 force = new Vector2();			
				force.x = rand.nextFloat() * 150 * (rand.nextBoolean() == true ? -1 : 1);
				force.y = rand.nextFloat() * 150 * (rand.nextBoolean() == true ? -1 : 1);
				c.body.applyLinearImpulse(force, position, true);
			}
		}
	}
	
	@Override
	public void onDrag(int x, int y) {	
		super.onDrag(x, y);
		position.x = x;
		position.y = y;
	}
	
	@Override
	public void onClick(int x, int y, int button) {
		super.onClick(x, y, button);		
		mouseActive = true;
		position = new Vector2(x, y);
	}

	@Override
	public void onRelease(int x, int y, int button) {
		super.onRelease(x, y, button);
		position.x = x;
		position.y = y;
		mouseActive = false;
	}

	@Override
	public void onKeyDown(int keycode) {	
		super.onKeyDown(keycode);
		if(keycode == Input.Keys.PLUS){
			CREATION_RATE++;			
		}
		if(keycode == Input.Keys.MINUS){
			CREATION_RATE = CREATION_RATE > 1 ? CREATION_RATE - 1 : CREATION_RATE;
		}
		Gdx.app.log("[DemoParticlePhysics]", "Creation rate is now " + CREATION_RATE);
	}
	
	public DemoParticlePhysics(boolean onAndroid) {
		super(onAndroid);
	}

	public DemoParticlePhysics(boolean onAndroid, int x, int y) {
		super(onAndroid, x, y);
	}
	
	public static void main(String args[]) {
		new DemoParticlePhysics(false, 1000, 600);
	}
}
