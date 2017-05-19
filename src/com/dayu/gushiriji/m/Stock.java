package com.dayu.gushiriji.m;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Stock
{
	private String Stock_name;  // ��Ʊ����
	private String Stock_sid;   // ��Ʊ����
	private String cjrq;  // �ɽ�����
	private String cjsj;   // �ɽ�ʱ��
	private String start_time = "20990101";  // ��ʼ���й�Ʊ
	private String end_time = "20010101";   //  �������й�Ʊ
	private int buy_times;    //  �������
	private int sell_times;    //  ��������
	private String hold_time;    //  ����ʱ������
	private int fengu_tol ;  //  �ֹ�����
	private int buy_tol ;   //����ɷ�
	private int sell_tol ;   //�����ɷ�
	private int fenhong_times;  // �ֺ����
	private double jiaoyizongjine; //�����ܽ��
	private double fenhong_tol ;  // �ֺ��ܽ��
	private double buy_tolmon ;  //������
	private double sell_tolmon ;  //�������
	private double yongjin_tol ;    // Ӷ���ܶ�
	private double yinhuashui_tol ;   // ӡ��˰�ܶ�
	private double guohufei_tol ;     // �������ܶ�
	private double qita_tol ;          //�����ܶ�
	private double jiaoyiguifei_tol ;  //���׹��
	private double pingjunchengben ;  //ƽ���ɱ�
	private double guxihonglisuibujiao ; //��Ϣ����˰����
	private double zonglirun ;          //������ 
	private boolean iscompleted = false;  //�Ƿ������Ľ���
	private DecimalFormat df = new DecimalFormat("000000");
	
	/**
	 * �Ƚϴ�������ʱ����ʵ��ʱ��Ĵ�С�������ǰʱ��Ȳ����󣬷���true��С�򷵻�false
	 * @param time
	 * @return boolean
	 */
	public boolean bijiaotime(String time1,String time2) //�Ƚϵ�ǰϵͳʱ���봫������ʱ���С
	{
		   
		   SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		    
		   Date dat1,dat2 = null;
		try
		{
			dat1 = df.parse(time1);
			dat2 = df.parse(time2);
			
			if ((dat1.getTime()-dat2.getTime())>0)
			   {
				   return true;
			   }
			   else {
				  return false;
			   }
		} catch (ParseException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return false;
	}
	
	public static double return2decimal(double d)  //������λС����double
	{
		BigDecimal b = new  BigDecimal(d); 
		return b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
	}
	
	public static String return2decimal(String d)  //������λС����double
	{
		BigDecimal b = new  BigDecimal(d); 
		return String.valueOf( b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());  
	}

	public double getJiaoyizongjine()
	{
		jiaoyizongjine = buy_tolmon + sell_tolmon;
		return return2decimal(jiaoyizongjine);
	}


	public double getGuxihonglisuibujiao()
	{
		return return2decimal(guxihonglisuibujiao);
	}

	public void setGuxihonglisuibujiao(double guxihonglisuibujiao)
	{
		this.guxihonglisuibujiao = guxihonglisuibujiao;
	}

	public int getFenhong_times()
	{
		return fenhong_times;
	}

	public void setFenhong_times(int fenhong_times)
	{
		this.fenhong_times = fenhong_times;
	}

	public double getBuy_tolmon()
	{
		return return2decimal(buy_tolmon);
	}

	public void setBuy_tolmon(double buy_tolmon)
	{
		this.buy_tolmon = buy_tolmon;
	}

	public double getSell_tolmon()
	{
		return return2decimal(sell_tolmon);
	}

	public void setSell_tolmon(double sell_tolmon)
	{
		this.sell_tolmon = sell_tolmon;
	}

	public void setBuy_tol(int buy_tol)
	{
		this.buy_tol = buy_tol;
	}

	public void setSell_tol(int sell_tol)
	{
		this.sell_tol = sell_tol;
	}

	public boolean isIscompleted()
	{
		return iscompleted;
	}

	public void setIscompleted() 
	{
		//��������ɷ�-����ɷ�+�ֺ�ɷ�=0 ������ȫ�����
		if ((sell_tol+buy_tol+fengu_tol)==0)
		this.iscompleted = true;
	}

	public String getStock_sid()
	{
		return Stock_sid;
	}

	public void setStock_sid(String stock_sid)
	{
		Stock_sid =  df.format( Integer.valueOf(stock_sid)) ;
	}

	public String getStart_time()
	{
		return start_time;
	}

	public void setStart_time(String start_time)
	{
		if (bijiaotime(this.start_time,start_time))
		{
			this.start_time = start_time;
		}
		
	}

	public String getEnd_time()
	{
		return end_time;
	}

	public void setEnd_time(String end_time)
	{
		if (bijiaotime(end_time,this.end_time))
		{
			this.end_time = end_time;
		}
	}

	public int getBuy_times()
	{
		return buy_times;
	}

	public void setBuy_times(int buy_times)
	{
		this.buy_times = buy_times;
	}

	public int getSell_times()
	{
		return sell_times;
	}

	public void setSell_times(int sell_times)
	{
		this.sell_times = sell_times;
	}

	public String getHold_time()
	{
		return hold_time;
	}

	public void setHold_time()
	{
		   SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		    
		   Date dat1,dat2 = null;
		try
		{
			dat1 = df.parse(this.end_time);
			dat2 = df.parse(this.start_time);
			
				this.hold_time = String.valueOf((dat1.getTime()-dat2.getTime())/(1000*24*3600));
			   
		} catch (ParseException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	public int getFengu_tol()
	{
		return fengu_tol;
	}

	public void setFengu_tol(int fengu_tol)
	{
		this.fengu_tol = fengu_tol;
	}

	public double getFenhong_tol()
	{
		return return2decimal(fenhong_tol);
	}

	public void setFenhong_tol(double fenhong_tol)
	{
		this.fenhong_tol = fenhong_tol;
	}

	public int getBuy_tol()
	{
		return buy_tol;
	}


	public int getSell_tol()
	{
		return sell_tol;
	}


	public double getYongjin_tol()
	{
		return return2decimal(yongjin_tol);
	}

	public void setYongjin_tol(double yongjin_tol)
	{
		this.yongjin_tol = yongjin_tol;
	}

	public double getYinhuashui_tol()
	{
		return return2decimal(yinhuashui_tol);
	}

	public void setYinhuashui_tol(double yinhuashui_tol)
	{
		this.yinhuashui_tol = yinhuashui_tol;
	}

	public double getGuohufei_tol()
	{
		return return2decimal(guohufei_tol);
	}

	public void setGuohufei_tol(double guohufei_tol)
	{
		this.guohufei_tol = guohufei_tol;
	}

	public double getQita_tol()
	{
		return return2decimal(qita_tol);
	}

	public void setQita_tol(double qita_tol)
	{
		this.qita_tol = qita_tol;
	}

	public double getJiaoyiguifei_tol()
	{
		return return2decimal(jiaoyiguifei_tol);
	}

	public void setJiaoyiguifei_tol(double jiaoyiguifei_tol)
	{
		this.jiaoyiguifei_tol = jiaoyiguifei_tol;
	}

	public double getPingjunchengben()
	{
		pingjunchengben = buy_tolmon / (buy_tol + fengu_tol);
		return return2decimal(pingjunchengben);
	}


	public String getStock_name()
	{
		return Stock_name;
	}

	public void setStock_name(String stock_name)
	{
		Stock_name = stock_name;
	}


	public double getZonglirun()
	{
		return return2decimal(zonglirun);
	}


	public void setZonglirun()
	{
		
		this.zonglirun = sell_tolmon - buy_tolmon + fenhong_tol
				         - yongjin_tol - yinhuashui_tol
				         - guohufei_tol - qita_tol - jiaoyiguifei_tol 
				         - guxihonglisuibujiao;
	}

	public String getCjrq()
	{
		return cjrq;
	}

	public void setCjrq(String cjrq)
	{
		this.cjrq = cjrq;
	}

	public String getCjsj()
	{
		return cjsj;
	}

	public void setCjsj(String cjsj)
	{
		this.cjsj = cjsj;
	}
	
	
}
