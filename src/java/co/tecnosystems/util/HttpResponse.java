/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.tecnosystems.util;

import flexjson.JSONSerializer;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author edgarelias
 */
public class HttpResponse {
    
    public static final String MSG = "msg";
    public static final String SUCCESS = "success";
    public static final String DATA = "data";
    public static final String TOTAL = "total";
    public static final String CLASS = "*.class";

    public HttpResponse() {
    }
    
    public static String message(String msg){
        HashMap<String, Object> r = new HashMap<String, Object>();
        r.put(MSG, msg);
        r.put(SUCCESS, true);
        return new JSONSerializer().serialize(r);
    }
    
    public static String message(String msg, boolean success){
        HashMap<String, Object> r = new HashMap<String, Object>();
        r.put(MSG, msg);
        r.put(SUCCESS, success);
        return new JSONSerializer().serialize(r);
    }
    
    public static HashMap<String, Object> message(String msg, List data){
        HashMap<String, Object> r = new HashMap<String, Object>();
        r.put(MSG, msg);
        r.put(SUCCESS, true);
        r.put(DATA, data);
        return r;
    }
    
    public static HashMap<String, Object> message(String msg, List data, int total){
        HashMap<String, Object> r = new HashMap<String, Object>();
        r.put(MSG, msg);
        r.put(SUCCESS, true);
        r.put(TOTAL, total);
        r.put(DATA, data);
        return r;
    }
    
    public static HashMap<String, Object> message(String msg, boolean success, List data, int total){
        HashMap<String, Object> r = new HashMap<String, Object>();
        r.put(MSG, msg);
        r.put(SUCCESS, success);
        r.put(TOTAL, total);
        r.put(DATA, data);
        return r;
    }
    
    public static String undefinedSession(){
        HashMap<String, Object> r = new HashMap<String, Object>();
        r.put(MSG, "La session expiró");
        r.put(SUCCESS, false);
        r.put("JSESSIONID", false);
        return new JSONSerializer().serialize(r);
    }
    
    public static String exceptionMessage(String msg){
        return message("Ocurrio una excepción al procesar la solicitud : "+msg, false);
    }
    
    public static String numericExceptionMessage(){
        return message("Un parametro numerico es texto", false);
    }
    
    public static String stringToDateExceptionMessage(String format){
        return message("El formato de la fecha no es valido, formato: "+format, false);
    }
    
}