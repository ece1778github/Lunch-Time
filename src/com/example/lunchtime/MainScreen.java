package com.example.lunchtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends Activity {
	
	// variables storing information for one user
	// there is a clear progress button under the admin screen
	// have not implemented time tracking yet
	
	private int currentLevel = 0;
		
	private final ArrayList<String> mood = new ArrayList<String>();

	public static int currentUser = 1;
	public static int currentQuestion;
	public static DatabaseHelper db=null;
	
	private final ArrayList<ArrayList> level1 = new ArrayList<ArrayList>();
	private final ArrayList<ArrayList> level2 = new ArrayList<ArrayList>();
	private final ArrayList<ArrayList> level3 = new ArrayList<ArrayList>();
	private final ArrayList<ArrayList> level4 = new ArrayList<ArrayList>();
	private final ArrayList<ArrayList> level5 = new ArrayList<ArrayList>();
	
	private TextView userID;
	View unlockPopup = null;
	//private String currentUserName="";
//////progress chart
	public static String playerName;

	private View playButton;
	private View practiceButton;
	private View rewardsButton;

    private Animator mCurrentAnimator;
    private MediaPlayer mpIntro;
	private MediaPlayer mpPlayButton;
	private MediaPlayer mpPractice;
	private MediaPlayer mpRewards;
    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;
	////Level Transition///////////
	public static boolean quit;
	////Level Transition Completed///////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
//    	this.overridePendingTransition(0, 0); 
		playButton = (View)findViewById(R.id.buttonmainplay);
		practiceButton = (View)findViewById(R.id.buttonmainpractice);
		rewardsButton = (View)findViewById(R.id.buttonmainrewards);
		//load user profile
		//load user progress

        // Hook up clicks on the thumbnail views.

        
		File currentUserFile = getBaseContext().getFileStreamPath("CurrentUser.txt");
        if(!currentUserFile.exists()) {
            try {
            	FileOutputStream fis = null;
            	fis = openFileOutput ("CurrentUser.txt", Context.MODE_PRIVATE);
    			fis.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		Toast.makeText(getApplicationContext(),  "Create a user in the administrator settings", Toast.LENGTH_SHORT).show();
        }
        else{
    		FileInputStream fis = null;
    		try {
    			fis = openFileInput ("CurrentUser.txt");
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	    InputStreamReader inputStreamReader = new InputStreamReader(fis);
    	    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    	    String line;
    	    try {
    			while ((line = bufferedReader.readLine()) != null) {
    				currentUser = Integer.parseInt(line);
    				//deleteFile(line);
    			}
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

    		try {
    			fis.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
	        if (currentUser == -1){
	    		Toast.makeText(getApplicationContext(),  "Create a user in the administrator settings", Toast.LENGTH_SHORT).show();
	        	
	        }
	        else{
	        	new currentUserLoadTask().execute();
	        }
        }
		quit = false;
		/******************/
		db=new DatabaseHelper(getApplicationContext());

//		if(true){
//			final Intent introduction = new Intent(this, ZoomActivity.class);
//			Handler handler = new Handler(); 
//		    handler.postDelayed(new Runnable() { 
//		         public void run() { 
//		        		startActivity(introduction);
//		         } 
//		    }, 500); 
//		}

	}
	public void startTutorial(View view){
		playButton.setEnabled(false);
		practiceButton.setEnabled(false);
		rewardsButton.setEnabled(false);
        initMps();
        mpIntro.start();
       
	}
    private void initMps(){
    	mpIntro = MediaPlayer.create(this, R.raw.wolfstart1); 
    	mpPlayButton = MediaPlayer.create(this, R.raw.wolfstart2);
    	mpPractice = MediaPlayer.create(this, R.raw.practice);
    	mpRewards = MediaPlayer.create(this, R.raw.rewards);
    	
    	mpIntro.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
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
		 		final View thumb1View = findViewById(R.id.buttonmainplay);
				Handler handler = new Handler(); 
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
			        	 zoomAnimation(thumb1View, R.drawable.button_main_play, R.id.expandedimageplay, mpPlayButton, playButton);
			         } 
			    }, 600); 
			    

		      final View thumb2View = findViewById(R.id.buttonmainpractice);
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
			            	zoomAnimation(thumb2View, R.drawable.button_main_practice,  R.id.expandedimagepractice, mpPractice, practiceButton);
			         } 
			    }, 6700); 
			    

		      final View thumb3View = findViewById(R.id.buttonmainrewards);
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
			            	zoomAnimation(thumb3View, R.drawable.button_main_rewards,  R.id.expandedimagerewards, mpRewards, rewardsButton);
			         } 
			    }, 11100); 
			    
//		       Retrieve and cache the system's default "short" animation time.
		      mShortAnimationDuration = getResources().getInteger(
		              android.R.integer.config_shortAnimTime);
		     }
		 });
    }
    
    private void zoomAnimation(final View thumbView, int imageResId, int expandViewId, MediaPlayer mp, final View button){
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
        		expandViewId);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.introduction)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        final float startScaleFinal = startScale;
        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
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

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                            .ofFloat(expandedImageView, View.X, startBounds.left))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView, 
                                            View.Y,startBounds.top))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView, 
                                            View.SCALE_X, startScaleFinal))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView, 
                                            View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                        button.setEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                        button.setEnabled(true);
                    }
                });
                set.start();
                mCurrentAnimator = set;
		     }
		 });
        mp.start();
    }
	class currentUserLoadTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			loadCurrentUser();

			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			Toast.makeText(getApplicationContext(),  "Current User is "+ playerName, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void loadCurrentUser(){
//		Cursor result = db.getReadableDatabase().rawQuery("SELECT rewardID FROM "+ db.REWARDTABLE, null);
		Cursor result = db.getReadableDatabase().rawQuery("SELECT * FROM playerInfo WHERE playerID = " + Integer.toString(MainScreen.currentUser), null);
		
		while (result.moveToNext()) {
			playerName=result.getString(1);
			currentQuestion = result.getInt(4);
		}
	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//load user profile
		//load user progress

		/******************/
		db=new DatabaseHelper(getApplicationContext());
		if(currentQuestion-1 == 25){
			playButton.setBackgroundResource(R.drawable.button_main_done);
		}
		else{
			playButton.setBackgroundResource(R.drawable.button_main_play);	
		}
	}


	
	public int getLvl(){
		
		return currentLevel;
		
	}  

	public void updateLvl(int currentLvl){
		//clear all questions for repopulating


		ArrayList<Long> question1 = new ArrayList<Long>();
		ArrayList<Long> question2 = new ArrayList<Long>();
		ArrayList<Long> question3 = new ArrayList<Long>();
		ArrayList<Long> question4 = new ArrayList<Long>();
		ArrayList<Long> question5 = new ArrayList<Long>();
		
		currentLevel = currentLvl;

    	switch (currentLevel) {
        case 1:
        	level1.add(question1);
        	level1.add(question2);
        	level1.add(question3);
        	level1.add(question4);
        	level1.add(question5);
            break;
        case 2:
        	level2.add(question1);
        	level2.add(question2);
        	level2.add(question3);
        	level2.add(question4);
        	level2.add(question5);
            break;
        case 3:
        	level3.add(question1);
        	level3.add(question2);
        	level3.add(question3);
        	level3.add(question4);
        	level3.add(question5);
            break;
        case 4:
        	level4.add(question1);
        	level4.add(question2);
        	level4.add(question3);
        	level4.add(question4);
        	level4.add(question5);
            break;
        case 5:
        	level5.add(question1);
        	level5.add(question2);
        	level5.add(question3);
        	level5.add(question4);
        	level5.add(question5);
            break;
        }
		
	}  
	
	public String[] getMoods(){
		
		return mood.toArray(new String[mood.size()]);
		
	}  
	
	public void addATime(int currentLevel, Long elapsedTime){
		
		
	}  
	
	
		
	public void startGameMode(View Button){

		Intent progressChart = new Intent(this, ProgressChart.class);
		progressChart.putExtra(LevelCompleted.PLAYER_NAME, playerName);
		startActivity(progressChart);
		
//		Intent introduction = new Intent(this, ZoomActivity.class);
//		introduction.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//		startActivity(introduction);
	}

	public void goToPractice(View Button){
		startActivity(new Intent(this, PracticeModeActivity.class));
	}
	
	public void goToKitchen(View Button){
		
		startActivity(new Intent(this, RewardSectionActivity.class));
		
	}

	
	public void goToAdmin(View Button){
		startActivity(new Intent(this, TestDataBase.class));
		
	}

/*********************************/
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}
	

	public void goToAdmin(MenuItem Button){
		startActivity(new Intent(this, UserManagementActivity.class));
		
	}
}
