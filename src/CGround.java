import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;


public class CGround{
	
	final static int GroundX = 5;
	final static int GroundY= 5;

	int gdCrop;//��ֲ����  0 Ϊδ��ֲ ����0Ϊ��ֲ��Ӧ��������Ϣ������Data ����
	int gdState;//����״̬    0 Ϊ������1 Ϊ�Ӳݣ�2Ϊ�ɺ�,3 Ϊ�溦
	int gdGrowState;//ֲ��ɳ�״̬
	int gdGrowDays;//ֲ��ɳ�����
	int gdX;//������ģ���еĺ�����λ��
	int gdY;//������ģ���е�������λ��
	static Random random = new Random(3);
	
	public CGround(int ix,int iy)
	{
		gdCrop = 0;
		gdState = 0;
		gdGrowState = 0;
		gdGrowDays = 0;
		gdX = ix + GroundX;
		gdY = iy + GroundY;
	}
	
	//�ı�����״̬
	void changeState(int state)
	{
		gdState = state;
	}
	
	//�ı�������ֲ����
	void changeCrop(int crop)
	{
		gdCrop = crop;
		gdState = 0;
		gdGrowState = 0;
		gdGrowDays = 0;
		
	}
	
	//ʱ������
	void NextDay()
	{
		if (gdCrop != 0 && gdGrowState < (Data.cropData[gdCrop][1]-1))
		{
			gdGrowDays++;
			if (gdGrowState != Data.cropData[gdCrop][1]-2)
				gdState = random.nextInt(3);
			if(Data.cropData[gdCrop][gdGrowState+2] >= gdGrowDays)
			{
			    gdGrowState++;
			}
		}
	}
	
	byte[] toByte()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		bos.write(gdCrop);
		bos.write(gdState);
		bos.write(gdGrowState);
		bos.write(gdGrowDays);
		
		return bos.toByteArray();
	}
	
	void loadData(ByteArrayInputStream bis)
	{
		gdCrop = bis.read();
		gdState = bis.read();
		gdGrowState = bis.read();
		gdGrowDays = bis.read();
	}
}
