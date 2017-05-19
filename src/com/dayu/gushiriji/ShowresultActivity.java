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
	 * ��������������Ե�����������ŷ���·�߳����ƶ�
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

		// ��SurfaceView����ͬ���󴫸��Լ���Canvas�ϻ����Լ�
		public void drawSelf(Canvas canvas) {
			
			//canvas.drawBitmap(img, x, y, paint);
			RectF oval2 = new RectF(60, 100, 200, 240);// ���ø��µĳ����Σ�ɨ�����  
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
				
				canvas.drawText("��������Ļ\n"+ String.valueOf(t_x)
					+ "\n" + String.valueOf(t_y)	, 20, 300, tp);
				
				 Paint photoPaint = new Paint();
				 photoPaint.setDither(true); //��ȡ��������ͼ�����  
		  	       photoPaint.setFilterBitmap(true);//����һЩ  
			
				 Rect rf = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
			//	 canvas.drawBitmap(img, null, rf, photoPaint);
                 canvas.drawBitmap(img, t_x, t_y-150, photoPaint);
				touchscreen = false;
			}
		}

		// ��ȡ�����һ��Ҫ���Ƶ�λ��(����������һ���߳�Ϊ400�������β����˶���)
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
	 * �������Ǽӹ���SurfaceView֮����࣬����Ҫ�˶�����������շ���������л���
	 */
	class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
		private Thread thread; // SurfaceViewͨ����Ҫ�Լ��������߳������Ŷ���
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
				    
					canvas = this.surfaceHolder.lockCanvas(); // ͨ��lockCanvas�������õ�ԓSurfaceView�Ļ���
				//	canvas.drawColor(Color.BLACK);
					 Paint photoPaint = new Paint(); //��������  
					 Bitmap bf = BitmapFactory.decodeResource(getResources(),R.drawable.p20150411_201603 ); 
					 int width = bf.getWidth(), hight = bf.getHeight();
					 
					 Matrix mt = new Matrix();
					 mt.setScale((float)Screenwidth/width, (float)Screenheight/hight);
		  	       photoPaint.setDither(true); //��ȡ��������ͼ�����  
		  	       photoPaint.setFilterBitmap(true);//����һЩ  
		  	      Rect rf = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
					canvas.drawBitmap(bf, null, rf, photoPaint);
					
					TextPaint tp = new TextPaint();
					tp.setColor(Color.WHITE);
					tp.setShadowLayer(3, 5, 5, Color.BLACK);
					tp.setTextSize(40);
					tp.setStrokeMiter(5);
					tp.setStrokeWidth(5);
					canvas.drawText("welcome to צ��", 20, 500, tp);
					
					
				//	canvas.drawBitmap( bf, mt, photoPaint);
					obj.drawSelf(canvas); // ��SurfaceView�Ļ�����������������������������Լ����Ƶ������ĳ��λ��
					this.surfaceHolder.unlockCanvasAndPost(canvas); // �ͷ������ύ���������ػ�
					try {
						Thread.sleep(100); // ������൱��֡Ƶ�ˣ���ֵԽС�����Խ����
					} catch (Exception e) {
						e.printStackTrace();
					}
				
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			Toast.makeText(getApplicationContext(), "SurfaceView�Ѿ�����", Toast.LENGTH_LONG).show();
			this.thread = null;
		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			Toast.makeText(getApplicationContext(), "SurfaceView�Ѿ�����", Toast.LENGTH_LONG).show();
			
			this.thread = new Thread(this);
			this.thread.start();
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
			// ������SurfaceView�����仯��ʱ�򴥷��Ĳ���
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
