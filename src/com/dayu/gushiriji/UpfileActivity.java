package com.dayu.gushiriji;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.xml.sax.InputSource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UpfileActivity extends Activity
{
	 String URL_Post = "http://dayuinf.com/Upfile";
	 HttpURLConnection urlConn = null;
	 boolean sendst = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upfile);
		
		Button btButton = (Button)findViewById(R.id.button1);
		
		 StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

	     StrictMode.setThreadPolicy(policy);
		
		btButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				int k = 3;
				if ( (k = HttpURLConnection_Post())==200)
				{
					Log.e("url", "post ok");
				}else
				{
					Log.e("url", "post fail"+k);
				}
			}
		});
		
		Button btn = (Button)findViewById(R.id.btn_send);
		btn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				sendst = true;
				
			}
		});
		
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();     
		if (adapter == null)     
		{     
		    // �豸��֧������      
		}     
		// ������      
		if (!adapter.isEnabled())     
		{     
		    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);     
		    // ���������ɼ��ԣ����300��      
		    intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);     
		    startActivity(intent);     
		}    
		
		    
		Set<BluetoothDevice> devices = adapter.getBondedDevices();     
		if(devices.size()>0)     
		{     
			 Iterator<BluetoothDevice> iterator = devices.iterator(); 
	      while(iterator.hasNext())
	      {  BluetoothDevice device = iterator.next();     
		    Log.e("lanya",device.getName());   
		    
               if (device.getName().equalsIgnoreCase("vincent-bt")) {      
                 int  connectState = device.getBondState();     
                   switch (connectState) {     
                       case BluetoothDevice.BOND_NONE:     
                           break;     
                       case BluetoothDevice.BOND_BONDING:     
                           break;     
                       case BluetoothDevice.BOND_BONDED:     
						// ����      
						 Log.e("lanya","start conn lanya"); 
						 bt_thread m_bt = new bt_thread(device);
						 m_bt.start();     
                           break;     
                   } 
             }
            } ;
		}  
		
	     BroadcastReceiver receiver = new BroadcastReceiver() {     
		        @Override     
		       public void onReceive(Context context, Intent intent) {     
		            String action = intent.getAction();     
		             if (BluetoothDevice.ACTION_FOUND.equals(action)) {     
		                 BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);     
		                 Log.e("lanya",device.getName());    
		                 if (device.getName().equalsIgnoreCase("vincent-bt")) {      
		                   int  connectState = device.getBondState();     
		                     switch (connectState) {     
		                         case BluetoothDevice.BOND_NONE:     
		                             break;     
		                         case BluetoothDevice.BOND_BONDING:     
		                             break;     
		                         case BluetoothDevice.BOND_BONDED:     
								// ����      
								 Log.e("lanya","start conn lanya"); 
								 bt_thread m_bt = new bt_thread(device);
								 m_bt.start();     
		                             break;     
		                     }     
		                 } ;
		            }     
		        }     
		     } ;
		
	    // ���ù㲥��Ϣ����      
	    IntentFilter intentFilter = new IntentFilter();     
	    intentFilter.addAction(BluetoothDevice.ACTION_FOUND);     
	    intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);     
	    intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);     
	    intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);     
	    // ע��㲥�����������ղ������������      
	    registerReceiver(receiver, intentFilter);     
	    // Ѱ�������豸��android�Ὣ���ҵ����豸�Թ㲥��ʽ����ȥ      
	    adapter.startDiscovery();    
	}
	
	class bt_thread extends Thread
	{
	  //	#�������ڷ���
	//	SerialPortServiceClass_UUID = '{00001101-0000-1000-8000-00805F9B34FB}'
	//	LANAccessUsingPPPServiceClass_UUID = '{00001102-0000-1000-8000-00805F9B34FB}'
	
		final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";     
	        UUID uuid = UUID.fromString(SPP_UUID);     
	        BluetoothDevice mdevice;   
		public bt_thread(BluetoothDevice device)
		{
			mdevice = device;
		}

		@SuppressLint("NewApi")
		@Override
		public void run()
		{
			
			try
			{
				Thread.sleep(3000);
			} catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
           BluetoothSocket socket;
		try
		{
			socket = mdevice.createRfcommSocketToServiceRecord(uuid);
			 socket.connect(); 
			 if(socket.isConnected())
			 {
				 Log.e("lanya","conn ok");
				 InputStream is = socket.getInputStream();
				 OutputStream os = socket.getOutputStream();
				 while (true)
				 {
					if (is.available()>0)
					 {
					  byte[] buf = new byte[is.available()];
					  is.read(buf, 0, is.available());
				      String st = new String(buf,"utf-8");
					  Log.e("lanya",st);
					 }
					
					if(sendst)
					{
						EditText edt = (EditText)findViewById(R.id.edit_sendtxt);
						
						byte[] buf = new byte[edt.getText().toString().length()];
						buf = edt.getText().toString().getBytes();
						os.write(buf);
						os.flush();
						sendst = false;
					}
				 }
			 }
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
	           
			super.run();
		}
		 
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upfile, menu);
		return true;
	}
	
	 private int HttpURLConnection_Post()
	 {  
		 int recode = 0;
	        try{  
	            //ͨ��openConnection ����  
	            URL url = new java.net.URL(URL_Post);  
	            urlConn=(HttpURLConnection)url.openConnection();  
	            //��������������   
	            urlConn.setDoOutput(true);  
	            urlConn.setDoInput(true);  
	              
	            urlConn.setRequestMethod("POST");  
	            urlConn.setUseCaches(false);  
	            urlConn.setReadTimeout(3000);
	            urlConn.setConnectTimeout(3000);
	            // ���ñ������ӵ�Content-type������Ϊapplication/x-www-form-urlencoded��    
	            urlConn.setRequestProperty("Content-Type","multipart/form-data");    
	            // ���ӣ���postUrl.openConnection()���˵����ñ���Ҫ��connect֮ǰ��ɣ�  
	            // Ҫע�����connection.getOutputStream�������Ľ���connect��    
	            urlConn.connect();  
	            //DataOutputStream��  
	            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());  
	            //Ҫ�ϴ��Ĳ���  
	            StringBuilder sbBuilder = new StringBuilder();
	          //  String content = "sid=" + URLEncoder.encode("liuyu", "utf-8");   
	            
	            sbBuilder.append("\r\n"+"------WebKitFormBoundaryqJvqYWLztmu3HUyV"+"\r\n");
	            sbBuilder.append("Content-Disposition: form-data; name=\"sid\""+"\r\n"+"\r\n");
	            sbBuilder.append("mypic"+"\r\n"+"------WebKitFormBoundaryqJvqYWLztmu3HUyV"+"\r\n");
	            sbBuilder.append("Content-Disposition: form-data; name=\"fileupS\"; filename=\"360��check.txt\""+"\r\n");
	            sbBuilder.append("Content-Type: text/plain"+"\r\n"+"\r\n");
	            
	           ///////////////////���ݿ�ʼ 
	            sbBuilder.append("[main]"+"\r\n"+"����=1458438821"+"\r\n"+"γ��=1458438864");
	            
	            
	           //////////////////���ݽ���
	            sbBuilder.append("\r\n"+"\r\n"+"------WebKitFormBoundaryqJvqYWLztmu3HUyV--"+"\r\n");
	            
	            //��Ҫ�ϴ�������д������  
	            out.writeUTF(sbBuilder.toString());
	            //ˢ�¡��ر�  
	            out.flush();  
	            out.close();     

	            recode = urlConn.getResponseCode();
	            
	            Log.e("gps", String.valueOf(recode));
	        }catch(Exception e){  
	            
	            e.printStackTrace();  
	        }  
	        
	        return recode;
	    }  

}
