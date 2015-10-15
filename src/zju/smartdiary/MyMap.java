package zju.smartdiary;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Date;

import zju.smartdiary.MainActivity.SectionsPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.graphics.Color;
import android.location.Location;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;

@SuppressLint("ValidFragment")
public class MyMap extends Fragment implements LocationSource,
		AMapLocationListener{

	private MapView mapView;
	private AMap aMap;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private HashMap<LocatePoint, String> locateMap = new HashMap<LocatePoint, String>();
	private MyDataBaseAdapter m_MyDataBaseAdapter;

	// ��¼�����ݿ������
	private Timestamp timestamp;
	// private int year,month,day,hour,minute,second;
	private int screenState;
	private double geoLat, geoLng;
	private String addr;

	LatLonPoint startPoint, endPoint;
	String addr1, addr2;

	// static MyMap newInstance(MyDataBaseAdapter m_MyDataBaseAdapter) {
	// MyMap f = new MyMap();
	// return f;
	// }

	public MyMap() {
	}

	public MyMap(MyDataBaseAdapter m_MyDataBaseAdapter) {
		this.m_MyDataBaseAdapter = m_MyDataBaseAdapter;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// /* ����MyDataBaseAdapter���� */
		// m_MyDataBaseAdapter = new MyDataBaseAdapter(this.getActivity());
		//
		// /* ȡ�����ݿ���� */
		// m_MyDataBaseAdapter.open();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.map, container, false);
		mapView = (MapView) v.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);// �˷���������д
		init();
		setLocateMap();

		return v;
	}

	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		// if (aMap == null) {
		aMap = mapView.getMap();
		setUpMap();
		// }
	}

	/**
	 * ����һЩamap������
	 */
	private void setUpMap() {
		// �Զ���ϵͳ��λС����
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// ����С�����ͼ��
		myLocationStyle.strokeColor(Color.BLACK);// ����Բ�εı߿���ɫ
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// ����Բ�ε������ɫ
		// myLocationStyle.anchor(int,int)//����С�����ê��
		myLocationStyle.strokeWidth(1.0f);// ����Բ�εı߿��ϸ
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
	}

	/**
	 * ����������д
	 */
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * ����������д
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		// deactivate();
	}

	/**
	 * ����������д
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ����������д
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * �˷����Ѿ�����
	 */
	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	/**
	 * ���λ
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this
					.getActivity());
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2�汾��������������true��ʾ��϶�λ�а���gps��λ��false��ʾ�����綨λ��Ĭ����true Location
			 * API��λ����GPS�������϶�λ��ʽ
			 * ����һ�������Ƕ�λprovider���ڶ�������ʱ�������2000���룬������������������λ���ף����ĸ������Ƕ�λ������
			 */
			mAMapLocationManager.requestLocationUpdates(
			// LocationManagerProxy.GPS_PROVIDER, 2000, 10, this);
					LocationProviderProxy.AMapNetwork, 60000, 10, this);
		}
	}

	/**
	 * ֹͣ��λ
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	/**
	 * gps��λ�ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		getScreenState();// ��ȡ"�Ƿ�����"״̬
		// getCurrentTime();//��ȡ��ǰʱ��
		getLatLng(aLocation);// ��ȡ��γ�Ⱥ͵�ַ
		insertData();// �����ݲ������ݿ�

	}

	/* ��ȡ�Ƿ�������״̬ */
	private void getScreenState() {
		Context context=this.getActivity();
		android.app.KeyguardManager mKeyguardManager = null ;
		if(context!=null){
			mKeyguardManager = (KeyguardManager) context.getSystemService(
						Context.KEYGUARD_SERVICE);
		}
		if (mKeyguardManager!=null&&mKeyguardManager.inKeyguardRestrictedInputMode())
			screenState = 1;
		else
			screenState = 0;
	}

	/* ��ȡ��ǰʱ�� */
	// private void getCurrentTime(){
	// Date date =new Date();
	// timestamp=new Timestamp(date.getTime());
	// Calendar c = Calendar.getInstance();
	// year = c.get(Calendar.YEAR);
	// month = c.get(Calendar.MONTH);
	// day = c.get(Calendar.DAY_OF_MONTH);
	// hour = c.get(Calendar.HOUR_OF_DAY);
	// minute = c.get(Calendar.MINUTE);
	// second = c.get(Calendar.SECOND);
	// }

	/* ��ȡ��γ�� */
	public void getLatLng(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// ��ʾϵͳС����

			geoLat = aLocation.getLatitude();
			geoLng = aLocation.getLongitude();

			// ����4λ��Ч����
			BigDecimal bLat = new BigDecimal(geoLat);
			double stdgeoLat = bLat.setScale(4, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			BigDecimal bLng = new BigDecimal(geoLng);
			double stdgeoLng = bLng.setScale(3, BigDecimal.ROUND_HALF_UP)
					.doubleValue();

			LocatePoint locatePoint = new LocatePoint(stdgeoLat, stdgeoLng);

			// Toast.makeText(this.getActivity(),"γ��:"+geoLat+"����:"+geoLng,
			// Toast.LENGTH_LONG).show();
			getAddress(locatePoint);
		}
	}

	/* ���ݾ�γ���жϵ�ǰ�ص� */
	public void getAddress(LocatePoint latLonPoint) {
		addr = locateMap.get(latLonPoint);
		if (addr == null) {
			Toast.makeText(this.getActivity(), R.string.no_result,
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this.getActivity(), addr, Toast.LENGTH_LONG).show();
		}
	}

	/* ����һ������ */
	void insertData() {
		m_MyDataBaseAdapter.insertData1(geoLat, geoLng, addr, screenState);
	}

	/* ��γ�����ַӳ��� */
	private void setLocateMap() {
		locateMap
				.put(new LocatePoint((double) 30.2565, (double) 120.123), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2585, (double) 120.119), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2585, (double) 120.120), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2587, (double) 120.119), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2589, (double) 120.121), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2590, (double) 120.120), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2590, (double) 120.121), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2590, (double) 120.122), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2591, (double) 120.120), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2591, (double) 120.121), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2591, (double) 120.122), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2592, (double) 120.121), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2593, (double) 120.121), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2594, (double) 120.121), "��¥");
		locateMap
		.put(new LocatePoint((double) 30.2595, (double) 120.120), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2595, (double) 120.121), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2596, (double) 120.121), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2597, (double) 120.121), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2598, (double) 120.121), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2599, (double) 120.121), "��¥");
		locateMap
				.put(new LocatePoint((double) 30.2608, (double) 120.122), "��¥");

		locateMap
				.put(new LocatePoint((double) 30.2626, (double) 120.125), "�ٳ�");
		locateMap
				.put(new LocatePoint((double) 30.2628, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2629, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2631, (double) 120.124), "�ٳ�");
		locateMap
				.put(new LocatePoint((double) 30.2631, (double) 120.125), "�ٳ�");
		locateMap
				.put(new LocatePoint((double) 30.2632, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2632, (double) 120.125), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2633, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2633, (double) 120.125), "�ٳ�");
		locateMap
				.put(new LocatePoint((double) 30.2634, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2635, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2636, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2636, (double) 120.125), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2637, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2638, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2638, (double) 120.125), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2639, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2639, (double) 120.125), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2640, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2641, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2641, (double) 120.125), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2642, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2642, (double) 120.125), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2643, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2644, (double) 120.124), "�ٳ�");
		locateMap.put(new LocatePoint((double) 30.2645, (double) 120.124), "�ٳ�");
		
		
		
		locateMap.put(new LocatePoint((double) 30.2636, (double) 120.121),
				"ͼ���");

		locateMap
				.put(new LocatePoint((double) 30.2650, (double) 120.121), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2655, (double) 120.123), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2656, (double) 120.123), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2657, (double) 120.123), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2658, (double) 120.123), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2658, (double) 120.124), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2659, (double) 120.123), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2659, (double) 120.124), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2660, (double) 120.123), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2660, (double) 120.125), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2661, (double) 120.123), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2662, (double) 120.123), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2663, (double) 120.123), "һʳ");
		locateMap
				.put(new LocatePoint((double) 30.2664, (double) 120.123), "һʳ");

		locateMap
				.put(new LocatePoint((double) 30.2644, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2646, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2648, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2649, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2650, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2651, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2652, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2653, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2654, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2655, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2656, (double) 120.122), "8��");//
		locateMap
				.put(new LocatePoint((double) 30.2657, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2658, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2659, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2660, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2661, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2662, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2663, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2664, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2665, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2666, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2667, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2668, (double) 120.122), "8��");
		locateMap
				.put(new LocatePoint((double) 30.2669, (double) 120.122), "8��");

		locateMap.put(new LocatePoint((double) 30.2656, (double) 120.124),
				"������");
	//	locateMap.put(new LocatePoint((double) 30.2657, (double) 120.124),
	//			"������");
		locateMap.put(new LocatePoint((double) 30.2660, (double) 120.124),
				"������");
		locateMap.put(new LocatePoint((double) 30.2661, (double) 120.124),
				"������");
		locateMap.put(new LocatePoint((double) 30.2661, (double) 120.125),
				"������");
		locateMap.put(new LocatePoint((double) 30.2661, (double) 120.126),
				"������");
		locateMap.put(new LocatePoint((double) 30.2662, (double) 120.125),
				"������");
		locateMap.put(new LocatePoint((double) 30.2662, (double) 120.126),
				"������");
		locateMap.put(new LocatePoint((double) 30.2663, (double) 120.125),
				"������");
		locateMap.put(new LocatePoint((double) 30.2663, (double) 120.126),
				"������");
		locateMap.put(new LocatePoint((double) 30.2664, (double) 120.125),
				"������");
		locateMap.put(new LocatePoint((double) 30.2667, (double) 120.125),
				"������");
		locateMap.put(new LocatePoint((double) 30.2699, (double) 120.127),
				"������");
	}
}