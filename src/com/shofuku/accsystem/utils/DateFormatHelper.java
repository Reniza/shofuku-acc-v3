package com.shofuku.accsystem.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

public class DateFormatHelper {

	public DateFormatHelper() {

	}
	
	public Timestamp getPaymentDateByTerm(Timestamp startDate, String paymentTerm) {
		int num =0; // here is a number of days to add
		
		if(paymentTerm.indexOf("7")>-1) {
			num=7;
		}else if(paymentTerm.indexOf("15")>-1) {
			num=15;
		}else if(paymentTerm.indexOf("30")>-1) {
			num=30;
		}else if(paymentTerm.indexOf("45")>-1) {
			num=45;
		}else if(paymentTerm.indexOf("60")>-1) {
			num=60;
		}else if(paymentTerm.indexOf("90")>-1) {
			num=90;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		
		// add number of days
		cal.add(Calendar.DATE ,num);
		//and now formatting the date to meet ur requirements,I prefer
		return new Timestamp(cal.getTime().getTime());
			
	}

	public Date calculatePaymentDate(Date date, String days) {
		
		return null;
	}
	
	public String dynamicParseTimestampToString(Timestamp ts,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String formattedDate = sdf.format(ts);
		String dateString[] = formattedDate.split("-");
		String dateStringComb = parseNumberTo3LetterMonth(dateString[0]) + "-" + dateString[1] + "-"
				+ dateString[2].substring(2);
		return dateStringComb;
	}
	
	public String parseTimestampToString(Timestamp ts) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		String formattedDate = sdf.format(ts);
		String dateString[] = formattedDate.split("-");
		String dateStringComb = parseNumberTo3LetterMonth(dateString[0]) + "-" + dateString[1] + "-"
				+ dateString[2].substring(2);
		return dateStringComb;
	}
	
	public String parseDateToString(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-YYYY");

	if(date!=null){
		String formattedDate = sdf.format(date);
		String dateString[] = formattedDate.split("-");
		String dateStringComb = parseNumberTo3LetterMonth(dateString[0]) + "-" + dateString[1] + "-"
		+ dateString[2].substring(0);
		return dateStringComb;
		}else{
		return "";
		}
	}
	
	public String dynamicParseDateToString(java.util.Date date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

	if(date!=null){
		String formattedDate = sdf.format(date);
		String dateString[] = formattedDate.split("-");
		String dateStringComb = parseNumberTo3LetterMonth(dateString[0]) + "-" + dateString[1] + "-"
		+ dateString[2].substring(2);
		return dateStringComb;
		}else{
		return "";
		}
	}

	public String parse3LetterMonthToNumber(String month) {

		for (Month m : Month.values())
			if (month.equalsIgnoreCase(m.getMonthAbrv())) {
				month = String.valueOf(m.getMonth());
				break;
			}
		return month;
	}
	
	public String parseNumberTo3LetterMonth(String month) {

		for (Month m : Month.values())
			if (Integer.valueOf(month)==m.getMonth()) {
				month = String.valueOf(m.getMonthAbrv());
				break;
			}
		return month;
	}
	
	public Timestamp dynamicParseStringToTimestamp(String date,String format) {

		String dateString[] = date.split("-");

		String dateStringComb = dateString[0] + "-" + dateString[1] + "-"
				+ dateString[2].substring(0, 2);

		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Timestamp timestInp = null;
		try {
			timestInp = new java.sql.Timestamp(sdf.parse(dateStringComb).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestInp;

	}
	
	public Timestamp dynamicParseWordedDateToTimestamp(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(SASConstants.TIMESTAMP_FORMAT);

		Timestamp timestInp = null;
		timestInp = new java.sql.Timestamp(date.getTime());
		return timestInp;

	}
	
	public Timestamp dynamicParseDateToTimestamp(java.util.Date date,String format) {

		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Timestamp timestInp = null;
		timestInp = new java.sql.Timestamp(date.getTime());
		return timestInp;

	}
	

	public Timestamp parseStringToTimestamp(String date) {

		String dateString[] = date.split("-");

		String dateStringComb = dateString[0] + "-" + dateString[1] + "-"
				+ dateString[2].substring(0, 2);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Timestamp timestInp = null;
		try {
			timestInp = new java.sql.Timestamp(sdf.parse(dateStringComb).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestInp;

	}

	public Date parseStringToTime(String timestampFormatDate) {

		String dateString[] = timestampFormatDate.split("-");
		String dateStringComb = dateString[0] + "-" + dateString[1] + "-"
				+ dateString[2].substring(0, 2);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(dateStringComb));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date( cal.getTime().getTime());
	}
	
	public String getDateToday() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
	
	public Timestamp getTimeStampToday() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp timestInp = null;
		String timeString= sdf.format(cal.getTime());
		try {
			return new java.sql.Timestamp(sdf.parse(timeString).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getDateFromDateString(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseStringToTime(date));
		return String.valueOf(cal.get(Calendar.DATE));
	}
	
	public String getMonthFromDateString(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseStringToTime(date));
		return String.valueOf(cal.get(Calendar.MONTH));
	}
	
	public String getYearFromDateString(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseStringToTime(date));
		return String.valueOf(cal.get(Calendar.YEAR));
	}
	
	public String parseDateToStringAsNumbers(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	if(date!=null){
		String formattedDate = sdf.format(date);
		String dateString[] = formattedDate.split("-");
		String dateStringComb = dateString[0] + "-" + dateString[1] + "-"
		+ dateString[2];
		return dateStringComb;
		}else{
		return "";
		}
	}


}
