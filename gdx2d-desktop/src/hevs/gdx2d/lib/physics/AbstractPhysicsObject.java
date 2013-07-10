package hevs.gdx2d.lib.physics;

import hevs.gdx2d.components.physics.PhysicsBox;
import hevs.gdx2d.components.physics.PhysicsCircle;
import hevs.gdx2d.components.physics.PhysicsStaticBox;
import hevs.gdx2d.components.physics.utils.PhysicsConstants;

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
public abstract class AbstractPhysicsObject implements ContactListener, BodyInterface{
	// A name for the object
	public String name;
	
	// Contains the body for physics simulation
	private Body body;
	
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
	public AbstractPhysicsObject(Type t, String name, Vector2 position, float width, float height, float density, float restitution, float friction, boolean isDynamic){
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
	public AbstractPhysicsObject(Type t, String name, Vector2 position, float width, float height, float density, float restitution, float friction, float angle, boolean isDynamic){
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
	private void createObject(Type t, String name, Vector2 position, float width, float height, float density, float restitution, float friction, float angle, boolean isDynamic){				
        // Conversions from pixel world to meters
		Vector2 pos = position.cpy().scl(PhysicsConstants.PIXEL_TO_METERS);
        width *= PhysicsConstants.PIXEL_TO_METERS;
        height *= PhysicsConstants.PIXEL_TO_METERS;
        
		bodyDef.position.set(pos);
        
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
		PhysicsWorld.getInstance().destroyBody(body);	
	}

	/**
	 * Makes the object react on collisions
	 */
	public void enableCollisionListener(){
		World world = PhysicsWorld.getInstance();
		world.setContactListener(this);
	}
	
	/**
	 * TODO: should this method be abstract ? Breaks the API but better for next year
	 * Function which is called when collision with this object occurs
	 * @param a
	 * @param body
	 * @param energy
	 */
	public void collision(AbstractPhysicsObject theOtherObject, float energy){				
	}

	/************************************************************************
	 * Internal functions for collisions, do not use
	 ************************************************************************/	
	@Override
	final public void beginContact(Contact contact) {		
	}		
	
	@Override
	final public void endContact(Contact contact) {
		AbstractPhysicsObject ob1 = null, ob2 = null; 		
		ob1 = (AbstractPhysicsObject) contact.getFixtureA().getBody().getUserData();
		ob2 = (AbstractPhysicsObject) contact.getFixtureB().getBody().getUserData();
		
		ob1.collision(ob2, lastCollideEnergy);
		ob2.collision(ob1, lastCollideEnergy);
		
		lastCollideEnergy = -1;
	}
	
	float lastCollideEnergy = -1;
	
	@Override
	final public void postSolve(Contact contact, ContactImpulse impulse) {
		lastCollideEnergy = impulse.getNormalImpulses()[0];
	}
	
	@Override
	final public void preSolve(Contact contact, Manifold oldManifold) {			
	}	

	/****
	 * Body method redefinition for meters / pixels
	 * Implementation of the {@link BodyInterface} interface
	 */
	@Override
	public Vector2 getBodyPosition(){
		return body.getPosition().scl(PhysicsConstants.METERS_TO_PIXELS);
	}
	
	@Override
	public float getBodyAngle(){
		return body.getAngle();
	}
	
	@Override
	public float getBodyAngleDeg(){
		return body.getAngle() * PhysicsConstants.RAD_TO_DEG;
	}
	
	@Override
	public float getBodyRadius(){
		if(f.getShape().getType() == Type.Circle){			
			return f.getShape().getRadius()*PhysicsConstants.METERS_TO_PIXELS;
		}
		else
		{			
			throw new UnsupportedOperationException("Only circle shapes have radius");
		}
	}

	@Override
	public void setBodyLinearDamping(float damping){
		body.setLinearDamping(damping);
	}
	
	@Override
	public void setBodyAngularDamping(float damping){
		body.setAngularDamping(damping);
	}

	@Override
	public void applyBodyTorque(float torque, boolean wake){
		body.applyTorque(torque, wake);
	}
	
	@Override
	public void setBodyAwake(boolean awake){
		body.setAwake(awake);
	}
	
	@Override
	public void setBodyLinearVelocity(Vector2 v){
		body.setLinearVelocity(v);
	}
	
	@Override
	public void setBodyLinearVelocity(float vx, float vy){
		body.setLinearVelocity(vx, vy);
	}
	
	@Override
	public void applyBodyAngularImpulse(float impulse, boolean wake){
		body.applyAngularImpulse(impulse, wake);
	}
	
	@Override
	public void applyBodyForce(float forceX, float forceY, float pointX,
			float pointY, boolean wake) {
		body.applyForce(forceX, forceY, pointX, pointY, wake);
	}
	
	@Override
	public void applyBodyForce(Vector2 force, Vector2 point, boolean wake) {
		body.applyForce(force, point, wake);
	}
	
	@Override
	public void applyBodyForceToCenter(float forceX, float forceY, boolean wake) {
		body.applyForceToCenter(forceX, forceY, wake);
	}

	@Override
	public void applyBodyForceToCenter(Vector2 force, boolean wake) {
		body.applyForceToCenter(force, wake);
	}
	
	@Override
	public void applyBodyLinearImpulse(float impulseX, float impulseY,
			float pointX, float pointY, boolean wake) {
		body.applyLinearImpulse(impulseX, impulseY, pointX, pointY, wake);
	}
	
	@Override
	public void applyBodyLinearImpulse(Vector2 impulse, Vector2 point,
			boolean wake) {
		body.applyLinearImpulse(impulse, point, wake);
	}

	/**
	 * Convenience method for some special operations. You should not use that normally because
	 * the dimensions are not scaled appropriately in the object itself.
	 * @return The body for the simulation
	 */	
	public Body getBody(){
		return this.body;
	}	
}
