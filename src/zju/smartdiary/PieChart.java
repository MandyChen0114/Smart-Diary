package zju.smartdiary;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

public class PieChart extends AbstractDemoChart {
	double[] values = new double[5];
	private MyDataBaseAdapter m_MyDataBaseAdapter;

	public PieChart() {
	}

	public PieChart(MyDataBaseAdapter m_MyDataBaseAdapter) {
		this.m_MyDataBaseAdapter = m_MyDataBaseAdapter;
	}

	protected CategorySeries buildCategoryDataset(String title, double[] values) {
		CategorySeries series = new CategorySeries(title);

		series.add("学习时间", values[0]);
		series.add("睡眠时间", values[1]);
		series.add("运动时间", values[2]);
		series.add("三餐时间", values[3]);
		series.add("娱乐时间", values[4]);

		return series;
	}

	public GraphicalView execute(Context context) {
		addValues();
		CategorySeries categorySeries = buildCategoryDataset("时间分布图", values);

		int[] colors = new int[] { Color.BLUE, Color.CYAN, Color.GREEN,
				Color.YELLOW, Color.RED };
		DefaultRenderer renderer = buildCategoryRenderer(colors);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomEnabled(true);
		renderer.setChartTitleTextSize(20);
		renderer.setDisplayValues(true);
		renderer.setShowLabels(true);
		SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
		r.setGradientEnabled(true);
		r.setGradientStart(0, Color.BLUE);
		r.setGradientStop(0, Color.GREEN);
		r.setHighlighted(true);
		GraphicalView view = ChartFactory.getPieChartView(context,
				categorySeries, renderer);

		return view;
	}

	void addValues() {
		Calendar today;
		int year, month, day;

		today = Calendar.getInstance();
		year = today.get(Calendar.YEAR);
		month = today.get(Calendar.MONTH);
		day = today.get(Calendar.DAY_OF_MONTH);

		Cursor cur = m_MyDataBaseAdapter.fetchDataSeven(year, month, day);
		int count = cur.getCount();

		double studyData = 0, sleepData = 0, sportData = 0, mealData = 0, entertainmentData = 0;

		for (int i = 0; i < count; i++) {
			studyData += cur.getDouble(2);
			sleepData += cur.getDouble(3);
			sportData += cur.getDouble(4);
			mealData += cur.getDouble(5);
			entertainmentData += cur.getDouble(6);
			cur.moveToNext();
		}

		values[0] =Math.round(studyData*100/(60000*60*count))/100.0;	
		values[1] =Math.round(sleepData*100/(60000*60*count))/100.0;
		values[2] =Math.round(sportData*100/(60000*60*count))/100.0;
		values[3] =Math.round(mealData*100/(60000*60*count))/100.0;
		values[4] =Math.round(entertainmentData*100/(60000*60*count))/100.0;
//		values[0] = studyData / (60000 * 60*count);
//		values[1] = sleepData / (60000 * 60*count);
//		values[2] = sportData / (60000 * 60*count);
//		values[3] = mealData / (60000 * 60*count);
//		values[4] = entertainmentData / (60000 * 60*count);
	}
}
