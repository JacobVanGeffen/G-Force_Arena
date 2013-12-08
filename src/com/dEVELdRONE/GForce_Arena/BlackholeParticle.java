package com.dEVELdRONE.GForce_Arena;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.FloatMath;

public class BlackholeParticle extends GravityParticle {

	private  Matrix[] mat;
	private Paint paint;
	private int matrixAt;
	
	public BlackholeParticle(float x, float y, double m, Level u) {
		super(x, y, m, u);
		reference = u;
		
		image = LevelSelectionActivity.BLACKHOLE;
		image = Bitmap.createScaledBitmap(image, (int)(image.getWidth()*mass/1000), (int)(image.getWidth()*mass/1000), false);
		
		paint = new Paint();

		mat = createAnimation();
		
		matrixAt = 0;
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
		
		matrixAt++;
		matrixAt %= 180;
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
		return true;
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
