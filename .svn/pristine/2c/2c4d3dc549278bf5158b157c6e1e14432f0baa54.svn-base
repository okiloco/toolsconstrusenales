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
 * @author eva
 */
public class JsonResponse1 extends JSONSerializer {

    private HashMap<String, Object> map = new HashMap();
    public static final String MSG = "msg";
    public static final String SUCCESS = "success";
    public static final String DATA = "data";
    public static final String TOTAL = "total";
    public static final String FECHA = "fecha";

    private void initConfig() {
        this.exclude("*.class");
        this.include(DATA);
    }

    public JsonResponse1(boolean success, HashMap<String, Object> hm) {
        this.initConfig();
        map = hm;
        map.put(SUCCESS, success);
    }

    public JsonResponse1(String message) {
        this.initConfig();
        map = new HashMap();
        map.put(MSG, message);
        map.put(SUCCESS, true);
    }

    public JsonResponse1(boolean success, String message, Object data) {
        this.initConfig();
        map = new HashMap();
        map.put(MSG, message);
        map.put(SUCCESS, success);
        map.put(DATA, data);
    }

    public JsonResponse1(boolean success, String message) {
        this.initConfig();
        map = new HashMap();
        map.put(MSG, message);
        map.put(SUCCESS, success);
    }

    public JsonResponse1(boolean success, List data) {
        this.initConfig();
        map = new HashMap();
        map.put(SUCCESS, success);
        map.put(DATA, data);
    }

    public JsonResponse1(boolean success, List data, int total) {
        this.initConfig();
        map = new HashMap();
        map.put(SUCCESS, success);
        map.put(DATA, data);
        map.put(TOTAL, total);
    }

    public JsonResponse1(boolean success, List data, int total, String fecha) {
        this.initConfig();
        map = new HashMap();
        map.put(SUCCESS, success);
        map.put(DATA, data);
        map.put(TOTAL, total);
        map.put(FECHA, fecha);
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

    @Override
    public String toString() {
        return this.serialize(this.map);
    }
}
