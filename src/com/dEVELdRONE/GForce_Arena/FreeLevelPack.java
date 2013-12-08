package com.dEVELdRONE.GForce_Arena;

import android.content.Context;

public class FreeLevelPack implements LevelPack {
	
	private Level[] levels;
	private Context context;
	
	public FreeLevelPack(Context context){
		this.context = context;
		levels = new Level[20];
		//fillLevels();
	}
	
	public Level levelGenerator(int levelNumber){
		Level level = new Level(context);
		
		level.setLevelNumber(levelNumber);
		
		switch(levelNumber){
		//level.init(suns, blackholes, goalXloc, goalYloc, 
		//holdingAreaXloc, holdingAreaYloc, holdingAreaWidth, holdingAreaHeight, 
		//howmanyParticles, wallType);
		case 1:
			
			level.init(1, 0, 700, 270, 
					50, 50, 100, 100, 
					100, "none");
			level.setupSun(0, 300, 300, 500);
			level.setupTime(1000);
			//set up blackholes
			//set up screen
			//etc.
			
			break;
			
			
		case 2:
			
			level.init(1, 1, 800, 400, 
					50, 50, 100, 100, 
					100, "none");
			level.setupSun(0, 300, 200, 500);
			level.setupBlackhole(0, 300, 400, 250);
			
			break;
			
			
		case 3:
			
			level.init(1, 0, 100, 100, 
					200, 200, 100, 100, 
					100, "loop");
			level.setupSun(0, 600, 400, 750);
			level.setupScreen(1);
			
			break;
			
			
		case 4:
			
			level.init(1, 1, 800, 400, 
					50, 50, 100, 100, 
					100, "none");
			level.setupSun(0, 300, 200, 500);
			level.setupBlackhole(0, 300, 400, 250);
			level.setupDialogue("Hello how are you doing sir?", 200, 50);
			level.setupTime(10000);
			
			break;
			
			
		case 5:
			
			level.init(3, 0, 800, 255, 100, 230, 50, 50, 100, "none");
			level.setupSun(0, 250, 100, 200);
			level.setupSun(1, 425, 400, 200);
			level.setupSun(2, 600, 100, 200);
			level.setupDialogue("Particles are only attracted to the closest object", 50, 50);
			level.setupTime(10000);
			
			break;
			
		case 6:
			
			level.init(3, 0, 800, 255, 100, 230, 50, 50, 100, "none");
			level.setupSun(0, 250, 100, 200);
			level.setupSun(1, 425, 400, 200);
			level.setupSun(2, 600, 100, 200);
			level.setupDialogue("Having to think yet?", 50, 50);
			level.setupTime(10000);
			
			break;
			
		//...	
			
			
		default:
			return null;
		}
		
		return level;
		
	}
	
	public void fillLevels(){
		for(int a=0; a<levels.length; a++)
			levels[a] = levelGenerator(a);
	}
	
	public Level get(int levelNumber){
		if(levelNumber>=levels.length)
			return null;
		
		//return levels[levelNumber];
		return levelGenerator(levelNumber);
	}
	
	public int getPackNumber(){return 0;}
	
}
