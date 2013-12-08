package com.dEVELdRONE.GForce_Arena;

import android.content.Context;
import android.media.MediaPlayer;

public abstract class SoundHandler {

	protected MediaPlayer sound;
    private Context context;
    private int raw;
	public static final int PLAYING=0, PAUSED=1, STOPED=2;
	protected int status;
    
    public SoundHandler(Context context, int soundPath){
    	this.context = context;
    	raw = soundPath;
		sound = MediaPlayer.create(context, raw);
    }
	
	public Thread getSoundRunner(){
		return new Thread(){
			public void run(){
				SoundHandler.this.run();
			}
		};
	}
	
	public boolean isRunning(){ return sound.isPlaying(); }
	
	public abstract void play();
	
	public void run(){
		sound.start();
	}
	
	public int status(){ return status; }
	
	public void stop(){
		//destroy runner
		sound.stop();
		status = STOPED;
	}
	
	public void release(){
		sound.release();
	}
	
	public void pause(){
		sound.pause();
		status = PAUSED;
	}
	
}
