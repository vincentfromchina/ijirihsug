package com.dayu.gushiriji;

import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.dayu.gushiriji.c.DBHelper;
import com.dayu.gushiriji.m.Stock;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Showresult extends View
{

	 private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG  
             | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG  
             | Canvas.CLIP_TO_LAYER_SAVE_FLAG;  
	private Paint mPaint ;
	private Bitmap bp, bitmap2;
	private DBHelper sqldb;
	 InputStream is;
	private String sum_zonglirun,sum_yongjin,sum_buytimes,sum_selltimes,
	sum_zongjine,sum_completed,sum_fenhong,max_zonglirun,max_lirunpercent,
	max_cjrq,min_cjrq;
	private TextPaint tp;
	 Options opts;
	int x=100,y=100;
	
	public Showresult(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mPaint = new Paint();
	    tp = new TextPaint();
		tp.setColor(Color.RED);
		tp.setShadowLayer(3, 1, 1, Color.BLACK);
		tp.setTextSize(50);
		 opts = new Options();
		 opts.inInputShareable = true;
		 opts.inPreferredConfig = Config.RGB_565;
		 
		 bp =  BitmapFactory.decodeResource(getResources(), R.drawable.cgcjd, opts);
		
		 
		 sqldb = new DBHelper(context,"stockdb.db", null) ;
		 Cursor c = null;
		 
		 
		 c = sqldb.query_Sum_zonglirun();
		 if (c!=null)	
		  {
			 c.moveToFirst();
			 sum_zonglirun = c.getString(0) + " Ԫ";
			 c.close();
		  }
		 
		 c = sqldb.query_Sum_zongyongjin();
		 if (c!=null)	
		  {
			 c.moveToFirst();
			 sum_yongjin = c.getString(0) + " Ԫ";
			 c.close();
		  }
		 
		 c = sqldb.query_Sum_fenhong();
		 if (c!=null)	
		  {
			 c.moveToFirst();
			 sum_fenhong = c.getString(0) + " Ԫ";
			 c.close();
		  }
		 
		 c = sqldb.query_Sum_buycishu();
		 if (c!=null)	
		  {
			 c.moveToFirst();
			 sum_buytimes = c.getString(0) + "��";
			 c.close();
		  }
		 
		 c = sqldb.query_Sum_sellcishu();
		 if (c!=null)	
		  {
			 c.moveToFirst();
			 sum_selltimes = c.getString(0) + "��";
			 c.close();
		  }
		 
		 c = sqldb.query_Sum_zongjine();
		 if (c!=null)	
		  {
			 c.moveToFirst();
			 sum_zongjine = c.getString(0) + " Ԫ";
			 c.close();
		  }	
		 
		 c = sqldb.query_Sum_completed();
		 if (c!=null)	
		  {
			 c.moveToFirst();
			 sum_completed = c.getString(0) + " ֧��Ʊ";
			 c.close();
		  }
		 
		 c = sqldb.query_zonglirun( false );
		 if (c!=null)	
		  {
			 c.moveToFirst();
			 max_zonglirun = c.getString(1) +" "+ c.getString(2) +" Ԫ";
			 c.close();
		  }
		 
		 c = sqldb.query_yinlipercent( false );
		 if (c!=null)	
		  {
			 c.moveToFirst();
			 max_lirunpercent = c.getString(1) +" "+ Stock.return2decimal(c.getString(4)) +"%";
			 c.close();
		  }
		 
		 c = sqldb.query_cjrq( "max" );
		 if (c!=null)	
		  {
			 c.moveToFirst();
			 max_cjrq = formatdate2str(c.getString(0));
			 c.close();
		  }
		 
		 c = sqldb.query_cjrq( "min" );
		 if (c!=null)	
		  {
			 c.moveToFirst();
			 min_cjrq = formatdate2str(c.getString(0));
			 c.close();
		  }
		 
	//	Log.e("gushiriji", ""+getStringWidth("aaw��")) ;
		
	//	Log.e("gushiriji", ""+getStringHeight("aaw��")) ;
	}
	
	 private int getStringWidth(String str) {
		  return (int) mPaint.measureText(str);
		 }

	 private int getStringHeight(String str) {
		  FontMetrics fr = mPaint.getFontMetrics();
		  return (int) Math.ceil(fr.descent - fr.top) + 2;  //ceil() ������������Ϊ��ӽ���������

		 }
	 
	 public String formatdate2str(String rq)
	 {
		 SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		 String cjrq ="";
		 java.util.Date d1;
		 try
		{   
			 d1 = df.parse(rq);
			 SimpleDateFormat df1 = new SimpleDateFormat("yyyy��MM��dd��");
			 cjrq = df1.format(d1);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return cjrq;
	 }

	@Override
	protected void onDraw(Canvas canvas)
	{
		 
		super.onDraw(canvas);
		
		PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		canvas.setDrawFilter(pfd);
		
		int mapwidth = canvas.getWidth(),mapheight = canvas.getHeight();
		
		int numline = 15;
		
		int includecharwidth = Math.round(mapwidth*930/1265);
		
		float onetext = includecharwidth/numline;
		
		 Rect rf = new Rect(0, 0, mapwidth, mapheight);
		 
		/* is = getResources().openRawResource(R.drawable.cgcjd);
		 
		
		 
		 x = x<800 ? x+20: 100;
		
		 y = y<800 ? y+20: 100;
		 
		 bitmap2 = Bitmap.createBitmap(BitmapFactory.decodeStream(is, null, opts),0,0,x,y);
		*/ 
		 
		 canvas.drawBitmap(bp, null, rf, mPaint);
		 
		 	 
		 StringBuilder sbBuilder = new StringBuilder();
		 sbBuilder.append(min_cjrq +"��"+max_cjrq+" �ڼ䳴�ɳɼ����£�\n����ɽ��� "+sum_completed);
		// sbBuilder.append("\n����ɽ���ͳ�ƣ�");
		 sbBuilder.append("\n������� "+ sum_buytimes + "  �������� " + sum_selltimes);
		 sbBuilder.append("\n���׽��(����+����) " + sum_zongjine +"\n֧��Ӷ�� " + sum_yongjin);
		 sbBuilder.append("\n�ֺ��� " + sum_fenhong +"\n������(����-֧��) " + sum_zonglirun);
		 sbBuilder.append("\n��������Ʊ "+ max_zonglirun);
		 sbBuilder.append("\n�����ٷֱ���� "+ max_lirunpercent);
		 sbBuilder.append("\n\nϣ���ٽ����� Ϊ���Ҿ���������Ҫ���ף�");
		 
		 tp.setTextSize(onetext);
		 tp.setAntiAlias(true);
		 
		 StaticLayout layout = new StaticLayout(sbBuilder.toString(),tp,includecharwidth,Alignment.ALIGN_NORMAL,1.5F,0.0F,true);

        
		 canvas.translate(mapwidth*195/1265,mapheight*352/1920);

		layout.draw(canvas);
		invalidate();
		 
	//	Log.e("gushiriji","mapwidth"+mapwidth+",mapheight"+mapheight+",includecharwidth"+includecharwidth+",onetext"+onetext ) ;
	//	Log.e("gushiriji", ""+canvas.isHardwareAccelerated());
		
	}
	
}
