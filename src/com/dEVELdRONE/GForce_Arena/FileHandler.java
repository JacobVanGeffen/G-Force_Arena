package com.dEVELdRONE.GForce_Arena;

import java.util.ArrayList;
import java.util.Scanner;

import android.content.Context;

public class FileHandler {
	
	/*
	 * File format:
	 * 
	 * {LevelPacks: 0 ... other levelpack #s ... }
	 * 
	 * {LevelPack.# : 
	 * [Level 1: /init/ suns, blackholes, goalXloc, goalYloc, 
	 * 		holdingAreaXloc, holdingAreaYloc, holdingAreaWidth, holdingAreaHeight, 
	 * 		howmanyParticles, wallType : 
	 * 		/for # of suns/ x, y, mass :
	 * 		/for # of blackholes/ x, y, mass :
	 *		/other new GravityParticles created will do the same/
	 *		/setupScreen/ scale :
	 *		/setupTime/ milliseconds :
	 *		/setupDialogue/ message, milliseconds, x, y]
	 * ...
	 * [Level 20: ... ]
	 * }
	 *	...
	 */

	public Level readLevel(Context context, String file){
		
		Level level = new Level(context);
		
		Scanner fin = new Scanner(file);
		
		
		return level;
	}
	
	public void readLevel(Level level){
		
	}
	
	public String writeLevel(Level level){
		
		String levelFile = "[Level";
		
		return levelFile;
	}
	
	public ArrayList<Integer> getLevelPacks(String file){
		ArrayList<Integer> packs = new ArrayList<Integer>();
		
		Scanner fin = new Scanner(file);
		int l=0; 
		boolean done = false;
		
		while(fin.hasNext())
			if(!fin.next().equals("{LevelPacks:"))
				continue;
			else break;
			
		do{
			try{
				l = fin.nextInt();
				packs.add(l);
			}catch(Exception e){
				done = true;
			}
		}while(!done);
		
		return packs;
	}
	
}
