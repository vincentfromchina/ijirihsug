package com.dayu.gushiriji;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import jxl.format.Orientation;

public class CustomTextView extends LinearLayout
{
	private Context mcContext;
	private TypedArray mtTypedArray;

	public CustomTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mcContext = context;
		setOrientation(LinearLayout.VERTICAL);
		mtTypedArray = context.obtainStyledAttributes(attrs, R.styleable.customTextview);
		
	}

	public CustomTextView(Context context)
	{
		this(context, null);
	}
	
	public void setText(ArrayList<HashMap<String, Object>> datas)
	{
		int image_width = mtTypedArray.getDimensionPixelOffset(R.styleable.customTextview_image_width, 300);
		int image_heigth = mtTypedArray.getDimensionPixelOffset(R.styleable.customTextview_image_heigth, 200);
		
		for (HashMap<String, Object> hashMap : datas)
		{
			String type = (String) hashMap.get("type");
			if(type.equals("image"))
			{
				ImageView tImageView = new ImageView(mcContext);
				//tImageView.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				tImageView.setLayoutParams(new LayoutParams(image_width,image_heigth));
				tImageView.setImageResource(R.drawable.cat);
				this.addView(tImageView);
				downloadpic dmc = new downloadpic(tImageView,(String) hashMap.get("value"));
				dmc.start();
			}else
			{
				TextView tv1 = new TextView(mcContext);
				tv1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				tv1.setText(Html.fromHtml((String)hashMap.get("txt")));
				this.addView(tv1);
			}
		}
	}
	
	private class downloadpic extends Thread
	{
		private ImageView iv;
		private String url;
		public  downloadpic(ImageView iv,String url)
		{
			this.iv= iv;
			this.url=url;
		}

		@Override
		public void run()
		{
			Drawable drawb = null;
			try
			{
				drawb = Drawable.createFromStream(new URL(url).openStream(), "image");
			} catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = mhHandler.obtainMessage();
			HashMap<String, Object> msHashMap = new HashMap<String, Object>();
			msHashMap.put("imageview", iv);
			msHashMap.put("image", drawb);
			msg.obj = msHashMap;
			try
			{
				Thread.sleep(5000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mhHandler.sendMessage(msg);
			super.run();
		}
		
	}
	
	 private Handler mhHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				HashMap<String, Object> msHashMap =(HashMap<String, Object>) msg.obj;
				ImageView ivImageView = (ImageView) msHashMap.get("imageview");
				Drawable db2 = (Drawable) msHashMap.get("image");
				ivImageView.setImageDrawable(db2);
			}
			
		};
	
}
