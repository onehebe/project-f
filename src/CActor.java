
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;


public class CActor extends Sprite {
	
	final static int BAGNUM = 6;
	final static int TOOLNUM = 5;
	final static int FARM = 1;
	final static int FISH = 2;
	final static int ANI = 3;
	final static int COOK = 4;
	private static int[] DOWN = {0,1,2,3};//向下的动画帧
	private static int[] LEFT = {4,5,6,7};//向左的动画帧
	private static int[] RIGHT = {8,9,10,11};//向右的动画帧
	private static int[] UP = {12,13,14,15};//向上的动画帧
	
	static int iX,iY;//主角位置
	static int sState;//0：静止向左  1：静止向右  2：静止向上  3：静止向下
	static int hState;//健康状况
	static int power;//体力值
	static int bagLv;//背包等级
	static int[] bag;//背包
	static int index;//手上物品序号
	static int tIndex;//手上工具序号
	static int gdExp;//种植经验
	static int gdLv;//种植等级
	static int fishExp;//钓鱼经验
	static int fishLv;//钓鱼等级
	static int aniExp;//动物喂养经验
	static int aniLv;//动物喂养等级
	static int cookExp;//烹饪经验
	static int cookLv;//烹饪等级
	Random random;//随机数产生器
	mainCanvas mCanvas;
	
	public CActor(Image pic,int FrameWidth,int FrameHeight,mainCanvas gameCanvas)
	{
		super(pic,FrameWidth,FrameHeight);
		mCanvas = gameCanvas;
		intialize();	
	}
	
	void intialize()
	{
		random = new Random();
		iX = 4;
		iY = 3;
		power = 51;
		tIndex = 0;
		bagLv = 0;
		bag = new int[BAGNUM+bagLv];
		bag[0] = 2;
		bag[1] = 1;
		bag[2] = 3;
		bag[3] = 4;
		index = 0;
		fishLv = 0;
		gdLv = 0;
		gdExp = 199;
		cookExp = 0;
		cookLv = 0;
	}
	
	//移动
	void iMove(int dx,int dy)
	{
		
		/****************
		 * 逻辑层
		 */
		
		iX += dx;
		iY += dy;
		//this.setPosition(x, y)
		
		/****************
		 * 图像层
		 */
	}
	
	void iTurn(int aspect)
	{
		switch (aspect)
		{
			case 2:
				   this.setFrameSequence(UP);
				   break;
			case 4:
				   this.setFrameSequence(LEFT);
				   break;
			case 32:
				   this.setFrameSequence(RIGHT);
				   break;
			case 64:
				   this.setFrameSequence(DOWN);
				   break;
		}
		this.setFrame(0);
	}
	//拾起物品
	int iPick(int res)
	{
		
		/****************
		 * 逻辑层
		 */
		int i = 0;
		while (i<(BAGNUM+bagLv) )
		{
			if (bag[i] == 0) break;
			i++;
		}
		if (i<(BAGNUM+bagLv))
		{
			bag[i] = res;
			switch ((res-1)/8)
			{
			case 0:
			case 1:
			case 2: mCanvas.infoRender("获得："+Data.cropName[res]);
					break;
			case 4: mCanvas.infoRender("获得: "+Data.fishName[res-33]);
					break;
			}
			return 1;//成功拾起
		}
		else {	mCanvas.infoRender("包裹已满");
				return 0;/*包裹满 拾起失败*/}
		/****************
		 * 图像层
		 */
	
	}
	
	//使用物品
	void iUse(int i)
	{
		
		/****************
		 * 逻辑层
		 */
		
		int a = bag[i]/10;
		switch (a)
		{
			case 0 ://TODO 不同物品使用
			case 1 :
			case 2 :
		}
		
		/****************
		 * 图像层
		 */

	}
	
	//判断物品位置
	int iHave(int target)
	{
		for (int i = 0;i<(BAGNUM+bagLv);i++)
		{
			if (bag[i] == target)
				return i;
		}
		return -1;
	}
	
	//种植
	void plant(CGround cg)
	{
		
		/****************
		 * 逻辑层
		 */
		mCanvas.infoRender("种植成功");
		cg.changeCrop(bag[index] - 40);
		bag[index] = 0;
		//经验增长
		addExp(FARM,3);
		//时间控制
		mCanvas.skip(5);
		
		
		/****************
		 * 图像层
		 */
		
	}

	//除草
	void weed(CGround cg)
	{
		
		/****************
		 * 逻辑层
		 */	
		mCanvas.infoRender("除草成功");
		cg.changeState(0);
			
		power--;
		//经验增长
		addExp(FARM,1);
		//时间控制
		mCanvas.skip(5);
	}
	
	//灌溉
	void irrigate(CGround cg)
	{
		/****************
		 * 逻辑层
		 */
		mCanvas.infoRender("灌溉成功");
		if(cg.gdState == 2)
		{
			cg.changeState(0);
		}
		//经验增长
		addExp(FARM,1);
		//时间控制
		mCanvas.skip(5);
		
		/****************
		 * 图像层
		 */

	}
	
	//耕地
	boolean plough(CGround cg)
	{
		
		/****************
		 * 逻辑层
		 */
		if (cg.gdCrop != 0)
		{
			if (mCanvas.confirmRender("是否除掉已种植物"))
			{
				cg.changeState(0);
				cg.changeCrop(0);
				
				power--;
				//经验增长
				addExp(FARM,1);
				//时间控制
				mCanvas.skip(5);
				return true;
				/****************
				 * 图像层
				 */
			}
		}
		return false;
	}
	
	//杀虫
	void killBug(CGround cg)
	{
		/****************
		 * 逻辑层
		 */
		System.out.println("killBug");
		cg.changeState(0);
		
		power--;
		//经验增长
		addExp(FARM,1);
		//时间控制
		mCanvas.skip(5);
		
		/****************
		 * 图像层
		 */
	}
	
	//收获
	void gain(CGround cg)
	{
		/****************
		 * 逻辑层
		 */

		if (cg.gdGrowState ==  Data.cropData[cg.gdCrop][1])
		{
			
			iPick(cg.gdCrop);
			cg.changeState(cg.gdGrowState+1);
			
		}
		//经验增长
		addExp(FARM,10);
		//时间控制
		mCanvas.skip(5);
		
		/****************
		 * 图像层
		 */
	}
	
	//钓鱼
	void fish()
	{
		/****************
		 * 逻辑层
		 */
			iPick(random.nextInt(fishLv+1)+33);
		//经验增长
		addExp(FISH,5);
		//时间控制
		mCanvas.skip(15);
		
		/****************
		 * 图像层
		 */
	}
	
	//喂养动物
	int feed(int kind)
	{
		switch (kind)
		{
		case 1:for (int i=0;i<mCanvas.dull.length;i++)
			{
				if (mCanvas.dull[i].aniKind!=0 && mCanvas.dull[i].aniState == 1)
				{
					mCanvas.dull[i].aniState = 0;
					addExp(ANI,5);
					mCanvas.infoRender("喂养成功");
					return 1;
				}
			}
			return 0;
		case 2:
			for (int i=0;i<mCanvas.chicken.length;i++)
			{
				if (mCanvas.chicken[i].aniKind!=0 && mCanvas.chicken[i].aniState == 1)
				{
					mCanvas.chicken[i].aniState = 0;
					addExp(ANI,5);
					mCanvas.infoRender("喂养成功");
					return 1;
				}
			}
			return 0;
		default:return -1;
		}
	}
	//治疗动物
	int medi(int kind)
	{
		switch (kind)
		{
		case 1:for (int i=0;i<3;i++)
			{
				if (mCanvas.dull[i].aniKind!=0 && mCanvas.dull[i].aniState == 2)
				{
					mCanvas.dull[i].aniState = 0;
					addExp(ANI,5);
					mCanvas.infoRender("治疗成功");
					return 1;
				}
			}
			return 0;
		case 2:
			for (int i=0;i<4;i++)
			{
				if (mCanvas.chicken[i].aniKind!=0 && mCanvas.chicken[i].aniState == 2)
				{
					mCanvas.chicken[i].aniState = 0;
					addExp(ANI,5);
					mCanvas.infoRender("治疗成功");
					return 1;
				}
			}
			return 0;
		default:return -1;
		}
	}
	
	int buyAni(int aniSN)//购买一只动物
	{
		
		/****************
		 * 逻辑层
		 */	
		int aniIndex = 0;
		if (mCanvas.money < Data.aniPrice[aniSN][0])	
		{
			switch (aniSN)
			{
				case 1:	while (aniIndex < 3 && mCanvas.dull[aniIndex].aniKind != 0)
						{
							aniIndex++;
						}
						if (aniIndex == 4)
						{
							mCanvas.infoRender("无空位");
							return 0;//无空位
						}else
						{
							mCanvas.dull[aniIndex].changeKind(aniSN);
							mCanvas.money -= Data.aniPrice[aniIndex][0];
							mCanvas.infoRender("买到一只奶牛 ");
							return 1;//交易成功
						}
				case 2:	while (aniIndex < 4 && mCanvas.chicken[aniIndex].aniKind != 0)
						{
							aniIndex++;
						}
						if (aniIndex == 4)
						{
							mCanvas.infoRender("无空位");
							return 0;//无空位
						}else
						{
							mCanvas.chicken[aniIndex].changeKind(aniSN);
							mCanvas.money -= Data.aniPrice[aniIndex][0];
							mCanvas.infoRender("买到一只鸡");
							return 1;//交易成功
						}
				case 3:	while (aniIndex < 4 && mCanvas.chicken[aniIndex].aniKind != 0)
						{
							aniIndex++;
						}
						if (aniIndex == 4)
						{
							mCanvas.infoRender("无空位");
							return 0;//无空位
						}else
						{
							mCanvas.chicken[aniIndex].changeKind(aniSN);
							mCanvas.money -= Data.aniPrice[aniIndex][0];
							mCanvas.infoRender("买到一只鸭");
							return 1;//交易成功
						}
				default: return -1;
			}
			
		}else
		{
			return -1;//金钱不足
		}
		/****************
		 * 图像层
		 */
	}
	int saleAni(int anik,int aniIndex)//卖掉一只动物
	{
		/****************
		 * 逻辑层
		 */
		/*switch (anik)
		{
		case 1:	if (mCanvas.dull[aniIndex].aniState<Data.aniData[anik][0])
				{
					return -1;//未长大
				}else if (mCanvas.dull[aniIndex].aniKind == 0) {return 0;/*没有动物///}
				else
				{
					mCanvas.dull[aniIndex].changeKind(0);
					mCanvas.money += Data.aniPrice[aniIndex][1];
					return 1;//交易成功
				}
		case 2: if (mCanvas.chicken[aniIndex-3].aniState<Data.aniData[anik][0])
				{
					return -1;//未长大
				}else if (mCanvas.chicken[aniIndex-3].aniKind == 0) 
						{return 0;/*没有动物///}
					else
					{
						mCanvas.chicken[aniIndex].changeKind(0);
						mCanvas.money += Data.aniPrice[aniIndex][1];
						return 1;//交易成功
					}
		default: return -1;
		}
		*/
		switch (anik)
		{
		case 1:
				mCanvas.dull[aniIndex].aniKind = 0;
				mCanvas.dull[aniIndex].aniGrowDays = 0;
				mCanvas.dull[aniIndex].aniState = 0;
				mCanvas.dull[aniIndex].aniGrowState = 0;
				mCanvas.money += Data.aniPrice[1][1];
				mCanvas.infoRender("卖出"+Data.aniName[1]);
				return 1;
		case 2:
		case 3:
				mCanvas.chicken[aniIndex-3].aniKind = 0;
				mCanvas.chicken[aniIndex-3].aniGrowDays = 0;
				mCanvas.chicken[aniIndex-3].aniState = 0;
				mCanvas.chicken[aniIndex-3].aniGrowState = 0;
				mCanvas.money += Data.aniPrice[anik][1];
				mCanvas.infoRender("卖出"+Data.aniName[anik]);
				return 1;
		default:
				return 1;
		}
		/****************
		 * 图像层
		 */
	}
	
	//烹饪
	boolean cook(int ckKind)
	{
		for (int i=1;i<=Data.cookData[ckKind][0];i++)
		{
			if (iHave(Data.cookData[ckKind][i+1]) == -1)
			{
				return false;//缺少物品
			}
		}
		for (int i=1;i<=Data.cookData[ckKind][0];i++)
		{
			bag[iHave(Data.cookData[ckKind][i+1])] = 0;
		}
		addExp(COOK,10);
		mCanvas.infoRender("烹饪完成");
		iPick(Data.cookData[ckKind][1]);
		return true;//制作成功
	}
	
	//休息
	void sleep()
	{
		if (power < 0)
		{
			if (mCanvas.hour == 0)
			{
				mCanvas.skip(480);
				//TODO 绘图	
			}else{
				mCanvas.hour = 23;
				mCanvas.skip(540);
			}		
			power += 30;
			mCanvas.nextDay();
			sleep();
			
		}else 
		{
			mCanvas.hour = 23;
			mCanvas.min = 0;
			mCanvas.skip(540);
			mCanvas.nextDay();
			power += 50;
			if (power >51)
				power = 51;
			
			//节日信息判断
			mCanvas.eSwitch = 0;
			for (int i=0;i<6;i++)
			{
				if (mCanvas.mouth == Data.fesData[i] && mCanvas.day == 30)
				{
					mCanvas.eSwitch = i;
					break;
				}else if (mCanvas.mouth == Data.fesData[i] && mCanvas.day == 29)
				{
					mCanvas.eSwitch = -i;
					break;
				}
			}
			//TODO 节日信息提示
			switch (mCanvas.eSwitch)
			{
				case -1:
				case 1:
				case -2:
				case 2:
				case -3:
				case 3:
				case -4:
				case 4:
				case -5:
				case 5:
			}
		}
	}

	void addExp(int src,int num)
	{
		switch (src)
		{
		case FARM:
			if (this.gdLv < Data.gdExpTab[0])
			{
				this.gdExp += num;
			}
			if (this.gdExp >= Data.gdExpTab[this.gdLv+1])
			{
				mCanvas.infoRender("耕种升级啦！");
				this.gdLv++;
			}
			break;
		case FISH:
			if (this.fishLv < Data.fishExpTab[0])
				this.fishExp += num;
			if (this.fishExp >= Data.fishExpTab[this.fishLv+1])
			{
				mCanvas.infoRender("钓鱼升级啦！");
				this.fishLv++;
			}
			break;
		case ANI:
			if (this.aniLv < Data.aniExpTab[0])
				this.aniExp += num;
			if (this.aniExp >= Data.aniExpTab[this.aniLv+1])
			{
				mCanvas.infoRender("喂养升级啦！");
				this.aniLv++;
			}
			break;
		case COOK:
			if (this.cookLv < Data.cookExpTab[0])
				this.cookExp += num;
			if (this.cookExp >= Data.cookExpTab[this.cookLv+1])
			{
				mCanvas.infoRender("烹饪升级啦！");
				this.cookLv++;
			}
			break;
		}
	}
	
	byte[] toByte()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write(iX);
		bos.write(iY);
		bos.write(sState);
		bos.write(hState);
		bos.write(power);
		bos.write(bagLv);
		for (int i=0;i<BAGNUM;i++)
			bos.write(bag[i]);
		bos.write(index);
		bos.write(tIndex);
		bos.write(gdExp);
		bos.write(gdLv);
		bos.write(fishExp);
		bos.write(fishLv);
		bos.write(aniExp);
		bos.write(aniLv);
		bos.write(aniExp);
		bos.write(aniLv);
		bos.write(cookExp);
		bos.write(cookLv);
		
		return bos.toByteArray();
	}
	
	void loadData(ByteArrayInputStream bis)
	{
		
		iX = bis.read();
		iY = bis.read();
		sState = bis.read();
		hState = bis.read();
		power = bis.read();
		bagLv = bis.read();
		for (int i=0;i<BAGNUM;i++)
			bag[i] = bis.read();
		index = bis.read();
		tIndex = bis.read();
		gdExp = bis.read();
		gdLv = bis.read();
		fishExp = bis.read();
		fishLv = bis.read();
		aniExp = bis.read();
		aniLv = bis.read();
		cookExp = bis.read();
		cookLv = bis.read();
	}
}
