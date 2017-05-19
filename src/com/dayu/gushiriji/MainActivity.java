package com.dayu.gushiriji;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import com.dayu.gushiriji.c.DBHelper;
import com.dayu.gushiriji.c.FolderFilePicker;
import com.dayu.gushiriji.c.FolderFilePicker.PickPathEvent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.Image;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
//import com.baidu.accountsdk.*;
//import com.baidu.accountsdk.BDAccountSDK;

public class MainActivity extends Activity
{
	
	private String mfilePath="";
	private String quanshang="guangfazhengquan";
	private DBHelper sqldb;
//	private static BDAccountSDK bdsdk;
//	static BDAccountSetting setting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btn_openfile = (Button)findViewById(R.id.btn_openfile);
		Button btn_showresult = (Button)findViewById(R.id.btn_showresult);
		Button btn_fenxi = (Button)findViewById(R.id.Btn_fenxi);
		ImageButton btn_guangfa = (ImageButton)findViewById(R.id.imgguangfa);
		ImageButton btn_guoyuan = (ImageButton)findViewById(R.id.imgguoyuan);
		Button Btn_jiaocheng = (Button)findViewById(R.id.Btn_jiaocheng);
		
		Context ct = getApplicationContext();
		
		/*
	    setting = new BDAccountSetting();
        
        setting.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		init_bddl(ct);
		
		bdsdk.login(ct, new ICallback<Void>()
		{

			@Override
			public void onCallback(int resultCode, String resultDesc,Void extraData)
			{
				String hint = "";
				switch(resultCode){
				case BDAccountSDK. LOGIN_RESULT_CODE_SUCCESS:
				hint = "登录成功";
				break;
				case BDAccountSDK.LOGIN_RESULT_CODE_CANCEL:
				hint = "取消登录";
				break;
				case BDAccountSDK.LOGIN_RESULT_CODE_FAIL:
				default:  //其他值为登录失败
				hint = "登录失败";
				break;
				}
			  Log.e("game", hint);	
			}
			
		});
		
		*/
		
		Btn_jiaocheng.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent excel = new Intent();
				
				excel.setClass(MainActivity.this, WebActivity.class);
			    excel.putExtra("urls", "http://jsonok.jsp.fjjsp.net/gushiriji/gushiriji_wangye.jsp");
				startActivity(excel);
			}
		});
		
		btn_guangfa.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				quanshang = "guangfazhengquan";
				pickFile(v);
			}
		});
		
		
		
		btn_guoyuan.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				quanshang = "guoyuanzhengquan";
				pickFile(v);
			}
		});
		
		btn_fenxi.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (mfilePath.toString().equals(""))
				{
					Toast.makeText(MainActivity.this, "请选择xls文件",
							Toast.LENGTH_LONG).show();
				}else{
				Intent excel = new Intent();
				
				excel.setClass(MainActivity.this, ExcelActivity.class);
				
			//	excel.setClass(MainActivity.this, PoiExcelactivity.class);
				excel.putExtra("filepath", mfilePath);
				excel.putExtra("quanshang", quanshang);
				startActivity(excel);
				}
			}
		});
		
		btn_openfile.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				 sqldb = new DBHelper(getBaseContext(),"stockdb.db", null) ;
				 Cursor c = null;
				 c = sqldb.query_count();
				 if (c!=null)	
				  {
					 c.moveToFirst();
					 int count = c.getInt(0);
					 c.close();
					 if (count==0)
					 {
						 Toast.makeText(getBaseContext(), "无数据，请先导入文件", Toast.LENGTH_LONG).show();
						return;
					 }
				  }
				Intent excel = new Intent();
				
				excel.setClass(MainActivity.this, Showresult.class);
				startActivity(excel);
			}
		});
		
		btn_showresult.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent excel = new Intent();
				
				excel.setClass(MainActivity.this, ItemListActivity.class);
			//	excel.setClass(MainActivity.this, ShowresultActivity.class);
				startActivity(excel);
			}
		});
	}
	
	private static void init_bddl(Context ct) {
        
		//	setting.setOrientation(Orientation.);//设置为横屏
	    //    bdsdk = BDAccountSDK.getInstance();
	    //    bdsdk.init(ct, setting);
		}
	
	public void pickFile(View v) {
		FolderFilePicker picker = new FolderFilePicker(this,
				new PickPathEvent() {

					@Override
					public void onPickEvent(String resultPath) {
						mfilePath = resultPath;
						if (mfilePath==null)
						{
							Toast.makeText(MainActivity.this, "请选择xls文件",
									Toast.LENGTH_LONG).show();
						}else{
						
						TextView tv1 = (TextView)findViewById(R.id.textView1);
						tv1.setText("文件："+mfilePath+",请点击分析文件");
						
						}
					}
				}, "xls");
		picker.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
