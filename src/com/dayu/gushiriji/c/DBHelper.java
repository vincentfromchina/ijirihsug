package com.dayu.gushiriji.c;

import java.io.File;

import com.dayu.gushiriji.m.Stock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper
{
	
	private static final String DB_NAME = "stockdb.db";  
    private static final int DB_VERSION = 4;    
    private static final String CREATE_INFO = "create table if not exists stock_sort("  
            + "zqdm VARCHAR2(10) NOT NULL ON CONFLICT FAIL,"
    		+ "zqmc VARCHAR2(16) NOT NULL ON CONFLICT FAIL,"
    		+ "buy_times INT, sell_times INT, "
            + "buy_tol INT, sell_tol INT, "
    		+ "buy_tolmon DOUBLE, sell_tolmon DOUBLE, "
            + "fengu_tol INT, fenhong_time INT, "
    		+ "fenhong_mon DOUBLE,  holdtime INT, "
            + "yinhuashui_tol DOUBLE, yongjin_tol DOUBLE,"
    		+ "jiaoyiguifei_tol DOUBLE,zonglirun DOUBLE, "
    		+ "iscompleted VARCHAR2(5),"
    		+ "start_time VARCHAR2(9), end_time VARCHAR2(9),"
    		+ "cjrq VARCHAR2(11),cjsj VARCHAR2(8)"
    		+ ")";  
    
   

	public DBHelper(Context context, String name, CursorFactory factory)
	{
		super(context, name, null, DB_VERSION);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		
		String sql = CREATE_INFO;
        db.execSQL(sql);
        
        Log.e("database","数据库创建成功");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
		String sql = "";
	  /* sql = "CREATE TABLE [condition] ([periodid] INT,  [wendu_l] INT,  [wendu_h] INT,  [sun_l] INT,  [sun_h] int,  [water] VARCHAR2(1000),  [pzid] INT,  [waterpersecond] INT)";
        db.execSQL(sql);
      		
		/* sql = "alter table condition add [suntime] VARCHAR2(500)";
		db.execSQL(sql);
		
	  
		 sql = "update condition set suntime='06:00:00|20:00:00'";
		 db.execSQL(sql); */
		 
				 
		String pre ="drop table if exists stock_sort";
		db.execSQL(pre);
		
	    sql = CREATE_INFO;
        db.execSQL(sql);
        
        Log.e("database","数据库更新成功");
	}
	
	public void delrec_stock_sort()
	{
		String sql ="delete from stock_sort";
		SQLiteDatabase db = getReadableDatabase();
    	db.execSQL(sql);
	}
	
	public void delrec_null()
	{
		String sql ="delete from stock_sort where buy_times = 0";
		SQLiteDatabase db = getReadableDatabase();
    	db.execSQL(sql);
	}

    public Cursor query_count()
    {
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	Cursor c = db.rawQuery("select count(*) as tol from stock_sort", null);
    	return c;
    }
    
    
    public Cursor query_zonglirun(boolean asc)
    {
    	String order;
    	order = asc ? "asc" : "desc";
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select zqdm,zqmc,zonglirun from stock_sort where iscompleted ='true' order by zonglirun "+ order +" limit 100", null);
    }
    
    public Cursor query_cjrq(String maxormin)
    {
       	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	
    	if (maxormin.toString().equals("max"))
    	{
    		return db.rawQuery("select max(cjrq) from stock_sort where iscompleted ='true'", null);
    	}else {
    		return db.rawQuery("select min(cjrq) from stock_sort where iscompleted ='true'", null);
		}
    }
    
    
    public Cursor query_zongjine(boolean asc)
    {
    	String order;
    	order = asc ? "asc" : "desc";
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select zqdm,zqmc,buy_tolmon+sell_tolmon as tol from stock_sort where iscompleted ='true'  order by tol "+ order +" limit 100", null);
    }
    
    public Cursor query_Sum_zonglirun()
    {
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select sum(zonglirun) as zonglirun from stock_sort where iscompleted ='true'", null);
    }
    
    public Cursor query_Sum_zongjine()
    {
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select sum(buy_tolmon+sell_tolmon) as zonglirun from stock_sort where iscompleted ='true'", null);
    }
    
    public Cursor query_Sum_zongyongjin()
    {
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select sum(yongjin_tol) as zongyongjin from stock_sort where iscompleted ='true'", null);
    }
    
    public Cursor query_Sum_buycishu()
    {
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select sum(buy_times) as buycishu from stock_sort where iscompleted ='true'", null);
    }
    
    public Cursor query_Sum_sellcishu()
    {
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select sum(sell_times) as sellcishu from stock_sort where iscompleted ='true'", null);
    }
    
    public Cursor query_Sum_completed()
    {
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select count(*) as completed from stock_sort where iscompleted ='true'", null);
    }
    
    public Cursor query_Sum_stockall()
    {
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select count(*) as completed from stock_sort ", null);
    }
    
    public Cursor query_Sum_fenhong()
    {
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select sum(fenhong_mon) as jiaoyicishu from stock_sort where iscompleted ='true'", null);
    }
    
    public Cursor query_yongjin(boolean asc)
    {
    	String order;
    	order = asc ? "asc" : "desc";
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select zqdm,zqmc,yongjin_tol+yinhuashui_tol as tol from stock_sort where iscompleted ='true' order by tol "+ order +" limit 100", null);
    }
    
    public Cursor query_fenhong(boolean asc)
    {
    	String order;
    	order = asc ? "asc" : "desc";
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select zqdm,zqmc,fenhong_mon from stock_sort where iscompleted ='true' order by fenhong_mon "+ order +" limit 100", null);
    }
    
    public Cursor query_jiaoyicishu(boolean asc)
    {
    	String order;
    	order = asc ? "asc" : "desc";
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select zqdm,zqmc,buy_times+sell_times as tol from stock_sort where iscompleted ='true'  order by tol "+ order +" limit 100", null);
    }
    
    public Cursor query_yinlipercent(boolean asc)
    {
    	String order;
    	order = asc ? "asc" : "desc";
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select zqdm,zqmc,zonglirun,buy_tolmon,zonglirun/buy_tolmon*100 as tol from stock_sort where"
    			          + " iscompleted ='true'  order by tol "+ order +" limit 100", null);
    }
    
    public Cursor query_yinlipercent_all(boolean asc)
    {
    	String order;
    	order = asc ? "asc" : "desc";
    	SQLiteDatabase db = getReadableDatabase();
    	Log.e("database","query访问成功");  
    	return db.rawQuery("select zqdm,zqmc,zonglirun,buy_tolmon,fenhong_mon,yongjin_tol,(zonglirun+fenhong_mon-yongjin_tol)/buy_tolmon*100 as tol "
    			          + "from stock_sort where"
    			          + " iscompleted ='true'  order by tol "+ order +" limit 100", null);
    }
    
    public void insert_stockrec(Stock s)
    {
    	String sql ="insert into stock_sort(zqmc,zqdm,buy_times,"
    			+ "sell_times,buy_tol,sell_tol,buy_tolmon,sell_tolmon,"
    			+ "fengu_tol,fenhong_time,fenhong_mon,holdtime,"
    			+ "yinhuashui_tol,yongjin_tol,jiaoyiguifei_tol,zonglirun,iscompleted,"
    			+ "start_time,end_time,cjrq,cjsj)"
    			+ "values ('"+ s.getStock_name()+ "','"+ s.getStock_sid()
    			+ "',"+ s.getBuy_times() + "," + s.getSell_times()
    			+ ","+ s.getBuy_tol() +"," + s.getSell_tol()
    			+ ","+ s.getBuy_tolmon() +"," + s.getSell_tolmon()
    			+ ","+ s.getFengu_tol() +"," + s.getFenhong_times()
    			+ ","+ s.getFenhong_tol() +"," + s.getHold_time()
    			+ ","+ s.getYinhuashui_tol() +"," +s.getYongjin_tol()
    			+ ","+ s.getJiaoyiguifei_tol() +"," +s.getZonglirun()
    			+ ",'"+ s.isIscompleted()+"','"+ s.getStart_time()
    			+ "','" + s.getEnd_time() +"','"+ s.getCjrq() 
    			+ "','"+ s.getCjsj() + "')";
    	SQLiteDatabase db = getReadableDatabase();
    	db.execSQL(sql);
    	
      //  Log.e("database","数据库insert成功");
    }
 
	@Override
	public void onOpen(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		super.onOpen(db);
		Log.e("database","数据库open成功");
	}

    
    
}
