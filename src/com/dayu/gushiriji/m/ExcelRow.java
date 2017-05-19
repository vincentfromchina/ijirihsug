package com.dayu.gushiriji.m;

public class ExcelRow
{
	private int cjrq; //成交日期
    private int cjsj; //成交时间
	private int zqdm;  //证券代码
	private int zqmc;  //证券名称
	private int mmbz; //买卖标志
	private int cjjg; //成交价格
	private int cjsl; //成交数量
	private int cjje; //成交金额
	private int jyj;  //净佣金
	private int yhS;  //印花税
	private int ghf;  //过户费
	private int qtf;  //其他费
	private int jygf;  //交易规费
	private int bz;   //备注
	
	public static final String s_cjrq = "成交日期";
	public static final String s_cjsj = "成交时间";
	public static final String s_zqdm = "证券代码";
	public static final String s_zqmc = "证券名称";
	public static final String s_mmbz = "买卖标志";
	public static final String s_cjjg = "成交价格";
	public static final String s_cjsl = "成交数量";
	public static final String s_cjje = "成交金额";
	public static final String s_jyj = "净佣金";
	public static final String s_yhS = "印花税";
	public static final String s_ghf = "过户费";
	public static final String s_cz = "操作";
	public static final String s_sxf = "手续费";
	public static final String s_cjjj = "成交均价";
	public static final String s_qtf = "其他费";
	public static final String s_jygf = "交易规费";
	
	public int getCjrq()
	{
		return cjrq;
	}
	public void setCjrq(int cjrq)
	{
		this.cjrq = cjrq;
	}
	public int getZqdm()
	{
		return zqdm;
	}
	public void setZqdm(int zqdm)
	{
		this.zqdm = zqdm;
	}
	public int getZqmc()
	{
		return zqmc;
	}
	public void setZqmc(int zqmc)
	{
		this.zqmc = zqmc;
	}
	public int getMmbz()
	{
		return mmbz;
	}
	public void setMmbz(int mmbz)
	{
		this.mmbz = mmbz;
	}
	public int getCjjg()
	{
		return cjjg;
	}
	public void setCjjg(int cjjg)
	{
		this.cjjg = cjjg;
	}
	public int getCjsl()
	{
		return cjsl;
	}
	public void setCjsl(int cjsl)
	{
		this.cjsl = cjsl;
	}
	public int getCjje()
	{
		return cjje;
	}
	public void setCjje(int cjje)
	{
		this.cjje = cjje;
	}
	public int getJyj()
	{
		return jyj;
	}
	public void setJyj(int jyj)
	{
		this.jyj = jyj;
	}
	public int getYhS()
	{
		return yhS;
	}
	public void setYhS(int yhS)
	{
		this.yhS = yhS;
	}
	public int getGhf()
	{
		return ghf;
	}
	public void setGhf(int ghf)
	{
		this.ghf = ghf;
	}
	public int getQtf()
	{
		return qtf;
	}
	public void setQtf(int qtf)
	{
		this.qtf = qtf;
	}
	public int getJygf()
	{
		return jygf;
	}
	public void setJygf(int jygf)
	{
		this.jygf = jygf;
	}
	public int getBz()
	{
		return bz;
	}
	public void setBz(int bz)
	{
		this.bz = bz;
	}
	public int getCjsj()
	{
		return cjsj;
	}
	public void setCjsj(int cjsj)
	{
		this.cjsj = cjsj;
	}
	
	
	
}
