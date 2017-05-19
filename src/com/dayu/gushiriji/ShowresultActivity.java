package com.dayu.gushiriji;

import android.os.Bundle;
import android.text.TextPaint;
import android.R.color;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

public class ShowresultActivity extends Activity
{
	int Screenwidth,Screenheight;
	boolean iskeepgoing =  true;
	boolean touchscreen  = false;
	
	
	float t_x,t_y;
	/*
	 * 这个类用来当测试的物件，会沿着方形路线持续移动
	 */
	class GameObject {
		private float x;
		private float y;
		private Bitmap img;
		private Paint paint;

		public GameObject() {
			this.img = BitmapFactory.decodeResource(getResources(), R.drawable.p1181494);
			this.x = 50;
			this.y = 50;
			this.paint = new Paint();
			paint.setColor(Color.RED);
		}

		// 在SurfaceView加锁同步后传给自己的Canvas上绘制自己
		public void drawSelf(Canvas canvas) {
			
			//canvas.drawBitmap(img, x, y, paint);
			RectF oval2 = new RectF(60, 100, 200, 240);// 设置个新的长方形，扫描测量  
			this.y = y+30;
			canvas.drawArc(oval2, 200, y, true, paint);
			if (y>200)
			{
				canvas.restore();
				y = 50;
			}
			
			if (touchscreen)
			{
				TextPaint tp = new TextPaint();
				tp.setColor(Color.WHITE);
				tp.setShadowLayer(3, 5, 5, Color.BLACK);
				tp.setTextSize(30);
				
				canvas.drawText("你点击了屏幕\n"+ String.valueOf(t_x)
					+ "\n" + String.valueOf(t_y)	, 20, 300, tp);
				
				 Paint photoPaint = new Paint();
				 photoPaint.setDither(true); //获取跟清晰的图像采样  
		  	       photoPaint.setFilterBitmap(true);//过滤一些  
			
				 Rect rf = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
			//	 canvas.drawBitmap(img, null, rf, photoPaint);
                 canvas.drawBitmap(img, t_x, t_y-150, photoPaint);
				touchscreen = false;
			}
		}

		// 获取物件下一次要绘制的位置(这里是沿着一个边长为400的正方形不断运动的)
		/*public float getNextAngle()
		{
			
		}*/
		
			
		
		public void getNextPos() {
			if (y == 50 && x != 300)
				x += 5;
			else if (x == 300 && y != 300)
				y += 5;
			else if (y == 300 && x != 50)
				x -= 5;
			else if (x == 50 && y != 50)
				y -= 5;
		}
	}

	/*
	 * 这个类就是加工了SurfaceView之后的类，所有要运动的物件都最终放在这里进行绘制
	 */
	class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
		private Thread thread; // SurfaceView通常需要自己单独的线程来播放动画
		private Canvas canvas;
		private SurfaceHolder surfaceHolder;
		
		private GameObject obj;

		public MySurfaceView(Context c) {
			super(c);
			this.surfaceHolder = this.getHolder();
			this.surfaceHolder.addCallback(this);
			this.obj = new GameObject();
		}

		@Override
		public void run() {
			while ( iskeepgoing ) {
			//	obj.getNextPos();
				    
					canvas = this.surfaceHolder.lockCanvas(); // 通过lockCanvas加锁并得到該SurfaceView的画布
				//	canvas.drawColor(Color.BLACK);
					 Paint photoPaint = new Paint(); //建立画笔  
					 Bitmap bf = BitmapFactory.decodeResource(getResources(),R.drawable.p20150411_201603 ); 
					 int width = bf.getWidth(), hight = bf.getHeight();
					 
					 Matrix mt = new Matrix();
					 mt.setScale((float)Screenwidth/width, (float)Screenheight/hight);
		  	       photoPaint.setDither(true); //获取跟清晰的图像采样  
		  	       photoPaint.setFilterBitmap(true);//过滤一些  
		  	      Rect rf = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
					canvas.drawBitmap(bf, null, rf, photoPaint);
					
					TextPaint tp = new TextPaint();
					tp.setColor(Color.WHITE);
					tp.setShadowLayer(3, 5, 5, Color.BLACK);
					tp.setTextSize(40);
					tp.setStrokeMiter(5);
					tp.setStrokeWidth(5);
					canvas.drawText("welcome to 爪哇", 20, 500, tp);
					
					
				//	canvas.drawBitmap( bf, mt, photoPaint);
					obj.drawSelf(canvas); // 把SurfaceView的画布传给物件，物件会用这个画布将自己绘制到上面的某个位置
					this.surfaceHolder.unlockCanvasAndPost(canvas); // 释放锁并提交画布进行重绘
					try {
						Thread.sleep(100); // 这个就相当于帧频了，数值越小画面就越流畅
					} catch (Exception e) {
						e.printStackTrace();
					}
				
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			Toast.makeText(getApplicationContext(), "SurfaceView已经销毁", Toast.LENGTH_LONG).show();
			this.thread = null;
		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			Toast.makeText(getApplicationContext(), "SurfaceView已经创建", Toast.LENGTH_LONG).show();
			
			this.thread = new Thread(this);
			this.thread.start();
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
			// 这里是SurfaceView发生变化的时候触发的部分
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.showresult);
	      this.Screenwidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels; 
	      this.Screenheight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
	     
	//	setContentView(new MySurfaceView(getApplicationContext()));
		
	}
	
	

	@Override
	protected void onPause()
	{
		iskeepgoing = false;
		super.onPause();
	}



	@Override
	protected void onDestroy()
	{
		iskeepgoing = false;
		super.onDestroy();
	}

    

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		t_x = event.getRawX();
		t_y = event.getRawY();
		
		touchscreen = true;
		return super.onTouchEvent(event);
	}



	@Override
	protected void onResume()
	{
		iskeepgoing = true;
	//	new MySurfaceView(getApplicationContext());
		super.onResume();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.showresult, menu);
		return true;
	}

}
