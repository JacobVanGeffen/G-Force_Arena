package com.dEVELdRONE.GForce_Arena;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class LevelActivity extends Activity {
	
	public static int levelNumber, packNumber;
	protected static Level level;
	protected static LevelPack pack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//enables full screen with no title
      	requestWindowFeature(Window.FEATURE_NO_TITLE);
      	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		
		Level.deviceHeight = display.getHeight();
		Level.deviceWidth = display.getWidth();
		
		Log.d("LevelActivity", display.getWidth() + " " + display.getHeight());
		
		//levelNumber = 5; //for testing purposes
		/*if(freepack[levelNumber-1] != null)
			level = freepack[levelNumber-1];
		else*/
		
		pack = new FreeLevelPack(this);
		
		level = pack.get(levelNumber);
		
		setContentView(level);
	}

	@Override
	protected void onPause() {
		super.onPause();
		level.onPause();
		/*if(levelNumber<=20)
			freepack[levelNumber-1] = level;
		previousLevel = level;*/
	}

	@Override
	protected void onResume() {
		super.onResume();
		level.onResume();
		//level.startGame();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		Log.i("Create Options Menu", "here");
		level.onGamePause();
		level.showPaused = true;
		
		return true;
	}
	
	@Override
	public void onBackPressed(){
		level.onGamePause();
		level.showPaused = true;
	}

}
