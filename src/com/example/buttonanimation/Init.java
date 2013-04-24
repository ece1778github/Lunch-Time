package com.example.buttonanimation;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.SparseIntArray;

import com.example.lunchtime.R;

public class Init extends android.app.Application {
	
	private static Typeface centuryGothic;
	private static Typeface franklineGothicDemi;
	
	private static LayerDrawable star1;
	private static LayerDrawable star2;
	private static LayerDrawable star3;
	

    private static SparseIntArray clockMinutes;
    private static SparseIntArray clockHours;
	
    
    
    
    public void onCreate() {
        super.onCreate();

        // typeface caching
        initializeTypefaces();
        initializeStars();
        
        initializeVoiceClips();
    }
    
    private void initializeStars(){
    	star1 = starInit();
    	star2 = starInit();
    	star3 = starInit();
    }
    private LayerDrawable starInit(){
    	Resources r = getResources();
    	Drawable[] layers = new Drawable[2];
    	layers[0] = r.getDrawable(R.drawable.star_outline);
    	layers[1] = r.getDrawable(R.drawable.star_filled);
    	LayerDrawable star = new LayerDrawable(layers);
    	return star;
    }
    private void initializeTypefaces(){
    	centuryGothic   = Typeface.createFromAsset(getAssets(), "font/4365.ttf");
    	franklineGothicDemi   = Typeface.createFromAsset(getAssets(), "font/FGD.ttf");
    }
    ////////////////get functions//////////////////
    public static Typeface getFont(String type){
    	if (type.equals("centuryGothic")==true)
    		return centuryGothic;
    	else if(type.equals("franklineGothicDemi")==true)
    		return franklineGothicDemi;
    	else
    		return(null);
    }
    
    public static LayerDrawable getStar(String starName){
     	if (starName.equals("star1")) {
        	return star1;
     	}
     	else if(starName.equals("star2")){	
     		return star2;
     	}
     	else{
        	return star3;
        }
    }
    
    public static SparseIntArray getHourMap(){
    	
    	return clockHours;
    	
    }
    public static SparseIntArray getMinuteMap(){
    	
    	return clockMinutes;
    	
    }
    

    private void initializeVoiceClips(){
    	
    	clockMinutes = new SparseIntArray();
    	clockHours = new SparseIntArray();
    	
    	clockMinutes.put(0, R.raw.oclock);
    	clockMinutes.put(1, R.raw.minute1);
    	clockMinutes.put(2, R.raw.minute2);
    	clockMinutes.put(3, R.raw.minute3);
    	clockMinutes.put(4, R.raw.minute4);
    	clockMinutes.put(5, R.raw.minute5);
    	clockMinutes.put(6, R.raw.minute6);
    	clockMinutes.put(7, R.raw.minute7);
    	clockMinutes.put(8, R.raw.minute8);
    	clockMinutes.put(9, R.raw.minute9);
    	clockMinutes.put(10, R.raw.t10);
    	clockMinutes.put(11, R.raw.t11);
    	clockMinutes.put(12, R.raw.t12);
    	clockMinutes.put(13, R.raw.t13);
    	clockMinutes.put(14, R.raw.t14);
    	clockMinutes.put(15, R.raw.t15);
    	clockMinutes.put(16, R.raw.t16);
    	clockMinutes.put(17, R.raw.t17);
    	clockMinutes.put(18, R.raw.t18);
    	clockMinutes.put(19, R.raw.t19);
    	clockMinutes.put(20, R.raw.t20);
    	clockMinutes.put(21, R.raw.t21);
    	clockMinutes.put(22, R.raw.t22);
    	clockMinutes.put(23, R.raw.t23);
    	clockMinutes.put(24, R.raw.t24);
    	clockMinutes.put(25, R.raw.t25);
    	clockMinutes.put(26, R.raw.t26);
    	clockMinutes.put(27, R.raw.t27);
    	clockMinutes.put(28, R.raw.t28);
    	clockMinutes.put(29, R.raw.t29);
    	clockMinutes.put(30, R.raw.t30);
    	clockMinutes.put(31, R.raw.t31);
    	clockMinutes.put(32, R.raw.t32);
    	clockMinutes.put(33, R.raw.t33);
    	clockMinutes.put(34, R.raw.t34);
    	clockMinutes.put(35, R.raw.t35);
    	clockMinutes.put(36, R.raw.t36);
    	clockMinutes.put(37, R.raw.t37);
    	clockMinutes.put(38, R.raw.t38);
    	clockMinutes.put(39, R.raw.t39);
    	clockMinutes.put(40, R.raw.t40);
    	clockMinutes.put(41, R.raw.t41);
    	clockMinutes.put(42, R.raw.t42);
    	clockMinutes.put(43, R.raw.t43);
    	clockMinutes.put(44, R.raw.t44);
    	clockMinutes.put(45, R.raw.t45);
    	clockMinutes.put(46, R.raw.t46);
    	clockMinutes.put(47, R.raw.t47);
    	clockMinutes.put(48, R.raw.t48);
    	clockMinutes.put(49, R.raw.t49);
    	clockMinutes.put(50, R.raw.t50);
    	clockMinutes.put(51, R.raw.t51);
    	clockMinutes.put(52, R.raw.t52);
    	clockMinutes.put(53, R.raw.t53);
    	clockMinutes.put(54, R.raw.t54);
    	clockMinutes.put(55, R.raw.t55);
    	clockMinutes.put(56, R.raw.t56);
    	clockMinutes.put(57, R.raw.t57);
    	clockMinutes.put(58, R.raw.t58);
    	clockMinutes.put(59, R.raw.t59);

    	clockHours.put(0, R.raw.t12);
    	clockHours.put(1, R.raw.hour1);
    	clockHours.put(2, R.raw.hour2);
    	clockHours.put(3, R.raw.hour3);
    	clockHours.put(4, R.raw.hour4);
    	clockHours.put(5, R.raw.hour5);
    	clockHours.put(6, R.raw.hour6);
    	clockHours.put(7, R.raw.hour7);
    	clockHours.put(8, R.raw.hour8);
    	clockHours.put(9, R.raw.hour9);
    	clockHours.put(10, R.raw.t10);
    	clockHours.put(11, R.raw.t11);
    	clockHours.put(12, R.raw.t12);
    	
    }

    
    
    
}