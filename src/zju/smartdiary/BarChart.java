package zju.smartdiary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class BarChart extends AbstractDemoChart {
    List<double[]> values = new ArrayList<double[]>();
	private MyDataBaseAdapter m_MyDataBaseAdapter;

	public BarChart() {
	}

	public BarChart(MyDataBaseAdapter m_MyDataBaseAdapter) {
		this.m_MyDataBaseAdapter = m_MyDataBaseAdapter;
	}
	
	
	 public GraphicalView execute(Context context) {
		    String[] titles = new String[] { "差", "好" };

		    addValues();
		    
		    int[] colors = new int[] { Color.BLUE, Color.CYAN };
		    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		    setChartSettings(renderer, "生活质量图", "学习质量、睡眠质量、饮食状况、运动状况", "一周内的天数", 0,
		        5, 0, 7, Color.GRAY, Color.LTGRAY);
		    renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		    renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
		    renderer.setXLabels(4);
		    renderer.setYLabels(7);
		    renderer.setXLabelsAlign(Align.LEFT);
		    renderer.setYLabelsAlign(Align.LEFT);
		    renderer.setPanEnabled(true, false);
		    // renderer.setZoomEnabled(false);
		    renderer.setZoomRate(0.5f);
		    renderer.setBarSpacing(0.2f);
		    GraphicalView view=ChartFactory.getBarChartView(context, buildBarDataset(titles, values), renderer,
		        Type.STACKED);
		    
		    return view;
		  }
	 
	 void addValues(){
		  Calendar today;
		  int year, month, day;
		  
		  today = Calendar.getInstance();
		  year = today.get(Calendar.YEAR);
		  month = today.get(Calendar.MONTH);
		  day = today.get(Calendar.DAY_OF_MONTH);
		 
		  Cursor cur=m_MyDataBaseAdapter.fetchDataSeven(year,month,day);
		  int count=cur.getCount();
		  
		  double[] good=new double[4];
		  double[] bad=new double[4];

		  for(int i=0;i<4;i++){
			  good[i]=0;
			  bad[i]=0;
		  }
		  
		  cur.moveToLast();
		  for(int i=0;i<count;i++){//数据库中的项号
			  good[0]+=cur.getInt(7);
			  good[1]+=cur.getInt(8);
			  good[2]+=cur.getInt(9);
			  good[3]+=cur.getInt(10);
			  cur.moveToPrevious();
		  }
		  
		  bad[0]=count-good[0];
		  bad[1]=count-good[1];
		  bad[2]=count-good[2];
		  bad[3]=count-good[3];
		  
		  values.add(bad);
		  values.add(good);
	  }
}
