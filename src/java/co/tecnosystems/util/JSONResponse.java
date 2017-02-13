/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.tecnosystems.util;

/**
 *
 * @author FVARGAS
 */
import flexjson.JSONSerializer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author eva
 */
public class JSONResponse extends JSONSerializer {

    private String msg;
    private HashMap<String, Object> map = new HashMap();
    private List lista = new LinkedList();
    public static final String MSG = "msg";
    public static final String SUCCESS = "success";
    public static final String DATA = "data";
    public static final String TOTAL = "total";
    public static final String FECHA = "fecha";
    public static final String OBJECT = "result";
    public static final String ID = "id";
    public static final String CANT1 = "cantidadTurno1";
    public static final String CANT2 = "cantidadTurno2";
    public static final String KILOS1 = "kiloTurno1";
    public static final String KILOS2 = "kiloTurno2";
    public static final String PRENSISTA1 = "Prensista1";
    public static final String PRENSISTA2 = "Prensista2";

    private void initConfig() {
        this.exclude("*.class");
        this.include("*.data", "result");
    }

    public JSONResponse(String message) {
        this.initConfig();
        map = new HashMap();
        map.put(MSG, message);
        map.put(SUCCESS, true);
    }

    public JSONResponse(String message, String imagen) {
        this.initConfig();
        map = new HashMap();
        map.put(MSG, message);
        map.put(SUCCESS, true);
        map.put(("imagen"), imagen);
    }

    public JSONResponse(String msg, boolean success, Object object) {
        this.initConfig();
        map = new HashMap();
        map.put(MSG, msg);
        map.put(SUCCESS, success);
        map.put(OBJECT, object);

    }

    public JSONResponse(boolean success, String message) {
        this.initConfig();
        map = new HashMap();
        map.put(MSG, message);
        map.put(SUCCESS, success);
    }

    public JSONResponse(String id, String fecha, boolean success, String prensa) {
        this.initConfig();
        map = new HashMap();
        map.put(ID, id);
        map.put(FECHA, fecha);
        map.put(SUCCESS, success);
        map.put("prensa", prensa);

    }

    public JSONResponse(boolean success, String message, List data) {
        this.initConfig();
        map = new HashMap();
        map.put(MSG, message);
        map.put(SUCCESS, success);
        map.put(DATA, data);
    }
    
       
    
    public JSONResponse(boolean success, HashMap<String, Object> hm){
       this.initConfig();
       map = hm;
       map.put(SUCCESS, success);
   }
    
    
    
     public JSONResponse(HashMap<String, Object> hm){
//       this.initConfig();
////       map = hm;       
         map = new HashMap();
         map = hm;
   }

    public JSONResponse(boolean success, String nombreCampo, String message, List data) {
        this.initConfig();
        map = new HashMap();
        map.put(nombreCampo, message);
        map.put(SUCCESS, success);
        map.put(DATA, data);
    }

    public JSONResponse(boolean success, String message, List data, String nombreCampo, String valor) {
        this.initConfig();
        map = new HashMap();
        map.put(MSG, message);
        map.put(SUCCESS, success);
        map.put(DATA, data);
        map.put(nombreCampo, valor);
    }

    public JSONResponse(String message,
            String cantidad_disponible, String bastidores_consumidos,
            String nro_bastidores, String cantidad_programada, boolean success) {
        
        this.initConfig();
        map = new HashMap();
        map.put(MSG, message);
        map.put(SUCCESS, success);       
        map.put("cantidad_disponible", cantidad_disponible);
        map.put("bastidores_consumidos", bastidores_consumidos);
        map.put("nro_bastidores", nro_bastidores);
        map.put("cantidad_programada", cantidad_disponible);
    }

    public JSONResponse(boolean success, String message, List data, String longitudTotal, String pesoTotal, String aleacion) {
        this.initConfig();
        map = new HashMap();
        map.put(MSG, message);
        map.put(SUCCESS, success);
        map.put(DATA, data);
        map.put("longitudTotal", longitudTotal);
        map.put("pesoTotal", pesoTotal);
        map.put("aleacion", aleacion);
    }

    public JSONResponse(boolean success, String message, String nombreCampo, String valor) {
        this.initConfig();
        map = new HashMap();
        map.put(MSG, message);
        map.put(SUCCESS, success);
        map.put(nombreCampo, valor);
    }

    public JSONResponse(boolean success, String message, String campo1, String valor1, String campo2, String valor2) {
        this.initConfig();
        map = new HashMap();
        map.put(MSG, message);
        map.put(SUCCESS, success);
        map.put(campo1, valor1);
        map.put(campo2, valor2);
    }

    public JSONResponse(boolean success, List data) {
        this.initConfig();
        map = new HashMap();
        map.put(SUCCESS, success);
        map.put(DATA, data);
    }

    public JSONResponse(boolean success, List data, int total) {
        this.initConfig();
        map = new HashMap();
        map.put(SUCCESS, success);
        map.put(DATA, data);
        map.put(TOTAL, total);
    }

     public JSONResponse(List data) {
        this.initConfig();
        lista = data;     
        
      
    }
    
    public JSONResponse(boolean success, List data, int total, String fecha) {
        this.initConfig();
        map = new HashMap<String, Object>();
        map.put(SUCCESS, success);
        map.put(DATA, data);
        map.put(TOTAL, total);
        map.put(FECHA, fecha);
    }

    public JSONResponse(boolean success, List data, int total, String nombreCampo, String valorCampo) {
        this.initConfig();
        map = new HashMap<String, Object>();
        map.put(SUCCESS, success);
        map.put(DATA, data);
        map.put(TOTAL, total);
        map.put(nombreCampo, valorCampo);
    }

    public JSONResponse(boolean success, List data, int total, String msg, String nombreCampo, String valorCampo) {
        this.initConfig();
        map = new HashMap<String, Object>();
        map.put(SUCCESS, success);
        map.put(DATA, data);
        map.put(TOTAL, total);
        map.put(MSG, msg);
        map.put(nombreCampo, valorCampo);
    }

    public JSONResponse(boolean success, String msg, String prensista1, String prensista2, List data) {
        this.initConfig();
        map = new HashMap<String, Object>();
        map.put(SUCCESS, success);
        map.put(DATA, data);
        map.put(MSG, msg);
        map.put(PRENSISTA1, prensista1);
        map.put(PRENSISTA2, prensista2);
    }

    public JSONResponse(boolean success, String msg, int total, List data) {
        this.initConfig();
        map = new HashMap<String, Object>();
        map.put(SUCCESS, success);
        map.put(DATA, data);
        map.put(TOTAL, total);
        map.put(MSG, msg);
    }

    public JSONResponse(boolean success, String msg, int total, List data, float cant1, float cant2, float kilos1, float kilos2) {
        this.initConfig();
        map = new HashMap<String, Object>();
        map.put(DATA, data);
        map.put(SUCCESS, success);
        map.put(TOTAL, total);
        map.put(MSG, msg);
        map.put(CANT1, cant1);
        map.put(CANT2, cant2);
        map.put(KILOS1, kilos1);
        map.put(KILOS2, kilos2);
    }

    public JSONResponse(boolean success, String msg, int total, List data, String nombreCampo, String valorCampo) {
        this.initConfig();
        map = new HashMap<String, Object>();
        map.put(SUCCESS, success);
        map.put(DATA, data);
        map.put(TOTAL, total);
        map.put(MSG, msg);
        map.put(nombreCampo, valorCampo);
    }

    public static HashMap<String, Object> message(String msg) {
        HashMap<String, Object> r = new HashMap<String, Object>();
        r.put(MSG, msg);
        r.put(SUCCESS, true);
        return r;
    }

    public static HashMap<String, Object> message(String msg, boolean success) {
        HashMap<String, Object> r = new HashMap<String, Object>();
        r.put(MSG, msg);
        r.put(SUCCESS, success);
        return r;
    }

    public static HashMap<String, Object> message(String msg, List data) {
        HashMap<String, Object> r = new HashMap<String, Object>();
        r.put(MSG, msg);
        r.put(SUCCESS, true);
        r.put(DATA, data);
        return r;
    }

    public static HashMap<String, Object> message(String msg, List data, int total) {
        HashMap<String, Object> r = new HashMap<String, Object>();
        r.put(MSG, msg);
        r.put(SUCCESS, true);
        r.put(TOTAL, total);
        r.put(DATA, data);
        return r;
    }

    public static HashMap<String, Object> message(String msg, boolean success, List data, int total) {
        HashMap<String, Object> r = new HashMap<String, Object>();
        r.put(MSG, msg);
        r.put(SUCCESS, success);
        r.put(TOTAL, total);
        r.put(DATA, data);
        return r;
    }

    public static HashMap<String, Object> undefinedSession() {
        HashMap<String, Object> r = new HashMap<String, Object>();
        r.put(MSG, "Session indefinida");
        r.put(SUCCESS, false);
        return r;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static JSONResponse exception(String ex) {
        return new JSONResponse("Ocurrio una excepción al procesar la solicitud: " + ex);
    }

    @Override
    public String toString() {
        return this.serialize(this.map);
    }
    
         public String toStringList() {
        return this.serialize(this.lista);
    }
}
