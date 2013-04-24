package com.example.lunchtime;

import java.io.IOException;
import java.util.ArrayList;

import com.example.lunchtime.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WolfAnimation extends MainScreen {
	boolean itemTouched;
	String itemTouchedName = "sandwitch";
	ArrayList<String> remainingRewards = new ArrayList<String>();
	private ArrayList<String> rewards = new ArrayList<String>();
	
	ArrayList<Integer> rewardIds = new ArrayList<Integer>();
	ArrayList<String> plateFood = new ArrayList<String>();
	ArrayList<Integer> plateIds = new ArrayList<Integer>();
	int objectCoordinates[] = {0,0};

	private SensorManager mSensorManager;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity
	boolean shakeStart = false;

	boolean breadDown = false;
    ImageView moveItem = null;
    ImageView sandwichSize = null;

	//DatabaseHelper db=null;
	private ImageView wolfHead;

	private MediaPlayer mpThankFed;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wolfanimation);
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;
	    initMps();
	    wolfHead = (ImageView)findViewById(R.id.wolf_head);
	    itemTouched = false;
		//feedWolf();
		
	}

    private void initMps(){
    	mpThankFed = MediaPlayer.create(this, R.raw.thanks);
    	
    	mpThankFed.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
		 {
	     @Override
	     public void onCompletion(MediaPlayer mp) 
	     {
	          // Code to start the next audio in the sequence
	 		try {
	 			mp.stop();
	 			mp.prepare();
	 			mp.seekTo(0);
	 		} catch (IllegalStateException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
//	 		mpMinute.start();
//	 		dialogBox.setVisibility(View.VISIBLE);
	     }
	 });
	@Override
	public void onWindowFocusChanged (boolean hasFocus){
	    //ImageView rewardObject = (ImageView) findViewById(200);


	    moveItem = (ImageView) findViewById(R.id.reward_sandwich);
	    Matrix matrix = new Matrix();

	    RelativeLayout thisLayout = (RelativeLayout) findViewById(R.id.wolf_animate_view);
	    sandwichSize = (ImageView) findViewById(R.id.sandwich_size);
       // matrix.postTranslate(touchX - objectCoordinates[0], touchY - objectCoordinates[1]);
        matrix.postTranslate(thisLayout.getWidth()/2-sandwichSize.getWidth()/2, thisLayout.getHeight()-110-sandwichSize.getHeight()/2);
        moveItem.setScaleType(ScaleType.MATRIX);
        moveItem.setImageMatrix(matrix);
		
	}
	
	//public void feedWolf(View view){
	public void feedWolf(){
		RelativeLayout rewardView = (RelativeLayout) findViewById(R.id.wolf_animate_view);
		ImageView topBread = (ImageView) findViewById(R.id.top_bread);
		Matrix matrix = new Matrix();
		
		matrix.reset();
		//matrix.postTranslate(/*55,130);*/(rewardView.getWidth()/2 - 125)/2, (rewardView.getHeight()/2 - 75)/2);
		matrix.postTranslate((rewardView.getWidth()/2 - 125)/2,-90);
		topBread.setScaleType(ScaleType.MATRIX);
		topBread.setImageMatrix(matrix);
		topBread.bringToFront();
		topBread.setVisibility(ImageView.VISIBLE);
	
		TextView hw = (TextView) findViewById(R.id.textView1);
	    hw.setText("vw:"+Integer.toString(rewardView.getWidth())+"vh:"+Integer.toString(rewardView.getHeight())+"bw:"+Integer.toString(topBread.getHeight())+"bh:"+Integer.toString(topBread.getHeight()));
		
	    TranslateAnimation translate = new TranslateAnimation(0,0,0,(rewardView.getHeight()/2 - 75)/2+90);
	    translate.setDuration(1000);
	    //translate.reset();  
	    translate.setFillBefore(true);
	    translate.setFillAfter(true);
	    translate.setFillEnabled(true);
	    topBread.clearAnimation();
	    topBread.startAnimation(translate);
	    
	    

		RewardScrollView rewardScrollView = (RewardScrollView) findViewById(R.id.rewardItemScrollView);
		rewardScrollView.setVisibility(View.INVISIBLE);
		ImageView plateImage = (ImageView) findViewById(R.id.plateImage);
		plateImage.setVisibility(View.INVISIBLE);
		//ImageView wolfHead = (ImageView) findViewById(R.id.wolf_head);
		//wolfHead.setVisibility(View.VISIBLE);
		
	}


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_reward_section, menu);
		return true;
	}
	
	
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		if (event.getAction()==MotionEvent.ACTION_DOWN && !itemTouched){
			touchDown(x, y);
		}
		
		if(event.getAction()==MotionEvent.ACTION_UP && itemTouched){
			touchUp(x,y);
		}
		else if(itemTouched){

			int coordinates[] = {0,0};
			int height = 0;
			int width = 0;
			
			moveReward(x,y);

			ImageView regionImage = (ImageView) findViewById(R.id.region);
			regionImage.getLocationOnScreen(coordinates);
			height = coordinates[1] + regionImage.getHeight();
			width = coordinates[0] + regionImage.getWidth();
			if(x>coordinates[0] && x<width&&y>coordinates[1]&&y<height){
				wolfHead.setImageResource(R.drawable.wolf_openmouth);
			}
			else{
				wolfHead.setImageResource(R.drawable.wolf_awake);
			}
			
			
			
		}
		

        return false;
    }
	
	@SuppressLint("NewApi")
	public void touchDown(float x, float y){
		int height = 0;
		int width = 0;

	    //LinearLayout layout = (LinearLayout) findViewById(R.id.rewardItemLinearLayout);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		float matrixCoordinates[] = {0,0,0,0,0,0,0,0,0};
		moveItem.getImageMatrix().getValues(matrixCoordinates);
		height = (int) matrixCoordinates[Matrix.MTRANS_Y];
		width = (int) matrixCoordinates[Matrix.MTRANS_X];
		if(x>width && x<(width + sandwichSize.getWidth())&&y>height&&y<(height + sandwichSize.getHeight())){
				itemTouched = true;

		}
		
		TextView hw = (TextView) findViewById(R.id.textView1);
	    hw.setText(itemTouchedName+","+String.valueOf(x)+","+","+String.valueOf(height)+","+String.valueOf(width));
	}
	
	public void moveReward(float touchX, float touchY){
	    //ImageView rewardObject = (ImageView) findViewById(200);

    	//moveItem.setVisibility(ImageView.VISIBLE);
	    Matrix matrix = new Matrix();

	  //matrix.postTranslate(0,100);
	    ImageView sandwichSize = (ImageView) findViewById(R.id.sandwich_size);
       // matrix.postTranslate(touchX - objectCoordinates[0], touchY - objectCoordinates[1]);
        matrix.postTranslate(touchX-sandwichSize.getWidth()/2, touchY-110-sandwichSize.getHeight()/2);
        moveItem.setScaleType(ScaleType.MATRIX);
        moveItem.setImageMatrix(matrix);
		
	}

	public void touchUp(float x, float y){


		int coordinates[] = {0,0};
		int height = 0;
		int width = 0;

		wolfHead.getLocationOnScreen(coordinates);
		height = coordinates[1] + wolfHead.getHeight();
		width = coordinates[0] + wolfHead.getWidth();
		if(x>coordinates[0] && x<width&&y>coordinates[1]&&y<height){
			wolfHead.setImageResource(R.anim.wolf_chew_anim);
			moveItem.setVisibility(ImageView.INVISIBLE);
			final AnimationDrawable wolfHeadDrawable = (AnimationDrawable)wolfHead.getDrawable();
			wolfHead.post(
					new Runnable(){

					  @Override
					  public void run() {
						  wolfHeadDrawable.start();
					  }
					});

			Handler handler = new Handler(); 
		    handler.postDelayed(new Runnable() { 
		         public void run() { 	
		     		mpThankFed.start();
		         } 
		    }, 2100); 	  
		}

		itemTouched = false;
//		int coordinates[] = {0,0};
//		int height = 0;
//		int width = 0;
//
//		ImageView plateImage = (ImageView) findViewById(R.id.region);
//		plateImage.getLocationOnScreen(coordinates);
//		height = coordinates[1] + plateImage.getHeight();
//		width = coordinates[0] + plateImage.getWidth();
//		if(x>coordinates[0] && x<width&&y>coordinates[1]&&y<height){
//			if (itemTouchedName.equals("bread")){
//				breadDown = true;
//			}
//			if(!breadDown){
//				Toast.makeText(getApplicationContext(),  "You must put the bread down first", Toast.LENGTH_SHORT).show();
//			    Integer removeReward = rewardIds.get(remainingRewards.indexOf(itemTouchedName));
//			    ImageView rewardObject = (ImageView) findViewById(removeReward.intValue());
//				rewardObject.setVisibility(ImageView.VISIBLE);
//			}
//			else{
//				plateFood.add(itemTouchedName);
////				setPlate(itemTouchedName);
//				//for(int i = 0; i < plateFood.size(); i++){
//				//	plate = plate + plateFood.get(i) + "\n";
//				//}
//				//TextView hw = (TextView) findViewById(R.id.plateText);
//			    //hw.setText(plate);
//			}
//		}
//		else{
//		    Integer removeReward = rewardIds.get(remainingRewards.indexOf(itemTouchedName));
//		    ImageView rewardObject = (ImageView) findViewById(removeReward.intValue());
//			rewardObject.setVisibility(ImageView.VISIBLE);
//		}
////		itemTouchedName = "";
//		itemTouched = false;
//		
//
//    	RelativeLayout rewardLayout = (RelativeLayout) findViewById(R.id.reward_view);
//	    ImageView rewardObject = (ImageView) findViewById(1335);
//	    rewardLayout.removeView(rewardObject);

	}
	
	
	private final SensorEventListener mSensorListener = new SensorEventListener(){
		public void onSensorChanged(SensorEvent event){
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			mAccelLast = mAccelCurrent;
			mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
			float delta = mAccelCurrent - mAccelLast;
			mAccel = mAccel * 0.9f + delta;
			
			
			if(mAccel > 2){
				shakeStart = true;
			}
			else{
				if(shakeStart){
					shakeStart = false;
//					feedWolf();
				}
			}
		}

	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    }

	};
	
	protected void onResume() {
	    super.onResume();
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
	    super.onPause();
	    mSensorManager.unregisterListener(mSensorListener);
	}
	
	
}
