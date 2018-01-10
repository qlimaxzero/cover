package com.cover.util;


/**
 * Created by zhaoqing on 2017/10/16.
 */
public class TimeUtil {

    /**
     * 获取秒级时间戳
     *
     * @return
     */
    public static int getSecondTimestamp() {
        Long l = getTimestamp() / 1000l;
        return l.intValue();
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    public static long getTimestamp() {
        return System.currentTimeMillis();
    }

}
