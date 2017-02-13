/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.tecnosystems.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author edgarelias
 */
public class DateUtil {

    public static final String FORMAT_SLASH = "dd/MM/yyyy";
    public static final String FORMAT_GUION = "dd-MM-yyyy";
    public static final String FORMAT_SQL = "yyyy-MM-dd";
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss.SSS";

    public static Date stringToDate(String fecha, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(fecha);
    }
    
    public static String dateToString(Date fecha, String format){
        return new SimpleDateFormat(format).format(fecha);
    }
    public static String getYear(){
        Date now = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("Y");
        return simpleDateformat.format(now);
    }
    public static String getMonth(){
        Date now = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("MM");
        return simpleDateformat.format(now);
    }
    
}
