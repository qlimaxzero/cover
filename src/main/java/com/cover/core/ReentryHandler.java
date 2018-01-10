package com.cover.core;


/**
 * Created by zhaoqing on 2017/11/6.
 */
public interface ReentryHandler {

    boolean isReentry(String key, int repeatTime);

}
