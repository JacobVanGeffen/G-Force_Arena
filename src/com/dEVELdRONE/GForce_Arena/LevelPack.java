package com.dEVELdRONE.GForce_Arena;

public interface LevelPack {

	//**DO NOT USE OUT OF LEVELPACK
	public Level levelGenerator(int levelNumber);
	
	public Level get(int levelNumber);
	
	public int getPackNumber();
	
}
