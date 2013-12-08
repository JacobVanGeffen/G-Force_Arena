package com.dEVELdRONE.GForce_Arena;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;

public class RectangularBound extends Bound{
	
	private Point[] corners;
	private Paint innerPaint, edgePaint;
	private boolean isInternallyBound;
	private float minX, maxX, minY, maxY;

	public RectangularBound(Point a, Point b, Point c, Point d, boolean isInternallyBound){
		corners[0] = a; 
		corners[1] = b;
		corners[2] = c;
		corners[3] = d;
		
		innerPaint = new Paint();
		edgePaint = new Paint();
		edgePaint.setAlpha(150);
		
		minX = a.x; maxX = a.x; minY = a.y; maxY = a.y;
		for(Point p : corners){
			if(p.x < minX) minX = p.x;
			if(p.x > maxX) maxX = p.x;
			if(p.y < minY) minY = p.y;
			if(p.y > maxY) maxY = p.y;
		}
		
		this.isInternallyBound = isInternallyBound;
	}

	@Override
	public boolean isInternallyBound() {return isInternallyBound;}

	@Override
	public boolean isInBounds(Point p) {
		if(isInternallyBound())
			return p.x <= maxX && p.x >= minX && p.y <= maxY && p.y >= minY;
		else
			return !(p.x < maxX && p.x > minX && p.y < maxY && p.y > minY);
	}

	@Override
	public void draw(Canvas canvas) {
		
	}

}
