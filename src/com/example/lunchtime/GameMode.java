package com.example.lunchtime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.format.Time;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.buttonanimation.Init;
import com.example.buttonanimation.MyButton;



@SuppressLint("ResourceAsColor")
public class GameMode extends Activity
	implements MediaPlayer.OnCompletionListener, PopupWindow.OnDismissListener {

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
   float audioSpeed = 2.0f;
   int priority = 1000;
   
   
   //////////clock time

	private MediaPlayer mpHour;
	private MediaPlayer mpMinute;
   
   
   //////////////////////////////
	//lvl progression
	private int currentLvl;
	private int numWins = 0;

	
	private MyButton button1 = null;
	private MyButton button2 = null;
	private MyButton button3 = null;
	private Random randomGenerator = null;
	private int correctButton;
	private final int BUTTON1 = 1;
	private final int BUTTON2 = 2;
	private final int BUTTON3 = 3;
	//timer
	private long startTime;
	private long endTime;
	private int elapsedTime;
	
	private ImageButton happy;
	private ImageButton bored;
	private ImageButton sad;
	
	
	private final String CORRECT_ANSWER = "Correct";
	private final String WRONG_ANSWER = "Wrong";
	private View buttonInScope;
	

	private PopupWindow pwExperienceSample;

	private MediaPlayer mpBored;
	private MediaPlayer mpCorrectAnswer;
	private MediaPlayer mpHappy;
	private MediaPlayer mpHowYouFeel;
	private MediaPlayer mpSad;
	private MediaPlayer mpTryAgain;
	private MediaPlayer mpWhatTimeIsIt;
	private MediaPlayer mpYourDoGreat;
	private MediaPlayer mpYouEarnNewReward;
	private MediaPlayer mpSetTimeTo;

	private LayoutInflater inflater;
	

	private ImageView star1View;
	private ImageView star2View;
	private ImageView star3View;
	private ImageView star4View;
	private ImageView star5View;
	
	private String playerName;


	private int currentQuestion;
	private Vibrator vibe;
	
	private TextView answerFeedback;
	
	private View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);
		initGame();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//stop the game if player has completed 30 questions // just show the progress chart 
		if(currentQuestion - 1 >= 25 || MainScreen.quit==true){
			MainScreen.quit=false;
			finish();
		}

		else{
		    setStars(currentQuestion-1, true);
			Handler handler = new Handler(); 
			view = this.findViewById(R.id.LinearLayout01);
		    handler.postDelayed(new Runnable() { 
		         public void run() { 
		     		pwExperienceSample.showAtLocation(view, Gravity.CENTER, 0, -100); 	
		     		mpHowYouFeel.start();
		         } 
		    }, 500); 	  

		}
//    	cQ.setText(Integer.toString(currentQuestion));
	    
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
	
	public void onDismiss() {
		disableEmotionButton(false);
      	 getQuestion();
	}

/////////////////////////////Initialize Game///////////////////////////////////  
	public void initGame(){


/////////////////////////////Obtaining Widgets///////////////////////////////////
		obtainWidgets();
/////////////////////////////Obtaining Widgets Completed///////////////////////////////////

/////////////////////////////Initialize Audio Clips///////////////////////////////////
    	initAudioClips();
/////////////////////////////Initialize Audio Clips Completed///////////////////////////////////
		 
/////////////////////////////Initialize Experience Sample///////////////////////////////////
		initExpSample();
/////////////////////////////Initialize Experience Sample Completed///////////////////////////////////
/////////////////////////////Initialize Vibrator///////////////////////////////////  
		vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
/////////////////////////////Initialize Vibrator Completed///////////////////////////////////  

/////////////////////////////Initialize Sound///////////////////////////////////  
		initSound();
/////////////////////////////Initialize Sound Completed///////////////////////////////////  
		oldMinute = 0;
		oldHour = 12;
        /////////////////////////
		currentQuestion = MainScreen.currentQuestion;    

		
	}
/////////////////////////////Initialize Game Completed///////////////////////////////////  
/////////////////////////////Initialize Sound///////////////////////////////////  
	public void initSound(){
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		soundsMap = new SparseIntArray();
		//soundsMap.put(SOUND1, soundPool.load(this, R.raw.click1, 1));
		//soundsMap.put(SOUND2, soundPool.load(this, R.raw.click2, 1));
		//soundsMap.put(SOUND3, soundPool.load(this, R.raw.click3, 1));
		//soundsMap.put(SOUND4, soundPool.load(this, R.raw.click4, 1));
		soundsMap.put(SOUND5, soundPool.load(this, R.raw.click5, 1));
		
		clockMinutes = Init.getMinuteMap();
		clockHours = Init.getHourMap();
	}
/////////////////////////////Initialize Sound Completed///////////////////////////////////  
/////////////////////////////Obtaining Widgets///////////////////////////////////
public void obtainWidgets(){

	button1 = (MyButton) findViewById(R.id.button1);
	button2 = (MyButton) findViewById(R.id.button2);
	button3 = (MyButton) findViewById(R.id.button3);
	star1View = (ImageView)findViewById(R.id.star1);
	star2View = (ImageView)findViewById(R.id.star2);
	star3View = (ImageView)findViewById(R.id.star3);
	star4View = (ImageView)findViewById(R.id.star4);
	star5View = (ImageView)findViewById(R.id.star5);
}
/////////////////////////////Obtaining Widgets Completed///////////////////////////////////

/////////////////////////////Initialize Audio Clips///////////////////////////////////
	public void initAudioClips(){

	    mpCorrectAnswer  = MediaPlayer.create(this, R.raw.correct_answer_sound);
	    mpCorrectAnswer.setOnCompletionListener(this);    	
	    

		mpTryAgain   = MediaPlayer.create(this, R.raw.try_again);
	    mpTryAgain.setOnCompletionListener(this);  

	    

		 mpBored   = MediaPlayer.create(this, R.raw.bored);
		 mpBored.setOnCompletionListener(this);    	
		    
		 mpHappy   = MediaPlayer.create(this, R.raw.happy);
		 mpHappy.setOnCompletionListener(this);    	
		    
		 mpHowYouFeel   = MediaPlayer.create(this, R.raw.how_are_you_feeling);
		 mpHowYouFeel.setOnCompletionListener(this);    
		    
		 mpSad   = MediaPlayer.create(this, R.raw.sad);
		 mpSad.setOnCompletionListener(this);    
		    
		 mpWhatTimeIsIt   = MediaPlayer.create(this, R.raw.what_time_is_it);
		 mpWhatTimeIsIt.setOnCompletionListener(this);    
		    
		 mpYourDoGreat   = MediaPlayer.create(this, R.raw.your_doing_great);
		 mpYourDoGreat.setOnCompletionListener(this);   
		    
		 mpYouEarnNewReward   = MediaPlayer.create(this, R.raw.youv_earned_a_new_reward);
		 mpYouEarnNewReward.setOnCompletionListener(this);    	
	    
		 mpSetTimeTo   = MediaPlayer.create(this, R.raw.set_the_time_to);
		 mpYouEarnNewReward.setOnCompletionListener(this);    	
		
	}
/////////////////////////////Initialize Audio Clips Completed///////////////////////////////////
	
/////////////////////////////Initialize Experience Sample///////////////////////////////////
	private void initExpSample(){

		inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		 int width = 600;
		 int height = width * 85/269;   
		 width = 450;
		 height = width * 180/294;
	     View pwExperienceView = inflater.inflate(R.layout.experience_layout, null, false); 
	     pwExperienceSample = new PopupWindow(pwExperienceView, width, height, true);
	     pwExperienceSample.setOnDismissListener(this);
	    happy =(ImageButton)pwExperienceView.findViewById(R.id.happy);
	    bored =(ImageButton)pwExperienceView.findViewById(R.id.bored);
	    sad = (ImageButton)pwExperienceView.findViewById(R.id.sad);
//	    feeling = (MyTextView)pwExperienceView.findViewById(R.id.feeling);
	    		
	    
	    registerEmotionButtonOnClickHappy();
	    registerEmotionButtonOnClickBored();
	    registerEmotionButtonOnClickSad();
		
	}

//	 mpHour.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
//	 {
//	     @Override
//	     public void onCompletion(MediaPlayer mp) 
//	     {
//	     }
//	 });
	private void disableEmotionButton(boolean disable){

		if (disable == true){
			happy.setClickable(false);
			bored.setClickable(false);
			sad.setClickable(false);		
		}
		else {
			happy.setClickable(true);
			bored.setClickable(true);
			sad.setClickable(true);			
		}
		
	}
    private void registerEmotionButtonOnClickHappy(){
		    happy.setOnClickListener(new OnClickListener()
		    {
	           public void onClick(View v)
	           {
	        	   	disableEmotionButton(true);
		        	mpHappy.start();
		        	emotionState = "Happy";
		        	new AddUserSurveyData().execute();
		        	Handler handler = new Handler(); 
		        	handler.postDelayed(new Runnable() { 
	       	         public void run() { 
	       	        	 pwExperienceSample.dismiss();
	       	         } 
		        	}, 2200); 
	           	
	           }
		    });
    }

    private void registerEmotionButtonOnClickBored(){

	    bored.setOnClickListener(new OnClickListener()
        {
           public void onClick(View v)
           {
        	   	 disableEmotionButton(true);
	        	 mpBored.start();
	        	 emotionState = "Bored";
		         new AddUserSurveyData().execute();
		         Handler handler = new Handler(); 
		         handler.postDelayed(new Runnable() { 
       	         public void run() { 
       	        	 pwExperienceSample.dismiss();
       	         } 
       	    }, 2200); 
           }
        });
    }
    private void registerEmotionButtonOnClickSad(){
    	
	    sad.setOnClickListener(new OnClickListener()
       {
           public void onClick(View v)
           {
        	     disableEmotionButton(true);
	        	 mpSad.start();
	        	 emotionState = "sad";
		         new AddUserSurveyData().execute();
		         Handler handler = new Handler(); 
		         handler.postDelayed(new Runnable() { 
       	         public void run() { 
       	        	 pwExperienceSample.dismiss();
       	         } 
       	    }, 2200); 
           }
       });
    }

/////////////////////////////Initialize Experience Sample Completed///////////////////////////////////    
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
    

//	 AddUserSurveyData
	private void progressionManager(int whichOne, View button){
		//check if player has pressed correct answer button
		buttonInScope = button;
    	endTime = System.currentTimeMillis();
    	elapsedTime = Math.round((endTime - startTime)/1000);
//    	showTime.setText(Integer.toString(elapsedTime) + " sec");
    	startTime = System.currentTimeMillis();
		timeForResponse=elapsedTime;
		if (touchQuestion){
			String clockTime = timeToString(oldHour*100+oldMinute);
			if (clockTime.equals(answer)){ 
				answerFeedback.setText("Good Job!");
				answerFeedback.setTextColor(this.getResources().getColor(R.color.green));
				answerFeedback.setVisibility(ImageView.VISIBLE);
				disableGameButtons(true);
				buttonInScope.setBackgroundResource(R.drawable.button_game_correct);
				correctness="Correct";

				setStars(currentQuestion, false);
				currentQuestion = currentQuestion + 1;
	
				setQuestion();
				new AddUserTimeData().execute();
			    	    	
			}

			else{
				answerFeedback.setText("Try Again");

				answerFeedback.setTextColor(this.getResources().getColor(R.color.red));
				answerFeedback.setVisibility(ImageView.VISIBLE);

				Handler handler = new Handler(); 
			    handler.postDelayed(new Runnable() { 
		        	private View buttonLocal = buttonInScope;
			         public void run() { 
							answerFeedback.setVisibility(ImageView.INVISIBLE);
			         } 
			    }, 1000); 
				correctness="Incorrect";
				new AddUserTimeData().execute();
				vibe.vibrate(150);
				buttonInScope.setBackgroundResource(R.drawable.button_game_wrong);

			    mpTryAgain.start();	    

			    handler.postDelayed(new Runnable() { 
		        	private View buttonLocal = buttonInScope;
			         public void run() { 
			        	 buttonLocal.setBackgroundResource(R.drawable.button_game_unpressed);
			         } 
			    }, 1000); 
			}
			
		}
		else if (buildQuestion){
			if (checkCorrect()){ 
				answerFeedback.setText("Good Job!");
				answerFeedback.setTextColor(this.getResources().getColor(R.color.green));
				answerFeedback.setVisibility(ImageView.VISIBLE);
				disableGameButtons(true);
				buttonInScope.setBackgroundResource(R.drawable.button_game_correct);
				correctness="Correct";

				setStars(currentQuestion, false);
				currentQuestion = currentQuestion + 1;
	
				setQuestion();
				new AddUserTimeData().execute();
			    	    	
			}
	    	//each time a wrong answer is selected, the level restarts
			else{
				answerFeedback.setText("Try Again");
				answerFeedback.setTextColor(this.getResources().getColor(R.color.red));
				answerFeedback.setVisibility(ImageView.VISIBLE);
				//clear number of wins to zero
				//clearWins();
				Handler handler = new Handler(); 
			    handler.postDelayed(new Runnable() { 
		        	private View buttonLocal = buttonInScope;
			         public void run() { 
							answerFeedback.setVisibility(ImageView.INVISIBLE);
			         } 
			    }, 1000); 
				correctness="Incorrect";
				new AddUserTimeData().execute();
				vibe.vibrate(150);
				buttonInScope.setBackgroundResource(R.drawable.button_game_wrong);
	//			displayTryAgain();
			    mpTryAgain.start();	       
	
			    handler.postDelayed(new Runnable() { 
		        	private View buttonLocal = buttonInScope;
			         public void run() { 
			        	 buttonLocal.setBackgroundResource(R.drawable.button_game_unpressed);
			         } 
			    }, 1000); 
			}
			
		}
		else{
			if (whichOne == correctButton){ 
				answerFeedback.setText("Good Job!");
				answerFeedback.setTextColor(this.getResources().getColor(R.color.green));
				answerFeedback.setVisibility(ImageView.VISIBLE);
				
				disableGameButtons(true);
				buttonInScope.setBackgroundResource(R.drawable.button_game_correct);
				correctness="Correct";

				setStars(currentQuestion, false);
				currentQuestion = currentQuestion + 1;
	
				setQuestion();
				new AddUserTimeData().execute();

			    	    	
			}
	    	//each time a wrong answer is selected, the level restarts
			else{
				answerFeedback.setText("Try Again");
				answerFeedback.setTextColor(this.getResources().getColor(R.color.red));
				answerFeedback.setVisibility(ImageView.VISIBLE);

				Handler handler = new Handler(); 
			    handler.postDelayed(new Runnable() { 
		        	private View buttonLocal = buttonInScope;
			         public void run() { 
							answerFeedback.setVisibility(ImageView.INVISIBLE);
			         } 
			    }, 1000); 
				//clear number of wins to zero
				//clearWins();
				correctness="Incorrect";
				new AddUserTimeData().execute();
				vibe.vibrate(150);
				buttonInScope.setBackgroundResource(R.drawable.button_game_wrong);
	//			displayTryAgain();
			    mpTryAgain.start();	       
	
			    handler.postDelayed(new Runnable() { 
		        	private View buttonLocal = buttonInScope;
			         public void run() { 
			        	 buttonLocal.setBackgroundResource(R.drawable.button_game_unpressed);
			         } 
			    }, 1000); 
			}
		}

	}
	
	public void disableGameButtons(boolean disable){
		if(disable == true){
			button1.setEnabled(false);
			button2.setEnabled(false);
			button3.setEnabled(false);
		}
		else{
			button1.setEnabled(true);
			button2.setEnabled(true);
			button3.setEnabled(true);
		}
	}
	
	public void button1Click(View button){
		progressionManager(BUTTON1, button);
		
	}
	public void button2Click(View button){

		progressionManager(BUTTON2, button);
		
	}
	public void button3Click(View button){
		progressionManager(BUTTON3, button);
		
	}
//////////helper functions that deal with game progression; all are called in progressionManager
//////////////////////clear the number of wins///////////
	private void clearStars(){
	
		star1View.setImageResource(R.drawable.star_outline_game);
		star2View.setImageResource(R.drawable.star_outline_game);
		star3View.setImageResource(R.drawable.star_outline_game);
		star4View.setImageResource(R.drawable.star_outline_game);
		star5View.setImageResource(R.drawable.star_outline_game);
	
	}
	private void setStars(int cQ, boolean inOnResume){
	
		numWins = cQ%5;
		switch (numWins) {
			case 1:
				star1View.setImageResource(R.drawable.star_filled_game);
				break;
			case 2:
				star1View.setImageResource(R.drawable.star_filled_game);
				star2View.setImageResource(R.drawable.star_filled_game);
				break;
			case 3:
				star1View.setImageResource(R.drawable.star_filled_game);
				star2View.setImageResource(R.drawable.star_filled_game);
				star3View.setImageResource(R.drawable.star_filled_game);
				break;
			case 4:
				star1View.setImageResource(R.drawable.star_filled_game);
				star2View.setImageResource(R.drawable.star_filled_game);
				star3View.setImageResource(R.drawable.star_filled_game);
				star4View.setImageResource(R.drawable.star_filled_game);
				break;
			case 0:
				if(inOnResume){
					star1View.setImageResource(R.drawable.star_outline_game);
					star2View.setImageResource(R.drawable.star_outline_game);
					star3View.setImageResource(R.drawable.star_outline_game);
					star4View.setImageResource(R.drawable.star_outline_game);
					star5View.setImageResource(R.drawable.star_outline_game);
				}
				else{
					star1View.setImageResource(R.drawable.star_filled_game);
					star2View.setImageResource(R.drawable.star_filled_game);
					star3View.setImageResource(R.drawable.star_filled_game);
					star4View.setImageResource(R.drawable.star_filled_game);
					star5View.setImageResource(R.drawable.star_filled_game);
				}
				break;
			default:
				break;
		}
	
	}
	private void clearWins(){

    	star1View.setImageResource(R.drawable.star_outline_game);
    	star2View.setImageResource(R.drawable.star_outline_game);
    	star3View.setImageResource(R.drawable.star_outline_game);
    	star4View.setImageResource(R.drawable.star_outline_game);
    	star5View.setImageResource(R.drawable.star_outline_game);
		numWins = 0;
		
	}
/////////////////////update the current level//////////////

	private ArrayList<String> rewards = new ArrayList<String>();
	private int level;
	private void goToLevelCompleted(){
		rewards.clear();
		ArrayList<String> rewardsPass = new ArrayList<String>();
		level = currentQuestion/5 ;
		switch(level){
		case 1:
			rewards.add("bread");
			
			break;
		case 2:
			rewards.add("jam");
			rewards.add("peanutbutter");
			break;
		case 3:
			rewards.add("lettuce");
			rewards.add("onion");
			rewards.add("tomato");
			break;
		case 4:
			rewards.add("ham");
			rewards.add("egg");
			rewards.add("cheese");
			break;
		default:
			break;
		}
		new AddPlayerRewardData(this).execute();
	}	
	
	/*****************************************Clock Question Additions*****************/
	int clockX = 0;
	int clockY = 0;
	int oldMinute = 0;
	int oldHour = 0;

	int newMinute;
	int newHour;
	boolean hourHold = false;
	boolean minuteHold = false;

		@Override
		public void onWindowFocusChanged (boolean hasFocus) {
			if (!setUp){
				answerFeedback = (TextView) findViewById(R.id.question_feedback);
				setClockArms();
				setupClock();
				ImageView buildClock = (ImageView) findViewById(R.id.builder_clock_face);
				buildClock.setVisibility(ImageView.INVISIBLE);
				
		    	ImageView clockNumberImage;
		    	for (int i = 1; i <=12; i++){
		    		clockNumberImage = (ImageView) findViewById(100+i);
		    		clockNumberImage.setVisibility(ImageView.INVISIBLE);
		    	}
			}
		}
		
		@SuppressLint("NewApi")
		public void setTime(int hour, int minute){
			String time = "";
			if (minute<10)
				time = String.valueOf(hour) + ":0" + String.valueOf(minute);
			else
				time = String.valueOf(hour) + ":" + String.valueOf(minute);
			//TextView timeView = (TextView) findViewById(R.id.temp);
			//timeView.setText(time);
	    	ImageView minuteArm = (ImageView) findViewById(R.id.game_minute_arm);
			minuteArm.setRotation(minute*6);
	    	ImageView hourArm = (ImageView) findViewById(R.id.game_hour_arm);
	    	hourArm.setRotation(hour*30 + minute/2);

	    	/*ImageView minuteArmDown = (ImageView) findViewById(R.id.minute_arm_down);
	    	minuteArmDown.setRotation(minute*6);
	    	ImageView hourArmDown = (ImageView) findViewById(R.id.hour_arm_down);
	    	hourArmDown.setRotation(hour*30 + minute/2);*/
		}

		@SuppressLint("NewApi")
		public void setClockArms(){
			ImageView clockFace = (ImageView) findViewById(R.id.game_clock_face);
			ImageView hourHand = (ImageView) findViewById(R.id.game_hour_arm);
			ImageView minuteHand = (ImageView) findViewById(R.id.game_minute_arm);
			//TextView tempField = (TextView) findViewById(R.id.temp);
			
			int coordinates[] = {0,0};
			clockFace.getLocationOnScreen(coordinates);
			int clockWidthCenter = clockFace.getWidth()/2 + coordinates[0];
			int clockHeightCenter = clockFace.getHeight()/ 2 + coordinates[1];

	/*		clockFace.getLocationOnScreen(coordinates);
			int minuteWidthCenter = minuteHand.getWidth()/2 + coordinates[0];
			int minuteHeightCenter = minuteHand.getHeight()/ 2 + coordinates[1];
		    minuteHand.setPivotX(minuteWidthCenter);
		    minuteHand.setPivotY(minuteHeightCenter);

			hourHand.getLocationOnScreen(coordinates);
			int hourWidthCenter = hourHand.getWidth()/2 + coordinates[0];
			int hourHeightCenter = hourHand.getHeight()/ 2 + coordinates[1];
		    hourHand.setPivotX(hourWidthCenter);
		    hourHand.setPivotY(hourHeightCenter);
	*/		
			int minuteWidth = 44;
			int minuteHeight = 416;
			int hourWidth = 44;
			int hourHeight = 228;
			
			//tempField.setText(String.valueOf(hourHand.getWidth())+","+String.valueOf(hourHand.getHeight())+","+String.valueOf(coordinates[0])+","+String.valueOf(coordinates[1]));
		    /*Matrix matrix = new Matrix();
		    matrix.postTranslate(clockWidthCenter-minuteWidth/2, clockHeightCenter-minuteHeight/2);
		    minuteHand.setScaleType(ScaleType.MATRIX);
		    minuteHand.setImageMatrix(matrix);
		    matrix.reset();
		    matrix.postTranslate(clockWidthCenter-hourWidth/2, clockHeightCenter-hourHeight/2);
		    hourHand.setScaleType(ScaleType.MATRIX);
		    hourHand.setImageMatrix(matrix);
			*/

		    minuteHand.setPivotX(minuteHand.getWidth()/2);
		    minuteHand.setPivotY(minuteHand.getHeight()/2);
		    hourHand.setPivotX(hourHand.getWidth()/2);
		    hourHand.setPivotY(hourHand.getHeight()/2);/*
		    minuteHand.setPivotX(clockWidthCenter/);
		    minuteHand.setPivotY(clockHeightCenter/2);
		    hourHand.setPivotX(clockWidthCenter/4);
		    hourHand.setPivotY(clockHeightCenter/2);*/
		    
		    clockX = clockWidthCenter;
		    clockY = clockHeightCenter;

		}
		
		public void rotateTime(int hour, int minute){
			long rotateTime = 1000;
			int minuteDegree = (minute*6) - (oldMinute*6) + 360;
			int hourDegree = (hour*30 + minute/2) - (oldHour*30 + oldMinute/2);
			if (hourDegree<=0)
				hourDegree +=360;
			ImageView hourHand = (ImageView) findViewById(R.id.game_hour_arm);
			ImageView minuteHand = (ImageView) findViewById(R.id.game_minute_arm);
	    	newHour = hour;
	    	newMinute = minute;
	    	
	    	RotateAnimation hourRotation = new RotateAnimation(0, hourDegree, hourHand.getWidth()/2, hourHand.getHeight()/2);
	    	hourRotation.setStartOffset(0);
	    	hourRotation.setDuration(rotateTime);
	    	hourRotation.setFillEnabled(true);
	    	hourHand.startAnimation(hourRotation);
	    	
	    	RotateAnimation minuteRotation = new RotateAnimation(0, minuteDegree, minuteHand.getWidth()/2, minuteHand.getHeight()/2);
	        minuteRotation.setStartOffset(0);
			minuteRotation.setDuration(rotateTime);
			minuteRotation.setFillEnabled(true);
	        minuteHand.startAnimation(minuteRotation);
	        
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
		
		public void randomTime(){
			int minute = ((int)(Math.random()*1000000)) % 60;
			int hour = ((int)(Math.random()*1000000)) % 12;
			if (hour == 0)
				hour = 12;

			rotateTime(hour, minute);
			
		}
		
		boolean touchQuestion = false;
		
		public float getAngle(float xValue, float yValue){
			double x = (double) xValue - clockX;
			double y = (double) yValue - clockY;
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
		
		public int getClockMinute(float angle){
			if (angle<0)
				angle = 360 + angle;
			int minute = (int) angle/6;
			return minute;
		}
		
		public int getClockHour(int minute){
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
		
		
		public int getClockHourHold(float angle){
			if (angle<0)
				angle = 360 + angle;
			int hour = (int) angle/30;
			if(hour == 0)
				hour = 12;
			return hour;
		}
		
		@Override 
		public boolean onTouchEvent(MotionEvent event) {
			if (touchQuestion){
		    	ImageView minuteArm = (ImageView) findViewById(R.id.game_minute_arm);
		    	ImageView hourArm = (ImageView) findViewById(R.id.game_hour_arm);
//		    	ImageView minuteArmDown = (ImageView) findViewById(R.id.minute_arm_down);
//		    	ImageView hourArmDown = (ImageView) findViewById(R.id.hour_arm_down);
//		    	
//				if(event.getAction()==MotionEvent.ACTION_DOWN){
//					minuteArmDown.setVisibility(0);
//					hourArmDown.setVisibility(0);
//					minuteArm.setVisibility(4);
//					hourArm.setVisibility(4);
//				}
//				
//				else if(event.getAction()==MotionEvent.ACTION_UP){
//					minuteArmDown.setVisibility(4);
//					hourArmDown.setVisibility(4);
//					minuteArm.setVisibility(0);
//					hourArm.setVisibility(0);
//				}
//			
			
				float x = event.getX();
				float y = event.getY();// - TitleBarHeight;
				float angle = getAngle(x,y);
				
				//TextView tempText = (TextView) findViewById(R.id.temp);
				//tempText.setText(String.valueOf(TitleBarHeight)+","+String.valueOf(x)+","+String.valueOf(y));
				//tempText.setText(String.valueOf(angle)+","+String.valueOf(x)+","+String.valueOf(y));
				int minute = oldMinute;
				int hour = oldHour;

				int currentLevel = (currentQuestion-1)/5+1;
				if(currentLevel == 2){
					hour = getClockHourHold(angle);
					if(oldHour != hour){
						//do clicking
						playSound(SOUND5, audioSpeed);
					}
					oldHour = hour;
				}
				else if(currentLevel == 5){
					minute = getClockMinute(angle);
					hour = getClockHour(minute);
				}
				else{
					minute = getClockMinute(angle);
					if(oldMinute != minute){
						//do clicking
						playSound(SOUND5, audioSpeed);
					}
					oldMinute = minute;
				}
				setTime(hour, minute);
				
			}
			else if (buildQuestion){
				float x = event.getX();
				float y = event.getY();
				BuilderScrollView numberView = (BuilderScrollView) findViewById(R.id.numberScrollView);
				
				if (numberView.scrollX >= 0 && !itemTouched && !clockTouched){
					touchDown(numberView.scrollX);
				}
				else if(event.getAction()==MotionEvent.ACTION_DOWN && !itemTouched && !clockTouched){
					touchDownClock(x,y);
				}
				else if(event.getAction()==MotionEvent.ACTION_UP && !itemTouched && clockTouched){
					touchUpClock(x,y);
				}
				else if(event.getAction()==MotionEvent.ACTION_UP && itemTouched && !clockTouched){
					touchUp(x,y);
				}
				else if(itemTouched||clockTouched){
					moveReward(x,y);
				}
				
			}
		        return false;
	    }

		
		///////////////////////get question out and set time/////////////////////

		
		
		int getHour(int answerTime){
	
		return answerTime/100;
	
		}
	
	
	
		int getMinute(int answerTime){
	
		return answerTime%100;
	
		}
/////set time to sequence
//		int resource =  clockHours.get(oldHour);
//		 mpHour   = MediaPlayer.create(context, resource);
//		 mpMinute   = MediaPlayer.create(context, clockMinutes.get(oldMinute));
//		 mpHour.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
//		 {
//		     @Override
//		     public void onCompletion(MediaPlayer mp) 
//		     {
//		          // Code to start the next audio in the sequence
//		 		try {
//		 			mp.stop();
//		 			mp.prepare();
//		 			mp.seekTo(0);
//		 		} catch (IllegalStateException e) {
//		 			// TODO Auto-generated catch block
//		 			e.printStackTrace();
//		 		} catch (IOException e) {
//		 			// TODO Auto-generated catch block
//		 			e.printStackTrace();
//		 		}
//		 		mpMinute.start();
//		     }
//		 });
//		 mpSetTimeTo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
//		 {
//		     @Override
//		     public void onCompletion(MediaPlayer mp) 
//		     {
//		          // Code to start the next audio in the sequence
//		 		try {
//		 			mp.stop();
//		 			mp.prepare();
//		 			mp.seekTo(0);
//		 		} catch (IllegalStateException e) {
//		 			// TODO Auto-generated catch block
//		 			e.printStackTrace();
//		 		} catch (IOException e) {
//		 			// TODO Auto-generated catch block
//		 			e.printStackTrace();
//		 		}
//		 		mpHour.start();
//		     }
//		 });
//
//		 mpSetTimeTo.start();
	
		class LoadQuestionTask extends AsyncTask<Void, String, Void> {

		    Context context;
		    private LoadQuestionTask(Context context) {
		        this.context = context.getApplicationContext();
		    }
			@Override
		
			protected Void doInBackground(Void... unused) {
		
				loadQuestionData();
				return null;
		
			}
	
	
			@Override
			protected void onPostExecute(Void unused) {
				answerFeedback.setVisibility(ImageView.INVISIBLE);
				int currentLevel = (currentQuestion-1)/5+1;
				if (currentLevel ==3 || currentLevel==4){
			    	ImageView changeGameClock = (ImageView) findViewById(R.id.game_clock_face);
			        int resId = getResources().getIdentifier("practice_clock_face", "drawable", getPackageName());
			        changeGameClock.setImageResource(resId);
				}
				else if (currentLevel ==2 || currentLevel==5){
			    	ImageView changeGameClock = (ImageView) findViewById(R.id.game_clock_face);
			        int resId = getResources().getIdentifier("wolf_clock_face", "drawable", getPackageName());
			        changeGameClock.setImageResource(resId);
					
				}
				if (touchQuestion){

			    	button2.setText("Select");
			    	button1.setVisibility(View.INVISIBLE);
			    	button3.setVisibility(View.INVISIBLE);
			    	TextView clockQuestion = (TextView) findViewById(R.id.clockQuestion);
			    	clockQuestion.setText("Set the time to " + answer);
			    	clockQuestion.setVisibility(View.VISIBLE);

			    	ImageView clockNumberImage;
			    	for (int i = 1; i <=12; i++){
			    		clockNumberImage = (ImageView) findViewById(100+i);
			    		clockNumberImage.setVisibility(ImageView.INVISIBLE);
			    	}
			    	BuilderScrollView builderScrollView = (BuilderScrollView) findViewById(R.id.numberScrollView);
			    	builderScrollView.setVisibility(View.INVISIBLE);
			    	ImageView builderClock = (ImageView) findViewById(R.id.builder_clock_face);
			    	builderClock.setVisibility(ImageView.INVISIBLE);
			    	ImageView gameClock = (ImageView) findViewById(R.id.game_clock_face);
			    	gameClock.setVisibility(ImageView.VISIBLE);
			    	ImageView minuteHand = (ImageView) findViewById(R.id.game_minute_arm);
			    	minuteHand.setVisibility(ImageView.VISIBLE);
			    	ImageView hourHand = (ImageView) findViewById(R.id.game_hour_arm);
			    	hourHand.setVisibility(ImageView.VISIBLE);
			    	//////////////////////////////////
			    	//////////////////////////////////
			    /////set time to sequence
					int resource =  clockHours.get(getHour(answerInt));
					 mpHour   = MediaPlayer.create(context, resource);
					 resource = clockMinutes.get(getMinute(answerInt));
					 mpMinute   = MediaPlayer.create(context, resource);
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
					     }
					 });
					 mpSetTimeTo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
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
					 		mpHour.start();
					     }
					 });
					 int hour = 12;
					 if (currentLevel == 3 || currentLevel==4){
						 hour = getHour(answerInt);
						 oldHour = hour;
						 
					 }
					 rotateTime(hour,00);
					 
					 mpSetTimeTo.start();
					
				}
				else if (buildQuestion){

			    	button2.setText("Select");
			    	button1.setVisibility(View.INVISIBLE);
			    	button3.setVisibility(View.INVISIBLE);
			    	TextView clockQuestion = (TextView) findViewById(R.id.clockQuestion);
			    	clockQuestion.setVisibility(View.INVISIBLE);
			    	
			    	ImageView clockNumberImage;
			    	for (int i = 1; i <=12; i++){
			    		clockNumberImage = (ImageView) findViewById(100+i);
			    		clockNumberImage.setVisibility(ImageView.VISIBLE);
			    	}
			    	BuilderScrollView builderScrollView = (BuilderScrollView) findViewById(R.id.numberScrollView);
			    	builderScrollView.setVisibility(View.VISIBLE);
			    	ImageView builderClock = (ImageView) findViewById(R.id.builder_clock_face);
			    	builderClock.setVisibility(ImageView.VISIBLE);
			    	ImageView gameClock = (ImageView) findViewById(R.id.game_clock_face);
			    	gameClock.setVisibility(ImageView.INVISIBLE);
			    	ImageView minuteHand = (ImageView) findViewById(R.id.game_minute_arm);
			    	minuteHand.setVisibility(ImageView.INVISIBLE);
			    	ImageView hourHand = (ImageView) findViewById(R.id.game_hour_arm);
			    	hourHand.setVisibility(ImageView.INVISIBLE);
			    	getSetNumbers();
			    	setupBuildQuestion();
					setupNumbers();
					
				}
				else{
			    	//change this to generate questions based on level

		      		mpWhatTimeIsIt.start();
			    	randomGenerator = new Random();
			    	correctButton= randomGenerator.nextInt(6) + 1;
			    	TextView clockQuestion = (TextView) findViewById(R.id.clockQuestion);
			    	clockQuestion.setVisibility(View.INVISIBLE);
			    	button1.setVisibility(View.VISIBLE);
			    	button3.setVisibility(View.VISIBLE);
			    	
			    	ImageView clockNumberImage;
			    	for (int i = 1; i <=12; i++){
			    		clockNumberImage = (ImageView) findViewById(100+i);
			    		clockNumberImage.setVisibility(ImageView.INVISIBLE);
			    	}
			    	BuilderScrollView builderScrollView = (BuilderScrollView) findViewById(R.id.numberScrollView);
			    	builderScrollView.setVisibility(View.INVISIBLE);
			    	ImageView builderClock = (ImageView) findViewById(R.id.builder_clock_face);
			    	builderClock.setVisibility(ImageView.INVISIBLE);
			    	ImageView gameClock = (ImageView) findViewById(R.id.game_clock_face);
			    	gameClock.setVisibility(ImageView.VISIBLE);
			    	ImageView minuteHand = (ImageView) findViewById(R.id.game_minute_arm);
			    	minuteHand.setVisibility(ImageView.VISIBLE);
			    	ImageView hourHand = (ImageView) findViewById(R.id.game_hour_arm);
			    	hourHand.setVisibility(ImageView.VISIBLE);
			    	switch(correctButton){
				    	case 1:
					    	button1.setText(answer);
					    	button2.setText(wrong1);
					    	button3.setText(wrong2);
					    	correctButton = 1;
					    	break;
				    	case 2:
					    	button1.setText(answer);
					    	button2.setText(wrong2);
					    	button3.setText(wrong1);
					    	correctButton = 1;
					    	break;
				    	case 3:
					    	button1.setText(wrong1);
					    	button2.setText(wrong2);
					    	button3.setText(answer);
					    	correctButton = 3;
					    	break;
				    	case 4:
					    	button1.setText(wrong1);
					    	button2.setText(answer);
					    	button3.setText(wrong2);
					    	correctButton = 2;
					    	break;
				    	case 5:
					    	button1.setText(wrong2);
					    	button2.setText(wrong1);
					    	button3.setText(answer);
					    	correctButton = 3;
					    	break;
				    	case 6:
					    	button1.setText(wrong2);
					    	button2.setText(answer);
					    	button3.setText(wrong1);
					    	correctButton = 2;
					    	break;
					    default:
					    	break;
			    	}
			    	
					rotateTime(getHour(answerInt), getMinute(answerInt));
				}

					disableGameButtons(false);
			    	startTime = System.currentTimeMillis();
			
			}
	
	
		}
	


		private String answer;
		
		private String wrong1;

		private String wrong2;
		
		private int answerInt;
		private int wrong1Int;
		private int wrong2Int;
		public void loadQuestionData(){
	
			Cursor result = MainScreen.db.getReadableDatabase().rawQuery("SELECT * FROM questionInfo WHERE questionID = " + Integer.toString(currentQuestion), null);
			if (result != null){
				while (result.moveToNext()) {
					String questionType = result.getString(1);
					answerInt = result.getInt(2);
					wrong1Int = result.getInt(3);
					wrong2Int = result.getInt(4);
						
					answer = timeToString(answerInt);
					wrong1 = timeToString(wrong1Int);
					wrong2 = timeToString(wrong2Int);
					if (questionType.equals("Clock")){
						touchQuestion = true;
						buildQuestion = false;
					}
					else if (questionType.equals("Build")){
						touchQuestion = false;
						buildQuestion = true;
					}
					else{
						touchQuestion = false;
						buildQuestion = false;
					}
				}
			}
	
		}
		
		String timeToString(int timeInt){
			String timeString;
			if (getMinute(timeInt)<10)
				timeString 	= Integer.toString(getHour(timeInt)) + ":0" + Integer.toString(getMinute(timeInt));
			else
				timeString 	= Integer.toString(getHour(timeInt)) + ":" + Integer.toString(getMinute(timeInt));
			return timeString;
		}

		public void getQuestion(){
			
			new GetUserQuestionTask(this).execute();
		}
		

		public void setQuestion(){
			
			new SetUserQuestionTask(this).execute();
		}
		
		

		private String correctness;
		private int timeForResponse;
		private String emotionState;
		private String rewardData;

		//retrieves the level (question number) of the current user
		class GetUserQuestionTask extends AsyncTask<Void, String, Void> {

		    Context context;
		    private GetUserQuestionTask(Context context) {
		        this.context = context.getApplicationContext();
		    }
		    @Override
		    protected Void doInBackground(Void... unused) {
		        Cursor result = MainScreen.db.getReadableDatabase().rawQuery("SELECT * FROM playerInfo WHERE playerID = " + Integer.toString(MainScreen.currentUser), null);
		        
		        if (result != null){
		            while (result.moveToNext()){
		                currentQuestion = result.getInt(4);
		                playerName = result.getString(1);
		            }
		        }
		        return null;
		    }
		    
		    @Override
		    protected void onPostExecute(Void unused) {
//		    	cQ.setText(Integer.toString(currentQuestion));
		    	new LoadQuestionTask(context).execute();
		    }
		    
		}

		//sets the level (question number) of the current user
		class SetUserQuestionTask extends AsyncTask<Void, String, Void> {

			
		    Context context;
		    private SetUserQuestionTask(Context context) {
		        this.context = context.getApplicationContext();
		    }
		    @Override
		    protected Void doInBackground(Void... unused) {
		        ContentValues editValues = new ContentValues();
		        
		        editValues.put("level", currentQuestion);
		        String editId[] = {Integer.toString(MainScreen.currentUser)};
		        
		        MainScreen.db.getWritableDatabase().update(DatabaseHelper.PLAYERTABLE, editValues, "playerID = ?", editId);
		        MainScreen.currentQuestion = currentQuestion;
		        
		        return null;
		    }
		    
			
//			if (touchQuestion){
//
//				//say time
//				//oldHour, oldMinute
//
////				playSound(SOUND_12, audioSpeed);
////				playSound(SOUND_56, audioSpeed);
//				int resource =  clockHours.get(oldHour);
//				 mpHour   = MediaPlayer.create(context, resource);
//				 mpMinute   = MediaPlayer.create(context, clockMinutes.get(oldMinute));
//				 mpHour.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
//				 {
//				     @Override
//				     public void onCompletion(MediaPlayer mp) 
//				     {
//				          // Code to start the next audio in the sequence
//				 		try {
//				 			mp.stop();
//				 			mp.prepare();
//				 			mp.seekTo(0);
//				 		} catch (IllegalStateException e) {
//				 			// TODO Auto-generated catch block
//				 			e.printStackTrace();
//				 		} catch (IOException e) {
//				 			// TODO Auto-generated catch block
//				 			e.printStackTrace();
//				 		}
//				 		mpMinute.start();
//				     }
//				 });
//				 mpHour.start();
//			}

			
//			if (touchQuestion){
//
//				//say time
//				//oldHour, oldMinute
//
////				playSound(SOUND_12, audioSpeed);
////				playSound(SOUND_56, audioSpeed);
//				int resource =  clockHours.get(oldHour);
//				 mpHour   = MediaPlayer.create(context, resource);
//				 mpMinute   = MediaPlayer.create(context, clockMinutes.get(oldMinute));
//				 mpHour.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
//				 {
//				     @Override
//				     public void onCompletion(MediaPlayer mp) 
//				     {
//				          // Code to start the next audio in the sequence
//				 		try {
//				 			mp.stop();
//				 			mp.prepare();
//				 			mp.seekTo(0);
//				 		} catch (IllegalStateException e) {
//				 			// TODO Auto-generated catch block
//				 			e.printStackTrace();
//				 		} catch (IOException e) {
//				 			// TODO Auto-generated catch block
//				 			e.printStackTrace();
//				 		}
//				 		mpMinute.start();
//				     }
//				 });
//				 mpHour.start();
//					Handler handler = new Handler(); 
//				    handler.postDelayed(new Runnable() { 
//				    	private View buttonLocal = buttonInScope;
//				         public void run() { 
//				        	getQuestion();
//							buttonLocal.setBackgroundResource(R.drawable.button_game_unpressed);
//				         } 
//				    }, 3000); 
//			}
			
		    @Override
		    protected void onPostExecute(Void unused) {

				if(((currentQuestion-1)%5) == 0){	
					   
					
				    Handler handler = new Handler(); 
				    handler.postDelayed(new Runnable() { 
				         public void run() { 
								goToLevelCompleted();
				         } 
				    }, 1000);
					
				    handler.postDelayed(new Runnable() { 
				    	private View buttonLocal = buttonInScope;
				         public void run() { 

								clearWins();
								buttonLocal.setBackgroundResource(R.drawable.button_game_unpressed);
//								goToNextLevel();
				         } 
				    }, 2000); 

				}
// or stay in current level				
				else {	
							buttonInScope.setBackgroundResource(R.drawable.button_game_correct);
		//					displayGoodJob();    
		//					encouragement.setText("Great Job!");
							Handler handler = new Handler(); 
						    handler.postDelayed(new Runnable() { 
						    	private View buttonLocal = buttonInScope;
						         public void run() { 
						        	getQuestion();
									buttonLocal.setBackgroundResource(R.drawable.button_game_unpressed);
						         } 
						    }, 1000); 
				}
				mpCorrectAnswer.start();
		    }
		    
		}

		//retrieves the date and time
		String getDate(){
		    Time date = new Time();
		    date.setToNow();
		    String currentDate = date.format("%Y/%m/%d-%H:%M:%S");
		    return currentDate;
		}

		//adds a time value to the database
		//increment currentQuestion after this function and the next
		//limit?
		class AddUserTimeData extends AsyncTask<Void, String, Void> {
		    @Override
		    protected Void doInBackground(Void... unused) {
		        
		        ContentValues insertValues = new ContentValues();
		        insertValues.put("date", getDate());
		        insertValues.put("playerID", MainScreen.currentUser);
		        insertValues.put("questionID", currentQuestion-1);
		        insertValues.put("correct", correctness);//put "Correct" or "Incorrect"
		        insertValues.put("answerTime", timeForResponse);//put time as an integer
		        MainScreen.db.getWritableDatabase().insert(DatabaseHelper.TIME, null, insertValues);
		        
		        return null;
		    }
		    
		    @Override
		    protected void onPostExecute(Void unused) {

		    }
		    
		}
		//adds a survey value to the database
		class AddUserSurveyData extends AsyncTask<Void, String, Void> {
		    @Override
		    protected Void doInBackground(Void... unused) {
		        
		        ContentValues insertValues = new ContentValues();
		        insertValues.put("date", getDate());
		        insertValues.put("playerID", MainScreen.currentUser);
		        insertValues.put("questionID", currentQuestion);
		        insertValues.put("correct", "Start");//put "Correct" or "Incorrect"
		        insertValues.put("response", emotionState);//put "Happy", "Bored" or "Sad"
		        MainScreen.db.getWritableDatabase().insert(DatabaseHelper.SURVEY, null, insertValues);
		        
		        return null;
		    }
		    
		    @Override
		    protected void onPostExecute(Void unused) {
		    }
		    
		}

		//adds a reward to the player's inventory
		class AddPlayerRewardData extends AsyncTask<Void, String, Void> {
			
		    Context context;
		    private AddPlayerRewardData(Context context) {
		        this.context = context.getApplicationContext();
		    }
		    @Override
		    protected Void doInBackground(Void... unused) {
		       for(String item:rewards){ 
			        ContentValues insertValues = new ContentValues();
			        insertValues.put("playerID", MainScreen.currentUser);
			        insertValues.put("rewardID", item);//put reward name(see list in reward database
			        MainScreen.db.getWritableDatabase().insert(DatabaseHelper.PLAYERREWARDTABLE, null, insertValues);
		       }
		        return null;
		    }
		    
		    @Override
		    protected void onPostExecute(Void unused) {	
		    	clearStars();
				Intent transition = new Intent(context, LevelCompleted.class);
				transition.putExtra(LevelCompleted.PLAYER_NAME, playerName);
				transition.putExtra(LevelCompleted.PLAYER_CURRENTQUESTION, level);
				transition.putStringArrayListExtra(LevelCompleted.REWARDS, rewards);
				startActivity(transition);
				
		    }
		    
		}		
		
		/************************Clock Builder************************/
		int clockBuildX;
		int clockBuildY;
		int clockRadius;
		int clockNumberHeight;
		int clockNumberWidth;
		int clockIconHeight;
		int clockIconWidth;
		int titlebar;
		boolean setUp = false;
		boolean buildQuestion = false;

		ArrayList<Integer> startingIds = new ArrayList<Integer>();
		ArrayList<Integer> notStartingIds = new ArrayList<Integer>();

		boolean itemTouched;
		boolean clockTouched;
		String itemTouchedName;
		ArrayList<String> remainingRewards = new ArrayList<String>();
		
		ArrayList<Integer> linearNumberIds = new ArrayList<Integer>();
		ArrayList<String> clockNumbers = new ArrayList<String>();
		//ArrayList<Integer> clockIds = new ArrayList<Integer>();
		boolean numberSet[] = {false, false, false, false, false, false, false, false, false, false, false, false};
		boolean startingNumbers[] = {false, false, false, false, false, false, false, false, false, false, false, false};
		//int clockNumberIds[] = {0};
		int currentNumber = -1;
		int currentSpot = -1;
	    ImageView moveItem = null;

		/*@Override
		public void onWindowFocusChanged (boolean hasFocus) {
			if (!setUp){
				setupClock();
				setupNumbers();
			}
		}*/
		
		private void getSetNumbers(){
			startingIds.clear();
			notStartingIds.clear();
			while (wrong1Int>0){
				startingIds.add(wrong1Int%10);
				wrong1Int /= 10;
			}
			while (wrong2Int>0){
				startingIds.add(wrong2Int%100);
				wrong2Int /= 100;
			}
			
		}

		@SuppressLint("NewApi")
		public void setupClock()
		{
			ImageView clockFace = (ImageView) findViewById(R.id.builder_clock_face);
			int coordinates[] = {0,0};
			clockFace.getLocationOnScreen(coordinates);
	    	RelativeLayout clockBuilder = (RelativeLayout) findViewById(R.id.LinearLayout01);
	    	
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			titlebar = size.y - clockBuilder.getHeight();
			clockRadius = clockFace.getHeight()/2 - clockFace.getHeight()/8;
			clockBuildX = clockFace.getWidth()/2 + coordinates[0];
			clockBuildY = clockFace.getHeight()/ 2 + coordinates[1]-titlebar;
			ImageView clockNumberSize = (ImageView) findViewById(R.id.clock_size);
			ImageView numberIcon = (ImageView) findViewById(R.id.icon_size);
			clockNumberHeight = clockNumberSize.getHeight();
			clockNumberWidth = clockNumberSize.getWidth();
			clockIconHeight = numberIcon.getHeight();
			clockIconWidth = numberIcon.getWidth();
	    	
		    for(int i = 1; i <= 12; i++)
		    {
		        ImageView clockNumber = new ImageView(GameMode.this);
		        notStartingIds.add(i);
		        int resId = getResources().getIdentifier("clock_question", "drawable", getPackageName());
		        clockNumber.setImageResource(resId);
		        clockNumber.setContentDescription("question");
		        clockNumber.setId(100+i);
		        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
		        		RelativeLayout.LayoutParams.MATCH_PARENT,
		        		RelativeLayout.LayoutParams.MATCH_PARENT);

		    	clockBuilder.addView(clockNumber, layoutParams);
		    	placeNumber(i*30, clockNumber);
		    }
		    setUp = true;
		}
		
		public void setupBuildQuestion(){

			clockNumbers.clear();
			for (int i = 0; i < 12; i++){
				numberSet[i] = false;
			}
		    for(int i = 1; i <= 12; i++)
		    {
		        ImageView clockNumber = (ImageView) findViewById(100+i);
		        if(startingIds.contains(i)){
		        	startingNumbers[i%12] = true;
		        	int resId = getResources().getIdentifier("clock_"+Integer.toString(i), "drawable", getPackageName());
		        	clockNumber.setImageResource(resId);
		        	clockNumber.setContentDescription(Integer.toString(i));
		        }
		        else{
		        	startingNumbers[i%12] = false;
		        	notStartingIds.add(i);
		        	int resId = getResources().getIdentifier("clock_question", "drawable", getPackageName());
		        	clockNumber.setImageResource(resId);
		        	clockNumber.setContentDescription("question");
		        }
		    }
		}
		
		private void placeNumber(int angle, ImageView number){
			
	    	int radiusX = getRadiusX(angle);
	    	int radiusY = getRadiusY(angle);
	    	int matrixX = clockBuildX + radiusX-clockNumberWidth/2;
	    	int matrixY = clockBuildY + radiusY-clockNumberHeight/2;

			Matrix matrix = new Matrix();
			
			matrix.reset();
			matrix.postTranslate(matrixX,matrixY);
			number.setScaleType(ScaleType.MATRIX);
			number.setImageMatrix(matrix);
			
			
		}
		
		public float getBuildAngle(float xValue, float yValue){
			double x = (double) xValue - clockBuildX;
			double y = (double) yValue - clockBuildY;
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
		
		int getRadiusX(int angle){
			int xComponent = 0;
			double calculateAngle = angle;
			if (calculateAngle > 180)
				calculateAngle = 360-calculateAngle;
			if (calculateAngle > 90)
				calculateAngle = 180-calculateAngle;
			if (calculateAngle > 90)
				calculateAngle = 180-calculateAngle;
			calculateAngle = calculateAngle * Math.PI / 180;
			if(calculateAngle<45)
				xComponent = (int) (Math.sin(calculateAngle)*clockRadius);
			else
				xComponent = (int) (Math.cos(calculateAngle)*clockRadius);
			
			if (angle>=180)
				xComponent*=-1;
			
			return xComponent;
		}
		
		int getRadiusY(int angle){
			int yComponent = 0;
			double calculateAngle = angle;
			if (calculateAngle > 180)
				calculateAngle = 360-calculateAngle;
			if (calculateAngle > 90)
				calculateAngle = 180-calculateAngle;
			if (calculateAngle > 90)
				calculateAngle = 180-calculateAngle;
			calculateAngle = calculateAngle * Math.PI / 180;
			if(calculateAngle<45)
				yComponent = (int) (Math.cos(calculateAngle)*clockRadius);
			else
				yComponent = (int) (Math.sin(calculateAngle)*clockRadius);
			
			if (angle <=90 || angle >= 270)
				yComponent*=-1;
			
			return yComponent;
		}
		
		
		public void setupNumbers()
		{

	        remainingRewards.clear();
	        linearNumberIds.clear();
		    LinearLayout layout = (LinearLayout) findViewById(R.id.numberLinearLayout);
		    for(int i : notStartingIds)
		    {
		        ImageView numberItem = new ImageView(GameMode.this);
		        int resId = getResources().getIdentifier("icon_" + Integer.toString(i), "drawable", getPackageName());
		        numberItem.setImageResource(resId);
		        numberItem.setId(150+i);
		        numberItem.setContentDescription(Integer.toString(i));
		        linearNumberIds.add(150+i);
		        layout.addView(numberItem);
		        remainingRewards.add(Integer.toString(i));

				TextView infoText = (TextView) findViewById(R.id.infoText);
				infoText.setText("width:"+Integer.toString(numberItem.getWidth())+", height:"+Integer.toString(numberItem.getHeight()));
		    
		    }
		}


		@SuppressLint("NewApi")
		public void touchDownClock(float x,float y){
			ImageView numberObject = null;
			int height = 0;
			int width = 0;

		    //LinearLayout layout = (LinearLayout) findViewById(R.id.rewardItemLinearLayout);
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);

		   
			for(int i = 1; i <= 12; i++){
				numberObject = (ImageView) findViewById(100+i);
				float matrixCoordinates[] = {0,0,0,0,0,0,0,0,0};
				numberObject.getImageMatrix().getValues(matrixCoordinates);
				//numberObject.getLocationOnScreen(objectCoordinates);
				height = (int) matrixCoordinates[Matrix.MTRANS_Y] + clockNumberHeight + titlebar;
				width = (int) matrixCoordinates[Matrix.MTRANS_X] + clockNumberWidth;
				if(x>matrixCoordinates[Matrix.MTRANS_X] && x<width && y>matrixCoordinates[Matrix.MTRANS_Y] && y<height && numberSet[i%12]){
					clockTouched = true;
					itemTouchedName = numberObject.getContentDescription().toString();
					currentNumber = Integer.valueOf(numberObject.getContentDescription().toString());
					currentSpot = i;
					numberObject.setVisibility(ImageView.INVISIBLE);
					

					TextView hw = (TextView) findViewById(R.id.infoText);
				    hw.setText(itemTouchedName);
					
			        moveItem = new ImageView(GameMode.this);
			        int resId = getResources().getIdentifier("icon_" + itemTouchedName, "drawable", getPackageName());
			        //int resId = getResources().getIdentifier("large_"+ itemTouchedName, "drawable", getPackageName());
			        moveItem.setImageResource(resId);
			        moveItem.setContentDescription(itemTouchedName);
			        moveItem.setId(200);
			    	RelativeLayout rewardLayout = (RelativeLayout) findViewById(R.id.LinearLayout01);
			    	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
			    	    RelativeLayout.LayoutParams.MATCH_PARENT,
			    	    RelativeLayout.LayoutParams.MATCH_PARENT);
			    	//layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			    	rewardLayout.addView(moveItem, layoutParams);
			    	moveItem.setVisibility(ImageView.INVISIBLE);
				}
			}
			
			////////////////
			clockNumbers.add(itemTouchedName);
			
		}

		@SuppressLint("NewApi")
		public void touchDown(float x){
			ImageView numberObject = null;
			int height = 0;
			int width = 0;

		    //LinearLayout layout = (LinearLayout) findViewById(R.id.rewardItemLinearLayout);
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);

		   
			for(int i = 0; i < linearNumberIds.size(); i++){


				int objectCoordinates[] = {0,0};
				numberObject = (ImageView) findViewById(linearNumberIds.get(i));
				numberObject.getLocationOnScreen(objectCoordinates);
				height = objectCoordinates[1] + numberObject.getHeight();
				width = objectCoordinates[0] + numberObject.getWidth();
				if(x>objectCoordinates[0] && x<width){
					itemTouched = true;
					itemTouchedName = numberObject.getContentDescription().toString();
					currentNumber =  Integer.valueOf(numberObject.getContentDescription().toString());
					
					TextView hw = (TextView) findViewById(R.id.infoText);
				    hw.setText("lalala"+itemTouchedName+","+","+String.valueOf(linearNumberIds.size())+","+String.valueOf(i));

			        //int resId = getResources().getIdentifier("clock_" + Integer.toString(currentNumber), "drawable", getPackageName());
			        //clockSpot.setImageResource(resId);
					numberObject.setVisibility(ImageView.INVISIBLE);
					
			        moveItem = new ImageView(GameMode.this);
			        int resId = getResources().getIdentifier("icon_" + itemTouchedName, "drawable", getPackageName());
			        moveItem.setImageResource(resId);
			        moveItem.setContentDescription(itemTouchedName);
			        moveItem.setId(200);
			    	RelativeLayout rewardLayout = (RelativeLayout) findViewById(R.id.LinearLayout01);
			    	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
			    	    RelativeLayout.LayoutParams.MATCH_PARENT,
			    	    RelativeLayout.LayoutParams.MATCH_PARENT);
			    	rewardLayout.addView(moveItem, layoutParams);
			    	moveItem.setVisibility(ImageView.INVISIBLE);
				}
			}
			
		}
		
		public void moveReward(float touchX, float touchY){
		    //ImageView rewardObject = (ImageView) findViewById(200);

	    	moveItem.setVisibility(ImageView.VISIBLE);
		    Matrix matrix = new Matrix();

		  //matrix.postTranslate(0,100);

	       // matrix.postTranslate(touchX - objectCoordinates[0], touchY - objectCoordinates[1]);
	        matrix.postTranslate(touchX-clockIconWidth/2, touchY-clockIconHeight/2);
	        moveItem.setScaleType(ScaleType.MATRIX);
	        moveItem.setImageMatrix(matrix);
			
		}

		public void touchUp(float x, float y){

			int coordinates[] = {0,0};
			int height = 0;
			int width = 0;
			int resId;
			ImageView clockSpot;

			ImageView clockImage = (ImageView) findViewById(R.id.builder_clock_face);
			clockImage.getLocationOnScreen(coordinates);
			height = coordinates[1] + clockImage.getHeight();
			width = coordinates[0] + clockImage.getWidth();
			if(x>coordinates[0] && x<width&&y>coordinates[1]&&y<height){
				clockNumbers.add(itemTouchedName);
				int numberSpot = setClock(x,y);
				if (numberSpot == 0){
				    Integer removeReward = linearNumberIds.get(remainingRewards.indexOf(itemTouchedName));
				    ImageView rewardObject = (ImageView) findViewById(removeReward.intValue());
					rewardObject.setVisibility(ImageView.VISIBLE);
				}
				else{
			    	if (numberSpot < 0){
					   /* Integer removeReward = linearNumberIds.get(remainingRewards.indexOf(itemTouchedName));
					    ImageView rewardObject = (ImageView) findViewById(removeReward.intValue());
						rewardObject.setVisibility(ImageView.VISIBLE);*/
			    		numberSpot *=-1;
			    		
						clockSpot = (ImageView) findViewById(100+numberSpot);
						
			    		String replaceImage = clockSpot.getContentDescription().toString();
			    		
			    	    LinearLayout layout = (LinearLayout) findViewById(R.id.numberLinearLayout);
			    		
				        ImageView numberItem = new ImageView(GameMode.this);
				        resId = getResources().getIdentifier("icon_" + replaceImage, "drawable", getPackageName());
				        numberItem.setImageResource(resId);
				        numberItem.setId(150+Integer.parseInt(replaceImage));
				        numberItem.setContentDescription(replaceImage);
				        linearNumberIds.add(150+Integer.parseInt(replaceImage));
				        layout.addView(numberItem);
				        remainingRewards.add(replaceImage);
			    	}
					resetRewards(itemTouchedName);
					clockSpot = (ImageView) findViewById(100+numberSpot);
			        resId = getResources().getIdentifier("clock_" + Integer.toString(currentNumber), "drawable", getPackageName());
			        clockSpot.setImageResource(resId);
			        clockSpot.setContentDescription(Integer.toString(currentNumber));
					numberSet[numberSpot%12] = true;
				}
			}
			else{
			    Integer removeReward = linearNumberIds.get(remainingRewards.indexOf(itemTouchedName));
			    ImageView rewardObject = (ImageView) findViewById(removeReward.intValue());
				rewardObject.setVisibility(ImageView.VISIBLE);
			}
			itemTouchedName = "";
			itemTouched = false;
			

	    	RelativeLayout rewardLayout = (RelativeLayout) findViewById(R.id.LinearLayout01);
		    ImageView rewardObject = (ImageView) findViewById(200);
		    rewardLayout.removeView(rewardObject);

		}
		
		public void touchUpClock(float x, float y){

			int coordinates[] = {0,0};
			int height = 0;
			int width = 0;
			int resId;
			ImageView clockSpot;

			ImageView clockImage = (ImageView) findViewById(R.id.builder_clock_face);
			clockImage.getLocationOnScreen(coordinates);
			height = coordinates[1] + clockImage.getHeight();
			width = coordinates[0] + clockImage.getWidth();
			if(x>coordinates[0] && x<width&&y>coordinates[1]&&y<height){
				clockNumbers.add(itemTouchedName);
				int numberSpot = setClock(x,y);

				TextView infoText = (TextView) findViewById(R.id.infoText);
				infoText.setText("num:"+Integer.toString(numberSpot)+", cur:"+Integer.toString(currentSpot));
				if (numberSpot == 0 || numberSpot == currentSpot || numberSpot*-1 == currentSpot){
				    ImageView clockNumberObject = (ImageView) findViewById(100 + currentSpot);
				    clockNumberObject.setVisibility(ImageView.VISIBLE);
				}
				else{
					if (numberSpot < 0){
			    		numberSpot *=-1;
			    		
						clockSpot = (ImageView) findViewById(100+numberSpot);
						
			    		String replaceImage = clockSpot.getContentDescription().toString();
			    		
			    	    LinearLayout layout = (LinearLayout) findViewById(R.id.numberLinearLayout);
			    		
				        ImageView numberItem = new ImageView(GameMode.this);
				        resId = getResources().getIdentifier("icon_" + replaceImage, "drawable", getPackageName());
				        numberItem.setImageResource(resId);
				        numberItem.setId(150+Integer.parseInt(replaceImage));
				        numberItem.setContentDescription(replaceImage);
				        linearNumberIds.add(150+Integer.parseInt(replaceImage));
				        layout.addView(numberItem);
				        remainingRewards.add(replaceImage);
			    		
					    ImageView clockNumberObject = (ImageView) findViewById(100 + currentSpot);
					    clockNumberObject.setVisibility(ImageView.VISIBLE);
			    	}
				    numberSet[currentSpot%12] = false;
				    ImageView clockNumberObject = (ImageView) findViewById(100 + currentSpot);
				    clockNumberObject.setVisibility(ImageView.VISIBLE);
			        resId = getResources().getIdentifier("clock_question", "drawable", getPackageName());
			        clockNumberObject.setImageResource(resId);
			        clockNumberObject.setContentDescription("question");
			        
					clockSpot = (ImageView) findViewById(100+numberSpot);
			        resId = getResources().getIdentifier("clock_" + Integer.toString(currentNumber), "drawable", getPackageName());
			        clockSpot.setImageResource(resId);
			        clockSpot.setContentDescription(Integer.toString(currentNumber));
					numberSet[numberSpot%12] = true;
				}
			}
			else{
			    ImageView clockNumberObject = (ImageView) findViewById(100 + currentSpot);
			    clockNumberObject.setVisibility(ImageView.VISIBLE);
			}
			itemTouchedName = "";
			clockTouched = false;
			
	    	RelativeLayout rewardLayout = (RelativeLayout) findViewById(R.id.LinearLayout01);
		    ImageView rewardObject = (ImageView) findViewById(200);
		    rewardLayout.removeView(rewardObject);

		}
		
		
		public int setClock(float xValue, float yValue){
	    	int dropAngle = (int) getBuildAngle(xValue, yValue);

	    	dropAngle = ((dropAngle+360+15)/30)*30;
	    	int numberSpot = (dropAngle/30)%12;
	    	if (numberSpot == 0)
	    		numberSpot = 12;
	    	if (startingNumbers[numberSpot%12])
	    		return 0;
	    	else if (numberSet[numberSpot%12])
			    return -numberSpot;
		    else
				return numberSpot;
				
		}
		
		public void resetRewards(String usedReward)
		{
		    LinearLayout layout = (LinearLayout) findViewById(R.id.numberLinearLayout);
		    Integer removeReward = linearNumberIds.get(remainingRewards.indexOf(usedReward));

		    ImageView rewardObject = (ImageView) findViewById(removeReward.intValue());
			layout.removeView(rewardObject);
			
			linearNumberIds.remove(remainingRewards.indexOf(usedReward));
		    remainingRewards.remove(usedReward);
		}
		
		public boolean checkCorrect(){
			boolean correct = true;
			for(int i=1; i<=12; i++){
				ImageView clockSpot = (ImageView) findViewById(100+i);
				
	    		String replaceImage = clockSpot.getContentDescription().toString();
	    		if(!replaceImage.equals(Integer.toString(i))){
	    			correct = false;
	    		}
			}
			if(correct){
				return true;
			}
			else{
				return false;
			}
		}
}
