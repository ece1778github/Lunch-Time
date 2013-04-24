package com.example.lunchtime;

import java.io.IOException;
import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RewardSectionActivity extends Activity {
	boolean itemTouched;
	String itemTouchedName;
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

	private View readyButton;
    private Animator mCurrentAnimator;
	private MediaPlayer mpReadyButton;

    private int mShortAnimationDuration;
	//DatabaseHelper db=null;
	private ImageView dialogBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reward_section);

		readyButton = (View)findViewById(R.id.readybutton);
		readyButton.setEnabled(false);
//	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;

		new RewardLoadTask().execute();
		itemTouched = false;
		itemTouchedName = "";
		//feedWolf();
		initMps();
		dialogBox = (ImageView)findViewById(R.id.dialogbox);
		mpReadyButton.start();
//		  final View thumb1View = findViewById(R.id.ready_button);
//			Handler handler = new Handler(); 
//		    handler.postDelayed(new Runnable() { 
//		         public void run() { 
//		        	 zoomAnimation(thumb1View, R.drawable.playbutton, R.id.expanded_image_play, mpReadyButton, readyButton);
//		         } 
//		    }, 1000); 
	}
	

    private void initMps(){
    	mpReadyButton = MediaPlayer.create(this, R.raw.rewardswolf);
    	
    	mpReadyButton.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
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
	 		dialogBox.setVisibility(View.VISIBLE);
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
        findViewById(R.id.reward_view)
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
	public void switchToFeeding(View view){
		feedWolf();

		
		final Intent feeding = new Intent(this, WolfAnimation.class);
    	Handler handler = new Handler(); 
    	handler.postDelayed(new Runnable() { 
	         public void run() { 
	        	 startActivity(feeding);
	        	 finish();
	         } 
    	}, 2500); 
	}
	
	//public void feedWolf(View view){
	public void feedWolf(){
		RelativeLayout rewardView = (RelativeLayout) findViewById(R.id.reward_view);
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

	class RewardLoadTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			loadRewards();

			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			
			setupRewards();
		}
		
	}
	
	public void loadRewards(){
		String name;
//		Cursor result = db.getReadableDatabase().rawQuery("SELECT rewardID FROM "+ db.REWARDTABLE, null);
		Cursor result = MainScreen.db.getReadableDatabase().rawQuery("SELECT rewardID FROM playerRewards WHERE playerID = " + Integer.toString(MainScreen.currentUser), null);
		
		while (result.moveToNext()) {
			name=result.getString(0);
			rewards.add(name);
		}
		remainingRewards = rewards;
	
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
		RewardScrollView rewardView = (RewardScrollView) findViewById(R.id.rewardItemScrollView);
		
		if (rewardView.scrollX >= 0 && !itemTouched){
			touchDown(rewardView.scrollX);
		}
		
		else if(event.getAction()==MotionEvent.ACTION_UP && itemTouched){
			touchUp(x,y);
		}
		else if(itemTouched){
			moveReward(x,y);
		}
		

        return false;
    }
	
	@SuppressLint("NewApi")
	public void touchDown(float x){
		ImageView rewardObject = null;
		int height = 0;
		int width = 0;

	    //LinearLayout layout = (LinearLayout) findViewById(R.id.rewardItemLinearLayout);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

	   
		for(int i = 0; i < rewardIds.size(); i++){
			rewardObject = (ImageView) findViewById(rewardIds.get(i));
			rewardObject.getLocationOnScreen(objectCoordinates);
			height = objectCoordinates[1] + rewardObject.getHeight();
			width = objectCoordinates[0] + rewardObject.getWidth();
			if(x>objectCoordinates[0] && x<width){
				itemTouched = true;
				itemTouchedName = rewardObject.getContentDescription().toString();
				rewardObject.setVisibility(ImageView.INVISIBLE);
				
		        moveItem = new ImageView(RewardSectionActivity.this);
		        int resId = getResources().getIdentifier(itemTouchedName + "_icon", "drawable", getPackageName());
		        moveItem.setImageResource(resId);
		        moveItem.setContentDescription(itemTouchedName);
		        moveItem.setId(200);
		    	RelativeLayout rewardLayout = (RelativeLayout) findViewById(R.id.reward_view);
		    	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
		    	    RelativeLayout.LayoutParams.MATCH_PARENT,
		    	    RelativeLayout.LayoutParams.MATCH_PARENT);
		    	//layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		    	rewardLayout.addView(moveItem, layoutParams);
		    	moveItem.setVisibility(ImageView.INVISIBLE);
			}
		}
		
		TextView hw = (TextView) findViewById(R.id.textView1);
	    hw.setText(itemTouchedName+","+String.valueOf(x)+","+","+String.valueOf(height)+","+String.valueOf(width));
	}
	
	public void moveReward(float touchX, float touchY){
	    //ImageView rewardObject = (ImageView) findViewById(200);

    	moveItem.setVisibility(ImageView.VISIBLE);
	    Matrix matrix = new Matrix();

	  //matrix.postTranslate(0,100);

       // matrix.postTranslate(touchX - objectCoordinates[0], touchY - objectCoordinates[1]);
        matrix.postTranslate(touchX-50, touchY-110-50);
        moveItem.setScaleType(ScaleType.MATRIX);
        moveItem.setImageMatrix(matrix);
		
	}

	public void touchUp(float x, float y){

		int coordinates[] = {0,0};
		int height = 0;
		int width = 0;

		ImageView plateImage = (ImageView) findViewById(R.id.plateImage);
		plateImage.getLocationOnScreen(coordinates);
		height = coordinates[1] + plateImage.getHeight();
		width = coordinates[0] + plateImage.getWidth();
		if(x>coordinates[0] && x<width&&y>coordinates[1]&&y<height){
			if (itemTouchedName.equals("bread")){
				breadDown = true;
				readyButton.setEnabled(true);
			}
			if(!breadDown){
				Toast.makeText(getApplicationContext(),  "You must put the bread down first", Toast.LENGTH_SHORT).show();
			    Integer removeReward = rewardIds.get(remainingRewards.indexOf(itemTouchedName));
			    ImageView rewardObject = (ImageView) findViewById(removeReward.intValue());
				rewardObject.setVisibility(ImageView.VISIBLE);
			}
			else{
				plateFood.add(itemTouchedName);
				setPlate(itemTouchedName);
				//for(int i = 0; i < plateFood.size(); i++){
				//	plate = plate + plateFood.get(i) + "\n";
				//}
				//TextView hw = (TextView) findViewById(R.id.plateText);
			    //hw.setText(plate);
				resetRewards(itemTouchedName);
			}
		}
		else{
		    Integer removeReward = rewardIds.get(remainingRewards.indexOf(itemTouchedName));
		    ImageView rewardObject = (ImageView) findViewById(removeReward.intValue());
			rewardObject.setVisibility(ImageView.VISIBLE);
		}
		itemTouchedName = "";
		itemTouched = false;
		

    	RelativeLayout rewardLayout = (RelativeLayout) findViewById(R.id.reward_view);
	    ImageView rewardObject = (ImageView) findViewById(200);
	    rewardLayout.removeView(rewardObject);

	}
	
	public void setupRewards()
	{
	    LinearLayout layout = (LinearLayout) findViewById(R.id.rewardItemLinearLayout);
	    for(int i = 0; i < rewards.size(); i++)
	    {
	        String rewardItemName = rewards.get(i);
	        ImageView rewardItem = new ImageView(RewardSectionActivity.this);
	        int resId = getResources().getIdentifier(rewardItemName + "_icon", "drawable", getPackageName());
	        rewardItem.setImageResource(resId);
	        rewardItem.setContentDescription(rewardItemName);
	       // rewardItem.setImageResource(R.drawable.ic_launcher);
	        rewardItem.setId(100+i);
	        rewardIds.add(100+i);
	        layout.addView(rewardItem);
	    
	    }
	    ImageView tem = (ImageView) findViewById(R.id.plateImage);
		TextView hw = (TextView) findViewById(R.id.textView1);
	    hw.setText(String.valueOf(tem.getHeight()));
	}
	
	public void setPlate(String plateReward){
        ImageView plateItem = new ImageView(RewardSectionActivity.this);
        int resId = getResources().getIdentifier(plateReward + "_plate", "drawable", getPackageName());
        plateItem.setImageResource(resId);
        plateItem.setContentDescription(plateReward);
        plateItem.setId(150 + plateFood.size());
    	plateIds.add(150 + plateFood.size());
    	RelativeLayout rewardLayout = (RelativeLayout) findViewById(R.id.reward_view);
    	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
    	    RelativeLayout.LayoutParams.WRAP_CONTENT,
    	    RelativeLayout.LayoutParams.WRAP_CONTENT);
    	layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    	rewardLayout.addView(plateItem, layoutParams);
    	
		
	}
	
	
//	private final SensorEventListener mSensorListener = new SensorEventListener(){
//		public void onSensorChanged(SensorEvent event){
//			float x = event.values[0];
//			float y = event.values[1];
//			float z = event.values[2];
//			mAccelLast = mAccelCurrent;
//			mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
//			float delta = mAccelCurrent - mAccelLast;
//			mAccel = mAccel * 0.9f + delta;
//			
//			
//			if(mAccel > 2){
//				shakeStart = true;
//			}
//			else{
//				if(shakeStart){
//					shakeStart = false;
//					feedWolf();
//				}
//			}
//		}
//
//	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//	    }
//
//	};
	
	protected void onResume() {
	    super.onResume();
//	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
	    super.onPause();
//	    mSensorManager.unregisterListener(mSensorListener);
	}
	public void resetRewards(String usedReward)
	{
	    LinearLayout layout = (LinearLayout) findViewById(R.id.rewardItemLinearLayout);
	    Integer removeReward = rewardIds.get(remainingRewards.indexOf(usedReward));

	    ImageView rewardObject = (ImageView) findViewById(removeReward.intValue());
		layout.removeView(rewardObject);
		
	    rewardIds.remove(remainingRewards.indexOf(usedReward));
	    remainingRewards.remove(usedReward);
	}
}
