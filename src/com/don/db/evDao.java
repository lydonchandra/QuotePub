package com.don.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class evDao {
	private JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private static final String STOCK_SELECT = 
		"select symbol, data, open, high, low, close, vol, ev from ";
	
	private String getStockSelect(String stockname) {
		return STOCK_SELECT  + stockname; 
	}
	
	public List<Stock> getStock(String stockname) {
		List<Stock> matches = jdbcTemplate.query( getStockSelect(stockname),
				new Object[] { },
				new RowMapper() {
					public Object mapRow( ResultSet rs, int rowNum ) {
						Stock stock = new Stock();
						try {
							stock.symbol = rs.getString(1);
							stock.date = rs.getString(2);
							stock.open = rs.getDouble(3);
							stock.high = rs.getDouble(4);
							stock.low = rs.getDouble(5);
							stock.close = rs.getDouble(6);
							stock.vol = rs.getLong(7);
							stock.ev = rs.getDouble(8);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return stock;						
					}
		}
		);
		return matches;
	}
}
