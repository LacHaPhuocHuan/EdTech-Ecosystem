package com.thanhha.edtechcosystem.userservice.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final String DATE_FORMAT="dd/MM/yyyy";
    private static final DateFormat df=new SimpleDateFormat(DATE_FORMAT);

    public static String convertDateToString(Date date){
        return df.format(date);
    }
    public static Date convertToDate(String date) throws ParseException {
        return df.parse(date);
    }



}
