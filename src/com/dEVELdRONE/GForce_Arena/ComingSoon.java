package com.dEVELdRONE.GForce_Arena;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class ComingSoon extends Activity implements OnTouchListener{

	private ImageView screen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//enables full screen with no title
      	requestWindowFeature(Window.FEATURE_NO_TITLE);
      	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.soon);
		
		screen = (ImageView) findViewById(R.id.soon);
		
		screen.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		onBackPressed();
		return false;
	}

}
