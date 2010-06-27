package com.don.web;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StringMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.don.web.jms.UploadJmsImpl;

public class UploadCsvController extends SimpleFormController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		if( !(request instanceof MultipartHttpServletRequest)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
			return null;
		}
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multipartRequest.getFile("file");
		File destination = new File( "/tmp/ " + file.getOriginalFilename() );
		file.transferTo(destination);
		response.getWriter().write("success, file uploaded to" + destination.getAbsolutePath());
		response.flushBuffer();
		
		uploadImpl.sendCsvFile(destination);
		
		return null; 
	}
	

	
	private UploadJmsImpl uploadImpl;
	public void setUploadImpl( UploadJmsImpl upload) {
		this.uploadImpl = upload;
	}
	
}
