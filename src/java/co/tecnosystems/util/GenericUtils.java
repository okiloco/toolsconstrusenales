/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.tecnosystems.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author teo
 */
public class GenericUtils {

    public static int getDaysFromDate(Date fechaIni) {
        Date now = new Date();
        long diff = now.getTime() - fechaIni.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static Date getDateFromString(String format, String d) {
        Date date = null;
        try {

            date = new SimpleDateFormat(format, Locale.ENGLISH).parse(d);
        } catch (ParseException ex) {
            Logger.getLogger(GenericUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;

    }

    public static Date convertirAHoraCero(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date convertirAHora59(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 59);
        return calendar.getTime();
    }

     public static Date addToDate(Date fecha,int pro,int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.set(Calendar.HOUR_OF_DAY, value);
       
        return calendar.getTime();
    }
    
    public static String getStringFromDate(Date d, String patron) {
        if (d == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(patron);
        String result = sdf.format(d);

        return result;

    }

    public static int getDateProperty(Date d, int field) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        return c.get(field);
    }

    public static String getDuracion(Date date1, Date date2) {
        String result = "";
        // Crear 2 instancias de Calendar
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        // Establecer las fechas
        cal1.setTime(date1);

        cal2.setTime(date2);

        // conseguir la representacion de la fecha en milisegundos
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();

        long diff = milis2 - milis1;
        Calendar diferencia = new GregorianCalendar();
        diferencia.setTimeInMillis(diff);
       // System.out.println("Duracion: " + diferencia.get(Calendar.DATE) + "D" + diferencia.get(Calendar.HOUR) + "H" + diferencia.get(Calendar.MINUTE) + "M" + diferencia.get(Calendar.SECOND) + "S");
        if (diferencia.get(Calendar.DATE) != 0) {
            result = String.format("%d D %d H %d M", diferencia.get(Calendar.DATE), diferencia.get(Calendar.HOUR), diferencia.get(Calendar.MINUTE));
        } else if (diferencia.get(Calendar.HOUR) != 0) {
            result = String.format("%d H %d M", diferencia.get(Calendar.HOUR), diferencia.get(Calendar.MINUTE));
        } else if (diferencia.get(Calendar.MINUTE) != 0) {
            result = String.format("%d M", diferencia.get(Calendar.MINUTE));
        }
        return result;
    }

}
