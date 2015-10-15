package zju.smartdiary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class LineChart extends AbstractDemoChart {
		List<double[]> values = new ArrayList<double[]>();
		private MyDataBaseAdapter m_MyDataBaseAdapter;
		static int days=7;
		
		public LineChart() {
		}

		public LineChart(MyDataBaseAdapter m_MyDataBaseAdapter) {
			this.m_MyDataBaseAdapter = m_MyDataBaseAdapter;
		}
		
	  /**
	   * Executes the chart demo.
	   * 
	   * @param context the context
	   * @return the built intent
	   */
	  public GraphicalView execute(Context context) {
	    String[] titles = new String[] { "学习时间", "睡眠时间", "运动时间", "三餐时间","娱乐时间" };
	    List<double[]> x = new ArrayList<double[]>();
	    for (int i = 0; i < titles.length; i++) {
	      x.add(new double[] { 1, 2, 3, 4, 5, 6, 7});
	    }
	    
	    addValues();
	    
	    int[] colors = new int[] { Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED};
	    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
	        PointStyle.TRIANGLE, PointStyle.SQUARE, PointStyle.POINT };
	    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
	    }
	    setChartSettings(renderer, "日常活动图", "天数", "小时数/h", 1, 7, 0, 24,
	        Color.LTGRAY, Color.LTGRAY);
	    renderer.setXLabels(7);
	    renderer.setYLabels(10);
	    renderer.setShowGrid(true);
	    renderer.setXLabelsAlign(Align.RIGHT);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    renderer.setZoomButtonsVisible(true);
	    renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
	    renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });

	    XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
//	    XYSeries series = dataset.getSeriesAt(0);
//	    series.addAnnotation("Vacation", 6, 30);
	    GraphicalView view=ChartFactory.getLineChartView(context, dataset, renderer);
	    
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
		  
		  double[] studyData=new double[days];
		  double[] sleepData=new double[days];
		  double[] sportData=new double[days];
		  double[] mealData=new double[days];
		  double[] entertainmentData=new double[days];
		  
		  for(int i=0;i<days;i++){
			  studyData[i]=0;
			  sleepData[i]=0;
			  sportData[i]=0;
			  mealData[i]=0;
			  entertainmentData[i]=0;
		  }
		  
		  cur.moveToLast();
		  for(int i=days-1;i>days-1-count;i--){
			  studyData[i]=cur.getDouble(2)/(60000*60);
			  sleepData[i]=cur.getDouble(3)/(60000*60);
			  sportData[i]=cur.getDouble(4)/(60000*60);
			  mealData[i]=cur.getDouble(5)/(60000*60);
			  entertainmentData[i]=cur.getDouble(6)/(60000*60);
			  cur.moveToPrevious();
		  }
		  
//		  studyDate={12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
//			        13.9 }};
		  values.add(studyData);
		  values.add(sleepData);
		  values.add(sportData);
		  values.add(mealData);
		  values.add(entertainmentData);
	  }
}
