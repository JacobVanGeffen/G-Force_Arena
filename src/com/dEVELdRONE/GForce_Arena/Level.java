package com.dEVELdRONE.GForce_Arena;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*IMPORTANT:
 * init must be called once and only once, called from the levelGenerator
 * levelGenerator must be called from the activity
 * public static int in LevelActivity will denote which level to request, 
 * set by LevelSelection by which level is chosen
 * 
 * Drawing:
 * x = screen.getDeviceX(x), same for y
 * width = width*screen.getDrawingWidthScale(), same for height
 * **these are what should be put in for drawing, DO NOT actually assign these values
 * Coordinates assigned are in respect to the (0, 0) of the entire play screen
 * 
 * Just for clarification, other classes for specific levels are not intended to be made, 
 * they are set up in the levelGenerator
 * However, subclasses of Level may be made for a different variation of levels
 * that include other specialized particles and things of this nature
 * 
 * Will have time limit to get certain % of particles into the goal
 */

/*TODO:
 * create onPause and onResume methods - not finished
 * create class for pause/play button (w/ onTouch), and a pause menu
 * fix putting particles in goal (error occurs when in goal)
 */

public class Level extends Universe implements OnTouchListener{
	
	protected Animation blackhole_rotation;
	protected Animation sun_rotation;
	protected SunParticle[] suns;
	private SunParticle movingSun;
	protected BlackholeParticle[] blackholes;
	protected HoldingArea box;
	protected Goal goal;
	protected Screen screen;
	protected PauseMenu menu;
	private Paint paint = new Paint();
	private Context context;
	private Dialogue dialogue;
	protected boolean started, running, alive;
	public boolean showPaused;
	//might move static variables to their own class
	public static float deviceHeight, deviceWidth;
	private float pointer1x, pointer1y, pointer2x, pointer2y;
	private int levelNumber;
	private int percentIn;
	private int timeLeft; //milliseconds
	private float previousDist, previousScale;
	
	public Level(Context context){
		super(context);
		
		this.context = context;
		holder = getHolder();
		
		screen = new Screen(0, 0, 960, 540); //change to new Screen(0, 0, 960, 540);
		
		goal = new Goal(BitmapFactory.decodeResource(getResources(), R.drawable.goal));

		menu = new PauseMenu(deviceWidth, deviceHeight);
		
		drawer = new Thread(this);
		
		dialogue = new Dialogue();
		
		setOnTouchListener(this);
		
		started = false;
		showPaused = false;
		
		percentIn = 0;
		timeLeft = 60000;
		previousDist = 0;
		levelNumber = -1;
	}
	
	protected void setLevelNumber(int l){
		//used for read/write file
		levelNumber = l;
	}
	
	//might move levelGenerator and init/setup methods to their own class
	//** init = mandatory, setup = optional
	
	protected void init(int suns, int blackholes, 
			float goalXloc, float goalYloc,
			float holdingAreaXloc, float holdingAreaYloc,
			float holdingAreaWidth, float holdingAreaHeight,
			int howmanyParticles, String wallType){
		
		initSuns(suns);
		initBlackholes(blackholes);
		initHoldingArea(holdingAreaXloc, holdingAreaYloc, holdingAreaWidth, holdingAreaHeight);
		setupParticles(howmanyParticles, wallType);
		
		goal.setLocation(goalXloc, goalYloc);
		
	}
	
	protected void initSuns(int suns){
		this.suns = new SunParticle[suns];
		
		for(int a=0; a<suns; a++)
			this.suns[a] = new SunParticle(0, 0, 500, this);
	}
	
	protected void initBlackholes(int blackholes){
		this.blackholes = new BlackholeParticle[blackholes];

		for(int a=0; a<blackholes; a++)
			this.blackholes[a] = new BlackholeParticle(0, 0, 500, this);
	}
	
	protected void initHoldingArea(float holdingAreaXloc, float holdingAreaYloc, 
			float holdingAreaWidth, float holdingAreaHeight){
		box = new HoldingArea(holdingAreaXloc, holdingAreaYloc, holdingAreaWidth, holdingAreaHeight);
	}
	
	protected void setupSun(int index, float x, float y, float mass){
		try{
			suns[index].setLocation(x, y);
			suns[index].setMass(mass);
			addGravityParticle(suns[index]);
		}catch(IndexOutOfBoundsException e){}
	}
	
	protected void setupBlackhole(int index, float x, float y, float mass){
		try{
			blackholes[index].setLocation(x, y);
			blackholes[index].setMass(mass);
			addGravityParticle(blackholes[index]);
		}catch(IndexOutOfBoundsException e){}
	}
	
	protected void setupScreen(float scale){
		screen.expandSetup(scale);
	}
	
	protected void setupParticles(int howmany, String wallType){ //particle type is always metro
		addParticles(howmany, box.x, box.y, box.x+box.width, box.y+box.height);
		Particle.setWalls(wallType);
	}
	
	protected void setupTime(int milliseconds){
		timeLeft = milliseconds;
	}
	
	protected void setupDialogue(String message){
		dialogue = new Dialogue(message);
	}
	
	protected void setupDialogue(String message, int milliseconds){
		dialogue = new Dialogue(message, milliseconds);
	}
	
	protected void setupDialogue(String message, float x, float y){
		dialogue = new Dialogue(message, x, y);
	}
	
	protected void setupDialogue(String message, int milliseconds, float x, float y){
		dialogue = new Dialogue(message, milliseconds, x, y);
	}
	
	//Move file stuff to its own class
	
	public Level readLevel(Context context, String file){
		
		Level level = new Level(context);
		
		
		return level;
	}
	
	public void readLevel(Level level){
		
	}
	
	public String writeLevel(Level level){
		
		String levelFile = "[Level: ";
		
		return levelFile;
	}
	
	public void startGame(){
		box.open();
		new Particle().move();
		started = true;
		running = true;
	}
	
	public void reset(){
		Intent i = new Intent("com.dEVELdRONE.GForce_Arena.LEVELACTIVITY");
		context.startActivity(i);
	}
	
	public void recycle(){
		for(GravityParticle g : blackholes)
			g.recycle();
		for(GravityParticle g2 : suns)
			g2.recycle();
	}
	
	public void putParticleInGoal(Particle p){
		if(goal.containsParticle(p)){
			p.isInGoal = true;
			percentIn++;
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
				
		int action = event.getAction();
        int pointerCount = event.getPointerCount();
        
        if((running || !started) && !showPaused){
        
	        switch(action & MotionEvent.ACTION_MASK){
	        
	        case MotionEvent.ACTION_DOWN:
	        	movingSun = null;
	        	
	        	for(SunParticle s : suns){
	        		if(s.containsPoint(screen.getScreenX(event.getX()), screen.getScreenY(event.getY()))){
	        			movingSun = s;
	        			Log.d("onTouch", "Sun is touched");
	        		}
	        	}
	        	
	        	pointer1x = event.getX();
	        	pointer1y = event.getY();
	        	
	        	break;
	        	
	        case MotionEvent.ACTION_POINTER_DOWN:
	        	pointer2x = event.getX(1);
	        	pointer2y = event.getY(1);
	        	
	        	if(pointerCount==2){
	        		if(!started)
	        			startGame();
	        		else if(running)
	        			onGamePause();
	        		else
	        			onGameResume();
	        	}
	        	
	        	
	        	break;
	        	
	        case MotionEvent.ACTION_UP:
	        	pointer1x = pointer2x;
	        	pointer1y = pointer2y;
	        	pointer2x = pointer2y = -1;
	        	previousScale = previousDist = 0;
	        	movingSun = null;
	        	break;
	        	
	        case MotionEvent.ACTION_POINTER_UP:
	        	if(pointerCount<2)
	        		pointer2x = pointer2y = -1;
	        	previousScale = previousDist = 0;
	        	movingSun = null;
	        	break;
	        	
	        case MotionEvent.ACTION_MOVE:
	        	float N1x = screen.getScreenX(event.getX()), N1y = screen.getScreenY(event.getY()),
	        		O1x = screen.getScreenX(pointer1x), O1y = screen.getScreenY(pointer1y);
	        	
	        	if(movingSun!=null && pointerCount == 1 && !started){
	        		        		
	        		if(pointer1x > 0 )
	        			movingSun.move(N1x - O1x, N1y - O1y);
	        		
	        		if(goal.containsSun(movingSun))
	        			movingSun.move(O1x - N1x, O1y - N1y);
	        		
	        		pointer1x = event.getX();
	        		pointer1y = event.getY();
	        		
	        	}else if(pointerCount==1 && event.getPointerId(0) == 0){
	        		
	        		screen.move(O1x - N1x, O1y - N1y);
	        		
	        		try{
	        			pointer1x = event.getX();
	        			pointer1y = event.getY();
	        		}catch(Exception e){}
	        		
	        	}
	        		
	        	break;
	        
	        }
	        
        }
        
        else menu.onTouch(event);
        
		return true;
	}
	
	//create blending effect w/ color particles when entering goal, sun, blackhole, etc.
	//change alpha or change certain rgb values
	protected void onDraw(Canvas canvas) {
		
		canvas.drawColor(Color.BLACK);
		
		if(!started)
			box.draw(canvas);
		
		if(running || (!started && !showPaused)){
			paint.setAlpha(255);
		}else{
			paint.setAlpha(100);
			goal.setAlpha(50);
		}
		
		goal.draw(canvas);
		
		for(SunParticle p : suns){
			if(running || (!started && !showPaused)){
				p.setAlpha(255);
			}else{
				p.setAlpha(100);
			}
			
			p.draw(canvas);
		}
				
		for(BlackholeParticle p : blackholes){
			if(running || (!started && !showPaused)){
				p.setAlpha(255);
			}else{
				p.setAlpha(100);
			}
			
			p.draw(canvas);
			
		}
		
		for(Particle p : matter){
			if(!p.isInGoal){
				canvas.drawPoint(screen.getDeviceX(p.xloc), screen.getDeviceY(p.yloc), paint);
				
				if(running || !started){
					p.addPath(new android.graphics.Point((int)p.getX(), (int)p.getY()));
				
					while(p.getPath().size()>4) 
						p.removePath(0);
				}
				
				if(p.getPath().size() > 0)
					canvas.drawLine(screen.getDeviceX(p.getX()), screen.getDeviceY(p.getY()), 
							screen.getDeviceX(p.getPath().get(p.getPath().size()-1).x), 
							screen.getDeviceY(p.getPath().get(p.getPath().size()-1).y), paint);
				
				for(int a=1; a<p.getPath().size(); a++)
					canvas.drawLine(screen.getDeviceX(p.getPath().get(a-1).x), screen.getDeviceY(p.getPath().get(a-1).y), 
						screen.getDeviceX(p.getPath().get(a).x), screen.getDeviceY(p.getPath().get(a).y), paint);
		
			}
		}
		
		dialogue.draw(canvas);
		
		menu.draw(canvas);
	
	}
	
	public void onPause(){
		alive = false;
		running = false;
		started = false;
		Particle.setMoving(false);
		Particle.setLoseM(true);
		while(true){
			try {
				drawer.join();
			}catch (InterruptedException e) {}
			break;
		}
		drawer = null;
		removeGravityParticles();
		Particle.stop();
		recycle();
		System.gc();
	}
	
	public void onResume(){
		Log.d("onResume", "Level resume");
		Particle.setLoseM(false);
		alive = true;
		drawer = new Thread(this);
		drawer.start();
	}
	
	public void onGamePause(){
		//when game is paused
		
		
		
		running = false;
	}
	
	public void onGameResume(){
		//when the game resumes or the play button is pressed
		
		
		showPaused = false;
		running = true;
	}

	public void run(){ //this would be transferable to onTick(State s)
		Canvas canvas;
		paint.setColor(Color.GREEN);
		while(alive){
			if(!holder.getSurface().isValid())
				continue;
			
			canvas = holder.lockCanvas();
			
			onDraw(canvas);
			
			holder.unlockCanvasAndPost(canvas);
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {}
			
			if(running)
				timeLeft -= 25;
			
			/*if(timeLeft<=10000 && running)
				Toast.makeText(context.getApplicationContext(),
						timeLeft/1000, Toast.LENGTH_SHORT).show();*/
			
			if(timeLeft<=0 || percentIn==100)
				endGame();
			
		}
	}
	
	public void endGame(){
		//context.startActivity(new Intent("com.dEVELdRONE.GForce_Arena.ENDGAMEACTIVITY"));
		((Activity) context).onBackPressed();
	}
	
	public void scale(float scale){
		screen.expand(scale);
	}
	
	public float getScale(){
		return screen.getScale();
	}

	public float getDrawingWidthScale(){
		return screen.getDrawingWidthScale();
	}
	
	public float getDrawingHeightScale(){
		return screen.getDrawingHeightScale();
	}
	
	public float getDeviceX(float screenX){
		return screen.getDeviceX(screenX);
	}
	
	public float getDeviceY(float screenY){
		return screen.getDeviceY(screenY);
	}
	
	public float getScreenX(float deviceX){
		return screen.getScreenX(deviceX);
	}
	
	public float getScreenY(float deviceY){
		return screen.getScreenX(deviceY);
	} 
	
	public boolean sunIsOverAnother(SunParticle sunParticle, float x, float y) {
		for(SunParticle s : suns){
			if(s.getRadius()+sunParticle.getRadius()>FloatMath.sqrt((x-s.xloc)*(x-s.xloc)+(y-s.yloc)*(y-s.yloc)))
				return true;
		}
		return false;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public boolean isStarted(){
		return started;
	}
	
	//use for read/write file, will have private string that can be altered for this
	public String toString(){
		return "[Level: " + levelNumber+"]";
	}
	
	/*
	 * coordinates and size for the following classes are public, so no getters or setters
	 * **exception: screen scale, height, and width, as these must be altered together, not individually
	 */
	
	private class Screen{
		
		private float x, y, centralX, centralY;
		private float scale, originalScale; //scale is inversely proportional to screen size, scale 1/2 --> 2x size
		private float height, width, maxHeight, maxWidth, originalHeight, originalWidth;
		private float deviceXScale, deviceYScale;
		private float MIN_X, MIN_Y, MAX_X, MAX_Y;
		
		public Screen(float x, float y, float width, float height){
			this.x = x;
			this.y = y;
			this.width = maxWidth = originalWidth = width;
			this.height = maxHeight = originalHeight = height;
			centralX = x+width/2;
			centralY = y+height/2;
			scale = 1;
			deviceXScale = deviceWidth/width;
			deviceYScale = deviceHeight/height;
			MIN_X = MIN_Y = 0;
			MAX_X = width;
			MAX_Y = height;
		}

		public Screen(float x, float y, float width, float height, float maxWidth, float maxHeight){
			this.x = x;
			this.y = y;
			this.width = originalWidth = width;
			this.height = originalHeight = height;
			this.maxHeight = maxHeight;
			this.maxWidth = maxWidth;
			centralX = x+width/2;
			centralY = y+height/2;
			scale = 1;
			deviceXScale = deviceWidth/width;
			deviceYScale = deviceHeight/height;
			MIN_X = MIN_Y = 0;
			MAX_X = width;
			MAX_Y = height;
		}
		
		public void expandSetup(float scale) {
			originalScale = this.scale = scale;
			width=originalWidth*scale;
			height=originalHeight*scale;
			MAX_X = width;
			MAX_Y = height;
		}
		
		public void expand(float scale){
			if(this.scale*scale <= 2*originalScale && this.scale*scale>=.5*originalScale)
				this.scale *= scale;
			
			else if(this.scale*scale > 2*originalScale)
				scale = 2*originalScale;
			
			else if(this.scale*scale < .5*originalScale)
				scale = (float) .5*originalScale;
			
			width=originalWidth*scale;
			height=originalHeight*scale;
			
			x = centralX - width/2;
			y = centralY - height/2;
		}
		
		public void expand(float originalLength, float finalLength){
			
			float scale = originalLength/finalLength;
			expand(scale);
			
		}
		
		public void expand(float originalScale, float originalLength, float finalLength){
			
			float newScale = FloatMath.sqrt(originalLength/finalLength);
			expand(newScale/originalScale);
			
		}
		
		public void move(float f, float g){
			centralX+=f;
			x+=f;
			centralY+=g;
			y+=g;
		}
		
		public void moveTo(float x, float y){
			centralX = x;
			centralY = y;
			this.x = x-width/2;
			this.y = y-height/2;
		}
		
		public float getHeight(){
			return height;
		}
		
		public float getWidth(){
			return width;
		}
		
		public float getMaxHeight(){
			return maxHeight;
		}
		
		public float getMaxWidth(){
			return maxWidth;
		}
		
		public float getScale(){
			return scale;
		}
		
		public float getDrawingWidthScale(){
			return scale * deviceXScale;
		}
		
		public float getDrawingHeightScale(){
			return scale * deviceYScale;
		}
		
		public float getDeviceX(float screenX){
			return (screenX-x)*scale*deviceXScale;
		}
		
		public float getDeviceY(float screenY){
			return (screenY-y)*scale*deviceYScale;
		}
		
		public float getScreenX(float deviceX){
			return deviceX/deviceXScale/scale+x;
		}
		
		public float getScreenY(float deviceY){
			return deviceY/deviceYScale/scale+y;
		}
		
	}
	
	private class HoldingArea{
		
		public float x, y, width, height;
		private boolean isVisible;
		private Paint paint;
		
		public HoldingArea(float x, float y, float width, float height){
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			paint = new Paint();
			paint.setColor(Color.WHITE);
			isVisible = true;
		}
		
		public void draw(Canvas canvas){
			if(isVisible){ //canvas.drawBitmap(holding area pic, resize & stuff);
				
				canvas.drawLine(screen.getDeviceX(x), screen.getDeviceY(y), 
						screen.getDeviceX(x)+width*screen.getDrawingWidthScale(), screen.getDeviceY(y), paint);
				
				canvas.drawLine(screen.getDeviceX(x)+width*screen.getDrawingWidthScale(), screen.getDeviceY(y), 
						screen.getDeviceX(x)+width*screen.getDrawingWidthScale(), 
						screen.getDeviceY(y)+height*screen.getDrawingHeightScale(), paint);
				
				canvas.drawLine(screen.getDeviceX(x)+width*screen.getDrawingWidthScale(), 
						screen.getDeviceY(y)+height*screen.getDrawingHeightScale(),
						screen.getDeviceX(x), screen.getDeviceY(y)+height*screen.getDrawingHeightScale(), paint);
				
				canvas.drawLine(screen.getDeviceX(x), screen.getDeviceY(y)+height*screen.getDrawingHeightScale(), 
						screen.getDeviceX(x), screen.getDeviceY(y), paint);
				
			}
		}
		
		public void open(){
			isVisible = false;
		}
	}
	
	private class Goal{
		
		private Bitmap image;
		private Paint paint;
		private int alphainc;
		public float x, y;
		public static final double MAX_DIST = 100;
		
 		public Goal(Bitmap image){
			this.image = Bitmap.createScaledBitmap(image, 200, 200, false);
			paint = new Paint();
			alphainc = 1;
			x = 0;
			y = 0;
		}
		
		public Goal(Bitmap image, float x, float y){
			this.image = Bitmap.createScaledBitmap(image, 200, 200, false);
			paint = new Paint();
			alphainc = 1;
			this.x = x;
			this.y = y;
		}
		
		public void setLocation(float x, float y){
			this.x = x;
			this.y = y;
		}
		
		public boolean containsParticle(Particle p){
			double dist = FloatMath.sqrt((p.xloc - x) * (p.xloc - x) + (p.yloc - y) * (p.yloc - y));
			return dist <= MAX_DIST;
		}
		
		public boolean containsSun(SunParticle p){
			float dist = FloatMath.sqrt((p.xloc - x) * (p.xloc - x) + (p.yloc - y) * (p.yloc - y));
			return dist < p.getRadius() + 100;
		}
		
		public void draw(Canvas canvas){
			
			double wscale = screen.getDrawingWidthScale(), hscale = screen.getDrawingHeightScale();
			int iwidth = image.getWidth(), iheight = image.getHeight();
			
			int width = (int)(wscale * iwidth);
			int height = (int)(hscale * iheight);
			
			if(running || !started){
				if(paint.getAlpha() >= 255)
					alphainc = -1;
				else if(paint.getAlpha() <= 50)
					alphainc = 1;
			}
			
			paint.setAlpha(paint.getAlpha()+alphainc);
			
			canvas.drawBitmap(
					Bitmap.createScaledBitmap(image, width, height, false), 
					screen.getDeviceX(x-image.getWidth()/2), screen.getDeviceY(y-image.getHeight()/2), paint);
		}
		
		public void setAlpha(int a){
			paint.setAlpha(a);
		}
		
	}
	
	private class PauseMenu{
		
		private Paint paint;
		private Bitmap image;
		private float width, height;
		private float touchedX, touchedY;
		private Bitmap reset, resume, quit;
		private int alpha;
		private String clicked;
		private float RESET_MIN_X, RESET_MAX_X, RESET_MIN_Y, RESET_MAX_Y,
			RESUME_MIN_X, RESUME_MAX_X, RESUME_MIN_Y, RESUME_MAX_Y,
			QUIT_MIN_X, QUIT_MAX_X, QUIT_MIN_Y, QUIT_MAX_Y,
			inGoalX, inGoalY, timeLeftX, timeLeftY, levelNumberX, levelNumberY,
			X_INTERVAL, Y_INTERVAL;
		
		public PauseMenu(float width, float height){
			this.width = width;
			this.height = height;
			
			image = scaleToScreen(BitmapFactory.decodeResource(getResources(), R.drawable.pause_menu2));
			
			reset = scaleToScreen(BitmapFactory.decodeResource(getResources(), R.drawable.reset_button2));
			resume = scaleToScreen(BitmapFactory.decodeResource(getResources(), R.drawable.resume_button));
			quit = scaleToScreen(BitmapFactory.decodeResource(getResources(), R.drawable.quit_button));
			
			RESET_MIN_X = screen.getDeviceX(100); RESET_MAX_X = screen.getDeviceX(300);  
			RESUME_MIN_X = screen.getDeviceX(380); RESUME_MAX_X = screen.getDeviceX(580);  
			QUIT_MIN_X = screen.getDeviceX(660); QUIT_MAX_X = screen.getDeviceX(860); 
			
			RESET_MIN_Y = RESUME_MIN_Y = QUIT_MIN_Y = screen.getDeviceY(300);
			RESET_MAX_Y = RESUME_MAX_Y = QUIT_MAX_Y = screen.getDeviceY(500);
			
			inGoalX = screen.getDeviceX(190); inGoalY = screen.getDeviceY(190);
			timeLeftX = screen.getDeviceX(760); timeLeftY = screen.getDeviceY(190);
			levelNumberX = screen.getDeviceX(500); levelNumberY = screen.getDeviceY(164);
			
			X_INTERVAL = screen.getDeviceX(1);
			Y_INTERVAL = screen.getDeviceY(1);
			
			clicked = "";
			
			alpha = 0;
			paint = new Paint();
			paint.setAlpha(alpha);
			
		}
		
		public Bitmap scaleToScreen(Bitmap bitmap){
			return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*screen.getDrawingWidthScale()), (int) (bitmap.getHeight()*screen.getDrawingHeightScale()), false);
		}
		
		public void onTouch(MotionEvent event){
			
			float x = event.getX(), y = event.getY();
			
			switch(event.getAction()){
			
			case MotionEvent.ACTION_DOWN:
				touchedX = x;
				touchedY = y;
				
				if(touchedX >= RESUME_MIN_X && touchedX <= RESUME_MAX_X && touchedY >= RESUME_MIN_Y && touchedY <= RESUME_MAX_Y){
					clicked = "resume";
					resume = BitmapFactory.decodeResource(getResources(), R.drawable.resume_button_pressed);
					RESUME_MIN_X += 10*X_INTERVAL;
					RESUME_MIN_Y += 10*Y_INTERVAL;
				}
				else if(touchedX >= RESET_MIN_X && touchedX <= RESET_MAX_X && touchedY >= RESET_MIN_Y && touchedY <= RESET_MAX_Y){
					clicked = "reset";
					reset = BitmapFactory.decodeResource(getResources(), R.drawable.reset_button_pressed);
					RESET_MIN_X += 10*X_INTERVAL;
					RESET_MIN_Y += 10*Y_INTERVAL;
				}
				else if(touchedX >= QUIT_MIN_X && touchedX <= QUIT_MAX_X && touchedY >= QUIT_MIN_Y && touchedY <= QUIT_MAX_Y){
					clicked = "quit";
					quit = BitmapFactory.decodeResource(getResources(), R.drawable.quit_button_pressed);
					QUIT_MIN_X += 10*X_INTERVAL;
					QUIT_MIN_Y += 10*Y_INTERVAL;
				}
				
				break;
				
			case MotionEvent.ACTION_UP:
				
				if(clicked.equals("resume")){
					onResumeClicked();

					resume = BitmapFactory.decodeResource(getResources(), R.drawable.resume_button);
					RESUME_MIN_X -= 10*X_INTERVAL;
					RESUME_MIN_Y -= 10*Y_INTERVAL;
				}
				else if(clicked.equals("reset")){
					onResetClicked();

					reset = BitmapFactory.decodeResource(getResources(), R.drawable.reset_button2);
					RESET_MIN_X -= 10*X_INTERVAL;
					RESET_MIN_Y -= 10*Y_INTERVAL;
				}
				else if(clicked.equals("quit")){
					onQuitClicked();

					quit = BitmapFactory.decodeResource(getResources(), R.drawable.quit_button);
					QUIT_MIN_X -= 10*X_INTERVAL;
					QUIT_MIN_Y -= 10*Y_INTERVAL;
				}
				
				touchedX = touchedY = -1;
				clicked = "";
				
				break;
			
			}
			
		}
		
		public void draw(Canvas canvas){
			if(!running && started  || showPaused && !started){
				
				canvas.drawBitmap(image, 0, 0, paint);
				
				canvas.drawBitmap(reset, RESET_MIN_X, RESET_MIN_Y, paint);
				canvas.drawBitmap(resume, RESUME_MIN_X, RESUME_MIN_Y, paint);
				canvas.drawBitmap(quit, QUIT_MIN_X, QUIT_MIN_Y, paint);
				
				/*
				 * display "Pause(d)"
				 * Level number
				 * time left
				 * %/# in goal
				 * reset button
				 * resume button
				 * quit button
				 */

				paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "5thagent.ttf"));
				paint.setColor(Color.rgb(196, 196, 196));
				paint.setTextSize(100*X_INTERVAL);
				canvas.drawText(percentIn+"%", (inGoalX - (percentIn+"%").length()*30*screen.deviceXScale), inGoalY, paint);
				canvas.drawText(timeLeft/1000+"", (timeLeftX - (timeLeft/1000+"").length()*30*screen.deviceXScale), timeLeftY, paint);
				paint.setTextSize(30);
				paint.setColor(Color.rgb(160, 160, 160));
				paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "octincollege.ttf"));
				canvas.drawText(Level.this.levelNumber+"", levelNumberX, levelNumberY, paint);

				dialogue.drawPaused(canvas, X_INTERVAL, Y_INTERVAL);
				
				//...
			
				if((alpha+=255)<=255)
					paint.setAlpha(alpha);
				else
					paint.setAlpha(255);
				
			}
			else alpha = 0;
		}
		
		public void onResetClicked(){
			reset();
		}
		
		public void onResumeClicked(){
			if(started)
				onGameResume();
			else showPaused = false;
		}
		
		public void onQuitClicked(){
			((Activity) context).finish();
		}
		
	}
	
	private class Dialogue{
		
		private String text;
		private Paint paint;
		private int time, alpha;
		private float x, y;
		
		public Dialogue(){ 
			this("", 0, 0, 0);
		}
		
		public Dialogue(String message){
			this(message, message.length() * 10 + 200);
		}
		
		public Dialogue(String message, float x, float y){
			this(message, message.length() * 10 + 200, x, y);
		}
		
		public Dialogue(String message, int milliseconds){
			this(message, milliseconds, 50, 50);
		}
		
		public Dialogue(String message, int milliseconds, float x, float y){
			
			text = message;
			
			paint = new Paint();
			paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "pirulen.ttf"));
			paint.setColor(Color.rgb(196, 196, 196));
			paint.setTextSize(20);
			
			this.y = y;
			this.x = x;
						
			time =  milliseconds;
			alpha = 255;
			
		}
		
		public void draw(Canvas canvas){
			if(time>0 && (!started || running)){
				
				canvas.drawText(text, x, y, paint);
				
				time-=5;
				
			}
			else if(alpha>0 && (!started || running)){
				
				paint.setAlpha(alpha);
				canvas.drawText(text, x, y, paint);
				
				alpha-=5;
				
			}
		}
		
		public void drawPaused(Canvas canvas, float xint, float yint){

			if(started && !running && alpha<=0){
				
				paint.setAlpha(255);
				canvas.drawText(text, 50*xint, 40*yint, paint);
				
			}
		}
		
	}

}
