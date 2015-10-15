package zju.smartdiary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.GraphicalView;
import com.amap.api.maps2d.MapView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public  class Statistics extends Fragment{
	private MyDataBaseAdapter m_MyDataBaseAdapter;
	
	Context context;
	
	LinearLayout m_LinearLayout;
	
	ListView m_ListView;
	
//	RadioGroup		m_RadioGroup;
//	RadioButton		m_Radio1;
//	RadioButton m_Radio2;
//	RadioButton m_Radio3;	
	GraphicalView graphicalView; 
    
//public  class Statistics extends AbstractDemoChart{
//	static Statistics newInstance() {
//		Statistics f = new Statistics();
//	    return f;
//	}
	
	public Statistics() {
	}

	public Statistics(MyDataBaseAdapter m_MyDataBaseAdapter) {
		this.m_MyDataBaseAdapter = m_MyDataBaseAdapter;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = this.getActivity();
		
		m_LinearLayout=new LinearLayout(context);
		m_LinearLayout.setOrientation(LinearLayout.VERTICAL);
//		m_LinearLayout.setBackgroundColor(Color.BLACK);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		m_ListView=new ListView(context);
		m_LinearLayout.addView(m_ListView, param);
		
////		radioGroupInit();
//				
		LineChart lineChart=new LineChart(m_MyDataBaseAdapter);
		GraphicalView lineChartView=lineChart.execute(context);
		return lineChartView;
//		
//		PieChart pieChart=new PieChart(m_MyDataBaseAdapter);
//		GraphicalView pieChartView=pieChart.execute(context);
//		
//		BarChart barChart=new BarChart(m_MyDataBaseAdapter);
//		GraphicalView barChartView=barChart.execute(context);
//		
//		m_LinearLayout.addView(lineChartView, param);
//	//	m_LinearLayout.addView(pieChartView, param);
//		m_LinearLayout.addView(barChartView, param);
//		
//	//	m_LinearLayout.addView(m_RadioGroup, param);
//	//	m_LinearLayout.addView(graphicalView, param);
		
//		return m_LinearLayout;
	}
	

		
		  
//	void radioGroupInit(){
//		m_RadioGroup=new RadioGroup(context);
//		m_Radio1=new RadioButton(context);
//		m_Radio2=new RadioButton(context);
//		m_Radio3=new RadioButton(context);
//		
//		m_Radio1.setText("日常活动图");
//		m_Radio2.setText("时间分布图");
//		m_Radio3.setText("生活质量图");
//		
//		m_Radio1.setId(1);
//		m_Radio2.setId(2);
//		m_Radio3.setId(3);
//		
//		m_RadioGroup.addView(m_Radio1);
//		m_RadioGroup.addView(m_Radio2);
//		m_RadioGroup.addView(m_Radio3);
//		
//		/* 设置事件监听  */
//		OnCheckedChangeListener listener=new RadioGroup.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId)
//			{
//				// TODO Auto-generated method stub
//				if (checkedId == m_Radio1.getId())
//				{
//					LineChart lineChart=new LineChart(m_MyDataBaseAdapter);
//					graphicalView=lineChart.execute(context);
//				}
//				else if (checkedId == m_Radio2.getId())
//				{
//					PieChart pieChart=new PieChart(m_MyDataBaseAdapter);
//					graphicalView=pieChart.execute(context);
//	//				graphicalView.postInvalidate();
//				}
//				else{
//					BarChart barChart=new BarChart(m_MyDataBaseAdapter);
//					graphicalView=barChart.execute(context);
//				}
//			}
//		};
//		
//		m_RadioGroup.setOnCheckedChangeListener(listener);  
//		m_RadioGroup.check(1);
//	}
	
	
}
