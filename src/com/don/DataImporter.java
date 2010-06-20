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
        Class.forName("com.mysql.jdbc.Driver");
        String db = "stock5";
        String url = "jdbc:mysql://localhost:3306/" + db;
        Connection conn = java.sql.DriverManager.getConnection(url, "root", "");
        Statement stat = conn.createStatement();

        String filename = "/Users/lydonchandra/scala/stockdata/NASDAQ_20100507.csv";
        CSVReader reader = new CSVReader(new FileReader(filename));

        String[] aLine = new String[10];
        String lastSymbol = "";
        while( (aLine = reader.readNext()) != null  ) {
            String symbol = aLine[0];
            if( !symbol.equals(lastSymbol) && !symbol.equalsIgnoreCase("Symbol")) {

                ResultSet rs = stat.executeQuery("select * from information_schema.tables where table_name='$" + symbol + "' and table_schema='" + db + "';" );
                if( !rs.next() ) {
                    // just in case the table exists even though it's not in information_schema.tables - is this possible?
                    stat.executeUpdate("drop table if exists $" + symbol + ";");
                    stat.executeUpdate("create table $" + symbol +
                            " (symbol varchar(20),data varchar(20),open varchar(20),high varchar(20),low varchar(20),close varchar(20),vol varchar(20), ev varchar(20));");
                    }
                }

                lastSymbol = symbol;


                if( !symbol.equalsIgnoreCase("Symbol") ) {
                    String str =  "insert into $" + symbol + " (symbol,data,open,high,low,close,vol) values ('" +
                                    aLine[0] + "','" + aLine[1] + "','" + aLine[2] + "','" + aLine[3] +
                                    "','" + aLine[4] + "','" + aLine[5] + "','" + aLine[6] + "')";

                    stat.executeUpdate(str);
                }
        }
        System.out.println("done");


        
    }
}
