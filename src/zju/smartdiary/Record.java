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

	private static double EARTH_RADIUS = 6378137;//����뾶
	
	/* ���ݿ���һ����¼���ֶ� */
	double geoLat1, geoLng1, geoLat2, geoLng2;
	int screenState1, screenState2;
	String addr1, addr2;
	Date date1 = new Date(), date2 = new Date();

	/* Ҫ��ʾ������ */
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
	
	/*�Ʋ��õ����м����*/
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

		studyTimeView.setText("ѧϰʱ��:" + MillisecToString(studyTime));
		playPhoneNumView.setText("���ֻ�����:" + playPhoneNum + "��");
		playPhoneTimeView.setText("���ֻ�ʱ��:" + MillisecToString(playPhoneTime));
		studyQualityView.setText("ѧϰ״��:" + BoolToString(studyQuality));
		sleepTimeView.setText("˯��ʱ��:" + MillisecToString(sleepTime));
		fallAsleepMomentView.setText("˯��ʱ��:" + CalToString(fallAsleepMoment));
		getUpMomentView.setText("��ʱ��:" + CalToString(getUpMoment));
		wakeUpNumView.setText("��������:" + wakeUpNum + "��");
		sleepQualityView.setText("˯��״��:" + BoolToString(sleepQuality));
		sportTimeView.setText("�˶�ʱ��:" + MillisecToString(sportTime));
		sportDisView.setText("�˶�·��:" + sportDis + "��");
		sportQualityView.setText("����״��:" + BoolToString(sportQuality));
		mealTimeView.setText("����ʱ��:" + MillisecToString(mealTime));
		BreakfastView.setText("���:" + CalToString(breakfast1) + "~"
				+ CalToString(breakfast2));
		lunchView.setText("���:" + CalToString(lunch1) + "~"
				+ CalToString(lunch2));
		dinnerView.setText("���:" + CalToString(dinner1) + "~"
				+ CalToString(dinner2));
		mealQualityView.setText("��ʳ״��:" + BoolToString(mealQuality));
		entertainmentTimeView.setText("����ʱ��:"
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

	/* ����ת�ɡ���Сʱ���֡� */
	String MillisecToString(long ms) {
		return ms / (1000 * 60 * 60) + "Сʱ" + (ms / (1000 * 60)) % 60 + "��";
	}

	/* ��������boolֵת�ɡ��á��򡰲 */
	String BoolToString(int quality) {
		if (quality==1)
			return "��";
		else
			return "��";
	}

	/* Calendarת�ɡ�HH:mm�� */
	@SuppressLint("SimpleDateFormat")
	String CalToString(Calendar cal) {
		SimpleDateFormat f = new SimpleDateFormat("HH:mm");
		return f.format(cal.getTime());
	}

	@SuppressLint("SimpleDateFormat")
	/* �Ʋ��û���Ϊ */
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

					// ��ʱ���stringת����date����
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

					// ��ʱ���stringת����date����
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
		// ����4λ��Ч����
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
	
	/* �Ʋ��û���Ϊ */
	void speculate() {
		long time = date2.getTime() - date1.getTime();
		Calendar cal1 = Calendar.getInstance(), cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int thisHour = cal1.get(Calendar.HOUR_OF_DAY);

		// �˶���������
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
		
		//�˶�
		else if(addr1.equals("�ٳ�")){
			sportTime+=time;
			double dis=GetDistance(geoLat1,geoLng1,geoLat2,geoLng2);
			sportDis+=(int)dis;
		}

		/* �����ң�������ѧϰ�����֡�˯����ѧϰ��������ô���֣���
		�ٶ����û���22��~5��֮����˯��5��֮���𴲡���˯ǰ��������2min�ֻ���˯�������ĵ�һ��������������2min�ֻ�*/
//		if (addr1==null||addr1.equals("8��")){//for test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		else if (addr1.equals("8��")) {
			entertainmentTime += time;
			if (thisHour >= 22 || thisHour <= 5) {//22��~5�㣬��˯ʱ��
				if (screenState1 == 1) {
					if (wakeUpTime >= 2*60*1000) {//���ֻ�����2min���������ж�Ҫ˯����
						tmpCal1 = cal1;
						sleep = true;
						tmpNum = 0;//�Ӵ�ʱ��ʼ������������
					} else if (wakeUpTime > 0 && sleep == true) {//˯����֮�����ֻ�ʱ��С��2min���ж�����һ�� 
						tmpNum += 1;
					}
					wakeUpTime = 0;//һ��������ʱ�䣬����
				} else {
					wakeUpTime += time;
				}
			} else if (thisHour > 5) {//5���Ժ���ʱ��
				fallAsleepMoment = tmpCal1;//˯��ʱ��ȷ��
				if (screenState1 == 1) {
					wakeUpTime = 0;//һ��������ʱ�䣬����
				} else {
					wakeUpTime += time;
					if (flag == false) {//������ο�ʼ���ֻ���ʱ��Ϊ������ʱ��
						tmpCal2 = cal1;
						flag = true;
					}
					if (wakeUp == false) {
						if (wakeUpTime >= 2*60*1000) {//���ֻ�����2min���ж�������
							wakeUp = true;
							getUpMoment = tmpCal2;
							wakeUpNum = tmpNum;
							sleepTime=getUpMoment.getTime().getTime()-fallAsleepMoment.getTime().getTime();
							entertainmentTime-=sleepTime;
						} else{//���ֻ�����2min���ж���˯��������һ��
							if(screenState2==1){//��һ����¼������ʱ��˵���������ʱ�����
								tmpNum += 1;
								flag=false;
							}
						}
					}
				}
			}
		}

		// ��ѧϰ��������ѧϰ���������ֻ���
		else if (addr1.equals("��¥") || addr1.equals("ͼ���")) {
			studyTime += time;
			if (screenState1 == 0) {// �����ֻ�
				if (screenState2 == 1) {
					playPhoneNum += 1;
				}
				playPhoneTime += time;
			}
		}

		// ��ʳ��ʱ���ڳԷ�(�������ʳ�ã�Ҫ���ܷ�λ��һֱ��ʳ�ã�
		else if (addr1.equals("һʳ") || addr1.equals("������")) {
			// mealTime+=time;
			if (thisHour < 10) {// ��10��ǰ���ڳ����

				if (breakfast1.get(Calendar.HOUR_OF_DAY) == 0) {// Breakfast1��δ���ã����ǳ�ʼ����0ֵ��ʱ����ʼ�����
					breakfast1 = cal1;
				}
				if ((addr2 == null || !addr2.equals(addr1))&&(cal1.getTime().getTime()-breakfast1.getTime().getTime()<60*60000)) {// ��һ����¼������ͬһ��ʳ��ʱ����ͳ���
					if (breakfast2.get(Calendar.HOUR_OF_DAY) != 0) {
						mealTime -= breakfast2.getTime().getTime()
								- breakfast1.getTime().getTime();
					}
					breakfast2 = cal1;
					mealTime += breakfast2.getTime().getTime()
							- breakfast1.getTime().getTime();
				}
			} else if (thisHour >= 10 && thisHour < 16) {// ��10��~14��֮�䣬�ڳ����
				if (lunch1.get(Calendar.HOUR_OF_DAY) == 0) {// lunch1��δ���ã����ǳ�ʼ����0ֵ��ʱ����ʼ�����
					lunch1 = cal1;
				}
				if (addr2 == null || !addr2.equals(addr1)) {// ��һ����¼������ͬһ��ʳ��ʱ����ͳ���
					if (lunch2.get(Calendar.HOUR_OF_DAY) != 0) {
						mealTime -= lunch2.getTime().getTime()
								- lunch1.getTime().getTime();
					}
					lunch2 = cal1;
					mealTime += lunch2.getTime().getTime()
							- lunch1.getTime().getTime();
				}
			} else if (thisHour >= 16) {// ��16��ǰ���ڳ����
				if (dinner1.get(Calendar.HOUR_OF_DAY) == 0) {// dinner1��δ���ã����ǳ�ʼ����0ֵ��ʱ����ʼ�����
					dinner1 = cal1;
				}
				if (addr2 == null || !addr2.equals(addr1)) {// ��һ����¼������ͬһ��ʳ��ʱ����ͳ���
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
		//�����������ʱ���������=24Сʱ��������Ͷ�entertainmentTime����Ӧ�ĵ���
		if(today.get(Calendar.HOUR_OF_DAY)>=22){
			entertainmentTime=24*60*60000-studyTime-playPhoneTime-sleepTime-sportTime-mealTime;
			int count=m_MyDataBaseAdapter.fetchData(year,month,day).getCount();
			if(count==0){
				insertData();
			}
		}
		
		//�ж�ѧϰ״��
		if(studyTime<3*60*60000||playPhoneNum>=5||playPhoneTime>15*60000)
			studyQuality=0;
		else
			studyQuality=1;
		
		//�ж�˯��״��
		if(sleepTime<8*60*60000||fallAsleepMoment.get(Calendar.HOUR_OF_DAY)<6||wakeUpNum>2)
			sleepQuality=0;
		else
			sleepQuality=1;
		
		//�ж��˶�״��
		if(sportTime<1*60*60000||sportDis<5000)
			sportQuality=0;
		else
			sportQuality=1;
		
		//�ж���ʳ״��
		if(breakfast1.get(Calendar.HOUR_OF_DAY)==0)
			mealQuality=0;
		else if(lunch1.get(Calendar.HOUR_OF_DAY)==0||lunch1.get(Calendar.HOUR_OF_DAY)>1)
			mealQuality=0;
		else if(dinner1.get(Calendar.HOUR_OF_DAY)==0||dinner1.get(Calendar.HOUR_OF_DAY)>6)
			mealQuality=0;
		else
			mealQuality=1;
	}
	
	/* ����һ������ */
	void insertData() {
		m_MyDataBaseAdapter.insertData2(studyTime,sleepTime,sportTime,mealTime,
				entertainmentTime,studyQuality,sleepQuality,sportQuality,mealQuality);
	}

}