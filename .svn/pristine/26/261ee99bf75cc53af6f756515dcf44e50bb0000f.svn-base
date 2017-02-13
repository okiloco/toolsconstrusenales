/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.tecnosystems.util;

import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author teo
 */
public class GenericJSONResponse extends JSONSerializer {

    public final static int SUCCESFUL_PROCESSED_STATUS = 10;
    public final static int GENERAL_BUSINESS_LOGIC_ERROR = 20;
    public final static int INVALID_SESSION_STATUS = 21;
    public final static int NOT_FOUND_RESOURCE_STATUS = 22;
    public final static int CONFLICT_STATUS = 23;
    public final static int INTERNAL_ERROR_STATUS = 24;

    private int status;
    private String msg;
    private boolean success;
    private List data;
    private Object object;
    private int total;

    private String src;

    HashMap<String, Object> map = new HashMap<String, Object>();

    public static final String MSG = "msg";
    public static final String STATUS = "status";
    public static final String SUCCESS = "success";
    public static final String OBJECT = "object";
    public static final String DATA = "data";
    public static final String TOTAL = "total";

    public static final String SRC = "src";

    public GenericJSONResponse(boolean b, int SUCCESFUL_PROCESSED_STATUS, List list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void initConfig() {
        this.exclude("*.class");
        this.include(DATA);
        this.include(OBJECT);
    }

    public GenericJSONResponse(boolean success, int status, String message) {
        this.initConfig();
        this.status = status;
        this.msg = message;
        this.success = success;
        map = new HashMap<String, Object>();
        map.put(STATUS, status);
        map.put(MSG, msg);
        map.put(SUCCESS, success);
    }

    public GenericJSONResponse(String src, boolean success, int status) {
        this.initConfig();
        this.status = status;
        this.success = success;
        this.src = src;
        map = new HashMap<String, Object>();
        map.put(STATUS, status);
        map.put(SUCCESS, success);
        map.put(SRC, src);
    }

    public GenericJSONResponse(boolean success, int status, String message, List data) {
        this.initConfig();
        this.status = status;
        this.msg = message;
        this.success = true;
        this.data = data;

        map = new HashMap<String, Object>();
        map.put(MSG, msg);
        map.put(STATUS, status);
        map.put(SUCCESS, success);
        map.put(DATA, data);
    }

    public GenericJSONResponse(boolean success, int status, String message, List data, int total) {
        this.initConfig();
        this.status = status;
        this.success = success;
        this.msg = message;
        this.total = total;
        this.data = data;

        map = new HashMap<String, Object>();
        map.put(STATUS, status);
        map.put(MSG, msg);
        map.put(SUCCESS, success);
        map.put(TOTAL, total);
        map.put(DATA, data);
    }

    public GenericJSONResponse(boolean success, int status, String message, Object object) {
        this.initConfig();
        this.success = success;
        this.status = status;
        this.msg = message;
        this.object = object;

        map = new HashMap<String, Object>();
        map.put(STATUS, status);
        map.put(MSG, msg);
        map.put(SUCCESS, success);
        map.put(OBJECT, object);
    }

    public GenericJSONResponse(boolean success, int status, String message, Object object, List data) {
        this.initConfig();
        this.success = success;
        this.status = status;
        this.msg = message;
        this.object = object;
        this.data = data;

        map = new HashMap<String, Object>();
        map.put(STATUS, status);
        map.put(MSG, msg);
        map.put(SUCCESS, success);
        map.put(OBJECT, object);
        map.put(DATA, data);
    }

    public GenericJSONResponse(boolean success, int status, String message, Object object, List data, int total) {
        this.initConfig();
        this.success = success;
        this.status = status;
        this.msg = message;
        this.object = object;
        this.data = data;
        this.total = total;

        map = new HashMap<String, Object>();
        map.put(STATUS, status);
        map.put(MSG, msg);
        map.put(SUCCESS, success);
        map.put(OBJECT, object);
        map.put(DATA, data);
        map.put(TOTAL, total);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.serialize(map);
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

}
