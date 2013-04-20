/**
 * 
 */
package com.san.rp.eggtoss;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.san.rp.eggtoss.model.Egg;
import com.san.rp.eggtoss.model.components.Speed;

/**
 * @author Rajendra
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	
	private static final String TAG = MainGamePanel.class.getSimpleName();
	
	private GameThread thread;
	private Egg egg;

	public MainGamePanel(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create the game loop thread
		thread = new GameThread(getHolder(), this);
		
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d("surfaceChanged", "x,y"+width+","+height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		egg = new Egg(BitmapFactory.decodeResource(getResources(), R.drawable.egg), getWidth()/2, getHeight());
				
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("Touch:", "==>x,y="+egg.getX()+","+egg.getY());
		if(!egg.isFlying()) {
			egg.getSpeed().setYv(20f);
			egg.setFlying(true);
		}
		/*
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// delegating event handling to the egg
			egg.handleActionDown((int)event.getX(), (int)event.getY());
			
			// check if in the lower part of the screen we exit
			if (event.getY() > getHeight() - 50) {
				thread.setRunning(false);
				((Activity)getContext()).finish();
			} else {
				Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
			}
		} if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// the gestures
			if (egg.isTouched()) {
				// the egg was picked up and is being dragged
				egg.setX((int)event.getX());
				egg.setY((int)event.getY());
			}
		} if (event.getAction() == MotionEvent.ACTION_UP) {
			// touch was released
			if (egg.isTouched()) {
				egg.setTouched(false);
			}
		}*/
		return true;
	}

	public void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		egg.draw(canvas);
	}

	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	public void update() {
		// check collision with right wall if heading right
		if (egg.getSpeed().getxDirection() == Speed.DIRECTION_RIGHT
				&& egg.getX() + egg.getBitmap().getWidth() / 2 >= getWidth()) {
			egg.getSpeed().toggleXDirection();
		}
		// check collision with left wall if heading left
		if (egg.getSpeed().getxDirection() == Speed.DIRECTION_LEFT
				&& egg.getX() - egg.getBitmap().getWidth() / 2 <= 0) {
			egg.getSpeed().toggleXDirection();
		}
		// check collision with bottom wall if heading down
		if (egg.getSpeed().getyDirection() == Speed.DIRECTION_DOWN) {
			//egg.getSpeed().setYv(egg.getSpeed().getYv() + 1);
			if (egg.getY() + egg.getBitmap().getHeight() / 2 >= getHeight()) {
				egg.getSpeed().toggleYDirection();
				egg.getSpeed().setYv(0);
				egg.setFlying(false);
			}
		}
		// check collision with top wall if heading up
		if (egg.getSpeed().getyDirection() == Speed.DIRECTION_UP) {
			//egg.getSpeed().setYv(egg.getSpeed().getYv() - 1);
			//if((egg.getSpeed().getYv() <= 0) || (egg.getY() - egg.getBitmap().getHeight() / 2 <= 0)) {
			if((egg.getY() - egg.getBitmap().getHeight() / 2 <= 0)) {
				egg.getSpeed().toggleYDirection();
			} else if (egg.isFlying() && egg.getSpeed().getYv() <= 0) {
				egg.getSpeed().toggleYDirection();
			}
			
		}
		// Update the lone egg
		egg.update();
	}

}
