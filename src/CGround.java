import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;


public class CGround{
	
	final static int GroundX = 5;
	final static int GroundY= 5;

	int gdCrop;//种植作物  0 为未种植 大于0为种植对应作物，相关信息保存在Data 类里
	int gdState;//土地状态    0 为正常，1 为杂草，2为干旱,3 为虫害
	int gdGrowState;//植物成长状态
	int gdGrowDays;//植物成长天数
	int gdX;//在土地模块中的横坐标位置
	int gdY;//在土地模块中的纵坐标位置
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
	
	//改变土地状态
	void changeState(int state)
	{
		gdState = state;
	}
	
	//改变土地种植作物
	void changeCrop(int crop)
	{
		gdCrop = crop;
		gdState = 0;
		gdGrowState = 0;
		gdGrowDays = 0;
		
	}
	
	//时间推移
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
