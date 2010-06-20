package com.don.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.don.db.Stock;
import com.don.db.evDao;

public class HomePageController extends AbstractController {
	public HomePageController() {}
	
	private evDao evdao;
	public void setEvDao( evDao evdao ) {
		this.evdao = evdao;
	}
	
	protected ModelAndView handleRequestInternal( HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Stock> theStocks = evdao.getStock("$AAPL");
		return new ModelAndView("ev", "stocks", theStocks);
	}
	
	

}
 	