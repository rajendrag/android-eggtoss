/**
 * 
 */
package com.san.rp.eggtoss.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.san.rp.eggtoss.model.components.Speed;

/**
 * This is the main GameObject or Actor of out game that can caught by a basket or broken.
 * 
 * @author Rajendra
 *
 */
public class Egg extends Actor{

	//private Bitmap bitmap;	// the actual bitmap
	//private int x;			// the X coordinate
	//private int y;			// the Y coordinate
	private boolean touched;	// if droid is touched/picked up
	//private Speed speed;	// the speed with its directions
	private boolean flying;
	
	public Egg(Bitmap bitmap, int x, int y) {
		super(bitmap, x, y);
	}
	

	public boolean isTouched() {
		return touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}

	/**
	 * Method which updates the droid's internal state every tick
	 */
	@Override
	public void update() {
		applyGravity();
		if (!touched) {
			setX(getX() + (int)(getSpeed().getXv() * getSpeed().getxDirection())); 
			setY(getY() + (int)(getSpeed().getYv() * getSpeed().getyDirection()));
		}
	}
	
	public void applyGravity() {
		if(flying) {
			float cv = getSpeed().getYv();
			String direction = null;
			if(getSpeed().getyDirection() == Speed.DIRECTION_UP) {
				getSpeed().setYv(cv-1);
				direction = "up";
			} else {
				getSpeed().setYv(cv+1);
				direction = "down";
			}
			Log.d("Direction & speed=", direction+" & "+ cv);
		}
	}
	
	/**
	 * Handles the {@link MotionEvent.ACTION_DOWN} event. If the event happens on the 
	 * bitmap surface then the touched state is set to <code>true</code> otherwise to <code>false</code>
	 * @param eventX - the event's X coordinate
	 * @param eventY - the event's Y coordinate
	 */
	public void handleActionDown(int eventX, int eventY) {
		if (eventX >= (getX() - bitmap.getWidth() / 2) && (eventX <= (getX() + bitmap.getWidth()/2))) {
			if (eventY >= (getY() - bitmap.getHeight() / 2) && (getY() <= (getY() + bitmap.getHeight() / 2))) {
				// droid touched
				setTouched(true);
			} else {
				setTouched(false);
			}
		} else {
			setTouched(false);
		}

	}

	public void intersect() {
		
	}
	public boolean isFlying() {
		return flying;
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}
}
