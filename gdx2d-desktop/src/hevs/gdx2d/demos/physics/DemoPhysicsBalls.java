package hevs.gdx2d.demos.physics;

import hevs.gdx2d.components.bitmaps.BitmapImage;
import hevs.gdx2d.components.physics.utils.PhysicsWorld;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.PortableApplication;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Orientation;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * A demo for physics, based on box2d
 * 
 * @author Pierre-André Mudry (mui)
 * @version 1.0
 */
public class DemoPhysicsBalls extends PortableApplication {

	ConcurrentLinkedQueue<Body> list = new ConcurrentLinkedQueue<Body>();	
	
	// A world with gravity, pointing down
	World world = PhysicsWorld.getInstance();
	Box2DDebugRenderer debugRenderer;
	BitmapImage img;

	static final int BOX_VELOCITY_ITERATIONS = 7;
	static final int BOX_POSITION_ITERATIONS = 4;	
	
	boolean hasAccelerometers;
	
	public DemoPhysicsBalls(boolean onAndroid) {
		super(onAndroid);
	}

	@Override
	public void onInit() {			
		setTitle("Physics demo with box2d, mui 2013");
		hasAccelerometers = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
		
		world.setGravity(new Vector2(0, -15));
		
		// Create the ground
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vector2(0, 10));
		Body groundBody = world.createBody(groundBodyDef);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(getWindowWidth() * 2, 10);
		groundBody.createFixture(groundBox, 0.0f);

		// Left wall
		groundBodyDef.position.set(new Vector2(0, 120));
		Body leftBody = world.createBody(groundBodyDef);
		PolygonShape leftBox = new PolygonShape();
		leftBox.setAsBox(20, 250);
		leftBody.createFixture(leftBox, 0.0f);

		// Right wall
		groundBodyDef.position.set(new Vector2(getWindowWidth(), 120));
		Body rightBody = world.createBody(groundBodyDef);
		PolygonShape rightBox = new PolygonShape();
		rightBox.setAsBox(20, 250);
		rightBody.createFixture(rightBox, 0.0f);
	
		// Used to display debug information about the physics
		debugRenderer = new Box2DDebugRenderer();
		img = new BitmapImage("data/soccer.png");
	}

	/**
	 * Adds a ball at a given location
	 * @param x
	 * @param y
	 */
	public void addBall(int x, int y) {				
		// If there exists enough ball already, remove the oldest one
		if(list.size() > 50){
			Body b = list.poll();
			world.destroyBody(b);			
		}
		
		// The ball that falls
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);

		// Used for determining the physical characteristics of the ball
		CircleShape dynamicCircle = new CircleShape(); // it is round
		float size = (float) ((Math.random() * 15.0f)) + 15f;
		dynamicCircle.setRadius(size);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicCircle;
		fixtureDef.density = 5.5f;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0.75f;

		// Add the ball to the worlds
		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.setUserData(new Float(size));
		
		// Add the ball to the list of existing balls
		list.add(body);
	}
	
	@Override
	public void onClick(int x, int y, int button) {
		super.onClick(x, y, button);

		if (button == Input.Buttons.LEFT)
			addBall(x, y);			
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {
		g.clear();		

		// For every body in the world
		for(int i = 0; i < list.size(); i++){
			Body b = list.poll();
			Float s = (Float) b.getUserData();			
			g.drawTransformedPicture((int) b.getPosition().x, (int) b.getPosition().y, b.getAngle() * 57.2957795f, s, s, img);
			b.setAwake(true);
			list.add(b);			
		}

		PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime());
		
		g.drawSchoolLogoUpperRight();
		g.drawFPS();
	}

	// For low-pass filtering accelerometer
	private double smoothedValue = 0;
	private final double SMOOTHING = 30; // This value changes the dampening effect of the low-pass	
	
	/**
	 * Called periodically
	 */
	@Override
	public void onGameLogicUpdate() {		
	
		if(hasAccelerometers){
			// On tablet, orientation is different than on phone
			Orientation nativeOrientation = Gdx.input.getNativeOrientation();
			
			float accel;
			
			if(nativeOrientation == Orientation.Landscape)
				accel = -Gdx.input.getAccelerometerY();
			else
				accel = Gdx.input.getAccelerometerX();																			
			
			// Low pass filtering of the value
			smoothedValue += (accel - smoothedValue) / SMOOTHING;
			world.setGravity(new Vector2(-(float)(smoothedValue), -10));				
		}
	}

	public static void main(String[] args) {
		new DemoPhysicsBalls(false);
	}
}
