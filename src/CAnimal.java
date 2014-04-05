import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;


public class CAnimal {

	mainCanvas mCanvas;
	
	int aniIndex;
	int aniState;//动物状态（0：正常，1：生病，2：饥饿）
	int aniKind;//动物种类（0：无此动物，大于0：为数字对应的动物 1.鸡 2.鸭 3.牛）
	int aniGrowState;//动物成长状态
	int aniGrowDays;//动物成长天数
	static Random random = new Random(3);
	
	public CAnimal(int i)
	{
		aniIndex = i;
		aniState = 0;
		aniKind = 0;
		aniGrowState = 0;
		aniGrowDays = 0;
	}
	
	void changeKind(int kind)//改变动物种类
	{
		aniKind = kind;
	}
	
	void changeState(int state)//改变动物状态
	{
		aniState = 0;
	}
	
	//时间推移
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
