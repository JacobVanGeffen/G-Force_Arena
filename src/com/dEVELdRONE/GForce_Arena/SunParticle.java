package com.dEVELdRONE.GForce_Arena;

import java.io.File;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;

public class SunParticle extends GravityParticle{

	//public static final Bitmap sun_pic = ...;
	
	//have float incrementing each time drawn
	//will represent degrees to be rotated
	
	private  Matrix[] mat;
	private Paint paint;
	private int matrixAt;
		
	private BitmapFactory.Options options = new BitmapFactory.Options();
	
	public SunParticle(float x, float y, double m, Level u) {
		super(x, y, m, u);
		reference = u;
		
		/*options.inJustDecodeBounds = true;
		
		File file = new File(reference.getContext().getFilesDir(), "playersun.png");
		file.setReadable(true);
		
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		
		options.inSampleSize = getScale(options.outWidth, options.outHeight, (int)mass, (int)mass);
		options.inJustDecodeBounds = false;
		
		image = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		Log.i("Constructor", !file.canRead()?"false":"good");*/
		
		image = LevelSelectionActivity.SUN;
		image = Bitmap.createScaledBitmap(image, (int)(image.getWidth()*mass/1000), (int)(image.getWidth()*mass/1000), false);
		
		paint = new Paint();
		
		mat = createAnimation();
		
		matrixAt = 0;
	}
	
	public int getScale(int originalWidth,int originalHeight, final int requiredWidth,final int requiredHeight){
		int scale = 1;
			
		if(originalWidth>requiredWidth || originalHeight>requiredHeight){
			
			if(originalWidth<originalHeight)
				scale=Math.round((float)originalWidth/requiredWidth);
			else
				scale=Math.round((float)originalHeight/requiredHeight);
			
		}
		
		return scale;
	}

	
	@Override
	public void setMass(double m) {
		super.setMass(m);
		image = Bitmap.createScaledBitmap(image, (int)(image.getWidth()*mass/1000), (int)(image.getWidth()*mass/1000), false);
	}

	@Override
	public void draw(Canvas canvas){
		
		Bitmap toDraw = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), mat[matrixAt], false);
		
		float width = toDraw.getWidth(), height = toDraw.getHeight();
		
		toDraw = Bitmap.createScaledBitmap(toDraw, (int)(toDraw.getWidth()*reference.getDrawingWidthScale()), 
				(int)(toDraw.getHeight()*reference.getDrawingHeightScale()), false);
		
		canvas.drawBitmap(toDraw,
				reference.getDeviceX(xloc-width/2), reference.getDeviceY(yloc-height/2), paint);
		if((reference.isRunning() || !reference.isStarted()) && !reference.showPaused){
			matrixAt++;
			matrixAt %= 180;
		}
	}
	
	//not used
	public void onTouch(MotionEvent event){
		int action = event.getAction();
		switch(action & MotionEvent.ACTION_MASK){
		
		case MotionEvent.ACTION_DOWN:
			if(!reference.sunIsOverAnother(this, event.getX(), event.getY()))
				setLocation(reference.getScreenX(event.getX()), reference.getScreenY(event.getY()));
			break;
			
		case MotionEvent.ACTION_MOVE:
			if(!reference.sunIsOverAnother(this, event.getX(), event.getY()))
				setLocation(reference.getScreenX(event.getX()), reference.getScreenY(event.getY()));
			break;
		
		}
	}
	
	public Matrix[] createAnimation(){
		Matrix[] anim = new Matrix[180];
		
		for(int a=0; a<180; a++){
			anim[a] = new Matrix();
			anim[a].postRotate(2*a);
		}
		
		return anim;
		
	}
	
	public void setAlpha(int a){
		paint.setAlpha(a);
	}
	
	@Override
	public boolean loseM(){
		return false;
	}
	
	public void move(float xdist, float ydist){
		xloc += xdist;
		yloc += ydist;
	}
	
	public void moveTo(float x, float y){
		xloc = x;
		yloc = y;
	}
	
	public float getRadius(){
		return image.getWidth()*2/5;
	}
	
	public boolean containsPoint(float x, float y){
		return FloatMath.sqrt((xloc - x)*(xloc-x)+(yloc-y)*(yloc-y)) < getRadius();
	}
	
}
