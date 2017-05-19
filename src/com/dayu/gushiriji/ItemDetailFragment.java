package com.dayu.gushiriji;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dayu.gushiriji.c.DBHelper;
import com.dayu.gushiriji.dummy.DummyContent;
import com.dayu.gushiriji.m.Stock;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment
{
	DBHelper sqldb;
	boolean isasc = false;
	String witchcontent ="";
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment()
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID))
		{
			
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null)
		{
			witchcontent = mItem.id;
			
			((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.content);
			
			TextView jieshao = (TextView)rootView.findViewById(R.id.item_jieshao);
			
			ListView lv1 = (ListView)rootView.findViewById(R.id.rec_listview);
			
			Button btn_fanxiang = (Button)rootView.findViewById(R.id.btn_desc);
			
			
			sqldb = new DBHelper(getActivity(),"stockdb.db", null) ;
			
			Cursor c = null;
			
			btn_fanxiang.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					Cursor c = null;
					isasc = !isasc;

					switch (witchcontent)
					{
					case "1":  //净利润最多
						c = sqldb.query_zonglirun( isasc );
						break;
					case "4":  //交易金额最大
						c = sqldb.query_zongjine( isasc );
						break;
					case "2":  //佣金最多
						c = sqldb.query_yongjin( isasc );
						break;
					case "3":  //分红最多
						c = sqldb.query_fenhong( isasc );
						break;
					case "5":  //交易次数最多
						c = sqldb.query_jiaoyicishu( isasc );
						break;
					case "6":  //盈利百分比最高
						c = sqldb.query_yinlipercent( isasc );
						break;
					case "7":  //百分比最高(+分红-佣金)
						c = sqldb.query_yinlipercent_all( isasc );
						break;
					default:
						break;
					}
					
					Log.e("gushiriji","getCount:"+ c.getCount());
					ListView lv1 = (ListView)getActivity().findViewById(R.id.rec_listview);
				//	TextView jieshao = (TextView)getActivity().findViewById(R.id.item_jieshao);
				//	jieshao.setText("进入了反向排序");
			    	if (c!=null)	
				    {	int tolrec = c.getCount();	
				      List<HashMap<String,String>> mlist = new ArrayList<HashMap<String,String>>();
					 if (tolrec > 0 ){
						Log.e("gushiriji", "进入了");
						
						  c.moveToFirst();
						  for (int i = 0; i < tolrec; i++)
						{
							  HashMap<String,String> mHashMap = new HashMap<String,String>();
							  switch (mItem.id)
								{
		       						case "6":  //盈利百分比最高
		       					//		abc[i] = c.getString(0)+"   "+ c.getString(1)+"   " +Stock.return2decimal(c.getDouble(4)) +"% \n利润 " + c.getString(2) +"元   买入 "+ c.getString(3)+"元 " ;
		       							mHashMap.put("stock_sid", c.getString(0));
		       							mHashMap.put("stock_name", c.getString(1) + "   盈利 " +Stock.return2decimal(c.getDouble(4)) +"%");
		       							mHashMap.put("tol", "总利润      " + c.getString(2) +"元   \n买入金额   "+ c.getString(3)+"元 ");
		       							break;
		       						case "7":  //百分比最高(+分红-佣金)
		       	       					//		abc[i] = c.getString(0)+"   "+ c.getString(1)+"   " +Stock.return2decimal(c.getDouble(4)) +"% \n利润 " + c.getString(2) +"元   买入 "+ c.getString(3)+"元 " ;
		       	       							mHashMap.put("stock_sid", c.getString(0));
		       	       							mHashMap.put("stock_name", c.getString(1) + "   盈利 " +Stock.return2decimal(c.getDouble(6)) +"%");
		       	       							mHashMap.put("tol", "总利润		" + c.getString(2) +"元   \n分红 			"+ c.getString(4)+"元\n佣金			"+ c.getString(5)+"元\n买入金额	"+ c.getString(3) +"元");
		       	       							break;
		       						case "5":  //交易次数最多
				       					//		abc[i] = c.getString(0)+"   "+ c.getString(1)+"   " +Stock.return2decimal(c.getDouble(4)) +"% \n利润 " + c.getString(2) +"元   买入 "+ c.getString(3)+"元 " ;
				       							mHashMap.put("stock_sid", c.getString(0));
				       							mHashMap.put("stock_name", c.getString(1));
				       							mHashMap.put("tol", c.getString(2)+"次");
				       							break;
								    default:
								//	   abc[i] = c.getString(0)+"   "+ c.getString(1)+"   " + c.getString(2) +"元";
									    mHashMap.put("stock_sid", c.getString(0));
		      							mHashMap.put("stock_name", c.getString(1));
		      							mHashMap.put("tol", c.getString(2)+"元");
									   break;
								}
						    mlist.add(mHashMap); 
							c.moveToNext();
						}
					}
		        	SimpleAdapter mSimpleAdapter;
					
					
					mSimpleAdapter = new SimpleAdapter(getActivity(), mlist, R.layout.listdetail,
							new String[] {"stock_sid","stock_name","tol"}, new int[] { R.id.zqdm,R.id.zqmc,R.id.showdetail});
					
					
					lv1.setAdapter(mSimpleAdapter);

				    c.close();
			     	}
				
				}
			});
			
			List<HashMap<String,String>> mlist = new ArrayList<HashMap<String,String>>();
			
			switch (mItem.id)
			{
			case "1":  //净利润最多
				c = sqldb.query_zonglirun( isasc );
				jieshao.setText("统计方式：取总利润前100条数据");
				break;
			case "4":  //交易金额最大
				c = sqldb.query_zongjine( isasc );
				jieshao.setText("统计方式：交易金额=买入金额+卖出金额，取前100条数据");
				break;
			case "2":  //佣金最多
				c = sqldb.query_yongjin( isasc );
				jieshao.setText("统计方式：佣金=印花税+净佣金，取前100条数据");
				break;
			case "3":  //分红最多
				c = sqldb.query_fenhong( isasc );
				jieshao.setText("统计方式：分红=购买股票期间分得的总金额，取总利润前100条数据");
				break;
			case "5":  //交易次数最多
				c = sqldb.query_jiaoyicishu( isasc );
				jieshao.setText("统计方式：交易次数=购买次数+卖出次数，取前100条数据");
				break;
			case "6":  //盈利百分比最高
				c = sqldb.query_yinlipercent( isasc );
				jieshao.setText("统计方式：百分比=总利润÷买入总金额，取前100条数据");
				break;
			case "7":  //百分比最高(+分红-佣金)
				c = sqldb.query_yinlipercent_all( isasc );
				jieshao.setText("统计方式：百分比=(总利润+分红-佣金)÷买入总金额，取前100条数据");
				break;	
			default:
				break;
			}
			 
		if (c!=null)	
		{	int tolrec = c.getCount();
		    //	Log.e("gushiriji","getCount:"+ c.getCount());
			if (tolrec > 0 ){
				Log.e("gushiriji", "进入了");
				  c.moveToFirst();
				  for (int i = 0; i < tolrec; i++)
				{
					  HashMap<String,String> mHashMap = new HashMap<String,String>();
					  switch (mItem.id)
						{
       						case "6":  //盈利百分比最高
       					//		abc[i] = c.getString(0)+"   "+ c.getString(1)+"   " +Stock.return2decimal(c.getDouble(4)) +"% \n利润 " + c.getString(2) +"元   买入 "+ c.getString(3)+"元 " ;
       							mHashMap.put("stock_sid", c.getString(0));
       							mHashMap.put("stock_name", c.getString(1) + "   盈利 " +Stock.return2decimal(c.getDouble(4)) +"%");
       							mHashMap.put("tol", "总利润      " + c.getString(2) +"元   \n买入金额   "+ c.getString(3)+"元 ");
       							break;
       						case "7": //百分比最高(+分红-佣金)
       	       					//		abc[i] = c.getString(0)+"   "+ c.getString(1)+"   " +Stock.return2decimal(c.getDouble(4)) +"% \n利润 " + c.getString(2) +"元   买入 "+ c.getString(3)+"元 " ;
       	       							mHashMap.put("stock_sid", c.getString(0));
       	       							mHashMap.put("stock_name", c.getString(1) + "   盈利 " +Stock.return2decimal(c.getDouble(6)) +"%");
       	       							mHashMap.put("tol", "总利润		" + c.getString(2) +"元   \n分红			"+ c.getString(4)+"元\n佣金			"+ c.getString(5)+"元\n买入金额	"+ c.getString(3) +"元");
       	       							break;
       						case "5":  //交易次数最多
		       					//		abc[i] = c.getString(0)+"   "+ c.getString(1)+"   " +Stock.return2decimal(c.getDouble(4)) +"% \n利润 " + c.getString(2) +"元   买入 "+ c.getString(3)+"元 " ;
		       							mHashMap.put("stock_sid", c.getString(0));
		       							mHashMap.put("stock_name", c.getString(1));
		       							mHashMap.put("tol", c.getString(2)+"次");
		       							break;
						    default:
						//	   abc[i] = c.getString(0)+"   "+ c.getString(1)+"   " + c.getString(2) +"元";
							    mHashMap.put("stock_sid", c.getString(0));
      							mHashMap.put("stock_name", c.getString(1));
      							mHashMap.put("tol", c.getString(2)+"元");
							   break;
						}
				    mlist.add(mHashMap); 
					c.moveToNext();
				}
			}
			
	/*		ArrayAdapter  mlistadapter ;
		
			mlistadapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1 , abc);  //android.R.layout.simple_list_item_1
		*/			
			SimpleAdapter mSimpleAdapter;
			
			
			mSimpleAdapter = new SimpleAdapter(getActivity(), mlist, R.layout.listdetail,
					new String[] {"stock_sid","stock_name","tol"}, new int[] { R.id.zqdm,R.id.zqmc,R.id.showdetail});
			
			
			lv1.setAdapter(mSimpleAdapter);
			
		//	View tv_zqmc =  lv1.getChildAt(0);
		//	Log.e("gushiriji", ""+lv1.getChildCount())  ;
			
			Log.e("gushiriji", mItem.id);
			 c.close();
	     	}
		
		   
		}
		
		return rootView;
	}

	
}
