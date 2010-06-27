package com.don;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import au.com.bytecode.opencsv.CSVReader;

public class DataImporter {
    public static void main(String [] args) throws ClassNotFoundException, SQLException, IOException, ParseException {
        Class.forName(MyConstants.DATABASE_DRIVER);
        String db = "stock8";
        String url = MyConstants.DATABASE_JDBC + db;
        Connection conn = java.sql.DriverManager.getConnection(url, "root", "");
        Statement statement = conn.createStatement();

//        String filename = "/Users/lydonchandra/scala/stockdata/NASDAQ_20100507.csv";
        String filename = "/Users/lydonchandra/Documents/workspace-sts-carbon/QuotePub3/src/com/don/test/data/test.csv";        
        CSVReader reader = new CSVReader(new FileReader(filename));
        
        
        String[] aLine = new String[10];
        String lastSymbol = "";
        DataImporter dataImporter = new DataImporter();
        while( (aLine = reader.readNext()) != null  ) { // for each line in csv file
            String symbol = aLine[0];
            if( !symbol.equals(lastSymbol) && !symbol.equalsIgnoreCase("Symbol")) {            	
                if( !dataImporter.isTable(symbol, db, statement) ) // symbol/table does not exist, create one
                	dataImporter.createTable2(symbol, statement);
            }

            lastSymbol = symbol;

            if( !symbol.equalsIgnoreCase("Symbol") ) {
            	dataImporter.insertLine2(aLine, symbol, statement);
            }
        }
    	
//    	String date = "09-Jun-2010 09:02";
//        // before 09-Jun-2010 09:02
//        // after  date=2010-06-09 time=09:02
//
//        DateFormat dateFormatter1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm"); //"07-May-2010 09:00"
//    	
//        DateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy"); //"07-May-2010 09:00"
//        DateFormat timeFormatter = new SimpleDateFormat("HH:mm"); //"07-May-2010 09:00"
//        
//        Date date1 = dateFormatter1.parse(date);
//        String formatDate = dateFormatter.format(date1);
//        String formatTime = timeFormatter.format(date1);
        
        System.out.println("done");
    }
    
//    public void importData(String filename, String db) throws ClassNotFoundException, SQLException, IOException {
//        Class.forName(MyConstants.DATABASE_DRIVER);
//        String url = MyConstants.DATABASE_JDBC + db;
//        Connection conn = java.sql.DriverManager.getConnection(url, MyConstants.DB_USERNAME, MyConstants.DB_PASS);
//        Statement statement = conn.createStatement();
//        
//        CSVReader reader = new CSVReader(new FileReader(filename));
//        
//        String[] aLine = new String[10];
//        String lastSymbol = "";
//        DataImporter dataImporter = new DataImporter();
//        while( (aLine = reader.readNext()) != null  ) { // for each line in csv file
//            String symbol = aLine[0];
//            if( !symbol.equals(lastSymbol) && !symbol.equalsIgnoreCase("Symbol")) {            	
//                if( !dataImporter.isTable(symbol, db, statement) ) // symbol/table does not exist, create one
//                	dataImporter.createTable(symbol, statement);
//            }
//
//            lastSymbol = symbol;
//
//            if( !symbol.equalsIgnoreCase("Symbol") ) {
//            	dataImporter.insertLine(aLine, symbol, statement);
//            }
//        }
//        System.out.println("done");
//    }

    public void importData(String filename, String db) throws ClassNotFoundException, SQLException, IOException, ParseException {
        Class.forName(MyConstants.DATABASE_DRIVER);
        String url = MyConstants.DATABASE_JDBC + db;
        Connection conn = java.sql.DriverManager.getConnection(url, MyConstants.DB_USERNAME, MyConstants.DB_PASS);
        Statement statement = conn.createStatement();
        
        CSVReader reader = new CSVReader(new FileReader(filename));
        
        String[] aLine = new String[10];
        String lastSymbol = "";
        DataImporter dataImporter = new DataImporter();
        while( (aLine = reader.readNext()) != null  ) { // for each line in csv file
            String symbol = aLine[0];
                       
            if( !symbol.equals(lastSymbol) && !symbol.equalsIgnoreCase("Symbol")) {            	
                if( !dataImporter.isTable(symbol, db, statement) ) // symbol/table does not exist, create one
                	dataImporter.createTable2(symbol, statement);
            }

            lastSymbol = symbol;

            if( !symbol.equalsIgnoreCase("Symbol") ) {
            	dataImporter.insertLine2(aLine, symbol, statement);
            }
        }
        System.out.println("done");
    }

    
    public boolean isTable(String symbol, String db, Statement statement) throws SQLException {
    	ResultSet rs = statement.executeQuery("select * from information_schema.tables where table_name='$" + symbol + "' and table_schema='" + db + "';" );
    	return rs.next();    		
    }
    
//    public boolean createTable(String symbol, Statement statement) throws SQLException {
//        // just in case the table exists even though it's not in information_schema.tables - is this possible?
//        statement.executeUpdate("drop table if exists $" + symbol + ";");
//        statement.executeUpdate("create table $" + symbol +
//                " (symbol varchar(20),data varchar(20),open varchar(20),high varchar(20),low varchar(20),close varchar(20),vol varchar(20), ev varchar(20));");
//		return true;
//    }
//    
//    
//    public boolean insertLine(String[] aLine, String symbol, Statement statement ) throws SQLException{
//    	String str =  "insert into $" + symbol + " (symbol,data,open,high,low,close,vol) values ('" +
//        aLine[0] + "','" + aLine[1] + "','" + aLine[2] + "','" + aLine[3] +
//        "','" + aLine[4] + "','" + aLine[5] + "','" + aLine[6] + "')";
//
//    	statement.executeUpdate(str);
//    	return true;
//    	
//    }
    
    
    public boolean createTable2(String symbol, Statement statement) throws SQLException {
        // just in case the table exists even though it's not in information_schema.tables - is this possible?
        statement.executeUpdate("drop table if exists $" + symbol + ";");
        statement.executeUpdate("create table $" + symbol +
        		"( symbol char(10), date date, time time, open decimal(18,4), high decimal(18,4), low decimal(18,4), close decimal(18,4), vol int , ev decimal(18,4));");
		return true;
    }
    
    public boolean insertLine2(String[] aLine, String symbol, Statement statement ) throws SQLException, ParseException{
    	String date = aLine[1];
        // before 09-Jun-2010 09:02
        // after  date=2010-06-09 time=09:02
    	DateFormat dateFormatter1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm"); //"07-May-2010 09:00"
    	
        DateFormat dateFormatter = new SimpleDateFormat("yyyy:MM:dd"); //"07-May-2010 09:00"
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm"); //"07-May-2010 09:00"
        
        Date origDate = dateFormatter1.parse(date);
        String formatDate = dateFormatter.format(origDate);
        String formatTime = timeFormatter.format(origDate);
        
    	String str =  "insert into $" + symbol + " (symbol,date,time,open,high,low,close,vol) values ('" +
	        aLine[0] /*symbol*/ + "','" + 
	        formatDate /*date*/ + "','" + 
	        formatTime /*time*/ + "','" +
	        aLine[2]  /*open*/ + "','" + 
	        aLine[3] /*high*/+ "','" + 
	        aLine[4] /*low*/ + "','" + 
	        aLine[5] /*close*/+ "','" + 
	        aLine[6] /*vol*/+ "')";

    	
    	statement.executeUpdate(str);
    	return true;
    	
    }
    
    
}
