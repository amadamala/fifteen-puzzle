package com.g1.android.puzzle;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * N puzzle or Fifteen Puzzle game.
 */
public class FifteenPuzzle extends Activity implements OnGestureListener {

	GestureDetector mGestureDetector;

	private static final int DIM = 4;
	private int mEmptyCell = DIM * DIM;
	private static boolean NOT_FINISHED = true;
	private static boolean FINISHED = false;
    private static int EMPTY_TILE_VAL = 0;
	private static final String TAG = "15 Puzzle";
	private long mStartTime = 0L;
	private long mEndTime = 0L;
	private boolean mGameStatus = NOT_FINISHED;
	private Toast mCurrentToast;

	private Integer[] btnIds = { R.id.Button0, R.id.Button1, R.id.Button2,
			R.id.Button3, R.id.Button4, R.id.Button5, R.id.Button6,
			R.id.Button7, R.id.Button8, R.id.Button9, R.id.Button10,
			R.id.Button11, R.id.Button12, R.id.Button13, R.id.Button14,
			R.id.Button15 };

	private Integer[] btnVals = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
			14, 15 };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setIsLongpressEnabled(false);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.npuzzle);
		initialize();
	}

	/* Repeat until creation of solvable initial board */
	private WindowManager mWindowMgr;
	private Display mDisplay;

	private void initialize() {
		mWindowMgr = getWindowManager();
		mDisplay = mWindowMgr.getDefaultDisplay();

		mStartTime = System.nanoTime();
		Bitmap b = resize(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.aum));
		// b = resize(b);

		makeTilesOf(b, mDisplay);
		b = null; // d = null; w = null;
		for (boolean isSolvable = false; isSolvable == false;) {
			Collections.shuffle(Arrays.asList(btnVals));
			isSolvable = isSolvable(btnVals);
			Log.i(TAG, "Random State:: " + Arrays.asList(btnVals));
		}

		initTile();
	}

	private void initTile() {
		ImageButton tile = null;
		int val;
		for (int i = 0; i < 16; i++) {
			val = btnVals[i];
			tile = (ImageButton) findViewById(btnIds[i]);
			tile.setTag(Integer.toString(val));
			if (val != 0) {
				tile.setImageBitmap(tiles[val - 1]);
			}
			if (val == 0) {
				mEmptyCell = i;
				tile.setVisibility(View.INVISIBLE);
			}
			tile.setOnTouchListener(mTouch);
		}

	}

	// Resize the Bitmap image
	private Bitmap resize(Bitmap bm) {

		mWindowMgr = getWindowManager();
		mDisplay = mWindowMgr.getDefaultDisplay();

		int bitmapWidth = bm.getWidth();
		int bitmapHeight = bm.getHeight();

		float scaleWidth = ((float) mDisplay.getWidth()) / bitmapWidth;
		float scaleHeight = ((float) mDisplay.getHeight()) / bitmapHeight;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bitmapWidth,
				bitmapHeight, matrix, true);
		return resizedBitmap;
	}

	/**
	 * Verifies the board for solvability.For more details of solvability goto
	 * URL: http://mathworld.wolfram.com/15Puzzle.html
	 */
	private boolean isSolvable(Integer[] board) {
		int inversionSum = 0; // If this sum is even, then it is solvable
		for (int i = 0; i < board.length; i++) {
			// For empty square add the row number to inversionSum
			if (board[i] == 0) {
				inversionSum += ((i / DIM) + 1); // add the Row number
				continue;
			}
			int count = 0;
			for (int j = i + 1; j < board.length; j++) {
				if (board[j] == 0) {
					continue;
				} else if (board[i] > board[j]) {
					count++;
				}
			}
			inversionSum += count;
		}

		// if inversionSum is even then the game is solvable
		return ((inversionSum % 2) == 0) ? true : false;
	}

	// divides the image into tiles
	private Bitmap[] tiles = new Bitmap[DIM * DIM];

	private void makeTilesOf(Bitmap b, Display d) {

		// TODO: change the code after changing onsizechanged
		final int TITLE_HEIGHT = 40;

		int tileWidth = (d.getWidth() / DIM);
		int tileHeight = ((d.getHeight() - TITLE_HEIGHT) / DIM);

		for (int r = 0, c = 0, x = 0, y = 0, i = 0; i < tiles.length; i++) {
			r = i / DIM;
			c = i % DIM;

			x = (c * tileWidth);
			y = (r * tileHeight);
			tiles[i] = Bitmap.createBitmap(b, x, y, tileWidth, tileHeight);
		}

	}

	/**
	 * After each click this will decide how to move the button
	 */
	private void move(int row, int col) {

		final int eRow = mEmptyCell / DIM;
		final int eCol = mEmptyCell % DIM;
		final int rDiff = Math.abs(eRow - row);
		final int cDiff = Math.abs(eCol - col);

		if (mGameStatus == FINISHED) { // if game already finished
			return;
		}
		
		
		//int diff = Math.abs(cDiff);
		int target;
		int src;

		if(dir == LEFT){
			target = eRow * DIM + eCol;
			src = target  + 1;
			for (int i = 0; i < cDiff; i++) {
				update(target++, src++);
			}
		} else if(dir == RIGHT){
			target = eRow * DIM + eCol;
			src = target - 1;
			for (int i = 0; i < cDiff; i++) {
				update(target--, src--);
			}
		}

		//diff = Math.abs(rDiff);
		if (dir == UP) { // -ve difference, move column up
			target = ((eRow-1) * DIM) + eCol;
			src = target + DIM;
			for (int i = 0; i < rDiff; i++) {
				update(target += DIM, src += DIM);
			}
		} else if (dir == DOWN) { // + ve difference, move column down
			target = ((eRow + 1) * DIM) + eCol;
			src = target - DIM;
			for (int i = 0; i < rDiff; i++) {
				update(target -= DIM, src -= DIM);
				
			}
		}

		// Swap the empty square(target) with the clicked square
		src = (row * DIM) + col;
		target = (eRow * DIM) + eCol;
		btnVals[src] = EMPTY_TILE_VAL;
		
		ImageButton tile = (ImageButton)findViewById(btnIds[src]);
		tile.setTag(btnVals[src].toString());
		
		tile = (ImageButton) findViewById(btnIds[target]);
		tile.setVisibility(View.VISIBLE);

		tile = (ImageButton) findViewById(btnIds[src]);
		tile.setVisibility(View.INVISIBLE);
		
		mEmptyCell = src;
	}

	private int dir = 0; // Direction of the fling
	public OnTouchListener mTouch = new OnTouchListener() {

		public boolean onTouch(View view, MotionEvent event) {
			if (mGameStatus == FINISHED) {
				return false;
			}
			if (event.getAction() == MotionEvent.ACTION_UP && dir != 0) {

				ImageButton tile = (ImageButton) findViewById(view.getId());
				int val = Integer.parseInt(tile.getTag().toString());
				int index = positionOf(val);
				int row = index / DIM;
				int col = index % DIM;

				if (isValidTouch(row, col, dir)) {
					move(index / DIM, index % DIM);
					if (isFinished(btnVals)) {
						afterFinish();
					}
				}
			}

			return true;
		}

	};

	/* Update btnVals list and change the tag on buttons */
	private void update(int target, int src) {
		// update btnVals
		ImageButton srcTile = (ImageButton) findViewById(btnIds[src]);
		int srcTagVal = Integer.parseInt(srcTile.getTag().toString());
		btnVals[target] = srcTagVal;
		// change target ImageButton tag
		ImageButton targetTile = ((ImageButton) findViewById(btnIds[target]));
		int destTagVal = Integer.parseInt(srcTile.getTag().toString());
		targetTile.setTag(srcTile.getTag());
		// swap imagebutton background
		srcTile.setImageBitmap(tiles[destTagVal - 1]);
		targetTile.setImageBitmap(tiles[srcTagVal - 1]);

	}

	/* If the drag and empty box are in the same direction returns true */
	private static final int RIGHT = 1;
	private static final int LEFT = 2;
	private static final int DOWN = 3;
	private static final int UP = 4;

	private boolean isValidTouch(int row, int col, final int dir) {

		final int eRow = mEmptyCell / DIM;
		final int eCol = mEmptyCell % DIM;
		final int rDiff = eRow - row;
		final int cDiff = eCol - col;

		final boolean isInRow = (row == eRow);
		final boolean isInCol = (col == eCol);

		if (isInRow) {
			return (cDiff > 0 && dir == RIGHT) || ((cDiff < 0 && dir == LEFT));
		} else if (isInCol) {
			return (rDiff > 0 && dir == DOWN) || ((rDiff < 0 && dir == UP));
		}
		return false;
	}

	/**
	 * Checks whether the game is finished or not
	 */
	public boolean isFinished(Integer[] vals) {

		for (int i = 1; i <= vals.length - 1; i++)
			if (vals[i - 1] != i)
				return false;
		// afterFinish();
		return true;
	}

	// If game finished record the end time and set the game status as finished
	private void afterFinish() {
		if (mGameStatus == NOT_FINISHED) {
			mEndTime = System.nanoTime();
			mGameStatus = FINISHED;
		}

		// Fill the empty button and show toast
		ImageButton b1 = (ImageButton) findViewById(btnIds[btnIds.length - 1]);
		b1.setImageBitmap(tiles[tiles.length - 1]);
		b1.setVisibility(View.VISIBLE);
		showToast(getString(R.string.win_text));

	}

	private void showToast(String text) {
		cancelToasts();
		mCurrentToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		mCurrentToast.show();
	}

	private void cancelToasts() {
		if (mCurrentToast != null) {
			mCurrentToast.cancel();
			mCurrentToast = null;
		}
	}

	/* Gives the index by processing the text on cell */
	private int positionOf(int val) {

		for (int i = 0; i < 16; i++) {
			if (btnVals[i] == val)
				return i;
		}

		throw (new IllegalStateException("Index should be [0-15]"));
	}

	private void onSizeChanged() {
		Bitmap b = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.aum);
		final int TITLE_HEIGHT = 40;
		WindowManager w = getWindowManager();
		Display d = w.getDefaultDisplay();

		b = resize(b);
		makeTilesOf(b, d);

		int width = d.getWidth();
		int height = d.getHeight() - TITLE_HEIGHT;

		// Release the memory
		b = null; //d = null; w = null;
		Log.d(TAG, "button width:: " + width + ", height:: " + height);
		// setting button dimensions
		resizeTile(width / DIM, height / DIM);
		initTile();
	}

	// setting button dimensions
	private void resizeTile(int width, int height) {
		ImageButton tile = null;
		for (int i = 0; i < btnIds.length; i++) {
			tile = (ImageButton) findViewById(btnIds[i]);
			tile.setMinimumWidth(width);
			tile.setMinimumHeight(height);
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "New Game");
		menu.add(0, 1, 0, "Image");
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.removeItem(2);
		TimeUnit unit = TimeUnit.SECONDS;
		long time;
		if (mGameStatus == NOT_FINISHED) {
			time = unit.convert((System.nanoTime() - mStartTime),
					TimeUnit.NANOSECONDS);
			menu.add(0, 2, 0, "time:" + time);
		} else {
			time = unit.convert((mEndTime - mStartTime), TimeUnit.NANOSECONDS);
			menu.add(0, 2, 0, "time:" + time);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 0:
			Log.v(TAG, "New Game");
			cancelToasts();
			Intent intent = new Intent(this, FifteenPuzzle.class);
			startActivity(intent);
			finish();
			return true;
		case 1:
			Intent imgIntent = new Intent(this, ReferenceImage.class);
			startActivity(imgIntent);
			return true;
		case 2:
			return true;
		}

		return false;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		onSizeChanged();
	}

	public boolean onDown(MotionEvent e) {
		return true;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		dir = 0;
		dir = (Math.abs(velocityX) > Math.abs(velocityY)) ? ((velocityX > 0) ? RIGHT
				: LEFT) // X-axis
				: ((velocityY > 0) ? DOWN : UP); // Y-axis
		return true;
	}

	public void onLongPress(MotionEvent e) {
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distX,
			float distY) {
		return false;
	}

	public void onShowPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean returnValue = false;
		returnValue = returnValue | mGestureDetector.onTouchEvent(ev);
		returnValue = returnValue | super.dispatchTouchEvent(ev);
		return returnValue;
	}
	
}

