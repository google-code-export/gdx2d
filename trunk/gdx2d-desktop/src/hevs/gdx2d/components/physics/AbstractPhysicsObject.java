package hevs.gdx2d.components.physics;

import hevs.gdx2d.components.physics.utils.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.World;

/**
 * An abstract physical object that contains everything
 * required for simulation. For concrete instances, see {@link PhysicsBox}, 
 * {@link PhysicsCircle} and {@link PhysicsStaticBox}.
 * 
 * @author Pierre-André Mudry (mui)
 * @version 1.0
 */
public abstract class AbstractPhysicsObject implements ContactListener{
	// A name for the object
	public String name;
	
	// Contains the body for physics simulation
	public Body body;
	
	// The physical characteristics of the the object, such as friction etc...
	protected Fixture f;
	
	// Reusable object for creating other objects
	static final private BodyDef bodyDef = new BodyDef();
	
	// FIXME: the objects do not react correctly on screen orientation changes...
	
	/**
	 * The abstract constructor
	 * @param t
	 * @param name
	 * @param position
	 * @param width
	 * @param height
	 * @param density
	 * @param restitution
	 * @param friction
	 * @param isDynamic
	 */
	public AbstractPhysicsObject(Type t, String name, Vector2 position, int width, int height, float density, float restitution, float friction, boolean isDynamic){
		this.name = name;
		createObject(t, name, position, width, height, density, restitution, friction, 0, isDynamic);
	}
	
	/**
	 * Another abstract constructor
	 * @param t
	 * @param name
	 * @param position
	 * @param width
	 * @param height
	 * @param density
	 * @param restitution
	 * @param friction
	 * @param angle
	 * @param isDynamic
	 */
	public AbstractPhysicsObject(Type t, String name, Vector2 position, int width, int height, float density, float restitution, float friction, float angle, boolean isDynamic){
		this.name = name;
		createObject(t, name, position, width, height, density, restitution, friction, angle, isDynamic);
	}
	
	/**
	 * Create an object
	 * @param t
	 * @param name
	 * @param position
	 * @param width
	 * @param height
	 * @param isDynamic
	 */
	private void createObject(Type t, String name, Vector2 position, int width, int height, float density, float restitution, float friction, float angle, boolean isDynamic){				
        bodyDef.position.set(position);
        
		if(isDynamic)
			bodyDef.type = BodyType.DynamicBody;
		else
			bodyDef.type = BodyType.StaticBody;    

		// The shape used for collisions in physics
		Shape s;
		
		if(t == Type.Circle){
			s = new CircleShape();			
			s.setRadius(width);
		}
		else
		{
			PolygonShape p = new PolygonShape();			
			p.setAsBox(width, height, new Vector2(0,0), angle);
			s = p;
		}
					
        World world = PhysicsWorld.getInstance();
		body = world.createBody(bodyDef);		
						
		createFixture(s, density, restitution, friction);
		
		// Destroy the shape because we don't need it anymore (JNI side)
		s.dispose();
		body.setUserData(this);		
	}
	
	/**
	 * Creates the fixture (collision information) attached to the object
	 * @param s The shape we want for collision
	 * @param density The density of the object, in kg/ms² 
	 * @param restitution For elastic collisions
	 * @param friction Coulomb friction, does not work for circles
	 */
	protected void createFixture(Shape s, float density, float restitution, float friction){		
		FixtureDef def = new FixtureDef();				
		def.density = density;
		def.restitution = restitution;
		def.friction = friction;		
		def.shape = s;		
						
		f = body.createFixture(def);
		
		// Makes things stop slowly, always
		body.setLinearDamping(0.001f);
	}
	
	/**
	 * Should be called before destroying the object
	 */
	public void destroy(){		
		synchronized (PhysicsWorld.getInstance()) {
			PhysicsWorld.getInstance().destroyBody(body);	
		}				
	}

	/**
	 * Called periodically
	 */
	public void step(){		
	}
			
	public void enableCollisionListener(){
		World world = PhysicsWorld.getInstance();
		world.setContactListener(this);
	}
	
	/**
	 * Function which is called when collision with this object occurs
	 * @param a
	 * @param body
	 * @param energy
	 */
	public void collision(AbstractPhysicsObject theOtherObject, float energy){				
	}
	
	@Override
	final public void beginContact(Contact contact) {		
	}		
	
	@Override
	final public void endContact(Contact contact) {		
	}
	
	@Override
	final public void postSolve(Contact contact, ContactImpulse impulse) {
		AbstractPhysicsObject ob1 = null, ob2 = null; 		
		ob1 = (AbstractPhysicsObject) contact.getFixtureA().getBody().getUserData();
		ob2 = (AbstractPhysicsObject) contact.getFixtureB().getBody().getUserData();
		
		float energy = impulse.getNormalImpulses()[0];
		ob1.collision(ob2, energy);
		ob2.collision(ob1, energy);
	}
	
	@Override
	final public void preSolve(Contact contact, Manifold oldManifold) {			
	}	
	
}
