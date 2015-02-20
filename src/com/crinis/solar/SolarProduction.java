package com.crinis.solar;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SolarProduction extends Activity {

	private View mChart;
	private String[] mMonth = new String[] { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	private int[] income;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.solar_production);
		ArrayList<String> ac_monthly = getIntent().getStringArrayListExtra(
				"ac_monthly");

		income = new int[ac_monthly.size()];
		// mStringArray = ac_monthly.toArray(mStringArray);

		for (int i = 0; i < ac_monthly.size(); i++) {
			income[i] = (int) (Float.parseFloat(ac_monthly.get(i)));
		}

		openChart();
	}

	private void openChart() {
		int[] x = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
		// int[] income = { 100, 250, 270, 300, 200, 350, 370, 380, 0, 0, 0, 0
		// };

		// Creating an XYSeries for Income
		XYSeries incomeSeries = new XYSeries("X-axis (Months)");
		// Creating an XYSeries for Expense
		XYSeries expenseSeries = new XYSeries("Y-axis (AC Energy)");
		// Adding data to Income and Expense Series
		for (int i = 0; i < x.length; i++) {
			incomeSeries.add(i, income[i]);
			// expenseSeries.add(i, expense[i]);
		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(incomeSeries);
		// Adding Expense Series to dataset
		dataset.addSeries(expenseSeries);

		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
		// incomeRenderer.setColor(Color.CYAN); // color of the graph set to
		// cyan
		incomeRenderer.setFillPoints(true);
		incomeRenderer.setLineWidth(2f);
		incomeRenderer.setDisplayChartValues(true);
		// setting chart value distance
		incomeRenderer.setDisplayChartValuesDistance(10);
		// setting line graph point style to circle
		// incomeRenderer.setPointStyle(PointStyle.CIRCLE);
		// setting stroke of the line chart to solid
		// incomeRenderer.setStroke(BasicStroke.SOLID);
		// for filling area
		incomeRenderer.setFillBelowLine(true);
		// incomeRenderer.setFillBelowLineColor(Color.YELLOW);

		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
		// expenseRenderer.setColor(Color.GREEN);
		expenseRenderer.setFillPoints(true);
		expenseRenderer.setLineWidth(2f);
		expenseRenderer.setDisplayChartValues(true);
		// setting line graph point style to circle
		// expenseRenderer.setPointStyle(PointStyle.SQUARE);
		// setting stroke of the line chart to solid
		// expenseRenderer.setStroke(BasicStroke.SOLID);
		// for filling area
		expenseRenderer.setFillBelowLine(true);
		// expenseRenderer.setFillBelowLineColor(Color.RED);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		// multiRenderer.setChartTitle("Income vs Expense Chart");
		// multiRenderer.setXTitle("Year 2014");
		// multiRenderer.setYTitle("Amount in Dollars");

		/***
		 * Customizing graphs
		 */
		// setting text size of the title
		multiRenderer.setChartTitleTextSize(20);
		// setting text size of the axis title
		multiRenderer.setAxisTitleTextSize(18);
		// setting text size of the graph lable
		multiRenderer.setLabelsTextSize(15);
		// setting zoom buttons visiblity
		multiRenderer.setZoomButtonsVisible(false);
		// setting pan enablity which uses graph to move on both axis
		multiRenderer.setPanEnabled(false, false);
		// setting click false on graph
		multiRenderer.setClickEnabled(false);
		multiRenderer.setZoomEnabled(false, false);
		// setting zoom to false on both axis
		// multiRenderer.setZoomEnabled(true, true);
		// setting lines to display on y axis
		multiRenderer.setShowGridY(true);
		// setting lines to display on x axis
		multiRenderer.setShowGridX(true);
		// setting legend to fit the screen size
		multiRenderer.setFitLegend(true);
		// setting displaying line on grid
		// multiRenderer.setShowGrid(true);
		// setting zoom to false
		multiRenderer.setZoomEnabled(false);
		// setting external zoom functions to false
		multiRenderer.setExternalZoomEnabled(false);
		// setting displaying lines on graph to be formatted(like using
		// graphics)
		multiRenderer.setAntialiasing(false);
		// setting to in scroll to false
		multiRenderer.setInScroll(false);
		// setting to set legend height of the graph
		multiRenderer.setLegendHeight(30);
		// setting x axis label align
		multiRenderer.setXLabelsAlign(Align.CENTER);
		// setting y axis label to align
		multiRenderer.setYLabelsAlign(Align.RIGHT);
		// setting text style
		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		// setting no of values to display in y axis
		multiRenderer.setYLabels(10);
		// setting y axis max value, Since i'm using static values inside the
		// graph so i'm setting y max value to 4000.
		// if you use dynamic values then get the max y value and set here
		multiRenderer.setYAxisMax(1000);

		multiRenderer.setYAxisMin(100);
		// setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMin(-0.5);
		// setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMax(11);
		// setting bar size or space between two bars
		// multiRenderer.setBarSpacing(0.5);
		// Setting background color of the graph to transparent
		multiRenderer.setBackgroundColor(Color.WHITE);
		// Setting margin color of the graph to transparent
		multiRenderer.setMarginsColor(Color.WHITE);// (getResources().getColor(Color.TRANSPARENT));
		multiRenderer.setApplyBackgroundColor(true);
		// multiRenderer.setScale(2f);
		// setting x axis point size
		multiRenderer.setPointSize(4f);
		// setting the margin size for the graph in the order top, left, bottom,
		// right
		multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });

		for (int i = 0; i < x.length; i++) {
			multiRenderer.addXTextLabel(i, mMonth[i]);
		}

		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to
		// multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(incomeRenderer);
		multiRenderer.addSeriesRenderer(expenseRenderer);

		// this part is used to display graph on the xml
		LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
		// remove any views before u paint the chart
		chartContainer.removeAllViews();
		// drawing bar chart
		mChart = ChartFactory.getLineChartView(SolarProduction.this, dataset,
				multiRenderer);
		// adding the view to the linearlayout
		chartContainer.addView(mChart);

	}

}
