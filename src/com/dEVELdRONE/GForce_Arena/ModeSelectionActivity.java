package com.dEVELdRONE.GForce_Arena;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ModeSelectionActivity extends Activity implements OnTouchListener{

	private ImageView screen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//enables full screen with no title
      	requestWindowFeature(Window.FEATURE_NO_TITLE);
      	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.mode);
		
		screen = (ImageView) findViewById(R.id.mode_selection);
		
		screen.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		Intent i;
		
		Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int height = display.getHeight();
		
		if(event.getY()<height/2){
			i = new Intent("com.dEVELdRONE.GForce_Arena.ARCADEACTIVITY");
			startActivity(i);
		}
		else{
			i = new Intent("com.dEVELdRONE.GForce_Arena.LEVELSELECTIONACTIVITY");
			startActivity(i);
		}
		
		return false;
		
	}

}
