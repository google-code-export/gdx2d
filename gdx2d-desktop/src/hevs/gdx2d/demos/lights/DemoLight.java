package hevs.gdx2d.demos.lights;

import hevs.gdx2d.components.physics.PhysicsCircle;
import hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries;
import hevs.gdx2d.components.physics.utils.PhysicsWorld;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.PortableApplication;

import java.util.ArrayList;
import java.util.Random;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Demonstrates the usage of shadows and lights in GDX2D
 * @author Pierre-André Mudry (mui)
 * @version 1.04
 */
public class DemoLight extends PortableApplication {

	// Physics-related attributes
	RayHandler rayHandler;
	World world;
	Box2DDebugRenderer debugRenderer;
	ArrayList<Body> list = new ArrayList<Body>();	
	
	// Light related attributes
	PointLight p;
	ConeLight c1, c2;
	
	private boolean firstRun = true;
	int width, height;
	
	public DemoLight(boolean onAndroid) {
		super(onAndroid);
	}

	@Override
	public void onInit() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		setTitle("Shadows and lights, mui 2013");
		
		world = PhysicsWorld.getInstance();
		world.setGravity(new Vector2(0, 0));
		
		// The light manager
		RayHandler.setColorPrecisionMediump();
		rayHandler = new RayHandler(world);

		// This is the light controlled by the mouse click and drag
		p = new PointLight(rayHandler, 200, Color.YELLOW, 400,
				this.getWindowWidth() / 2 - 50,
				this.getWindowHeight() / 2 + 150);
		
		p.setDistance(700f);
		p.setColor(new Color(0.9f, 0f, 0.9f, 0.9f));
		p.setActive(false);
 
		// The two light cones that are always present 
		c1 = new ConeLight(rayHandler, 200, new Color(1, 1, 1, 0.92f), 800, 0.2f * width, 0.9f * height, 270, 40);		
		c2 = new ConeLight(rayHandler, 200, new Color(0.1f, 0.1f, 1, 0.92f), 800, 0.8f * width, 0.9f * height, 270, 40);
				
		rayHandler.setCulling(true);
		rayHandler.setShadows(true);
		rayHandler.setBlur(true);		
		rayHandler.setAmbientLight(0.3f);		

		new PhysicsScreenBoundaries((int)width, (int)height);
		createRandomCircles(10);
		
		debugRenderer = new Box2DDebugRenderer();
	}

	/**
	 * Creates n physics objects that will then cast shadows
	 * @param n
	 */
	protected void createRandomCircles(int n) {

		Random r = new Random();

		while (n > 0) {
			Vector2 position = new Vector2((float) (width * Math.random()),(float) (height * Math.random()));
			PhysicsCircle circle = new PhysicsCircle("circle", position, 10, 1.2f, 1, 0.01f);
			circle.body.setLinearVelocity(r.nextFloat() * 6, r.nextFloat() * 6);
			n--;
			
			// Only add the body, the rest is useless
			list.add(circle.body);
		}
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		if (firstRun) {
			rayHandler.setCombinedMatrix(g.getCamera().combined);
			firstRun = false;
		}

		g.clear();
	
		// Draw all the lights
		synchronized (world) {
//			debugRenderer.render(world, g.getCamera().combined);
			
			// Render the blue spheres
			for (Body b : list) {																		
				g.drawFilledCircle((int)b.getPosition().x, (int)b.getPosition().y, 12, Color.BLUE);
			}
			
			// Render the lights
			rayHandler.updateAndRender();					
		}
		
		g.drawSchoolLogo();
		g.drawFPS();				
	}

	@Override
	public void onClick(int x, int y, int button) {
		if(button == Input.Buttons.RIGHT)
		{
			c1.setActive(!c1.isActive());
			c2.setActive(!c2.isActive());
		}
		
		if(button == Input.Buttons.LEFT)
		{
			p.setActive(true);
			p.setPosition(x, y);
		}
	}
	
	@Override
	public void onDrag(int x, int y) {		
		p.setPosition(x, y);
	}
	
	@Override
	public void onRelease(int x, int y, int button) {	
		p.setActive(false);
	}

	@Override
	public void onGameLogicUpdate() {
		synchronized (world) {
			world.step(1 / 50f, 7, 3);	
		}					
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DemoLight(false);
	}

}
