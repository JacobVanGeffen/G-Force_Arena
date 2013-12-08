package com.dEVELdRONE.GForce_Arena;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SpinningStuff extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spinning);
		
		Animation rotation = AnimationUtils.loadAnimation(this, R.anim.sunrotate);
		rotation.setRepeatCount(Animation.INFINITE);
		ImageView image = (ImageView) findViewById(R.id.rtblackhole);
		image.startAnimation(rotation);
		
		Animation rotation2 = AnimationUtils.loadAnimation(this, R.anim.blackholerotate);
		rotation2.setRepeatCount(Animation.INFINITE);
		ImageView image2 = (ImageView) findViewById(R.id.rtsun);
		image2.startAnimation(rotation2);
		
		/*rotating images
		final ImageView image = (ImageView) findViewById(R.id.pic);
        final Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.blackhole);
        final Matrix mat = new Matrix();
        mat.postRotate(90);
        Bitmap bMapRotate = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(), bMap.getHeight(), mat, true);
        image.setImageBitmap(bMapRotate);
        
        Thread rotator = new Thread(){
        	public void run(){
        		while(true){
	        		mat.postRotate(1);
        			//Log.d("Here", "Once");
	        		//image.setImageBitmap(Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(), bMap.getHeight(), mat, true));
	        		try {
						sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	}
        };
		//same principle for drawing bitmap on canvas
		//put in draw method of view instead of thread and use canvas.drawBitmap(Bitmap, Matrix)
        rotator.start();*/
	}

}
