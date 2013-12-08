package com.dEVELdRONE.GForce_Arena;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class GravityParticle extends Particle{

	protected double mass;
	private boolean added;
	public static float gforce = 200;
	private int pointerid;
	protected Bitmap image;
	protected Level reference;
	
	public GravityParticle(float x, float y, double m, Universe u){
		super(x, y, u);
		mass = m;
		width = 10;
		height = 10;
		added = false;
		try{
			reference = (Level) u;
		}catch(ClassCastException e){
			reference = null;
		}
	}
	
	public GravityParticle(float x, float y, double m, int id, Universe u){
		super(x, y, u);
		mass = m;
		width = 10;
		height = 10;
		added = true;
		pointerid = id;
	}
	
	public void setId(int id){
		pointerid = id;
	}
	
	public int getId(){
		return pointerid;
	}
	
	public void setMass(double m){
		mass = m;
	}
	
	public double getMass(){
		return mass;
	}
	
	public boolean isAdded(){
		return added;
	}
	
	public void setAdded(boolean b){
		added = b;
	}
	
	public static double getGForce(){
		return gforce;
	}
	
	public static void setGForce(float g){
		gforce = g;
	}

	public boolean equals(GravityParticle p) {
		return getId() == p.getId();
	}
	
	public void setImage(Bitmap bitmap){
		image = bitmap;
	}

	public boolean loseM() {
		return false;
	}
	
	public void draw(Canvas canvas){
		try{
			canvas.drawBitmap(
					Bitmap.createScaledBitmap(image, (int)(image.getWidth()*reference.getScale()), 
							(int)(image.getHeight()*reference.getScale()), false),
					xloc, yloc, null);
		}catch(NullPointerException e){}
	}
	
	public void recycle(){
		image.recycle();
	}
	
}
