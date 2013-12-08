package com.dEVELdRONE.GForce_Arena;

import android.graphics.Canvas;

public abstract class Bound {

	public abstract boolean isInternallyBound();
	public abstract boolean isInBounds(Point p);
	public abstract void draw(Canvas canvas);
	
}
