package hevs.gdx2d.lib;

import hevs.gdx2d.components.bitmaps.BitmapImage;
import hevs.gdx2d.components.graphics.Polygon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * A game graphics implementation for LibGDX based on the
 * interface of FunGraphics for INF1 class
 * 
 * @author Pierre-André Mudry (mui)
 * @author Nils Chatton (chn)
 * @version 1.51
 */
public class GdxGraphics implements Disposable
{
	/**
	 * For camera operations
	 */
	protected OrthographicCamera camera, fixedcamera;	
	
	public ShapeRenderer shapeRenderer;
	public SpriteBatch spriteBatch;
	protected Color currentColor = Color.WHITE;
	protected Color backgroundColor = Color.BLACK;

	// The standard font
	protected BitmapFont font;
	
	// For optimizing the current rendering mode and minimizing the number of
	// calls to begin() and end() in spriteBatch
	private enum t_rendering_mode {SHAPE_FILLED, SHAPE_LINE, SHAPE_POINT, SPRITE}; 
	private t_rendering_mode rendering_mode = t_rendering_mode.SPRITE; 

	// For sprite-based circles
	final Sprite circleSprite;					
	
	// For sprite-based logo
	final protected Texture logoTex = new Texture(Gdx.files.internal("data/logo_hes.png"));	
	final protected Texture circleTex = new Texture(Gdx.files.internal("data/circle.png"));
	
	/**
	 * When rendering with other methods than the one present here (for instance
	 * when using box2dlight), call this method to render shapes correctly
	 */
	public void resetRenderingMode(){
		checkmode(t_rendering_mode.SPRITE);
		checkmode(t_rendering_mode.SHAPE_LINE);
	}
	
	public GdxGraphics(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, OrthographicCamera camera) {
		this.shapeRenderer = shapeRenderer;
		this.spriteBatch = spriteBatch;
		this.font = new BitmapFont();
		this.camera = camera;
			
		// A camera that never moves
		this.fixedcamera = new OrthographicCamera();
		fixedcamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Enable alpha blending for shape renderer
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			
		// Create a default circleSprite for filled circles
		circleSprite = new Sprite(circleTex, 0, 0, 128, 128);
	}
		
	@Override
	public void dispose() {
		logoTex.dispose();
		circleTex.dispose();		
		font.dispose();
		spriteBatch.dispose();
	}
	
	/**
	 * Draws frame per second (FPS) information
	 */
	public void drawFPS(){
		spriteBatch.setProjectionMatrix(fixedcamera.combined);
		Color oldColor = font.getColor();
		font.setColor(Color.WHITE);		
		drawString(5, 15, "FPS: " + Gdx.graphics.getFramesPerSecond());
		font.setColor(oldColor);
		spriteBatch.setProjectionMatrix(camera.combined);
	}
	
	/**
	 * Draws school logo
	 */
	public void drawSchoolLogo(){
		checkmode(t_rendering_mode.SPRITE);
		spriteBatch.setProjectionMatrix(fixedcamera.combined);
		spriteBatch.draw(logoTex, getScreenWidth() - logoTex.getWidth(), 0);
		spriteBatch.setProjectionMatrix(camera.combined);
	}
	
	public void checkmode(t_rendering_mode mode) {
		if(mode == rendering_mode)
			return;
	
		if(rendering_mode != t_rendering_mode.SPRITE){
			shapeRenderer.end();
		}
	
		if(rendering_mode == t_rendering_mode.SPRITE)
		{
			spriteBatch.end();
		}
		
		switch (mode) {
		case SHAPE_LINE:
			shapeRenderer.begin(ShapeType.Line);
			rendering_mode = t_rendering_mode.SHAPE_LINE;
			break;

		case SHAPE_FILLED:
			shapeRenderer.begin(ShapeType.Filled);
			rendering_mode = t_rendering_mode.SHAPE_FILLED;
			break;
			
		case SHAPE_POINT:
			shapeRenderer.begin(ShapeType.Point);
			rendering_mode = t_rendering_mode.SHAPE_POINT;
			break;
			
		case SPRITE:	
			spriteBatch.begin();
			rendering_mode = t_rendering_mode.SPRITE;
			break;
		}	
		
	}

	/**
	 * Draws the school logo in the upper right corner of the screen
	 */
	public void drawSchoolLogoUpperRight(){
		checkmode(t_rendering_mode.SPRITE);
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		spriteBatch.setProjectionMatrix(fixedcamera.combined);
		spriteBatch.draw(logoTex, width - logoTex.getWidth(), height - logoTex.getHeight());
		spriteBatch.setProjectionMatrix(camera.combined);
	}
	
	/**
	 * A very simple way to change line width for shape operators (line, ...)
	 * @param width
	 */
	public void setPenWidth(float width){
		/**
		 * TODO : Line capping is not working nicely for non vertical lines in OpenGL.
		 * This is a known problem but at the moment we have decided not to implement
		 * any solution
		 */
		// Mode should be put on sprite, this is not a mistake
		checkmode(t_rendering_mode.SPRITE);
		Gdx.gl20.glLineWidth(width);
	}	
		
	/**
	 * Gets the height of the screen
	 */
	public int getScreenHeight() {	
		return Gdx.graphics.getHeight();		
	}
	
	/**
	 * Gets the width of the rendering surface
	 */
	public int getScreenWidth() {	
		return Gdx.graphics.getWidth();
	}
	
	/**
	 * @param x 
	 * @param y
	 * @param w
	 * @param h
	 * @param angle in degrees
	 */
	public void drawRectangle(float x, float y, float w, float h, float angle) {
		checkmode(t_rendering_mode.SHAPE_LINE);
		shapeRenderer.setColor(currentColor);
//		shapeRenderer.identity();
		shapeRenderer.translate(x + w / 2, y + w / 2, 0);		
		if(angle != 0)
			shapeRenderer.rotate(0, 0, 1, angle);		
		shapeRenderer.rect(-w / 2, -w / 2, w, h);
	}

	/**
	 * Clears the screen black
	 */
	public void clear() {
		clear(Color.BLACK);
	}

	boolean first = true;
	
	/**
	 * Clears the screen with a given color
	 * @param c
	 */
	public void clear(Color c) {
		Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(!first && rendering_mode == t_rendering_mode.SPRITE)
			spriteBatch.end();
		else
			first = !first;
		
		if(rendering_mode == t_rendering_mode.SPRITE)
			spriteBatch.begin();
	}

	/**
	 * Sets the current color used for graphics drawing
	 * @param c
	 */
	public void setColor(Color c) {
		currentColor = c;
	}

	/**
	 * Sets the color of a pixel using the current color ({@link #currentColor}
	 * @param x
	 * @param y
	 */
	public void setPixel(float x, float y) {
		checkmode(t_rendering_mode.SHAPE_POINT);
		shapeRenderer.identity();
		shapeRenderer.setColor(currentColor);
		shapeRenderer.point(x, y, 0);
	}

	/**
	 * Sets the color of a pixel using a color
	 * @param x
	 * @param y
	 * @param c
	 */
	public void setPixel(float x, float y, Color c) {
		checkmode(t_rendering_mode.SHAPE_POINT);
		shapeRenderer.identity();
		shapeRenderer.setColor(c);
		shapeRenderer.point(x, y, 0);
	}
		
	public void clearPixel(float x, float y) {
		checkmode(t_rendering_mode.SHAPE_POINT);
		shapeRenderer.identity();
		shapeRenderer.setColor(backgroundColor);
		shapeRenderer.point(x, y, 0);
	}

	public void drawLine(float p1x, float p1y, float p2x, float p2y) {
		checkmode(t_rendering_mode.SHAPE_LINE);
		shapeRenderer.setColor(currentColor);
		shapeRenderer.line(p1x, p1y, p2x, p2y);		
	}

	public void drawLine(float p1x, float p1y, float p2x, float p2y, Color c) {
		shapeRenderer.setColor(c);
		drawLine(p1x, p1y, p2x, p2y);		
	}

	public void drawFilledRectangle(float x, float y, float w, float h, float angle) {
		checkmode(t_rendering_mode.SHAPE_FILLED);
		shapeRenderer.identity();			
		shapeRenderer.translate(x + w / 2, y + w / 2, 0);
		shapeRenderer.rotate(0, 0, 1, angle);
		shapeRenderer.rect(-w / 2, -w / 2, w, h);		
	}
	
	public void drawFilledRectangle(float x, float y, float w, float h, float angle, Color c) {
		shapeRenderer.setColor(c);		
		drawFilledRectangle(x, y, w, h, angle);
	}

	public void drawCircle(float centerX, float centerY, float radius, Color c) {
		shapeRenderer.setColor(c);
		drawCircle(centerX, centerY, radius);
	}
	
	public void drawCircle(float centerX, float centerY, float radius) {
		checkmode(t_rendering_mode.SHAPE_LINE);		
		shapeRenderer.identity(); // Useless ?		
		shapeRenderer.circle(centerX, centerY, radius);
	}

	public void drawFilledCircle(float centerX, float centerY, float radius, Color c) {		
		if(radius > 64)
		{
			checkmode(t_rendering_mode.SHAPE_FILLED);
				shapeRenderer.setColor(c);
				shapeRenderer.identity();
				shapeRenderer.circle(centerX, centerY, radius);
		}
		else
		{		
			checkmode(t_rendering_mode.SPRITE);
			// Use a circleSprite-based approach to rendering circle			
			circleSprite.setScale(radius / 64.0f);
			circleSprite.setPosition(centerX-64, centerY-64);
			circleSprite.setColor(c);
			circleSprite.draw(spriteBatch);
		}
	}
	
	public void drawFilledBorderedCircle(float centerX, float centerY, float radius, Color inner, Color outer) {		
			checkmode(t_rendering_mode.SPRITE);			

			// This was slow...
//			// Use a circleSprite-based approach to rendering circle			
//			circleSprite.setPosition(centerX-64, centerY-64);
//			circleSprite.setScale(radius / 64.0f);
//			circleSprite.setColor(outer);
//			circleSprite.draw(spriteBatch);
//			circleSprite.setScale((radius - borderWidth) / 64.0f);
//			circleSprite.setColor(inner);
//			circleSprite.draw(spriteBatch);
			
			drawFilledCircle(centerX, centerY, radius, inner);
			drawCircle(centerX, centerY, radius, outer);
	}


	public void drawString(float posX, float posY, String str) {
		checkmode(t_rendering_mode.SPRITE);
		font.drawMultiLine(spriteBatch, str, posX, posY);
	}
	
	/**
	 * Draws a string with a specific font
	 * @param posX
	 * @param posY
	 * @param str
	 * @param f
	 */
	public void drawString(float posX, float posY, String str, BitmapFont f){
		checkmode(t_rendering_mode.SPRITE);
		f.drawMultiLine(spriteBatch, str, posX, posY);
	}
	
	/**
	 * Draws a string in the middle of the screen with a specific font
	 * @param posY
	 * @param str
	 * @param f
	 */
	public void drawStringCentered(float posY, String str, BitmapFont f){
		float w = f.getBounds(str).width;
		drawString((getScreenWidth() - w )/ 2.0f, posY, str, f);
	}
	
	
	/**
	 * Draws an image in the background that will not move with the camera
	 * @param t 
	 * @param i x coordinate in the screen space
	 * @param j y coordinate in the screen space
	 */
	public void drawBackground(BitmapImage t, float i, float j){
		drawBackground(t.getImage(), i, j);
	}
	
	/**
	 * Draws a texture in background that will not move with the camera
	 * @param t
	 * @param i x coordinate in the screen space
	 * @param j y coordinate in the screen space
	 */
	public void drawBackground(Texture t, float i, float j){
		checkmode(t_rendering_mode.SPRITE);
		spriteBatch.setProjectionMatrix(fixedcamera.combined);		
		spriteBatch.disableBlending();
		spriteBatch.draw(t, i, j);
		spriteBatch.enableBlending();
		spriteBatch.setProjectionMatrix(camera.combined);
	}

	/**
	 * Draws a picture at position ({@code posX, posY}).  
	 */
	public void drawPicture(float posX, float posY, BitmapImage bitmap) {						
		drawTransformedPicture(posX, posY, 0, 1, bitmap);		
	}
	
	/**
	 * Draws a picture at position ({@code posX, posY}). Center of rotation is located in the
	 * middle of the image. 
	 */
	public void drawTransformedPicture(float posX, float posY, float angle, float scale, BitmapImage bitmap) {
		drawTransformedPicture(posX, posY, bitmap.getImage().getWidth() / 2, bitmap.getImage().getHeight() / 2, angle, scale, bitmap);
	}
	
	/**
	 * Draws a picture at position ({@code posX, posY}) with a fixed width and height (not the one of the {@link BitmapImage} itself).
	 */
	public void drawTransformedPicture(float posX, float posY, float angle, float width, float height, BitmapImage bitmap) {
		checkmode(t_rendering_mode.SPRITE);
		spriteBatch.draw(bitmap.getRegion(), (float)posX-width, (float)posY-height, width, height, width*2, height*2, 1.0f, 1.0f,(float) angle);
	}
	
	/**
	 * Draws a picture at position ({@code posX, posY}) with a selectable center of rotation which is located
	 * at position offset by ({@code centerX, centerY}).
	 * 
	 * @param posX
	 * @param posY
	 * @param centerX
	 * @param centerY
	 * @param angle
	 * @param scale
	 * @param bitmap
	 */
	public void drawTransformedPicture(float posX, float posY, float centerX, float centerY, float angle, float scale, BitmapImage bitmap) {
		checkmode(t_rendering_mode.SPRITE);
		spriteBatch.draw(bitmap.getRegion(), posX-bitmap.getRegion().getRegionWidth()/2, posY-bitmap.getRegion().getRegionHeight()/2, centerX, centerY, (float) bitmap.getImage().getWidth(), (float) bitmap.getImage().getHeight(), (float) scale, (float) scale,(float) angle);
	}

	/**
	 * Draws a picture at position {@code pos} with a selectable alpha (transparency). See {@link #drawAlphaPicture(float, float, float, float, float, float, float, BitmapImage)}
	 * @param pos
	 * @param alpha
	 * @param img
	 */
	public void drawAlphaPicture(Vector2 pos, float alpha, BitmapImage img){
		drawAlphaPicture(pos.x, pos.y, alpha, img);
	}

	public void drawAlphaPicture(float posX, float posY, float scale, float alpha, BitmapImage img){
		drawAlphaPicture(posX, posY, img.getImage().getWidth() / 2, img.getImage().getHeight() / 2, 0, 1.0f, alpha, img);
	}
	
	public void drawAlphaPicture(float posX, float posY, float alpha, BitmapImage img){
		drawAlphaPicture(posX, posY, img.getImage().getWidth() / 2, img.getImage().getHeight() / 2, 0, 1.0f, alpha, img);
	}
	
	public void drawAlphaPicture(float posX, float posY, float centerX, float centerY, float angle, float scale, float alpha, BitmapImage img){		
		Color old = spriteBatch.getColor();
		spriteBatch.setColor(1.0f, 1.0f, 1.0f, alpha);
		drawTransformedPicture(posX, posY, angle, scale, img);
		spriteBatch.setColor(old);
	}
	
	public void drawPolygon(Polygon p) {
		checkmode(t_rendering_mode.SHAPE_LINE);
		shapeRenderer.setColor(currentColor);
		shapeRenderer.identity();		
		shapeRenderer.polygon(p.getVertices());
	}

	public void drawFilledPolygon(Polygon polygon, Color c) {		
		float[] vertices =  polygon.getEarClippedVertices();
		checkmode(t_rendering_mode.SHAPE_FILLED);
		shapeRenderer.setColor(c);
		shapeRenderer.identity();

		for (int i = 0; i < vertices.length; i+=6) {
			shapeRenderer.triangle(vertices[i], vertices[i+1], vertices[i+2], vertices[i+3], vertices[i+4], vertices[i+5]);
		}		
	}
	
	/****************************************************
	 * Camera stuff
	 ****************************************************/
	public OrthographicCamera getCamera(){
		return camera;
	}
	
	/**
	 * Move the camera to a fixed position
	 * @param x
	 * @param y
	 */
	public void moveCamera(float x, float y){
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(x, y);		
	}
	
	/**
	 * Puts the camera at its original location
	 */
	public void resetCamera(){
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());				
	}
	
	/**
	 * Displace the camera by a value
	 * @param dx
	 * @param dy
	 */
	public void scroll(float dx, float dy){
		camera.translate(dx, dy);
		camera.update();
	}
	
	/**
	 * Zooms in and out the camera, 1 is the default zoom-level
	 * @param factor
	 */
	public void zoom(float factor){
		camera.zoom = factor;
		camera.update();
	}
}