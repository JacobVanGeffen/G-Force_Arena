package com.dEVELdRONE.GForce_Arena;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class LevelSelectionActivity extends Activity {


	protected static ArrayList<LevelPack> packs;
	public static Bitmap SUN, BLACKHOLE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.levelselect);
		
		SUN = BitmapFactory.decodeResource(getResources(), R.drawable.playersun);
		BLACKHOLE = BitmapFactory.decodeResource(getResources(), R.drawable.blackhole);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	LevelActivity.levelNumber = position+1;
	        	LevelActivity.packNumber = 0;
	            startActivity(new Intent("com.dEVELdRONE.GForce_Arena.LEVELACTIVITY"));
	        }
	    });
	    
	}

}
