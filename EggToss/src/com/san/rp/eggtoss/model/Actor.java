/**
 * 
 */
package com.san.rp.eggtoss.model;

import com.san.rp.eggtoss.model.components.Speed;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * @author Rajendra
 *
 */
public class Actor {
	
	protected Bitmap bitmap; // source image
	
	private int x;			// the X coordinate
	private int y;			// the Y coordinate
	
	private int parentHeight;			// the X coordinate
	private int parentWidth;			// the Y coordinate
	
	private Speed speed;	// the speed with its directions
	
	//these are define the bounderies of the bitmap
	//might not be OK to consider all bitmaps as rectangles but this should be ok for this tiny game
	private int topEdge;
	private int bottomEdge;
	private int rightEdge;
	private int leftEdge;
	
	public Actor(Bitmap bitmap, int x, int y, int parentHeight, int parentWidth) {
		this.bitmap = bitmap;
		this.setX(x);
		this.setY(y - (bitmap.getHeight() / 2));
		this.speed = new Speed();
		this.parentHeight=parentHeight;
		this.parentWidth=parentWidth;
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, getX() - (bitmap.getWidth() / 2), getY() - (bitmap.getHeight() / 2), null);
	}
	
	public void update() {
		//super class doesnt do any updates
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public Speed getSpeed() {
		return speed;
	}
	public void setSpeed(Speed speed) {
		this.speed = speed;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the topEdge
	 */
	public int getTopEdge() {
		if(bitmap != null) {
			topEdge = y - (bitmap.getHeight()/2);
		}
		return topEdge;
	}

	/**
	 * @return the bottomEdge
	 */
	public int getBottomEdge() {
		if(bitmap != null) {
			bottomEdge = y + (bitmap.getHeight()/2);
		}
		return bottomEdge;
	}

	/**
	 * @return the rightEdge
	 */
	public int getRightEdge() {
		if(bitmap != null) {
			rightEdge = x + (bitmap.getWidth()/2);
		}
		return rightEdge;
	}

	/**
	 * @return the leftEdge
	 */
	public int getLeftEdge() {
		if(bitmap != null) {
			leftEdge = x - (bitmap.getWidth()/2);
		}
		return leftEdge;
	}
	
	@Override
	public String toString() {
		return "(x,y)=>("+x+","+y+")";
	}
	
	public int getParentHeight(){
		return parentHeight;
	}
	
	public int getParentWidth(){
		return parentWidth;
	}
}
