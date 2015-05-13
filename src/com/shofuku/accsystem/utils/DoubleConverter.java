package com.shofuku.accsystem.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class DoubleConverter extends StrutsTypeConverter {
	
	
	public double convertFromStringNoContextForDetailsTable(String stringValue) {
		DecimalFormat df = new DecimalFormat("000,000,000.00");
		
		double value=0.0;
		try {
			if(df.parse(stringValue) instanceof java.lang.Double ) {
				value = Double.valueOf(valueSubStringForDetailsTable((Double)(df.parse(stringValue))));
			}else if(df.parse(stringValue) instanceof java.lang.Long) {
				value = Long.valueOf(valueSubStringForDetailsTable((Long)(df.parse(stringValue))));
			}else {
				value = Long.valueOf(valueSubStringForDetailsTable((Long)(df.parse(stringValue))));
			}
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public double formatDoubleToCurrency(double value) {
		DecimalFormat df = new DecimalFormat("000,000,000.00");
		
		try {
			value = (Double) df.parse(String.valueOf(value));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	private String valueSubStringForDetailsTable(Object value){
		String x = String.valueOf(value);
		int beginIndex = x.indexOf(".");
		int endIndex = x.length();
		if(endIndex-beginIndex>3&&beginIndex>-1){
			x = x.substring(0, beginIndex+3);
		}
		return x;
	}
	
	public double convertFromStringNoContext(String stringValue) {
		DecimalFormat df = new DecimalFormat("000,000,000.00###");
		
		double value=0.0;
		try {
			if(df.parse(stringValue) instanceof java.lang.Double ) {
				value = Double.valueOf(valueSubString((Double)(df.parse(stringValue))));
			}else if(df.parse(stringValue) instanceof java.lang.Long) {
				value = Long.valueOf(valueSubString((Long)(df.parse(stringValue))));
			}else {
				value = Long.valueOf(valueSubString((Long)(df.parse(stringValue))));
			}
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return value;
	}
	/*
	 * this cuts all values after 4 decimal places
	 */
	private String valueSubString(Object value){
		String x = String.valueOf(value);
		int beginIndex = x.indexOf(".");
		int endIndex = x.length();
		if(endIndex-beginIndex>5&&beginIndex>-1){
			x = x.substring(0, beginIndex+5);
		}
		return x;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public Object convertFromString(Map context, String[] values, Class clazz) {
		DecimalFormat df = new DecimalFormat("000,000,000.00##");
		
		
		double value=0.0;
		try {
			if("".equalsIgnoreCase(values[0])){
				value =0.0;
			}else{
				if(df.parse(values[0]) instanceof java.lang.Double ) {
					value = Double.valueOf(valueSubString((Double)(df.parse(values[0]))));
				}else if(df.parse(values[0]) instanceof java.lang.Long) {
					value = Long.valueOf(valueSubString((Long)(df.parse(values[0]))));
				}else {
					value = Long.valueOf(valueSubString((Long)(df.parse(values[0]))));
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object value) {
		DecimalFormat df = new DecimalFormat("###,###,##0.00##");
		Double newValue = (Double)value;
		String x = df.format(newValue);
		return x;
	}
}
