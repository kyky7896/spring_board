package com.example.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper{

	public RequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public String[] getParameterValues(String name) {
		
		// 현재 request name에 해당하는 파라메터를 배열로 가져옴
		String[] values = super.getParameterValues(name);
		// null인경우 pass
		if (values == null) return values;
		for (int i=0; i < values.length; i++) {
			// 0,1,2값에 script라는 값이 있으면 지워서 담아줘라.
			values[i]=cleanXSS(values[i]);
		}
		return values;
	}
	
	@Override
	public String getParameter(String name) {
		log.info("getParameter : {} ",name);
		String value=cleanXSS(super.getParameter(name));
		log.info("getParameter value : {} ",value);
		return value;
	}
	
	private String cleanXSS(String value) {
		if (value==null) return value;
		return value.replaceAll("script", "");
	}
}
