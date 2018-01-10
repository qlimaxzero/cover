package com.cover.model;

/**
 * Created by zhaoqing on 2017/11/10.
 */
public class ParamMap {

    private String name;
    private Object value;
    private boolean param;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isParam() {
        return param;
    }

    public void setParam(boolean param) {
        this.param = param;
    }
}
