package com.dayu.gushiriji;

import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.dayu.gushiriji.c.DBHelper;
import com.dayu.gushiriji.m.ExcelRow;
import com.dayu.gushiriji.m.Stock;

import android.R.array;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.Visibility;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import jxl.*;

public class ExcelActivity extends Activity
{
	private TextView txt = null;  
	private ProgressBar pb1 = null;
	 public Stock m_stock[] = new Stock[1];
	 public ExcelRow m_excelrow;
	 private int Rows ;
	 private int Cols ;
	 private String Stock_tol[],col_name[];
	 private HashMap<String, Integer> mHashMap_stock;
	 DBHelper sqldb;
	 private String quanshang ="";
	 String mfilepath = "";
	 InputStream is = null;
	 private boolean selltoltype = true; //卖出股份 默认是 + 操作
	 
	 //单元格从0,0 开始  sheet.getCell(列,行)

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_excel);
		// Show the Up button in the action bar.
		setupActionBar();
		txt = (TextView)findViewById(R.id.txt_show);  
        txt.setMovementMethod(ScrollingMovementMethod.getInstance()); 
        pb1 = (ProgressBar)findViewById(R.id.pb_fenxi);
        pb1.setMax(100);
        Log.e("gushiriji", "进入界面");
        
        Button btn_seechengji = (Button)findViewById(R.id.btn_seechengji);
		Button btn_showdetail = (Button)findViewById(R.id.btn_showdetail);
		Button btn_haotwouse = (Button)findViewById(R.id.btn_howtwouse);
		
		btn_seechengji.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
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
				
				excel.setClass(ExcelActivity.this, ShowresultActivity.class);
				startActivity(excel);
			}
		});
		
		btn_haotwouse.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent excel = new Intent();
				excel.putExtra("urls", "http://baidu.com");
				excel.setClass(ExcelActivity.this, WebActivity.class);
				startActivity(excel);
			}
		});
		
		btn_showdetail.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent excel = new Intent();
				
				excel.setClass(ExcelActivity.this, ItemListActivity.class);
			//	excel.setClass(MainActivity.this, ShowresultActivity.class);
				startActivity(excel);
			}
		});
        
        mfilepath =  getIntent().getStringExtra("filepath");
        quanshang = getIntent().getStringExtra("quanshang");
                
        if (quanshang.toString().equals("guangfazhengquan"))
		{
        	selltoltype = true;
		} else if(quanshang.toString().equals("guoyuanzhengquan"))
		{
			selltoltype = false;
		}
        
        initgetinfo();
        
        Thread t_readexcel = new Thread(new Runnable()
		{
			public void run()
			{
				readExcel();
				
				query_Sum_zonglirun();
			}
		});
        
        t_readexcel.start();
	}

	
	protected void query_Sum_zonglirun()
	{
		Cursor c = sqldb.query_Sum_zonglirun();
		if (c.getCount()> 0 ){
			 
			  c.moveToFirst();
			 Double zonglirun = c.getDouble(c.getColumnIndex("zonglirun"));
			
			 Log.e("gushilirun","sumzonglirun:"+ zonglirun );
		}
	}

	private void initgetinfo()
	{
		initdb();
		
		File apppath = this.getFilesDir();
		Log.e("gushiriji", apppath.toString());
		
		m_excelrow = new ExcelRow();
		
		  try {  
			  Log.e("gushiriji", "init尝试打开文件" );
			  
	           is = new FileInputStream( mfilepath );  
	           Workbook book = Workbook.getWorkbook( is );  
	          // Workbook book = Workbook.getWorkbook(is);  
	           int num = book.getNumberOfSheets();  
	           txt.setText("the num of sheets is " + num+ "\n");  
	           // 获得第一个工作表对象  
	           Sheet sheet = book.getSheet(0);  
	           Rows = sheet.getRows();  
	           Cols = sheet.getColumns();
	           
	           Stock_tol = new String[Rows];
	           col_name = new String[Cols];
	           
	           mHashMap_stock = new HashMap<String,Integer>();
	           
	           txt.append("the name of sheet is " + sheet.getName() + "\n");  
	           txt.append("total rows is " + Rows + "\n");  
	           txt.append("total cols is " + Cols + "\n");  
	           
	         	           
	           initexcelrow( quanshang, sheet);
	              
	              for (int i = 0; i < Cols; ++i) {  
	            	  
	 		         if ( i == m_excelrow.getZqdm())
	 		         {
	 		            for (int j = 0; j < Rows-1; ++j) {  
	 		             // getCell(Col,Row)获得单元格的值  
	 		            	Stock_tol[j] =  sheet.getCell(i,j+1).getContents().toString();
	 		              // Log.e("gushiriji", Stock_tol[j]);
	 		            }
	 		         }
	              }
	              
	           runOnUiThread(new Runnable()
			{
				public void run()
				{
					pb1.setProgress(50);
					 txt.append("\n分析进度 50%");
				}
			});   
	         
	           book.close();  
	           
	           Stock_tol = array_unique(Stock_tol);
	           
	           m_stock = new Stock[Stock_tol.length];
	           
		        for (int t = 0; t < Stock_tol.length; t++)
				{
		        	 //  Log.e("gushiriji", Stock_tol[t]);
		        	mHashMap_stock.put(Stock_tol[t],t);
				}
	           
	          } catch (Exception e) {  
	        	  Log.e("initgushiriji", "打开文件失败" + e);
	           
	          } 
		
	}
	
	private  void initexcelrow( String quanshang,Sheet sheet)
	{
		for(int i = 0;i<Cols ;i++)
		{ col_name[i] = sheet.getCell(i,0).getContents().toString();
        //   Log.e("gushiriji", col_name[i]);
		}
		
		if(Arrays.asList(col_name).contains(m_excelrow.s_cjje))
		{
		   m_excelrow.setCjje( Arrays.asList(col_name).indexOf(m_excelrow.s_cjje));
		} 
		if(Arrays.asList(col_name).contains(m_excelrow.s_cjjg))
		{
		   m_excelrow.setCjjg( Arrays.asList(col_name).indexOf(m_excelrow.s_cjjg));
		} 
		if(Arrays.asList(col_name).contains(m_excelrow.s_cjjj))
		{
			   m_excelrow.setCjjg( Arrays.asList(col_name).indexOf(m_excelrow.s_cjjj));
		} 
		if(Arrays.asList(col_name).contains(m_excelrow.s_cjrq))
		{
			   m_excelrow.setCjrq( Arrays.asList(col_name).indexOf(m_excelrow.s_cjrq));
		} 
		if(Arrays.asList(col_name).contains(m_excelrow.s_cjsj))
		{
			   m_excelrow.setCjsj( Arrays.asList(col_name).indexOf(m_excelrow.s_cjsj));
		} 
		if(Arrays.asList(col_name).contains(m_excelrow.s_cjsl))
		{
			   m_excelrow.setCjsl( Arrays.asList(col_name).indexOf(m_excelrow.s_cjsl));
		}
		 if(Arrays.asList(col_name).contains(m_excelrow.s_cz))
		{
			   m_excelrow.setMmbz( Arrays.asList(col_name).indexOf(m_excelrow.s_cz));
		}
		 if(Arrays.asList(col_name).contains(m_excelrow.s_ghf))
		{
			   m_excelrow.setGhf( Arrays.asList(col_name).indexOf(m_excelrow.s_ghf));
		}
		 if(Arrays.asList(col_name).contains(m_excelrow.s_jyj))
		{
			   m_excelrow.setJyj( Arrays.asList(col_name).indexOf(m_excelrow.s_jyj));
		}
		 if(Arrays.asList(col_name).contains(m_excelrow.s_mmbz))
		{
			   m_excelrow.setMmbz( Arrays.asList(col_name).indexOf(m_excelrow.s_mmbz));
		}
		 if(Arrays.asList(col_name).contains(m_excelrow.s_sxf))
		{
			   m_excelrow.setJyj( Arrays.asList(col_name).indexOf(m_excelrow.s_sxf));
		}
		 if(Arrays.asList(col_name).contains(m_excelrow.s_yhS))
		{
			   m_excelrow.setYhS( Arrays.asList(col_name).indexOf(m_excelrow.s_yhS));
		} 
		 if(Arrays.asList(col_name).contains(m_excelrow.s_zqdm))
		{
			   m_excelrow.setZqdm( Arrays.asList(col_name).indexOf(m_excelrow.s_zqdm));
		} 
		 if(Arrays.asList(col_name).contains(m_excelrow.s_zqmc))
		{
		    m_excelrow.setZqmc( Arrays.asList(col_name).indexOf(m_excelrow.s_zqmc));
		} 
		 if(Arrays.asList(col_name).contains(m_excelrow.s_qtf))
		{
		    m_excelrow.setQtf( Arrays.asList(col_name).indexOf(m_excelrow.s_qtf));
		} 
		 if(Arrays.asList(col_name).contains(m_excelrow.s_jygf))
		{
		    m_excelrow.setJygf( Arrays.asList(col_name).indexOf(m_excelrow.s_jygf));
		}
		 
		    runOnUiThread(new Runnable()
					{
						public void run()
						{
							pb1.setProgress(25);
							 txt.append("\n正在分析\n分析进度 25%");
						}
					});   
		  
	}

	private void initdb()
	{
	    sqldb = new DBHelper(ExcelActivity.this,"stockdb.db", null) ;
		sqldb.delrec_stock_sort();
	}

	private void readExcel()
	{
		Log.e("gushiriji", "进入分析");
		  try {  
			  Log.e("gushiriji", "尝试打开文件" );
			  
	           InputStream is = new FileInputStream( mfilepath );  
	           Workbook book = Workbook.getWorkbook( is );  
	          // Workbook book = Workbook.getWorkbook(is);  
	           
	           // 获得第一个工作表对象  
	           Sheet sheet = book.getSheet(0);  
	         
	           int tmp_posinstock;
	           
	          for (int m = 0; m < Stock_tol.length; m++)
			   {
	        	   m_stock[m] = new Stock();
			   }
	           
	           for (int i = 0; i < Cols; ++i) {  
	           
		         if ( i == m_excelrow.getZqdm())
		         {
		            for (int j = 1; j < Rows; ++j) {  
		             // getCell(Col,Row)获得单元格的值  
		            
		            	 tmp_posinstock = mHashMap_stock.get(sheet.getCell(i,j).getContents().toString());
		            //	 Log.e("gushiriji",i +","+ j+ ":" + tmp_posinstock);
		            	
		            	 m_stock[tmp_posinstock].setStock_name( sheet.getCell(m_excelrow.getZqmc(),j).getContents());
		            	 m_stock[tmp_posinstock].setStock_sid( sheet.getCell(m_excelrow.getZqdm(),j).getContents() );
		            
							  switch (sheet.getCell(m_excelrow.getMmbz(),j).getContents())
								{
								case "证券买入":
									 m_stock[tmp_posinstock].setBuy_times( m_stock[tmp_posinstock].getBuy_times()+1 );
									 m_stock[tmp_posinstock].setBuy_tolmon( m_stock[tmp_posinstock].getBuy_tolmon() + Double.valueOf(sheet.getCell(m_excelrow.getCjje(),j).getContents()));
									 m_stock[tmp_posinstock].setBuy_tol(m_stock[tmp_posinstock].getBuy_tol() + Integer.valueOf(sheet.getCell(m_excelrow.getCjsl(),j).getContents()));
									 m_stock[tmp_posinstock].setYongjin_tol(m_stock[tmp_posinstock].getYongjin_tol() + Double.valueOf(sheet.getCell(m_excelrow.getJyj(),j).getContents()));
									 m_stock[tmp_posinstock].setYinhuashui_tol(m_stock[tmp_posinstock].getYinhuashui_tol() + Double.valueOf(sheet.getCell(m_excelrow.getYhS(),j).getContents()));
								//	 m_stock[tmp_posinstock].setQita_tol(m_stock[tmp_posinstock].getQita_tol() + Double.valueOf(sheet.getCell(m_excelrow.getQtf(),j).getContents()));
									 m_stock[tmp_posinstock].setGuohufei_tol(m_stock[tmp_posinstock].getGuohufei_tol() + Double.valueOf(sheet.getCell(m_excelrow.getGhf(),j).getContents()));
								//	 m_stock[tmp_posinstock].setJiaoyiguifei_tol(m_stock[tmp_posinstock].getJiaoyiguifei_tol() + Double.valueOf(sheet.getCell(m_excelrow.getJygf(),j).getContents()));
								//	 m_stock[tmp_posinstock].setStart_time(sheet.getCell(m_excelrow.getCjrq(),j).getContents());
									 m_stock[tmp_posinstock].setCjrq(sheet.getCell(m_excelrow.getCjrq(),j).getContents());
									 m_stock[tmp_posinstock].setCjsj(sheet.getCell(m_excelrow.getCjsj(),j).getContents());
									 break;
								case "证券卖出":
									 m_stock[tmp_posinstock].setSell_times( m_stock[tmp_posinstock].getSell_times()+1 );
									 m_stock[tmp_posinstock].setSell_tolmon( m_stock[tmp_posinstock].getSell_tolmon() + Double.valueOf(sheet.getCell(m_excelrow.getCjje(),j).getContents()));
									 
									 if (selltoltype)
									  { m_stock[tmp_posinstock].setSell_tol(m_stock[tmp_posinstock].getSell_tol() + Integer.valueOf(sheet.getCell(m_excelrow.getCjsl(),j).getContents()));}
									 else {
										 m_stock[tmp_posinstock].setSell_tol(m_stock[tmp_posinstock].getSell_tol() - Integer.valueOf(sheet.getCell(m_excelrow.getCjsl(),j).getContents()));
									 }
									 
									 m_stock[tmp_posinstock].setYongjin_tol(m_stock[tmp_posinstock].getYongjin_tol() + Double.valueOf(sheet.getCell(m_excelrow.getJyj(),j).getContents()));
									 m_stock[tmp_posinstock].setYinhuashui_tol(m_stock[tmp_posinstock].getYinhuashui_tol() + Double.valueOf(sheet.getCell(m_excelrow.getYhS(),j).getContents()));
									 m_stock[tmp_posinstock].setQita_tol(m_stock[tmp_posinstock].getQita_tol() + Double.valueOf(sheet.getCell(m_excelrow.getQtf(),j).getContents()));
									 m_stock[tmp_posinstock].setGuohufei_tol(m_stock[tmp_posinstock].getGuohufei_tol() + Double.valueOf(sheet.getCell(m_excelrow.getGhf(),j).getContents()));
									 m_stock[tmp_posinstock].setJiaoyiguifei_tol(m_stock[tmp_posinstock].getJiaoyiguifei_tol() + Double.valueOf(sheet.getCell(m_excelrow.getJygf(),j).getContents()));
								//	 m_stock[tmp_posinstock].setEnd_time(sheet.getCell(m_excelrow.getCjrq(),j).getContents());
									 m_stock[tmp_posinstock].setCjrq(sheet.getCell(m_excelrow.getCjrq(),j).getContents());
									 m_stock[tmp_posinstock].setCjsj(sheet.getCell(m_excelrow.getCjsj(),j).getContents());
									 break;
								case "证券买入清算":
									 m_stock[tmp_posinstock].setBuy_times( m_stock[tmp_posinstock].getBuy_times()+1 );
									 m_stock[tmp_posinstock].setBuy_tolmon( m_stock[tmp_posinstock].getBuy_tolmon() + Double.valueOf(sheet.getCell(m_excelrow.getCjje(),j).getContents()));
									 m_stock[tmp_posinstock].setBuy_tol(m_stock[tmp_posinstock].getBuy_tol() + Integer.valueOf(sheet.getCell(m_excelrow.getCjsl(),j).getContents()));
									 m_stock[tmp_posinstock].setYongjin_tol(m_stock[tmp_posinstock].getYongjin_tol() + Double.valueOf(sheet.getCell(m_excelrow.getJyj(),j).getContents()));
									 m_stock[tmp_posinstock].setYinhuashui_tol(m_stock[tmp_posinstock].getYinhuashui_tol() + Double.valueOf(sheet.getCell(m_excelrow.getYhS(),j).getContents()));
									 m_stock[tmp_posinstock].setGuohufei_tol(m_stock[tmp_posinstock].getGuohufei_tol() + Double.valueOf(sheet.getCell(m_excelrow.getGhf(),j).getContents()));
								//	 m_stock[tmp_posinstock].setStart_time(sheet.getCell(m_excelrow.getCjrq(),j).getContents());
									 m_stock[tmp_posinstock].setCjrq(sheet.getCell(m_excelrow.getCjrq(),j).getContents());
									 m_stock[tmp_posinstock].setCjsj(sheet.getCell(m_excelrow.getCjsj(),j).getContents());
									 break;
								case "证券卖出清算":
									 m_stock[tmp_posinstock].setSell_times( m_stock[tmp_posinstock].getSell_times()+1 );
									 m_stock[tmp_posinstock].setSell_tolmon( m_stock[tmp_posinstock].getSell_tolmon() + Double.valueOf(sheet.getCell(m_excelrow.getCjje(),j).getContents()));
									 m_stock[tmp_posinstock].setSell_tol(m_stock[tmp_posinstock].getSell_tol() - Integer.valueOf(sheet.getCell(m_excelrow.getCjsl(),j).getContents()));
									 m_stock[tmp_posinstock].setYongjin_tol(m_stock[tmp_posinstock].getYongjin_tol() + Double.valueOf(sheet.getCell(m_excelrow.getJyj(),j).getContents()));
									 m_stock[tmp_posinstock].setYinhuashui_tol(m_stock[tmp_posinstock].getYinhuashui_tol() + Double.valueOf(sheet.getCell(m_excelrow.getYhS(),j).getContents()));
									 m_stock[tmp_posinstock].setGuohufei_tol(m_stock[tmp_posinstock].getGuohufei_tol() + Double.valueOf(sheet.getCell(m_excelrow.getGhf(),j).getContents()));
								//	 m_stock[tmp_posinstock].setEnd_time(sheet.getCell(m_excelrow.getCjrq(),j).getContents());
									 m_stock[tmp_posinstock].setCjrq(sheet.getCell(m_excelrow.getCjrq(),j).getContents());
									 m_stock[tmp_posinstock].setCjsj(sheet.getCell(m_excelrow.getCjsj(),j).getContents());
									 break;
								case "股息入账":
									 m_stock[tmp_posinstock].setFenhong_times( m_stock[tmp_posinstock].getFenhong_times()+1 );
									 m_stock[tmp_posinstock].setFenhong_tol( m_stock[tmp_posinstock].getFenhong_tol() + Double.valueOf(sheet.getCell(m_excelrow.getCjje(),j).getContents()));
									 m_stock[tmp_posinstock].setYongjin_tol(m_stock[tmp_posinstock].getYongjin_tol() + Double.valueOf(sheet.getCell(m_excelrow.getJyj(),j).getContents()));
									 m_stock[tmp_posinstock].setYinhuashui_tol(m_stock[tmp_posinstock].getYinhuashui_tol() + Double.valueOf(sheet.getCell(m_excelrow.getYhS(),j).getContents()));
									 m_stock[tmp_posinstock].setGuohufei_tol(m_stock[tmp_posinstock].getGuohufei_tol() + Double.valueOf(sheet.getCell(m_excelrow.getGhf(),j).getContents()));
									 break;
								case "红股入账":
									 m_stock[tmp_posinstock].setFengu_tol( m_stock[tmp_posinstock].getFengu_tol()+Integer.valueOf(sheet.getCell(m_excelrow.getCjsl(),j).getContents()) );
									 m_stock[tmp_posinstock].setYongjin_tol(m_stock[tmp_posinstock].getYongjin_tol() + Double.valueOf(sheet.getCell(m_excelrow.getJyj(),j).getContents()));
									 m_stock[tmp_posinstock].setYinhuashui_tol(m_stock[tmp_posinstock].getYinhuashui_tol() + Double.valueOf(sheet.getCell(m_excelrow.getYhS(),j).getContents()));
									 m_stock[tmp_posinstock].setGuohufei_tol(m_stock[tmp_posinstock].getGuohufei_tol() + Double.valueOf(sheet.getCell(m_excelrow.getGhf(),j).getContents()));
									 break;
								case "股息红利税补缴":
									 m_stock[tmp_posinstock].setGuxihonglisuibujiao( m_stock[tmp_posinstock].getGuxihonglisuibujiao()+Double.valueOf(sheet.getCell(m_excelrow.getCjje(),j).getContents()) );
									 m_stock[tmp_posinstock].setYongjin_tol(m_stock[tmp_posinstock].getYongjin_tol() + Double.valueOf(sheet.getCell(m_excelrow.getJyj(),j).getContents()));
									 m_stock[tmp_posinstock].setYinhuashui_tol(m_stock[tmp_posinstock].getYinhuashui_tol() + Double.valueOf(sheet.getCell(m_excelrow.getYhS(),j).getContents()));
									 m_stock[tmp_posinstock].setGuohufei_tol(m_stock[tmp_posinstock].getGuohufei_tol() + Double.valueOf(sheet.getCell(m_excelrow.getGhf(),j).getContents()));
									 break;  
								default:
									break;
								}
						}
		         }  
	           }  
	           
	           runOnUiThread(new Runnable()
				{
					public void run()
					{
						pb1.setProgress(75);
						 txt.append("\n分析进度 75%");
					}
				});   
	           
	        //  Log.e("gushiriji", "getColumn"+sheet.findCell("剩余金额").getColumn()+"getRow"+sheet.findCell("剩余金额").getRow());
	           book.close();  
	           
	           checkallstock();
	           
	           runOnUiThread(new Runnable()
				{
					public void run()
					{
						pb1.setProgress(100);
						 txt.append("\n分析进度 100%\n分析完成！");
						  Button btn_seechengji = (Button)findViewById(R.id.btn_seechengji);
							Button btn_showdetail = (Button)findViewById(R.id.btn_showdetail);
							btn_seechengji.setVisibility(View.VISIBLE);	
							btn_showdetail.setVisibility(View.VISIBLE);	
					}
				});   
	           
	           int tmp = 0;
		          Log.e("gushiriji",  m_stock[tmp].getStock_name()+","+ m_stock[tmp].getBuy_times()+","+ m_stock[tmp].getBuy_tol()+","+
		        		  m_stock[tmp].getBuy_tolmon()+","+ m_stock[tmp].getSell_times()+","+ m_stock[tmp].getSell_tol()+","+ m_stock[tmp].getSell_tolmon()+
		        		  ","+ m_stock[tmp].getFengu_tol()+","+ m_stock[tmp].getFenhong_times()+","+
		        		  m_stock[tmp].getFenhong_tol()+","+m_stock[tmp].getStart_time()+","+m_stock[tmp].getEnd_time()+","+
		        		  m_stock[tmp].getHold_time()+","+m_stock[tmp].getPingjunchengben() +","+m_stock[tmp].getYongjin_tol()+","+
		        		  m_stock[tmp].getYinhuashui_tol()+","+m_stock[tmp].getJiaoyiguifei_tol() +","+ m_stock[tmp].isIscompleted()+","
		        		  +m_stock[tmp].getZonglirun());
	           
	          } 
		       catch(jxl.read.biff.BiffException e)
		       {
		    	   Log.e("gushiriji", "文件格式不正确，请重试");
		    	   runOnUiThread(new Runnable()
				{
					public void run()
					{
						txt.append("文件格式不正确!");
						Button btn_haotwouse = (Button)findViewById(R.id.btn_howtwouse);
						btn_haotwouse.setVisibility(View.VISIBLE);
					}
				});
		       }
		       catch (Exception e) {  
	        	  Log.e("gushiriji", "分析文件出错,请重试或参看说明！" + e);
	           
	          }  
		
	}
	
	public boolean checkallstock()
	{
		 for (int m = 0; m < Stock_tol.length; m++)
		   {
      	     m_stock[m].setHold_time();
      	     m_stock[m].setIscompleted();
      	     m_stock[m].setZonglirun();
      	     sqldb.insert_stockrec(m_stock[m]);
		   }
		 
		 sqldb.delrec_null();
		return true;
	}
	
	public static String[] array_unique(String[] a) {  
	    // array_unique  
	    List<String> list = new LinkedList<String>();  
	    for(int i = 0; i < a.length; i++) {  
	        if(!list.contains(a[i])) {  
	            list.add(a[i]);  
	        }  
	    }  
	    return (String[])list.toArray(new String[list.size()]);  
	}  

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar()
	{

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.excel, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
