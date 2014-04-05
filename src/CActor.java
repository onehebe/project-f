
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
	private static int[] DOWN = {0,1,2,3};//���µĶ���֡
	private static int[] LEFT = {4,5,6,7};//����Ķ���֡
	private static int[] RIGHT = {8,9,10,11};//���ҵĶ���֡
	private static int[] UP = {12,13,14,15};//���ϵĶ���֡
	
	static int iX,iY;//����λ��
	static int sState;//0����ֹ����  1����ֹ����  2����ֹ����  3����ֹ����
	static int hState;//����״��
	static int power;//����ֵ
	static int bagLv;//�����ȼ�
	static int[] bag;//����
	static int index;//������Ʒ���
	static int tIndex;//���Ϲ������
	static int gdExp;//��ֲ����
	static int gdLv;//��ֲ�ȼ�
	static int fishExp;//���㾭��
	static int fishLv;//����ȼ�
	static int aniExp;//����ι������
	static int aniLv;//����ι���ȼ�
	static int cookExp;//��⿾���
	static int cookLv;//��⿵ȼ�
	Random random;//�����������
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
	
	//�ƶ�
	void iMove(int dx,int dy)
	{
		
		/****************
		 * �߼���
		 */
		
		iX += dx;
		iY += dy;
		//this.setPosition(x, y)
		
		/****************
		 * ͼ���
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
	//ʰ����Ʒ
	int iPick(int res)
	{
		
		/****************
		 * �߼���
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
			case 2: mCanvas.infoRender("��ã�"+Data.cropName[res]);
					break;
			case 4: mCanvas.infoRender("���: "+Data.fishName[res-33]);
					break;
			}
			return 1;//�ɹ�ʰ��
		}
		else {	mCanvas.infoRender("��������");
				return 0;/*������ ʰ��ʧ��*/}
		/****************
		 * ͼ���
		 */
	
	}
	
	//ʹ����Ʒ
	void iUse(int i)
	{
		
		/****************
		 * �߼���
		 */
		
		int a = bag[i]/10;
		switch (a)
		{
			case 0 ://TODO ��ͬ��Ʒʹ��
			case 1 :
			case 2 :
		}
		
		/****************
		 * ͼ���
		 */

	}
	
	//�ж���Ʒλ��
	int iHave(int target)
	{
		for (int i = 0;i<(BAGNUM+bagLv);i++)
		{
			if (bag[i] == target)
				return i;
		}
		return -1;
	}
	
	//��ֲ
	void plant(CGround cg)
	{
		
		/****************
		 * �߼���
		 */
		mCanvas.infoRender("��ֲ�ɹ�");
		cg.changeCrop(bag[index] - 40);
		bag[index] = 0;
		//��������
		addExp(FARM,3);
		//ʱ�����
		mCanvas.skip(5);
		
		
		/****************
		 * ͼ���
		 */
		
	}

	//����
	void weed(CGround cg)
	{
		
		/****************
		 * �߼���
		 */	
		mCanvas.infoRender("���ݳɹ�");
		cg.changeState(0);
			
		power--;
		//��������
		addExp(FARM,1);
		//ʱ�����
		mCanvas.skip(5);
	}
	
	//���
	void irrigate(CGround cg)
	{
		/****************
		 * �߼���
		 */
		mCanvas.infoRender("��ȳɹ�");
		if(cg.gdState == 2)
		{
			cg.changeState(0);
		}
		//��������
		addExp(FARM,1);
		//ʱ�����
		mCanvas.skip(5);
		
		/****************
		 * ͼ���
		 */

	}
	
	//����
	boolean plough(CGround cg)
	{
		
		/****************
		 * �߼���
		 */
		if (cg.gdCrop != 0)
		{
			if (mCanvas.confirmRender("�Ƿ��������ֲ��"))
			{
				cg.changeState(0);
				cg.changeCrop(0);
				
				power--;
				//��������
				addExp(FARM,1);
				//ʱ�����
				mCanvas.skip(5);
				return true;
				/****************
				 * ͼ���
				 */
			}
		}
		return false;
	}
	
	//ɱ��
	void killBug(CGround cg)
	{
		/****************
		 * �߼���
		 */
		System.out.println("killBug");
		cg.changeState(0);
		
		power--;
		//��������
		addExp(FARM,1);
		//ʱ�����
		mCanvas.skip(5);
		
		/****************
		 * ͼ���
		 */
	}
	
	//�ջ�
	void gain(CGround cg)
	{
		/****************
		 * �߼���
		 */

		if (cg.gdGrowState ==  Data.cropData[cg.gdCrop][1])
		{
			
			iPick(cg.gdCrop);
			cg.changeState(cg.gdGrowState+1);
			
		}
		//��������
		addExp(FARM,10);
		//ʱ�����
		mCanvas.skip(5);
		
		/****************
		 * ͼ���
		 */
	}
	
	//����
	void fish()
	{
		/****************
		 * �߼���
		 */
			iPick(random.nextInt(fishLv+1)+33);
		//��������
		addExp(FISH,5);
		//ʱ�����
		mCanvas.skip(15);
		
		/****************
		 * ͼ���
		 */
	}
	
	//ι������
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
					mCanvas.infoRender("ι���ɹ�");
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
					mCanvas.infoRender("ι���ɹ�");
					return 1;
				}
			}
			return 0;
		default:return -1;
		}
	}
	//���ƶ���
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
					mCanvas.infoRender("���Ƴɹ�");
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
					mCanvas.infoRender("���Ƴɹ�");
					return 1;
				}
			}
			return 0;
		default:return -1;
		}
	}
	
	int buyAni(int aniSN)//����һֻ����
	{
		
		/****************
		 * �߼���
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
							mCanvas.infoRender("�޿�λ");
							return 0;//�޿�λ
						}else
						{
							mCanvas.dull[aniIndex].changeKind(aniSN);
							mCanvas.money -= Data.aniPrice[aniIndex][0];
							mCanvas.infoRender("��һֻ��ţ ");
							return 1;//���׳ɹ�
						}
				case 2:	while (aniIndex < 4 && mCanvas.chicken[aniIndex].aniKind != 0)
						{
							aniIndex++;
						}
						if (aniIndex == 4)
						{
							mCanvas.infoRender("�޿�λ");
							return 0;//�޿�λ
						}else
						{
							mCanvas.chicken[aniIndex].changeKind(aniSN);
							mCanvas.money -= Data.aniPrice[aniIndex][0];
							mCanvas.infoRender("��һֻ��");
							return 1;//���׳ɹ�
						}
				case 3:	while (aniIndex < 4 && mCanvas.chicken[aniIndex].aniKind != 0)
						{
							aniIndex++;
						}
						if (aniIndex == 4)
						{
							mCanvas.infoRender("�޿�λ");
							return 0;//�޿�λ
						}else
						{
							mCanvas.chicken[aniIndex].changeKind(aniSN);
							mCanvas.money -= Data.aniPrice[aniIndex][0];
							mCanvas.infoRender("��һֻѼ");
							return 1;//���׳ɹ�
						}
				default: return -1;
			}
			
		}else
		{
			return -1;//��Ǯ����
		}
		/****************
		 * ͼ���
		 */
	}
	int saleAni(int anik,int aniIndex)//����һֻ����
	{
		/****************
		 * �߼���
		 */
		/*switch (anik)
		{
		case 1:	if (mCanvas.dull[aniIndex].aniState<Data.aniData[anik][0])
				{
					return -1;//δ����
				}else if (mCanvas.dull[aniIndex].aniKind == 0) {return 0;/*û�ж���///}
				else
				{
					mCanvas.dull[aniIndex].changeKind(0);
					mCanvas.money += Data.aniPrice[aniIndex][1];
					return 1;//���׳ɹ�
				}
		case 2: if (mCanvas.chicken[aniIndex-3].aniState<Data.aniData[anik][0])
				{
					return -1;//δ����
				}else if (mCanvas.chicken[aniIndex-3].aniKind == 0) 
						{return 0;/*û�ж���///}
					else
					{
						mCanvas.chicken[aniIndex].changeKind(0);
						mCanvas.money += Data.aniPrice[aniIndex][1];
						return 1;//���׳ɹ�
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
				mCanvas.infoRender("����"+Data.aniName[1]);
				return 1;
		case 2:
		case 3:
				mCanvas.chicken[aniIndex-3].aniKind = 0;
				mCanvas.chicken[aniIndex-3].aniGrowDays = 0;
				mCanvas.chicken[aniIndex-3].aniState = 0;
				mCanvas.chicken[aniIndex-3].aniGrowState = 0;
				mCanvas.money += Data.aniPrice[anik][1];
				mCanvas.infoRender("����"+Data.aniName[anik]);
				return 1;
		default:
				return 1;
		}
		/****************
		 * ͼ���
		 */
	}
	
	//���
	boolean cook(int ckKind)
	{
		for (int i=1;i<=Data.cookData[ckKind][0];i++)
		{
			if (iHave(Data.cookData[ckKind][i+1]) == -1)
			{
				return false;//ȱ����Ʒ
			}
		}
		for (int i=1;i<=Data.cookData[ckKind][0];i++)
		{
			bag[iHave(Data.cookData[ckKind][i+1])] = 0;
		}
		addExp(COOK,10);
		mCanvas.infoRender("������");
		iPick(Data.cookData[ckKind][1]);
		return true;//�����ɹ�
	}
	
	//��Ϣ
	void sleep()
	{
		if (power < 0)
		{
			if (mCanvas.hour == 0)
			{
				mCanvas.skip(480);
				//TODO ��ͼ	
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
			
			//������Ϣ�ж�
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
			//TODO ������Ϣ��ʾ
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
				mCanvas.infoRender("������������");
				this.gdLv++;
			}
			break;
		case FISH:
			if (this.fishLv < Data.fishExpTab[0])
				this.fishExp += num;
			if (this.fishExp >= Data.fishExpTab[this.fishLv+1])
			{
				mCanvas.infoRender("������������");
				this.fishLv++;
			}
			break;
		case ANI:
			if (this.aniLv < Data.aniExpTab[0])
				this.aniExp += num;
			if (this.aniExp >= Data.aniExpTab[this.aniLv+1])
			{
				mCanvas.infoRender("ι����������");
				this.aniLv++;
			}
			break;
		case COOK:
			if (this.cookLv < Data.cookExpTab[0])
				this.cookExp += num;
			if (this.cookExp >= Data.cookExpTab[this.cookLv+1])
			{
				mCanvas.infoRender("�����������");
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
