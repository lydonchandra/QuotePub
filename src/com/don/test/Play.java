package com.don.test;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;

public class Play {

	public static void main(String[] args) throws IOException, ParseException {
		
		String sDate = "07-May-2010 10:05";
		DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
		Date date = formatter.parse(sDate);
		date = null;
//		OHLCDataItem[] items = new OHLCDataItem[10];
//		
//		int idx = 1;
//		for( int i=0; i<10; i++) {
//			items[i] = new OHLCDataItem(new Date(110, 2, idx), 1, 2, 1, 1.5, 1000);
//			idx++;
//		}
//		OHLCDataset dataset = new DefaultOHLCDataset("data1", items);	
//		
//		JFreeChart chart = ChartFactory.createCandlestickChart("stock", "datetime", "price", dataset, true);
//		ChartUtilities.saveChartAsPNG(new File("/Users/lydonchandra/chartdemo.png"), chart, 500, 500);
		
		
	}

}
