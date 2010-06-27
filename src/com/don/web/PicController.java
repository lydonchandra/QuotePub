package com.don.web;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.don.db.Stock;
import com.don.db.evDao;

public class PicController extends AbstractController{
	public PicController(){}
	
	private evDao evdao;
	public void setEvDao(evDao evdao) {
		this.evdao = evdao;
	}	
	
	public void renderChart(String var, OutputStream stream) throws Exception {
		String symbol = "$AAPL";
		List<Stock> theStocks = evdao.getStock(symbol);
		
		long stocksize = theStocks.size();
		OHLCDataItem[] items = new OHLCDataItem[(int) stocksize];
		DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm"); //"07-May-2010 09:00"
		for( int i=0; i<stocksize; i++) {
			Stock stock = theStocks.get(i);
			Date date = formatter.parse(stock.getDate());
			items[i] = new OHLCDataItem(		date,
												stock.getOpen(), 
												stock.getHigh(), 
												stock.getLow(), 
												stock.getClose(), 
												stock.getVol());
		}
		DefaultOHLCDataset dataset = new DefaultOHLCDataset(symbol, items);
		JFreeChart chart2 = ChartFactory.createHighLowChart(symbol, "minutes", "prices", dataset, true);		
		ChartUtilities.writeChartAsPNG(stream, chart2, 500, 500);		
	}



	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		renderChart("a", arg1.getOutputStream());
		// TODO Auto-generated method stub
		return null;
	}

}
