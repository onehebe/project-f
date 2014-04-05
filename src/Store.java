import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class Store {
	public int length;
	StoreElement[] storeElement;
	
	public Store(int max)
	{
		length = max;
		storeElement = new StoreElement[max];
		for (int i=0;i<max;i++)
		{
			storeElement[i] = new StoreElement();
		}
	}
	
	public int findStore()
	{
		for (int i=0;i<storeElement.length;i++)
		{
			if(storeElement[i].goods == 0)
				return i;
		}
		return -1;
	}
	
	public int findStore(int target)
	{
		for (int i=0;i<storeElement.length;i++)
		{
			if(storeElement[i].goods == target)
				return i;
		}
		return -1;
	}
	
	public boolean add(int target)
	{
		int i,j;
		if ((i = findStore()) == -1)
		{
			return false;
		}else{
			if ((j = findStore(target)) != -1)
			{
				storeElement[j].num++;
			}else
			{
				storeElement[i].goods = target;
				storeElement[i].num ++;
			}
			return true;
		}
	}
	
	public boolean add(int target,int num)
	{
		int i,j;
		if ((i = findStore()) == -1)
		{
			return false;
		}else{
			if ((j = findStore(target)) != -1)
			{
				storeElement[j].num += num;
			}else
			{
				storeElement[i].goods = target;
				storeElement[i].num += num;
			}
			return true;
		}
	}
	
	public boolean remove(int target)
	{
		int i;
		if ((i = findStore(target)) != -1)
		{
			storeElement[i].goods = 0;
			storeElement[i].num = 0;
			return true;
		}else{
			return false;
		}
	}
	
	public boolean remove(int target,int num)
	{
		int i;
		if ((i = findStore(target)) != -1)
		{
			if (storeElement[i].num < num)
			{
				return false;
			}else{
				storeElement[i].num -= num;
				if (storeElement[i].num == 0)
					storeElement[i].goods = 0;
				return true;
			}
		}else{
			return false;
		}
	}
	
	public int getGoods(int index)
	{
		return this.storeElement[index].goods;
	}
	
	public int getNum(int index)
	{
		return this.storeElement[index].num;
	}
	
	byte[] toByte()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		for (int i=0;i<length;i++)
		{
			bos.write(storeElement[i].goods);
			bos.write(storeElement[i].num);
		}
		return bos.toByteArray();
	}
	
	void loadData(ByteArrayInputStream bos)
	{
		for (int i=0;i<length;i++)
		{
			storeElement[i].goods = bos.read();
			storeElement[i].num = bos.read();
		}
	}
}


