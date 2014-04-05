import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;


public class mainCanvas extends GameCanvas implements Runnable {

	final static int UP = 2;
	final static int DOWN = 64;
	final static int LEFT = 4;
	final static int RIGHT = 32;
	final static int CONF = 256;
	final static int STAR = 2048;
	final static int SPA = 4096;
	final static int NOKIA_S40_9 = 1024;
	final static int SE_A = 512;
	final static int SE_B = 1024;
	final static int WTK_1 = 512;
	final static int WTK_3 = 1024;
	final static int WTK_7 = 2048;
	final static int WTK_9 = 4096;
	final static int WIDTH = 176;
	final static int HEIGHT = 220;
	final static int VIEW_WIDTH = 176;
	final static int VIEW_HEIGHT = 115;
	final static int HEAD_HEIGHT = 33;
	final static int BAN_HEIGHT = 72;
	
	final static int HOE = 0;
	final static int HOOK = 1;
	final static int KETTEL = 2;
	final static int BIOCIDE = 3;
	final static int FISHPOLE = 4;
	
	final static int MAP_FIELD = 1;//逻辑层：土地序号
	final static int MAP_POOL = 4;//逻辑层：池塘序号
	final static int MAP_BED = 10;//逻辑层：床序号
	
	final static int GAME = 100;//普通状态
	final static int SHOP_BUY_MENU = 110;//买菜单
	final static int SHOP_SELL_MENU = 111;//买菜单
	final static int SHOP_CHOICE = 120;
	final static int ANISHOP_BUY = 121;
	final static int ANISHOP_SELL = 122;
	final static int ANISHOP_CHOICE = 123;
	
	final static int GX = 2,GY = 2,GW = 5,GH = 5;
	final static int WX = 0,WY = 0;
	final static int AX1 = 0,AY1 =0,AX2 = 0,AY2 = 0,AW1=0,AW2=0;
	final static int BLOCK_WIDTH = 32;
	
	final static int SELECTION = 3;
	final static int SYSSELECTION = 4;
	
    private Font gamefont;
	
	static private CActor actor;
	static public CGround[][] grd;
	static public CAnimal[] dull;
	static public CAnimal[] chicken;
	
	private boolean running;
	
	private boolean newin = true;
	private int gState;//游戏主线程状态
	public int sState;//动作状态
	public int  eSwitch;//事件开关
	
	private int key;//按键
	private boolean keyPressed;//按键是否按下
	
	static private int sIndex;//场景序号
	private int sWidth;//场景宽度
	private int sHeight;//场景高度
	private boolean scSwitch;//场景切换开关
	
	private long timebuffer = 0;
	private int keybuffer = 0;
	private boolean action = true;
	
	static public int money;//金钱
	public int love[];//好感度
	private int pX,pY;//单元格位置
	private int fX,fY;//面对单元 格的位置
	private int aspect;//方向

	private long time1,time2,pressedTime;
	public int year,mouth,day,hour,min;
	private int timeIndex;
	
	private Store store;//仓库
	private int thisPage;
	
	//选项菜单变量
	private int banIndex;//菜单条指针
	private int bagIndex;//包裹格指针
	private int sysIndex;//系统选项指针
	private int shopIndex;//买指针
	private int kitP;
	private int[] kitbuf;//厨房缓存
	private boolean jump;
	private boolean saveImage;
	
	private int tx,ty,tw,th;
	
	private Image all;
	private Image tImage = Image.createImage(176,220);
	private Graphics tGraph = tImage.getGraphics();
	
	private Image title;
	private Image bw;
	private Image loading;
	
	private Image actorPic;
	private Image allMap;
	private Image s1_l1;
	private Image doorPic;
	
	private Image plant[];
	private Image fish[];
	private Image tool[];
	private Image aniPro[];
	private Image cropPic;
	private Image stateBox;
	
	private Image headPic;
	private Image ban;
	private Image bMenu;	
	private Image[] banner;
	private Image box1;
	private Image box2;
	private Image grayMask;
	private Image searchWord;
	private Image searchBox;
	private Image stick;
	private Image[] number;
	private Image[] number2;
	private Image[] word;
	private Image storePic;
	private Image nextButton[];
	private Image lastButton[];
	private Image[] confirm;
	
	//private Sprite sp1;
	private Sprite door;
	private Sprite door2;
	private Sprite cabe;
	private Sprite kit;
	private Sprite window;
	private Sprite window2;
	private Sprite desk;
	private Sprite bed;
	private Sprite[] spDull;
	private Sprite[] spChicken;
	private TiledLayer tl;
	private TiledLayer tl2;
	private TiledLayer cropLayer;
	private TiledLayer cropStateLayer;
	private LayerManager lm;
	
	private int[] banData = {0,71,142};
	private Graphics allGraph;
	
	private Thread instance;
	
	protected mainCanvas(boolean suppressKeyEvents) {
		super(false);
		setFullScreenMode(true);
		intialize();//初始化
	}
	
	private void intialize()
	{
		try{
			all = Image.createImage(178,220);
			actorPic = Image.createImage("/actor.png");
			ban = Image.createImage("/ban.png");
			bMenu = Image.createImage("/menu.png");
			stick = Image.createImage("/stick.png");
			banner = new Image[3];
			banner[0] = Image.createImage("/menu1.png");
			banner[1] = Image.createImage("/menu2.png");
			banner[2] = Image.createImage("/menu3.png");
			
			headPic = Image.createImage("/head.png");
			
			number = new Image[11];
			Image temp = Image.createImage("/number.png");
			number[0] = Image.createImage(temp, 90, 0, 10, 15, 0);
			for (int i=1;i<10;i++)
			{
				number[i] = Image.createImage(temp,(i-1)*10,0,10,15,0);
			}
			number[10] = Image.createImage(temp,100,0,10,15,0);

			number2 = new Image[10];
			temp = Image.createImage("/number2.png");
			for (int i=0;i<10;i++)
			{
				number2[i] = Image.createImage(temp,i*12,0,12,15,0);
			}
			
			word = new Image[2];
			temp = Image.createImage("/wordatom.png");
			for (int i=0;i<2;i++)
			{
				word[i] = Image.createImage(temp, i*20, 0, 20, 20, 0);
			}
			temp = null;
			
			box1 = Image.createImage("/box1.png");
			box2 = Image.createImage("/box2.png");
			
			plant = new Image[26];
			Image plantTemp = Image.createImage("/plant.png");
			
			grayMask = Image.createImage("/mask.png");
			searchWord = Image.createImage("/word.png");
			searchBox = Image.createImage("/searchBox.png");
			
			for (int i = 0; i<5 ;i++)
				for (int j=0; j<5;j++)
				{
					plant[i*5+j+1] = Image.createImage(plantTemp, j*30, i*30, 30, 30, 0);
				}
			plantTemp = null;
			
			fish = new Image[5];
			for (int i=0;i<5;i++)
				fish[i] = Image.createImage("/fish.png");
			
			tool = new Image[5];
			Image toolTemp = Image.createImage("/tool.png");
			
			for (int i = 0;i<actor.TOOLNUM;i++)
			{
					tool[i] = Image.createImage(toolTemp, (i%2)*30, (i/2)*30, 30, 30, 0);
			}
			toolTemp = null;
			
			Image aniTemp = Image.createImage("/anis.png");
			aniPro = new Image[5];
			for (int i = 0;i<5;i++)
			{
					aniPro[i] = Image.createImage(aniTemp, (i%2)*30, (i/2)*30, 30, 30, 0);
			}
			aniTemp = null;
			
			grd = new CGround[GW][GH];
			cropPic = Image.createImage("/crop.png");
			stateBox = Image.createImage("/stateBox.png");
			
			cropLayer = new TiledLayer(GW,GH,cropPic,32,32);
			cropStateLayer = new TiledLayer(GW,GH,stateBox,32,32);
			for (int i=0;i<GW;i++)
				for (int j=0;j<GH;j++)
				{
					grd[i][j] = new CGround(i,j);
				}
			
			cropPic = null;
			stateBox = null;
			
			cropLayer.setPosition((GX-1)*BLOCK_WIDTH, (GY-1)*BLOCK_WIDTH-2);
			cropStateLayer.setPosition((GX-1)*BLOCK_WIDTH, (GY-1)*BLOCK_WIDTH-16);
			
			dull = new CAnimal[3];
			chicken = new CAnimal[4];
			for (int i=0;i<3;i++)
				dull[i] = new CAnimal(i);
			for (int i=0;i<4;i++)
				chicken[i] = new CAnimal(i);
			
		}catch(Exception e){e.printStackTrace(); System.out.println("about");}

		
		actor = new CActor(actorPic,36,49,this);
		actor.iTurn(DOWN);
		actor.setRefPixelPosition(18, 49);
		
		aspect = 1;

		gState = 1;
		scSwitch = true;
		eSwitch = 0;
		key = 0;
		keyPressed = false;
		sIndex = 0;	

		money = 199;
		mouth = 0;
		day = 0;
		store = new Store(32);
		thisPage = 0;
		kitbuf = new int[5];
		
		time1 = 0;
		time2 = 0;
		hour = 8;
		mouth = 1;
		timeIndex = 0;
		running = true;
		jump = false;
		allGraph = all.getGraphics();
		gamefont = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD ,Font.SIZE_SMALL);
		allGraph.setFont(gamefont);
		
		instance = new Thread(this);
		instance.start();
		
		
	}
	
	/**************
	 * 时间控制
	 */
	
	public void skip(int time)
	{
		min = min + time;
		hour = hour + min / 60;
		day = day + hour / 24;
		mouth = mouth + day / 30;
		year = year + mouth / 12;
		min %= 60;
		hour %= 24;
		day %= 30;
		mouth %= 12;
		allGraph.setClip(115, 47+VIEW_HEIGHT+HEAD_HEIGHT, 50, 15);
		allGraph.drawImage(ban, 0, VIEW_HEIGHT+HEAD_HEIGHT, 0);
		allGraph.drawImage(number[hour/10], 115, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
		allGraph.drawImage(number[hour%10], 125, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
		allGraph.drawImage(number[min/10], 145, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
		allGraph.drawImage(number[min%10], 155, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
		allGraph.setClip(0, 0, WIDTH, HEIGHT);
	}
	
	public void nextDay()
	{
		//作物成长
		for (int i = 0;i<GW;i++)							
			for (int j = 0;j<GH;j++)
			{
				if (grd[i][j].gdCrop>=Data.seasonData[(mouth/3)*2] && grd[i][j].gdCrop<=Data.seasonData[(mouth/3)*2+1])
				{
					grd[i][j].NextDay();
				}
			}
		//动物成长
		for (int i=0;i<3;i++)
		{
			dull[i].NextDay();
		}
		for (int i=0;i<4;i++)
		{
			chicken[i].NextDay();
		}
	}
	/**************
	 * 系统控制方法 
	 */
	
	//加载图片
	private void intiPic()
	{	
		
		lm = new LayerManager();
		
		actor.setPosition(actor.iX * BLOCK_WIDTH, actor.iY * BLOCK_WIDTH-actor.getHeight()/2);
		
		sWidth = Data.mapInfo[sIndex][0];
		sHeight = Data.mapInfo[sIndex][1];
		switch (sIndex)
		{
			case 0:{
						
						try {
							int tempColor = allGraph.getColor();
							allGraph.setColor(0x000000);
							loading = Image.createImage("/loading.png");
							
							allGraph.fillRect(0, 0, WIDTH, HEIGHT);
							allGraph.drawImage(loading, 13, 70, 0);
							allGraph.setColor(tempColor);
							repaint();
							
							loading = null;
							allMap = Image.createImage("/all.png");
							
							doorPic = Image.createImage(32,64);
							doorPic.getGraphics().drawImage(allMap, -352, 0, 0);
							
							allMap = null;
							
							s1_l1 = Image.createImage("/s1_l1.png");						
						} catch (IOException e) {e.printStackTrace(); System.out.println("erro");}
						
						tl = new TiledLayer(Data.mapInfo[sIndex][0],Data.mapInfo[sIndex][1],s1_l1,32,32);
						s1_l1 = null;
						for (int i=0;i<Data.mapInfo[sIndex][0];i++)
							for (int j=0;j<Data.mapInfo[sIndex][1];j++)
							{
								tl.setCell(i, j, Math.abs(Data.map0[j][i]));
							}
						door = new Sprite(doorPic);
						door2 = new Sprite(door);
						doorPic = null;
						
						door.setPosition(96, 320);
						door2.setPosition(352, 320);
						
						for (int i=0;i<GW;i++)
							for(int j=0;j<GH;j++)
							{
								if (grd[i][j].gdCrop != 0)
								{
									cropLayer.setCell(i, j, grd[i][j].gdGrowState+1);
									cropStateLayer.setCell(i, j, grd[i][j].gdState+1);
								}else{
									cropLayer.setCell(i, j, 0);
									cropStateLayer.setCell(i, j, 0);
								}
							}
						
						lm.append(actor);
						lm.append(cropStateLayer);
						lm.append(cropLayer);
						lm.append(door);
						lm.append(door2);
						lm.append(tl);
						//TODO for test:	
						fixScreen();
						
						lm.setViewWindow(pX, pY, VIEW_WIDTH, VIEW_HEIGHT);
						lm.paint(allGraph, 0, HEAD_HEIGHT);
						
						headRender();
						banRender();
						
						repaint();
						break;
						
					}
			case 1:{
						try{
							int tempColor = allGraph.getColor();
							allGraph.setColor(0x000000);
							loading = Image.createImage("/loading.png");
							
							allGraph.fillRect(0, 0, WIDTH, HEIGHT);
							allGraph.drawImage(loading, 13, 70, 0);
							allGraph.setColor(tempColor);
							repaint();
							
							loading = null;
							
							Image s2_l1 = Image.createImage("/s2_l1.png");
							Image s2_l2 = Image.createImage("/s2_l2.png");
							tl = new TiledLayer(Data.mapInfo[sIndex][0],Data.mapInfo[sIndex][1],s2_l1,32,32);
							tl2 = new TiledLayer(Data.mapInfo2[sIndex][0], Data.mapInfo2[sIndex][1], s2_l2, 32, 32);
							s2_l1 = null;
							s2_l2 = null;
						}catch (IOException e){e.printStackTrace();}
						
						for (int i=0;i<Data.mapInfo[sIndex][0];i++)
							for (int j=0;j<Data.mapInfo[sIndex][1];j++)
							{
								tl.setCell(i, j, Math.abs(Data.map1[j][i]));
							}

						for (int i=0;i<Data.mapInfo2[sIndex][0];i++)
							for (int j=0;j<Data.mapInfo2[sIndex][1];j++)
							{
								tl2.setCell(i, j, Math.abs(Data.map1_2[j][i]));
							}
						
						lm.append(actor);
						lm.append(tl2);
						lm.append(tl);
						
						fixScreen();
						
						lm.setViewWindow(pX, pY , VIEW_WIDTH, VIEW_HEIGHT);
						lm.paint(allGraph, 0, HEAD_HEIGHT);
						
						headRender();
						banRender();
						
						repaint();
						break;
					}
			case 2:{
						try {
							loading = Image.createImage("/loading.png");
							
							
							allGraph.fillRect(0, 0, WIDTH, HEIGHT);
							allGraph.drawImage(loading, 13, 70, 0);
							repaint();
							
							loading = null;	
							
							s1_l1 = Image.createImage("/s1_l1.png");						
						} catch (IOException e) {e.printStackTrace();}
						
						for (int i=0;i<GW;i++)
							for(int j=0;j<GH;j++)
							{
								cropLayer.setCell(i, j, grd[i][j].gdGrowState+1);
								cropStateLayer.setCell(i, j, grd[i][j].gdState+1);
							}
						
						tl = new TiledLayer(Data.mapInfo[sIndex][0],Data.mapInfo[sIndex][1],s1_l1,32,32);
						s1_l1 = null;
						for (int i=0;i<Data.mapInfo[sIndex][0];i++)
							for (int j=0;j<Data.mapInfo[sIndex][1];j++)
							{
								tl.setCell(i, j, Math.abs(Data.map2[j][i]));
							}
						
						lm.append(actor);
						lm.append(cropStateLayer);
						lm.append(cropLayer);
						lm.append(tl);
						//TODO for test:	
						fixScreen();
						
						lm.setViewWindow(pX, pY, VIEW_WIDTH, VIEW_HEIGHT);
						lm.paint(allGraph, 0, HEAD_HEIGHT);
						
						headRender();
						banRender();
						
						repaint();
						break;
					}
			case 3:{
						Image s3_l1;
						Image s3_l2;
						try {
							int tempColor = allGraph.getColor();
							allGraph.setColor(0x000000);
							loading = Image.createImage("/loading.png");
							
							allGraph.fillRect(0, 0, WIDTH, HEIGHT);
							allGraph.drawImage(loading, 13, 70, 0);
							allGraph.setColor(tempColor);
							repaint();
							
							loading = null;				
							System.gc();
							
							
							s3_l1 = Image.createImage("/s3-l1.png");
							s3_l2 = Image.createImage("/s3-l2.png");
						} catch (IOException e) {e.printStackTrace(); break;}
						
						window = new Sprite(Image.createImage(s3_l2, 32, 32, 32, 32, 0));
						window.setPosition(64, 2);
						window2 = new Sprite(window);
						window2.setPosition(160, 2);
						cabe = new Sprite(Image.createImage(s3_l2, 0, 96, 96, 64, 0));
						cabe.setPosition(0, 16);
						kit = new Sprite(Image.createImage(s3_l2, 0, 160,96,64,0));
						kit.setPosition(96, 32);
						desk = new Sprite(Image.createImage(s3_l2, 64, 32 , 32, 32, 0));
						desk.setPosition(192, 32);
						bed = new Sprite(Image.createImage(s3_l2, 0, 0, 32, 96, 0));
						bed.setPosition(224, 32);
						
						tl = new TiledLayer(Data.mapInfo[sIndex][0],Data.mapInfo[sIndex][1],s3_l1,32,32);
						s3_l1 = null;
						s3_l2 = null;
						for (int i=0; i<Data.mapInfo[sIndex][0];i++)
							for (int j=0; j<Data.mapInfo[sIndex][1];j++)
							{
								tl.setCell(i, j, Math.abs(Data.map3[j][i]));
							}
						
						lm.append(actor);
						lm.append(cabe);
						lm.append(kit);
						lm.append(desk);
						lm.append(bed);
						lm.append(window);
						lm.append(window2);
						lm.append(tl);
						//TODO for test:
						fixScreen();
						lm.setViewWindow(pX, pY, VIEW_WIDTH, VIEW_HEIGHT);	
						lm.paint(allGraph, 0, HEAD_HEIGHT);
						
						headRender();
						banRender();
						
						repaint();
						break;
					}
			case 4:{
						Image s4_l1;
						Image s4_l2;
						try
						{
							int tempColor = allGraph.getColor();
							allGraph.setColor(0x000000);
							loading = Image.createImage("/loading.png");
							
							allGraph.fillRect(0, 0, WIDTH, HEIGHT);
							allGraph.drawImage(loading, 13, 70, 0);
							allGraph.setColor(tempColor);
							repaint();
							
							loading = null;
							
							Image temp = Image.createImage("/animal.png");
							Image dullPic = Image.createImage(temp,0,0,128,64,0);
							Image chickenPic = Image.createImage(temp,128,0,64,64,0);
							spDull = new Sprite[3];
							spChicken = new Sprite[4];
							for (int i=0;i<3;i++)
								spDull[i] = new Sprite(dullPic,64,64);
							for (int i=0;i<4;i++)
								spChicken[i] = new Sprite(chickenPic,64,32);
							temp = null;
							dullPic = null;
							chickenPic = null;
							
							for (int i=0;i<4;i++)
							{
								if (chicken[i].aniKind != 0)
								{
									spChicken[i].setFrame(chicken[i].aniKind - 2);
									spChicken[i].setVisible(true);
								}else 
									spChicken[i].setVisible(false);
							}
							spChicken[0].setPosition(256,40);
							spChicken[1].setPosition(280, 39);
							spChicken[2].setPosition(280, 80);
							spChicken[3].setPosition(270, 66);
							spChicken[1].setTransform(Sprite.TRANS_MIRROR);
							spChicken[2].setTransform(Sprite.TRANS_MIRROR);
							
							for (int i=0;i<3;i++)
							{
								if (dull[i].aniKind != 0)
									spDull[i].setVisible(true);
								else spDull[i].setVisible(false);
							}				
							spDull[0].setFrame(0);
							spDull[1].setFrame(1);
							spDull[2].setFrame(1);
							spDull[0].setPosition(40, 30);
							spDull[1].setPosition(5, 55);
							spDull[2].setPosition(50, 80);
							
							s4_l1 = Image.createImage("/s4-l1.png");
							s4_l2 = Image.createImage("/s4-l2.png");
							
						}catch(IOException e){e.printStackTrace();break;};
						tl = new TiledLayer(Data.mapInfo[sIndex][0], Data.mapInfo[sIndex][1], s4_l1, 32, 32);
						tl2 = new TiledLayer(Data.mapInfo2[sIndex][0], Data.mapInfo2[sIndex][1], s4_l2, 32, 32);
						s4_l1 = null;
						s4_l2 = null;
						
						for (int i=0; i<Data.mapInfo[sIndex][0];i++)
							for (int j=0; j<Data.mapInfo[sIndex][1];j++)
							{
								tl.setCell(i, j, Math.abs(Data.map4[j][i]));
							}
						for (int i=0; i<Data.mapInfo2[sIndex][0];i++)
							for (int j=0; j<Data.mapInfo2[sIndex][1];j++)
							{
								tl2.setCell(i, j, Math.abs(Data.map4_2[j][i]));
							}
						tl2.setPosition(0, 32);
						
						lm.append(actor);
						lm.append(tl2);
						lm.append(spChicken[3]);
						lm.append(spChicken[2]);
						lm.append(spChicken[1]);
						lm.append(spChicken[0]);
						lm.append(spDull[2]);
						lm.append(spDull[1]);
						lm.append(spDull[0]);
						lm.append(tl);
						
						fixScreen();
						lm.setViewWindow(pX, pY, VIEW_WIDTH, VIEW_HEIGHT);	
						lm.paint(allGraph, 0, HEAD_HEIGHT);
						
						headRender();
						banRender();
						
						repaint();
						break;
					}
		}	
		System.gc();
		
	}
	
	//装载菜单图片
	private void intiMenu()
	{
		try {
			bMenu = Image.createImage("menu.png");
			banner[0] = Image.createImage("1-1.png");
			banner[1] = Image.createImage("2-1.png");
			banner[2] = Image.createImage("3-1.png");
		} catch (IOException e) {e.printStackTrace();}
	}
	
	// 卸载图片
	private void unloadPic()
	{
		tl = null;
		tl2 = null;
		lm = null;
		switch (sIndex)
		{
		case 0:	door = null;
				door2 = null;
				break;
		case 1:	break;
		case 2:	break;
		case 3:	cabe = null;
				kit = null;
				desk = null;
				bed = null;
				window = null;
				window2 = null;
				break;
		case 4:	break;
		}
		System.gc();
	}
	//TODO 存档
	private boolean saveData(String name)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		RecordStore rs;
		RecordEnumeration re;
		boolean save = false;
		byte[]  tempbuf;
		try {
			rs = RecordStore.openRecordStore(name, true);
			re = rs.enumerateRecords(null, null, false);
			if (re.numRecords() == 4)
				save = true;
			
			bos.write(gState);
			bos.write(sState);
			bos.write(sIndex);
			bos.write(money);
			bos.write(aspect);
			bos.write(min);
			bos.write(hour);
			bos.write(day);
			bos.write(mouth);
			bos.write(year);
			bos.write(actor.toByte());
			tempbuf = bos.toByteArray();
			if (!save)
			{
				rs.addRecord(tempbuf, 0, tempbuf.length);
			}else{
				rs.setRecord(1, tempbuf, 0, tempbuf.length);
			}
			tempbuf = null;
			bos.reset();
			
			for (int i=0;i<5;i++)
				for (int j=0;j<5;j++)
			{
				bos.write(grd[i][j].toByte());
			}
			tempbuf = bos.toByteArray();
			if (!save)
			{
				rs.addRecord(tempbuf, 0, tempbuf.length);
			}else{
				rs.setRecord(2, tempbuf, 0, tempbuf.length);
			}
			tempbuf = null;
			bos.reset();
			
			for (int i=0;i<3;i++)
			{
				bos.write(dull[i].toByte());
			}
			for (int i=0;i<4;i++)
			{
				bos.write(chicken[i].toByte());
			}
			tempbuf = bos.toByteArray();
			if (!save)
			{
				rs.addRecord(tempbuf, 0, tempbuf.length);
			}else{
				rs.setRecord(3, tempbuf, 0, tempbuf.length);
			}
			tempbuf = null;
			bos.reset();
			
			bos.write(store.toByte());
			tempbuf = bos.toByteArray();
			if (!save)
			{
				rs.addRecord(tempbuf, 0, tempbuf.length);
			}else{
				rs.setRecord(4, tempbuf, 0, tempbuf.length);
			}
			tempbuf = null;
			bos.reset();
			bos.close();
			return true;
		} catch (Exception e) {e.printStackTrace();	return false;}
		
	}
	
	//TODO 读档
	private boolean readData(String name)
	{
		RecordStore rs;
		try {
			rs = RecordStore.openRecordStore(name, false);
			RecordEnumeration re;
			re = rs.enumerateRecords(null, null, false);
			if (re.numRecords() == 0)
				return false;
			byte[] tempbuf;
			ByteArrayInputStream bis;
			
			tempbuf = rs.getRecord(1);
			bis = new ByteArrayInputStream(tempbuf);
			gState = bis.read();
			sState = bis.read();
			sIndex = bis.read();
			money = bis.read();
			aspect = bis.read();
			min = bis.read();
			hour = bis.read();
			day = bis.read();
			mouth = bis.read();
			year = bis.read();
			actor.loadData(bis);
			bis.close();
			tempbuf = null;
			
			tempbuf = rs.getRecord(2);
			bis = new ByteArrayInputStream(tempbuf);
			for (int i=0;i<5;i++)
				for (int j=0;j<5;j++)
			{
				grd[i][j].loadData(bis);
			}
			bis.close();
			tempbuf = null;
			

			tempbuf = rs.getRecord(3);
			bis = new ByteArrayInputStream(tempbuf);
			for (int i=0;i<3;i++)
			{
				dull[i].loadData(bis);
			}
			for (int i=0;i<4;i++)
			{
				chicken[i].loadData(bis);
			}
			bis.close();
			tempbuf = null;

			tempbuf = rs.getRecord(4);
			bis = new ByteArrayInputStream(tempbuf);
			store.loadData(bis);
			bis.close();
			tempbuf = null;
			//bos.write(store.toByte());
		} catch (Exception e) {e.printStackTrace(); return false;}
		return true;
	}
	
	//TODO 退出
	private void quit()
	{
		
	}
	//屏幕定位
	private void fixScreen()
	{
		pX = actor.getX()-70;
		pY = actor.getY()-42;
		if (pX < 0)
			{pX = 0;}
		if (pX >= (sWidth * BLOCK_WIDTH - VIEW_WIDTH))
			{pX = (sWidth * BLOCK_WIDTH - VIEW_WIDTH);}
		if (pY < 0)
			{pY = 0;}
		if (pY >= (sHeight * BLOCK_WIDTH - VIEW_HEIGHT))
			{pY = (sHeight * BLOCK_WIDTH - VIEW_HEIGHT);}
	}
	
	/**************
	 * 一般动作
	 */
	private void shop()
	{
		int action = 1;//买卖开关 1.买 2.卖 3.离开
		boolean shopping = true;
		
		//加载图片
		allGraph.setColor(0x000000);
		allGraph.fillRect(0, 0, WIDTH, HEIGHT);
		allGraph.drawImage(loading, 13, 70, 0);
		repaint();
		
		
		while (shopping)
		{
			key = this.getKeyStates();
			if (key == 0)
			{
				keyPressed = false;
			}else if (!keyPressed)
			{
				keyPressed = true;
				switch (key)
				{
					case UP: {
								action = (action + 2) % 3;
								break;
							 }
					case DOWN:{
								action = (action + 1) % 3;
								break;
							  }
					case CONF:{
								switch (action)
								{
									case 0:{
												gState = 110;
												shopping = false;
												break;
											}
									case 1:{
												gState = 111;
												shopping = false;
												break;
											}
									case 2:{
												shopping = false;
												break;
											}
								}
							  }
				}
			}
			//TODO 绘制画面
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public void say()
	{
		//TODO 说话
	}
	
	//移动
	public void move(int aspect)
	{
		//TOTEST: move to decrease power:
		switch (aspect)
		{
			case UP:
						actor.iTurn(UP);
						for (int i=0;i<8;i++)
						{	
							actor.nextFrame();
							actor.move(0, -4);
							
							fixScreen();
							lm.setViewWindow(pX, pY, 178, VIEW_HEIGHT);
							lm.paint(allGraph, 0, HEAD_HEIGHT);
							repaint();
							try{Thread.sleep(50);}catch(Exception e){};
						}
						actor.setFrame(0);
						actor.iMove(0,-1);
						break;
					
			case DOWN:	
						actor.iTurn(DOWN);
						for (int i=0;i<8;i++)
						{
							actor.nextFrame();
							actor.move(0, 4);
							fixScreen();
							lm.setViewWindow(pX, pY, 178,  VIEW_HEIGHT);
							lm.paint(allGraph,  0, HEAD_HEIGHT);
							repaint();
							try{Thread.sleep(50);}catch(Exception e){};
						}
						actor.setFrame(0);
						actor.iMove(0, 1);
						break;
					  
			case LEFT:	
						actor.iTurn(LEFT);
						for (int i=0;i<8;i++)
						{
							actor.nextFrame();
							actor.move(-4, 0);
							fixScreen();
							lm.setViewWindow(pX, pY, 178,  VIEW_HEIGHT );
							lm.paint(allGraph, 0, HEAD_HEIGHT);
							repaint();
							try{Thread.sleep(50);}catch(Exception e){};
						}
						actor.setFrame(0);
						actor.iMove(-1, 0);
						break;
					  
			case RIGHT:	
						actor.iTurn(RIGHT);
						for (int i=0;i<8;i++)
						{
							actor.nextFrame();
							actor.move(4, 0);
							fixScreen();
							lm.setViewWindow(pX, pY, 178,  VIEW_HEIGHT);
							lm.paint(allGraph, 0, HEAD_HEIGHT);
							repaint();
							try{Thread.sleep(50);}catch(Exception e){};
						}
						actor.setFrame(0);
						actor.iMove(1, 0);
						break;
					   
		}
		powerPlus(-1);
		
	}
	
	void buyAni()
	{
		int index;
		index = 0;
		
		allGraph.setColor(0xcc9900);
		allGraph.fillRect(0, 0, 178, 220);
		allGraph.setColor(0xffcc66);
		allGraph.fillRect(4, 4, 170, 212);
		allGraph.setColor(0xcc9966);
		allGraph.fillRect(20, 50, 138, 120);
		allGraph.setColor(0x000000);
		allGraph.fillRect(23, 53, 132, 114);
		
		while(true)
		{
			key = getKeyStates();
			if (key == 0)
			{
				keyPressed = false;
			}else if (!keyPressed)
			{
				keyPressed = true;
				switch (key)
				{
				case UP:
				case LEFT:	index = (index+2)%3;
							break;
				case RIGHT:
				case DOWN:	index = (index+1)%3;
							break;
				case CONF:	System.out.println(actor.buyAni(index+1));
							gState = 100;
							banRender();
							headRender();
							return;
				}
			}
			for (int i=0;i<3;i++)
			{
				if (i == index)
				{
					allGraph.setColor(0xffffff);
					allGraph.fillRect(23, 53+i*30, 132,	30);
					allGraph.setColor(0x000000);
					allGraph.drawString(Data.aniName[i+1]+"   "+Data.aniPrice[i+1][0],28,56+i*30,0);	
				}else{
					allGraph.setColor(0x000000);
					allGraph.fillRect(23, 53+i*30, 132,	30);
					allGraph.setColor(0xffffff);
					allGraph.drawString(Data.aniName[i+1]+"   "+Data.aniPrice[i+1][0],28,56+i*30,0);	
				}
			}
			
			repaint();
			try{
			Thread.sleep(300);
			}catch(InterruptedException e){e.printStackTrace();};
		}
		
		
	}
	
	void sellAni()
	{
		int index,num,bin;
		int[] buf = new int[7];
		index = 0;
		num = 0;
		bin = 0;
		
		for (int i=0;i<3;i++)
		{
			if (dull[i].aniKind != 0)
				num++;
			buf[i] = dull[i].aniKind;
		}
		for (int i=0;i<4;i++)
		{
			if (chicken[i].aniKind != 0)
				num++;
			buf[i+3] = chicken[i].aniKind;
		}
		if (num == 0)
		{
			infoRender("没有动物可卖");
			repaint();
			gState = GAME;
			banRender();
			headRender();
			return;
		}
		
		allGraph.setColor(0xcc9900);
		allGraph.fillRect(0, 0, 178, 220);
		allGraph.setColor(0xffcc66);
		allGraph.fillRect(4, 4, 170, 212);
		allGraph.setColor(0xcc9966);
		allGraph.fillRect(20, 50, 138, 120);
		allGraph.setColor(0x000000);
		allGraph.fillRect(23, 53, 132, 114);
		allGraph.setClip(23, 53, 132, 114);
		
		while(true)
		{
			key = getKeyStates();
			if (key == 0)
			{
				keyPressed = false;
			}else if (!keyPressed)
			{
				keyPressed = true;
				switch (key)
				{
				case UP:
				case LEFT:	
							index = (index+6)%7;
							break;
				case RIGHT:
				case DOWN:	
							index = (index+1)%7;
							break;
				case CONF:	if (buf[index] != 0)
								actor.saleAni(buf[index],index);
							for (int i=0;i<3;i++)
							{
								if (dull[i].aniKind != 0)
									num++;
								buf[i] = dull[i].aniKind;
							}
							for (int i=0;i<4;i++)
							{
								if (chicken[i].aniKind != 0)
									num++;
								buf[i+3] = chicken[i].aniKind;
							}
							if (num == 0)
							{
								infoRender("没有动物可卖");
								repaint();
								gState = 100;
								banRender();
								headRender();
								return;
							}
							break;
				case SE_B:	gState = 100;
							banRender();
							headRender();
							return;
				}
			}
			if (index>3)
			{
				bin = index - 3;
				for (int i=bin;i<7;i++)
				{
					if (i == index)
					{
						allGraph.setColor(0xffffff);
						allGraph.fillRect(23, 53+(i-bin)*30, 132,	30);
						allGraph.setColor(0x000000);
						allGraph.drawString("位置"+(i+1)+" "+Data.aniName[buf[i]]+"  "+Data.aniPrice[buf[i]][1],28,56+(i-bin)*30,0);	
					}else{
						allGraph.setColor(0x000000);
						allGraph.fillRect(23, 53+(i-bin)*30, 132,	30);
						allGraph.setColor(0xffffff);
						allGraph.drawString("位置"+(i+1)+" "+Data.aniName[buf[i]]+"  "+Data.aniPrice[buf[i]][1],28,56+(i-bin)*30,0);	
					}
				}
			}else{
				for (int i=0;i<4;i++)
				{
					if (i == index)
					{
						allGraph.setColor(0xffffff);
						allGraph.fillRect(23, 53+i*30, 132,	30);
						allGraph.setColor(0x000000);
						allGraph.drawString("位置"+(i+1)+" "+Data.aniName[buf[i]]+"  "+Data.aniPrice[buf[i]][1],28,56+i*30,0);	
					}else{
						allGraph.setColor(0x000000);
						allGraph.fillRect(23, 53+i*30, 132,	30);
						allGraph.setColor(0xffffff);
						allGraph.drawString("位置"+(i+1)+" "+Data.aniName[buf[i]]+"  "+Data.aniPrice[buf[i]][1],28,56+i*30,0);	
					}
				}
			}
			repaint();
			try{
				Thread.sleep(200);
			}catch (InterruptedException e){e.printStackTrace();};
		}
		
	}
	
	
	/**********************
	 * 绘制动作
	 */
	//改变图像alpha值
	private Image alphaTrans(Image srcImg,int alpha)
	{
		Image tempImage;
		int iW = srcImg.getWidth();
		int iH = srcImg.getHeight();
		int[] ARGB = new int[iW*iH];
		srcImg.getRGB(ARGB, 0, iW, 0, 0, iW, iH);
		for (int i=0; i<iW*iH;i++)
		{
			ARGB[i] &= alpha;
		}
		tempImage = Image.createRGBImage(ARGB, iW, iH, true);
		return tempImage;
	}
	
	//体力增减
	private void powerPlus(int a)
	{
		actor.power += a;
		if (actor.power > 0)
		{
			allGraph.setClip(114, 174, 51, 6);
			allGraph.setColor(0x66FF99);
			allGraph.drawImage(ban, 0, VIEW_HEIGHT+HEAD_HEIGHT, 0);
			allGraph.fillRect(114, 174, actor.power, 6);
			allGraph.setClip(0, 0, WIDTH, HEIGHT);
			allGraph.drawImage(tool[actor.tIndex], 14, 31+VIEW_HEIGHT+HEAD_HEIGHT, 0);
			allGraph.setClip(115, 47+VIEW_HEIGHT+HEAD_HEIGHT, 50, 15);
			allGraph.drawImage(ban, 0, VIEW_HEIGHT+HEAD_HEIGHT, 0);
			allGraph.drawImage(number[hour/10], 115, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
			allGraph.drawImage(number[hour%10], 125, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
			if (timeIndex > 39)
			{
				allGraph.drawImage(number[10], 135, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
			}
			allGraph.drawImage(number[min/10], 145, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
			allGraph.drawImage(number[min%10], 155, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
			allGraph.setClip(0, 0, WIDTH, HEIGHT);
			repaint(114, 174, 51, 6);
		}else if(actor.power > -51)
		{
			allGraph.setClip(114, 174, 51, 6);
			allGraph.setColor(0xff3333);
			allGraph.drawImage(ban, 0, VIEW_HEIGHT+HEAD_HEIGHT, 0);
			allGraph.fillRect(114, 174, Math.abs(actor.power), 6);
			allGraph.setClip(0, 0, WIDTH, HEIGHT);
			allGraph.drawImage(tool[actor.tIndex], 14, 31+VIEW_HEIGHT+HEAD_HEIGHT, 0);
			allGraph.setClip(115, 47+VIEW_HEIGHT+HEAD_HEIGHT, 50, 15);
			allGraph.drawImage(ban, 0, VIEW_HEIGHT+HEAD_HEIGHT, 0);
			allGraph.drawImage(number[hour/10], 115, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
			allGraph.drawImage(number[hour%10], 125, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
			if (timeIndex > 39)
			{
				allGraph.drawImage(number[10], 135, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
			}
			allGraph.drawImage(number[min/10], 145, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
			allGraph.drawImage(number[min%10], 155, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
			allGraph.setClip(0, 0, WIDTH, HEIGHT);
			repaint(114, 174, 51, 6);
		}
	}
	
	public void paint(Graphics g) {
		
		super.paint(g);
		g.drawImage(all, 0, 0, 0);
	}

	//顶部栏渲染
	private void headRender()
	{
		allGraph.drawImage(headPic,0,0,0);
		//日期
		allGraph.drawImage(number2[(mouth+2)/10], 2, 7, 0);
		allGraph.drawImage(number2[(mouth+1)%10], 10, 7, 0);
		allGraph.drawImage(word[1], 22, 7, 0);
		allGraph.drawImage(number2[(day+2)/10], 44, 7, 0);
		allGraph.drawImage(number2[(day+1)%9], 52, 7, 0);
		allGraph.drawImage(word[0], 64, 7, 0);
		//金钱
		allGraph.drawImage(number2[money/10000],116,7,0);
		allGraph.drawImage(number2[(money%10000)/1000], 128, 7, 0);
		allGraph.drawImage(number2[(money%1000)/100], 140, 7, 0);
		allGraph.drawImage(number2[(money%100)/10], 152, 7, 0);
		allGraph.drawImage(number2[money%10], 164,7,0);
		
	}
	//状态栏渲染
	private void banRender()
	{
		allGraph.drawImage(ban, 0, VIEW_HEIGHT+HEAD_HEIGHT, 0);
		allGraph.setColor(0x66FF99);
		allGraph.fillRect(114, 174, actor.power, 6);
		allGraph.drawImage(tool[actor.tIndex], 14, 31+VIEW_HEIGHT+HEAD_HEIGHT, 0);
		allGraph.setClip(115, 47+VIEW_HEIGHT+HEAD_HEIGHT, 50, 15);
		allGraph.drawImage(ban, 0, VIEW_HEIGHT+HEAD_HEIGHT, 0);
		allGraph.drawImage(number[hour/10], 115, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
		allGraph.drawImage(number[hour%10], 125, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
		if (timeIndex > 39)
		{
			allGraph.drawImage(number[10], 135, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
		}
		allGraph.drawImage(number[min/10], 145, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
		allGraph.drawImage(number[min%10], 155, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
		allGraph.setClip(0, 0, WIDTH, HEIGHT);
		/*
		allGraph.setColor(0x66FF99);
		allGraph.fillRect(114, 174, actor.power, 6);
		allGraph.drawImage(tool[actor.tIndex], 14, 31+VIEW_HEIGHT, 0);
		allGraph.drawImage(number[hour/10], 115, 47+VIEW_HEIGHT, 0);
		allGraph.drawImage(number[hour%10], 125, 47+VIEW_HEIGHT, 0);
		if (timeIndex > 4)
		{
			allGraph.drawImage(number[10], 135, 47+VIEW_HEIGHT, 0);
		}
		allGraph.drawImage(number[min/10], 145, 47+VIEW_HEIGHT, 0);
		allGraph.drawImage(number[min%10], 155, 47+VIEW_HEIGHT, 0);
		allGraph.setClip(0, 0, WIDTH, HEIGHT);
		 */
		int tempIndex = actor.index;
		while (actor.bag[tempIndex] == 0)
		{
			tempIndex = (tempIndex+1)%(actor.BAGNUM+actor.bagLv);
			if (tempIndex == actor.index)
				return;
		}
		actor.index = tempIndex;
		switch ((actor.bag[actor.index]-1)/8)
		{
			case 0:
			case 1:
			case 2: allGraph.drawImage(plant[actor.bag[actor.index]], 67, 32+VIEW_HEIGHT+HEAD_HEIGHT, 0);
					break;
			case 3: allGraph.drawImage(aniPro[actor.bag[actor.index]-25], 67, 32+VIEW_HEIGHT+HEAD_HEIGHT, 0);
					break;
			case 4: allGraph.drawImage(fish[actor.bag[actor.index] - 33], 67, 32+VIEW_HEIGHT+HEAD_HEIGHT, 0);
					break;
			case 5: 
			case 6:
			case 7: allGraph.drawImage(plant[25], 67, 32+VIEW_HEIGHT+HEAD_HEIGHT, 0);
					break;
			default:break;
		}
		
	}
	void saveScreen(int x,int y,int width,int height)
	{
		saveImage = true;
		tx = x;
		ty = y;
		tw = width;
		th = height;
		
		tGraph.setClip(x, y, width, height);
		tGraph.drawImage(all,0,0,0);
		saveImage = true;
	}
	void reverseScreen()
	{
		int x = allGraph.getClipX();
		int y = allGraph.getClipY();
		int w = allGraph.getClipWidth();
		int h = allGraph.getClipHeight();
		
		allGraph.setClip(tx, ty, tw, th);
		allGraph.drawImage(tImage, 0, 0, 0);
		allGraph.setClip(x, y, w, h);
		
	}
	//物品状态栏渲染
	public void tinfo(int x,int y,int width,int height,int aspect,String s)
	{
		saveScreen(x, y, width, height);
		switch (aspect)
		{
		case 1:for (int i=4;i<height;i+=4)
				{
					allGraph.setColor(0xffffff);
					allGraph.fillRect(x, y, width, i);
					allGraph.setColor(0x000000);
					allGraph.fillRect(x+2, y+2, width-6, i-4);
					repaint(x,y,width,i);
					serviceRepaints();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {e.printStackTrace();}
				}
				allGraph.setColor(0xffffff);
				allGraph.drawString(s, (width-gamefont.stringWidth(s))/2, y+(height-gamefont.charWidth('啊'))/2, 0);
				repaint(x,y,width,height);
				break;
		case 0:for (int i=height;i>=4;i-=4)
				{
					allGraph.setColor(0xffffff);
					allGraph.fillRect(x, y+i, width, height-i);
					allGraph.setColor(0x000000);
					allGraph.fillRect(x+2, y+i+2, width-6, height-i-4);
					repaint(x,y+i,width,height-i);
					serviceRepaints();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {e.printStackTrace();}
				}
				allGraph.setColor(0xffffff);
				allGraph.drawString(s, x+(width-gamefont.stringWidth(s))/2, y+(height-gamefont.charWidth('啊'))/2, 0);
				repaint(x,y,width,height);
				break;
		}
	}
	//信息栏渲染
	public void infoRender(String info)
	{
		int tx = allGraph.getClipX();
		int ty = allGraph.getClipY();
		int tw = allGraph.getClipWidth();
		int th = allGraph.getClipHeight();
		allGraph.setClip(79-gamefont.stringWidth(info)/2, 75, gamefont.stringWidth(info)+20, 10+20*(info.length()/8 +1));
		
		allGraph.setColor(0xffcc00);
		allGraph.fillRoundRect(79-gamefont.stringWidth(info)/2, 75, gamefont.stringWidth(info)+20, 10+20*(info.length()/8 +1), 7, 7);
		allGraph.setColor(0x000000);
		allGraph.fillRoundRect(81-gamefont.stringWidth(info)/2, 77, gamefont.stringWidth(info)+ 16, 6+20*(info.length()/8 +1), 7, 7);
		allGraph.setColor(0xffffff);
		allGraph.drawString(info, 89-gamefont.stringWidth(info)/2, 80, 0);
		repaint();
		allGraph.setClip(tx, ty, tw, th);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	//选择栏
	public boolean confirmRender(String info)
	{
		boolean keyPressedTemp = false;
		int keyIndex = -1;
		allGraph.setColor(0xffcc00);
		allGraph.fillRoundRect(79-gamefont.stringWidth(info)/2, 70, gamefont.stringWidth(info)+20, 8+20*(info.length()/8 + 1), 5, 5);
		allGraph.setColor(0x000000);
		allGraph.fillRoundRect(81-gamefont.stringWidth(info)/2, 72, gamefont.stringWidth(info)+ 16, 4+20*(info.length()/8 + 1), 5, 5);
		allGraph.setColor(0xffffff);
		allGraph.drawString(info, 89-gamefont.stringWidth(info)/2, 75, 0);
		
		allGraph.setColor(0xffcc00);
		allGraph.fillRoundRect(45-gamefont.stringWidth("是")/2, 120, gamefont.stringWidth("是")+12, 25, 5, 5);
		allGraph.setColor(0x000000);
		allGraph.fillRoundRect(47-gamefont.stringWidth("是")/2, 122, gamefont.stringWidth("是")+ 8, 21, 5, 5);
		allGraph.setColor(0xffffff);
		allGraph.drawString("是", 49-gamefont.stringWidth("是")/2, 124, 0);
		
		allGraph.setColor(0xffcc00);
		allGraph.fillRoundRect(109-gamefont.stringWidth("否")/2, 120, gamefont.stringWidth("否")+12, 25, 5, 5);
		allGraph.setColor(0x000000);
		allGraph.fillRoundRect(111-gamefont.stringWidth("否")/2, 122, gamefont.stringWidth("否")+ 8, 21, 5, 5);
		allGraph.setColor(0xffffff);
		allGraph.drawString("否", 113-gamefont.stringWidth("否")/2, 124, 0);
		
		repaint();
		
		while (true)
		{
			key = this.getKeyStates();
			if (key == 0)
			{
				keyPressedTemp = false;
			}
			if (!keyPressedTemp)
			{
				switch(key)
				{
					case RIGHT:
					case LEFT:  
								keyPressedTemp = true;
								keyIndex = (keyIndex+1)%2;
								allGraph.setColor(0xffcc00);
								allGraph.fillRoundRect(45-gamefont.stringWidth("是")/2, 120, gamefont.stringWidth("是")+12, 25, 5, 5);
								if (keyIndex == 0) allGraph.setColor(0xffffff);
								else allGraph.setColor(0x000000);
								allGraph.fillRoundRect(47-gamefont.stringWidth("是")/2, 122, gamefont.stringWidth("是")+ 8, 21, 5, 5);
								if (keyIndex == 0) allGraph.setColor(0x000000);
								else allGraph.setColor(0xffffff);
								allGraph.drawString("是", 49-gamefont.stringWidth("是")/2, 124, 0);
								
								allGraph.setColor(0xffcc00);
								allGraph.fillRoundRect(109-gamefont.stringWidth("否")/2, 120, gamefont.stringWidth("否")+12, 25, 5, 5);
								if (keyIndex == 1) allGraph.setColor(0xffffff);
								else allGraph.setColor(0x000000);
								allGraph.fillRoundRect(111-gamefont.stringWidth("否")/2, 122, gamefont.stringWidth("否")+ 8, 21, 5, 5);
								if (keyIndex == 1) allGraph.setColor(0x000000);
								else allGraph.setColor(0xffffff);
								allGraph.drawString("否", 113-gamefont.stringWidth("否")/2, 124, 0);
								repaint();
								break;
								
					case CONF:  if (keyIndex == 0)
								{ return true;}
								else if (keyIndex == 1){return false;}
								break;
				}
			}
			/*try{
			Thread.sleep(300);}catch (InterruptedException e){e.printStackTrace();};*/
		}
	}
	/**************
	 * 主线程
	 */
	public void run() {
		
		while(running)
		{
			//主线程
			switch (gState)
			{
				//TODO 开始logo
				case 0:break;
				//TODO 开始菜单
				case 1:	{
							boolean run = true;
							int bgIndex = 0;
							if (newin)
							{
								newin = false;
								try{
									title = Image.createImage("/title.png");
									bw = Image.createImage("/bw.png");
								}catch(IOException e){e.printStackTrace(); break;}
								allGraph.setColor(0x000000);
								allGraph.fillRect(0, 0, 178, 220);
								allGraph.drawImage(title, 0, 0, 0);
								repaint();
							}
							allGraph.setClip(30, 149, 122, 30);
							allGraph.drawImage(title, 0, 0, 0);
							allGraph.drawImage(bw, 37, 149, 0);
							repaint();
							while (run)
							{
								key = this.getKeyStates();
								if (key == 0)
								{
									keyPressed = false;
								}else if (!keyPressed)
								{
									keyPressed = true;
									switch (key)
									{
									case LEFT:
									case UP:
											for (int i=0;i<15;i++)
											{
												allGraph.drawImage(title, 0, 0, 0);
												allGraph.drawImage(bw, 37, 149-bgIndex*30+i*2, 0);
												allGraph.drawImage(bw, 37, -31-bgIndex*30+i*2, 0);
												repaint();
												try {
													Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
											}
											bgIndex = (bgIndex+5)%6;
											allGraph.drawImage(title, 0, 0, 0);
											allGraph.drawImage(bw, 37, 149-bgIndex*30, 0);
											repaint();
											break;
									case RIGHT:
									case DOWN:
											for (int i=0;i<15;i++)
											{
												allGraph.drawImage(title, 0, 0, 0);
												allGraph.drawImage(bw, 37, 149-bgIndex*30-i*2, 0);
												allGraph.drawImage(bw, 37, 329-bgIndex*30-i*2, 0);
												repaint();
												try {
													Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
											}
											bgIndex = (bgIndex+1)%6;
											allGraph.drawImage(title, 0, 0, 0);
											allGraph.drawImage(bw, 37, 149-bgIndex*30, 0);
											repaint();
											break;
									case CONF:
											switch (bgIndex)
											{
											case 0:
													run = false;
													gState = 100;
													allGraph.setClip(0, 0, WIDTH, HEIGHT);
													break;
											case 1:
													if (readData("f"))
													{
														run = false;
														gState = 100;
														allGraph.setClip(0, 0, WIDTH, HEIGHT);
													}else{
														infoRender("没有存档");
													}
													break;
											}
									}
								}
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {e.printStackTrace();}
							}
						}
						break;
				//TODO 开始游戏
				case 100:
						{
							if (scSwitch)
							{
								intiPic();
								scSwitch = false;
							}
							
							//每15秒跳动15分钟
							time2 = System.currentTimeMillis();
							if (time2 - time1 >15000)
							{
								skip(15);
								time1 = System.currentTimeMillis();
								
								if (hour == 0)
								{
									
									actor.sleep();
									
								}	
							}
							timeIndex = (timeIndex+1)%80;
							if (timeIndex > 39)
							{
								allGraph.setClip(135, 47+VIEW_HEIGHT+HEAD_HEIGHT, 10, 15);
								allGraph.drawImage(ban, 0, VIEW_HEIGHT+HEAD_HEIGHT, 0);
								allGraph.drawImage(number[10], 135, 47+VIEW_HEIGHT+HEAD_HEIGHT, 0);
								allGraph.setClip(0,0,WIDTH,HEIGHT);
							}else{
								allGraph.setClip(135, 47+VIEW_HEIGHT+HEAD_HEIGHT, 10, 15);
								allGraph.drawImage(ban, 0, VIEW_HEIGHT+HEAD_HEIGHT, 0);
								allGraph.setClip(0,0,WIDTH,HEIGHT);
							}
							if (timeIndex%20 == 0)
							{
								cropStateLayer.move(0, -1);
							}else{cropStateLayer.move(0, 1); }
								
							//获取按键信息
							key = this.getKeyStates();
							
							if (key == 0)
							{
								timebuffer =0;
								keybuffer = 0;
								action = false;
								keyPressed = false;
							}else if (keybuffer == key)
									{
										timebuffer = System.currentTimeMillis() - pressedTime;
										if(timebuffer > 100)
										{
											action = true;
										}else {action = false;}
									}else{
										timebuffer = 0;
										keybuffer = key;
										action = true;
									}
							
							if (action && !keyPressed)
							{						
								pressedTime = System.currentTimeMillis();
								keyPressed = true;
								switch(key)
								{
									case UP:
												{
													keyPressed = false;
													actor.iTurn(UP);
													if (this.aspect !=0)
													{
														this.aspect = 0;
														break;
													}
													if (actor.iY > 0)
													{
														
														fY = actor.iY - 1;
														switch (sIndex)
														{
															case 0:if (Data.map0[fY][actor.iX] >=0)
																		move(UP);
																	break;
															case 1:if (Data.map1[fY][actor.iX] >=0)
																		move(UP);
																	break;
															case 2:if (Data.map2[fY][actor.iX] >=0)
																	move(UP);
																	break;
															case 3:if (Data.map3[fY][actor.iX] >=0)
																	move (UP);
																	break;
															case 4:if (Data.map4[fY][actor.iX] >=0)
																		move (UP);
																	break; 
														}
													}
													
													break;
												}
									case DOWN:
												{
													keyPressed = false;
													actor.iTurn(DOWN);
													if (this.aspect != 1)
													{
														this.aspect = 1;
														break;
													}
													
													if (actor.iY < sHeight-1)
													{
														
														fY = actor.iY + 1;
														switch (sIndex)
														{
															case 0:if (Data.map0[fY][actor.iX] >=0)
																	move(DOWN);
																	break;
															case 1:if (Data.map1[fY][actor.iX] >=0)
																	move(DOWN);
																	break;
															case 2:if (Data.map2[fY][actor.iX] >=0)
																	move(DOWN);
																	break;
															case 3:if (Data.map3[fY][actor.iX] >=0)
																	move(DOWN);
																	break;
															case 4:if (Data.map4[fY][actor.iX] >=0)
																	move(DOWN);
																	break;
														}
													}
													
													break;
												}
									case LEFT:
												{
													keyPressed = false;
													actor.iTurn(LEFT);
													if (this.aspect != 2)
													{
														this.aspect = 2;
														break;
													}
													if (actor.iX > 0)
													{
														
														fX = actor.iX - 1;;
														switch (sIndex)
														{
															case 0:if (Data.map0[actor.iY][fX] >=0)
																	move(LEFT);
																	break;
															case 1:if (Data.map1[actor.iY][fX] >=0)
																	move(LEFT);
																	break;
															case 2:if (Data.map2[actor.iY][fX] >= 0)
																	move(LEFT);
																	break;
															case 3:if (Data.map3[actor.iY][fX] >= 0)
																	move(LEFT);
																	break;
															case 4:if (Data.map4[actor.iY][fX] >= 0)
																	move(LEFT);
																	break;
														}
													}
													
													break;
												}
									case RIGHT: 
												{
													keyPressed = false;
													actor.iTurn(RIGHT);
													if (this.aspect != 3)
													{
														this.aspect = 3;
														break;
													}
													
													if (actor.iX <sWidth-1)
													{
														
														fX = actor.iX + 1;;
													
														switch (sIndex)
														{
															case 0:if (Data.map0[actor.iY][fX] >=0)
																	move(RIGHT);
																	break;
															case 1:if (Data.map1[actor.iY][fX] >=0)
																	move(RIGHT);
																	break;
															case 2:if (Data.map2[actor.iY][fX] >=0)
																	move(RIGHT);
																	break;
															case 3:if (Data.map3[actor.iY][fX] >=0)
																	move(RIGHT);
																	break;
															case 4:if (Data.map4[actor.iY][fX] >=0)
																	move(RIGHT);
																	break;
														}
													}
													
													break;
												}
									case CONF:{
													fX = actor.iX + Data.aspect[aspect][0];
													fY = actor.iY + Data.aspect[aspect][1];
													
													if (fX<0)
														fX = 0;
													if (fX>=Data.mapInfo[sIndex][0])
														fX = Data.mapInfo[sIndex][0] - 1;
													if (fY<0)
														fY = 0;
													if (fY >= Data.mapInfo[sIndex][1])
														fY = Data.mapInfo[sIndex][1] -1;
													
													switch (sIndex)
													{
														case 0: sState = Data.mapData0[fY][fX];
																break;
														case 1: sState = Data.mapData1[fY][fX];
																break;
														case 2: sState = Data.mapData2[fY][fX];
																break;
														case 3: sState = Data.mapData3[fY][fX];
																break;
														case 4: sState = Data.mapData4[fY][fX];
																break;
													}
													
													switch (sState)
													{
														//普通图块
														case 0: break;
														//面对土地
														case MAP_FIELD: {
																	if (grd[fX - GX+1][fY - GY+1].gdCrop != 0)
																	{
																		switch (actor.tIndex)
																		{
																			case HOE: 	if (actor.plough(grd[fX - GX+1][fY - GY+1]))
																						{
																							cropLayer.setCell(fX - GX+1, fY - GY+1, 0);
																							cropStateLayer.setCell(fX - GX+1, fY - GY+1, 0);
																						}
																						break;
																			case HOOK:{
																						
																						if (grd[fX - GX+1][fY - GY+1].gdState == HOOK)
																						{
																							actor.weed(grd[fX - GX+1][fY - GY+1]);
																							//图像层
																							cropStateLayer.setCell(fX-GX+1, fY-GY+1, 0);
																						}
																						
																						break;
																						}
																			case KETTEL:{
																							if (grd[fX - GX+1][fY - GY+1].gdState == KETTEL)
																							{
																								actor.irrigate(grd[fX - GX+1][fY - GY+1]);
																								cropStateLayer.setCell(fX-GX+1, fY-GY+1, 0);
																							}
																						break;
																						}
																			case BIOCIDE:{
																						if (grd[fX - GX+1][fY - GY+1].gdState == BIOCIDE)
																						{
																							actor.killBug(grd[fX - GX+1][fY - GY+1]);
																							//图像层
																							cropStateLayer.setCell(fX-GX+1, fY-GY+1, 0);
																						}
																						
																						break;
																						}
																			default :break;
																		}
																	}else{
																		if (actor.bag[actor.index] > 40 && actor.bag[actor.index] < 65 )
																		{
																			actor.plant(grd[fX - GX+1][fY - GY+1]);	
																			cropLayer.setCell(fX - GX+1, fY - GY+1, grd[fX - GX+1][fY - GY+1].gdGrowState+1);
																			cropStateLayer.setCell(fX - GX+1, fY - GY+1, 0);
																			banRender();
																		}
																	}
																	if (grd[fX - GX+1][fY - GY+1].gdGrowState == (Data.cropData[grd[fX - GX+1][fY - GY+1].gdCrop][1]-1))
																	{
																		if ((actor.iPick(grd[fX - GX+1][fY - GY+1].gdCrop)) == 0)
																		{
																			infoRender("包裹已满 物品将放入仓库中");
																			if (!store.add(grd[fX - GX+1][fY - GY+1].gdCrop))
																			{
																				infoRender("仓库已满  拾取失败");
																				jump = true;
																			}
																			
																		}
																		if (!jump)
																		{
																		grd[fX - GX+1][fY - GY+1].gdGrowState = Data.cropData[grd[fX - GX+1][fY - GY+1].gdCrop][1];
																		cropLayer.setCell(fX - GX+1, fY - GY+1, Data.cropData[grd[fX - GX+1][fY - GY+1].gdCrop][1]+1);
																		cropStateLayer.setCell(fX - GX+1, fY - GY+1, 0);
																		}else{
																			jump = false;
																		}
																		
																	}
																	
																break;
																}
														//面对鸡舍
														case 2: {
																	int chikenNum = 0;
																	for (int i=0;i<chicken.length;i++)
																		chikenNum++;
																	if (chikenNum != 0)
																	{
																		switch (actor.bag[actor.index])
																		{
																			case 48:{
																						actor.medi(2);
																						break;
																					}
																			case 47:{
																						actor.feed(2);
																						break;
																					}	
																		}
																	}
																	break;
																}
														//面对牛羊
														case 3:{
																int chikenNum = 0;
																for (int i=0;i<chicken.length;i++)
																	chikenNum++;
																if (chikenNum != 0)
																{
																	switch (actor.bag[actor.index])
																	{
																		case 48:{
																					actor.medi(1);
																					break;
																				}
																		case 47:{
																					actor.feed(1);
																					break;
																				}	
																	}
																}
															break;
														}
														//面对池塘
														case MAP_POOL: {
																	if (Data.mapData0[fY][fX] == 4)
																	{
																		if (actor.tIndex == 4)
																		{
																			actor.fish();
																		}
																	}else{
																		System.out.println("out");
																	}
																	break;
																}
														//面对床
														case MAP_BED:{
																	allGraph.setClip(0, HEAD_HEIGHT, VIEW_WIDTH, VIEW_HEIGHT);
																	allGraph.setColor(0x000000);
																	allGraph.fillRect(0, HEAD_HEIGHT, VIEW_WIDTH, VIEW_HEIGHT);
																	repaint();
																	actor.sleep();
																	try {
																		Thread.sleep(1000);
																	} catch (InterruptedException e) {e.printStackTrace();}
																	lm.paint(allGraph, 0, HEAD_HEIGHT);
																	headRender();
																	banRender();
																	infoRender("早上好");
																	repaint();
																	break;
																}
														case 11:{
																	if (actor.bag[actor.index]>0 && actor.bag[actor.index]<25)
																	{
																		//送人
																		actor.bag[actor.index] = 0;
																		love[sState] ++;
																	}
																	//面对一般人物
																	switch (sState)
																	{
																		case 10:
																				{
																					//TODO 启动商店
																					break;//面对杂货店员
																				}
																		case 11:{	//TODO 启动学习系统
																					break;//面对图书馆阿姨
																				}
																	}
																	break;
																}
														//面对仓库
														case 20:{
																	gState = 140;
																	break;
																}
														//面对厨房
														case 21:{
																	gState = 150;
																	break;
																}
														//从场景0到场景3
														case 100:{	
																	jump = true;
																	scSwitch = true;
																	unloadPic();
																	sIndex = 3;
																	actor.iX = 3;
																	actor.iY = 5;
																	actor.iTurn(UP);
																	break;
																 }
														//从场景0到场景1
														case 101:{
																	jump = true;
																	scSwitch = true;
																	unloadPic();
																	sIndex = 1;
																	actor.iX = 0;
																	actor.iY = 5;
																	actor.iTurn(RIGHT);
																	break;
																 }
														//从场景0到场景4
														case 102:{
																	jump = true;
																	scSwitch = true;
																	unloadPic();
																	sIndex = 4;
																	actor.iX = 5;
																	actor.iY = 6;
																	actor.iTurn(UP);
																	break;
																 }
														//从场景4到场景0
														case 103:{
																	jump = true;
																	scSwitch = true;
																	unloadPic();
																	sIndex = 0;
																	actor.iX = 11;
																	actor.iY = 12;
																	actor.iTurn(DOWN);
																	break;
																 }
														//从场景1到场景0
														case 104:{
																	jump = true;
																	scSwitch = true;
																	unloadPic();
																	sIndex = 0;
																	actor.iX = 6;
																	actor.iY = 13;
																	actor.iTurn(UP);
																	break;
																 }
														//从场景3到场景0
														case 105:{
																	jump = true;
																	scSwitch = true;
																	unloadPic();
																	sIndex = 0;
																	actor.iX = 3;
																	actor.iY = 12;
																	actor.iTurn(DOWN);
																	break;
																}
														//从场景1到商店
														case 106:{
																	gState = SHOP_CHOICE;
																	break;
																}
														case 107:{
																	gState = ANISHOP_CHOICE;
																	break;
																}
														
													}break;
											  }
									case STAR:{
												actor.tIndex = (actor.tIndex + 1)%actor.TOOLNUM;
												banRender();
												try {
													Thread.sleep(100);
												} catch (InterruptedException e) {e.printStackTrace();}
												break;
											  }
									case SPA: {
												do
												{
													actor.index = (actor.index + 1) % (actor.BAGNUM + actor.bagLv);
												}while (actor.bag[actor.index] == 0);
												banRender();
												try {
													Thread.sleep(100);
												} catch (InterruptedException e) {e.printStackTrace();}
												break;
												}
									/*//nokia s40 
									 case NOKIA_S40_9:{
									 					banIndex = 0;
									  					gState = 200;
									  					break;
									  				  }
									 * */
									//Sony Ericsson
									 case SE_B :{
									 				banIndex = 0;
									 				gState = 200;
									 				break;
									 			}	
									 
									/*//WTK
									 case WTK_9:{
									 				banIndex = 0;
									 				gState = 200;
									 				break;
									 			}
									 */
									default : break;
								}
							}
							if (!jump)
							{
								fixScreen();
								lm.paint(allGraph, 0, HEAD_HEIGHT);
								repaint();
							}else {jump = false;}
							
							break;
						}
						
						
						
						
				//TODO 买菜单
				case SHOP_BUY_MENU:{
							
							int goodsL = Data.shopData[mouth/3][0] ;
							int goods;
							shopIndex = 0;
							boolean run = true;
							saveImage = false;
							String name = "";
							int price = 0;
							
							try {
								storePic = Image.createImage("/halfScreen.png");
							} catch (IOException e) {e.printStackTrace();}
							allGraph.drawImage(storePic, 0, 0, 0);
							allGraph.drawImage(storePic, 0, 110, 0);
							for (int i=0;i<(actor.BAGNUM+actor.bagLv);i++)
							{
								allGraph.drawImage(box1, 5+(i%4)*39, 20 + (i/4)*44, 0);
								if (actor.bag[i] != 0)
								{
									switch ((actor.bag[i]-1)/8)
									{
									case 0:
									case 1:
									case 2: allGraph.drawImage(plant[actor.bag[i]], 7+(i%4)*39, 22 + (i/4)*44, 0);
											break;
									case 3: allGraph.drawImage(aniPro[actor.bag[i]-25], 7+(i%4)*39, 22 + (i/4)*44, 0);
											break;
									case 4: allGraph.drawImage(fish[actor.bag[i]-33], 7+(i%4)*39, 22 + (i/4)*44, 0);
											break;
									case 5:
									case 6:
									case 7: allGraph.drawImage(plant[25], 7+(i%4)*39, 22 + (i/4)*44, 0);
											break;
									}
								}
							}
							for (int i=0;i<Data.shopData[mouth/3][0];i++)
							{
								if (i == shopIndex)
								{
									allGraph.drawImage(box1, 5+(i%4)*39, 130 + (i/4)*44, 0);
								}else{
									allGraph.drawImage(box2, 5+(i%4)*39, 130 + (i/4)*44, 0);
								}
								switch ((Data.shopData[mouth/3][i+1]-1)/8)
								{
								case 0:
								case 1:
								case 2:	allGraph.drawImage(plant[Data.shopData[mouth/3][i+1]], 7+(i%4)*39, 132 + (i/4)*44, 0);
										break;
								case 3:	allGraph.drawImage(aniPro[Data.shopData[mouth/3][i+1]-25], 7+(i%4)*39, 132 + (i/4)*44, 0);
										break;
								}
								
							}
							allGraph.setClip(0, 0, WIDTH, HEIGHT/2);
							allGraph.drawImage(grayMask, 0, 0, 0);	
							allGraph.setClip(0, 0, WIDTH, HEIGHT);
							repaint(0,0,178,220);
							//shopIndex = 0;
							while (run)
							{
								key = this.getKeyStates();
								if (key == 0)
								{
									keyPressed = false;
								}else if (!keyPressed)
								{
									keyPressed = true;
									switch(key)
									{
										case RIGHT: {
														if (saveImage)
															reverseScreen();
														allGraph.drawImage(box2, 5+(shopIndex%4)*39, 130 + (shopIndex/4)*44, 0);
														switch ((Data.shopData[mouth/3][shopIndex+1]-1)/8)
														{
														case 0:
														case 1:
														case 2:	allGraph.drawImage(plant[Data.shopData[mouth/3][shopIndex+1]], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[Data.shopData[mouth/3][shopIndex+1]-25], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																break;
														}
														
														shopIndex = (shopIndex+1) % goodsL;
														
														allGraph.drawImage(box1, 5+(shopIndex%4)*39, 130 + (shopIndex/4)*44, 0);
														switch ((Data.shopData[mouth/3][shopIndex+1]-1)/8)
														{
														case 0:
														case 1:
														case 2:	allGraph.drawImage(plant[Data.shopData[mouth/3][shopIndex+1]], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																name = Data.cropName[Data.shopData[mouth/3][shopIndex+1]];
																price = Data.cropPrice[Data.shopData[mouth/3][shopIndex+1]][1];
																break;
														case 3:	allGraph.drawImage(aniPro[Data.shopData[mouth/3][shopIndex+1]-25], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																
																break;
														}
														repaint(0,110,178,110);
														tinfo(0,0,178,80,1,name+": "+ price);
														break;
													}
										case LEFT:	{
														if (saveImage)
															reverseScreen();
														allGraph.drawImage(box2, 5+(shopIndex%4)*39, 130 + (shopIndex/4)*44, 0);
														switch ((Data.shopData[mouth/3][shopIndex+1]-1)/8)
														{
														case 0:
														case 1:
														case 2:	allGraph.drawImage(plant[Data.shopData[mouth/3][shopIndex+1]], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[Data.shopData[mouth/3][shopIndex+1]-25], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																break;
														}
														
														shopIndex = (shopIndex+goodsL-1) % goodsL;
														
														allGraph.drawImage(box1, 5+(shopIndex%4)*39, 130 + (shopIndex/4)*44, 0);
														switch ((Data.shopData[mouth/3][shopIndex+1]-1)/8)
														{
														case 0:
														case 1:
														case 2:	allGraph.drawImage(plant[Data.shopData[mouth/3][shopIndex+1]], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																name = Data.cropName[Data.shopData[mouth/3][shopIndex+1]];
																price = Data.cropPrice[Data.shopData[mouth/3][shopIndex+1]][1];
																break;
														case 3:	allGraph.drawImage(aniPro[Data.shopData[mouth/3][shopIndex+1]-25], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																break;
														}
														repaint(0,110,178,110);
														tinfo(0,0,178,80,1,name+": "+ price);
														break;
													}
										case UP:	{
														if (saveImage)
															reverseScreen();
														allGraph.drawImage(box2, 5+(shopIndex%4)*39, 130 + (shopIndex/4)*44, 0);
														switch ((Data.shopData[mouth/3][shopIndex+1]-1)/8)
														{
														case 0:
														case 1:
														case 2:	allGraph.drawImage(plant[Data.shopData[mouth/3][shopIndex+1]], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[Data.shopData[mouth/3][shopIndex+1]-25], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																break;
														}
														
														shopIndex = (shopIndex + goodsL - 4) % goodsL;
														
														allGraph.drawImage(box1, 5+(shopIndex%4)*39, 130 + (shopIndex/4)*44, 0);
														switch ((Data.shopData[mouth/3][shopIndex+1]-1)/8)
														{
														case 0:
														case 1:
														case 2:	allGraph.drawImage(plant[Data.shopData[mouth/3][shopIndex+1]], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																name = Data.cropName[Data.shopData[mouth/3][shopIndex+1]];
																price = Data.cropPrice[Data.shopData[mouth/3][shopIndex+1]][1];
																break;
														case 3:	allGraph.drawImage(aniPro[Data.shopData[mouth/3][shopIndex+1]-25], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																break;
														}
														repaint(0,110,178,110);
														tinfo(0,0,178,80,1,name+": "+ price);
														break;
													}
										case DOWN:	{
														if (saveImage)
															reverseScreen();
														allGraph.drawImage(box2, 5+(shopIndex%4)*39, 130 + (shopIndex/4)*44, 0);
														switch ((Data.shopData[mouth/3][shopIndex+1]-1)/8)
														{
														case 0:
														case 1:
														case 2:	allGraph.drawImage(plant[Data.shopData[mouth/3][shopIndex+1]], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																name = Data.cropName[Data.shopData[mouth/3][shopIndex+1]];
																price = Data.cropPrice[Data.shopData[mouth/3][shopIndex+1]][1];
																break;
														case 3:	allGraph.drawImage(aniPro[Data.shopData[mouth/3][shopIndex+1]-25], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																break;
														}
														
														shopIndex = (shopIndex + 4) % goodsL;
														allGraph.drawImage(box1, 5+(shopIndex%4)*39, 130 + (shopIndex/4)*44, 0);
														switch ((Data.shopData[mouth/3][shopIndex+1]-1)/8)
														{
														case 0:
														case 1:
														case 2:	allGraph.drawImage(plant[Data.shopData[mouth/3][shopIndex+1]], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[Data.shopData[mouth/3][shopIndex+1]-25], 7+(shopIndex%4)*39, 132 + (shopIndex/4)*44, 0);
																break;
														}
														repaint(0,110,178,110);
														tinfo(0,0,178,80,1,name+": "+ price);
														break;
													}
										case CONF:	{
														switch (shopIndex-goodsL)
														{
															case 0:{
																		run = false;
																		storePic = null;
																		gState = 100;
																		banRender();
																		headRender();
																		keyPressed = true;
																		break;
																	}
															default :{
																		goods = Data.shopData[mouth/3][shopIndex+1];
																		if (money > Data.cropPrice[goods][0])
																		{
																			if (actor.iPick(goods) == 1)
																			{
																				money -=  Data.cropPrice[goods][0];
																			}else{
																				infoRender("包裹已满");
																			}
																		}else{
																			infoRender("金钱不足");
																		}
																		allGraph.setClip(0, 0, 176, 110);
																		allGraph.drawImage(storePic, 0, 0, 0);
																		for (int i=0;i<(actor.BAGNUM+actor.bagLv);i++)
																		{
																			allGraph.drawImage(box1, 5+(i%4)*39, 20 + (i/4)*44, 0);
																			if (actor.bag[i] != 0)
																			{
																				switch ((actor.bag[i]-1)/8)
																				{
																				case 0:
																				case 1:
																				case 2: allGraph.drawImage(plant[actor.bag[i]], 7+(i%4)*39, 22 + (i/4)*44, 0);
																						break;
																				case 3: allGraph.drawImage(aniPro[actor.bag[i]-25], 7+(i%4)*39, 22 + (i/4)*44, 0);
																						break;
																				case 4: allGraph.drawImage(fish[actor.bag[i]-33], 7+(i%4)*39, 22 + (i/4)*44, 0);
																						break;
																				case 5:
																				case 6:
																				case 7: allGraph.drawImage(plant[25], 7+(i%4)*39, 22 + (i/4)*44, 0);
																						break;
																				}
																			}
																		}
																		allGraph.drawImage(grayMask, 0, 0, 0);
																		allGraph.setClip(0, 0, WIDTH, HEIGHT);
																		repaint(0,0,178,110);
																		break;
																	 }
														}
														break;
													}	
										case SE_B:{
														run = false;
														storePic = null;
														gState = 100;
														banRender();
														headRender();
														keyPressed = true;
														break;
												}
									}
								
								}
			
							}
								break;
						 }
				
				
				//TODO 卖菜单
				case SHOP_SELL_MENU:{
							int res;
							boolean run = true;
							bagIndex = 0;
							saveImage = false;
							String name = "";
							int price = 0;
							
							try {		
								
								storePic = Image.createImage("/halfScreen.png");
								allGraph.drawImage(storePic, 0, 0, 0);
								allGraph.drawImage(storePic, 0, 110, 0);
								for (int i=0;i<Data.shopData[mouth/3][0];i++)
								{
									allGraph.drawImage(box2, 5+(i%4)*39, 130 + (i/4)*44, 0);
									allGraph.drawImage(plant[Data.shopData[mouth/3][i+1]], 7+(i%4)*39, 132 + (i/4)*44, 0);
								}
								
								allGraph.setClip(0, HEIGHT/2, WIDTH, HEIGHT/2);
								allGraph.drawImage(grayMask, 0, 0, 0);	
								allGraph.setClip(0, 0, WIDTH, HEIGHT);
								
								for (int i=0;i<(actor.BAGNUM+actor.bagLv);i++)
								{
									
									if (bagIndex == i)
									{
										allGraph.drawImage(box1, 5+(i%4)*39, 20 + (i/4)*44, 0);
									}else 
									{
										allGraph.drawImage(box2, 5+(i%4)*39, 20 + (i/4)*44, 0);
									}
									if (actor.bag[i] != 0)
									{
										switch ((actor.bag[i]-1)/8)
										{
										case 0:
										case 1:
										case 2: allGraph.drawImage(plant[actor.bag[i]], 7+(i%4)*39, 22 + (i/4)*44, 0);
												break;
										case 3: allGraph.drawImage(aniPro[actor.bag[i]-25], 7+(i%4)*39, 22 + (i/4)*44, 0);
												break;
										case 4: allGraph.drawImage(fish[actor.bag[i]-33], 7+(i%4)*39, 22 + (i/4)*44, 0);
												break;
										case 5:
										case 6:
										case 7: allGraph.drawImage(plant[25],7+(i%4)*39, 22 + (i/4)*44, 0);
												break;
										}
									}
								}
									
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							repaint();
							
							while (run)
							{
								key = this.getKeyStates();
								if (key == 0)
								{
									keyPressed = false;
								}else if (!keyPressed )
								{
									keyPressed = true;
									switch(key)
									{
										case RIGHT: {
														if (saveImage)
															reverseScreen();
														allGraph.drawImage(box2, 5+(bagIndex%4)*39, 20 + (bagIndex/4)*44, 0);
														if (actor.bag[bagIndex] != 0)
														{
															switch ((actor.bag[bagIndex]-1)/8)
															{
															case 0:
															case 1:
															case 2: allGraph.drawImage(plant[actor.bag[bagIndex]], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															case 3: allGraph.drawImage(aniPro[actor.bag[bagIndex]-25], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															case 4: allGraph.drawImage(fish[actor.bag[bagIndex]-33], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															case 5:
															case 6:
															case 7: allGraph.drawImage(plant[25],7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															}
														}
														
														bagIndex = (bagIndex+1)%(actor.BAGNUM+actor.bagLv);
														allGraph.drawImage(box1, 5+(bagIndex%4)*39, 20 + (bagIndex/4)*44, 0);
														if (actor.bag[bagIndex] != 0)
														{
															switch ((actor.bag[bagIndex]-1)/8)
															{
															case 0:
															case 1:
															case 2: allGraph.drawImage(plant[actor.bag[bagIndex]], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.cropName[actor.bag[bagIndex]];
																	price = Data.cropPrice[actor.bag[bagIndex]][1];
																	break;
															case 3: allGraph.drawImage(aniPro[actor.bag[bagIndex]-25], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.proName[actor.bag[bagIndex]-25];
																	price = Data.proPrice[actor.bag[bagIndex]-25];
																	break;
															case 4: allGraph.drawImage(fish[actor.bag[bagIndex]-33], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.fishName[actor.bag[bagIndex]-33];
																	price = Data.fishPrice[actor.bag[bagIndex]-33];
																	break;
															case 5:
															case 6:
															case 7: allGraph.drawImage(plant[25],7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.cropName[actor.bag[bagIndex]-40]+"种子";
																	price = 0;
																	break;
															}
														}else{
															name = "";
															price = -1;
														}
														repaint(0,0,178,110);
														switch (price)
														{
														case -1:reverseScreen();
																repaint(tx,ty,tw,th);
																break;
														case 0:	tinfo(0,140,178,80,0,name+":  不可交易");
																break;
														default:tinfo(0,140,178,80,0,name+":  "+ price);
																break;
														}
														
														break;
													}
													
										case LEFT:	{
														if (saveImage)
															reverseScreen();
														allGraph.drawImage(box2, 5+(bagIndex%4)*39, 20 + (bagIndex/4)*44, 0);
														if (actor.bag[bagIndex] != 0)
														{
															switch ((actor.bag[bagIndex]-1)/8)
															{
															case 0:
															case 1:
															case 2: allGraph.drawImage(plant[actor.bag[bagIndex]], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															case 3: allGraph.drawImage(aniPro[actor.bag[bagIndex]-25], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															case 4: allGraph.drawImage(fish[actor.bag[bagIndex]-33], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															case 5:
															case 6:
															case 7: allGraph.drawImage(plant[25],7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															}
														}
														
														bagIndex = (bagIndex + actor.BAGNUM+actor.bagLv-1)%(actor.BAGNUM+actor.bagLv);
														allGraph.drawImage(box1, 5+(bagIndex%4)*39, 20 + (bagIndex/4)*44, 0);
														if (actor.bag[bagIndex] != 0)
														{
															switch ((actor.bag[bagIndex]-1)/8)
															{
															case 0:
															case 1:
															case 2: allGraph.drawImage(plant[actor.bag[bagIndex]], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.cropName[actor.bag[bagIndex]];
																	price = Data.cropPrice[actor.bag[bagIndex]][1];
																	break;
															case 3: allGraph.drawImage(aniPro[actor.bag[bagIndex]-25], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.proName[actor.bag[bagIndex]-25];
																	price = Data.proPrice[actor.bag[bagIndex]-25];
																	break;
															case 4: allGraph.drawImage(fish[actor.bag[bagIndex]-33], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.fishName[actor.bag[bagIndex]-33];
																	price = Data.fishPrice[actor.bag[bagIndex]-33];
																	break;
															case 5:
															case 6:
															case 7: allGraph.drawImage(plant[25],7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.cropName[actor.bag[bagIndex]-40]+"种子";
																	price = 0;
																	break;
															}
														}else{
															name = "";
															price = -1;
														}
										
														repaint(0,0,178,110);
														switch (price)
														{
														case -1:reverseScreen();
																repaint(tx,ty,tw,th);
																break;
														case 0:	tinfo(0,140,178,80,0,name+":  不可交易");
																break;
														default:tinfo(0,140,178,80,0,name+":  "+ price);
																break;
														}
														
														break;
													}
										case UP:	{
														if (saveImage)
															reverseScreen();
														allGraph.drawImage(box2, 5+(bagIndex%4)*39, 20 + (bagIndex/4)*44, 0);
														if (actor.bag[bagIndex] != 0)
														{
															switch ((actor.bag[bagIndex]-1)/8)
															{
															case 0:
															case 1:
															case 2: allGraph.drawImage(plant[actor.bag[bagIndex]], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															case 3: allGraph.drawImage(aniPro[actor.bag[bagIndex]-25], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															case 4: allGraph.drawImage(fish[actor.bag[bagIndex]-33], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															case 5:
															case 6:
															case 7: allGraph.drawImage(plant[25],7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															}
														}
														
														bagIndex = (bagIndex + actor.BAGNUM+actor.bagLv - 4)%(actor.BAGNUM+actor.bagLv);
														allGraph.drawImage(box1, 5+(bagIndex%4)*39, 20 + (bagIndex/4)*44, 0);
														if (actor.bag[bagIndex] != 0)
														{
															switch ((actor.bag[bagIndex]-1)/8)
															{
															case 0:
															case 1:
															case 2: allGraph.drawImage(plant[actor.bag[bagIndex]], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.cropName[actor.bag[bagIndex]];
																	price = Data.cropPrice[actor.bag[bagIndex]][1];
																	break;
															case 3: allGraph.drawImage(aniPro[actor.bag[bagIndex]-25], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.proName[actor.bag[bagIndex]-25];
																	price = Data.proPrice[actor.bag[bagIndex]-25];
																	break;
															case 4: allGraph.drawImage(fish[actor.bag[bagIndex]-33], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.fishName[actor.bag[bagIndex]-33];
																	price = Data.fishPrice[actor.bag[bagIndex]-33];
																	break;
															case 5:
															case 6:
															case 7: allGraph.drawImage(plant[25],7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.cropName[actor.bag[bagIndex]-40]+"种子";
																	price = 0;
																	break;
															}
														}else{
															name = "";
															price = -1;
														}
														repaint(0,0,178,110);
														switch (price)
														{
														case -1:reverseScreen();
																repaint(tx,ty,tw,th);
																break;
														case 0:	tinfo(0,140,178,80,0,name+":  不可交易");
																break;
														default:tinfo(0,140,178,80,0,name+":  "+ price);
																break;
														}
														
														break;
													}
										case DOWN:	{
														if (saveImage)
															reverseScreen();
														allGraph.drawImage(box2, 5+(bagIndex%4)*39, 20 + (bagIndex/4)*44, 0);
														if (actor.bag[bagIndex] != 0)
														{
															switch ((actor.bag[bagIndex]-1)/8)
															{
															case 0:
															case 1:
															case 2: allGraph.drawImage(plant[actor.bag[bagIndex]], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															case 3: allGraph.drawImage(aniPro[actor.bag[bagIndex]-25], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															case 4: allGraph.drawImage(fish[actor.bag[bagIndex]-33], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															case 5:
															case 6:
															case 7: allGraph.drawImage(plant[25],7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	break;
															}
														}
														
														bagIndex = (bagIndex + 4)%(actor.BAGNUM+actor.bagLv);
														allGraph.drawImage(box1, 5+(bagIndex%4)*39, 20 + (bagIndex/4)*44, 0);
														if (actor.bag[bagIndex] != 0)
														{
															switch ((actor.bag[bagIndex]-1)/8)
															{
															case 0:
															case 1:
															case 2: allGraph.drawImage(plant[actor.bag[bagIndex]], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.cropName[actor.bag[bagIndex]];
																	price = Data.cropPrice[actor.bag[bagIndex]][1];
																	break;
															case 3: allGraph.drawImage(aniPro[actor.bag[bagIndex]-25], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.proName[actor.bag[bagIndex]-25];
																	price = Data.proPrice[actor.bag[bagIndex]-25];
																	break;
															case 4: allGraph.drawImage(fish[actor.bag[bagIndex]-33], 7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.fishName[actor.bag[bagIndex]-33];
																	price = Data.fishPrice[actor.bag[bagIndex]-33];
																	break;
															case 5:
															case 6:
															case 7: allGraph.drawImage(plant[25],7+(bagIndex%4)*39, 22 + (bagIndex/4)*44, 0);
																	name = Data.cropName[actor.bag[bagIndex]-40]+"种子";
																	price = 0;
																	break;
															}
														}else{
															name = "";
															price = -1;
														}
														repaint(0,0,178,110);
														switch (price)
														{
														case -1:reverseScreen();
																repaint(tx,ty,tw,th);
																break;
														case 0:	tinfo(0,140,178,80,0,name+":  不可交易");
																break;
														default:tinfo(0,140,178,80,0,name+":  "+ price);
																break;
														}
														
														break;
													}
										case CONF:	{
														res = actor.bag[bagIndex];
														if (res > 0 && res <32)
														{
															switch ((res-1)/8)
															{
															case 0:
															case 1:
															case 2:	money += Data.cropPrice[res][1];
																	actor.bag[bagIndex] = 0;
																	break;
															case 3: money += Data.proPrice[res-25];
																	actor.bag[bagIndex] = 0;
																	break;
															default :break;
															}
														}else{
															infoRender("不能卖");
															allGraph.drawImage(storePic, 0, 0, 0);
															for (int i=0;i<(actor.BAGNUM+actor.bagLv);i++)
															{
																
																if (bagIndex == i)
																{
																	allGraph.drawImage(box1, 5+(i%4)*39, 20 + (i/4)*44, 0);
																}else 
																{
																	allGraph.drawImage(box2, 5+(i%4)*39, 20 + (i/4)*44, 0);
																}
																if (actor.bag[i] != 0)
																{
																	switch ((actor.bag[i]-1)/8)
																	{
																	case 0:
																	case 1:
																	case 2: allGraph.drawImage(plant[actor.bag[i]], 7+(i%4)*39, 22 + (i/4)*44, 0);
																			break;
																	case 3: allGraph.drawImage(aniPro[actor.bag[i]-25], 7+(i%4)*39, 22 + (i/4)*44, 0);
																			break;
																	case 4: allGraph.drawImage(fish[actor.bag[i]-33], 7+(i%4)*39, 22 + (i/4)*44, 0);
																			break;
																	case 5:
																	case 6:
																	case 7: allGraph.drawImage(plant[25],7+(i%4)*39, 22 + (i/4)*44, 0);
																			break;
																	}
																}
															}
															repaint(0,0,178,110);
														}
														break;
													}	
										case SE_B:{
														run = false;
														storePic = null;
														gState = 100;
														banRender();
														headRender();
														keyPressed = true;
														break;
													}
									}
								}
							}
							break;
						 }
				
				//TODO 商店选项
				case SHOP_CHOICE:
				{
					int keyTemp;
					int keyIndex = -1;
					String info = "请问需要什么服务";
					boolean run = true;
					boolean keyPressedTemp = false;
					allGraph.setColor(0xffcc00);
					allGraph.fillRoundRect(79-gamefont.stringWidth(info)/2, 70, gamefont.stringWidth(info)+20, 8+20*(info.length()/8 + 1), 5, 5);
					allGraph.setColor(0x000000);
					allGraph.fillRoundRect(81-gamefont.stringWidth(info)/2, 72, gamefont.stringWidth(info)+ 16, 4+20*(info.length()/8 + 1), 5, 5);
					allGraph.setColor(0xffffff);
					allGraph.drawString(info, 89-gamefont.stringWidth(info)/2, 75, 0);
					
					allGraph.setColor(0xffcc00);
					allGraph.fillRoundRect(45-gamefont.stringWidth("买")/2, 120, gamefont.stringWidth("买")+12, 25, 5, 5);
					allGraph.setColor(0x000000);
					allGraph.fillRoundRect(47-gamefont.stringWidth("买")/2, 122, gamefont.stringWidth("买")+ 8, 21, 5, 5);
					allGraph.setColor(0xffffff);
					allGraph.drawString("买", 49-gamefont.stringWidth("买")/2, 124, 0);
					
					allGraph.setColor(0xffcc00);
					allGraph.fillRoundRect(109-gamefont.stringWidth("卖")/2, 120, gamefont.stringWidth("卖")+12, 25, 5, 5);
					allGraph.setColor(0x000000);
					allGraph.fillRoundRect(111-gamefont.stringWidth("卖")/2, 122, gamefont.stringWidth("卖")+ 8, 21, 5, 5);
					allGraph.setColor(0xffffff);
					allGraph.drawString("卖", 113-gamefont.stringWidth("卖")/2, 124, 0);
					
					repaint();
					
					while (run)
					{
						keyTemp = this.getKeyStates();
						if (keyTemp == 0)
						{
							keyPressedTemp = false;
						}
						if (!keyPressedTemp)
						{
							switch(keyTemp)
							{
								case RIGHT:
								case LEFT:  
											keyPressedTemp = true;
											keyIndex = (keyIndex+1)%2;
											allGraph.setColor(0xffcc00);
											allGraph.fillRoundRect(45-gamefont.stringWidth("买")/2, 120, gamefont.stringWidth("买")+12, 25, 5, 5);
											if (keyIndex == 0) allGraph.setColor(0xffffff);
											else allGraph.setColor(0x000000);
											allGraph.fillRoundRect(47-gamefont.stringWidth("买")/2, 122, gamefont.stringWidth("买")+ 8, 21, 5, 5);
											if (keyIndex == 0) allGraph.setColor(0x000000);
											else allGraph.setColor(0xffffff);
											allGraph.drawString("买", 49-gamefont.stringWidth("买")/2, 124, 0);
											
											allGraph.setColor(0xffcc00);
											allGraph.fillRoundRect(109-gamefont.stringWidth("卖")/2, 120, gamefont.stringWidth("卖")+12, 25, 5, 5);
											if (keyIndex == 1) allGraph.setColor(0xffffff);
											else allGraph.setColor(0x000000);
											allGraph.fillRoundRect(111-gamefont.stringWidth("卖")/2, 122, gamefont.stringWidth("卖")+ 8, 21, 5, 5);
											if (keyIndex == 1) allGraph.setColor(0x000000);
											else allGraph.setColor(0xffffff);
											allGraph.drawString("卖", 113-gamefont.stringWidth("卖")/2, 124, 0);
											repaint();
											break;
								case CONF:  if (keyIndex == 0)
											{	gState = SHOP_BUY_MENU;
												run = false;
												keyPressed = true;}
											else if (keyIndex == 1)
											{	gState = SHOP_SELL_MENU;
												run = false;
												keyPressed = true;}
											break;
							}
						}
						
					}
					break;
				}
				
				
				//TODO 买动物
				case ANISHOP_BUY:
						buyAni();
						break;
						
						
				//TODO 卖动物
				case ANISHOP_SELL:
						sellAni();
						break;
						
						
				//TODO 买卖动物选项
				case ANISHOP_CHOICE:{
						int keyTemp;
						int keyIndex = -1;
						String info = "请问需要什么服务";
						boolean run = true;
						boolean keyPressedTemp = false;
						allGraph.setColor(0xffcc00);
						allGraph.fillRoundRect(79-gamefont.stringWidth(info)/2, 70, gamefont.stringWidth(info)+20, 8+20*(info.length()/8 + 1), 5, 5);
						allGraph.setColor(0x000000);
						allGraph.fillRoundRect(81-gamefont.stringWidth(info)/2, 72, gamefont.stringWidth(info)+ 16, 4+20*(info.length()/8 + 1), 5, 5);
						allGraph.setColor(0xffffff);
						allGraph.drawString(info, 89-gamefont.stringWidth(info)/2, 75, 0);
						
						allGraph.setColor(0xffcc00);
						allGraph.fillRoundRect(45-gamefont.stringWidth("买")/2, 120, gamefont.stringWidth("买")+12, 25, 5, 5);
						allGraph.setColor(0x000000);
						allGraph.fillRoundRect(47-gamefont.stringWidth("买")/2, 122, gamefont.stringWidth("买")+ 8, 21, 5, 5);
						allGraph.setColor(0xffffff);
						allGraph.drawString("买", 49-gamefont.stringWidth("买")/2, 124, 0);
						
						allGraph.setColor(0xffcc00);
						allGraph.fillRoundRect(109-gamefont.stringWidth("卖")/2, 120, gamefont.stringWidth("卖")+12, 25, 5, 5);
						allGraph.setColor(0x000000);
						allGraph.fillRoundRect(111-gamefont.stringWidth("卖")/2, 122, gamefont.stringWidth("卖")+ 8, 21, 5, 5);
						allGraph.setColor(0xffffff);
						allGraph.drawString("卖", 113-gamefont.stringWidth("卖")/2, 124, 0);
						
						repaint();
						
						while (run)
						{
							keyTemp = this.getKeyStates();
							if (keyTemp == 0)
							{
								keyPressedTemp = false;
							}
							if (!keyPressedTemp)
							{
								switch(keyTemp)
								{
									case RIGHT:
									case LEFT:  
												keyPressedTemp = true;
												keyIndex = (keyIndex+1)%2;
												allGraph.setColor(0xffcc00);
												allGraph.fillRoundRect(45-gamefont.stringWidth("买")/2, 120, gamefont.stringWidth("买")+12, 25, 5, 5);
												if (keyIndex == 0) allGraph.setColor(0xffffff);
												else allGraph.setColor(0x000000);
												allGraph.fillRoundRect(47-gamefont.stringWidth("买")/2, 122, gamefont.stringWidth("买")+ 8, 21, 5, 5);
												if (keyIndex == 0) allGraph.setColor(0x000000);
												else allGraph.setColor(0xffffff);
												allGraph.drawString("买", 49-gamefont.stringWidth("买")/2, 124, 0);
												
												allGraph.setColor(0xffcc00);
												allGraph.fillRoundRect(109-gamefont.stringWidth("卖")/2, 120, gamefont.stringWidth("卖")+12, 25, 5, 5);
												if (keyIndex == 1) allGraph.setColor(0xffffff);
												else allGraph.setColor(0x000000);
												allGraph.fillRoundRect(111-gamefont.stringWidth("卖")/2, 122, gamefont.stringWidth("卖")+ 8, 21, 5, 5);
												if (keyIndex == 1) allGraph.setColor(0x000000);
												else allGraph.setColor(0xffffff);
												allGraph.drawString("卖", 113-gamefont.stringWidth("卖")/2, 124, 0);
												repaint();
												break;
									case CONF:  if (keyIndex == 0)
												{	gState = ANISHOP_BUY;
													run = false;
													keyPressed = true;}
												else if (keyIndex == 1)
												{	gState = ANISHOP_SELL;
													run = false;
													keyPressed = true;}
												break;
								}
							}
						}
						break;
				}
				
				//TODO 仓库
				case 140:{
						int storeIndex = -6;
						boolean run = true;
						
						try {
							storePic = Image.createImage("/halfScreen.png");
							Image temp = Image.createImage("/nextButton.png");
							
							nextButton = new Image[2];
							lastButton = new Image[2];
							nextButton[0] = Image.createImage(temp, 0, 0, 40, 30, 0);
							nextButton[1] = Image.createImage(temp, 40, 0, 40, 30, 0);
							lastButton[0] = Image.createImage(temp, 0, 30, 40, 30, 0);
							lastButton[1] = Image.createImage(temp, 40, 30, 40, 30, 0);
							temp = null;
							
						} catch (IOException e) {
							e.printStackTrace();
							System.exit(100);
						}
							
						thisPage = 0;
						
						allGraph.drawImage(storePic, 0, 0, 0);
						allGraph.drawImage(storePic, 0, 110, 0);
						
						for (int i=0;i<(actor.BAGNUM+actor.bagLv);i++)
						{
							if (i == (storeIndex+actor.BAGNUM+actor.bagLv))
							{
								allGraph.drawImage(box2, 7+(i%4)*39, 20 + (i/4)*44, 0);
							}else{
								allGraph.drawImage(box1, 7+(i%4)*39, 20 + (i/4)*44, 0);
							}
							if (actor.bag[i] != 0)
								switch (actor.bag[i]/8)
								{
								case 0:
								case 1:
								case 2: allGraph.drawImage(plant[actor.bag[i]], 7+(i%4)*39, 22 + (i/4)*44, 0);
										break;
								case 3:	allGraph.drawImage(aniPro[actor.bag[i]-25], 7+(i%4)*39, 22 + (i/4)*44, 0);
										break;
								case 4: allGraph.drawImage(fish[actor.bag[i]-33], 7+(i%4)*39, 22 + (i/4)*44, 0);
										break;
								case 5:
								case 6:
								case 7: allGraph.drawImage(plant[25], 7+(i%4)*39, 22 + (i/4)*44, 0);
										break;
								}
						}
						
						for (int i=0;i<8;i++)
						{
							if (storeIndex == i)
							{
								allGraph.drawImage(box2, 5+(i%4)*39, 115 + (i/4)*36, 0);
							}else{
								allGraph.drawImage(box1, 5+(i%4)*39, 115 + (i/4)*36, 0);
							}
							
							if (store.getGoods(i+thisPage*8) != 0)
							{
								switch (store.getGoods(i+thisPage*8)/8)
								{
								case 0:
								case 1:
								case 2: allGraph.drawImage(plant[store.getGoods(i+thisPage*8)], 7+(i%4)*39, 117 + (i/4)*36, 0);
										break;
								case 3:	allGraph.drawImage(aniPro[store.getGoods(i+thisPage*8)-25], 7+(i%4)*39, 117 + (i/4)*36, 0);
										break;
								case 4: allGraph.drawImage(fish[store.getGoods(i+thisPage*8)-33], 7+(i%4)*39, 117 + (i/4)*36, 0);
										break;
								case 5:
								case 6:
								case 7: allGraph.drawImage(plant[25], 7+(i%4)*39, 117 + (i/4)*36, 0);
										break;
								}
							}
						}
						
						
						if (storeIndex == 8)
						{
							allGraph.drawImage(lastButton[0], 10,185,0);
						}else{
							allGraph.drawImage(lastButton[1], 10,185,0);
						}
						
						if(storeIndex == 9)
						{
							allGraph.drawImage(nextButton[0], 80,185,0);
						}else{
							allGraph.drawImage(nextButton[1], 80,185,0);
						}
						
						repaint(0,0,WIDTH,HEIGHT);
						
						while (run)
						{
							key = this.getKeyStates();
							if (key == 0)
							{
								keyPressed = false;
							}else if (!keyPressed)
							{
								keyPressed = true;
								switch (key)
								{
									case UP:
											if (storeIndex < 0)
											{
												allGraph.drawImage(box1, 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 20 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
												if (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)] != 0)
													switch (actor.bag[(bagIndex+actor.BAGNUM+actor.bagLv)]/8)
													{
													case 0:
													case 1:
													case 2: allGraph.drawImage(plant[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((bagIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													case 3:	allGraph.drawImage(aniPro[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((bagIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													case 4: allGraph.drawImage(fish[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-33], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((bagIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													case 5:
													case 6:
													case 7: allGraph.drawImage(plant[25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													}
											}else if(storeIndex  <8)
											{
												allGraph.drawImage(box1, 5+(storeIndex%4)*39, 115 + (storeIndex/4)*36, 0);
												if (store.getGoods(storeIndex+thisPage*8) != 0)
												{
													switch (store.getGoods(storeIndex+thisPage*8)/8)
													{
													case 0:
													case 1:
													case 2: allGraph.drawImage(plant[store.getGoods(storeIndex+thisPage*8)], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													case 3:	allGraph.drawImage(aniPro[store.getGoods(storeIndex+thisPage*8)-25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													case 4: allGraph.drawImage(fish[store.getGoods(storeIndex+thisPage*8)-33], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													case 5:
													case 6:
													case 7: allGraph.drawImage(plant[25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													}
												}
											}else{
												if (storeIndex == 8)
													allGraph.drawImage(lastButton[1], 10,185,0);
												else allGraph.drawImage(nextButton[1], 80,185,0);
											}
											
											storeIndex -= 4;
											if (storeIndex<-actor.BAGNUM-actor.bagLv)
												storeIndex += actor.BAGNUM+actor.bagLv+9;
											
											if (storeIndex < 0)
											{
												allGraph.drawImage(box2, 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 20 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
												if (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)] != 0)
													switch (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]/8)
													{
													case 0:
													case 1:
													case 2: allGraph.drawImage(plant[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													case 3:	allGraph.drawImage(aniPro[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													case 4: allGraph.drawImage(fish[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-33], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													case 5:
													case 6:
													case 7: allGraph.drawImage(plant[25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													}
											}else if(storeIndex  <8)
											{
												allGraph.drawImage(box2, 5+(storeIndex%4)*39, 115 + (storeIndex/4)*36, 0);
												if (store.getGoods(storeIndex+thisPage*8) != 0)
												{
													switch (store.getGoods(storeIndex+thisPage*8)/8)
													{
													case 0:
													case 1:
													case 2: allGraph.drawImage(plant[store.getGoods(storeIndex+thisPage*8)], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													case 3:	allGraph.drawImage(aniPro[store.getGoods(storeIndex+thisPage*8)-25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													case 4: allGraph.drawImage(fish[store.getGoods(storeIndex+thisPage*8)-33], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													case 5:
													case 6:
													case 7: allGraph.drawImage(plant[25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													}
												}
											}else{
												if (storeIndex == 8)
													allGraph.drawImage(lastButton[0], 10,185,0);
												else allGraph.drawImage(nextButton[0], 80,185,0);
											}
											repaint();
											 break;
									case RIGHT:
											if (storeIndex < 0)
											{
												allGraph.drawImage(box1, 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 20 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
												if (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)] != 0)
													switch (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]/8)
													{
													case 0:
													case 1:
													case 2: allGraph.drawImage(plant[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													case 3:	allGraph.drawImage(aniPro[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													case 4: allGraph.drawImage(fish[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-33], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													case 5:
													case 6:
													case 7: allGraph.drawImage(plant[25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													}
											}else if(storeIndex  <8)
											{
												allGraph.drawImage(box1, 5+(storeIndex%4)*39, 115 + (storeIndex/4)*36, 0);
												if (store.getGoods(storeIndex+thisPage*8) != 0)
												{
													switch (store.getGoods(storeIndex+thisPage*8)/8)
													{
													case 0:
													case 1:
													case 2: allGraph.drawImage(plant[store.getGoods(storeIndex+thisPage*8)], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													case 3:	allGraph.drawImage(aniPro[store.getGoods(storeIndex+thisPage*8)-25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													case 4: allGraph.drawImage(fish[store.getGoods(storeIndex+thisPage*8)-33], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													case 5:
													case 6:
													case 7: allGraph.drawImage(plant[25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													}
												}
											}else{
												if (storeIndex == 8)
													allGraph.drawImage(lastButton[1], 10,185,0);
												else allGraph.drawImage(nextButton[1], 80,185,0);
											}
											
											storeIndex++;
											if (storeIndex>9)
												storeIndex = -6;
											
											if (storeIndex < 0)
											{
												allGraph.drawImage(box2, 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 20 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
												if (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)] != 0)
													switch (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]/8)
													{
													case 0:
													case 1:
													case 2: allGraph.drawImage(plant[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													case 3:	allGraph.drawImage(aniPro[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													case 4: allGraph.drawImage(fish[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-33], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													case 5:
													case 6:
													case 7: allGraph.drawImage(plant[25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
															break;
													}
											}else if(storeIndex  <8)
											{
												allGraph.drawImage(box2, 5+(storeIndex%4)*39, 115 + (storeIndex/4)*36, 0);
												if (store.getGoods(storeIndex+thisPage*8) != 0)
												{
													switch (store.getGoods(storeIndex+thisPage*8)/8)
													{
													case 0:
													case 1:
													case 2: allGraph.drawImage(plant[store.getGoods(storeIndex+thisPage*8)], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													case 3:	allGraph.drawImage(aniPro[store.getGoods(storeIndex+thisPage*8)-25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													case 4: allGraph.drawImage(fish[store.getGoods(storeIndex+thisPage*8)-33], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													case 5:
													case 6:
													case 7: allGraph.drawImage(plant[25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
															break;
													}
												}
											}else{
												if (storeIndex == 8)
													allGraph.drawImage(lastButton[0], 10,185,0);
												else allGraph.drawImage(nextButton[0], 80,185,0);
											}
											repaint();
											 break;
									case DOWN:
												if (storeIndex < 0)
												{
													allGraph.drawImage(box1, 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 20 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
													if (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)] != 0)
														switch (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]/8)
														{
														case 0:
														case 1:
														case 2: allGraph.drawImage(plant[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														case 4: allGraph.drawImage(fish[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-33], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														case 5:
														case 6:
														case 7: allGraph.drawImage(plant[25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														}
												}else if(storeIndex  <8)
												{
													allGraph.drawImage(box1, 5+(storeIndex%4)*39, 115 + (storeIndex/4)*36, 0);
													if (store.getGoods(storeIndex+thisPage*8) != 0)
													{
														switch (store.getGoods(storeIndex+thisPage*8)/8)
														{
														case 0:
														case 1:
														case 2: allGraph.drawImage(plant[store.getGoods(storeIndex+thisPage*8)], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[store.getGoods(storeIndex+thisPage*8)-25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														case 4: allGraph.drawImage(fish[store.getGoods(storeIndex+thisPage*8)-33], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														case 5:
														case 6:
														case 7: allGraph.drawImage(plant[25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														}
													}
												}else{
													if (storeIndex == 8)
														allGraph.drawImage(lastButton[1], 10,185,0);
													else allGraph.drawImage(nextButton[1], 80,185,0);
												}
												
												storeIndex += 4;
												if (storeIndex>9)
													storeIndex -= actor.BAGNUM+actor.bagLv+9;
												
												if (storeIndex < 0)
												{
													allGraph.drawImage(box2, 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 20 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
													if (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)] != 0)
														switch (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]/8)
														{
														case 0:
														case 1:
														case 2: allGraph.drawImage(plant[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														case 4: allGraph.drawImage(fish[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-33], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														case 5:
														case 6:
														case 7: allGraph.drawImage(plant[25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														}
												}else if(storeIndex  <8)
												{
													allGraph.drawImage(box2, 5+(storeIndex%4)*39, 115 + (storeIndex/4)*36, 0);
													if (store.getGoods(storeIndex+thisPage*8) != 0)
													{
														switch (store.getGoods(storeIndex+thisPage*8)/8)
														{
														case 0:
														case 1:
														case 2: allGraph.drawImage(plant[store.getGoods(storeIndex+thisPage*8)], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[store.getGoods(storeIndex+thisPage*8)-25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														case 4: allGraph.drawImage(fish[store.getGoods(storeIndex+thisPage*8)-33], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														case 5:
														case 6:
														case 7: allGraph.drawImage(plant[25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														}
													}
												}else{
													if (storeIndex == 8)
														allGraph.drawImage(lastButton[0], 10,185,0);
													else allGraph.drawImage(nextButton[0], 80,185,0);
												}
												repaint();
													break;
									case LEFT:
												if (storeIndex < 0)
												{
													allGraph.drawImage(box1, 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 20 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
													if (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)] != 0)
														switch (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]/8)
														{
														case 0:
														case 1:
														case 2: allGraph.drawImage(plant[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														case 4: allGraph.drawImage(fish[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-33], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														case 5:
														case 6:
														case 7: allGraph.drawImage(plant[25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														}
												}else if(storeIndex  <8)
												{
													allGraph.drawImage(box1, 5+(storeIndex%4)*39, 115 + (storeIndex/4)*36, 0);
													if (store.getGoods(storeIndex+thisPage*8) != 0)
													{
														switch (store.getGoods(storeIndex+thisPage*8)/8)
														{
														case 0:
														case 1:
														case 2: allGraph.drawImage(plant[store.getGoods(storeIndex+thisPage*8)], 7+(storeIndex%4)*39, 117 + (bagIndex/4)*36, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[store.getGoods(storeIndex+thisPage*8)-25], 7+(storeIndex%4)*39, 117 + (bagIndex/4)*36, 0);
																break;
														case 4: allGraph.drawImage(fish[store.getGoods(storeIndex+thisPage*8)-33], 7+(storeIndex%4)*39, 117 + (bagIndex/4)*36, 0);
																break;
														case 5:
														case 6:
														case 7: allGraph.drawImage(plant[25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														}
													}
												}else{
													if (storeIndex == 8)
														allGraph.drawImage(lastButton[1], 80,185,0);
													else allGraph.drawImage(nextButton[1], 10,185,0);
												}
												
												storeIndex--;
												if (storeIndex<-actor.bagLv-actor.BAGNUM)
													storeIndex = 9;
												
												if (storeIndex < 0)
												{
													allGraph.drawImage(box2, 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 20 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
													if (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)] != 0)
														switch (actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]/8)
														{
														case 0:
														case 1:
														case 2: allGraph.drawImage(plant[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														case 4: allGraph.drawImage(fish[actor.bag[(storeIndex+actor.BAGNUM+actor.bagLv)]-33], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														case 5:
														case 6:
														case 7: allGraph.drawImage(plant[25], 7+((storeIndex+actor.BAGNUM+actor.bagLv)%4)*39, 22 + ((storeIndex+actor.BAGNUM+actor.bagLv)/4)*44, 0);
																break;
														}
												}else if(storeIndex  <8)
												{
													allGraph.drawImage(box2, 5+(storeIndex%4)*39, 115 + (storeIndex/4)*36, 0);
													if (store.getGoods(storeIndex+thisPage*8) != 0)
													{
														switch (store.getGoods(storeIndex+thisPage*8)/8)
														{
														case 0:
														case 1:
														case 2: allGraph.drawImage(plant[store.getGoods(storeIndex+thisPage*8)], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[store.getGoods(storeIndex+thisPage*8)-25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														case 4: allGraph.drawImage(fish[store.getGoods(storeIndex+thisPage*8)-33], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														case 5:
														case 6:
														case 7: allGraph.drawImage(plant[25], 7+(storeIndex%4)*39, 117 + (storeIndex/4)*36, 0);
																break;
														}
													}
												}else{
													if (storeIndex == 8)
														allGraph.drawImage(lastButton[0], 10,185,0);
													else allGraph.drawImage(nextButton[0], 80,185,0);
												}
												repaint();
												break;
									case CONF:{
												if (storeIndex >= -(actor.bagLv+actor.BAGNUM) && storeIndex<0)
												{
													if (actor.bag[storeIndex+actor.bagLv+actor.BAGNUM] != 0)
													{
														if(store.add(actor.bag[storeIndex+actor.bagLv+actor.BAGNUM]))
														{
															actor.bag[storeIndex+actor.bagLv+actor.BAGNUM] = 0;
														}//else 仓库已满
													}
												}else if (storeIndex < 8){
													if (store.getGoods(storeIndex + thisPage*8) != 0)
													{
														
														if (actor.iPick(store.getGoods(storeIndex + thisPage*8)) == 1)
														{
															store.remove(store.getGoods(storeIndex + thisPage*8),1);
														
														}//else 背包已满
													}
												}else{
													if (storeIndex == 8)
														thisPage = (thisPage+3)%4;
													else thisPage = (thisPage+1)%4;
												}
											 
												for (int i=0;i<(actor.BAGNUM+actor.bagLv);i++)
												{
													if (i == (storeIndex+actor.BAGNUM+actor.bagLv))
													{
														allGraph.drawImage(box2, 7+(i%4)*39, 20 + (i/4)*44, 0);
													}else{
														allGraph.drawImage(box1, 7+(i%4)*39, 20 + (i/4)*44, 0);
													}
													if (actor.bag[i] != 0)
														switch (actor.bag[i]/8)
														{
														case 0:
														case 1:
														case 2: allGraph.drawImage(plant[actor.bag[i]], 7+(i%4)*39, 22 + (i/4)*44, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[actor.bag[i]-25], 7+(i%4)*39, 22 + (i/4)*44, 0);
																break;
														case 4: allGraph.drawImage(fish[actor.bag[i]-33], 7+(i%4)*39, 22 + (i/4)*44, 0);
																break;
														case 5:
														case 6:
														case 7: allGraph.drawImage(plant[25], 7+(i%4)*39, 22 + (i/4)*44, 0);
																break;
														}
												}
												
												for (int i=0;i<8;i++)
												{
													if (storeIndex == i)
													{
														allGraph.drawImage(box2, 5+(i%4)*39, 115 + (i/4)*36, 0);
													}else{
														allGraph.drawImage(box1, 5+(i%4)*39, 115 + (i/4)*36, 0);
													}
													
													if (store.getGoods(i+thisPage*8) != 0)
													{
														switch (store.getGoods(i+thisPage*8)/8)
														{
														case 0:
														case 1:
														case 2: allGraph.drawImage(plant[store.getGoods(i+thisPage*8)], 7+(i%4)*39, 117 + (i/4)*36, 0);
																break;
														case 3:	allGraph.drawImage(aniPro[store.getGoods(i+thisPage*8)-25], 7+(i%4)*39, 117 + (i/4)*36, 0);
																break;
														case 4: allGraph.drawImage(fish[store.getGoods(i+thisPage*8)-33], 7+(i%4)*39, 117 + (i/4)*36, 0);
																break;
														case 5:
														case 6:
														case 7: allGraph.drawImage(plant[25], 7+(i%4)*39, 117 + (i/4)*36, 0);
																break;
														}
													}
												}
												
												
												if (storeIndex == 8)
												{
													allGraph.drawImage(lastButton[0], 10,185,0);
												}else{
													allGraph.drawImage(lastButton[1], 10,185,0);
												}
												
												if(storeIndex == 9)
												{
													allGraph.drawImage(nextButton[0], 80,185,0);
												}else{
													allGraph.drawImage(nextButton[1], 80,185,0);
												}
												repaint();
												break;
											}
									case SE_B:{
													run = false;
													storePic = null;
													gState = 100;
													banRender();
													headRender();
													keyPressed = true;
											  }
											 	break;
								 	}
								}
							}
						break;
						}
							
				
				
				//TODO 厨房菜单
				case 150:{	
							int kitIndex;
							boolean run = true;
							
							kitIndex = 0;
							kitP = 0;
							for (int i=0;i<5;i++)
								kitbuf[i] = 0;
							try {
								confirm = new Image[2];
								Image temp = Image.createImage("/confirm2.png");
								confirm[0] = Image.createImage(temp, 0, 0, 50, 25, 0);
								confirm[1] = Image.createImage(temp, 0, 25, 50, 25, 0);
								temp = null;
								storePic = Image.createImage("/halfScreen.png");
								allGraph.drawImage(storePic, 0, 0, 0);
								allGraph.drawImage(storePic, 0, 110, 0);
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							allGraph.setColor(0x000000);
							if (kitIndex!=-5)
								allGraph.drawImage(box2, 15, 20, 0);
							else
								allGraph.drawImage(box1, 15, 20, 0);
							if (kitbuf[0] !=0)
								switch (kitbuf[0]/8)
								{
								case 0:
								case 1:
								case 2:	allGraph.drawImage(plant[kitbuf[0]], 17, 22, 0);
										break;
								case 3:	allGraph.drawImage(aniPro[kitbuf[0]-25], 17, 22, 0);
										break;
								}
								
							allGraph.drawString("+", 53, 30, 0);
							
							if (kitIndex!=-4)
								allGraph.drawImage(box2, 65, 20, 0);
							else 
								allGraph.drawImage(box1, 65, 20, 0);
							if (kitbuf[1] !=0)
								switch (kitbuf[0]/8)
								{
								case 0:
								case 1:
								case 2:	allGraph.drawImage(plant[kitbuf[1]], 67, 22, 0);
										break;
								case 3:	allGraph.drawImage(aniPro[kitbuf[1]-25], 67, 22, 0);
										break;
								}
							allGraph.drawString("+", 103, 30, 0);
							
							if (kitIndex!=-3)
								allGraph.drawImage(box2, 115, 20, 0);
							else
								allGraph.drawImage(box1, 115, 20, 0);
							if (kitbuf[2] !=0)
								switch (kitbuf[0]/8)
								{
								case 0:
								case 1:
								case 2:	allGraph.drawImage(plant[kitbuf[2]], 117, 22, 0);
										break;
								case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 117, 22, 0);
											break;
								}
							allGraph.drawString("+", 15, 74, 0);
							
							if (kitIndex!=-2)
								allGraph.drawImage(box2, 30, 64, 0);
							else 
								allGraph.drawImage(box1, 30, 64, 0);
							
							if (kitIndex != -1)
								allGraph.drawImage(confirm[1], 120, 75, 0);
							else
								allGraph.drawImage(confirm[0], 120, 75, 0);
							
							if (kitbuf[3] !=0)
								switch (kitbuf[0]/8)
								{
								case 0:
								case 1:
								case 2:	allGraph.drawImage(plant[kitbuf[3]], 32, 66, 0);
										break;
								case 3:	allGraph.drawImage(aniPro[kitbuf[3]-25], 32, 66, 0);
										break;
								}
							allGraph.drawString("=", 70, 74, 0);
							allGraph.drawImage(box2,84,64,0);
							
							for (int i=0;i<(actor.BAGNUM+actor.bagLv);i++)
							{
								
								if (bagIndex == i)
								{
									allGraph.drawImage(box1, 10+(i%4)*39, 130 + (i/4)*44, 0);
								}else 
								{
									allGraph.drawImage(box2, 10+(i%4)*39, 130 + (i/4)*44, 0);
								}
								if (actor.bag[i] != 0)
								{
									switch ((actor.bag[i]-1)/8)
									{
									case 0:
									case 1:
									case 2: allGraph.drawImage(plant[actor.bag[i]], 12+(i%4)*39, 132 + (i/4)*44, 0);
											break;
									case 3: allGraph.drawImage(aniPro[actor.bag[i]-25], 12+(i%4)*39, 132 + (i/4)*44, 0);
											break;
									case 4: allGraph.drawImage(fish[actor.bag[i]-33], 12+(i%4)*39, 132 + (i/4)*44, 0);
											break;
									case 5:
									case 6:
									case 7: allGraph.drawImage(plant[25],12+(i%4)*39, 132 + (i/4)*44, 0);
											break;
									}
								}
							}
							repaint();
							
							while (run)
							{
								key = this.getKeyStates();
								if (key == 0)
								{
									keyPressed = false;
								}else if (!keyPressed)
								{
									keyPressed = true;
									switch (key)
									{
									case LEFT:
									case UP: if (kitIndex >= 0)
											{
												allGraph.drawImage(box2, 10+(kitIndex%4)*39, 130 + (kitIndex/4)*44, 0);
												if (actor.bag[kitIndex] != 0)
												{
													switch ((actor.bag[kitIndex]-1)/8)
													{
													case 0:
													case 1:
													case 2: allGraph.drawImage(plant[actor.bag[kitIndex]], 12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
															break;
													case 3: allGraph.drawImage(aniPro[actor.bag[kitIndex]-25], 12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
															break;
													case 4: allGraph.drawImage(fish[actor.bag[kitIndex]-33], 12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
															break;
													case 5:
													case 6:
													case 7: allGraph.drawImage(plant[25],12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
															break;
													}
												}
											}
											else{
												switch(kitIndex)
												{
												case -1:
														allGraph.drawImage(confirm[1], 120, 75, 0);
														break;
												case -2:allGraph.drawImage(box2, 30, 64, 0);
														if (kitbuf[3] !=0)
															switch (kitbuf[3]/8)
															{
															case 0:
															case 1:
															case 2:	allGraph.drawImage(plant[kitbuf[3]], 32, 66, 0);
																	break;
															case 3:	allGraph.drawImage(aniPro[kitbuf[3]-25], 32, 66, 0);
																	break;
															}
														break;
												case -3:allGraph.drawImage(box2, 115, 20, 0);
														if (kitbuf[2] !=0)
															switch (kitbuf[2]/8)
															{
															case 0:
															case 1:
															case 2:	allGraph.drawImage(plant[kitbuf[2]], 117, 22, 0);
																	break;
															case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 117, 22, 0);
																	break;
															}
														break;
												case -4:allGraph.drawImage(box2, 65, 20, 0);
														if (kitbuf[1] !=0)
															switch (kitbuf[1]/8)
															{
															case 0:
															case 1:
															case 2:	allGraph.drawImage(plant[kitbuf[2]], 67, 22, 0);
																	break;
															case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 67, 22, 0);
																	break;
															}
														break;
												case -5:allGraph.drawImage(box2, 15, 20, 0);
														if (kitbuf[0] !=0)
															switch (kitbuf[0]/8)
															{
															case 0:
															case 1:
															case 2:	allGraph.drawImage(plant[kitbuf[2]], 17, 22, 0);
																	break;
															case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 17, 22, 0);
																	break;
															}
														break;
												}
											}
									
											if (kitIndex >= -5)
											 {
												 kitIndex--;
											 }else{
												 kitIndex = actor.BAGNUM+actor.bagLv-1;
											 }
											
											if (kitIndex >= 0)
											{
												allGraph.drawImage(box1, 10+(kitIndex%4)*39, 130 + (kitIndex/4)*44, 0);
												if (actor.bag[kitIndex] != 0)
												{
													switch ((actor.bag[kitIndex]-1)/8)
													{
													case 0:
													case 1:
													case 2: allGraph.drawImage(plant[actor.bag[kitIndex]], 12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
															break;
													case 3: allGraph.drawImage(aniPro[actor.bag[kitIndex]-25], 12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
															break;
													case 4: allGraph.drawImage(fish[actor.bag[kitIndex]-33], 12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
															break;
													case 5:
													case 6:
													case 7: allGraph.drawImage(plant[25],12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
															break;
													}
												}
											}
											else{
												switch(kitIndex)
												{
												case -1:
														allGraph.drawImage(confirm[0], 120, 75, 0);
														break;
												case -2:allGraph.drawImage(box1, 30, 64, 0);
														if (kitbuf[3] !=0)
															switch (kitbuf[3]/8)
															{
															case 0:
															case 1:
															case 2:	allGraph.drawImage(plant[kitbuf[3]], 32, 66, 0);
																	break;
															case 3:	allGraph.drawImage(aniPro[kitbuf[3]-25], 32, 66, 0);
																	break;
															}
														break;
												case -3:allGraph.drawImage(box1, 115, 20, 0);
														if (kitbuf[2] !=0)
															switch (kitbuf[2]/8)
															{
															case 0:
															case 1:
															case 2:	allGraph.drawImage(plant[kitbuf[2]], 117, 22, 0);
																	break;
															case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 117, 22, 0);
																	break;
															}
														break;
												case -4:allGraph.drawImage(box1, 65, 20, 0);
														if (kitbuf[1] !=0)
															switch (kitbuf[1]/8)
															{
															case 0:
															case 1:
															case 2:	allGraph.drawImage(plant[kitbuf[2]], 67, 22, 0);
																	break;
															case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 67, 22, 0);
																	break;
															}
														break;
												case -5:allGraph.drawImage(box1, 15, 20, 0);
														if (kitbuf[0] !=0)
															switch (kitbuf[0]/8)
															{
															case 0:
															case 1:
															case 2:	allGraph.drawImage(plant[kitbuf[2]], 17, 22, 0);
																	break;
															case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 17, 22, 0);
																	break;
															}
														break;
													}
												}
											 break;
									case RIGHT:
									case DOWN:  if (kitIndex >= 0)
												{
													allGraph.drawImage(box2, 10+(kitIndex%4)*39, 130 + (kitIndex/4)*44, 0);
													if (actor.bag[kitIndex] != 0)
													{
														switch ((actor.bag[kitIndex]-1)/8)
														{
														case 0:
														case 1:
														case 2: allGraph.drawImage(plant[actor.bag[kitIndex]], 12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
																break;
														case 3: allGraph.drawImage(aniPro[actor.bag[kitIndex]-25], 12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
																break;
														case 4: allGraph.drawImage(fish[actor.bag[kitIndex]-33], 12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
																break;
														case 5:
														case 6:
														case 7: allGraph.drawImage(plant[25],12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
																break;
														}
													}
												}
												else{
													switch(kitIndex)
													{
													case -1:
															allGraph.drawImage(confirm[1], 120, 75, 0);
															break;
													case -2:allGraph.drawImage(box2, 30, 64, 0);
															if (kitbuf[3] !=0)
																switch (kitbuf[3]/8)
																{
																case 0:
																case 1:
																case 2:	allGraph.drawImage(plant[kitbuf[3]], 32, 66, 0);
																		break;
																case 3:	allGraph.drawImage(aniPro[kitbuf[3]-25], 32, 66, 0);
																		break;
																}
															break;
													case -3:allGraph.drawImage(box2, 115, 20, 0);
															if (kitbuf[2] !=0)
																switch (kitbuf[2]/8)
																{
																case 0:
																case 1:
																case 2:	allGraph.drawImage(plant[kitbuf[2]], 117, 22, 0);
																		break;
																case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 117, 22, 0);
																		break;
																}
															break;
													case -4:allGraph.drawImage(box2, 65, 20, 0);
															if (kitbuf[1] !=0)
																switch (kitbuf[1]/8)
																{
																case 0:
																case 1:
																case 2:	allGraph.drawImage(plant[kitbuf[2]], 67, 22, 0);
																		break;
																case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 67, 22, 0);
																		break;
																}
															break;
													case -5:allGraph.drawImage(box2, 15, 20, 0);
															if (kitbuf[0] !=0)
																switch (kitbuf[0]/8)
																{
																case 0:
																case 1:
																case 2:	allGraph.drawImage(plant[kitbuf[2]], 17, 22, 0);
																		break;
																case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 17, 22, 0);
																		break;
																}
															break;
													}
												}
										
												if (kitIndex < actor.BAGNUM+actor.bagLv-1)
												 {
													 kitIndex++;
												 }else{
													 kitIndex = -5;
												 }
												
												if (kitIndex >= 0)
												{
													allGraph.drawImage(box1, 10+(kitIndex%4)*39, 130 + (kitIndex/4)*44, 0);
													if (actor.bag[kitIndex] != 0)
													{
														switch ((actor.bag[kitIndex]-1)/8)
														{
														case 0:
														case 1:
														case 2: allGraph.drawImage(plant[actor.bag[kitIndex]], 12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
																break;
														case 3: allGraph.drawImage(aniPro[actor.bag[kitIndex]-25], 12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
																break;
														case 4: allGraph.drawImage(fish[actor.bag[kitIndex]-33], 12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
																break;
														case 5:
														case 6:
														case 7: allGraph.drawImage(plant[25],12+(kitIndex%4)*39, 132 + (kitIndex/4)*44, 0);
																break;
														}
													}
												}
												else{
													switch(kitIndex)
													{
													case -1:
															allGraph.drawImage(confirm[0], 120, 75, 0);
															break;
													case -2:allGraph.drawImage(box1, 30, 64, 0);
															if (kitbuf[3] !=0)
																switch (kitbuf[3]/8)
																{
																case 0:
																case 1:
																case 2:	allGraph.drawImage(plant[kitbuf[3]], 32, 66, 0);
																		break;
																case 3:	allGraph.drawImage(aniPro[kitbuf[3]-25], 32, 66, 0);
																		break;
																}
															break;
													case -3:allGraph.drawImage(box1, 115, 20, 0);
															if (kitbuf[2] !=0)
																switch (kitbuf[2]/8)
																{
																case 0:
																case 1:
																case 2:	allGraph.drawImage(plant[kitbuf[2]], 117, 22, 0);
																		break;
																case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 117, 22, 0);
																		break;
																}
															break;
													case -4:allGraph.drawImage(box1, 65, 20, 0);
															if (kitbuf[1] !=0)
																switch (kitbuf[1]/8)
																{
																case 0:
																case 1:
																case 2:	allGraph.drawImage(plant[kitbuf[2]], 67, 22, 0);
																		break;
																case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 67, 22, 0);
																		break;
																}
															break;
													case -5:allGraph.drawImage(box1, 15, 20, 0);
															if (kitbuf[0] !=0)
																switch (kitbuf[0]/8)
																{
																case 0:
																case 1:
																case 2:	allGraph.drawImage(plant[kitbuf[2]], 17, 22, 0);
																		break;
																case 3:	allGraph.drawImage(aniPro[kitbuf[2]-25], 17, 22, 0);
																		break;
																}
															break;
														}
													}
												 break;
									case CONF: 
												if (kitIndex>=0 && kitIndex<actor.BAGNUM+actor.bagLv)
													{
														if (kitP<4 && actor.bag[bagIndex]<32)
														{
															kitbuf[kitP] = actor.bag[bagIndex];
															kitP = 0;
															while (kitbuf[kitP] != 0 && kitP<4)
															{
																kitP++;
															}
														}
														
													}
												else if(kitIndex == -1)
													{
													 	if(actor.cook(1))
													 	{
													 		allGraph.drawImage(storePic, 0, 0, 0);
														 	kitbuf[0] = 0;
														 	kitbuf[1] = 0;
														 	kitbuf[2] = 0;
														 	kitbuf[3] = 0;
														 	kitbuf[4] = 0;
														 	kitP = 0;
													 	}
													}
												else{
														kitbuf[kitIndex+5] = 0;
														kitP = kitIndex+5;
													}
											 break;
									case SE_B:
											 storePic = null;
											 confirm = null;
											 gState = 100;
											 keyPressed = true;
											 banRender();
											 headRender();
											 break;
									}
								repaint();
								}
								
							}
							break;
						 }
							
				//TODO 选项菜单
				case 200:{
							allGraph.drawImage(bMenu, 0, 0, 0);
							allGraph.drawImage(banner[banIndex], 0, banData[banIndex], 0);
							switch (banIndex)
							{
								case 0:
										for (int i=0;i<(actor.BAGNUM+actor.bagLv);i++)
										{
											
												allGraph.drawImage(box1, 55+(i%3)*39, 100 + (i/3)*44, 0);
												if (actor.bag[i] != 0)
												{
													switch (actor.bag[i]/8)
													{
													case 0:
													case 1:
													case 2: allGraph.drawImage(plant[actor.bag[i]], 57+(i%3)*39, 102 + (i/3)*44, 0);
															break;
													case 3: allGraph.drawImage(aniPro[actor.bag[i]-25], 57+(i%3)*39, 102 + (i/3)*44, 0);
															break;
													case 4: allGraph.drawImage(fish[actor.bag[i]-33], 57+(i%3)*39, 102 + (i/3)*44, 0);
															break;
													case 5:
													case 6:
													case 7: allGraph.drawImage(plant[25], 57+(i%3)*39, 102 + (i/3)*44, 0);
															break;
													}
												}
										}
										break;
								case 1:{
											allGraph.setColor(0xffffff);
											allGraph.drawString("耕种等级：LV"+actor.gdLv, 55, 16, 0);
											allGraph.drawString("钓鱼等级：LV"+actor.fishLv, 55, 66, 0);
											allGraph.drawString("喂养等级：LV"+actor.fishLv, 55, 116, 0);
											allGraph.drawString("烹饪等级：LV"+actor.fishLv, 55, 166, 0);
											allGraph.setColor(0x000000);
											allGraph.drawString("耕种等级：LV"+actor.gdLv, 54, 15, 0);
											allGraph.drawString("钓鱼等级：LV"+actor.fishLv, 54, 65, 0);
											allGraph.drawString("喂养等级：LV"+actor.fishLv, 54, 115, 0);
											allGraph.drawString("烹饪等级：LV"+actor.fishLv, 54, 165, 0);
											
											allGraph.drawImage(stick, 54, 40, 0);
											allGraph.drawImage(stick, 54, 90, 0);
											allGraph.drawImage(stick, 54, 140, 0);
											allGraph.drawImage(stick, 54, 190, 0);
											allGraph.setColor(0x00ff99);
											allGraph.fillRect(58, 44, (actor.gdExp - Data.gdExpTab[actor.gdLv])*93/(Data.gdExpTab[actor.gdLv + 1] - Data.gdExpTab[actor.gdLv]), 12);
											allGraph.fillRect(58, 94, (actor.fishExp - Data.gdExpTab[actor.fishLv])*93/(Data.fishExpTab[actor.gdLv + 1] - Data.fishExpTab[actor.gdLv]), 12);
											allGraph.fillRect(58, 144, (actor.aniExp - Data.aniExpTab[actor.fishLv])*93/(Data.aniExpTab[actor.gdLv + 1] - Data.aniExpTab[actor.gdLv]), 12);
											allGraph.fillRect(58, 194, (actor.cookExp - Data.cookExpTab[actor.fishLv])*93/(Data.cookExpTab[actor.gdLv + 1] - Data.cookExpTab[actor.gdLv]), 12);
											break;
										}
										
								case 2:
										allGraph.drawImage(searchWord, 56, 50, 0);
										break;
							}
							allGraph.setClip(46, 0, WIDTH-46, HEIGHT);
							allGraph.drawImage(grayMask, 0, 0, 0);
							allGraph.setClip(0, 0, WIDTH, HEIGHT);
							repaint();
							try{Thread.sleep(100);}catch (InterruptedException e){};
							//获取按键信息
							key = this.getKeyStates();
							if (key == 0)
							{
								keyPressed = false;
							}else if (!keyPressed)
							{
								keyPressed = true;
								/*//nokia s40 
								 case NOKIA_S40_9:{
								  					gState = 100;
								  					break;
								  				  }
								 * */
								/*//Sony Ericsson
								 case SE_A :{
								 				gState = 100;
								 				break;
								 			}	
								 */
								/*//WTK
								 case WTK_9:{
								 				gState = 100;
								 				break;
								 			}
								 */
								switch (key)
								{
									case UP: {	
												banIndex = (banIndex + SELECTION - 1)%SELECTION;
												break;
											 }
									case DOWN:{
												banIndex = (banIndex + 1)%SELECTION;
												break;
											  }
									case CONF:{
												switch (banIndex)
												{
													case 0:{//进入物品菜单
																bagIndex = 1;
																gState = 201;
																bagIndex = 0;
																break;
														   }
													case 1:{//进入状态菜单
																gState = 202;
																break;
															}
													case 2:{//进入系统菜单
																gState = 203;
																break;
															}
													case 3:{//返回游戏
																gState = 100;
																time1 = System.currentTimeMillis();
																break;
															}
												}
												break;
											  }
									case SE_B: gState = 100;
											   lm.paint(allGraph, 0, HEAD_HEIGHT);
											   banRender();
											   headRender();
											   repaint();
											   keyPressed = true;
									   		   break;
							
								}
							}
							break;
						}
				
				//TODO 物品菜单
				case 201:{
							//图像渲染
							allGraph.drawImage(bMenu, 0, 0, 0);
							allGraph.drawImage(banner[0], 0, 0, 0);
							for (int i=0;i<(actor.BAGNUM+actor.bagLv);i++)
							{
								if (i+1 == bagIndex)
								{
									allGraph.drawImage(box2, 55+(i%3)*39, 100 + (i/3)*44, 0);
								}else{
									allGraph.drawImage(box1, 55+(i%3)*39, 100 + (i/3)*44, 0);
								}
								if (actor.bag[i] != 0)
								{
									switch (actor.bag[i]/8)
									{
									case 0:
									case 1:
									case 2: allGraph.drawImage(plant[actor.bag[i]], 57+(i%3)*39, 102 + (i/3)*44, 0);
											break;
									case 3:	allGraph.drawImage(aniPro[actor.bag[i]-25], 57+(i%3)*39, 102 + (i/3)*44, 0);
											break;
									case 4: allGraph.drawImage(fish[actor.bag[i]-33], 57+(i%3)*39, 102 + (i/3)*44, 0);
											break;
									case 5:
									case 6:
									case 7: allGraph.drawImage(plant[25], 57+(i%3)*39, 102 + (i/3)*44, 0);
											break;
									}
								}
							}
							repaint();
							
							key = this.getKeyStates();
							if (key == 0)
							{
								keyPressed = false;
							}else if (!keyPressed)
							{
								keyPressed = true;
								switch (key)
								{
									case UP:
									case LEFT:{
												bagIndex --;
												if (bagIndex < 0)
												{
													bagIndex = actor.BAGNUM+actor.bagLv;
												}
												
										      }break;
									case DOWN:
									case RIGHT:{
												bagIndex ++;
												if (bagIndex > actor.BAGNUM+actor.bagLv)
												{
													bagIndex = 0;
												}
												
												}break;
									case CONF:{
												switch (bagIndex)
												{
													case 0: {//返回选项菜单
																gState = 200;
																break;
															}
													default : actor.index = bagIndex - 1;
												}
												break;
											  }
									case SE_B: gState = 100;
											   lm.paint(allGraph, 0, HEAD_HEIGHT);
											   headRender();
											   banRender();
											   repaint();
											   keyPressed = true;
											   break;
							
								}
							}
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {e.printStackTrace();}
							break;
							
						 }
				
				//TODO 技能状态菜单
				case 202:{
							//图像渲染
							key = this.getKeyStates();
							if (key == 0)
							{
								keyPressed = false;
							}else if (!keyPressed)
							{
								keyPressed = true;
								switch (key)
								{
								case CONF: gState = 200;
										   break;
								case SE_B: jump = true;
										   break;
								}
							}
							if (!jump)
							{
								allGraph.drawImage(bMenu, 0, 0, 0);
								allGraph.drawImage(banner[1], 0, banData[1], 0);
								allGraph.setColor(0xffffff);
								allGraph.drawString("耕种等级：LV"+actor.gdLv, 55, 16, 0);
								allGraph.drawString("钓鱼等级：LV"+actor.fishLv, 55, 66, 0);
								allGraph.drawString("喂养等级：LV"+actor.fishLv, 55, 116, 0);
								allGraph.drawString("烹饪等级：LV"+actor.fishLv, 55, 166, 0);
								allGraph.setColor(0x000000);
								allGraph.drawString("耕种等级：LV"+actor.gdLv, 54, 15, 0);
								allGraph.drawString("钓鱼等级：LV"+actor.fishLv, 54, 65, 0);
								allGraph.drawString("喂养等级：LV"+actor.fishLv, 54, 115, 0);
								allGraph.drawString("烹饪等级：LV"+actor.fishLv, 54, 165, 0);
								
								allGraph.drawImage(stick, 54, 40, 0);
								allGraph.drawImage(stick, 54, 90, 0);
								allGraph.drawImage(stick, 54, 140, 0);
								allGraph.drawImage(stick, 54, 190, 0);
								allGraph.setColor(0x00ff99);
								allGraph.fillRect(58, 44, (actor.gdExp - Data.gdExpTab[actor.gdLv])*93/(Data.gdExpTab[actor.gdLv + 1] - Data.gdExpTab[actor.gdLv]), 12);
								allGraph.fillRect(58, 94, (actor.fishExp - Data.gdExpTab[actor.fishLv])*93/(Data.fishExpTab[actor.gdLv + 1] - Data.fishExpTab[actor.gdLv]), 12);
								allGraph.fillRect(58, 144, (actor.aniExp - Data.aniExpTab[actor.fishLv])*93/(Data.aniExpTab[actor.gdLv + 1] - Data.aniExpTab[actor.gdLv]), 12);
								allGraph.fillRect(58, 194, (actor.cookExp - Data.cookExpTab[actor.fishLv])*93/(Data.cookExpTab[actor.gdLv + 1] - Data.cookExpTab[actor.gdLv]), 12);
								repaint();
							}else{
								jump = false;
								gState = 100;
								headRender();
								banRender();
								keyPressed = true;
								  
							}
							break;
						 }
				
				//TODO 系统菜单
				case 203:{
							//图像渲染
							
							key = this.getKeyStates();
							if (key == 0)
							{
								keyPressed = false;
							}else if (!keyPressed)
							{
								keyPressed = true;
								switch (key)
								{
									case LEFT:
									case UP:{	
												sysIndex = (sysIndex + SYSSELECTION - 1) % SYSSELECTION;
												break;
											   }	
									case RIGHT:
									case DOWN: {
												sysIndex = (sysIndex + 1) % SYSSELECTION;
												break;
												}
									case CONF:{
												switch (sysIndex)
												{
													case 0:{//存档
																if (saveData("f"))
																	infoRender("存档成功");
																else infoRender("存档失败");
																break;
															}
													case 1:{//读档
																readData("f");
																infoRender("已读取");
																scSwitch = true;
																break;
															}
													case 2:{//返回选项菜单
																gState = 200;
																break;
															}
													case 3:{//退出
																quit();
																break;
															}
												}
											  }
											  break;
									case SE_B: gState = 100;
											   jump = true;
											   
											   break;
									
								}
							}
							if (jump)
							{
								jump = false;
								lm.paint(allGraph, 0, HEAD_HEIGHT);
								banRender();
								headRender();
								repaint();
								keyPressed = true;
							}else{
								allGraph.drawImage(bMenu, 0, 0, 0);
								allGraph.drawImage(banner[2], 0, banData[2], 0);
								allGraph.drawImage(searchWord, 56, 50, 0);
								allGraph.drawImage(searchBox, 58, 50+sysIndex*32, 0);
								repaint();
							}
							break;
						 }
			}
		}
		

	}

}
