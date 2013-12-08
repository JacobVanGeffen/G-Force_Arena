package com.dEVELdRONE.GForce_Arena;

import android.content.Context;
import android.util.Log;

public class BackgroundSound extends SoundHandler{

	private boolean isOn;
	
	public BackgroundSound(Context context, int soundPath) {
		super(context, soundPath);
		sound.setLooping(true);
		status = STOPED;
	}

	@Override
	public void play() {
		Log.i("Sound Play", isOn+" "+status);
		if(isOn && status!=PLAYING){
			sound.start(); 
			status = PLAYING;
		} 
	}
	
	public void setIsOn(boolean b){ isOn = b; }
	
	public void stop(){
		setIsOn(false);
		super.stop();
	}
	
	
	
}
