package com.example.lunchtime;


import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buttonanimation.Init;

@SuppressLint("NewApi")
public class PracticeModeActivity extends Activity 
	implements MediaPlayer.OnCompletionListener{

	 //play sounds
    private SoundPool soundPool;
    private SparseIntArray soundsMap;

    private SparseIntArray clockMinutes;
    private SparseIntArray clockHours;
    
    int SOUND1=1;
    int SOUND2=2; 
    int SOUND3=3;
    int SOUND4=4; 
    int SOUND5=5; 
    float audioSpeed = 2f;
    int priority = 1000;
    
    
    //////////clock time

	private MediaPlayer mpHour;
	private MediaPlayer mpMinute;
    
    
    //////////////////////////////
	int TitleBarHeight;
	int screenWidth;
	int screenHeight;
	int oldMinute;
	int oldHour;
	int newMinute;
	int newHour;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice_mode);
		//////////sound stuff

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundsMap = new SparseIntArray();
        //soundsMap.put(SOUND1, soundPool.load(this, R.raw.click1, 1));
        //soundsMap.put(SOUND2, soundPool.load(this, R.raw.click2, 1));
        //soundsMap.put(SOUND3, soundPool.load(this, R.raw.click3, 1));
        //soundsMap.put(SOUND4, soundPool.load(this, R.raw.click4, 1));
        soundsMap.put(SOUND5, soundPool.load(this, R.raw.click5, 1));
        
        clockMinutes = Init.getMinuteMap();
        clockHours = Init.getHourMap();
        /////////////////////////
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y-38;
		oldMinute = 0;
		oldHour = 12;
    	ImageView minuteArm = (ImageView) findViewById(R.id.minute_arm);
		minuteArm.setPivotX(screenWidth/2);
		minuteArm.setPivotY(screenHeight/2);
    	ImageView hourArm = (ImageView) findViewById(R.id.hour_arm);
    	hourArm.setPivotX(screenWidth/2);
    	hourArm.setPivotY(screenHeight/2);

		setTime(oldHour, oldMinute);
		
		Rect rectgle= new Rect();
		Window window= getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
		int StatusBarHeight = rectgle.top;
		int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getWidth();
		
		TitleBarHeight= contentViewTop - StatusBarHeight;
		TextView tempText = (TextView) findViewById(R.id.temp);
		tempText.setText(String.valueOf(contentViewTop)+", "+String.valueOf(screenHeight)+", "+String.valueOf(rectgle.bottom));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_practice_mode, menu);
		return true;
	}

	public void onCompletion(MediaPlayer mp) {
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
	}
	 
    public void playSound(int sound, float fSpeed) {
	    AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    float volume = streamVolumeCurrent / streamVolumeMax;  
	    volume = volume * 0.2f;
	    if (priority<0){
	    	priority =1000;
	    }
	    soundPool.play(soundsMap.get(sound), volume, volume, priority--, 0, fSpeed);
   }
    
	
	public float getAngle(float xValue, float yValue){
		double x = (double) xValue - screenWidth/2;
		double y = (double) yValue - screenHeight/2;
		double x2 = (double) x*x;
		double y2 = (double) y*y;
		double z = (double) Math.sqrt(x2+y2);
		double z2 = z*z;

		double angle = 0;
		double cosAngle = (Math.acos((z2+x2-y2)/(2*x*z)));
		cosAngle = -(cosAngle/Math.PI*180 -90);
		if (y<0)
			angle = cosAngle;
		else if (x<0)
			angle = -180 - cosAngle;
		else
			angle = 180-cosAngle;
		
		return (float) angle;
	}
	
	public int getMinute(float angle){
		if (angle<0)
			angle = 360 + angle;
		int minute = (int) angle/6;
		return minute;
	}
	
	public int getHour(int minute){
		int hour = oldHour;
		if ((minute <= 15) && (oldMinute >= 45)){
			if(hour!=12)
				hour++;
			else
				hour = 1;
			oldHour = hour;
		}
		if ((minute >= 45) && (oldMinute <= 15)){
			if(hour!=1)
				hour--;
			else
				hour = 12;
			oldHour = hour;
		}
		if(oldMinute != minute){
			//do clicking
			playSound(SOUND5, audioSpeed);
		}
		oldMinute = minute;
		return hour;
	}
	
	public void setTime(int hour, int minute){
		String time = "";
		if (minute<10)
			time = String.valueOf(hour) + ":0" + String.valueOf(minute);
		else
			time = String.valueOf(hour) + ":" + String.valueOf(minute);
		TextView timeView = (TextView) findViewById(R.id.timeView);
		timeView.setText(time);
    	ImageView minuteArm = (ImageView) findViewById(R.id.minute_arm);
		minuteArm.setRotation(minute*6);
    	ImageView hourArm = (ImageView) findViewById(R.id.hour_arm);
    	hourArm.setRotation(hour*30 + minute/2);

    	ImageView minuteArmDown = (ImageView) findViewById(R.id.minute_arm_down);
    	minuteArmDown.setRotation(minute*6);
    	ImageView hourArmDown = (ImageView) findViewById(R.id.hour_arm_down);
    	hourArmDown.setRotation(hour*30 + minute/2);
	}

	@Override 
	public boolean onTouchEvent(MotionEvent event) {
    	ImageView minuteArm = (ImageView) findViewById(R.id.minute_arm);
    	ImageView hourArm = (ImageView) findViewById(R.id.hour_arm);
    	ImageView minuteArmDown = (ImageView) findViewById(R.id.minute_arm_down);
    	ImageView hourArmDown = (ImageView) findViewById(R.id.hour_arm_down);
    	
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			minuteArmDown.setVisibility(0);
			hourArmDown.setVisibility(0);
			minuteArm.setVisibility(4);
			hourArm.setVisibility(4);
		}
		
		else if(event.getAction()==MotionEvent.ACTION_UP){
			minuteArmDown.setVisibility(4);
			hourArmDown.setVisibility(4);
			minuteArm.setVisibility(0);
			hourArm.setVisibility(0);
		}
	
		float x = event.getX();
		float y = event.getY() - TitleBarHeight;
		float angle = getAngle(x,y);
		
		TextView tempText = (TextView) findViewById(R.id.temp);
		tempText.setText(String.valueOf(TitleBarHeight)+","+String.valueOf(x)+","+String.valueOf(y));
		//tempText.setText(String.valueOf(angle)+","+String.valueOf(x)+","+String.valueOf(y));
		
		int minute = getMinute(angle);
		if(true){
			//click
		}
		int hour = getHour(minute);
		setTime(hour, minute);
		//say time
		if(event.getAction()==MotionEvent.ACTION_UP){
			//oldHour, oldMinute

//			playSound(SOUND_12, audioSpeed);
//			playSound(SOUND_56, audioSpeed);
			
			
			 mpHour   = MediaPlayer.create(this, clockHours.get(oldHour));
			 mpMinute   = MediaPlayer.create(this, clockMinutes.get(oldMinute));
			 mpHour.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
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
			 		mpMinute.start();
			 		mpHour.reset();
			 		mpHour.release();
			     }
			 });
			 mpMinute.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
			 {
			     @Override
			     public void onCompletion(MediaPlayer mp) 
			     {
			    	 mpMinute.reset();
			    	 mpMinute.release();
			     }
			 });
			 mpHour.start();
		}

        return false;
    }
	
	public void rotateTime(int hour, int minute){
		long rotateTime = 500;
		int minuteDegree = (minute*6) - (oldMinute*6) + 360;
		int hourDegree = (hour*30 + minute/2) - (oldHour*30 + oldMinute/2);
		if (hourDegree<=0)
			hourDegree +=360;
    	ImageView minuteArm = (ImageView) findViewById(R.id.minute_arm);
    	ImageView hourArm = (ImageView) findViewById(R.id.hour_arm);
    	newHour = hour;
    	newMinute = minute;
    	
    	RotateAnimation hourRotation = new RotateAnimation(0, hourDegree, screenWidth/2, screenHeight/2);
    	hourRotation.setStartOffset(0);
    	hourRotation.setDuration(rotateTime);
    	hourRotation.setFillEnabled(true);
    	hourArm.startAnimation(hourRotation);
    	
    	RotateAnimation minuteRotation = new RotateAnimation(0, minuteDegree, screenWidth/2, screenHeight/2);
        minuteRotation.setStartOffset(0);
		minuteRotation.setDuration(rotateTime);
		minuteRotation.setFillEnabled(true);
        minuteArm.startAnimation(minuteRotation);
        
    	minuteRotation.setAnimationListener(new AnimationListener() {

    	    @Override
    	    public void onAnimationStart(Animation animation) {

    	    }

    	    @Override
    	    public void onAnimationRepeat(Animation animation) {

    	    }

    	    @Override
    	    public void onAnimationEnd(Animation animation) {
    	        setTime(newHour, newMinute);
    	        oldHour = newHour;
    	        oldMinute = newMinute;

    	    }
    	});
        

	}
	
	public void randomTime(View view){
		int minute = ((int)(Math.random()*1000000)) % 60;
		int hour = ((int)(Math.random()*1000000)) % 12;
		if (hour == 0)
			hour = 12;

		rotateTime(hour, minute);
		
	}
	
}
