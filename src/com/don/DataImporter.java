package com.don;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import au.com.bytecode.opencsv.CSVReader;

public class DataImporter {
    public static void main(String [] args) throws ClassNotFoundException, SQLException, IOException {
        Class.forName(MyConstants.DATABASE_DRIVER);
        String db = "stock6";
        String url = MyConstants.DATABASE_JDBC + db;
        Connection conn = java.sql.DriverManager.getConnection(url, "root", "");
        Statement statement = conn.createStatement();

        String filename = "/Users/lydonchandra/scala/stockdata/NASDAQ_20100507.csv";
        CSVReader reader = new CSVReader(new FileReader(filename));
        
        String[] aLine = new String[10];
        String lastSymbol = "";
        DataImporter dataImporter = new DataImporter();
        while( (aLine = reader.readNext()) != null  ) { // for each line in csv file
            String symbol = aLine[0];
            if( !symbol.equals(lastSymbol) && !symbol.equalsIgnoreCase("Symbol")) {            	
                if( !dataImporter.isTable(symbol, db, statement) ) // symbol/table does not exist, create one
                	dataImporter.createTable(symbol, statement);
            }

            lastSymbol = symbol;

            if( !symbol.equalsIgnoreCase("Symbol") ) {
            	dataImporter.insertLine(aLine, symbol, statement);
            }
        }
        System.out.println("done");
    }
    
    public boolean isTable(String symbol, String db, Statement statement) throws SQLException {
    	ResultSet rs = statement.executeQuery("select * from information_schema.tables where table_name='$" + symbol + "' and table_schema='" + db + "';" );
    	return rs.next();    		
    }
    
    public boolean createTable(String symbol, Statement statement) throws SQLException {
        // just in case the table exists even though it's not in information_schema.tables - is this possible?
        statement.executeUpdate("drop table if exists $" + symbol + ";");
        statement.executeUpdate("create table $" + symbol +
                " (symbol varchar(20),data varchar(20),open varchar(20),high varchar(20),low varchar(20),close varchar(20),vol varchar(20), ev varchar(20));");
		return true;

    }
    
    public boolean insertLine(String[] aLine, String symbol, Statement statement ) throws SQLException{
    	String str =  "insert into $" + symbol + " (symbol,data,open,high,low,close,vol) values ('" +
        aLine[0] + "','" + aLine[1] + "','" + aLine[2] + "','" + aLine[3] +
        "','" + aLine[4] + "','" + aLine[5] + "','" + aLine[6] + "')";

    	statement.executeUpdate(str);
    	return true;
    	
    }
}
