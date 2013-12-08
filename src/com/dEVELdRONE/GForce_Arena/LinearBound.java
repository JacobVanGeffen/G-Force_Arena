package com.dEVELdRONE.GForce_Arena;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LinearBound extends Bound{

	private Point a, b;
	private Paint paint;
	private float minX, maxX, minY, maxY;
	
	public LinearBound(Point a, Point b){
		this.a = a;
		this.b = b;
		
		minX = a.x; maxX = a.x; minY = a.y; maxY = a.y;
		if(b.x < minX) minX = b.x;
		if(b.x > maxX) maxX = b.x;
		if(b.y < minY) minY = b.y;
		if(b.y > maxY) maxY = b.y;
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}

	@Override
	public boolean isInternallyBound() {return false;}

	@Override
	public boolean isInBounds(Point p) {
		return p.x <= maxX && p.x >= minX && p.y <= maxY && p.y >= minY && (a.y - p.y) == (a.y - b.y)/(a.x - b.x)*(a.x - p.x);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawLine(a.x, a.y, b.x, b.y, paint);
	}

}
