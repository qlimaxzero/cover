package com.cover.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoqing on 2017/11/10.
 */
public class Init implements InitializerReturnType {

    @Override
    public Map<Class, Object> getInstanceOfReturnTypes() {
        Map<Class, Object> map = new HashMap<>();
        //map.put(AA.class, new AA(-1, "error"));
        return map;
    }

}
