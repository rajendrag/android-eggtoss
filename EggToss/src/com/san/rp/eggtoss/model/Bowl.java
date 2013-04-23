/**
 * 
 */
package com.san.rp.eggtoss.model;

import com.san.rp.eggtoss.model.components.Speed;

import android.graphics.Bitmap;

/**
 * @author Rajendra
 *
 */
public class Bowl extends Actor{

	public Bowl(Bitmap bitmap, int x, int y, int parentHeight, int parentWidth) {
		super(bitmap, x, y, parentHeight, parentWidth);
	}
	
	public void update() {
		setX(getX() + (int)(getSpeed().getXv() * getSpeed().getxDirection()));
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
	}
}
