import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;


public class CAnimal {

	mainCanvas mCanvas;
	
	int aniIndex;
	int aniState;//����״̬��0��������1��������2��������
	int aniKind;//�������ࣨ0���޴˶������0��Ϊ���ֶ�Ӧ�Ķ��� 1.�� 2.Ѽ 3.ţ��
	int aniGrowState;//����ɳ�״̬
	int aniGrowDays;//����ɳ�����
	static Random random = new Random(3);
	
	public CAnimal(int i)
	{
		aniIndex = i;
		aniState = 0;
		aniKind = 0;
		aniGrowState = 0;
		aniGrowDays = 0;
	}
	
	void changeKind(int kind)//�ı䶯������
	{
		aniKind = kind;
	}
	
	void changeState(int state)//�ı䶯��״̬
	{
		aniState = 0;
	}
	
	//ʱ������
	void NextDay()
	{
		if (aniKind != 0 && aniState < 4)
		{
			aniGrowDays++;
			aniState = random.nextInt(3);
			if (aniGrowDays >= Data.aniData[aniKind][aniState+1])
				aniGrowState ++;
		}
	}
	
	byte[] toByte()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		bos.write(aniIndex);
		bos.write(aniState);
		bos.write(aniGrowState);
		bos.write(aniGrowDays);
		
		return bos.toByteArray();
	}
	
	void loadData(ByteArrayInputStream bis)
	{
		
		aniIndex = bis.read();
		aniState = bis.read();
		aniGrowState = bis.read();
		aniGrowDays = bis.read();
	}
}
