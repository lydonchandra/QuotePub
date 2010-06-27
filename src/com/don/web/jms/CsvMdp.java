package com.don.web.jms;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.don.DataImporter;
import com.don.StockDistribution;

public class CsvMdp implements MessageListener {

	@Override
	public void onMessage(Message message) {
		MapMessage mapMessage = (MapMessage) message;
		try {
			String filepath = mapMessage.getString("file");
			File file = new File(filepath);
			if( file.canRead() ) {
				dataImporter.importData(filepath, "stock7");
				stockDistribution.calculateAllEv("stock7", "07-May-2010" );
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private StockDistribution stockDistribution;
	public void setStockDistribution( StockDistribution stockDistribution ) {
		this.stockDistribution = stockDistribution;
	}
	
	private DataImporter dataImporter;
	public void setDataImporter( DataImporter dataImporter) {
		this.dataImporter = dataImporter;
	}

}
