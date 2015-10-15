package zju.smartdiary;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class Record extends Fragment {
	private MyDataBaseAdapter m_MyDataBaseAdapter;

	private static double EARTH_RADIUS = 6378137;//地球半径
	
	/* 数据库中一条记录的字段 */
	double geoLat1, geoLng1, geoLat2, geoLng2;
	int screenState1, screenState2;
	String addr1, addr2;
	Date date1 = new Date(), date2 = new Date();

	/* 要显示的数据 */
	long studyTime = 0, playPhoneTime = 0, sleepTime = 0, sportTime = 0,
			mealTime = 0, entertainmentTime = 0;
	Calendar fallAsleepMoment = Calendar.getInstance(), getUpMoment = Calendar
			.getInstance(), breakfast1 = Calendar.getInstance(),
			breakfast2 = Calendar.getInstance(), lunch1 = Calendar
					.getInstance(), lunch2 = Calendar.getInstance(),
			dinner1 = Calendar.getInstance(), dinner2 = Calendar.getInstance();
	int studyQuality = 0, sleepQuality = 0, sportQuality = 0,
			mealQuality = 0;
	int playPhoneNum = 0, wakeUpNum = 0, sportDis = 0;

	Calendar today;
	int year, month, day;
	
	/*推测用到的中间变量*/
	boolean sleep = false, flag = false, wakeUp = false;
	long wakeUpTime=0;
	int tmpNum = 0;
//	boolean eating=false;
	Calendar tmpCal1 = Calendar.getInstance(), tmpCal2 = Calendar
			.getInstance();

	public Record() {
	}

	public Record(MyDataBaseAdapter m_MyDataBaseAdapter) {
		this.m_MyDataBaseAdapter = m_MyDataBaseAdapter;
	}

	// static Record newInstance() {
	// Record f = new Record();
	// return f;
	// }

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.record, container, false);

		init();
		query();

		TextView studyTimeView = (TextView) v.findViewById(R.id.studyTime);
		TextView playPhoneNumView = (TextView) v
				.findViewById(R.id.playPhoneNum);
		TextView playPhoneTimeView = (TextView) v
				.findViewById(R.id.playPhoneTime);
		TextView studyQualityView = (TextView) v
				.findViewById(R.id.studyQuality);
		TextView sleepTimeView = (TextView) v.findViewById(R.id.sleepTime);
		TextView fallAsleepMomentView = (TextView) v
				.findViewById(R.id.fallAsleepMoment);
		TextView getUpMomentView = (TextView) v.findViewById(R.id.getUpMoment);
		TextView wakeUpNumView = (TextView) v.findViewById(R.id.wakeUpNum);
		TextView sleepQualityView = (TextView) v
				.findViewById(R.id.sleepQuality);
		TextView sportTimeView = (TextView) v.findViewById(R.id.sportTime);
		TextView sportDisView = (TextView) v.findViewById(R.id.sportDis);
		TextView sportQualityView = (TextView) v
				.findViewById(R.id.sportQuality);
		TextView mealTimeView = (TextView) v.findViewById(R.id.mealTime);
		TextView BreakfastView = (TextView) v.findViewById(R.id.Breakfast);
		TextView lunchView = (TextView) v.findViewById(R.id.lunch);
		TextView dinnerView = (TextView) v.findViewById(R.id.dinner);
		TextView mealQualityView = (TextView) v.findViewById(R.id.mealQuality);
		TextView entertainmentTimeView = (TextView) v
				.findViewById(R.id.entertainmentTime);

		studyTimeView.setText("学习时间:" + MillisecToString(studyTime));
		playPhoneNumView.setText("玩手机次数:" + playPhoneNum + "次");
		playPhoneTimeView.setText("玩手机时间:" + MillisecToString(playPhoneTime));
		studyQualityView.setText("学习状况:" + BoolToString(studyQuality));
		sleepTimeView.setText("睡眠时间:" + MillisecToString(sleepTime));
		fallAsleepMomentView.setText("睡着时间:" + CalToString(fallAsleepMoment));
		getUpMomentView.setText("起床时间:" + CalToString(getUpMoment));
		wakeUpNumView.setText("醒来次数:" + wakeUpNum + "次");
		sleepQualityView.setText("睡眠状况:" + BoolToString(sleepQuality));
		sportTimeView.setText("运动时间:" + MillisecToString(sportTime));
		sportDisView.setText("运动路程:" + sportDis + "米");
		sportQualityView.setText("锻炼状况:" + BoolToString(sportQuality));
		mealTimeView.setText("三餐时间:" + MillisecToString(mealTime));
		BreakfastView.setText("早餐:" + CalToString(breakfast1) + "~"
				+ CalToString(breakfast2));
		lunchView.setText("午餐:" + CalToString(lunch1) + "~"
				+ CalToString(lunch2));
		dinnerView.setText("晚餐:" + CalToString(dinner1) + "~"
				+ CalToString(dinner2));
		mealQualityView.setText("饮食状况:" + BoolToString(mealQuality));
		entertainmentTimeView.setText("娱乐时间:"
				+ MillisecToString(entertainmentTime));

		return v;
	}
	
	void init(){
		today = Calendar.getInstance();
		year = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH);
		day = today.get(Calendar.DAY_OF_MONTH);//for test!!!!!!!!!!!!!!!!!!!!!

		fallAsleepMoment.set(year, month, day, 0, 0, 0);
		getUpMoment.set(year, month, day, 0, 0, 0);
		breakfast1.set(year, month, day, 0, 0, 0);
		breakfast2.set(year, month, day, 0, 0, 0);
		lunch1.set(year, month, day, 0, 0, 0);
		lunch2.set(year, month, day, 0, 0, 0);
		dinner1.set(year, month, day, 0, 0, 0);
		dinner2.set(year, month, day, 0, 0, 0);		
		studyTime = 0;
		playPhoneTime = 0;
		sleepTime = 0;
		sportTime = 0;
		mealTime = 0;
		entertainmentTime = 0;
		playPhoneNum = 0;
		wakeUpNum = 0;
		sportDis = 0;
		studyQuality = 0;
		sleepQuality = 0;
		sportQuality = 0;
		mealQuality = 0;
	}

	/* 毫秒转成“几小时几分” */
	String MillisecToString(long ms) {
		return ms / (1000 * 60 * 60) + "小时" + (ms / (1000 * 60)) % 60 + "分";
	}

	/* 根据质量bool值转成“好”或“差” */
	String BoolToString(int quality) {
		if (quality==1)
			return "好";
		else
			return "差";
	}

	/* Calendar转成“HH:mm” */
	@SuppressLint("SimpleDateFormat")
	String CalToString(Calendar cal) {
		SimpleDateFormat f = new SimpleDateFormat("HH:mm");
		return f.format(cal.getTime());
	}

	@SuppressLint("SimpleDateFormat")
	/* 推测用户行为 */
	void query() {
		String timestr;
		SimpleDateFormat format;

		Cursor cur = m_MyDataBaseAdapter.fetchData1(year, month, day);
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					timestr = cur.getString(1);
					geoLat1 = cur.getDouble(2);
					geoLng1 = cur.getDouble(3);
					addr1 = cur.getString(4);
					screenState1 = cur.getInt(5);

					// 把时间从string转化到date类型
					format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						date1 = format.parse(timestr);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					cur.moveToNext();
					timestr = cur.getString(1);
					geoLat2 = cur.getDouble(2);
					geoLng2 = cur.getDouble(3);
					addr2 = cur.getString(4);
					screenState2 = cur.getInt(5);

					// 把时间从string转化到date类型
					format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						date2 = format.parse(timestr);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					speculate();

					cur.moveToPrevious();
				} while (cur.moveToNext() && !cur.isLast());
			}
		}
		evaluate();
	}

	private double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}

	public double GetDistance(double lat1, double lng1, double lat2, double lng2)
	{
		// 保留4位有效数字
//		BigDecimal mlat1 = new BigDecimal(lat1);
//		double stdgeoLat1 = mlat1.setScale(4, BigDecimal.ROUND_HALF_UP)
//				.doubleValue();
//		BigDecimal mlng1 = new BigDecimal(lng1);
//		double stdgeoLng1 = mlng1.setScale(4, BigDecimal.ROUND_HALF_UP)
//				.doubleValue();
//		BigDecimal mlat2 = new BigDecimal(lat2);
//		double stdgeoLat2 = mlat2.setScale(4, BigDecimal.ROUND_HALF_UP)
//				.doubleValue();
//		BigDecimal mlng2 = new BigDecimal(lng2);
//		double stdgeoLng2 = mlng2.setScale(4, BigDecimal.ROUND_HALF_UP)
//				.doubleValue();
					
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lng1) - rad(lng2);

	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * EARTH_RADIUS;
	   s = Math.round(s * 10000) / 10000;
	   return s;
	}
	
	/* 推测用户行为 */
	void speculate() {
		long time = date2.getTime() - date1.getTime();
		Calendar cal1 = Calendar.getInstance(), cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int thisHour = cal1.get(Calendar.HOUR_OF_DAY);

		// 运动或是娱乐
		if (addr1 == null) {
//			if (eating == false) {
				double dis = GetDistance(geoLat1, geoLng1, geoLat2, geoLng2);
				if (dis == 0) {
					entertainmentTime += time;
				} else {
					sportTime += time;
					sportDis += (int) dis;
				}
//			}
		}
		
		//运动
		else if(addr1.equals("操场")){
			sportTime+=time;
			double dis=GetDistance(geoLat1,geoLng1,geoLat2,geoLng2);
			sportDis+=(int)dis;
		}

		/* 在寝室，可能在学习？娱乐、睡觉（学习和娱乐怎么区分？）
		假定：用户在22点~5点之间入睡，5点之后起床。入睡前会玩至少2min手机再睡，醒来的第一件事是先玩至少2min手机*/
//		if (addr1==null||addr1.equals("8舍")){//for test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		else if (addr1.equals("8舍")) {
			entertainmentTime += time;
			if (thisHour >= 22 || thisHour <= 5) {//22点~5点，入睡时间
				if (screenState1 == 1) {
					if (wakeUpTime >= 2*60*1000) {//玩手机超过2min后锁屏，判断要睡觉了
						tmpCal1 = cal1;
						sleep = true;
						tmpNum = 0;//从此时开始计算醒来次数
					} else if (wakeUpTime > 0 && sleep == true) {//睡着了之后，玩手机时间小于2min，判断醒来一次 
						tmpNum += 1;
					}
					wakeUpTime = 0;//一次醒来的时间，清零
				} else {
					wakeUpTime += time;
				}
			} else if (thisHour > 5) {//5点以后，起床时间
				fallAsleepMoment = tmpCal1;//睡着时间确定
				if (screenState1 == 1) {
					wakeUpTime = 0;//一次醒来的时间，清零
				} else {
					wakeUpTime += time;
					if (flag == false) {//设置这次开始玩手机的时间为醒来的时间
						tmpCal2 = cal1;
						flag = true;
					}
					if (wakeUp == false) {
						if (wakeUpTime >= 2*60*1000) {//玩手机超过2min则判断醒来了
							wakeUp = true;
							getUpMoment = tmpCal2;
							wakeUpNum = tmpNum;
							sleepTime=getUpMoment.getTime().getTime()-fallAsleepMoment.getTime().getTime();
							entertainmentTime-=sleepTime;
						} else{//玩手机少于2min则判断是睡觉中醒来一次
							if(screenState2==1){//下一条记录是锁屏时，说明这次醒来时间结束
								tmpNum += 1;
								flag=false;
							}
						}
					}
				}
			}
		}

		// 在学习（可能在学习过程中玩手机）
		else if (addr1.equals("曹楼") || addr1.equals("图书馆")) {
			studyTime += time;
			if (screenState1 == 0) {// 在玩手机
				if (screenState2 == 1) {
					playPhoneNum += 1;
				}
				playPhoneTime += time;
			}
		}

		// 在食堂时，在吃饭(如果经过食堂？要看能否定位出一直在食堂）
		else if (addr1.equals("一食") || addr1.equals("怡膳堂")) {
			// mealTime+=time;
			if (thisHour < 10) {// 在10点前，在吃早餐

				if (breakfast1.get(Calendar.HOUR_OF_DAY) == 0) {// Breakfast1还未设置（还是初始化的0值）时，开始吃早餐
					breakfast1 = cal1;
				}
				if ((addr2 == null || !addr2.equals(addr1))&&(cal1.getTime().getTime()-breakfast1.getTime().getTime()<60*60000)) {// 下一条记录不再在同一个食堂时，早餐吃完
					if (breakfast2.get(Calendar.HOUR_OF_DAY) != 0) {
						mealTime -= breakfast2.getTime().getTime()
								- breakfast1.getTime().getTime();
					}
					breakfast2 = cal1;
					mealTime += breakfast2.getTime().getTime()
							- breakfast1.getTime().getTime();
				}
			} else if (thisHour >= 10 && thisHour < 16) {// 在10点~14点之间，在吃午餐
				if (lunch1.get(Calendar.HOUR_OF_DAY) == 0) {// lunch1还未设置（还是初始化的0值）时，开始吃早餐
					lunch1 = cal1;
				}
				if (addr2 == null || !addr2.equals(addr1)) {// 下一条记录不再在同一个食堂时，午餐吃完
					if (lunch2.get(Calendar.HOUR_OF_DAY) != 0) {
						mealTime -= lunch2.getTime().getTime()
								- lunch1.getTime().getTime();
					}
					lunch2 = cal1;
					mealTime += lunch2.getTime().getTime()
							- lunch1.getTime().getTime();
				}
			} else if (thisHour >= 16) {// 在16点前，在吃晚餐
				if (dinner1.get(Calendar.HOUR_OF_DAY) == 0) {// dinner1还未设置（还是初始化的0值）时，开始吃早餐
					dinner1 = cal1;
				}
				if (addr2 == null || !addr2.equals(addr1)) {// 下一条记录不再在同一个食堂时，晚餐吃完
					if (dinner2.get(Calendar.HOUR_OF_DAY) != 0) {
						mealTime -= dinner2.getTime().getTime()
								- dinner1.getTime().getTime();
					}
					dinner2 = cal1;
					mealTime += dinner2.getTime().getTime()
							- dinner1.getTime().getTime();
				}
			}
		}
	}

	void evaluate() {
		//如果出现所有时间加起来！=24小时的情况，就对entertainmentTime做相应的调整
		if(today.get(Calendar.HOUR_OF_DAY)>=22){
			entertainmentTime=24*60*60000-studyTime-playPhoneTime-sleepTime-sportTime-mealTime;
			int count=m_MyDataBaseAdapter.fetchData(year,month,day).getCount();
			if(count==0){
				insertData();
			}
		}
		
		//判断学习状况
		if(studyTime<3*60*60000||playPhoneNum>=5||playPhoneTime>15*60000)
			studyQuality=0;
		else
			studyQuality=1;
		
		//判断睡眠状况
		if(sleepTime<8*60*60000||fallAsleepMoment.get(Calendar.HOUR_OF_DAY)<6||wakeUpNum>2)
			sleepQuality=0;
		else
			sleepQuality=1;
		
		//判断运动状况
		if(sportTime<1*60*60000||sportDis<5000)
			sportQuality=0;
		else
			sportQuality=1;
		
		//判断饮食状况
		if(breakfast1.get(Calendar.HOUR_OF_DAY)==0)
			mealQuality=0;
		else if(lunch1.get(Calendar.HOUR_OF_DAY)==0||lunch1.get(Calendar.HOUR_OF_DAY)>1)
			mealQuality=0;
		else if(dinner1.get(Calendar.HOUR_OF_DAY)==0||dinner1.get(Calendar.HOUR_OF_DAY)>6)
			mealQuality=0;
		else
			mealQuality=1;
	}
	
	/* 插入一条数据 */
	void insertData() {
		m_MyDataBaseAdapter.insertData2(studyTime,sleepTime,sportTime,mealTime,
				entertainmentTime,studyQuality,sleepQuality,sportQuality,mealQuality);
	}

}