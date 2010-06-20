package com.don;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class StockDistribution {
    public static void main(String [] args ) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String db = "stock5";
        String url = "jdbc:mysql://localhost:3306/" + db;
        Connection conn = java.sql.DriverManager.getConnection(url, "root", "");
        Statement stat = conn.createStatement();

        ResultSet rs = stat.executeQuery("select table_name from information_schema.tables where table_schema = '" + db + "'");
        // store all stock names in arrStock, so we can use it later to calculate the EV
        List<String> arrStock = new ArrayList<String>();

        while( rs.next() ) {
           String tableName = rs.getString("table_name");
           arrStock.add(tableName);
        }

        String theDate = "07-May-2010";
        StockDistribution stockDist = new StockDistribution();
        for( int idx=0; idx< arrStock.size(); idx++ ) {
            stockDist.popEv( arrStock.get(idx), theDate); 
        }
    }

    public String popEvCore(double prevClose, String symbol, double open, double high, double low, double close, long vol) {
        double gain = close - open;
        double spread = high - low;
        final double PI = 0.01;
        double evHigh = Math.max(high, prevClose);
        double evLow = Math.min(low, prevClose);
        double evDiff = prevClose - close;
        double evSpread = high - low;
        double effVol = (evDiff + PI) / (evSpread + PI) * vol;
        String effVolStr = "" + effVol;
        if( effVolStr.length() > 10)
            effVolStr = effVolStr.substring(0,10); // keep length max 10, so no mysql insert exception (column is varchar(10)

        return effVolStr;        
    }

    public void popEv(String stock, String date) throws SQLException {
        String db = "stock5";
        String url = "jdbc:mysql://localhost:3306/" + db;
        Connection conn = java.sql.DriverManager.getConnection(url, "root", "");
        
        Statement stat2 = conn.createStatement();
        Statement stat = conn.createStatement();
        ResultSet rs2 = stat2.executeQuery("select symbol,data,open,high,low,close,vol from " + stock + " where data like '" + date + "%' order by data");

        String symbol = "";
        double prevClose = 0.0;
        int idx = 0;
        while( rs2.next() ) {
            symbol = rs2.getString("symbol");
            double open  = rs2.getDouble("open");
            String data = rs2.getString("data");
            double close = rs2.getDouble("close");
            double high  = rs2.getDouble("high");
            double low   = rs2.getDouble("low") ;
            long volume = rs2.getLong("vol") ;

            String datetime = rs2.getString("data");

            if(idx > 0) {
                String effVolStr = popEvCore(prevClose, symbol, open, high, low, close, volume);
                String str =  "update " + stock + " set ev = '" + effVolStr + "' where data = '" + data + "'";
                stat.executeUpdate(str);
            }
            idx++;
            prevClose = close;
        }
        System.out.println( symbol + "~~ done");

    }
}
