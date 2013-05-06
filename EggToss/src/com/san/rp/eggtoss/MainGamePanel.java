/**
 * 
 */
package com.san.rp.eggtoss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.san.rp.eggtoss.model.Bowl;
import com.san.rp.eggtoss.model.Egg;
import com.san.rp.eggtoss.model.components.Point;
import com.san.rp.eggtoss.model.components.Speed;

/**
 * @author Rajendra
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	
	private static final String TAG = MainGamePanel.class.getSimpleName();
	
	//minimum distance between bowls
	private static final int BOWL_DISTANCE = 150;
	
	private GameThread thread;
	private Egg egg;
	private boolean surfaceCreated=false;
	
	private List<Bowl> bowls = new ArrayList<Bowl>();
	
	public MainGamePanel(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		surfaceCreated=false;
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
		 if (surfaceCreated == false)
	        {
			 	egg = new Egg(BitmapFactory.decodeResource(getResources(), R.drawable.egg), getWidth()/2, getHeight(), getHeight(), getWidth());
				//Bowl bowl = new Bowl(BitmapFactory.decodeResource(getResources(), R.drawable.bowl), getWidth()/2, 400);
				//bowls.add(bowl);
				addNewBowl();
				addNewBowl();
				addNewBowl();
	            createGameThread();
	            surfaceCreated = true;
	        }	
	}

	public void createGameThread(){
		if(null==thread || thread.getState()==Thread.State.TERMINATED) {
			// create the game loop thread
			thread = new GameThread(getHolder(), this);
		}
		thread.setRunning(true);
		thread.start();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		surfaceCreated = false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN &&
                event.getAction() != MotionEvent.ACTION_MOVE) {
            return false;
        }
		Log.d("Touch:", "==>x,y="+egg.getX()+","+egg.getY());
		if(!egg.isFlying()) {
			Point touchedAt=new Point((int)event.getX(),2*(int)event.getY());
			egg.getSpeed().setYv(20f);
			Point start=new Point(egg.getX(),egg.getY());//new Point(100,700));
			egg.setStart(start);
			egg.setFlyStartedAt(System.currentTimeMillis());
			egg.setMiddle(touchedAt);
			egg.setEnd(new Point(start.getX()+2*((int)event.getX()-start.getX()),start.getY()));
			egg.setFlying(true);
			Log.d("touch location ", touchedAt.toString());
			Log.d("start "+egg.getStart()+" middle="+egg.getMiddle()+" end="+egg.getEnd(),"****** All points");
			for(Bowl bowl : bowls) {
				bowl.getSpeed().setXv(10f);
			}
			
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
		for(Bowl bowl : bowls) {
			bowl.draw(canvas);
		}
	}

	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	public void update() {
				
		// Update the lone egg
		egg.update();
		/*for(Bowl bowl : bowls) {
			bowl.update();
		}
		if(egg.isFlying()) {
			detectCollision();
		}*/
	}

	private void detectCollision() {
		//for(int i = 0; i < bowls.size(); i++) {
		for(int i = bowls.size() -1; i >= 0; i--) {
			Bowl bowl = bowls.get(i);
			if (egg.getSpeed().getyDirection() == Speed.DIRECTION_DOWN) {
				Log.d("DetectCollesion Bowl"+i, bowl.toString());
				int delta = (int)(bowl.getBottomEdge() - (egg.getBottomEdge() + egg.getSpeed().getYv()));
				Log.d("Delta", delta+"");
				if (delta>= -5 && delta <= 5) {
					//String debug = "Egg"+egg.toString()+" and Bowl"+bowl.toString()+"";
					//Log.d("Colliding", debug);
					int y = bowl.getTopEdge()-(int)(egg.getBitmap().getHeight()/2);
					Log.d("Resetting Y", y+"");
					egg.setY(y);
					egg.getSpeed().setYv(0);
					//egg.setY(bowl.getTopEdge());
					egg.getSpeed().setyDirection(Speed.DIRECTION_UP);
					egg.setFlying(false);
				}
			}
			
		}
	}
	
	private void addNewBowl() {
		if(bowls.size() == 0) {
			Bowl bowl = new Bowl(BitmapFactory.decodeResource(getResources(), R.drawable.bowl), getWidth()/2, getHeight() - BOWL_DISTANCE, getHeight(), getWidth());
			bowl.getSpeed().setxDirection(Speed.DIRECTION_RIGHT);
			bowls.add(bowl);
		} else {
			Bowl previous = bowls.get(bowls.size() - 1);
			Bowl bowl = new Bowl(BitmapFactory.decodeResource(getResources(), R.drawable.bowl), getWidth()/2, previous.getY() - BOWL_DISTANCE,getHeight(), getWidth());
			bowl.getSpeed().setxDirection(previous.getSpeed().getxDirection()*-1);
			bowls.add(bowl);
		}
	}
	
	public void stopThread() {
		thread.setRunning(false);
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.e("MainGamePannel-StopThread", "terminateThread corrupts");
		}
	}
	
	public boolean isSurfaceCreated(){
		return surfaceCreated;
	}

}
