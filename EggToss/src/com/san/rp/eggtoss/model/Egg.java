/**
 * 
 */
package com.san.rp.eggtoss.model;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;

import com.san.rp.eggtoss.model.components.BezierUtil;
import com.san.rp.eggtoss.model.components.Point;
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
	private Point start;
	private Point end;
	private Point middle;
	
	//time vars in milliseconds.
	private long flyTime=2000; 
	private long flyStartedAt;
	private float interpolatedTime=0;
	private int eggWidth=20;
	
	
	public Egg(Bitmap bitmap, int x, int y, int parentHeight, int parentWidth) {
		super(bitmap, x, y, parentHeight,parentWidth);
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
		
		if(interpolatedTime>1 || start==null){
			interpolatedTime=0;
			flyStartedAt=0;
			setFlying(false);
		}else {
			interpolatedTime=(System.currentTimeMillis()-flyStartedAt)/(flyTime*1.0f);
		}
		
		if(!isFlying())
			return;
		
		applyGravity();		
		
		if (!touched) {
			/*setX(getX() + (int)(getSpeed().getXv() * getSpeed().getxDirection())); 
			setY(getY() + (int)(getSpeed().getYv() * getSpeed().getyDirection()));*/
			int xValToSet = (int)(BezierUtil.calcBezier(interpolatedTime, start.getX(), middle.getX(), end.getX()));
			int yValToSet = (int)(BezierUtil.calcBezier(interpolatedTime, start.getY(),middle.getY(),end.getY()));
			if(xValToSet-10<0)
				xValToSet=0;
			if(xValToSet+eggWidth>getParentWidth())
				xValToSet=getParentWidth()-eggWidth;
			setX(xValToSet); 
			setY(yValToSet);
			Log.d("Updated Egg status :  "+interpolatedTime,this.toString());
		}
		
		// check collision with right wall if heading right
		if (getSpeed().getxDirection() == Speed.DIRECTION_RIGHT
				&& getX() + getBitmap().getWidth() / 2 >= getParentWidth()) {
			getSpeed().toggleXDirection();
		}
		// check collision with left wall if heading left
		if (getSpeed().getxDirection() == Speed.DIRECTION_LEFT
				&& getX() - getBitmap().getWidth() / 2 <= 0) {
			getSpeed().toggleXDirection();
		}
		// check collision with bottom wall if heading down
		//if (getSpeed().getyDirection() == Speed.DIRECTION_DOWN) {
			//getSpeed().setYv(getSpeed().getYv() + 1);
			if (getY() + getBitmap().getHeight() / 2 >= getParentHeight()) {
				getSpeed().toggleYDirection();
				getSpeed().setYv(0);
				setFlying(false);
			}
		//}
		// check collision with top wall if heading up
		if (getSpeed().getyDirection() == Speed.DIRECTION_UP) {
			if((getY() - getBitmap().getHeight() / 2 <= 0)) {
				Log.d("Hits top", "Hitstop"+toString());
				getSpeed().toggleYDirection();
				getSpeed().setYv(1f);
			} else if (isFlying() && getSpeed().getYv() <= 0) {
				getSpeed().toggleYDirection();
			}
			
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


	public Point getStart() {
		return start;
	}


	public void setStart(Point start) {
		this.start = start;
	}


	public Point getEnd() {
		return end;
	}


	public void setEnd(Point end) {
		this.end = end;
	}


	public Point getMiddle() {
		return middle;
	}


	public void setMiddle(Point middle) {
		this.middle = middle;
	}


	public long getFlyStartedAt() {
		return flyStartedAt;
	}


	public void setFlyStartedAt(long flyStartedAt) {
		this.flyStartedAt = flyStartedAt;
	}
	
	
}
